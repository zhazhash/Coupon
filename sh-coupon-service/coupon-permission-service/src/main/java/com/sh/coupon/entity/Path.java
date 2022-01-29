package com.sh.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author admin
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="coupon_path")
public class Path {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

    /**
     * 路径模式
     */
    @Column(name = "path_pattern" ,nullable = false)
    private String pathPattern;

    /**
     * http 方法类型
     */
    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    /**
     * 路径名称
     */
    @Column(name = "path_name",nullable = false)
    private String pathName;

    /**
     * 服务名称
     */
    @Column(name = "service_name",nullable = false)
    private String serviceName;

    /**
     * 操作模式， 读还是写
     */
    @Column(name = "op_mode",nullable = false)
    private String opMode;

    /**
     * 不带主键的构造器
     * @param pathPattern
     * @param httpMethod
     * @param pathName
     * @param serviceName
     * @param opMode
     */
    public Path(String pathPattern, String httpMethod, String pathName, String serviceName, String opMode) {
        this.pathPattern = pathPattern;
        this.httpMethod = httpMethod;
        this.pathName = pathName;
        this.serviceName = serviceName;
        this.opMode = opMode;
    }
}
