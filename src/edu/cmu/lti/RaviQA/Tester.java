package edu.cmu.lti.RaviQA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class Tester {
	public static void main(String[] args) {
		HashMap<String, ArrayList<String>> kwdArticleMap = Utils.LoadIndex();
		/*
		 * for (String s : kwdArticleMap.keySet()) { System.out.println(s); }
		 */

		// String input = "What move neuron?";
		String input = "What inhibit apoptotic cell death";

		ArrayList<ArrayList<String>> entities = Utils.getEntities(input);
		// System.out.println(x);
		for (String s : entities.get(0)) {
			System.out.println(s);
		}

		System.out.println();

		for (String s : entities.get(1)) {
			System.out.println(s);
		}

		System.out.println("===========");

		ArrayList<Article> candidateArticleList = Utils.getArticle(
				kwdArticleMap, entities);

		for (Article a : candidateArticleList) {
			System.out.println(a.fileName + " : " + a.score);
		}

		System.out.println("===========");

		ArrayList<Sentence> candidateSentenceList = Utils.getCandSentence(
				candidateArticleList, entities);

		for (Sentence s : candidateSentenceList) {
			System.out.format("%f : %s\n", s.score, s.text);
		}

		// findEntityWithMaxFrequency(candidateSentenceList, "human CD34+");
		AnswerProcessor answerProcessor = new AnswerProcessor();
		System.out.println("result is "
				+ AnswerProcessor.findEntityWithMaxFrequency(
						candidateSentenceList, "G-CSF"));

	}

	public static ArrayList<ArrayList<String>> getEntities(String input) {

		String[] words = input.replaceAll("[^a-zA-Z ]", "").toLowerCase()
				.split("\\s+");
		ArrayList<ArrayList<String>> answer = new ArrayList<ArrayList<String>>();
		ArrayList<String> noun = new ArrayList<String>();
		ArrayList<String> verb = new ArrayList<String>();
		answer.add(noun);
		answer.add(verb);

		Collections.addAll(verb, words);
		Collections.addAll(noun, words);
		for (int i = 1; i < words.length - 1; ++i) {
			StringBuilder sb = new StringBuilder().append(words[i]);
			for (int j = i + 1; j < words.length; ++j) {
				sb.append(" ");
				sb.append(words[j]);
				noun.add(sb.toString());
			}
		}
		return answer;
	}
}
