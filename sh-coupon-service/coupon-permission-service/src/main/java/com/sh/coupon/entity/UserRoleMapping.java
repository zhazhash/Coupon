package com.sh.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * user与role的映射关系实体类
 * @author SH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon_user_role_mapping")
public class UserRoleMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    /**
     * 用户id
     */
    @Column(name = "user_id",nullable = false)
    private  Integer userId;

    /**
     * role表主键
     */
    @Column(name = "role_id",nullable = false)
    private Integer roleId;
}
