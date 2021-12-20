package com.sh.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.converter.CouponStatusConverter;
import com.sh.coupon.serialization.CouponSerialize;
import com.sh.coupon.vo.CouponTemplateSDK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 优惠卷实体表
 */
@Data
@JsonSerialize(using = CouponSerialize.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
    @Column(name = "id",nullable = false)
    private Integer id;

    /**
     * 关联优惠卷模板的主键  逻辑外键
     */
    @Column(name = "template_id",nullable = false)
    private Integer templateId;
    /**
     * 领取用户
     */
    @Column(name = "user_id",nullable = false)
    private Long userId ;
    /**
     * 优惠卷码
     */
    @Column(name = "copon_code",nullable = false)
    private String couponCode;

    /**
     * 领取时间
     */
    @Column(name = "assign_time",nullable = false)
    @CreatedDate
    private Date assignTime;
    /**
     * 优惠卷状态
     */
    @Convert(converter = CouponStatusConverter.class)
    @Column(name = "status",nullable = false)
    private CouponStatus status;
    /**
     * 用户优惠卷对应的模板信息
     */
    @Transient
    private CouponTemplateSDK couponTemplateSDK;

    /**
     * 返回一个无效的Coupon对象
     * @return
     */
    public static Coupon invalidCoupon(){
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }

    public Coupon(Integer templateId, Long userId,String couponCode,CouponStatus status){
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }

}
