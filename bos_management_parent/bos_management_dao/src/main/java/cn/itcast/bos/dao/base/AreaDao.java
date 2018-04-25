package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Area;

public interface AreaDao extends JpaRepository<Area, String> {

	@Query(" from Area where shortcode like ? or citycode like ? or province like ? or city like ? or district like ?")
	List<Area> findByQ(String q1,String q2,String q3,String q4,String q5);

	Area findByProvinceAndCityAndDistrict(String province, String city, String district);

}
