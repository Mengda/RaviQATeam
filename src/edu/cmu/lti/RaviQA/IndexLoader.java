package edu.cmu.lti.RaviQA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class IndexLoader {

	public static HashMap<String, ArrayList<String>> Load() {
		HashMap<String, ArrayList<String>> answer = new HashMap<String, ArrayList<String>>();
		File f = new File("./preprocessing/KeyDict");
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while (line != null) {
				String key = line.split("\t")[0];
				String[] fileNames = line.split("\t")[1].split("#");
				ArrayList<String> value = new ArrayList<String>();
				Collections.addAll(value, fileNames);
				answer.put(key, value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}
}
