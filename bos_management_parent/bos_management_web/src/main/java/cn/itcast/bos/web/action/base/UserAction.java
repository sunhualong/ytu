package cn.itcast.bos.web.action.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.UserService;
import cn.itcast.bos.utils.MD5Utils;
import cn.itcast.bos.web.action.common.CommonAction;

public class UserAction extends CommonAction<User> {
	@Autowired
	private UserService userService;
	
	private String checkCode;
	private Integer[] roleIds;

	@Action(value="userAction_login",results={
			@Result(name="success",type="redirect",location="index.html"),
			@Result(name="input",type="redirect",location="login.html")})
	public String login(){
		String checkCodeFS = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
		if(StringUtils.isNotBlank(checkCode)&&checkCode.equals(checkCodeFS)){
			Subject subject = SecurityUtils.getSubject();
			AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), MD5Utils.md5(model.getPassword()));
			try {
				subject.login(token);
				return SUCCESS;
			} catch (AuthenticationException e) {
				e.printStackTrace();
				return INPUT;
			}
		}
		return INPUT;
	}
	@Action(value="userAction_logout",results={@Result(name="success",type="redirect",location="/login.html")})
	public String logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}
	@Action("userAction_pageQuery")
	public String pageQuery(){
		Pageable pageable = this.createPageable();
		Page<User> pageData = userService.pageQuery(pageable);
		this.javaToJson(pageData, new String[]{"roles"});
		return NONE;
	}
	@Action(value="userAction_save",results={@Result(name="success",type="redirect",location="pages/system/userlist.html")})
	public String save(){
		userService.save(model,roleIds);
		return SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}
	
	
	

}
