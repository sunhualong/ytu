package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillDao;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillDao wayBillDao;
	
	/**
     * 保存运单
     */
    @Override
    public void save(WayBill model) {
        wayBillDao.save(model);
    }

    /**
     * 分页查询运单
     */
    @Override
    public Page<WayBill> pageQuery(Pageable pageable) {
        return wayBillDao.findAll(pageable);
    }

    /**
     * 删除运单
     */
    @Override
    public void delete(String wayBillIds) {
        String[] strArray = wayBillIds.split(",");
        for (String wayBillIdStr : strArray) {
            Integer wayBillId = Integer.valueOf(wayBillIdStr);
            wayBillDao.delete(wayBillId);
        }
    }
}
