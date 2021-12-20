package com.sh.coupon.service.impl;

import com.sh.coupon.dao.CouponTemplateDao;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.CouponTemplate;
import com.sh.coupon.service.ITemplateBaseService;
import com.sh.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateBaseSeriveImpl implements ITemplateBaseService {

    private final CouponTemplateDao templateDao;

    @Autowired
    public TemplateBaseSeriveImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if(!template.isPresent()){
            throw new CouponException("Template Is Not Exist: " + id);
        }
        return template.get();
    }

    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        List<CouponTemplate> templates = templateDao.findAllByAvailableAndExpired(true,false);

        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
//  Function.identity() 代表自己
        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toMap(CouponTemplateSDK::getId, Function.identity()));
    }

    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template){

        return new CouponTemplateSDK(template.getId(),template.getName(),template.getLogo(),template.getDesc(),
                template.getCategory().getCode(),template.getProductLine().getCode(),template.getKey(), template.getTarget().getCode(),template.getRule());
    }

}
