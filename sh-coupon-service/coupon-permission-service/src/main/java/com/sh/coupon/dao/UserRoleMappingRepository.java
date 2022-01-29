package com.sh.coupon.dao;

import com.sh.coupon.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRoleMapping dao
 * @author admin
 */
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping,Integer> {
}
