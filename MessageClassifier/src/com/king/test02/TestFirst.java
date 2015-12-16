package com.king.test02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.king.test.Content;

/**
 * 从原训练集文件分出垃圾短信文件
 * 
 * @author king
 * 
 */
public class TestFirst {

	public static void main(String[] args) {
		String trainPath = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\train.txt";
		String trashPath ="C:\\Users\\king\\Desktop\\trash.txt";

		String encoding = "utf-8";
		
		InputStreamReader trainInputStreamReader = null;
		BufferedReader trainReader = null;
		
		OutputStreamWriter trashOutputStreamWriter = null;
		BufferedWriter trashWriter = null;
		
		try {
			File trainFile = new File(trainPath);
			trainInputStreamReader = new InputStreamReader(new FileInputStream(
					trainFile), encoding);
			trainReader = new BufferedReader(
					trainInputStreamReader);
			
			File trashFile = new File(trashPath);
			if (!trashFile.exists()) {
				trashFile.createNewFile();
			}
			trashOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(
					trashFile,true), encoding);//如果追加方式用true
			trashWriter = new BufferedWriter(trashOutputStreamWriter);
			
			String textLine = "";
			int Cnt = 0;
			
			while ((textLine = trainReader.readLine()) != null) {
				//对test文本分割
				String[] strs = textLine.split("	");
				String lable = strs[1];
				if (lable.equals(Content.BAD)) {
					Cnt ++;
					trashWriter.write(textLine);
					trashWriter.newLine();
					trashWriter.flush();//一定要刷新，不然就会出现异常
					System.out.println(textLine);
				}
			}
			System.out.println("垃圾短信数量："+Cnt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (trainInputStreamReader != null) {
				try {
					trainInputStreamReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (trainReader != null) {
				try {
					trainReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (trashOutputStreamWriter != null) {
				try {
					trashOutputStreamWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (trashWriter != null) {
				try {
					trashWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
