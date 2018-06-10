package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.OrderDao;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderQueryService;

@Service
@Transactional
public class OrderQueryServiceImpl implements OrderQueryService{
    
    @Autowired
    private OrderDao orderDao;

    @Override
    public Page<Order> pageQuery(Pageable pageable) {
        Page<Order> page = orderDao.findAll(pageable);
        return page;
    }

}
