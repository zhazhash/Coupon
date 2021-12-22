package com.sh.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sh.coupon.constant.Constant;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.Coupon;
import com.sh.coupon.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisServicImpl implements IRedisService {
    /*Redis 客户端*/
    private final StringRedisTemplate redisTemplate;

    public RedisServicImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Coupon> getCacheCoupons(Long userId, Integer status) {
        log.info("Get CouponS From Cache:{},{}",userId,status);
        String redisKey = status2RedisKey(status,userId);
        List<String> couponStr = redisTemplate.opsForHash().values(redisKey)
                .stream().map( o -> Objects.toString(o,null))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponStr)){
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponStr.stream().map(s -> JSON.parseObject(s,Coupon.class)).collect(Collectors.toList());
    }

    @Override
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List To Cache For User:{}, Status{}",userId,JSON.toJSONString(status));
        Map<String,String> map = Maps.newHashMap();
        map.put("-1", JSON.toJSONString(Coupon.invalidCoupon()));

        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                status.forEach(s -> {
                    String redisKey = status2RedisKey(s,userId);
                    redisOperations.opsForHash().putAll(redisKey,map);
                });
                return null;
            }
        };
        //pipelined 支持将命令统一发送至redis并统一返回结果
    log.info("Pipelined Exe Result : {}", JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));

    }



    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {
        return null;
    }

    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        return null;
    }

    private String status2RedisKey(Integer code, Long userId) {
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(code);
        switch (couponStatus){
            case USED:
                redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USED,userId);
                break;
            case USABLE:
                redisKey = String.format("%s%s",Constant.RedisPrefix.USER_COUPON_USABLE,userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%d",Constant.RedisPrefix.USER_COUPON_EXPIRED,userId);
                break;
        }
        return redisKey;
    }
}
