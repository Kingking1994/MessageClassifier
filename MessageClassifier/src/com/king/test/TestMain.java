package com.king.test;

import java.util.Date;



public class TestMain {
	
	public static void main(String[] args) {
		String testPath = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\test.txt";
		String trainPath = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\train.txt";
		//输出文件的路径
		String outPutPath = "C:\\Users\\king\\Desktop\\";
		//输出文件的文件名
		String fileName = "第三次";
		
		Date start = new Date(System.currentTimeMillis());
		
		Classifier.doClassify(testPath, trainPath,outPutPath,fileName);
		
		Date end = new Date(System.currentTimeMillis());
		
		System.out.println(start);
		System.out.println(end);
	}

}
