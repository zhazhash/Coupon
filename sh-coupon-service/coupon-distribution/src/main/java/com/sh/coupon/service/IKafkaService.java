package com.sh.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * kafka 相关接口定义
 */
public interface IKafkaService {
    /**
     * 消费 优惠卷kafka信息
     * @param record {@link ConsumerRecord}
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?,?> record);
}
