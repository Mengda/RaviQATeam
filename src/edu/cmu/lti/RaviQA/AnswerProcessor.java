package edu.cmu.lti.RaviQA;

import java.io.IOException;
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
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import abner.Tagger;

public class AnswerProcessor {
  private ArrayList<Candidate> candidates;

  private static HashSet<String> dictionary = new HashSet<String>();

  private ArrayList<String> answers;

  private static String ExampleSentience = "John hit the ball";

  // private static String eg =
  // "CLU1 and CLU2 are predicted to produce intracellular and secreted proteins";
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
  private static void sentieceParser(String str) {
    String grammar = "englishPCFG.ser.gz";
    String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
    LexicalizedParser lp = LexicalizedParser.loadModel(grammar);
    lp.setOptionFlags(options);

    Tree parse = lp.parse(str);
    extractAllNameEntity(parse);

    parse.pennPrint();

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

    Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed(true);
    System.out.println(tdl);
    TypedDependency a;
    a = tdl.iterator().next();
    System.out.println("a = :");
    // System.out.println(gs.typedDependenciesCCprocessed(true));
    System.out.println();
  }

  private static String findWordWithMaxFrequency(ArrayList<Sentence> candidates) {
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
            if (result.get(temp.toString()) > currentMax)
              currentAnswer = temp.toString();
          } else {
            result.put(temp.toString(), 1);
          }
        }
      }// end iteration of tokens
    }

    return currentAnswer;

  }

  static String findEntityWithMaxFrequency(ArrayList<Sentence> candidates, String QuestionEntity) {

    HashMap<String, Integer> result = new HashMap<String, Integer>();

    Tagger abnerTagger = null;
    abnerTagger = new Tagger(Tagger.BIOCREATIVE);
    int currentMax = 0;
    String currentAnswer = "";
    for (int k = 0; k < candidates.size(); k++) {
      String[][] nerTagged = abnerTagger.getEntities(candidates.get(k).text);
      // for (int i = 0; i < nerTagged.length; i++) {
      int i = 0;
      for (int j = 0; j < nerTagged[i].length; j++) {
        // System.out.println(nerTagged[i][j]);
        if (nerTagged[i][j].equals(QuestionEntity))
          continue;
        if (!result.containsKey(nerTagged[i][j]))
          result.put(nerTagged[i][j], 0);

        result.put(nerTagged[i][j], result.get(nerTagged[i][j]) + 1);
        if (result.get(nerTagged[i][j]) >= currentMax) {
          currentMax = result.get(nerTagged[i][j]);
          currentAnswer = nerTagged[i][j];
        }
      }
    }

    // String nerTagged = abnerTagger.tagABNER("gene");

    return currentAnswer;

  }

  private static String predictAnswer(ArrayList<Sentence> candidates) {
    return findWordWithMaxFrequency(candidates);
  }

  private static void ReVerbExtract(String str) throws IOException {
    // Looks on the classpath for the default model files.
    OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
    ChunkedSentence sent = chunker.chunkSentence(str);

    // Prints out the (token, tag, chunk-tag) for the sentence
    System.out.println(str);
    for (int i = 0; i < sent.getLength(); i++) {
      String token = sent.getToken(i);
      String posTag = sent.getPosTag(i);
      String chunkTag = sent.getChunkTag(i);
      System.out.println(token + " " + posTag + " " + chunkTag);
    }

    // Prints out extractions from the sentence.
    ReVerbExtractor reverb = new ReVerbExtractor();
    ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
    for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
      double conf = confFunc.getConf(extr);
      System.out.println("Arg1=" + extr.getArgument1());
      System.out.println("Rel=" + extr.getRelation());
      System.out.println("Arg2=" + extr.getArgument2());
      System.out.println("Conf=" + conf);
    }
  }

  public static void main(String arg[]) throws IOException {
    // System.out.println("Answer is: " + findEntityWithMaxFrequency(new ArrayList<Sentence>(),
    // "G-CSF"));
    ArrayList<Sentence> candidates = new ArrayList<Sentence>();
    Sentence cand = new Sentence("human CD34+ is not G-CSF", 1.0);
    // // cand.text = "What move CD34+?";
    // //
    candidates.add(cand);
    // findEntityWithMaxFrequency(candidates, "");
    System.out.println("Answer is " + findEntityWithMaxFrequency(candidates, "G-CSF"));
    // //sentieceParser("What move CD34+?");
    // //ReVerbExtract("What move neuron?");
    // //tagger("just test");

  }

}
