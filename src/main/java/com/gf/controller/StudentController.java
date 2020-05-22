package com.gf.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.json.JsonTools;
import com.example.demo.util.tree.TreeTools;
import com.gf.config.MyInvocationSecurityMetadataSourceService;
import com.gf.config.SecurityUtils;
import com.gf.entity.Menu;
import com.gf.entity.Role;
import com.gf.entity.Role_Permission;
import com.gf.entity.Student;
import com.gf.entity.User;
import com.gf.service.PermissionService;
import com.gf.service.RoleService;
import com.gf.service.StudentService;
import com.tmsps.ne4spring.LayuiPage;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping("/student")
public class StudentController extends ProjBaseAction{
	
	@Autowired
	private StudentService studentService;
	
	private static final String ROLE_FACULTY="ROLE_FACULTY";
	private static final String ROLE_FACULTY_READ="ROLE_FACULTY_READ";
	
	@RequestMapping("/treeList")
	public String role_add(Model model) {
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		System.out.println(roleName);
		if(ROLE_FACULTY.equals(roleName)||ROLE_FACULTY_READ.equals(roleName)) {
			return  "student/tree";
		}else {
			return  "student/allTree";
		}
	}
	
	@RequestMapping("/getStudentListByTeam")
	public String getStudentListByTeam(Model model,String team) {
		List<Map<String, Object>> studentListByTeam = studentService.getStudentListByTeam(team);
		model.addAttribute("studentList", studentListByTeam);
		return  "student/showStudentList";
	}
	
	
	
	@RequestMapping("/treeFacultyList")
	@ResponseBody
	public String treeList(Model model) {
		User user = SecurityUtils.getUser();
		String faculty = user.getExplanation();
		List<Menu> list=new ArrayList<Menu>();
		int allCount = studentService.AllCount();
		int allReportStatusCount = studentService.AllReportStatusCount();
		Menu menu = new Menu();
		menu.setName("山西农业大学");
		menu.setTitle("山西农业大学"+"("+allReportStatusCount+"/"+allCount+")");
		menu.setStatus(0);
		menu.setId("0");
		menu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
		menu.setAllcount(allCount);
		menu.setReportCount(allReportStatusCount);
		list.add(menu);
			int facultyCount = studentService.facultyCount(faculty);
			int facultyReportStatusCount = studentService.facultyReportStatusCount(faculty);
			Menu facultyMenu = new Menu();
			facultyMenu.setName(faculty);
			facultyMenu.setTitle(faculty+"("+facultyReportStatusCount+"/"+facultyCount+")");
			facultyMenu.setStatus(0);
			facultyMenu.setId(menu.getKid());
			facultyMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
			facultyMenu.setAllcount(facultyCount);
			facultyMenu.setReportCount(facultyReportStatusCount);
			list.add(facultyMenu);
			List<Map<String, Object>> professionList = studentService.professionList(faculty);
			for(Map<String, Object> profession:professionList) {
				int professionCount = studentService.professionCount(faculty,profession.get("profession").toString());
				int professionReportStatusCount = studentService.professionReportStatusCount(faculty,profession.get("profession").toString());
				Menu professionMenu = new Menu();
				professionMenu.setName(profession.get("profession").toString());
				professionMenu.setTitle(profession.get("profession").toString()+"("+professionReportStatusCount+"/"+professionCount+")");
				professionMenu.setStatus(0);
				professionMenu.setId(facultyMenu.getKid());
				professionMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
//				bs.saveObj(professionMenu);
				professionMenu.setAllcount(professionCount);
				professionMenu.setReportCount(professionReportStatusCount);
				list.add(professionMenu);
				List<Map<String, Object>> gradeList = studentService.gradeList(faculty,profession.get("profession").toString());
				for(Map<String, Object> grade:gradeList) {
					int gradeCount = studentService.gradeCount(faculty,profession.get("profession").toString(),grade.get("grade").toString());
					int gradeReportStatusCount = studentService.gradeReportStatusCount(faculty,profession.get("profession").toString(),grade.get("grade").toString());
					Menu gradeMenu = new Menu();
					gradeMenu.setName(grade.get("grade").toString());
					gradeMenu.setTitle(grade.get("grade").toString()+"("+gradeReportStatusCount+"/"+gradeCount+")");
					gradeMenu.setStatus(0);
					gradeMenu.setId(professionMenu.getKid());
					gradeMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
//					bs.saveObj(gradeMenu);
					gradeMenu.setAllcount(gradeCount);
					gradeMenu.setReportCount(gradeReportStatusCount);
					list.add(gradeMenu);
					List<Map<String, Object>> teamList = studentService.teamList(faculty,profession.get("profession").toString(),grade.get("grade").toString());
					for(Map<String, Object> team:teamList) {
						int teamCount = studentService.teamCount(faculty,profession.get("profession").toString(),grade.get("grade").toString(),team.get("team").toString());
						int teamReportStatusCount = studentService.teamReportStatusCount(faculty,profession.get("profession").toString(),grade.get("grade").toString(),team.get("team").toString());
						Menu teamMenu = new Menu();
						teamMenu.setName(team.get("team").toString());
						teamMenu.setTitle(team.get("team").toString()+"("+teamReportStatusCount+"/"+teamCount+")");
						teamMenu.setStatus(0);
						teamMenu.setId(gradeMenu.getKid());
						teamMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
//						bs.saveObj(teamMenu);
						teamMenu.setAllcount(teamCount);
						teamMenu.setReportCount(teamReportStatusCount);
						list.add(teamMenu);
					}
				}
			}
		List<Menu> listTree=buildTree(list,"0");
		String json = JsonTools.toJson(listTree);
//		System.out.println("json"+json);
		return  json;
	}
	
