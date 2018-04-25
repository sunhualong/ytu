package cn.itcast.crm.service;

import java.util.List;

import javax.jws.WebService;

import cn.itcast.crm.domain.Customer;
@WebService
public interface CustomerService {
	public List<Customer> findAll();
	public List<Customer> findByFixedAreaIdIsNull();
	public List<Customer> findByFixedAreaId(String fixedAreaId);
	public void assignCustomersToFixedArea(String fixedAreaId, Integer[] customerIds);
	public void save(Customer customer);
	public Customer findByTelephone(String telephone);
	public Customer findByTelephoneAndPassword(String telephone, String password);
	public Customer findByAddress(String address);
}
