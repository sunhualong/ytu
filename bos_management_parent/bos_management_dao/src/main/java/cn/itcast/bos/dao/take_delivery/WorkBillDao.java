package cn.itcast.bos.dao.take_delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.WorkBill;

public interface WorkBillDao extends JpaRepository<WorkBill, Integer> {

	List<WorkBill> findByPickstate(String string);

}
