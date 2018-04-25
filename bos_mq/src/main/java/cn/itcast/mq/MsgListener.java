package cn.itcast.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import cn.itcast.bos.utils.MsgUtils;
@Component("msgListener")
public class MsgListener implements MessageListener{

	@Override
	public void onMessage(Message msg) {
		MapMessage message=(MapMessage) msg;
		try {
			String telephone = message.getString("telephone");
			String content = message.getString("content");
			System.out.println("content： "+content+",telephone: "+telephone);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
