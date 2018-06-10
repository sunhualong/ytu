package cn.itcast.bos.service.take_delivery;

import java.util.List;

import javax.jws.WebService;

import cn.itcast.bos.domain.take_delivery.Order;

@WebService
public interface OrderService {

	public void save(Order order);

    public List<Order> pageQueryByOrderType(int page, int rows);

    public Long count();

    public void updateCourier(Order model);

    
}
