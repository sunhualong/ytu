package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.PermissionDao;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.PermissionService;
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionDao permissionDao;

	@Override
	public Page<Permission> pageQuery(Pageable pageable) {
		return permissionDao.findAll(pageable);
	}

	@Override
	public void save(Permission model) {
		permissionDao.save(model);
	}

	@Override
	public List<Permission> findAll() {
		return permissionDao.findAll();
	}

	@Override
	public List<Permission> findByUser(User user) {
		if("admin".equals(user.getUsername())){
			return permissionDao.findAll();
		}else{
			return permissionDao.findByUser(user.getId());
		}
	}
}
