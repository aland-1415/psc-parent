package com.pqx.psc.util;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

/**
 * @author quanxing.peng
 * @date 2020年2月29日
 */
public class DubboUtil {

	public static Object invokeDubbo(Class<?> clazz, Method method, Object[] args,String zookeeperAddr) {
		ApplicationConfig app = new ApplicationConfig();
    	app.setName("ESCAutoTest");
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(zookeeperAddr);
        
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setApplication(app);
        reference.setRegistry(registry);
        reference.setInterface(clazz);
        reference.setGeneric(true);
        reference.setTimeout(100000);
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);
        
        return genericService.$invoke(method.getName(), ReflectUtil.getParamTypeNames(method), args);
	}
	
}
