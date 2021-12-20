package com.sh.coupon;

import com.netflix.discovery.shared.Application;
import com.sh.coupon.TemplateApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TemplateApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TemplateTests {

    @Test
    public void contextLoads(){

    }
}
