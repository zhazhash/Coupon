package com.sh.coupon.dao;

import com.sh.coupon.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * path对应的dao
 * @author admin
 */
public interface PathRepository extends JpaRepository<Path,Integer> {
    /**
     * 根据微服务名称，查找path记录
     * @param serviceName
     * @return
     */
    List<Path> findAllByServiceName(String serviceName);

    /**
     * 根据路径模式以及请求类型，查找数据库
     * @param pathPattern
     * @param httpMethod
     * @return
     */
    Path findByPathPatternAndHttpMethod(String pathPattern,String httpMethod);
}
