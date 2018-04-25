package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.base.RoleService;
import cn.itcast.bos.web.action.common.CommonAction;

public class RoleAction extends CommonAction<Role>{
	
	private Integer[] permissionIds;
	private String menuIds;

	@Autowired
	private RoleService roleService;
	@Action("roleAction_pageQuery")
	public String pageQuery(){
		Pageable pageable = createPageable();
		Page<Role> pageData = roleService.pageQuery(pageable);
		this.javaToJson(pageData, new String[]{"users","permissions","menus"});
		return NONE;
	}
	
	@Action(value="roleAction_save",results={@Result(name="success",type="redirect",location="/pages/system/role.html")})
	public String save(){
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
	
	@Action("roleAction_findAll")
	public String findAll(){
		List<Role> list = roleService.findAll();
		this.javaToJson(list, new String[]{"users","permissions","menus"});
		return NONE;
	}

	public void setPermissionIds(Integer[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	
}
