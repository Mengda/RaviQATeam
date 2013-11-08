package edu.cmu.lti.RaviQA;

import java.util.*;

public class Utils {
	static public ArrayList<ArrayList<String>> getEntities(String input) {

		return null;
	}

	static public HashMap<String, ArrayList<String>> LoadIndex() {

		return null;
	}

	public static ArrayList<Article> getArticle(
			HashMap<String, ArrayList<String>> kwdArticalMap,
			ArrayList<ArrayList<String>> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ArrayList<Sentence> getCandSentence(
			ArrayList<Article> candidateArticalList,
			ArrayList<ArrayList<String>> entities) {
		try {
			return ArticleProcessor.getCandSentence(candidateArticalList, entities);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Candidate> getCandAns(
			ArrayList<Sentence> candidateSentenceList,
			ArrayList<ArrayList<String>> entities) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static ArrayList<String> GetSynonym(String word){
		ArrayList<String> answer = new ArrayList<String>();
		
		answer.add(word);
		// TODO add synonym dictionary.
		
		return answer;
		
	}
}
