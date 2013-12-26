package com.osquare.mydearnest.entity;


import org.springframework.data.annotation.Id;

public class TestPost {
	
	@Id
	private String id;
	private String name;
	private int age;
	
	public TestPost(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	@Override
	public String toString() {
		return "Persion [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
