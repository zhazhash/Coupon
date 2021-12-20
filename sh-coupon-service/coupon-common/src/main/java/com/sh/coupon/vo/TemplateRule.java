package com.sh.coupon.vo;

import com.sh.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 优惠卷规则对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {
    /*优惠卷过期规则*/
    private Expiration expiration;
    /*折扣*/
    private Discount discount;
    /*每个人最多可以领取几张的规则*/
    private Integer limitation;
    /*使用范围 ：地域 + 商品类型*/
    private Usage usage;
    /*权重 ：可以和哪些优惠卷叠加使用，同一类型的优惠卷不可以叠加使用*/
    private String weight;

    public boolean validate() {

        return this.expiration.validate() && this.discount.validate() && this.usage.validate()
                && StringUtils.isNotBlank(weight) && limitation > 0;
    }

    /**
     * 有限期限规则
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expiration{
        /* 有效期规则，对应PeriodType的code字段*/
        private Integer period;
        /*有效间隔：只对变动性有效期有效*/
        private Integer gap;
        /*优惠卷模板的失效日期*/
        private Long deadline;

        boolean validate(){

            return null != PeriodType.of(period) && gap > 0 && deadline >0 ;
        }
    }

    /**
     * 折扣，需要与类型配合决定
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Discount{
        /*额度*/
        private Integer quota;
        /*基准：需要满多少才可以使用折扣*/
        private Integer base;
        boolean validate(){
            return quota > 0 && base > 0;
        }
    }

    /**
     * 使用范围
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage{
        /*省份*/
        private String province;
        /*城市*/
        private String city;
        /*商品类型:list「文娱，生鲜，家具，全品类」*/
        private String goodsType;

        boolean validate(){

            return StringUtils.isNotBlank(goodsType);
        }

    }
}
