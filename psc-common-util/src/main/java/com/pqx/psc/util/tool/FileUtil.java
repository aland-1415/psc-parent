package com.pqx.psc.util.tool;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author quanxing.peng
 * @date 2020/11/12
 */
public class FileUtil {


    public static List<File> getAllFiles(String path){
        return  getAllFiles(path, "");
    }

    /**
     * @param depth 如果是文件夹，传0的结果为空，传1才是文件夹下一级的文件
     */
    public static List<File> getAllFiles(String path, int depth){
        return  getAllFiles(path, "", depth);
    }

    /**
     * @param pattern 正则表达式，为空则返回所有
     */
    public static List<File> getAllFiles(String path, String pattern){
        return  getAllFiles(path, pattern, 9999);
    }

    /**
     * @param pattern 正则表达式，为空则返回所有
     * @param depth  如果是文件夹，传0的结果为空，传1才是文件夹下一级的文件
     */
    public static List<File> getAllFiles(String path, String pattern, int depth){
        File originfile = new File(path);
        List<File> files = new ArrayList<File>();
        if (originfile.isDirectory()) {
            if (depth > 0){
                for (File file : originfile.listFiles()) {
                    if (file.isDirectory()) {
                        files.addAll(getAllFiles(file.getPath(), pattern, depth - 1));
                    } else {
                        files.add(file);
                    }
                }
            }
        } else {
            files.add(originfile);
        }
        //筛选
        List<File> suiteFiles = new ArrayList<File>();
        if (!StringUtils.isBlank(pattern)){
            for (File file : files){
                Matcher matcher = Pattern.compile(pattern).matcher(file.getAbsolutePath());
                if (matcher.find()){
                    suiteFiles.add(file);
                }
            }
        }else {
            suiteFiles = files;
        }
        return suiteFiles;
    }

    /**
     * 删除文件及文件夹内的所有内容
     */
    public static boolean delFiles(String path){
        boolean result = false;

        File file = new File(path);
        if (!file.exists())
            return true;

        //目录
        if(file.isDirectory()){
            File[] childrenFiles = file.listFiles();
            for (File childFile:childrenFiles){
                result = delFiles(childFile.getPath());
                if(!result){
                    return result;
                }
            }
        }
        //删除 文件、空目录
//        result = file.delete();
        try {
			Files.delete(file.toPath());
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

        return result;
    }
    
    
}
