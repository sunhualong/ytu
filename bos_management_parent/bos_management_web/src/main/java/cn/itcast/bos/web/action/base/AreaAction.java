package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

public class AreaAction extends CommonAction<Area> {

	@Autowired
	private AreaService areaService;

	
	private File xlsFile;
	private String contentType;
	private String filename;
	
	/**
     * 添加区域功能
     * @return
     */
    @Action(value="areaAction_save",results={
            @Result(name="success",type="redirect",location="/pages/base/area.html"),
            @Result(name="input",type="redirect",location="/flag.html")})
    public String save(){
        //查询所有的区域
        List<Area> listArea=areaService.findAll();
        //获取所有区域的id
        int flag=0;
        for(Area area : listArea){
            if(model.getDistrict().equals(area.getDistrict()) && model.getCity().equals(area.getCity()) && model.getProvince().equals(area.getProvince())){
                flag=1;
            }
            
        }
        if(flag==0){
            String id = UUID.randomUUID().toString();
            model.setId(id);
            //获得区域的省市区为其添加城市城市简码和城市编码
            //赋值城市简码
            String province  =model.getProvince();
            String city=model.getCity();
            String district=model.getDistrict();
            
            
            //先把最后一个字去掉
            //赋值城市编码
            province.substring(0, province.length()-1);
            city.substring(0, city.length()-1);
            district.substring(0, district.length()-1);
            
            String citycode=PinYin4jUtils.hanziToPinyin(city.substring(0, city.length()-1),"");
            model.setCitycode(citycode);
            
            //赋值城市简码
            String temp = province.substring(0,province.length()-1)+city.substring(0, city.length()-1)+district.substring(0, district.length()-1);
             String[] shortcodearr=PinYin4jUtils.getHeadByString(temp);
             String shortcode=StringUtils.join(shortcodearr);
             model.setShortcode(shortcode);
            
            areaService.saveOne(model);
            return SUCCESS;
        }else{
            return INPUT;
        }
    }
    
  //通过属性驱动获取页面传过来的数据
    private String deleId;
    
    
    public void setDeleId(String deleId) {
        this.deleId = deleId;
    }
    
    /**
     * 区域删除
     * @return
     */
    @Action(value="area_deleteId",results={@Result(name="success",type="redirect",location="/pages/base/area.html")})
    public String deleteById(){
        //获取页面传过来的数据
        areaService.deleteId(deleId);
        return SUCCESS;
    }


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
		
		String path = ServletActionContext.getServletContext().getRealPath("WEB-INF/template/area2.jrxml");
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
