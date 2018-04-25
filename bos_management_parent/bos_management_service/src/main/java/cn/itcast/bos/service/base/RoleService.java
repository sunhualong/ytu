package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

public interface RoleService {

	Page<Role> pageQuery(Pageable pageable);

	void save(Role model, Integer[] permissionIds, String menuIds);

	List<Role> findAll();

	List<Role> findByUser(User user);

}
