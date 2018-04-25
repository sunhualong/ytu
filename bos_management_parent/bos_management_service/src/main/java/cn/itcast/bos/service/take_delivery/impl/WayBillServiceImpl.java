package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillDao;
import cn.itcast.bos.service.take_delivery.WayBillService;
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillDao wayBillDao;
}
