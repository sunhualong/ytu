package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.CommonAction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class AreaAction extends CommonAction<Area> {

	@Autowired
	private AreaService areaService;

	
	private File xlsFile;
	private String contentType;
	private String filename;

	@Action("area_findAll")
	public String findAll(){
		String q = ServletActionContext.getRequest().getParameter("q");
		if(q==null){
			q="";
		}
		List<Area> list = areaService.findAllByQ(q);
		javaToJson(list, new String[]{"subareas"});
		return NONE;
	}
	@Action("area_import")
	public String importDate() throws IOException {
		String flag = "0";
		try {
			FileInputStream s = new FileInputStream(xlsFile);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(s);
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			List<Area> list = new ArrayList<>();
			for (Row row : sheet) {
				if(row.getRowNum()!=0){
					Area area = new Area(row.getCell(0).getStringCellValue(), 
							row.getCell(1).getStringCellValue(), 
							row.getCell(2).getStringCellValue(), 
							row.getCell(3).getStringCellValue(), 
							row.getCell(4).getStringCellValue()); 
					String province = row.getCell(1).getStringCellValue();
					String city = row.getCell(2).getStringCellValue();
					String district = row.getCell(3).getStringCellValue();
					area.setCitycode(PinYin4jUtils.hanziToPinyin(city.substring(0, city.length()-1), ""));
					String temp = province.substring(0,province.length()-1)+city.substring(0, city.length()-1)+district.substring(0, district.length()-1);
					area.setShortcode(StringUtils.join(PinYin4jUtils.getHeadByString(temp)));
					list.add(area);
				}
			}
			areaService.save(list);
		} catch (Exception e) {
			flag = "1";
			e.printStackTrace();
		}
		ServletActionContext.getResponse().getWriter().write(flag);
		return NONE;
	}

	@Action("area_pageQuery")
	public String pageQuery() throws IOException{
		Pageable pageable = this.createPageable();
		Page<Area> pageData = areaService.findAll(pageable);
		javaToJson(pageData, new String[]{"subareas"});
		return NONE;
	}
	
//	@Autowired
//	private DataSource dataSource;
	
	@Action("areaAction_export")
	public String export() throws IOException, JRException, SQLException{
		List<Area> list = areaService.findAll();
		ServletOutputStream os = ServletActionContext.getResponse().getOutputStream();
		String name="区域信息.pdf";
		String agent = ServletActionContext.getRequest().getHeader("User-Agent");
		String filename = FileUtils.encodeDownloadFilename(name, agent);
		String mimeType = ServletActionContext.getServletContext().getMimeType(name);
		ServletActionContext.getResponse().setContentType(mimeType);
		ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename);
		
		String path = ServletActionContext.getServletContext().getRealPath("WEB-INF/template/report1.jrxml");
		Map<String, Object> param = new HashMap<>();
		param.put("compony", "哈哈哈");
		JasperReport report = JasperCompileManager.compileReport(path);
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, param,new JRBeanCollectionDataSource(list));
		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		exporter.exportReport();
		os.close();
		return NONE;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	

}
