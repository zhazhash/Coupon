package com.sh.coupon.service.impl;

import com.sh.coupon.dao.CouponTemplateDao;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.CouponTemplate;
import com.sh.coupon.service.IBuildTemplateService;
import com.sh.coupon.service.ISyncService;
import com.sh.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 优惠卷模板实现类
 */
@Slf4j
@Service
public class BuildTemplateSericeImpl implements IBuildTemplateService {
    private final CouponTemplateDao templateDao;
    private final ISyncService syncService;

    @Autowired
    public BuildTemplateSericeImpl(CouponTemplateDao templateDao, ISyncService syncService) {
        this.templateDao = templateDao;
        this.syncService = syncService;
    }

    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws CouponException {

        if (!request.validate()){
            throw new CouponException("BuildTemplate Param Is Not Valid !");
        }
        if(null != templateDao.findByName(request.getName())){
            throw new CouponException("Exist Same Name Template!");
        }
        CouponTemplate template = templateDao.save(request2Template(request));
        syncService.syncConstructCouponByTemplate(template);
        return template;
    }
    private CouponTemplate request2Template(TemplateRequest request){
        return new CouponTemplate(request.getName(),request.getLogo(),request.getDesc(),request.getCategory(),request.getProductLine()
        ,request.getCount(),request.getUserId(),request.getTarget(),request.getRule());
    }
}
