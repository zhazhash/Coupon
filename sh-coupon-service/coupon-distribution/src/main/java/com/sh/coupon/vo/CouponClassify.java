package com.sh.coupon.vo;

import com.google.common.collect.Lists;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.constant.PeriodType;
import com.sh.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;

/**
 *根据用户优惠卷的状态进行用户优惠卷分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {
    /*未使用的*/
    private List<Coupon> usable;
    /*已使用的*/
    private List<Coupon> used;
    /*已过期的*/
    private List<Coupon> expired;

    public static CouponClassify classify(List<Coupon> coupons){
        List<Coupon> uasble = Lists.newArrayList();
        List<Coupon> used = Lists.newArrayList();
        List<Coupon> expired = Lists.newArrayList();

        coupons.forEach(c -> {
            boolean isTimeExpired;
            long curTime = new Date().getTime();
            if(c.getCouponTemplateSDK().getRule().getExpiration().getDeadline().equals(
                    PeriodType.REGULR.getCode()
            )){
                isTimeExpired = c.getCouponTemplateSDK().getRule().getExpiration().getDeadline() <= curTime;
            }else {
                isTimeExpired = DateUtils.addDays(c.getAssignTime(),
                        c.getCouponTemplateSDK().getRule().getExpiration().getGap()).getTime() <= curTime;
            }
            if(c.getStatus() == CouponStatus.USED){
                used.add(c);
            }else if(c.getStatus() == CouponStatus.EXPIRED || isTimeExpired){
                expired.add(c);
            }else {
                uasble.add(c);
            }
        });

        return new CouponClassify(uasble,used,expired);
    }

}
