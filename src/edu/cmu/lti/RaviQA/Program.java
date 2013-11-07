package edu.cmu.lti.RaviQA;
import java.util.*;
public class Program {
	HashMap<String, ArrayList<String>> kwdArticalMap = Utils.LoadIndex();
	
	// Repeat
	String input  = System.console().readLine();
	ArrayList<ArrayList<String>> entities = Utils.GetEntities(input);
	ArrayList<Artical> candidateArticalList = Utils.GetArtical(kwdArticalMap, entities);
	ArrayList<Sentence> candidateSentenceList = Utils.GetCandSentence(candidateArticalList, entities);
	ArrayList<Candidate> candidateAnswerList = Utils.GetCandAns(candidateSentenceList, entities);
	// Print output
	
	// End of Repeat
}
