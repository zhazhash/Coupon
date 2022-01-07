package com.sh.coupon.executor;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.vo.GoodsInfo;
import com.sh.coupon.vo.SettlementInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象类规则定义
 */
public abstract class AbstractExecutor {

    /**
     * 校验商品类型与优惠卷是否匹配
     *  需要注意：
     *  1。这里实现的但品类优惠卷校验，多品类优惠卷重载此方法
     *  2。商品只需要有一个优惠卷要求的商品类型去匹配就可以
     * @param settlementInfo
     * @return
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo){
        List<Integer> goodsType = settlementInfo.getGoodsInfos().stream()
                .map(GoodsInfo::getType).collect(Collectors.toList());

        List<Integer> templateGoodsType = JSON.parseObject(settlementInfo.getCouponAndTemplateInfos()
                .get(0).getCouponTemplate().getRule().getUsage()
                .getGoodsType(),List.class);
            //是否有交集 CollectionUtils.intersection
        return CollectionUtils.isNotEmpty(
                CollectionUtils.intersection(goodsType,templateGoodsType));
    }

    /**
     *  处理优惠卷类型与优惠卷不匹配情况
     * @param settlementInfo
     * @param goodSum
     * @return
     */
    protected  SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlementInfo,double goodSum){
        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlementInfo);
        //当商品类型不满足时直接返回总价，并清空优惠卷
        if (!isGoodsTypeSatisfy) {
            settlementInfo.setCost(goodSum);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
        }
        return null;
    }

    /**
     * 获取商品总价
     * @param goodsInfos
     * @return
     */
    protected double goodsCostSum(List<GoodsInfo> goodsInfos){
        return goodsInfos.stream().mapToDouble(g -> g.getPrice() * g.getCount()).sum();
    }

    /**
     * 保留两位小数
     * @param value
     * @return
     */
    protected double retain2Decimals(double value){
        return new BigDecimal(value).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 最小支付费用
     * @return
     */
    protected double minCost(){
        return  0.1;
    }

}
