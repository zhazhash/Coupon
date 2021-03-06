package com.sh.coupon.serialization;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sh.coupon.entity.CouponTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 自定义优惠卷模板序列化类
 */
public class CouponTemplateSerialize extends JsonSerializer<CouponTemplate> {
    @Override
    public void serialize(CouponTemplate couponTemplate,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id",couponTemplate.getId().toString());
            jsonGenerator.writeStringField("name",couponTemplate.getName());
            jsonGenerator.writeStringField("logo",couponTemplate.getLogo());
            jsonGenerator.writeStringField("desc",couponTemplate.getDesc());
            jsonGenerator.writeStringField("category",couponTemplate.getCategory().getDescription());
            jsonGenerator.writeStringField("productLine",couponTemplate.getProductLine().getDescription());
            jsonGenerator.writeStringField("count",couponTemplate.getCount().toString());
            jsonGenerator.writeStringField("createTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(couponTemplate.getCreateTime()));
            jsonGenerator.writeStringField("userId",couponTemplate.getUserId().toString());
            jsonGenerator.writeStringField("key",couponTemplate.getKey() + String.format("%4d",couponTemplate.getId()));
            jsonGenerator.writeStringField("target",couponTemplate.getTarget().getDescription());
            jsonGenerator.writeStringField("rule", JSON.toJSONString(couponTemplate.getRule()));

            jsonGenerator.writeEndObject();
    }
}
