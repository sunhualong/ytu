package cn.itcast.bos.web.action.take_delivery;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.CommonAction;

public class WayBillAction extends CommonAction<WayBill>{

	@Autowired
	private WayBillService wayBillService;
	
	@Action(value="waybill_save",results={@Result(name="success",type="redirect",location="/pages/take_delivery/waybill_quick.html")})
	public String save(){
		wayBillService.save(model);
		return SUCCESS;
	}
	
	/**
     * 查询所有运单
     */
    @Action("waybill_pageQuery")
    public String findAll() {
        //定义条件
        Pageable pageable = this.createPageable();
        Page<WayBill> pagedata = wayBillService.pageQuery(pageable);
        
        this.javaToJson(pagedata, null);
        return NONE;
    }
}
