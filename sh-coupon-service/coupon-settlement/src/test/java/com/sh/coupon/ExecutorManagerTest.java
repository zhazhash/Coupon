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
import java.util.Collections;

@Slf4j
@SpringBootTest(classes = SettlementApplication.class)
@RunWith(SpringRunner.class)
public class ExecutorManagerTest {

    @Autowired
    private ExecutorManger executorManger;
    @Test
    public void couputeRule() throws CouponException {
        //满减
        SettlementInfo settlementInfo = fakeManJianSettlementInfo();
        SettlementInfo result = executorManger.couputeRule(settlementInfo);
        log.info("{}",result.getCost());
        log.info("{}",result.getCouponAndTemplateInfos().size());
        log.info("{}",result.getCouponAndTemplateInfos());
        //折扣
        SettlementInfo settlementInfo1 = fakeZheKouSettlementInfo();
        SettlementInfo result1 = executorManger.couputeRule(settlementInfo1);
        log.info("{}",result1.getCost());
        log.info("{}",result1.getCouponAndTemplateInfos().size());
        log.info("{}",result1.getCouponAndTemplateInfos());
        //立减
        SettlementInfo settlementInfo2 = fakeLiJianSettlementInfo();
        SettlementInfo result2 = executorManger.couputeRule(settlementInfo2);
        log.info("{}",result2.getCost());
        log.info("{}",result2.getCouponAndTemplateInfos().size());
        log.info("{}",result2.getCouponAndTemplateInfos());
        //满减 + 折扣
        SettlementInfo settlementInfo3 = fakeManJianAndZheKouSettlementInfo();
        SettlementInfo result3 = executorManger.couputeRule(settlementInfo3);
        log.info("{}",result3.getCost());
        log.info("{}",result3.getCouponAndTemplateInfos().size());
        log.info("{}",result3.getCouponAndTemplateInfos());

    }

    private SettlementInfo fakeManJianAndZheKouSettlementInfo() {
        SettlementInfo settlementInfo = new SettlementInfo();
        settlementInfo.setEmploy(false);
        settlementInfo.setGoodsInfos(Arrays.asList(
                new GoodsInfo(GoodsType.OTHERS.getCode(),20.15,1),
                new GoodsInfo(GoodsType.OTHERS.getCode(),300.23,2)));
//        settlementInfo.setUserId();
        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);
        CouponTemplateSDK couponTemplateSDK = new CouponTemplateSDK();
        couponTemplateSDK.setId(1001);
        couponTemplateSDK.setCategory(CouponCategory.MANJIAN.getCode());
        couponTemplateSDK.setKey("1212212");
        TemplateRule templateRule = new TemplateRule();
        templateRule.setDiscount(new TemplateRule.Discount(20,199));
        templateRule.setUsage(new TemplateRule.Usage("北京","北京", JSON.toJSONString(
                Arrays.asList(GoodsType.JIAJU.getCode(),
                        GoodsType.OTHERS.getCode()))));
        templateRule.setWeight(JSON.toJSONString(Collections.singletonList("11qaasas1002")));
        couponTemplateSDK.setRule(templateRule);
        ctInfo.setCouponTemplate(couponTemplateSDK);

        SettlementInfo.CouponAndTemplateInfo ctInfo1 = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo1.setId(1);
        CouponTemplateSDK couponTemplateSDK1 = new CouponTemplateSDK();
        couponTemplateSDK1.setId(1002);
        couponTemplateSDK1.setCategory(CouponCategory.ZHEKOU.getCode());
        couponTemplateSDK1.setKey("11qaasas");
        TemplateRule templateRule1 = new TemplateRule();
        templateRule1.setDiscount(new TemplateRule.Discount(20,0));
        templateRule1.setUsage(new TemplateRule.Usage("北京","北京", JSON.toJSONString(
                Arrays.asList(GoodsType.JIAJU.getCode(),
                        GoodsType.OTHERS.getCode()))));
        templateRule1.setWeight(JSON.toJSONString(Collections.singletonList("12122121001")));
        couponTemplateSDK1.setRule(templateRule1);
        ctInfo1.setCouponTemplate(couponTemplateSDK1);


        settlementInfo.setCouponAndTemplateInfos(Arrays.asList(ctInfo,ctInfo1));
        return settlementInfo;
    }

    private SettlementInfo fakeLiJianSettlementInfo() {
        {
            SettlementInfo settlementInfo = new SettlementInfo();
            settlementInfo.setEmploy(false);
            settlementInfo.setGoodsInfos(Arrays.asList(
                    new GoodsInfo(GoodsType.ALL.getCode(),20.15,1),
                    new GoodsInfo(GoodsType.OTHERS.getCode(),79.85,1)));
//        settlementInfo.setUserId();
            SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
            ctInfo.setId(1);
            CouponTemplateSDK couponTemplateSDK = new CouponTemplateSDK();
            couponTemplateSDK.setId(1);
            couponTemplateSDK.setCategory(CouponCategory.LIJIAN.getCode());
            couponTemplateSDK.setKey("11qaasas");
            TemplateRule templateRule = new TemplateRule();
            templateRule.setDiscount(new TemplateRule.Discount(20,0));
            templateRule.setUsage(new TemplateRule.Usage("北京","北京", JSON.toJSONString(
                    Arrays.asList(GoodsType.JIAJU.getCode(),
                            GoodsType.OTHERS.getCode()))));
            couponTemplateSDK.setRule(templateRule);
            ctInfo.setCouponTemplate(couponTemplateSDK);
            settlementInfo.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));
            return settlementInfo;
        }
    }

    private SettlementInfo fakeZheKouSettlementInfo() {{
        SettlementInfo settlementInfo = new SettlementInfo();
        settlementInfo.setEmploy(false);
        settlementInfo.setGoodsInfos(Arrays.asList(
                new GoodsInfo(GoodsType.ALL.getCode(),20.15,1),
                new GoodsInfo(GoodsType.OTHERS.getCode(),79.85,1)));
//        settlementInfo.setUserId();
        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);
        CouponTemplateSDK couponTemplateSDK = new CouponTemplateSDK();
        couponTemplateSDK.setId(1);
        couponTemplateSDK.setCategory(CouponCategory.ZHEKOU.getCode());
        couponTemplateSDK.setKey("11qaasas");
        TemplateRule templateRule = new TemplateRule();
        templateRule.setDiscount(new TemplateRule.Discount(20,0));
        templateRule.setUsage(new TemplateRule.Usage("北京","北京", JSON.toJSONString(
                Arrays.asList(GoodsType.JIAJU.getCode(),
                        GoodsType.OTHERS.getCode()))));
        couponTemplateSDK.setRule(templateRule);
        ctInfo.setCouponTemplate(couponTemplateSDK);
        settlementInfo.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));
        return settlementInfo;
    }
    }

    private SettlementInfo fakeManJianSettlementInfo() {
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
