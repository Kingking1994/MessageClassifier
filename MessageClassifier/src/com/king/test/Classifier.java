package com.king.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Classifier {
	public static void doClassify(String testPath,
			String trainPath,String outPutPath, String fileName) {
		
		Map<String, Map<String, Double>> result = DataPreProsess.prosess(trainPath);
		Map<String, Double> goodMsg = result.get("good");
		Map<String, Double> badMsg = result.get("bad");
		
		CSVUtil csvUtil = new CSVUtil(outPutPath, fileName);
		
		//下面先要对test的读取和处理，得到的是string，double的键值对
		File file = new File(testPath);
		String encoding = "utf-8";

		if (file.isFile()) {
			try {
				InputStreamReader inputStreamReader = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);

				String lineText = "";
				String number = "";
				String text = "";
				
				try {
					while ((lineText = bufferedReader.readLine()) != null) {
						
						Map<String, Double> testMap = new HashMap<String, Double>();
						List<String> testList = new ArrayList<>();
						
						String[] strs = lineText.split("	");
						if (strs.length > 1) {
							
							number = strs[0];
							text = strs[1];
							// 分词
							StringReader sr = new StringReader(text);
							IKSegmenter ik = new IKSegmenter(sr, true);
							Lexeme lex = null;
							while ((lex = ik.next()) != null) {
								boolean flag = true;
								String word = lex.getLexemeText();
								// 去掉中性词
								for (String neuter : Neuters.neuters) {
									if (word.equals(neuter)) {
										flag = false;
										break;
									}
								}
								if (flag) {
									testList.add(word);
								}
							}
							//得到测试文本处理后的map
							testMap = DataPreProsess.countTest(testList);
							
							
							//得到分类概率
							double pGOOD = calculatePro(goodMsg, testMap);
							double pBAD = calculatePro(badMsg, testMap);
							
							
							//进行比较
							if (pBAD > pGOOD) {
								csvUtil.addItem(number, Content.GOOD);
								System.out.println(number+" : "+Content.GOOD);
							}else{
								csvUtil.addItem(number, Content.BAD);
								System.out.println(number+" : "+Content.BAD);
							}
							sr = null;
							ik = null;
						}else{
							number = strs[0];
							csvUtil.addItem(number, Content.GOOD);
							System.out.println(number+" : "+Content.GOOD);
						}
						
						
						//对资源进行回收
						testMap = null;
						testList = null;
						strs = null;
						number = null;
						text = null;
						
						
					}
					csvUtil.close();
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
		} else {
			System.out.println("不是文件");
		}

	}

	
	public static double calculatePro(Map<String, Double> trainMap,Map<String, Double> testMap){
		double p =1;
		double sum = getSum(trainMap);
		for (Map.Entry<String, Double> testEntity : testMap.entrySet()) {
			for (Map.Entry<String, Double> trainEntity : trainMap.entrySet()) {
				if (testEntity.getKey().equals(trainEntity.getKey())) {
					p *= (testEntity.getValue() / sum );
				}
			}
		}
		return p;
	}
	
	
	public static double getSum(Map<String, Double> trainMap){
		double sum =0;
		for (Map.Entry<String, Double> trainEntity : trainMap.entrySet()) {
			sum +=trainEntity.getValue();
		}
		return sum;
	}
}
