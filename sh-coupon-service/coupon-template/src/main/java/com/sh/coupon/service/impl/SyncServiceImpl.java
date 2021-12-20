package com.sh.coupon.service.impl;

import com.google.common.base.Stopwatch;
import com.sh.coupon.constant.Constant;
import com.sh.coupon.dao.CouponTemplateDao;
import com.sh.coupon.entity.CouponTemplate;
import com.sh.coupon.service.ISyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 异步服务接口实现
 */
@Slf4j
@Service
public class SyncServiceImpl implements ISyncService {

    private final CouponTemplateDao couponTemplateDao;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public SyncServiceImpl(CouponTemplateDao couponTemplateDao, StringRedisTemplate redisTemplate) {
        this.couponTemplateDao = couponTemplateDao;
        this.redisTemplate = redisTemplate;
    }

    @Async("getAsyncExecutor")
    @Override
    public void syncConstructCouponByTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> couponCodes  = buildCouponCode(template);
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE ,template.getId());
        log.info("Push CouponCode To Redis : {}" ,redisTemplate.opsForList().rightPushAll(redisKey,couponCodes));
        template.setAvailable(true);
        couponTemplateDao.save(template);
        watch.stop();
        log.info("Construct CouponCode By Template Cost : ms",watch.elapsed(TimeUnit.MILLISECONDS));

        //TODO 通知响应的人成功， 邮件，短信等
        log.info("CouponTemplate({}) is Available !" ,template.getId());
    }

    /**
     * 构造优惠卷码
     *  优惠卷码由18位组成
     *  前四位：产品线 + 类型
     *  中间六位：优惠卷模板创建日期随机（时间为：210101）
     *  后八位：0～9随机数构成
     * @param template
     * @return
     */
    private Set<String> buildCouponCode (CouponTemplate template){
        Stopwatch stopwatch = Stopwatch.createStarted();//记录执行时间
        Set<String> result = new HashSet<>(template.getCount());

        String prefix4 = template.getProductLine().getCode() + template.getCategory().getCode();

        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());
        for (int i = 0 ; i != template.getCount() ; i++){
            result.add(buildCouponCodeSuffix14(date));
        }
        while (result.size() < template.getCount()){
            result.add(buildCouponCodeSuffix14(date));
        }
        stopwatch.stop();
        log.info("Build Coupon Code Const: {} ms",stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    /**
     * 构造后十四位随机数
     * @param date 日期格式
     * @return 14位随机数
     */
    private String buildCouponCodeSuffix14(String date) {
        char [] bases = new char[]{'1','2','3','4','5','6','7','8','9'};
        //将日期转换为object类型后强转为char 生成集合
        List<Character> chars = date.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        //随机组合（洗牌算法）
        Collections.shuffle(chars);
        //将char类型集合转换为string并组成一个字符串
        String mid6 = chars.stream().map(Object::toString).collect(Collectors.joining());
        //随机时不允许第一位数为0，
        String suffix8 = RandomStringUtils.random(1,bases) + RandomStringUtils.randomNumeric(7);
        return mid6 + suffix8;
    }
}
