package com.sh.coupon.service;

import com.sh.coupon.contant.RoleEnum;
import com.sh.coupon.dao.PathRepository;
import com.sh.coupon.dao.RolePathMappingRepository;
import com.sh.coupon.dao.RoleRepository;
import com.sh.coupon.dao.UserRoleMappingRepository;
import com.sh.coupon.entity.Path;
import com.sh.coupon.entity.Role;
import com.sh.coupon.entity.RolePathMapping;
import com.sh.coupon.entity.UserRoleMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户权限验证
 * @author admin
 */
@Slf4j
@Service
public class PermissionService {
    private final PathRepository pathRepository;
    private final RolePathMappingRepository rolePathMappingRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;

    public PermissionService(PathRepository pathRepository, RolePathMappingRepository rolePathMappingRepository, RoleRepository roleRepository, UserRoleMappingRepository userRoleMappingRepository) {
        this.pathRepository = pathRepository;
        this.rolePathMappingRepository = rolePathMappingRepository;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    /**
     * 用户访问接口权限校验
     * @param userId
     * @param uri
     * @param httpMethod
     * @return 是否包含
     */
    public boolean checkPermission(Long userId,String uri,String httpMethod){
        Path path = pathRepository.findByPathPatternAndHttpMethod(uri,httpMethod);
        //由于有些接口不需要权限验证，所有不存在于路径表中，对于此种uri直接放过。如：健康检查
        if (null == path){
            return true;
        }
        UserRoleMapping user = userRoleMappingRepository.findByUserId(userId);
        if(null == user){
          log.error("userId not exist is UserRoleMapping:{}" ,userId);
          return false;
        }
        Optional<Role> role = roleRepository.findById(user.getRoleId());
        if (!role.isPresent()){
            log.info("roleId not exist in Role:{}",user.getRoleId());
            return  false;
        }
        //如果是超级管理员，直接返回true
        if(RoleEnum.SUPER_ADMIN.name().equals(role.get().getRoleTag())){
            return true;
        }

        RolePathMapping rolePathMapping = rolePathMappingRepository
                .findByRoleIdAndPathId(role.get().getId(),path.getId());
        return null != rolePathMapping;
    }
}
