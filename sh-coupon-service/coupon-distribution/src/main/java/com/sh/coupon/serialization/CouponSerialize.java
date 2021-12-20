package com.sh.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sh.coupon.entity.Coupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CouponSerialize extends JsonSerializer<Coupon> {
    @Override
    public void serialize(Coupon coupon, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id",coupon.getId().toString());
        jsonGenerator.writeStringField("status",coupon.getStatus().getCode().toString());
        jsonGenerator.writeStringField("templateId",coupon.getTemplateId().toString());
        jsonGenerator.writeStringField("userId",coupon.getUserId().toString());
        jsonGenerator.writeStringField("couponCode",coupon.getCouponCode());
        jsonGenerator.writeStringField("assignTime",new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(coupon.getAssignTime()));
        jsonGenerator.writeStringField("name",coupon.getCouponTemplateSDK().getName());
        jsonGenerator.writeStringField("logo",coupon.getCouponTemplateSDK().getLogo());
        jsonGenerator.writeStringField("desc",coupon.getCouponTemplateSDK().getDesc());
        jsonGenerator.writeStringField("expiration", JSON.toJSONString(coupon.getCouponTemplateSDK().getRule().getExpiration()));
        jsonGenerator.writeStringField("discount", JSON.toJSONString(coupon.getCouponTemplateSDK().getRule().getDiscount()));
        jsonGenerator.writeStringField("usage", JSON.toJSONString(coupon.getCouponTemplateSDK().getRule().getUsage()));

        jsonGenerator.writeEndObject();
    }
}
