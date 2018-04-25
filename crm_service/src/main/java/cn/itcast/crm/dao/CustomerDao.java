package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer> {

	List<Customer> findByFixedAreaIdIsNull();

	List<Customer> findByFixedAreaId(String fixedAreaId);

	@Query("update Customer set fixedAreaId=null where fixedAreaId=?")
	@Modifying
	void clearByFixedAreaId(String fixedAreaId);

	@Query("update Customer set fixedAreaId=? where id=?")
	@Modifying
	void assignCustomersToFixedArea(String fixedAreaId, Integer id);

	Customer findByTelephone(String telephone);

	Customer findByTelephoneAndPassword(String telephone, String password);

	Customer findByAddress(String address);

}
