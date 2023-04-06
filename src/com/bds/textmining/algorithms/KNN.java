package com.bds.textmining.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.util.Pair;

import com.bds.textmining.preprocessing.*;
import com.bds.textmining.vectorization.Vectorizer;

public class KNN {
	Map<String, List<Double>> tfidfLabeled;
	Preprocessor preprocessor;
	Vectorizer vectorizer;
	FileReader f;
	SimilarityMeasures s;
	String vocabList[];
	public KNN() {
		this.preprocessor = new Preprocessor();
		this.vectorizer = new Vectorizer();
		this.f = new FileReader();
		this.s = new SimilarityMeasures();
		this.vocabList=null;
	}
	
	public void fit(String path) throws IOException {
		
		List<List<String>> preprocessedLabeled = this.preprocessor.pipeline(path, 3, 5);
		String v[] = {};
		this.tfidfLabeled = (this.vectorizer.generateTfIdfMatrix(preprocessedLabeled,v));	
		Map<String, List<String>> terms = new HashMap<String, List<String>>();
        Set<String> vocabulary = new HashSet<String>();
		
		for(int i=0;i < preprocessedLabeled.size();i++) {
			List<String> l = preprocessedLabeled.get(i);
			String key = l.get(l.size()-1);
			List<String> val = new ArrayList<>();
			for(int j =0;j<l.size()-1;j++) {
				val.add(l.get(j));
				vocabulary.add(l.get(j));
			}
			terms.put(key, val);
		}
		this.vocabList = vocabulary.toArray(new String[vocabulary.size()]);
        Arrays.sort(this.vocabList);
      

	}
	
	public Map<String,String> predict(String testDir, String similarityMeasure, int k, boolean fuzzy) throws IOException {
		List<List<String>> preprocessedUnLabeled = this.preprocessor.pipeline(testDir, 3, 5);

		Map<String, List<Double>> tfidfUnLabeled = (this.vectorizer.generateTfIdfMatrix(preprocessedUnLabeled, this.vocabList));

		Map<String,String> predicted = new HashMap<String, String>();
		k = Math.min(this.tfidfLabeled.keySet().size(), k);
		
		for(String filenametest : tfidfUnLabeled.keySet()) {
			List<Pair<String, Double>> similarityScores = new ArrayList<Pair<String, Double>>();
			List<Double> testVector = tfidfUnLabeled.get(filenametest);
			for(String filenametrain : this.tfidfLabeled.keySet()) {
				List<Double> trainVector = tfidfLabeled.get(filenametrain);
				if(similarityMeasure.equals("cosine"))
					similarityScores.add(new Pair<String,Double>(f.extractFolderName(filenametrain),s.cosineSimilarity(testVector, trainVector)));
				else
					similarityScores.add(new Pair(f.extractFolderName(filenametrain),s.euclideanDistance(testVector, trainVector)));		
			}

			if(similarityMeasure.equals("cosine"))
				Collections.sort(similarityScores, Comparator.comparing(p -> -p.second()));
			else
				Collections.sort(similarityScores, Comparator.comparing(p -> p.second()));
			Map<String, Integer> counts = new HashMap<String,Integer>();
			for(int i=0;i<k;i++) {
				if(counts.containsKey(similarityScores.get(i).first())) {
					counts.put(similarityScores.get(i).first(), counts.get(similarityScores.get(i).first())+1);
				}
				else
					counts.put(similarityScores.get(i).first(), 1);

			}
			if(fuzzy) {
				int cp=0;
				for(String c : counts.keySet()) {
					predicted.put(filenametest+" "+String.valueOf(cp++),"Document "+filenametest+" belongs to class "+c+" with "+" probability "+String.valueOf(counts.get(c)*1.0/k));
				}
			}
			else {
				int max=0; String label ="";
				for(String c : counts.keySet()) {
					if(counts.get(c)>max)
					{
						max=counts.get(c);
						label = c;
					}
				}
				predicted.put(filenametest,label);
			}
		}
		

		return predicted;
	}
}
