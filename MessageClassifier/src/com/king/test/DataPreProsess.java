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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class DataPreProsess {
	public static final double prosessNum = 20000;

	public static Map<String, Map<String, Double>> prosess(String path) {

		

		File file = new File(path);
		String encoding = "utf-8";

		Map<String, Map<String, Double>> result = new ConcurrentHashMap<>();
		Map<String, Double> goodMsg = new HashMap<String, Double>();
		Map<String, Double> badMsg = new HashMap<String, Double>();
		List<String> goodList = new ArrayList<>();
		List<String> badtList = new ArrayList<>();

		if (file.isFile()) {
			try {
				InputStreamReader inputStreamReader = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);

				String lineText = "";
				String number = "";
				String lable = "";
				String text = "";

				int i = 0;

				try {
					while ((lineText = bufferedReader.readLine()) != null) {
						String[] strs = lineText.split("	");
						number = strs[0];
						lable = strs[1];
						text = strs[2];
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
								if (lable.equals(Content.GOOD)) {
									goodList.add(word);
								} else {
									badtList.add(word);
								}
							}

						}

						System.out.println("成功处理训练集：第" + i + "条");
						// 控制训练集大小
						if (i >= prosessNum) {
							break;
						}
						i++;
					}
					System.out.println("开始统计词频");
					// 统计词频(这里用时最长)
					goodMsg = countTrain(goodList);
					badMsg = countTrain(badtList);

					result.put("good", goodMsg);
					result.put("bad", badMsg);
					System.out.println("统计完成");

					bufferedReader.close();
					inputStreamReader.close();
					return result;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			System.out.println("不是文件");
			return null;
		}
	}

	/**
	 * 统计词频
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, Double> countTrain(List<String> list) {
		// 阀值
		int minHold = 1;
		int maxHold = 200;
		Set<String> set = new TreeSet<String>();
		for (String str : list) {
			set.add(str);
		}

		Iterator<String> ite = set.iterator();
		Map<String, Double> map = new HashMap<>();

		while (ite.hasNext()) {
			String word = ite.next();
			double count = 0;
			boolean flag = true;
			for (String str : list) {
				if (str.equals(word)) {
					count++;
					// 词频大于maxHold就跳出循环，不再计算
					if (count > maxHold) {
						flag = false;
						break;
					}
				}
			}
			// 对词频进行过滤，
			if (count >= minHold && flag) {
				System.out.println(word + ":" + count);
				map.put(word, count);
			}
		}

		return map;
	}

	public static Map<String, Double> countTest(List<String> list) {
		// 阀值
		Set<String> set = new TreeSet<String>();
		for (String str : list) {
			set.add(str);
		}

		Iterator<String> ite = set.iterator();
		Map<String, Double> map = new HashMap<>();

		while (ite.hasNext()) {
			String word = ite.next();
			double count = 0;
			for (String str : list) {
				if (str.equals(word)) {
					count++;
				}
			}
			// 对词频进行过滤，
			map.put(word, count);

		}

		return map;
	}
}
