package edu.cmu.lti.RaviQA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author mengdayang
 * 
 */
public class ArticleProcessor {

	static DocumentBuilderFactory dbf = null;
	static DocumentBuilder dBuilder = null;

	static DocumentBuilder getDocumentBuilder() {
		if (dBuilder == null)
			try {
				dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(false);
				dbf.setNamespaceAware(true);
				dbf.setFeature("http://xml.org/sax/features/namespaces", false);
				dbf.setFeature("http://xml.org/sax/features/validation", false);
				dbf.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
				dbf.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
				dBuilder = dbf.newDocumentBuilder();
				dBuilder.setEntityResolver(new EntityResolver() {
					@Override
					public InputSource resolveEntity(String publicId,
							String systemId) {
						return null;
					}
				});
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		return dBuilder;
	}

	public static ArrayList<Article> getArticle(
			HashMap<String, ArrayList<String>> kwdArticleMap,
			ArrayList<ArrayList<String>> entities) {

		ArrayList<Article> answer = new ArrayList<Article>();
		HashMap<String, Double> answerMap = new HashMap<String, Double>();

		for (String kwd : entities.get(0)) {
			// TODO Need to find a way to better match.
			for (String dictKwd : kwdArticleMap.keySet()) {
				if (StringUtils.contains(dictKwd, kwd)) {
					for (String fileName : kwdArticleMap.get(dictKwd)) {
						if (!answerMap.containsKey(fileName)) {
							answerMap.put(fileName, 0.);
						}
						answerMap.put(fileName, answerMap.get(fileName) + 1);
					}
				}
			}
		}

		for (String fileName : answerMap.keySet()) {
			answer.add(new Article(fileName, answerMap.get(fileName)));
		}
		return answer;
	}

	public static ArrayList<Article> getArticle_nonpreciseMatch(
			HashMap<String, ArrayList<String>> kwdArticleMap,
			ArrayList<ArrayList<String>> entities) {

		ArrayList<Article> answer = new ArrayList<Article>();
		HashMap<String, Double> answerMap = new HashMap<String, Double>();

		for (String kwd : entities.get(0)) {
			for (String dictEntity : kwdArticleMap.keySet())
				if (dictEntity.contains(kwd)) {
					for (String fileName : kwdArticleMap.get(kwd)) {
						if (!answerMap.containsKey(fileName)) {
							answerMap.put(fileName, 0.);
						}
						answerMap.put(fileName, answerMap.get(fileName) + 1);
					}
				}
		}

		for (String fileName : answerMap.keySet()) {
			answer.add(new Article(fileName, answerMap.get(fileName)));
		}
		return answer;
	}

	public static ArrayList<Sentence> getCandSentence(
			ArrayList<Article> candidateArticleList,
			ArrayList<ArrayList<String>> entities) throws Exception {

		ArrayList<Sentence> answer = new ArrayList<Sentence>();
		HashMap<String, Double> result = new HashMap<String, Double>();

		for (Article article : candidateArticleList) {
			Double score = article.score;
			String fileName = article.fileName;
			fileName = "D:" + fileName;
			System.out.println("in getCandSentence, filename = \"" + fileName
					+ "\"");

			File fXmlFile = new File(fileName);
			try {
				Document doc = getDocumentBuilder().parse(fXmlFile);
				doc.getDocumentElement().normalize();

				processNode(doc.getChildNodes(), entities, result, score);
			} catch (FileNotFoundException e) {
				System.err.format("File not found, file = \"%s\"\n", fileName);
			}
		}
		for (String key : result.keySet()) {
			answer.add(new Sentence(key, result.get(key)));
		}

		Collections.sort(answer, new Comparator<Sentence>() {
			public int compare(Sentence a, Sentence b) {
				return b.score.compareTo(a.score);
			}
		});

		return answer;
	}

	private static void processNode(NodeList nodeList,
			ArrayList<ArrayList<String>> entities,
			HashMap<String, Double> result, Double score) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				String[] sentences = tempNode.getTextContent().split("\\.");

				for (String sentence : sentences) {
					if (result.containsKey(sentence))
						continue;
					Integer match = 0;
					for (String s : entities.get(0)) {
						match = match + StringUtils.countMatches(sentence, s);
					}
					for (String s : Utils.GetSynonym(entities.get(1).get(0))) {
						match = match + StringUtils.countMatches(sentence, s)
								* 10;
					}

					if (match != 0) {

						result.put(sentence, score * match);
					}
				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					processNode(tempNode.getChildNodes(), entities, result,
							score);

				}

			}

		}
	}
}
