package cn.itcast.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import cn.itcast.bos.utils.MailUtils;

@Component("emailListener")
public class EmailListener implements MessageListener{

	@Override
	public void onMessage(Message arg0) {
		MapMessage message=(MapMessage) arg0;
		try {
			String content = message.getString("content");
			String to = message.getString("to");
			MailUtils.sendMail("激活邮件", content, to);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
