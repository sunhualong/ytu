package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;

public interface PermissionService {

	Page<Permission> pageQuery(Pageable pageable);

	void save(Permission model);

	List<Permission> findAll();

	List<Permission> findByUser(User user);

}
