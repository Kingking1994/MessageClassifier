package com.king.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文本文件读取器
 * @author king
 *
 */
public class Test {
	
	public static void fileRead(String path){
		File file = new File(path);
		String encoding = "utf-8";
		if (file.isFile()) {
			try {
				InputStreamReader inputStreamReader = new InputStreamReader(
						new FileInputStream(file),encoding);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String lineText = "";
				int i =0;
				try {
					while ((lineText = bufferedReader.readLine()) != null) {
						System.out.println(lineText);
						String[] strs = lineText.split("	");
						for (String string : strs) {
							System.out.println(string);
						}
						if (i >= 1000) {
//							break;
						}
						i++;
					}
					bufferedReader.close();
					inputStreamReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
//		String path = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\test.txt";
//		Test.fileRead(path);
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("123456", "1");
//		map.put("123426", "0");
//		map.put("122456", "1");
//		map.put("125426", "0");
//		map.put("126456", "1");
//		map.put("127426", "0");
//		map.put("128456", "1");
//		map.put("129426", "0");
//		CSVUtil.createCSVFile(map, "C:\\Users\\king\\Desktop\\", "test");
	}
}
