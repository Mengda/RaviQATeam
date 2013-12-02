package edu.cmu.lti.RaviQA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.common.base.*;;

//import com.google.common.base.CharMatcher;


/**
 * 
 * @author mengdayang
 * 
 */
public class IndexLoader {

	public static HashMap<String, ArrayList<String>> Load() {
		HashMap<String, ArrayList<String>> answer = new HashMap<String, ArrayList<String>>();
		File f = new File("../../preprocessing/KeyDict");
		System.out.println(System.getProperty("user.dir"));
		File directory = new File("");//设定为当前文件夹
		try{
		    System.out.println(directory.getCanonicalPath());//获取标准的路径
		    System.out.println(directory.getAbsolutePath());//获取绝对路径
		}catch(Exception e){}
		//File f = new File("C:/Users/Eltshan/git/raviQAProject/RaviQATeam/preprocessing/KeyDict");
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while (line != null) {
				String[] components = line.split("\t");
				if (components.length!=2) {
					//System.err.println("KeyDict Format Error! line = \"" + line + "\"");
					line = br.readLine();
					continue;
				}
				
				if(!CharMatcher.ASCII.matchesAllOf(line)){
					//System.err.println("Line contains non-ASCII characters! line = \"" + line + "\"");
					line = br.readLine();
					continue;
				}
				
				String key = components[0].toLowerCase();
				if(!answer.containsKey(key)){
					answer.put(key, new ArrayList<String>());
				}
				
				String[] fileNames = components[1].split("#");
				ArrayList<String> value = answer.get(key);
				Collections.addAll(value, fileNames);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}
}
