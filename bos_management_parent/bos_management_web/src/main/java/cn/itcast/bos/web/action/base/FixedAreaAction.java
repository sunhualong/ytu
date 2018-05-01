package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.CommonAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.cxf.service.CustomerService;

public class FixedAreaAction extends CommonAction<FixedArea> {
	@Autowired
	private FixedAreaService fixedAreaService;
	@Autowired
	private CustomerService customerService;

	@Action(value="fixedArea_save",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
	@Action("fixedArea_pageQuery")
	public String pageQuery(){
		Pageable pageable = this.createPageable();
		Page<FixedArea> pageData = fixedAreaService.findAll(pageable);
		this.javaToJson(pageData, new String[]{"subareas","couriers"});
		return NONE;
	}
	@Action("findAllCustomers")
	public String findAllCustomers(){
		List<Customer> findAll = customerService.findAll();
		System.out.println(findAll);
		return NONE;
	}
	@Action("fixedArea_findCustomersWithOutFixedArea")
	public String findCustomersWithOutFixedArea(){
		List<Customer> list = customerService.findByFixedAreaIdIsNull();
		this.javaToJson(list, null);
		return NONE;
	}
	@Action("fixedArea_findCustomersByFixedArea")
	public String findCustomersByFixedArea(){
		List<Customer> list = customerService.findByFixedAreaId(model.getId());
		this.javaToJson(list, null);
		return NONE;
	}
	@Action(value="fixedArea_assignCustomersToFixedArea",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
	public String assignCustomersToFixedArea(){
		String id = model.getId();
		String[] customerIds = ServletActionContext.getRequest().getParameterValues("customerIds");
		List<String> list = new ArrayList<>();
		if(customerIds!=null){
			list=Arrays.asList(customerIds);
		}
		List<Integer> il = new ArrayList<>();
		for (String string : list) {
			il.add(Integer.valueOf(string));
		}
		customerService.assignCustomersToFixedArea(id,il);
		return SUCCESS;
	}
	@Action("fixedArea_findCustomersById")
	public String findCustomerById(){
		List<Customer> list = customerService.findByFixedAreaId(model.getId());
		if(list==null){
			list=new ArrayList<>();
		}
		System.out.println("fixedarea"+list);
		this.javaToJson(list, null);
		return NONE;
	}
	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		String id = model.getId();
		String courierId= ServletActionContext.getRequest().getParameter("courierId");
		String takeTimeId= ServletActionContext.getRequest().getParameter("takeTimeId");
		fixedAreaService.associationCourierToFixedArea(id,Integer.valueOf(courierId),Integer.valueOf(takeTimeId));
		return SUCCESS;
	}
	
	
	
}
