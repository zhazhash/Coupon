package com.sh.coupon;

import com.sh.coupon.permission.PermissionClient;
import com.sh.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * 权限探测监听器
 * ApplicationReadyEvent 表示监听applcation 启动后
 * @author admin
 */
@Slf4j
@Component
public class PermissionDetectListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final String KEY_SERVER_CTX = "server.servlet.context-path";
    private static final String KEY_SERVER_NAME = "spring.application.name";
    @SuppressWarnings("all")
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        new Thread(()->{
            List<PermissionInfo> infos = scanPermission(ctx);
            registerPermission(infos,ctx);
        }).start();

    }

    private void registerPermission(List<PermissionInfo> infos,ApplicationContext ctx){
        PermissionClient permissionClient = ctx.getBean(PermissionClient.class);
        if (null == permissionClient){
            log.error("no permissionClient bean found");
        }
        String name = ctx.getEnvironment().getProperty(KEY_SERVER_NAME);
        PermissionRegistry registry = new PermissionRegistry(permissionClient,name);
        boolean result = registry.register(infos);
        if (result){
            log.info("register down");
        }
    }

    private List<PermissionInfo> scanPermission(ApplicationContext ctx){
        String pathPerfix = ctx.getEnvironment().getProperty(KEY_SERVER_CTX);
        RequestMappingHandlerMapping mappingBean = (RequestMappingHandlerMapping) ctx.getBean("requestMappingHandlerMapping");
        //扫描权限
        List<PermissionInfo> permissionInfos = new AnnotaionScanner(pathPerfix).scanPermission(mappingBean.getHandlerMethods());
        permissionInfos.forEach(p -> log.info("{}" ,p));
        log.info("{} permission found",permissionInfos.size());
        log.info("********* permission scan ********");
        return permissionInfos;
    }
}
