package edu.cmu.lti.RaviQA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class Tester {
//  public static void main(String[] args) throws IOException {
//    HashMap<String, ArrayList<String>> kwdArticleMap = Utils.LoadIndex();
//    /*
//     * for (String s : kwdArticleMap.keySet()) { System.out.println(s); }
//     */
//
//    //String input = "What inhibit apoptotic cell death";
//    String input = "What inhibit apoptotic cell death";
//    // System.out.print("Enter your question: ");
//    // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    //
//    // try {
//    // input = br.readLine();
//    // } catch (IOException e) {
//    // // TODO Auto-generated catch block
//    // e.printStackTrace();
//    // }
//    //ArrayList<ArrayList<String>> entities = Utils.getEntities(input);
//    // System.out.println(x);
////    for (String s : entities.get(0)) {
////      System.out.println(s);
//  //}
//
////    System.out.println();
////
////    for (String s : entities.get(1)) {
//////      System.out.println(s);
////    }
//
//    System.out.println("===========");
//
//    ArrayList<Article> candidateArticleList = Utils.getArticle(kwdArticleMap, entities);
//
//    for (Article a : candidateArticleList) {
//      System.out.println(a.fileName + " : " + a.score);
//    }
//
//    System.out.println("===========");
//
//    ArrayList<Sentence> candidateSentenceList = Utils.getCandSentence(candidateArticleList,
//            entities);
//
//    for (Sentence s : candidateSentenceList) {
//      System.out.format("%f : %s\n", s.score, s.text);
//    }
//    // String sss =
//    // "Based on these data, only 3% (1/31) of SN neurons from the elderly subjects harbour a clonally expanded point mutation, a very different scenario to mtDNA deletions, where high levels of deletions were detected in COX normal neurons from the same subjects (Table 1) and other elderly subjects (Bender et al";
//    // System.out.println(StringUtils.countMatches(sss, "neurons"));
//    // System.out.println(StringUtils.countMatches(sss, "neuron"));
//
//    // findEntityWithMaxFrequency(candidateSentenceList, "human CD34+");
//    System.out.println("total candidate number is:" + candidateSentenceList.size());
//    AnswerProcessor ansProcessor = new AnswerProcessor(candidateSentenceList);
//    //ansProcessor.CandidateReVerbExtract();
//    System.out.println("result is " + ansProcessor.findEntityWithMaxFrequency( "G-CSF"," "));
//    
//
//  }

  public static ArrayList<ArrayList<String>> getEntities(String input, String QuestionVerb) {

    input = input.replace(QuestionVerb, "");// delete the verb
    String[] words = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
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
    System.out.println(noun);
    return answer;
  }
}
