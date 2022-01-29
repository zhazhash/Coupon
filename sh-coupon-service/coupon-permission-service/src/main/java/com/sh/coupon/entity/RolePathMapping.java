package com.sh.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * role与path的映射关系实体类
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon_role_path_mapping")
public class RolePathMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    /**
     * role表 主键
     */
    @Column(name = "role_id",nullable = false)
    private Integer roleId;
    /**
     * path表 主键
     */
    @Column(name = "path_id",nullable = false)
    private Integer pathId;

}
