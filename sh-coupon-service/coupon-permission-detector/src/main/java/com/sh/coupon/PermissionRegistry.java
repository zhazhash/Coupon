package com.sh.coupon;

import com.sh.coupon.permission.PermissionClient;
import com.sh.coupon.vo.CommonResponse;
import com.sh.coupon.vo.CreatePathRequest;
import com.sh.coupon.vo.PermissionInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限注册组件
 * @author admin
 */
@Slf4j
@AllArgsConstructor
public class PermissionRegistry {
    /**
     * 权限服务sdk 客户端
     */
    private PermissionClient permissionClient;
    /**服务名称*/
    private String serviceName;

    /**
     * 权限注册
     * @param infoList
     * @return
     */
    boolean register(List<PermissionInfo> infoList){
        if (CollectionUtils.isEmpty(infoList)){
         log.info("接口信息为空");
         return false;
        }
        List<CreatePathRequest.PathInfo> pathInfos = infoList.stream().map(info -> CreatePathRequest.PathInfo.builder()
        .pathPattern(info.getUrl()).httpMethod(info.getMethod()).pathName(info.getDescription()).serviceName(serviceName)
        .opMode(info.getIsRead() ? OpModeEnum.READ.name() : OpModeEnum.WIRTE.name()).build()).collect(Collectors.toList());

        CommonResponse<List<Integer>> response = permissionClient.createPath(new CreatePathRequest(pathInfos));

        if (CollectionUtils.isNotEmpty(response.getData())){
            log.info("注册地址信息：{}" , response.getData());
            return true;
        }
        log.error("未注册接口，参数为：{}",infoList);
        return false;
    }
}
