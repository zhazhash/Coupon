package com.sh.coupon.vo;

import com.sh.coupon.constant.CouponCategory;
import com.sh.coupon.constant.DistributeTarget;
import com.sh.coupon.constant.ProductLine;
import com.sh.coupon.vo.TemplateRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {
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
    private Integer count;
    /*创建用户*/
    private Long userId;
    /*目标用户*/
    private Integer target;
    /*优惠卷规则*/
    private TemplateRule rule;

    public boolean validate(){
        boolean stringValid = StringUtils.isNoneBlank(name)
                && StringUtils.isNotBlank(logo) && StringUtils.isNotBlank(desc)
                && StringUtils.isNotBlank(category);
        boolean enumValid = null != ProductLine.of(productLine) && null != CouponCategory.of(category)
                && null != DistributeTarget.of(target);
        boolean numValid = userId > 0 && count > 0;

        return stringValid && enumValid && numValid && rule.validate();
    }

}
