package com.sh.coupon.converter;


import com.alibaba.fastjson.JSON;
import com.sh.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TemplateRuleConverter implements AttributeConverter<TemplateRule,String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule rule) {
        return JSON.toJSONString(rule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String s) {
        return JSON.parseObject(s,TemplateRule.class);
    }
}
