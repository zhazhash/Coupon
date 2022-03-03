package com.sh.coupon.controller;

import com.sh.coupon.annotaion.ShCouponPermission;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.Coupon;
import com.sh.coupon.service.IUserService;
import com.sh.coupon.vo.AcquireTemplateRequest;
import com.sh.coupon.vo.CouponTemplateSDK;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 */
@Slf4j
@RestController
public class UserServiceController {
    /*用户服务接口*/
    private final IUserService userService;

    public UserServiceController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户id和优惠卷状态查找用户优惠卷记录
     * @param userId 用户id
     * @param status 状态
     * @return
     * @throws CouponException
     */
    @GetMapping("/coupons")
    @ShCouponPermission(description = "findCouponsByStatus",readOnly = true)
    public List<Coupon> findCouponsByStatus(@RequestParam("userId") Long userId, @RequestParam("status") Integer status) throws CouponException {
        log.info("Find Coupons By Status :{},{}",userId,status);
       return userService.findCouponByStatus(userId,status);
    }
    @GetMapping("/template")
    public List<CouponTemplateSDK> findAvailableTemplate(@RequestParam("userId") Long userId)throws  CouponException{
        return userService.findAvailableTemplate(userId);
    }

    /**
     * 用户领取一个优惠卷
     * @param request
     * @return
     * @throws CouponException
     */
    @PostMapping("/acquire/template")
    @ShCouponPermission(description = "acquireTemplate",readOnly = false)
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request) throws CouponException{
        return userService.acquireTemplate(request);
    }
    @PostMapping("/sellement")
    @ShCouponPermission(description = "settlement",readOnly = false)
    public SettlementInfo settlement(@RequestBody SettlementInfo settlementInfo) throws CouponException{
        return userService.settlenment(settlementInfo);
    }

}
