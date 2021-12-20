package com.sh.coupon.dao;

import com.sh.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 接口定义
 * JpaRepository<CouponTemplate,Integer> 第一个为 实体类名称， 第二个为 实体类主键字段
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate,Integer> {

    /**
     * 根据模板名称来获取
     * @param name
     * @return
     */
    CouponTemplate findByName (String name);

    /**
     * 根据availble和expired查询
     * @param availble
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean availble,Boolean expired);


    List<CouponTemplate> findAllByExpired(Boolean expired);

}
