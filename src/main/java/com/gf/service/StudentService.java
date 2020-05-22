package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class StudentService extends BaseService{
	
	public List<Map<String, Object>> facultyList() {
		String sql = "SELECT  DISTINCT faculty from student";
		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}
	public List<Map<String, Object>> professionList(String faculty) {
		String sql = "SELECT distinct profession from student where faculty =?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
	}
	public List<Map<String, Object>> gradeList(String faculty,String profession) {
		String sql = "SELECT  distinct grade from student where faculty =? and profession=?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
	}
	public List<Map<String, Object>> teamList(String faculty,String profession,String grade) {
		String sql = "SELECT  distinct team from student where faculty =? and profession=? and grade=?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
	}
	public List<Map<String, Object>> getStudentListByTeam(String team) {
		String sql = "SELECT * from student where team =? ";
		NeParamList param = NeParamList.makeParams();
		param.add(team);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}
	public int facultyCount(String faculty) {
		String sql = "SELECT  *  from student where faculty =?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	public int professionCount(String faculty,String profession) {
		String sql = "SELECT  * from student where faculty =? and profession=?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	public int gradeCount(String faculty,String profession,String grade) {
		String sql = "SELECT  * from student where faculty =? and profession=? and grade=?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	
	public int teamCount(String faculty,String profession,String grade,String team) {
		String sql = "SELECT * from student where faculty =? and profession=? and grade=? and team =? ";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
		
	}
	
	public int facultyReportStatusCount(String faculty) {
		String sql = "SELECT  *  from student where faculty =? and reportStatus='已报到'";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	public int professionReportStatusCount(String faculty,String profession) {
		String sql = "SELECT  * from student where faculty =? and profession=? and reportStatus='已报到'";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	public int gradeReportStatusCount(String faculty,String profession,String grade) {
		String sql = "SELECT  * from student where faculty =? and profession=? and grade=? and reportStatus='已报到'";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	
	public int teamReportStatusCount(String faculty,String profession,String grade,String team) {
		String sql = "SELECT * from student where faculty =? and profession=? and grade=? and team =? and reportStatus='已报到'";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
		
	}
	public int AllFacultyCount(String faculty) {
		String sql = "SELECT  *  from student where faculty=? ";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	public int AllCount() {
		String sql = "SELECT  *  from student ";
		List<Map<String, Object>> list = bs.findList(sql);
		return list.size();
	}
	public int AllFacultyReportStatusCount(String faculty) {
		String sql = "SELECT  *  from student where faculty=? and reportStatus='已报到'";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
	}
	public int AllReportStatusCount() {
		String sql = "SELECT  *  from student where  reportStatus='已报到'";
		List<Map<String, Object>> list = bs.findList(sql);
		return list.size();
	}
	
	public List<Map<String, Object>> select_StudentList(Integer page, Integer limit,String faculty,String profession,String grade,String team,String reportStatus,String studentID,String explanation) {
		page = (page - 1) * limit;
		String sql = "select * from student  where  faculty=? and profession=? and grade=? and team=? and reportStatus=? and studentID like ? and faculty=? and status=0  order by reportStatus desc limit " + page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		param.add(reportStatus);
		param.addLike(studentID);
		param.add(explanation);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}
	
	public Integer select_StudentListCount(String faculty,String profession,String grade,String team,String reportStatus,String studentID,String explanation) {
		String sql = "select * from student  where  faculty=? and profession=? and grade=? and team=? and reportStatus=? and studentID like ? and faculty=? and status=0  order by reportStatus desc";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		param.add(reportStatus);
		param.addLike(studentID);
		param.add(explanation);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
		
	}
	
	public List<Map<String, Object>> select_StudentAllList(Integer page, Integer limit,String faculty,String profession,String grade,String team,String reportStatus,String studentID) {
		page = (page - 1) * limit;
		String sql = "select * from student  where  faculty=? and profession=? and grade=? and team=? and reportStatus=? and studentID like ? and status=0  order by reportStatus desc limit " + page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		param.add(reportStatus);
		param.addLike(studentID);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}
	public Integer select_StudentAllListCount(String faculty,String profession,String grade,String team,String reportStatus,String studentID) {
		String sql = "select * from student  where  faculty=? and profession=? and grade=? and team=? and reportStatus=? and studentID like ? and status=0  order by reportStatus desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		param.add(reportStatus);
		param.addLike(studentID);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list.size();
		
	}
	
	public List<Map<String, Object>> StudentAllList(String faculty,String profession,String grade,String team,String reportStatus) {
		String sql = "select * from student  where  faculty=? and profession=? and grade=? and team=? and reportStatus=? and status=0  order by reportStatus desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty);
		param.add(profession);
		param.add(grade);
		param.add(team);
		param.add(reportStatus);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}

}
