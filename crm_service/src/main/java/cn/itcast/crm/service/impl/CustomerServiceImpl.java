package cn.itcast.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerDao;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;
	@Override
	public List<Customer> findAll() {
		return customerDao.findAll();
	}
	@Override
	public List<Customer> findByFixedAreaIdIsNull() {
		return customerDao.findByFixedAreaIdIsNull();
	}
	@Override
	public List<Customer> findByFixedAreaId(String fixedAreaId) {
		return customerDao.findByFixedAreaId(fixedAreaId);
	}
	@Override
	public void assignCustomersToFixedArea(String fixedAreaId, Integer[] customerIds) {
		customerDao.clearByFixedAreaId(fixedAreaId);
		if(customerIds!=null){
			for (Integer id : customerIds) {
				customerDao.assignCustomersToFixedArea(fixedAreaId,id);
			}
		}
	}
	@Override
	public void save(Customer customer) {
		customerDao.save(customer);
	}
	@Override
	public Customer findByTelephone(String telephone) {
		return customerDao.findByTelephone(telephone);
	}
	@Override
	public Customer findByTelephoneAndPassword(String telephone, String password) {
		return customerDao.findByTelephoneAndPassword(telephone,password);
	}
	@Override
	public Customer findByAddress(String address) {
		return customerDao.findByAddress(address);
	}

}
