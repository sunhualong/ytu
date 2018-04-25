package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.base.PermissionService;
import cn.itcast.bos.web.action.common.CommonAction;

public class PermissionAction extends CommonAction<Permission> {
	@Autowired
	private PermissionService permissionService;

	@Action("permissionAction_pageQuery")
	public String pageQuery(){
		Pageable pageable = this.createPageable();
		Page<Permission> pageData = permissionService.pageQuery(pageable);
		this.javaToJson(pageData, new String[]{"roles"});
		return NONE;
	}
	
	@Action(value="permissionAction_save",results={@Result(name="success",type="redirect",location="pages/system/permission.html")})
	public String save(){
		permissionService.save(model);
		return SUCCESS;
	}
	
	@Action("permissionAction_findAll")
	public String findAll(){
		List<Permission> list = permissionService.findAll();
		this.javaToJson(list, new String[]{"roles"});
		return NONE;
	}
}
