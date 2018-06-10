package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.RoleDao;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.RoleService;
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Override
	public Page<Role> pageQuery(Pageable pageable) {
		return roleDao.findAll(pageable);
	}

	@Override
	public void save(Role model, Integer[] permissionIds, String menuIds) {
		roleDao.save(model);
		String[] mIds = menuIds.split(",");
		for (Integer id : permissionIds) {
			Permission p = new Permission();
			p.setId(id);
			model.getPermissions().add(p);
		}
		for (String string : mIds) {
			Menu m = new Menu();
			m.setId(Integer.valueOf(string));
			model.getMenus().add(m);
		}
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAll();
	}

	@Override
	public List<Role> findByUser(User user) {
		if("admin".equals(user.getUsername())){
			return roleDao.findAll();
		}else{
			return roleDao.findByUser(user.getId());
		}
	}

    @Override
    public Role findById(Integer id) {
        Role findOne = roleDao.findOne(id);
        return findOne;
    }

    @Override
    public void editSave(Role model, Integer[] permissionIds, String menuIds) {
        model.getPermissions().clear();
        model.getMenus().clear();
        
        if (StringUtils.isNotBlank(menuIds)) {
            String[] menuIdArray = menuIds.split(",");
            for (String menuId : menuIdArray) {
                Menu menu = new Menu();
                menu.setId(Integer.valueOf(menuId));
                model.getMenus().add(menu); 
            }
        }
        
        //角色关联权限
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                Permission permission = new Permission();
                permission.setId(permissionId);
                model.getPermissions().add(permission);
            }
        }
        roleDao.save(model);
    }
}
