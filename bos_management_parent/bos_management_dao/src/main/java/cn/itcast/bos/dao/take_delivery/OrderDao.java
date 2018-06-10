package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderDao  extends JpaRepository<Order, Integer>,JpaSpecificationExecutor<Order> {

}