	@RequestMapping("/treeAllList")
	@ResponseBody
	public String treeAllList(Model model) {
		List<Menu> list=new ArrayList<Menu>();
		int allCount = studentService.AllCount();
		int allReportStatusCount = studentService.AllReportStatusCount();
		Menu menu = new Menu();
		menu.setName("山西农业大学");
		menu.setTitle("山西农业大学"+"("+allReportStatusCount+"/"+allCount+")");
		menu.setStatus(0);
		menu.setId("0");
		menu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
		menu.setAllcount(allCount);
		menu.setReportCount(allReportStatusCount);
		list.add(menu);
		
		List<Map<String, Object>> facultyList = studentService.facultyList();
		for(Map<String, Object> faculty:facultyList) {
			int facultyCount = studentService.facultyCount(faculty.get("faculty").toString());
			int facultyReportStatusCount = studentService.facultyReportStatusCount(faculty.get("faculty").toString());
			Menu facultyMenu = new Menu();
			facultyMenu.setName(faculty.get("faculty").toString());
			facultyMenu.setTitle(faculty.get("faculty").toString()+"("+facultyReportStatusCount+"/"+facultyCount+")");
			facultyMenu.setStatus(0);
			facultyMenu.setId(menu.getKid());
			facultyMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
			facultyMenu.setAllcount(facultyCount);
			facultyMenu.setReportCount(facultyReportStatusCount);
			list.add(facultyMenu);
			List<Map<String, Object>> professionList = studentService.professionList(faculty.get("faculty").toString());
			for(Map<String, Object> profession:professionList) {
				int professionCount = studentService.professionCount(faculty.get("faculty").toString(),profession.get("profession").toString());
				int professionReportStatusCount = studentService.professionReportStatusCount(faculty.get("faculty").toString(),profession.get("profession").toString());
				Menu professionMenu = new Menu();
				professionMenu.setName(profession.get("profession").toString());
				professionMenu.setTitle(profession.get("profession").toString()+"("+professionReportStatusCount+"/"+professionCount+")");
				professionMenu.setStatus(0);
				professionMenu.setId(facultyMenu.getKid());
				professionMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
//				bs.saveObj(professionMenu);
				professionMenu.setAllcount(professionCount);
				professionMenu.setReportCount(professionReportStatusCount);
				list.add(professionMenu);
				List<Map<String, Object>> gradeList = studentService.gradeList(faculty.get("faculty").toString(),profession.get("profession").toString());
				for(Map<String, Object> grade:gradeList) {
					int gradeCount = studentService.gradeCount(faculty.get("faculty").toString(),profession.get("profession").toString(),grade.get("grade").toString());
					int gradeReportStatusCount = studentService.gradeReportStatusCount(faculty.get("faculty").toString(),profession.get("profession").toString(),grade.get("grade").toString());
					Menu gradeMenu = new Menu();
					gradeMenu.setName(grade.get("grade").toString());
					gradeMenu.setTitle(grade.get("grade").toString()+"("+gradeReportStatusCount+"/"+gradeCount+")");
					gradeMenu.setStatus(0);
					gradeMenu.setId(professionMenu.getKid());
					gradeMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
//					bs.saveObj(gradeMenu);
					gradeMenu.setAllcount(gradeCount);
					gradeMenu.setReportCount(gradeReportStatusCount);
					list.add(gradeMenu);
					List<Map<String, Object>> teamList = studentService.teamList(faculty.get("faculty").toString(),profession.get("profession").toString(),grade.get("grade").toString());
					for(Map<String, Object> team:teamList) {
						int teamCount = studentService.teamCount(faculty.get("faculty").toString(),profession.get("profession").toString(),grade.get("grade").toString(),team.get("team").toString());
						int teamReportStatusCount = studentService.teamReportStatusCount(faculty.get("faculty").toString(),profession.get("profession").toString(),grade.get("grade").toString(),team.get("team").toString());
						Menu teamMenu = new Menu();
						teamMenu.setName(team.get("team").toString());
						teamMenu.setTitle(team.get("team").toString()+"("+teamReportStatusCount+"/"+teamCount+")");
						teamMenu.setStatus(0);
						teamMenu.setId(gradeMenu.getKid());
						teamMenu.setKid(UUID.randomUUID().toString().replaceAll("-", ""));
//						bs.saveObj(teamMenu);
						teamMenu.setAllcount(teamCount);
						teamMenu.setReportCount(teamReportStatusCount);
						list.add(teamMenu);
					}
				}
			}
		}
		List<Menu> listTree=buildTree(list,"0");
		String json = JsonTools.toJson(listTree);
//		System.out.println("json"+json);
		return  json;
	}
	
