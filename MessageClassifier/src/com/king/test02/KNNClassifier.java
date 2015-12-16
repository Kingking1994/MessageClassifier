package com.king.test02;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
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

import com.king.test.Content;
import com.king.test.Neuters;

public class KNNClassifier {
	public static final int contentK = 15;
	
	public static final int threadCnt = 1;
	
	public static final int testMsgCnt = 200000;
	
	
	
	
	/**
	 * 分词
	 * 原始短信文本
	 * @param text
	 *单词队列
	 * @return
	 */
	public static List<String> splitText(String text){
		List<String> list = new ArrayList<>();
		StringReader sr = new StringReader(text);
		IKSegmenter ik = new IKSegmenter(sr, true);
		Lexeme lex = null;
		try {
			while ((lex = ik.next()) != null) {
				String word = lex.getLexemeText();
//				//去掉中性词
//				for (String string : Neuters.neuters) {
//					if (!word.equals(string)) {
						list.add(word);
//					}
//				}			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return list;
	}
	/**
	 * 统计词频
	 * @param testList
	 * @param trainList
	 * @return 返回文本向量
	 */
	public static Map<String, Map<String, Double>> count(List<String> testList,List<String> trainList){
		
		Map<String, Map<String, Double>> result = new ConcurrentHashMap<>();
		
		//整合两个list的词
		Set<String> set = new TreeSet<String>();
		for (String str : testList) {
			set.add(str);
		}
		for (String str : trainList) {
			set.add(str);
		}
		
		Iterator<String> testIte = set.iterator();
		//对test进行词频统计
		Map<String, Double> testMap = new HashMap<>();		
		while (testIte.hasNext()) {
			String word = testIte.next();
			double count = 0;
			for (String str : testList) {
				if (str.equals(word)) {
					count++;
				}
			}
			testMap.put(word, count);
		}
		Iterator<String> trainIte = set.iterator();
		//对train进行词频统计
        Map<String, Double> trainMap = new HashMap<>();		
		while (trainIte.hasNext()) {
			String word = trainIte.next();
			double count = 0;
			for (String str : trainList) {
				if (str.equals(word)) {
					count++;
				}
			}
			trainMap.put(word, count);
		}
			
		result.put("test", testMap);
		result.put("train", trainMap);
		
		
		return result;
	}
	
	/**
	 * 计算余弦相似度
	 * @param map
	 * @return
	 */
	public static double cos(Map<String, Map<String, Double>> map){
		Map<String, Double> testMap = map.get("test");
		Map<String, Double> trainMap = map.get("train");
		
		
		
		double teXtr = 0;
		double teXte = 0;
		double trXtr = 0;
		
		boolean flag = true;
		
		for (Map.Entry<String, Double> testEntity : testMap.entrySet()) {
			for (Map.Entry<String, Double> trainEntity : trainMap.entrySet()){
				//计算trXtr
				if (flag) {
					trXtr += trainEntity.getValue()*trainEntity.getValue();
				}
				//计算teXtr
				if (trainEntity.getKey().equals(testEntity.getKey())) {
					teXtr += trainEntity.getValue()*testEntity.getValue();
				}
			}
			//防止多次计算trXtr
			flag = false;
			//计算teXte
			teXte += testEntity.getValue()*testEntity.getValue();
		}
		
		double semblance = teXtr / Math.sqrt(trXtr * teXte);

		return semblance;
	}
	/**
	 * 在投票队列里找到最小相似度的短信的下标
	 * @param voteList
	 * @return
	 */
	public static int getMinMsgEntity(List<MsgEntity> voteList){
		int minIndex = 0;
		double minSemblance = 10;
		for (int i = 0; i < voteList.size(); i++) {
			if (voteList.get(i).getSemblance() < minSemblance) {
				minSemblance = voteList.get(i).getSemblance();
				minIndex = i;
			}
		}
		
		return minIndex;
	}
	/**
	 * 
	 * @param voteList
	 * @param comingEntity
	 */
	public static void compare(List<MsgEntity> voteList,MsgEntity comingEntity){
		if (voteList.size() < contentK) {
			voteList.add(comingEntity);
		}else{
			int minIndex = getMinMsgEntity(voteList);
			MsgEntity minMsgEntity = voteList.get(minIndex);
			if (minMsgEntity.getSemblance() < comingEntity.getSemblance()) {
				voteList.remove(minIndex);
				voteList.add(minIndex, comingEntity);
			}

		}
	}
	
	/**
	 * 投票
	 * @param voteList
	 * @return
	 */
	public static String vote(List<MsgEntity> voteList){
		int goodVotedCnt = 0;
		int badVotedCnt = 0;
		for (MsgEntity msgEntity : voteList) {
			if (msgEntity.getLable().equals(Content.GOOD)) {
				goodVotedCnt++;
			}else{
				badVotedCnt++;
			}
		}
		if (goodVotedCnt > badVotedCnt) {
			return Content.GOOD;
		}else{
			return Content.BAD;
		}
	}
	
	
}
