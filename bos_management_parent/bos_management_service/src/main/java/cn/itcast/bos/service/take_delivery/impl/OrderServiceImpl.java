package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaDao;
import cn.itcast.bos.dao.base.CourierDao;
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
	@Autowired
	private CourierDao courierDao;
	
	@Override
	public void save(Order order) {
		//Customer customer = customerService.findByAddress(order.getSendAddress());
//		if(customer!=null&&StringUtils.isNotBlank(customer.getFixedAreaId())){
//			FixedArea fixedArea = fixedAreaDao.findOne(customer.getFixedAreaId());
//			findCouriers(fixedArea,order);
//			return;
//			
//		}else{
			Area area= order.getSendArea();
			area = areaDao.findByProvinceAndCityAndDistrict(area.getProvince(),area.getCity(),area.getDistrict());
			Set<SubArea> subareas = area.getSubareas();
			for (SubArea subArea : subareas) {
				String sendAddress = order.getSendAddress();
				if(sendAddress!=null&&(sendAddress.contains(subArea.getKeyWords())||sendAddress.contains(subArea.getAssistKeyWords()))){
					FixedArea fixedArea=subArea.getFixedArea();
					findCouriers(fixedArea,order);
					return;
//				}
			}
		}
		order.setOrderType("人工分单");
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
			order.setOrderType("已分单");
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
		order.setOrderType("人工分单");
        order.setStatus("1");
        order.setOrderTime(new Date());
        order.setSendArea(null);
        order.setRecArea(null);
        orderDao.save(order);
	}
    @Override
    public List<Order> pageQueryByOrderType(int page, int rows) {
        Pageable pageable = new PageRequest(page-1, rows);
        Specification<Order> specification = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("orderType").as(String.class), "人工分单");
            }
        };
        Page<Order> all = orderDao.findAll(specification, pageable);
        return all.getContent();
    }
    @Override
    public Long count() {
        Specification<Order> specification = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("orderType").as(String.class), "人工分单");
            }
        };
        return orderDao.count(specification);
    }
    @Override
    public void updateCourier(Order model) {
        Order order = orderDao.findOne(model.getId());
        Courier courier = courierDao.findOne(model.getCourier().getId());
        order.setCourier(courier);
        order.setOrderType("已分单");
        WorkBill workBill = createWorkBill(order);
        workBillDao.save(workBill);
    }
}
