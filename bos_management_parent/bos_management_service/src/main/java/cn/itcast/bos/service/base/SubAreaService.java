package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.SubArea;

public interface SubAreaService {

	void save(SubArea model);

	Page<SubArea> findAll(Pageable pageable);

	List<SubArea> findByFixedAreaId(String id);

	List<SubArea> findAll();

	List<SubArea> findAllById(String ids);

	List<SubArea> findGroupedSubArea();

}
