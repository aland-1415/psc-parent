package com.pqx.psc.util.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pqx.psc.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author quanxing.peng
 * @date 2021/2/26
 */
@Slf4j
public class FeishuMsgUtil {
    private final static String APP_ID = "cli_9f76a10e587dd00e";
    private final static String APP_SECRET = "bUzYMYw0jYY3DaXM6LVYGmKWOmWlqqum";

    private final static String HEADER_NAME_CONTENT_TYPE = "Content-Type";
    private final static String HEADER_NAME_AUTH = "Authorization";
    private final static String CONTENT_TYPE = "application/json";
    private final static int FEISHU_SUC_CODE = 0;

    private static Map<String, String> userEmail2OpenIdMap = new HashMap<String, String>();

    //获取飞书access_token
    private static String getCurrentAuthToken(){
        String pre = "Bearer ";
        String url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal/";

        Map header = new HashMap();
        header.put(HEADER_NAME_CONTENT_TYPE, CONTENT_TYPE);
        JSONObject params = new JSONObject();
        params.put("app_id", APP_ID);
        params.put("app_secret", APP_SECRET);
        String result = HttpClientUtil.doJsonPost(url, params, header);
        JSONObject reJson = JSONObject.parseObject(result);
        if (reJson.getInteger("code") != FEISHU_SUC_CODE){
            log.error("从飞书获取实时access_token失败！飞书返回值：{}", reJson);
            throw new RuntimeException("从飞书获取实时access_token失败！");
        }
        String accessToken = reJson.getString("tenant_access_token");
        return pre + accessToken;
    }

    //通过邮箱获取用户openId,也可以传手机号，参数为名为：&mobiles=13812345678&mobiles=+12126668888
    private static List<String> getUserOpenIdsByEmail(List<String> emails){
        String url = "https://open.feishu.cn/open-apis/user/v1/batch_get_id?";
        List<String> openIds = new ArrayList<String>();
        if (emails.size() == 0){
            return openIds;
        }
        // 1、先从本地缓存查
        Set<String> unknowEails = new HashSet<>();
        for(String email : emails){
            String openId = userEmail2OpenIdMap.get(email);
            if (openId == null){
                unknowEails.add(email);
            }else {
                openIds.add(openId);
            }
        }
        if (unknowEails.size() == 0){
            return openIds;
        }
        // 2、再去飞书查
        for (String email : unknowEails){
            url = url + "emails=" + email + "&";
        }
        Map<String, Object> httpHeader = new HashMap<>();
        httpHeader.put(HEADER_NAME_AUTH, getCurrentAuthToken());
        String httpRe = HttpClientUtil.doGet(url, httpHeader);
        JSONObject reJson = JSONObject.parseObject(httpRe);
        if (reJson.getInteger("code") == FEISHU_SUC_CODE){
            JSONObject exitUsers = reJson.getJSONObject("data").getJSONObject("email_users");
            JSONArray notExitUsers = reJson.getJSONObject("data").getJSONArray("emails_not_exist");
            if (exitUsers != null && !exitUsers.isEmpty()){
                exitUsers.keySet().forEach(k -> {
                    String openId = ((JSONObject)exitUsers.getJSONArray(k).get(0)).getString("open_id");
                    userEmail2OpenIdMap.put(k, openId);
                    openIds.add(openId);
                });
            }
            if (notExitUsers.size() > 0){
                log.warn("未从飞书里查询到以下用户信息：{}", notExitUsers);
            }
        }else {
            log.error("查询飞书用户openId失败，请求信息：{}\n 返回信息：{}", url, reJson);
        }
        return openIds;
    }

    public static void sendUserMsg(List<String> emails, String content){
        String url = "https://open.feishu.cn/open-apis/message/v4/send/";
        List<String> openIds = getUserOpenIdsByEmail(emails);
        if (openIds.size() == 0){
            throw new RuntimeException("传入的飞书用户emails全都无效");
        }

        Map header = new HashMap();
        header.put(HEADER_NAME_CONTENT_TYPE, CONTENT_TYPE);
        header.put(HEADER_NAME_AUTH, getCurrentAuthToken());
        JSONObject params = JSONObject.parseObject("{\"open_id\":\"\",\"msg_type\":\"text\",\"content\":{\"text\":\"content\"}}");
        for (String user : openIds){
            params.put("open_id", user);
            params.getJSONObject("content").put("text", content);
            HttpClientUtil.doJsonPost(url, params, header);
        }
    }
}
