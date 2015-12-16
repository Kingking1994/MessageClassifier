package com.king.test02;

public class MsgEntity {

	private String lable;
	private String text;
	private double semblance;
	
	
	
	public MsgEntity(String lable, String text, double semblance) {
		super();
		this.lable = lable;
		this.text = text;
		this.semblance = semblance;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public double getSemblance() {
		return semblance;
	}
	public void setSemblance(double semblance) {
		this.semblance = semblance;
	}
	
}
