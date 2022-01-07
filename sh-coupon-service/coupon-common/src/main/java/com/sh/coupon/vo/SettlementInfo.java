package com.sh.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 结算信息对象定义
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {
    /*用户id*/
    private Long userId;
    /*商品列表*/
    private List<GoodsInfo> goodsInfos;
    /*优惠卷列表*/
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;
    /*结果结算金额*/
    private Double cost;
    /*是否使结算生效*/
    private Boolean employ;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class  CouponAndTemplateInfo{
        /* 优惠卷id */
        private Integer id;
        private CouponTemplateSDK couponTemplate;
    }

}
