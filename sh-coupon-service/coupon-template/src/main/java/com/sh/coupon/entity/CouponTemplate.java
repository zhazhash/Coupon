package com.sh.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sh.coupon.constant.CouponCategory;
import com.sh.coupon.constant.DistributeTarget;
import com.sh.coupon.constant.ProductLine;
import com.sh.coupon.converter.CouponCategoryConverter;
import com.sh.coupon.converter.DistributeTargetConverter;
import com.sh.coupon.converter.ProductLineConverter;
import com.sh.coupon.converter.TemplateRuleConverter;
import com.sh.coupon.serialization.CouponTemplateSerialize;
import com.sh.coupon.vo.TemplateRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 优惠卷模板类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon_template")
@JsonSerialize(using = CouponTemplateSerialize.class)
public class CouponTemplate implements Serializable {

    /* 主键， 自增，不允许为空*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,nullable = false)
    private Integer id;

    /*是否可用状态*/
    @Column(name = "available",nullable = false)
    private Boolean available;
    /*是否过期*/
    @Column(name = "expired",nullable = false)
    private Boolean expired;
    /*优惠卷名称*/
    @Column(name = "name",nullable = false)
    private String name;

    /*优惠卷logo*/
    @Column(name = "logo", nullable = false)
    private String logo;

    /*优惠卷描述*/
    @Column(name = "intro",nullable = false)
    private String desc;
    /*优惠卷分类*/
    @Column(name = "category",nullable = false)
    @Convert(converter = CouponCategoryConverter.class)
    private CouponCategory category;
    /*产品线*/
    @Column(name = "product_line", nullable = false)
    @Convert(converter = ProductLineConverter.class)
    private ProductLine productLine;

    /*总数*/
    @Column(name = "coupon_count",nullable = false)
    private Integer count;
    /*创建时间*/
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    /*创建用户ID*/
    @Column(name = "user_id",nullable = false)
    private Long userId;
    /*优惠卷模板编码*/
    @Column(name = "template_key",nullable = false)
    private String key;

    /*目标用户*/
    @Column(name = "target",nullable = false)
    @Convert(converter = DistributeTargetConverter.class )
    private DistributeTarget target;

    /*优惠卷规则*/
    @Column(name = "rule",nullable = false)
    @Convert(converter = TemplateRuleConverter.class)
    private TemplateRule rule;


    public CouponTemplate (String name ,String logo,String desc ,String category,Integer productLine,Integer count
                            ,Long userId,Integer target,TemplateRule rule){
            this.available = false;
            this.expired =false;
            this.name = name;
            this.logo = logo;
            this.desc = desc;
            this.category = CouponCategory.of(category);
            this.productLine = ProductLine.of(productLine);
            this.count = count;
            this.userId = userId;

            //优惠卷唯一编码，4（产品线和类型）+ 8（日期 ）+ ID（扩充为4位）
            this.key =productLine.toString() + category +new SimpleDateFormat("yyyyMMdd").format(new Date());
            this.target = DistributeTarget.of(target);
            this.rule = rule;
    }

}
