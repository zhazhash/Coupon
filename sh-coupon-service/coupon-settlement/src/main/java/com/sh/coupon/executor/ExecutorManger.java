package com.sh.coupon.executor;

import com.sh.coupon.constant.CouponCategory;
import com.sh.coupon.constant.RuleFlag;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠卷执行规则管理器
 * 即根据用户的请求找到对应的Executor ，去做结算
 * BeanPostProcessor ： bean 后置处理器
 *
 */
@Slf4j
@Component
public class ExecutorManger implements BeanPostProcessor {

    private Map<RuleFlag,RuleExecutor> executorIndex = new HashMap<>();

    /**
     * 优惠卷规则计算入口 ，需保证传递的优惠卷个数大于等于1
     * @param settlementInfo
     * @return
     * @throws CouponException
     */
    public SettlementInfo couputeRule (SettlementInfo settlementInfo) throws CouponException{
        SettlementInfo result = null;
        if(settlementInfo.getCouponAndTemplateInfos().size() == 1){
            CouponCategory category = CouponCategory.of(settlementInfo.getCouponAndTemplateInfos().get(0)
                    .getCouponTemplate().getCategory());
            switch (category){
                case MANJIAN:
                    return executorIndex.get(RuleFlag.MANJIAN).couputeRule(settlementInfo);
                case ZHEKOU:
                    return executorIndex.get(RuleFlag.ZHEKOU).couputeRule(settlementInfo);
                case LIJIAN:
                    return executorIndex.get(RuleFlag.LIJIAN).couputeRule(settlementInfo);
            }
        }
        if(settlementInfo.getCouponAndTemplateInfos().size() == 2){
            List<CouponCategory> categories = new ArrayList<>();
            settlementInfo.getCouponAndTemplateInfos().forEach(ct ->
                    categories.add(CouponCategory.of(ct.getCouponTemplate().getCategory())));
            if (categories.contains(CouponCategory.MANJIAN) && categories.contains(CouponCategory.ZHEKOU)){
               return executorIndex.get(RuleFlag.MANJIAN_ZHEKOU).couputeRule(settlementInfo);
            }
        }
        throw new CouponException("Not Support For More Template Category");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(!(bean instanceof RuleExecutor)){
            return bean;
        }
        RuleExecutor ruleExecutor = (RuleExecutor)bean;
        if (executorIndex.containsKey(ruleExecutor.ruleConfig())){
            throw new IllegalStateException("There is alreadly an Executor for rule" +
                    "flag" + ruleExecutor.ruleConfig().toString());
        }
        log.debug("Load executor {} for rule flag {}" ,ruleExecutor.getClass(),ruleExecutor.ruleConfig());
        executorIndex.put(ruleExecutor.ruleConfig(),ruleExecutor);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
