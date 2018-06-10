package cn.itcast.bos.web.action.take_delivery;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderQueryService;
import cn.itcast.bos.web.action.common.CommonAction;

public class OrderAction extends CommonAction<Order> {
    
    @Autowired
    private OrderQueryService orderService;
    
    @Action(value="orderAction_pageQuery")
    public String pageQuery(){
        Pageable pageable = this.createPageable();
        Page<Order> pageData = orderService.pageQuery(pageable);
        this.javaToJson(pageData, new String[]{"workBills","courier"});
        return NONE;
    }

}