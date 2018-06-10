package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {

	Page<Standard> findAll(Pageable pageable);

	List<Standard> findByName(String name);

	List<Standard> findByStandardName(String string);

	void save(Standard standard);

	List<Standard> findAll();

    void delete(String ids);

}
