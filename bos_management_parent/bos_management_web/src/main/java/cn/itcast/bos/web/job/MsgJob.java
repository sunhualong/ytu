package cn.itcast.bos.web.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyuncs.exceptions.ClientException;

import cn.itcast.bos.dao.take_delivery.WorkBillDao;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.utils.MsgUtils;

public class MsgJob {
	@Autowired
	private WorkBillDao workBillDao;

	public void sendMsg() throws ClientException{
		 List<WorkBill> list = workBillDao.findByPickstate("新单");
		 if(list!=null){
			 for (WorkBill workBill : list) {
				String telephone = workBill.getCourier().getTelephone();
				MsgUtils.sendSms(telephone,RandomStringUtils.randomAlphanumeric(4));
				System.out.println("send"+new Date().toLocaleString());
			}
		 }
	}
}
