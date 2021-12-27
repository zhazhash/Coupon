package com.sh.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.constant.Constant;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.dao.CouponDao;
import com.sh.coupon.entity.Coupon;
import com.sh.coupon.service.IKafkaService;
import com.sh.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * fakfa相关操作
 * 将cache中的Coupon状态同步到db中
 */
@Slf4j
@Component
public class KafkaServiceImpl implements IKafkaService {
    private final CouponDao couponDao;

    public KafkaServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    @KafkaListener(topics = {Constant.TOPIC},groupId = "SH-Coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
       Optional<?> kafkaMessage = Optional.ofNullable(record);
       if(kafkaMessage.isPresent()){
        Object message = kafkaMessage.get();
           CouponKafkaMessage kafkaInfo = JSON.parseObject(message.toString(),CouponKafkaMessage.class);
           log.info("Receive CouponFakfaMessage :{}",message.toString());
           CouponStatus couponStatus = CouponStatus.of(kafkaInfo.getStatus());
           switch (couponStatus){
               case USED:
                   processUesdCoupons(kafkaInfo,couponStatus);
                   break;
               case USABLE:
                   break;
               case EXPIRED:
                   processExpiredCoupons(kafkaInfo,couponStatus);
                   break;
           }

       }

    }

    private void processExpiredCoupons(CouponKafkaMessage kafkaInfo, CouponStatus couponStatus) {
        //TODO 可发送提醒等
        proessCouponByStatus(kafkaInfo,couponStatus);
    }

    /**
     * 处理已使用到用户优惠卷
     * @param kafkaInfo
     * @param couponStatus
     */
    private void processUesdCoupons(CouponKafkaMessage kafkaInfo, CouponStatus couponStatus) {
        //TODO 后续使用优惠卷逻辑， 比如可给用户发短信

        proessCouponByStatus(kafkaInfo,couponStatus);
    }

    /**
     * 根据优惠卷状态信息处理
     * @param kafkaInfo
     * @param couponStatus
     */
    private void proessCouponByStatus(CouponKafkaMessage kafkaInfo, CouponStatus couponStatus) {
        List<Coupon> coupons = couponDao.findAllById(kafkaInfo.getIds());
        if(CollectionUtils.isEmpty(coupons) || coupons.size() != kafkaInfo.getIds().size()){
            log.error("Can Not Find Right Coupon Info :{}" , JSON.toJSONString(kafkaInfo));
            //TODO 可添加发送邮件等逻辑
            return;
        }
        coupons.forEach(c -> c.setStatus(couponStatus));
        log.info("CouponKafkaMessage Op Coupon Count :{}",couponDao.saveAll(coupons).size());
    }
}
