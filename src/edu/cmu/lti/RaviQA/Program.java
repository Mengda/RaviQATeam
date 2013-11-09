package edu.cmu.lti.RaviQA;

import java.util.*;
/**
 * 
 * @author mengdayang
 *
 */
public class Program {
	public static void main(String[] args) {
		HashMap<String, ArrayList<String>> kwdArticalMap = Utils.LoadIndex();

		// Repeat
		String input = System.console().readLine();
		ArrayList<ArrayList<String>> entities = Utils.getEntities(input);
		ArrayList<Article> candidateArticalList = Utils.getArticle(
				kwdArticalMap, entities);
		ArrayList<Sentence> candidateSentenceList = Utils.getCandSentence(
				candidateArticalList, entities);
		ArrayList<Candidate> candidateAnswerList = Utils.getCandAns(
				candidateSentenceList, entities);
		// Print output

		// End of Repeat
	}
}