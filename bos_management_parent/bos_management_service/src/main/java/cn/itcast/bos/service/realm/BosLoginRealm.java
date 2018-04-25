package cn.itcast.bos.service.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.PermissionService;
import cn.itcast.bos.service.base.RoleService;
import cn.itcast.bos.service.base.UserService;

public class BosLoginRealm extends AuthorizingRealm{
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RoleService roleService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		List<Permission> list = permissionService.findByUser(user);
		List<Role> rlist = roleService.findByUser(user);
		SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
		if(rlist!=null){
			for (Role role : rlist) {
				sai.addRole(role.getKeyword());
			}
		}
		if(list!=null){
			for (Permission p : list) {
				sai.addStringPermission(p.getKeyword());
			}
		}
		return sai;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken) token;
		User user = userService.findByUsername(upt.getUsername());
		if(user!=null){
			return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		}
		return null;
	}

}
