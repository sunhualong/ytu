package cn.itcast.bos.web.action.take_delivery;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.common.CommonAction;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class DispatcherAction extends CommonAction<Order> {

	@Autowired
	private OrderService orderService;
	/**
	 * 查询订单类型为手动分担的订单集合
	 * @return
	 */
	@Action("dispatcherAction_findByOrderType")
	public String findByOrderType(){
		List<Order> list = orderService.pageQueryByOrderType(page,rows);
		Long total = orderService.count();
		
		//将参数封装成前端框架格式
		Map<String, Object> map = new HashMap<>();
		map.put("total", total);
		map.put("rows", list);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"workBills","courier"});
		JSONObject jsonObject = JSONObject.fromObject(map, config);
		
		//响应回前端
		ServletActionContext.getResponse().setContentType("application/json;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 进行人工调度
	 * @return
	 */
	@Action(value="dispatcherAction_dispatcher",results={@Result(name="success",type="redirect",location="/pages/take_delivery/dispatcher.html")})
	public String dispatcher(){
		orderService.updateCourier(model);
		return SUCCESS;
	}
	
}
