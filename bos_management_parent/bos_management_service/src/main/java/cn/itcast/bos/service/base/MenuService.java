package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.system.Menu;

public interface MenuService {


	Page<Menu> pageQuery(Specification<Menu> spec, Pageable pageable);

	List<Menu> findByParentMenuIsNull();

	void save(Menu model);

	List<Menu> findAll();

	List<Menu> findByUser();

}
