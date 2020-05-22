package com.gf.entity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Menu extends DataModel{
	
	 private String name;
	 private String title;
	 private String id;
	 private List<Menu> children;
	 private Integer reportCount;
	 private Integer allcount;
	 
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Menu> getChildren() {
		return children;
	}
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	
	public Integer getReportCount() {
		return reportCount;
	}
	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}
	public Integer getAllcount() {
		return allcount;
	}
	public void setAllcount(Integer allcount) {
		this.allcount = allcount;
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
		return "Menu [name=" + name + ", title=" + title + ", id=" + id + ", children=" + children + ", reportCount="
				+ reportCount + ", allcount=" + allcount + ", kid=" + kid + ", status=" + status + ", created="
				+ created + "]";
	}
	public static void main(String[] args) {
	 List<Menu> treeMenu=new ArrayList<Menu>();
	 
	 List<Menu> list=new ArrayList<Menu>();
	 Menu menu1=new Menu();
	 menu1.setKid("1");
	 menu1.setTitle("父级1");
	 menu1.setId("0");
	 Menu menu2=new Menu();
	 menu2.setKid("2");
	 menu2.setTitle("父级2");
	 menu2.setId("0");
	 Menu menu1_1=new Menu();
	 menu1_1.setKid("3");
	 menu1_1.setTitle("子级1_1");
	 menu1_1.setId("1");
	 Menu menu1_2=new Menu();
	 menu1_2.setKid("4");
	 menu1_2.setTitle("子级1_2");
	 menu1_2.setId("1");
	 Menu menu1_2_1=new Menu();
	 menu1_2_1.setKid("5");
	 menu1_2_1.setTitle("子级1_2_1");
	 menu1_2_1.setId("4");
	 list.add(menu1);
	 list.add(menu2);
	 list.add(menu1_1);
	 list.add(menu1_2);
	 list.add(menu1_2_1);
	 
	 List<Menu> listTree=buildTree(list,"0");
	 
	 String trssJson=JSON.toJSONString(listTree);
	 System.out.println(trssJson);
	 
	 }
	 
	 public static List<Menu> buildTree(List<Menu> list,String parentId){
	 List<Menu> menus=new ArrayList<Menu>();
	       for (Menu menu : list) {  
	   
	    	   String menuId = menu.getKid();
	    	   String pid = menu.getId();
	           if (parentId == pid) {  
	               List<Menu> menuLists = buildTree(list, menuId);  
	               
	               menu.setChildren(menuLists);
	               menus.add(menu);  
	           }  
	       }  
	      
	 return menus;
	 }
}
