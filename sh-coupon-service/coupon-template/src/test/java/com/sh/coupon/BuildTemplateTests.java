package com.sh.coupon;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.constant.CouponCategory;
import com.sh.coupon.constant.DistributeTarget;
import com.sh.coupon.constant.PeriodType;
import com.sh.coupon.constant.ProductLine;
import com.sh.coupon.service.IBuildTemplateService;
import com.sh.coupon.vo.TemplateRequest;
import com.sh.coupon.vo.TemplateRule;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApplication.class)
public class BuildTemplateTests {
    @Autowired
    private IBuildTemplateService iBuildTemplateService;
    @Test
    public void  buildTemplate() throws  Exception{
        System.err.println(JSON.toJSONString(iBuildTemplateService.buildTemplate(fakeTemplateRequest())));
        Thread.sleep(5000L);
    }

    private TemplateRequest fakeTemplateRequest(){

        TemplateRequest request = new TemplateRequest();
        request.setName("优惠卷模板-" + new Date().getTime());
        request.setLogo("SHTest");
        request.setDesc("优惠卷");
        request.setCategory(CouponCategory.MANJIAN.getCode());
        request.setProductLine(ProductLine.TIANMAO.getCode());
        request.setCount(10000);
        request.setUserId(10000L);
        request.setTarget(DistributeTarget.SINGLE.getCode());

        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(PeriodType.REGULR.getCode(),1, DateUtils.addDays(new Date(),60).getTime()));
        rule.setDiscount(new TemplateRule.Discount(5,1));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));
        rule.setUsage(new TemplateRule.Usage("北京","北京", JSON.toJSONString(Arrays.asList("文娱","家具"))));
        rule.setLimitation(1);
        request.setRule(rule);
        return request;
    }

}
