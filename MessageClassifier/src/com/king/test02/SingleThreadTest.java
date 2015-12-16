package com.king.test02;

public class SingleThreadTest {
	public static void main(String[] args) {
		
		String testPath = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\test.txt";
		String trainPath = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\train.txt";
		//输出文件的路径
		String outPutPath = "C:\\Users\\king\\Desktop\\";
		//输出文件的文件名
		String fileName = "第4次knn";
		
		SingleThread.singleThreadRun(testPath, trainPath, outPutPath, fileName);
	}
}
