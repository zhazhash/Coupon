package com.sh.coupon.dao;

import com.sh.coupon.entity.RolePathMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * role_path_mapping dao
 * @author admin
 */
public interface RolePathMappingRepository extends JpaRepository<RolePathMapping,Integer> {
    /**
     * 根据角色id与路径id查询
     * 根据角色id与路径id查询
     * @param role
     * @param pathId
     * @return
     */
    RolePathMapping findByRoleIdAndPathId(Integer role, Integer pathId);
}
