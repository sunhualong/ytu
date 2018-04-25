package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.common.CommonAction;

public class TakeTimeAction extends CommonAction<TakeTime>{

	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value="takeTime_findAll")
	public String findAll(){
		List<TakeTime> list = takeTimeService.findAll();
		this.javaToJson(list, null);
		return NONE;
	}
}
