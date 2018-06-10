package cn.itcast.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.aliyuncs.exceptions.ClientException;

import cn.itcast.bos.utils.MD5Utils;
import cn.itcast.bos.utils.MailUtils;
import cn.itcast.bos.utils.MsgUtils;
import cn.itcast.bos.web.action.common.CommonAction;
import cn.itcast.crm.domain.Customer;
import cn.itcast.cxf.service.CustomerService;

public class CustomerAction extends CommonAction<Customer> {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private JmsTemplate jmsTemplate;

	@Action("customer_sendMsg")
	public String sendMsg() {
		String tel = model.getTelephone();
		String code = RandomStringUtils.randomNumeric(4);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute(model.getTelephone(), code);
		System.out.println(tel);
		System.out.println(code);
		try {
			MsgUtils.sendSms(tel, code);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return NONE;
	}

	@Action(value="customer_regist",results={@Result(name="success",type="redirect",location="signup-success.html"),
			@Result(name="input",type="redirect",location="signup.html")})
	public String regist(){
		String code = ServletActionContext.getRequest().getParameter("code");
		String codefSession = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		System.out.println(code+"   code");
		System.out.println(codefSession+"   sessionCode");
		if(StringUtils.isNotBlank(code)&&code.equals(codefSession)){
			model.setPassword(MD5Utils.md5(model.getPassword()));
			
			final String activeCode = RandomStringUtils.randomNumeric(24);
//			jmsTemplate.send("sendEmail",new MessageCreator() {
//				public Message createMessage(Session session) throws JMSException {
//					MapMessage mapMessage = session.createMapMessage();
//					String url = MailUtils.activeUrl+"?telephone="+model.getTelephone()+"&activeCode="+activeCode;
//					String content = "尊敬的用户,请点击以下链接进行账号激活<br/>"
//							+ "<a href='"+url+"'>点击激活</a>";
//					mapMessage.setString("content",content);
//					mapMessage.setString("to", model.getEmail());
//					return mapMessage;
//				}
//			});
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1, TimeUnit.DAYS);
			String url = MailUtils.activeUrl+"?telephone="+model.getTelephone()+"&activeCode="+activeCode;
			String content = "尊敬的用户,请点击以下链接进行账号激活<br/><a href='"+url+"'>点击激活</a>";
			MailUtils.sendMail("激活邮件", content, model.getEmail());
			
//			jmsTemplate.send("sendMsg", new MessageCreator() {
//				public Message createMessage(Session session) throws JMSException {
//					MapMessage mapMessage = session.createMapMessage();
//					mapMessage.setString("telephone", model.getTelephone());
//					String content = "恭喜您,注册成功! 请前往邮箱"+model.getEmail()+"激活";
//					mapMessage.setString("content",content);
//					return mapMessage;
//				}
//			});
			customerService.save(model);
			return SUCCESS;
		}else{
			return INPUT;
		}
	}
	@Action(value="customer_activeMail")
	public String activeMail() throws IOException{
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		
		String activeCodeFRedis = redisTemplate.opsForValue().get(model.getTelephone());
		String activeCode = ServletActionContext.getRequest().getParameter("activeCode");
		if(activeCodeFRedis!=null){
			if(activeCodeFRedis.equals(activeCode)){
				Customer customer = customerService.findByTelephone(model.getTelephone());
				System.out.println(customer.getType()+"   type");
				if(customer!=null&&customer.getType()==null){
					customer.setType(1);
					customerService.save(customer);
					ServletActionContext.getResponse().getWriter().write("激活成功");
				}else{
					ServletActionContext.getResponse().getWriter().write("不可重复激活");
				}
			}else{
				ServletActionContext.getResponse().getWriter().write("激活码异常");
			}
		}else{
			ServletActionContext.getResponse().getWriter().write("激活码已失效");
		}
		return NONE;
	}
	@Action(value="customer_login",results={@Result(name="success",type="redirect",location="index.html"),
			@Result(name="input",type="redirect",location="login.html")})
	public String login(){
		String valuedateCodeSession = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
		String valuedateCode = ServletActionContext.getRequest().getParameter("checkcode");
		System.out.println(valuedateCodeSession+"   "+valuedateCode+"    "+model.getTelephone()+"   "+model.getPassword());
		if(valuedateCode!=null&&valuedateCode.equals(valuedateCodeSession)){
			Customer customer = customerService.findByTelephoneAndPassword(model.getTelephone(), MD5Utils.md5(model.getPassword()));
			if(customer!=null&&customer.getType()!=null&&customer.getType() == 1){
				ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
				return SUCCESS;
			}
		}
		return INPUT;
	}

}
