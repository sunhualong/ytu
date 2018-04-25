package cn.itcast.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.web.action.common.CommonAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.cxf.service.OrderService;

public class OrderAction extends CommonAction<Order> {
	@Autowired
	private OrderService orderService;
	
	private String sendAreaInfo;
	private String recAreaInfo;
	
	@Action("order_add")
	public String save(){
		String[] sendAreaStr = sendAreaInfo.split("/");
		String[] recAreaStr = recAreaInfo.split("/");
		Area sendArea= new Area(sendAreaStr[0], sendAreaStr[1], sendAreaStr[2]);
		Area recArea= new Area(recAreaStr[0], recAreaStr[1], recAreaStr[2]);
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		if(customer!=null){
			model.setCustomer_id(customer.getId());
			model.setTelephone(customer.getTelephone());
		}
		orderService.save(model);
		return NONE;
	}

	
	
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

}
