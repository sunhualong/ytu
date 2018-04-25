package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierDao courierDao;

	@Override
	public Page<Courier> findAll(Pageable pageable) {
		return courierDao.findAll(pageable);
	}

	@Override
	@RequiresPermissions("abc")//注解方式实现权限控制
	public void save(Courier model) {
		courierDao.save(model);
	}

	@Override
	public void updateDelTag(String ids) {
		//编码方式实现权限控制
		Subject subject = SecurityUtils.getSubject();
		subject.checkPermission("abc");
		String[] idArr=ids.split(",");
		for (String id : idArr) {
			courierDao.updateDelTag(Integer.valueOf(id));
		}
	}

	@Override
	public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
		return courierDao.findAll(specification, pageable);
	}

	@Override
	public List<Courier> findAll() {
		return courierDao.findAll();
	}
}