	@RequestMapping("/import_execlStudent")
    public String update() {
    	return "student/import_studentDatabase";
    }
	
	
	
	@RequestMapping("/showStudentByStudentID")
    public String showStudentByStudentID(Model model) {
		User user = SecurityUtils.getUser();
		String sql = "select * from student  where userID=? ";
		Student student = bs.findObj(sql, new Object[] { user.getKid() }, Student.class);
		model.addAttribute("student", student);
    	return "student/showStudent";
    }
	
	@RequestMapping("/showProfessionList")
	public String showProfession(String faculty,Model model) {
		List<Map<String, Object>> professionList = studentService.professionList(faculty);
		List<Map<String, Object>> gradeList = studentService.gradeList(faculty,(String)professionList.get(0).get("profession"));
		model.addAttribute("professionList", professionList);
		return "student/professionIndex";
	}
	
	@RequestMapping("/facultyIndex")
	public String facultyIndex(Model model) {
		List<Map<String, Object>> facultyList = studentService.facultyList();
		model.addAttribute("facultyList", facultyList);
		return "student/facultyIndex";
	}
	
	@RequestMapping("/update_reportStatusOne")
	@ResponseBody
	public String update_reportStatusOne(String kid) {
		Student student = bs.findById(kid, Student.class);
		student.setReportStatus("待审核");
		int updateObj = bs.updateObj(student);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	@RequestMapping("/update_reportStatusTwo")
	@ResponseBody
	public String update_reportStatusTwo(String kid) {
		Student student = bs.findById(kid, Student.class);
		student.setReportStatus("已报到");
		int updateObj = bs.updateObj(student);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	
	@RequestMapping("/studentList")
	@ResponseBody
	public LayuiPage<?> studentList(Integer page, Integer limit,String faculty,String profession,String grade,String team,String reportStatus,String studentID) {
		LayuiPage<Student> LayuiPage = new LayuiPage<Student>();
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		List<Map<String, Object>> selectList = null;
		Integer count;
		if("ROLE_FACULTY".equals(roleName)) {
			selectList = studentService.select_StudentList(page,limit,faculty,profession,grade,team,reportStatus,studentID,user.getExplanation());
			count=studentService.select_StudentListCount(faculty,profession,grade,team,reportStatus,studentID,user.getExplanation());
		}else{
			selectList = studentService.select_StudentAllList(page,limit,faculty,profession,grade,team,reportStatus,studentID);
			count=studentService.select_StudentAllListCount(faculty,profession,grade,team,reportStatus,studentID);
		}
		LayuiPage.setCode(0);
		LayuiPage.setData(selectList);
		LayuiPage.setCount(count);
		return LayuiPage;
	}

	@RequestMapping("/index")
	public String list(Model model) {
		List<Map<String, Object>> facultyList = studentService.facultyList();
		model.addAttribute("facultyList", facultyList);
		return "student/list";
	}
	
	@RequestMapping("/professionList")
	@ResponseBody
	public String professionList(String faculty) {
		List<Map<String, Object>> professionList = studentService.professionList(faculty);;
		return  JsonTools.toJson(professionList);
	}
	
	@RequestMapping("/gradeList")
	@ResponseBody
	public String gradeList(String faculty,String profession) {
		List<Map<String, Object>> gradeList = studentService.gradeList(faculty,profession);;
		return  JsonTools.toJson(gradeList);
	}
	
	@RequestMapping("/teamList")
	@ResponseBody
	public String teamList(String faculty,String profession,String grade) {
		List<Map<String, Object>> teamList = studentService.teamList(faculty,profession,grade);;
		return  JsonTools.toJson(teamList);
	}
	
	@RequestMapping("/resetPasswordUserByStudentID")
	@ResponseBody
	public String resetPasswordUserById(String studentID) {
		String code;
		String sql = "select * from user  where username=? ";
		User user = bs.findObj(sql, new Object[] { studentID }, User.class);
		user.setPassword(DigestUtils.md5DigestAsHex("123456".toString().getBytes()));
		int saveObj = bs.updateObj(user);
		if(saveObj==1) {
			code="200";
		}else {
			code="500";
		}
		return JsonTools.toJson(code);
	}
	
	
	
	
	public static List<Menu> buildTree(List<Menu> list,String parentId){
		 List<Menu> menus=new ArrayList<Menu>();
		       for (Menu menu : list) {  
		   
		           String menuId = menu.getKid();
		           String pid = menu.getId();
		           if (parentId.equals(pid)) {  
		               List<Menu> menuLists = buildTree(list, menuId);  
		               menu.setChildren(menuLists);
		               menus.add(menu);  
		           }  
		       }  
		      
		 return menus;
	}
	
	
	
}
