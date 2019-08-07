package com.bonc.security.springSecurity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.bonc.entity.jpa.Permission;
import com.bonc.entity.jpa.Role;
/**
 * 权限permission解析类，自定义规则
 * @return
 */
@Configuration
public class PermissionEvaluatorImpl implements PermissionEvaluator{
	@Override
//	targetDomainObject即是我们之前定义的resourceId，而permission即为privilege
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		Set<String> permissions=new HashSet<>();
		if(authentication.getPrincipal().toString().compareToIgnoreCase("anonymousUser") != 0){
//			String privilege = targetDomainObject + "-" + permission;
			for(Role role : ((UserDetailsImpl)authentication.getPrincipal()).getUser().getRoles()){
				permissions.addAll(role.getPermissons()
						.stream()
						.filter(Objects::nonNull)
						.map(Permission::getpName)
						.collect(Collectors.toList()));
			}
		}				
		return permissions.contains(permission);
	}
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
