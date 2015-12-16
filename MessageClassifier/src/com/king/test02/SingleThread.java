package com.king.test02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.king.test.CSVUtil;
import com.king.test.Content;

public class SingleThread {

	//定义训练集大小
	public static final int trainMsgCnt = 20000;
	
	public static void singleThreadRun(String testPath, String trainPath,
			String outPutPath, String fileName) {

		String encoding = "utf-8";

		File trainFile = new File(trainPath);
		InputStreamReader trainInputStreamReader;

		File testFile = new File(testPath);
		InputStreamReader testInputStreamReader;

		CSVUtil csvUtil = new CSVUtil(outPutPath, fileName);
		try {
			trainInputStreamReader = new InputStreamReader(new FileInputStream(
					trainFile), encoding);
			BufferedReader trainReader = new BufferedReader(
					trainInputStreamReader);

			testInputStreamReader = new InputStreamReader(new FileInputStream(
					testFile), encoding);
			BufferedReader testReader = new BufferedReader(
					testInputStreamReader);

			String testLineText = "";
			String testNumber = "";
			String testText = "";

			String trainLineText = "";
			String trainNumber = "";
			String trainLable = "";
			String trainText = "";
			double semblance = 0;
			
			List<MsgEntity> voteList = new ArrayList<>();
			List<String> trainList = new ArrayList<>();
			List<String> testList = new ArrayList<>();
			

			while ((testLineText = testReader.readLine()) != null) {
				//对test文本分割
				String[] testStrs = testLineText.split("	");
				if (testStrs.length > 1) {
					testNumber = testStrs[0];
					testText = testStrs[1];
					//分词
					testList = KNNClassifier.splitText(testText);
					
					int index = 0 ;
					long startTime = System.currentTimeMillis();
					
					while ((trainLineText = trainReader.readLine()) != null) {
						String[] trainStrs = trainLineText.split("	");
						trainNumber = trainStrs[0];
						trainLable = trainStrs[1];
						trainText = trainStrs[2];
						
						trainList = KNNClassifier.splitText(trainText);
						
						semblance = KNNClassifier.cos(KNNClassifier.count(testList, trainList));
						MsgEntity comingEntity = new MsgEntity(trainLable, trainText, semblance);
						KNNClassifier.compare(voteList, comingEntity);
						
						trainList.clear();
						trainStrs = null;
						
						if (index >trainMsgCnt) {
							break;
						}
						index ++;
					}
					
					String testLable = KNNClassifier.vote(voteList);
					csvUtil.addItem(testNumber, testLable);
					
					long endTime = System.currentTimeMillis();
					System.out.println(testNumber+" : "+testLable+">>>>>time:"+(endTime-startTime));
					
					
					
					testList.clear();
					voteList.clear();
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

			trainInputStreamReader.close();
			testInputStreamReader.close();
			trainReader.close();
			testReader.close();
			csvUtil.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
