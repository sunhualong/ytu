package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WorkBillDao;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.WorkBillService;

@Service
@Transactional
public class WorkBillServiceImpl implements WorkBillService{
    
    @Autowired 
    private WorkBillDao workBillDao;

    @Override
    public Page<WorkBill> pageQuery(Pageable pageable) {
        Page<WorkBill> page = workBillDao.findAll(pageable);
        return page;
    }

}
