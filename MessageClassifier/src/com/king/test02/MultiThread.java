package com.king.test02;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MultiThread implements Runnable{

	private static BufferedReader testReader = null;
	private static BufferedReader trainReader = null;
	
	static{  
        try {  
        	testReader = new BufferedReader(new FileReader("testPath"),512); 
        	trainReader = new BufferedReader(new FileReader("trainPath"),512);
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }
	
	@Override
	public void run() {
	}

}
