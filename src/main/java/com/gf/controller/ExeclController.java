package com.gf.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.FileUtil;
import com.example.demo.util.json.JsonTools;
import com.gf.entity.Role;
import com.gf.entity.Student;
import com.gf.entity.User;
import com.gf.entity.User_Role;
import com.gf.service.StudentService;

@Controller
@RequestMapping("/execl")
public class ExeclController extends ProjBaseAction{
	
	
	 
	@Value("${upload.path}")
	private String uploadPath;
	@Autowired
	private StudentService studentService;
	
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String ROLE_STUDENT = "ROLE_STUDENT";
    private final static String STUDENT = "学生";
	
    //导入数据
    @RequestMapping(value = "/import_execlStudent")
    public String import_execlOne(@RequestParam(value="file", required=false) MultipartFile file,HttpServletRequest request,Model model) throws Exception{
		boolean checkFile = checkFile(file);
		String result;
		if(checkFile) {
    		String originalFilename = file.getOriginalFilename();
    		String suffix = file.getOriginalFilename().substring(originalFilename.lastIndexOf("."));
    		String filename = System.currentTimeMillis() + suffix;
    		String pathname= FileUtil.getPath()+filename;
    		String fullPath = uploadPath + pathname;
    		FileUtil.saveFile(file, fullPath);
    		long startTime = new Date().getTime();
    		Map<String, Object> map = execlToList(new File(fullPath),suffix);
    		List<Student> execlToList = (List<Student>) map.get("data");
    		int allCount =(int) map.get("allCount");
    		int nullCount =(int) map.get("nullCount");
    		int repeatCount =(int) map.get("repeatCount");
    		String sql = "select * from role  where name=? ";
    		Role role = bs.findObj(sql, new Object[] { ROLE_STUDENT }, Role.class);
    		if(execlToList.size()!=0) {
    			User user;
    			User_Role userRoles;
    			int saveObj;
    			for(int i=0;i<execlToList.size();i++) {
    				user = new User();
    				user.setUsername(execlToList.get(i).getStudentID());
    				user.setPassword(DigestUtils.md5DigestAsHex("123456".toString().getBytes()));
    				user.setExplanation(STUDENT);
    				user.setStatus(0);
    				bs.saveObj(user);
    				userRoles = new User_Role();
    				userRoles.setUser_id(user.getKid());
    				userRoles.setRole_id(role.getKid());
    				userRoles.setStatus(0);
    				saveObj = bs.saveObj(userRoles);
    				if(saveObj==1) {
    					execlToList.get(i).setStatus(0);
    					execlToList.get(i).setLoginPassword("123456");
    					execlToList.get(i).setReportStatus("未报到"); 
    					execlToList.get(i).setUserId(user.getKid());
    					bs.saveObj(execlToList.get(i));
    				}
    			}
    		}
    		FileUtil.deleteFile(fullPath);
    		long endTime = new Date().getTime();
    		long time=endTime-startTime;
    		long hour=(time/(60*60*1000));
		    long min=((time/(60*1000))-hour*60);
		    long s=(time/1000-hour*60*60-min*60);
		     result="共有"+(allCount-nullCount)+"条数据需要导入,成功导入"+execlToList.size()+"条数据,其中"+repeatCount+"条重复,共用时"+hour+"小时"+min+"分"+s+"秒";
		}else {
			 result="请选择需要导入数据的文件！";
		}
		model.addAttribute("result", result);
		return "/student/import_studentDatabase";
    }
    
    
    public Map<String, Object> execlToList(File file,String suffix){
    	Map<String, Object> hashMap = new HashMap<String,Object>();
    	List<Student> list = new ArrayList<Student>();
    	Student student=null;
    	Workbook wb = null;
    	try {
    		FileInputStream fis = new FileInputStream(file);
			if(suffix.equals(".xls")) {
				 wb = new HSSFWorkbook(fis);
			}else if(suffix.equals(".xlsx")) {
				wb =new XSSFWorkbook(fis);
			}
			int allCount = 0;//共需要导入的
			int nullCount = 0;//该行为空的
			int repeatCount = 0;//重复的
			int numberOfSheets = wb.getNumberOfSheets();//获取所有的sheet
			for(int j=0;j<numberOfSheets;j++) {
				Sheet sheet = wb.getSheetAt(j);
				allCount+=sheet.getLastRowNum();
				if(sheet!=null) {
					for(int i=1;i<sheet.getLastRowNum()+1;i++) {
						Row row = sheet.getRow(i);
						if(row==null) {
							nullCount++;
							continue;
						}
						String studentID = new BigDecimal(row.getCell(1).toString()).toPlainString();
						String sql = "select * from student  where studentID=? ";
						Student result = bs.findObj(sql, new Object[] { studentID }, Student.class);
						if(result!=null) {
							repeatCount++;
							continue;
						}
						String name = String.valueOf(row.getCell(2).getStringCellValue());
						String sex = String.valueOf(row.getCell(3).getStringCellValue());
						DecimalFormat df = new DecimalFormat("0");
						String grade = String.valueOf(df.format(row.getCell(4).getNumericCellValue()));
					//	String grade = String.valueOf(row.getCell(4).getStringCellValue());
						String profession = String.valueOf(row.getCell(5).getStringCellValue());
						String team = String.valueOf(row.getCell(6).getStringCellValue());
						String faculty = String.valueOf(row.getCell(7).getStringCellValue());
						
						student=new Student();
						student.setStudentID(studentID);
						student.setName(name);
						student.setSex(sex);
						student.setGrade(grade);
						student.setProfession(profession);
						student.setTeam(team);
						student.setFaculty(faculty);
						list.add(student);
					}
				}
			}
			
			hashMap.put("data",list);
			hashMap.put("allCount",allCount);
			hashMap.put("nullCount",nullCount);
			hashMap.put("repeatCount",repeatCount);
			
		} catch (FileNotFoundException e) {
			logger.warn("没有文件！");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashMap;
    }
    

    public boolean checkFile(MultipartFile file){
    	boolean st=false;
        //判断文件是否存在
        if(null == file){
            return st;
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
        	 return st;
        }
		return true;
    }

    
    @RequestMapping(value = "/export_studentExcel")
    @ResponseBody
    public String export_employmentExcelCount(HttpServletResponse response,String faculty,String profession,String grade,String team,String reportStatus) throws Exception{
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	HSSFSheet sheet = workbook.createSheet("sheet1");
    	createTitleCount(workbook,sheet);
    	//新增数据行，并且设置单元格数据
//    	int rowNum=1;
//    	long ID1=20171000111L;
//    	for (int i = 0; i < 1000; i++) {
//    		HSSFRow row = sheet.createRow(rowNum);
//    		row.createCell(0).setCellValue(i+1+"");
//    		row.createCell(1).setCellValue(ID1+i);
//    		row.createCell(2).setCellValue("张飞"+i);
//    		row.createCell(3).setCellValue("男");
//    		row.createCell(4).setCellValue("2017");
//    		row.createCell(5).setCellValue("计算机");
//    		row.createCell(6).setCellValue("计科1701");
//    		row.createCell(7).setCellValue("信息学院");
//    		rowNum++;
//    	}
    	int rowNum=1;
    	List<Map<String, Object>> studentAllList = studentService.StudentAllList(faculty,profession,grade,team,reportStatus);
    	for (int i = 0; i < studentAllList.size(); i++) {
    		HSSFRow row = sheet.createRow(rowNum);
    		row.createCell(0).setCellValue(i+1+"");
    		row.createCell(1).setCellValue(studentAllList.get(i).get("studentID")+"");
    		row.createCell(2).setCellValue(studentAllList.get(i).get("name")+"");
    		row.createCell(3).setCellValue(studentAllList.get(i).get("sex")+"");
    		row.createCell(4).setCellValue(studentAllList.get(i).get("grade")+"");
    		row.createCell(5).setCellValue(studentAllList.get(i).get("profession")+"");
    		row.createCell(6).setCellValue(studentAllList.get(i).get("team")+"");
    		row.createCell(7).setCellValue(studentAllList.get(i).get("faculty")+"");
    		row.createCell(8).setCellValue(studentAllList.get(i).get("reportStatus")+"");
    		rowNum++;
    	}
    	String fileName =faculty+profession+grade+team+reportStatus+"学生报到数据.xls";
    	//生成excel文件
    	buildExcelFile(fileName, workbook);
    	//浏览器下载excel
    	buildExcelDocument(fileName,workbook,response);
    	return JsonTools.toJson("200");
    }
    
    //表头
    public void createTitleCount(HSSFWorkbook workbook,HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,22*256);
        sheet.setColumnWidth(2,22*256);
        sheet.setColumnWidth(3,22*256);
        sheet.setColumnWidth(4,22*256);
        sheet.setColumnWidth(5,22*256);
        sheet.setColumnWidth(6,22*256);
        sheet.setColumnWidth(7,22*256);
        sheet.setColumnWidth(8,22*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("编号");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("学号");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("性别");
        cell.setCellStyle(style);
        
        cell = row.createCell(4);
        cell.setCellValue("年级");
        cell.setCellStyle(style);
        
        cell = row.createCell(5);
        cell.setCellValue("专业");
        cell.setCellStyle(style);
        
        cell = row.createCell(6);
        cell.setCellValue("班级");
        cell.setCellStyle(style);
        
        cell = row.createCell(7);
        cell.setCellValue("所属学院");
        cell.setCellStyle(style);
        
        cell = row.createCell(8);
        cell.setCellValue("报到状态");
        cell.setCellStyle(style);
        
    }
    
    //生成excel文件
    public void buildExcelFile(String filename,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    public void buildExcelDocument(String filename,HSSFWorkbook workbook,HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
    

}
