package edu.cmu.lti.RaviQA;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author mengdayang
 *
 */
public class ArticleProcessor {

	public static ArrayList<Article> getArticle(
			HashMap<String, ArrayList<String>> kwdArticalMap,
			ArrayList<ArrayList<String>> entities) {
		ArrayList<Article> answer = new ArrayList<Article>();
		HashMap<String, Double> answerMap = new HashMap<String, Double>();
		
		for(String kwd : entities.get(0)){
			if(kwdArticalMap.containsKey(kwd)){
				for(String fileName : kwdArticalMap.get(kwd)){
					if(!answerMap.containsKey(fileName)){
						answerMap.put(fileName, 0.);
					}
					answerMap.put(fileName, answerMap.get(fileName)+1);
				}
			}
		}
		
		for(String fileName : answerMap.keySet()){
			answer.add(new Article(fileName, answerMap.get(fileName)));
		}
		
		return answer;
	}

	
	public static ArrayList<Sentence> getCandSentence(
			ArrayList<Article> candidateArticalList,
			ArrayList<ArrayList<String>> entities) throws Exception {

		ArrayList<Sentence> answer = new ArrayList<Sentence>();

		for (Article article : candidateArticalList) {
			Double score = article.score;
			String fileName = article.fileName;

			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			processNode(doc.getChildNodes(), entities, answer, score);

		}

		return answer;
	}

	private static void processNode(NodeList nodeList,
			ArrayList<ArrayList<String>> entities, ArrayList<Sentence> answer, Double score) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				String[] sentences = tempNode.getTextContent().split(".");
				for (String sentence : sentences) {
					Integer match = 0;
					for (String s : entities.get(0)) {
						match = match + StringUtils.countMatches(sentence, s);
					}
					for(String s : Utils.GetSynonym(entities.get(1).get(0))){
						match = match + StringUtils.countMatches(sentence, s) * 10;
					}
					if(match!=0){
						answer.add(new Sentence(sentence,match * score));
					}
				}
				
				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					processNode(tempNode.getChildNodes(), entities, answer, score);

				}

			}

		}
	}
}
