package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.UserDao;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.UserService;
import cn.itcast.bos.utils.MD5Utils;
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public Page<User> pageQuery(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	public void save(User model, Integer[] roleIds) {
		model.setPassword(MD5Utils.md5(model.getPassword()));
		userDao.save(model);
		for (Integer id : roleIds) {
			Role r = new Role();
			r.setId(id);
			model.getRoles().add(r);
		}
	}
}
