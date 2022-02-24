package com.sh.coupon;

import com.google.common.collect.Lists;
import com.sh.coupon.annotaion.IgnorePerrmission;
import com.sh.coupon.annotaion.ShCouponPermission;
import com.sh.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 接口权限扫描器
 * @author admin
 */
@Slf4j
public class AnnotaionScanner {

    private String pathPrefix = "";
    private String PACKAGE= "com.sh.coupon";

    AnnotaionScanner(String  pathPrefix){
        this.pathPrefix = trimPath(pathPrefix);
    }

    /**
     * 构造 所有controller 的权限信息
     * @param mappingMap
     * @return
     */
    List<PermissionInfo> scanPermission(Map<RequestMappingInfo,HandlerMethod> mappingMap){
        List<PermissionInfo> result = Lists.newArrayList();
        mappingMap.forEach((mapInfo,method) ->{
            result.addAll(buildPermission(mapInfo,method));
        });
        return result;
    }

    /**
     * 构造 Controller 的权限信息
     * @param mapInfo {@link RequestMappingInfo} @RequestMapping 对应的信息
     * @param handlerMethod {@link HandlerMethod}@RequestMapping 对应的方法的详情，包括方法、类、参数、注解
     * @return
     */
    private List<PermissionInfo> buildPermission(
            RequestMappingInfo mapInfo, HandlerMethod handlerMethod
            ){
        //方法相关的信息
        Method javaMethod = handlerMethod.getMethod();
        // 方法属于哪个类
        Class baseClass = javaMethod.getDeclaringClass();
        if(!isCouponPackage(baseClass.getName())){
            log.info("方法不存在于需要扫描的包中" + javaMethod.getName());
            return Collections.emptyList();
        }
        IgnorePerrmission ignorePerrmission = javaMethod.getAnnotation(IgnorePerrmission.class);
        if (null != ignorePerrmission){
            log.info("该方法不需要权限" + javaMethod.getName());
            return Collections.emptyList();
        }
        ShCouponPermission couponPermission = javaMethod.getAnnotation(ShCouponPermission.class);
        if(null == couponPermission){
            log.error("请确认该方法是否需要扫描，如需扫描请配置ShCouponPermission注解，如不需要则配置IgnorePerrmission注解。");
            return Collections.emptyList();
        }
        //取出 RequestMapping配置的url
        Set<String> urls = mapInfo.getPatternsCondition().getPatterns();
        //取出 requestMapping配置的所有http请求类型
        Set<RequestMethod> methods = mapInfo.getMethodsCondition().getMethods();
        //是否是所有http请求类型都可访问
        boolean allReqMethod = false;
        if (CollectionUtils.isEmpty(methods)){
            allReqMethod = true;
        }
        List<PermissionInfo> infos = new ArrayList<>();

        for (String url : urls) {
            if (allReqMethod){
                PermissionInfo info = buildPermissionInfo(HttpMethodEnum.ALl.name(),javaMethod.getName(),
                        this.pathPrefix + url,couponPermission.readOnly(),couponPermission.description(),couponPermission.extra());
                infos.add(info);
                log.info(info.toString());
                continue;
            }
            for (RequestMethod method : methods) {
                PermissionInfo info = buildPermissionInfo(method.name(),javaMethod.getName(),
                this.pathPrefix + url , couponPermission.readOnly(),couponPermission.description(),couponPermission.extra());
                infos.add(info);
                log.info(info.toString());
            }
        }
        return infos;
    }

    /**
     * 生成单个接口权限信息
     */
    private PermissionInfo buildPermissionInfo(String reqMethod,
        String javaMethod,String path, Boolean readOnly ,String desc,String extra){
            PermissionInfo info = new PermissionInfo();
            info.setMethod(reqMethod);
            info.setIsRead(readOnly);
            info.setExtra(extra);
            info.setUrl(path);
            info.setDescription(StringUtils.isNotBlank(desc) ? desc : javaMethod);
        return  info;
    }

    /**
     * 将path格式化，使其 以/开都不以/结尾
     * @param path
     * @return
     */
    private String trimPath(String path){
        if(StringUtils.isBlank(path)){
            return "";
        }
        if (!path.startsWith("/")){
            path = "/" + path;
        }
        if (path.endsWith("/")){
            path = path.substring(0,path.length() - 1);
        }
        return path;
    }

    /**
     * 判断当前类是否在需要扫描的包中
     * @param className
     * @return
     */
    private boolean isCouponPackage(String className){
        return className.startsWith(PACKAGE);
    }


}
