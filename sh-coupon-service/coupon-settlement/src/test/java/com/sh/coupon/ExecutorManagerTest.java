package com.sh.coupon;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.constant.CouponCategory;
import com.sh.coupon.constant.GoodsType;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.executor.ExecutorManger;
import com.sh.coupon.vo.CouponTemplateSDK;
import com.sh.coupon.vo.GoodsInfo;
import com.sh.coupon.vo.SettlementInfo;
import com.sh.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
@Slf4j
@SpringBootTest(classes = SettlementApplication.class)
@RunWith(SpringRunner.class)
public class ExecutorManagerTest {

    @Autowired
    private ExecutorManger executorManger;
    @Test
    public void couputeRule() throws CouponException {
        SettlementInfo settlementInfo = fakeSettlementInfo();
        SettlementInfo result = executorManger.couputeRule(settlementInfo);
        log.info("{}",result.getCost());
        log.info("{}",result.getCouponAndTemplateInfos().size());
        log.info("{}",result.getCouponAndTemplateInfos());

    }

    private SettlementInfo fakeSettlementInfo() {
        SettlementInfo settlementInfo = new SettlementInfo();
        settlementInfo.setEmploy(false);
        settlementInfo.setGoodsInfos(Arrays.asList(
                new GoodsInfo(GoodsType.ALL.getCode(),20.15,1),
                new GoodsInfo(GoodsType.OTHERS.getCode(),300.23,2)));
//        settlementInfo.setUserId();
        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);
        CouponTemplateSDK couponTemplateSDK = new CouponTemplateSDK();
        couponTemplateSDK.setId(1);
        couponTemplateSDK.setCategory(CouponCategory.MANJIAN.getCode());
        couponTemplateSDK.setKey("1212212");
        TemplateRule templateRule = new TemplateRule();
        templateRule.setDiscount(new TemplateRule.Discount(20,199));
        templateRule.setUsage(new TemplateRule.Usage("北京","北京", JSON.toJSONString(
                Arrays.asList(GoodsType.JIAJU.getCode(),
                        GoodsType.OTHERS.getCode()))));
        couponTemplateSDK.setRule(templateRule);
        ctInfo.setCouponTemplate(couponTemplateSDK);
        settlementInfo.setCouponAndTemplateInfos(Arrays.asList(ctInfo));
        return settlementInfo;
    }
}
