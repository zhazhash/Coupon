package com.sh.coupon.vo;

import lombok.Data;

/**
 * 接口权限信息组装类
 * @author admin
 */
@Data
public class PermissionInfo {
    /**Controller 的 url*/
    private String url;
    /**方法类型*/
    private String method;
    /**是否是只读的*/
    private Boolean isRead;
    /**方法描述信息*/
    private String description;
    /**扩展属性*/
    private String extra;


    @Override
    public String toString() {
        return "url = " + url +",method = " + method
                + ",isRead = " + isRead + ",description = " + description
                +",extra = " + extra;
    }
}
