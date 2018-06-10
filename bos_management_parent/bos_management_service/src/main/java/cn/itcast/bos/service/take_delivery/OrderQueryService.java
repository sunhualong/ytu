package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderQueryService {
    
    public Page<Order> pageQuery(Pageable pageable);

}
