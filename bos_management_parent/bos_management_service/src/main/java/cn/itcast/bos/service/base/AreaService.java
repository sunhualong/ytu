package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Area;

public interface AreaService {

	void save(List<Area> list);

	Page<Area> findAll(Pageable pageable);

	List<Area> findAll();

	List<Area> findAllByQ(String q);

    void saveOne(Area model);

    void deleteId(String deleId);

}
