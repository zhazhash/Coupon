import com.alibaba.fastjson.JSON;
import com.sh.coupon.DistributionApplication;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DistributionApplication.class)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    private Long fakeUserId = 20001L;

    @Autowired
    private IUserService userService;

    @Test
    public void testFindCouponByStatus() throws CouponException {
        System.err.println(JSON.toJSONString(userService.findCouponByStatus(fakeUserId, CouponStatus.USED.getCode())));
    }
    @Test
    public void testFindAvailabletemplate() throws CouponException{
        System.err.println(JSON.toJSONString(userService.findAvailableTemplate(fakeUserId)));
    }
}
