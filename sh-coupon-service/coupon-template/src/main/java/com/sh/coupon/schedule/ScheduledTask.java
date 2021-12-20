package com.sh.coupon.schedule;

import com.google.common.collect.Lists;
import com.sh.coupon.dao.CouponTemplateDao;
import com.sh.coupon.entity.CouponTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务类
 *
 */
@Slf4j
@Component
public class ScheduledTask {
    private final CouponTemplateDao templateDao;
    @Autowired
    public ScheduledTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void offlineCouponTemplate(){
        log.info("Start To Expire Coupontemplate");
        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        if(CollectionUtils.isEmpty(templates)){
            log.info("Done To Expire CouponTemplate");
            return;
        }
        List<CouponTemplate> expiredTemplates = Lists.newArrayListWithCapacity(templates.size());
        Date cur = new Date();
        templates.forEach(t ->{
            if (t.getRule().getExpiration().getDeadline() < cur.getTime()){
                t.setExpired(true);
                expiredTemplates.add(t);
            }
        });
        log.info("Expired CouponTemplate Num: {}",templateDao.saveAll(expiredTemplates));

        log.info("Done To Expire CouponTemplate");
    }
}
