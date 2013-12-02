package edu.cmu.lti.RaviQA;

import java.util.*;

/**
 * 
 * @author mengdayang
 * 
 */
public class Utils {
	// returnvalue.get(0) = noun phrase
	// returnvalue.get(1) = verb
	static public ArrayList<ArrayList<String>> getEntities(String input, String verb) {
		// Using a silly getEntities!
		System.out.println("Using a silly getEntities!");
		return Tester.getEntities(input,verb);
		//return null;
	}

	static public HashMap<String, ArrayList<String>> LoadIndex() {
		return IndexLoader.Load();
	}

	public static ArrayList<Article> getArticle(
			HashMap<String, ArrayList<String>> kwdArticalMap,
			ArrayList<ArrayList<String>> entities) {
		return ArticleProcessor.getArticle(kwdArticalMap, entities);
	}

	public static ArrayList<Sentence> getCandSentence(
			ArrayList<Article> candidateArticalList,
			ArrayList<ArrayList<String>> entities) {
		try {
			return ArticleProcessor.getCandSentence(candidateArticalList,
					entities);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Candidate> getCandAns(
			ArrayList<Sentence> candidateSentenceList,
			ArrayList<ArrayList<String>> entities) {
		return null;
	}

	public static ArrayList<String> GetSynonym(String word) {
		ArrayList<String> answer = new ArrayList<String>();

		answer.add(word);
		// TODO add synonym dictionary.
		return answer;

	}
}
