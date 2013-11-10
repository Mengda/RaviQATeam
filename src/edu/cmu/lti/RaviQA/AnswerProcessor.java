package edu.cmu.lti.RaviQA;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class AnswerProcessor {
  private ArrayList<Candidate> candidates;

  private static HashSet<String> dictionary = new HashSet<String>();

  private ArrayList<String> answers;

  private static String ExampleSentience = "John hit the ball";
  //private static String eg = "CLU1 and CLU2 are predicted to produce intracellular and secreted proteins";
  // part-of-speech tagger.
  private static String eg = "I am one of the people who don't like Apple product";

  private static ArrayList<TaggedWord> tagger(String text) {
    // Initialize the tagger
    MaxentTagger tagger = new MaxentTagger("english-bidirectional-distsim.tagger");
    // The sample string=========================
    String sample = "John hit the ball";

    // String tagged = tagger.tagString(sample);
    List<List<HasWord>> x = tagger.tokenizeText(new StringReader(sample),
            PTBTokenizerFactory.newTokenizerFactory());

    ArrayList<TaggedWord> taggedResult = tagger.tagSentence(x.get(0));
    // Output the result
    System.out.println(taggedResult.get(0).tag());
    return taggedResult;

  }

  // name entity extractor. Not completed yet.
  private static HashSet<String> extractAllNameEntity(Tree parse) {

    Iterator<Tree> tmp = parse.iterator();

    while (tmp.hasNext()) {
      Tree currentTreeNode = tmp.next();
      if (currentTreeNode.isLeaf()) {

        // if (currentTreeNode.parent().value() == "NN" )
        System.out.println(currentTreeNode.value());
        // System.out.println(x);

      }
    }
    return null;

  }

  // parser, will be usint in next iteration. hopefully
  private static void sentieceParser() {
    String grammar = "englishPCFG.ser.gz";
    String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
    LexicalizedParser lp = LexicalizedParser.loadModel(grammar);
    lp.setOptionFlags(options);

    Tree parse = lp.parse(eg);
    extractAllNameEntity(parse);

    parse.pennPrint();

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

    Collection tdl = gs.typedDependenciesCCprocessed();
    System.out.println(gs.typedDependenciesCCprocessed(true));
    System.out.println();
  }

  private static String findWordWithMaxFrequency(ArrayList<Candidate> candidates) {
    edu.stanford.nlp.process.TokenizerFactory<Word> factory = PTBTokenizerFactory
            .newTokenizerFactory();
    HashMap<String, Integer> result = new HashMap<String, Integer>();
    int currentMax = 1;
    String currentAnswer = null;
    for (int i = 0; i < candidates.size(); i++) {
      Tokenizer<Word> tokenizer = factory.getTokenizer(new StringReader(candidates.get(i).text));
      
      while (tokenizer.hasNext()) {
        Word temp = tokenizer.next();
        if (dictionary.contains(temp.toString())) {
          if (result.containsKey(temp.toString())) {
            result.put(temp.toString(), result.get(temp.toString()) + 1);
            if(result.get(temp.toString()) > currentMax)
              currentAnswer = temp.toString();
          } else {
            result.put(temp.toString(), 1);
          }
        }
      }// end iteration of tokens
    }

    return currentAnswer;

  }

  private static String predictAnswer(ArrayList<Candidate> candidates)
  {
    return findWordWithMaxFrequency( candidates);
  }
  public static void main(String arg[]) {

    sentieceParser();
    //System.out.println("is this working?");
    //tagger("just test");
  }


}
