package com.king.test02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.king.test.CSVUtil;
import com.king.test.Content;

/**
 * 自定义一个分类器
 * @author king
 *
 */
public class KingClassifier {
	
	public static void kingClassify(String testPath, String trashPath,
			String outPutPath, String fileName,int compareCnt,double expireSem,double percent) {

		String encoding = "utf-8";

		File trashFile = new File(trashPath);
		InputStreamReader trashInputStreamReader;

		File testFile = new File(testPath);
		InputStreamReader testInputStreamReader;

		CSVUtil csvUtil = new CSVUtil(outPutPath, fileName);
		try {
			trashInputStreamReader = new InputStreamReader(new FileInputStream(
					trashFile), encoding);
			BufferedReader trashReader = new BufferedReader(
					trashInputStreamReader);

			testInputStreamReader = new InputStreamReader(new FileInputStream(
					testFile), encoding);
			BufferedReader testReader = new BufferedReader(
					testInputStreamReader);

			String testLineText = "";
			String testNumber = "";
			String testText = "";

			String trashLineText = "";
			String trashText = "";
			double semblance = 0;
			
			List<String> trashList = new ArrayList<>();
			List<String> testList = new ArrayList<>();
			
			//mark，以便reset回到文件头部
			trashReader.mark((int)trashFile.length());

			while ((testLineText = testReader.readLine()) != null) {
				
				
				//对test文本分割
				String[] testStrs = testLineText.split("	");
				if (testStrs.length > 1) {
					testNumber = testStrs[0];
					testText = testStrs[1];
					//分词
					testList = KNNClassifier.splitText(testText);
					
					//记录测试文本和垃圾文本比较了index次
					int index = 0 ;
					//每次开始的时间
					long startTime = System.currentTimeMillis();
					//相似度数组
//					double [] semblanceArray = new double[compareCnt];
					
					int higherCnt = 0;
					int lowerCnt = 0;
					
					while (true) {
						//如果reader指针在文件最后，那么就reset，实现循环
						trashLineText = trashReader.readLine();
						if (trashLineText == null) {
							trashReader.reset();
							trashLineText = trashReader.readLine();
						}
						
						
						String[] trashStrs = trashLineText.split("	");
						
						trashText = trashStrs[2];
						
						trashList = KNNClassifier.splitText(trashText);
						//计算两个文本的余弦相似度
						semblance = KNNClassifier.cos(KNNClassifier.count(testList, trashList));
//						System.out.println(semblance);
//						semblanceArray[index] = semblance;
						
						
						if (semblance >= expireSem) {
							higherCnt++;
						}else{
							lowerCnt++;
						}
						
						trashList.clear();
						trashStrs = null;
						
						//规定比较的次数为compareCnt
						if (index >= compareCnt -1) {
							index = 0;
							break;
						}
						index++;
						
					}				
					String testLable = "";
					//计算一个平均相似度
//					double averageSem = 0;
//					double semSum = 0;
//					for (int i = 0; i < semblanceArray.length; i++) {
//						semSum +=semblanceArray[i];
//					}
//					averageSem = semSum / compareCnt;
//					System.out.println(averageSem);
					
					//如果平均相似度大于或等于我们期望的相似度，那就是垃圾短信
//					if (averageSem >= expireSem) {
//						testLable = Content.BAD;
//					}else{
//						testLable = Content.GOOD;
//					}
					
					if (higherCnt > compareCnt*percent) {
						testLable = Content.BAD;
					}else{
						testLable = Content.GOOD;
					}
					
					//把结果写入csv文件
					csvUtil.addItem(testNumber, testLable);
					//每次结束时间
					long endTime = System.currentTimeMillis();
					System.out.println(testNumber+" : "+testLable+">>>>>time:"+(endTime-startTime)+">>>>>>>>"+"H::"+higherCnt+"**L::"+lowerCnt);
					
					
					
					testList.clear();
					testStrs = null;
					
					//出现短信内容为空时，默认不是垃圾短信
				}else{
					testNumber = testStrs[0];
					csvUtil.addItem(testNumber, Content.GOOD);
					System.out.println(testNumber+" : "+Content.GOOD);
					
					testStrs = null;
					testNumber = null;
				}
			
			}

			//对资源进行关闭操作
			trashInputStreamReader.close();
			testInputStreamReader.close();
			trashReader.close();
			testReader.close();
			csvUtil.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public static void main(String[] args) {
		String testPath = "C:\\Users\\king\\Desktop\\垃圾短信数据\\垃圾短信数据\\test.txt";
		String trashPath = "C:\\Users\\king\\Desktop\\trash.txt";
		//输出文件的路径
		String outPutPath = "C:\\Users\\king\\Desktop\\";
		//输出文件的文件名
		String fileName = "第6次king";
		//比较的次数
		int compareCnt = 200;
		//期望相似度
		double expireSem = 0.07;
		
		double percent = 0.4;
		
		long start = System.currentTimeMillis();
		kingClassify(testPath, trashPath, outPutPath, fileName, compareCnt ,expireSem,percent);
		long end = System.currentTimeMillis();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(new Date(start)));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(new Date(end)));
	}
}
