package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.cxf.transport.servlet.servicelist.ServiceListJAASAuthenticator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.common.CommonAction;

public class SubAreaAction extends CommonAction<SubArea> {
	@Autowired
	private SubAreaService subAreaService;
	
	private String ids;
	
	@Action(value="subArea_save",results={@Result(name="success",location="/pages/base/sub_area.html",type="redirect")})
	public String save(){
		subAreaService.save(model);
		return SUCCESS;
	}
	@Action("subArea_pageQuery")
	public String pageQuery(){
		Pageable pageable = this.createPageable();
		Page<SubArea> pageData = subAreaService.findAll(pageable);
		this.javaToJson(pageData, new String[]{"subareas","fixedArea"});
		return NONE;
	}
	@Action("subArea_findByFixedAreaId")
	public String findByFixedAreaId(){
		String id = ServletActionContext.getRequest().getParameter("id");
		List<SubArea> list = subAreaService.findByFixedAreaId(id);
		System.out.println("subarea"+list);
		this.javaToJson(list, new String[]{"subareas","fixedArea"});;
		return NONE;
	}
	@Action("subArea_export")
	public String export() throws IOException{
		List<SubArea> list = subAreaService.findAllById(ids);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("分区编号");
		row.createCell(1).setCellValue("分区关键字");
		row.createCell(2).setCellValue("分区辅助关键字");
		row.createCell(3).setCellValue("区域编号");
		if(list!=null){
			for (int i=0;i<list.size();i++) {
				SubArea sa = list.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				row2.createCell(0).setCellValue(sa.getId());
				row2.createCell(1).setCellValue(sa.getKeyWords());
				row2.createCell(2).setCellValue(sa.getAssistKeyWords());
				Area area = sa.getArea();
				if(area!=null){
					row2.createCell(3).setCellValue(area.getId());
				}else{
					row2.createCell(3).setCellValue("未指定区域");
				}
			}
		}
		String filename="找你干啥.xls";
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
		ServletActionContext.getResponse().setContentType(mimeType);
		String header = ServletActionContext.getRequest().getHeader("User-Agent");
		String filename2 = FileUtils.encodeDownloadFilename(filename, header);
		ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename2);
		wb.write(outputStream);
		return NONE;
	}
	@Action("subAreaAction_findGroupedSubArea")
	public String findGroupedSubArea(){
		List<SubArea> list = subAreaService.findGroupedSubArea();
		this.javaToJson(list, null);
		return NONE;
	}
	
	public void setIds(String ids) {
		this.ids = ids;
	}

}
