package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	Page<User> pageQuery(Pageable pageable);

	void save(User model, Integer[] roleIds);

}
