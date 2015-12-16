package com.king.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSVUtil {

	private File csvFile = null;
	private BufferedWriter csvFileOutputStream = null;

	public CSVUtil(String outPutPath, String fileName) {
		try {
			File file = new File(outPutPath);
			if (!file.exists()) {
				file.mkdir();
			}
			// 定义文件名格式并创建
			csvFile = File.createTempFile(fileName, ".csv",
					new File(outPutPath));
			System.out.println("csvFile：" + csvFile);
			// UTF-8使正确读取分隔符","
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(csvFile), "UTF-8"), 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成为CVS文件
	 * 
	 * @return
	 */
	public void addItem(String key,String value) {
		
		try {
			csvFileOutputStream.write(key + "," + value);
			csvFileOutputStream.newLine();
			csvFileOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			csvFileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public File getCsvFile() {
		return csvFile;
	}

}