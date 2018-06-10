package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	Page<Courier> findAll(Pageable pageable);

	void save(Courier model);

	void updateDelTag(String ids);

	Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

	List<Courier> findAll();

    List<Courier> findByFixedAreaId(String fixedAreaId);

    List<Courier> findAllCourier();

}
