package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import cn.itcast.bos.web.action.common.CommonAction;
public class StandardAction extends CommonAction<Standard> {

	@Autowired
	private StandardService standardService;
	@Action("standard_findAll")
	public String findAll() throws IOException{
		List<Standard> list = standardService.findAll();
		this.javaToJson(list, null);
		return NONE;
	}
	@Action(value="standard_pageQuery")
	public String pageQuery() throws IOException{
		Pageable pageable = this.createPageable();
		Page<Standard> list = standardService.findAll(pageable);
//		List<Standard> list = standardService.findByName("十斤");
//		List<Standard> list = standardService.findByStandardName("十斤");
		this.javaToJson(list, null);
		return NONE;
	}
	@Action(value="standard_save",results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
	public String save(){
		standardService.save(model);
		return SUCCESS;
	}
	

}
