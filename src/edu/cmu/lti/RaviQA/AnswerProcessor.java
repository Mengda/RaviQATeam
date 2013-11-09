package edu.cmu.lti.RaviQA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class AnswerProcessor {
	private ArrayList<Candidate> candidates;
	private ArrayList<String> answers;
	private static String ExampleSentience = "How can I view the change history of an individual file in Git, complete with what has changed?";

	public static void main(String arg[]) {
		String grammar = "englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar);
		lp.setOptionFlags(options);
		Tree parse = lp.parse(ExampleSentience);
		parse.pennPrint();

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

		Collection tdl = gs.typedDependenciesCCprocessed();
		System.out.println(gs.typedDependenciesCCprocessed(true));
		System.out.println();
	}

}
