package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bos.web.action.common.CommonAction;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class CourierAction extends CommonAction<Courier> {

	@Autowired
	private CourierService courierService;

	@Action("courier_pageQuery")
	public String pageQuery() throws IOException{
		final String courierNum=model.getCourierNum();
		final Standard standard = model.getStandard();
		final String company = model.getCompany();
		final String type = model.getType();
		
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if(StringUtils.isNotBlank(courierNum)){
					Predicate predicate = cb.like(root.get("courierNum").as(String.class), "%"+courierNum+"%");
					list.add(predicate);
				}
				if(StringUtils.isNotBlank(company)){
					Predicate predicate = cb.like(root.get("company").as(String.class), "%"+company+"%");
					list.add(predicate);
				}
				if(StringUtils.isNotBlank(type)){
					Predicate predicate = cb.like(root.get("type").as(String.class), "%"+type+"%");
					list.add(predicate);
				}
				if(standard!=null&&StringUtils.isNotBlank(standard.getName())){
					Join<Object, Object> join = root.join("standard");
					Predicate predicate = cb.equal(join.get("name").as(String.class), standard.getName());
					list.add(predicate);
				}
				Predicate[] restrictions = new Predicate[list.size()];
				list.toArray(restrictions);
				return cb.and(restrictions );
			}
		};
		
		
		
		Pageable pageable = this.createPageable();
		Page<Courier> pageData = courierService.findAll(specification,pageable);
		javaToJson(pageData, new String[]{"fixedAreas"});
		return NONE;
	}
	@Action(value="courier_save",results={
			@Result(name="success",location="/pages/base/courier.html",type="redirect"),
			@Result(name="input",location="/unauthorized.html",type="redirect")})
	public String save(){
		model.setDeltag('0');
		try {
			courierService.save(model);
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	@Action(value="courier_updateDelTag",results={
			@Result(name="success",location="/pages/base/courier.html",type="redirect"),
			@Result(name="input",location="/unauthorized.html",type="redirect")})
	public String updateDelTag(){
		String ids = (String) ServletActionContext.getRequest().getParameter("ids");
		try {
			courierService.updateDelTag(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	@Action("courier_findAll")
	public String findAll(){
		List<Courier> list = courierService.findAll();
		this.javaToJson(list, new String[]{"fixedAreas"});
		return NONE;
	}

}
