package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.MenuDao;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	public Page<Menu> pageQuery(Specification<Menu>spec,Pageable pageable) {
		return menuDao.findAll(spec,pageable);
	}

	@Override
	public List<Menu> findByParentMenuIsNull() {
		return menuDao.findByParentMenuIsNull();
	}

	@Override
	public void save(Menu model) {
		if(model.getParentMenu()!=null&&model.getParentMenu().getId()==null){
			model.setParentMenu(null);
		}
		menuDao.save(model);
	}

	@Override
	public List<Menu> findAll() {
		return menuDao.findAll();
	}

	@Override
	public List<Menu> findByUser() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if("admin".equals(user.getUsername())){
			return menuDao.findAll();
		}else{
			return menuDao.findByUser(user.getId());
		}
	}
}
