package com.sh.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateSDK {
    private Integer id;
    /*优惠卷名称*/
    private String name;
    /*优惠卷 logo*/
    private String logo;
    /*优惠卷描述信息*/
    private String desc;
    /*优惠卷分类*/
    private String category;
    /*产品线*/
    private Integer productLine;
    /*总数*/
    private String key;
    /*目标用户*/
    private Integer target;
    /*优惠卷规则*/
    private TemplateRule rule;
}
