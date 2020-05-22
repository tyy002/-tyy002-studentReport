package com.gf.entity;

import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Student extends DataModel{
	private String studentID;//学号
	private String name;//姓名
	private String sex;//性别
	private String grade;//年级
	private String profession;//专业
	private String team;//班级
	private String faculty;//院系
	private String loginPassword;//登录密码
	private String reportStatus;//报道状态 0 未报到 1 已报到 2待审核
	private String userId;//账号
	
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();
	
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getFaculty() {
		return faculty;
	}
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "Student [studentID=" + studentID + ", name=" + name + ", sex=" + sex + ", grade=" + grade
				+ ", profession=" + profession + ", team=" + team + ", faculty=" + faculty + ", loginPassword="
				+ loginPassword + ", reportStatus=" + reportStatus + ", userId=" + userId + ", kid=" + kid + ", status="
				+ status + ", created=" + created + "]";
	}

	
	
	
	

}
