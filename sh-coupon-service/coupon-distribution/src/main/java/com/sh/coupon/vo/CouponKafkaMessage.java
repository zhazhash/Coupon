package com.sh.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 优惠卷 kafka消息对象定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponKafkaMessage {
    /* 优惠卷状态*/
    private Integer status;
    /*coupon 主键*/
    private List<Integer> ids;
}
