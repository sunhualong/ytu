package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaDao;
import cn.itcast.bos.dao.base.FixedAreaDao;
import cn.itcast.bos.dao.take_delivery.OrderDao;
import cn.itcast.bos.dao.take_delivery.WorkBillDao;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.crm.domain.Customer;
import cn.itcast.cxf.service.CustomerService;
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private FixedAreaDao fixedAreaDao;
	@Autowired
	private WorkBillDao workBillDao;
	@Autowired
	private AreaDao areaDao;
	
	@Override
	public void save(Order order) {
		Customer customer = customerService.findByAddress(order.getSendAddress());
		if(customer!=null&&StringUtils.isNotBlank(customer.getFixedAreaId())){
			FixedArea fixedArea = fixedAreaDao.findOne(customer.getFixedAreaId());
			findCouriers(fixedArea,order);
			return;
			
		}else{
			Area area= order.getSendArea();
			area = areaDao.findByProvinceAndCityAndDistrict(area.getProvince(),area.getCity(),area.getDistrict());
			Set<SubArea> subareas = area.getSubareas();
			for (SubArea subArea : subareas) {
				String sendAddress = order.getSendAddress();
				if(sendAddress!=null&&(sendAddress.contains(subArea.getKeyWords())||sendAddress.contains(subArea.getAssistKeyWords()))){
					FixedArea fixedArea=subArea.getFixedArea();
					findCouriers(fixedArea,order);
					return;
				}
			}
		}
		order.setOrderType("1");
		order.setStatus("1");
		order.setOrderTime(new Date());
		order.setSendArea(null);
		order.setRecArea(null);
		orderDao.save(order);
	}
	private WorkBill createWorkBill(Order order) {
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setAttachbilltimes(0);
		workBill.setSmsNumber(UUID.randomUUID().toString());
		workBill.setCourier(order.getCourier());
		workBill.setOrder(order);
		workBill.setRemark(order.getRemark());
		return workBill;
	}
	private void findCouriers(FixedArea fixedArea,Order order){
		Set<Courier> couriers = fixedArea.getCouriers();
		for (Courier courier : couriers) {
			order.setCourier(courier);
			order.setOrderType("1");
			order.setStatus("1");
			order.setOrderTime(new Date());
			order.setSendArea(null);
			order.setRecArea(null);
			orderDao.save(order);
			WorkBill workBill = createWorkBill(order);
			workBillDao.save(workBill);
			String msg=courier.getTelephone()+"请到"+order.getSendAddress()+"取件,联系电话:"+order.getSendMobile();
			System.out.println(msg);
			return ;
		}
	}
}
