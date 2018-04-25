package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;

public interface SubAreaDao extends JpaRepository<SubArea, String>{

	//@Query(value="from SubArea where fixedArea = ?")
	List<SubArea> findByFixedArea(FixedArea fa);

	SubArea findById(String id);

	@Query("select a.province,count(*) from SubArea s join s.area a group by a.province")
	List<SubArea> findGroupedSubArea();

}
