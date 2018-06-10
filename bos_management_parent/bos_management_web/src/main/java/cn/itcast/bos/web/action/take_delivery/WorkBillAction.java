package cn.itcast.bos.web.action.take_delivery;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.WorkBillService;
import cn.itcast.bos.web.action.common.CommonAction;

public class WorkBillAction extends CommonAction<WorkBill>{

    @Autowired
    private WorkBillService workBillService;
    
    @Action("workBillAction_pageQuery")
    public String pageQuery() {
        Pageable pageable = this.createPageable();
        Page<WorkBill> pageData = workBillService.pageQuery(pageable);
        this.javaToJson(pageData, new String[]{"order","fixedAreas","standard","takeTime"});
        return NONE;
    }
}
