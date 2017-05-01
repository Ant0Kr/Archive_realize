package com.epam.archive.models;

public class Person {
	private String surname;
	private String name;
	private String fathername;
	private String phone;
	private String email;
	private String nameJob;
	private String experienceJob;
	
	public Person(String surname,String name,String fathername,
			String phone,String email,String nameJob,String experienceJob){
		this.surname = surname;
		this.name = name;
		this.fathername = fathername;
		this.phone = phone;
		this.email = email;
		this.nameJob = nameJob;
		this.experienceJob = experienceJob;
	}
	
	public void setValues(String surname,String name,String fathername,
			String phone,String email,String nameJob,String experienceJob){
		this.surname = surname;
		this.name = name;
		this.fathername = fathername;
		this.phone = phone;
		this.email = email;
		this.nameJob = nameJob;
		this.experienceJob = experienceJob;
	}
	
	public Person(Person person){
		this.surname = person.getSurname();
		this.name = person.getName();
		this.fathername = person.getFathername();
		this.phone = person.getPhone();
		this.email = person.getEMail();
		this.nameJob = person.getNameJob();
		this.experienceJob = person.getExperienceJob();
	}
	
	public Person() {}

	public String getSurname(){
		return surname;
	}
	
	public String getName(){
		return name;
	}
	
	public String getFathername(){
		return fathername;
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getEMail(){
		return email;
	}
	
	public String getNameJob(){
		return nameJob;
	}
	
	public String getExperienceJob(){
		return experienceJob;
	}
	
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setFathername(String fathername){
		this.fathername = fathername;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public void setEMail(String email){
		this.email = email;
	}
	
	public void setNameJob(String nameJob){
		this.nameJob = nameJob;
	}
	
	public void setExperienceJob(String experienceJob){
		this.experienceJob = experienceJob;
	}
}
