package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;
public interface StandardDao extends JpaRepository<Standard, Integer> {

	List<Standard> findByName(String name);

//	@Query("from Standard where name=?")
	@Query(value="select * from t_standard where c_name=?",nativeQuery=true)
	List<Standard> findByStandardName(String string);

}
