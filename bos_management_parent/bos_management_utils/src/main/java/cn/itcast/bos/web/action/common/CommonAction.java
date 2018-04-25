package cn.itcast.bos.web.action.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class CommonAction<T> extends ActionSupport implements ModelDriven<T>{

	protected T model;
	protected int page;
	protected int rows;
	
	public void javaToJson(Page pageData,String[] excludes){
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		JsonConfig config = new JsonConfig();
		config.setExcludes(excludes);
		JSONObject object = JSONObject.fromObject(map,config);
		ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().write(object.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Pageable createPageable(){
		Pageable pageable = new PageRequest(page-1, rows);
		return pageable;
	}
	
	public void javaToJson(List list,String[] excludes){
		JsonConfig config = new JsonConfig();
		config.setExcludes(excludes);
		JSONArray array = JSONArray.fromObject(list,config);
		ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().write(array.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CommonAction() {
		super();
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] arguments = parameterizedType.getActualTypeArguments();
		Class modelClass = (Class) arguments[0];
		try {
			model=(T) modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	@Override
	public T getModel() {
		return model;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
