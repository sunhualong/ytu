package cn.itcast.bos.web.action.base;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.base.MenuService;
import cn.itcast.bos.web.action.common.CommonAction;

public class MenuAction extends CommonAction<Menu> {
	@Autowired
	private MenuService menuService;
	
	@Action("menuAction_pageQuery")
	public String pageQery(){
		setPage(Integer.valueOf(model.getPage()));
		Specification<Menu> specification = new Specification<Menu>() {
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.isNull(root.get("parentMenu").as(Menu.class));
				return predicate;
			}
		};
		Pageable pageable = createPageable();
		Page<Menu> pageData = menuService.pageQuery(specification,pageable);
		this.javaToJson(pageData, new String[]{"roles","childrenMenus","parentMenu"});
		return NONE;
	}
	
	@Action("menuAction_findByParentMenuIsNull")
	public String findByParentMenuIsNull(){
		List<Menu> list = menuService.findByParentMenuIsNull();
		this.javaToJson(list, new String[]{"roles","childrenMenus","parentMenu"});
		return NONE;
	}
	
	@Action(value="menuAction_save",results={@Result(name="success",type="redirect",location="/pages/system/menu.html")})
	public String save(){
		menuService.save(model);
		return SUCCESS;
	}
	
	@Action("menuAction_findAll")
	public String findAll(){
		List<Menu> list = menuService.findAll();
		this.javaToJson(list, new String[]{"roles","childrenMenus","children","parentMenu"});
		return NONE;
	}
	
	@Action("menuAction_findByUser")
	public String findByUser(){
		List<Menu> list = menuService.findByUser();
		this.javaToJson(list, new String[]{"roles","childrenMenus","children","parentMenu"});
		return NONE;
	}

}
