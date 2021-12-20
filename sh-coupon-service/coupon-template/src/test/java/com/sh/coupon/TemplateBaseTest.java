package com.sh.coupon;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.service.ITemplateBaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApplication.class)
public class TemplateBaseTest {
    @Autowired
    private ITemplateBaseService iTemplateBaseService;

    @Test
    public void testBuildTemplateInfo() throws CouponException{
        System.out.println(iTemplateBaseService.buildTemplateInfo(10));
        System.out.println("iTemplateBaseService.buildTemplateInfo(1) = " + iTemplateBaseService.buildTemplateInfo(1));
    }

    @Test
    public void testFindAllUsableTemplate() throws CouponException{
        System.out.println(iTemplateBaseService.findAllUsableTemplate());
    }
    @Test
    public void testFindIds2TemplateSDK() throws CouponException{
        System.out.println("iTemplateBaseService.findIds2TemplateSDK(new Integer()) = "
                + iTemplateBaseService.findIds2TemplateSDK(Arrays.asList(1,2,3,10)));

    }

}
