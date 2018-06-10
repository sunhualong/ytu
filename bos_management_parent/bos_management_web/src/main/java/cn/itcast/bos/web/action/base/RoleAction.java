package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.base.RoleService;
import cn.itcast.bos.web.action.common.CommonAction;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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
	
	@Action("roleAction_editRole")
    public String editRole(){
        Role role = roleService.findById(model.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("name", role.getName());
        map.put("keyword", role.getKeyword());
        map.put("description", role.getDescription());
        map.put("permissions", role.getPermissions());
        map.put("menus", role.getMenus());
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"users","roles","childrenMenus","parentMenu"});
        ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");
        JSONObject json = JSONObject.fromObject(map, jsonConfig);
        try {
            ServletActionContext.getResponse().getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NONE;
    }
    @Action(value="roleAction_edit",results={@Result(name="success",type="redirect",location="/pages/system/role.html")})
    public String editSave(){
        roleService.editSave(model,permissionIds,menuIds);
        return SUCCESS;
    }
}
