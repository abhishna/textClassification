package com.bds.textmining.preprocessing;

import java.io.*;
import java.util.*;

import com.bds.textmining.preprocessing.FileReader;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.CoreEntityMention;

public class Preprocessor {
	
	public List<List<String>> addFilename(List<List<String>> words, List<String> files){
		int i=0;
		for(List<String> l : words) {
			l.add(files.get(i++));
		}
		return words;
	}
	
	public List<List<String>> removeFilename(List<List<String>> words, List<String> files){
		int i=0;
		for(List<String> l : words) {
			l.remove(files.get(i++));
		}
		return words;
	}

	public List<List<String>> filterStopWords(List<List<String>>  words, String dir) throws IOException {
		FileReader fr = new FileReader();
		List<String> files = fr.getFilesFromDirectory(dir);
		words = removeFilename(words, files);
		
		FileInputStream fileStreamGoogleStopWords = new FileInputStream("src/googleStopwords.txt");
		BufferedReader brGoogleStopWords = new BufferedReader(new InputStreamReader(fileStreamGoogleStopWords));
		
		FileInputStream fileStreamStanfordStopWords = new FileInputStream("src/stanfordCoreNLPStopwords.txt");
		BufferedReader brStanfordStopWords = new BufferedReader(new InputStreamReader(fileStreamStanfordStopWords));

		String stopword;
		List<String> stopwords = new ArrayList<String>();

		while ((stopword = brGoogleStopWords.readLine()) != null) {

			if(!stopwords.contains(stopword))
				stopwords.add(stopword);		}
		
		while ((stopword = brStanfordStopWords.readLine()) != null) {

			if(!stopwords.contains(stopword))
				stopwords.add(stopword);
		}
		
		for(int i=0;i<stopwords.size();i++) {
			for(int j=0;j<words.size();j++) {
				words.get(j).removeAll(Collections.singleton(stopwords.get(i)));
			}
		}

		fileStreamGoogleStopWords.close();
		fileStreamStanfordStopWords.close();
		return addFilename(words, files);
	}
	
	public List<List<String>> stemWords(List<List<String>>  words, String dir) throws IOException {
		
		FileReader fr = new FileReader();
		List<String> files = fr.getFilesFromDirectory(dir);
		words = removeFilename(words, files);
		
		Stemmer stemmer = new Stemmer();
		List<List<String>> stemmed = new ArrayList<>();
		int l =0;
		for(List<String> line : words) {
			stemmed.add(new ArrayList<String>());
			for(String w : line) {
				stemmed.get(l).add(stemmer.stem(w));
			}
			l++;
		}
		return addFilename(stemmed, files);
	}
	
	public List<List<String>> lemmatize(List<List<String>>  words, String dir) throws IOException{
		
		FileReader fr = new FileReader();
		List<String> files = fr.getFilesFromDirectory(dir);
		words = removeFilename(words, files);
		
	    Properties props = new Properties();
		List<List<String>> lemmatized = new ArrayList<>();

	    props.setProperty("annotators", "tokenize,pos,lemma");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    int l = 0;
	    for(List<String> line : words) {
			lemmatized.add(new ArrayList<String>());
	    	String sentence = "";
			for(String w : line) {
				sentence += w + " ";
			}
			sentence.trim();
			CoreDocument document = pipeline.processToCoreDocument(sentence);
			for (CoreLabel tok : document.tokens()) {
				lemmatized.get(l).add(tok.lemma());
	    }
			l++;
	  }
	    int i=0,j=0;
	    for(List<String> d : lemmatized) {
			for(String w : d) {
				lemmatized.get(i).set(j, w.toLowerCase());
				j++;
			}
			i++;
			j=0;
		}
	    return addFilename(lemmatized, files);
	}
	
	public List<List<String>> combineMultiWordNamedEntities(String dir) throws IOException{
		FileReader fr = new FileReader();
		List<String> files = fr.getFilesFromDirectory(dir);
		List<List<String>> combined = new ArrayList<>();
		Preprocessor preprocessor = new Preprocessor();		 
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,pos,lemma,ner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    int l =0;
		for(String filename : files) {

			List<String>  words = fr.readArticle(filename, false);
	
			combined.add(new ArrayList<String>());

			
	    	String sentence = "";
		    for(String w : words) {
				sentence += w + " ";
			}
		    sentence.trim();
			CoreDocument doc = new CoreDocument(sentence);
		    pipeline.annotate(doc);
		    String combinedEntity="";
		    for (CoreEntityMention em : doc.entityMentions()) {
		    	combinedEntity = sentence.replace(em.text(), em.text().replaceAll(" ","_"));
		    }
		    if(combinedEntity.equals("")) {
		    	combined.get(l++).addAll(Arrays.asList(sentence.split(" ")));
		    	continue;
		    }
		    else {
		    	combined.get(l).addAll(Arrays.asList(combinedEntity.split(" ")));
		    	l++;
		    }
		  }
		int i = 0,j=0;
		for(List<String> d : combined) {
			for(String w : d) {
				combined.get(i).set(j, w.toLowerCase());
				j++;
			}
			i++;
			j=0;
		}
	    return addFilename(combined, files);
	}
	
	  public List<String> generateNGramsFromSentence(List<String> words, int n) {
	        List<String> nGrams = new ArrayList<String>();
	        for (int i = 0; i < words.size() - n + 1; i++) {
	        	StringBuilder ng = new StringBuilder();
		        for (int j = i; j < i+n; j++)
		            ng.append((j > i ? " " : "") + words.get(j));
	            nGrams.add(ng.toString());
	        }
	        	
	        return nGrams;
	    }
	  
	  public  List<String> generateAllNGramsFromDataset(String dir, int N, int threshold) throws IOException{
		  FileReader f = new FileReader();
		  List<Integer> n = new ArrayList();
		  for(int i=2;i<=N;i++)
			  n.add(i);
		  List<String> files = f.getFilesFromDirectory(dir);
		  List<String> ngrams = new ArrayList<>();
		  for(int nn : n) {
			  for(String file : files) {
				  List<String> words = f.readArticle(file, true);
				  List<String> ng = generateNGramsFromSentence(words, nn);
				  ngrams.addAll(ng);	  
			  }
		  }
		  
		  Map<String, Integer> map = new HashMap<String, Integer>();     
		    for(int i = 0; i < ngrams.size(); i++){
	            if(!map.containsKey(ngrams.get(i))){
	               map.put(ngrams.get(i), 1);
	            }
	            else{
	               map.put(ngrams.get(i), map.get(ngrams.get(i))+1);
	            }
		    }
		    
		    Iterator iter = map.entrySet().iterator();
		    List<String> frequentNGrams = new ArrayList<>();
	     
	        while (iter.hasNext()) {
	 
	            Map.Entry item = (Map.Entry)iter.next();
	            int freq = (int)item.getValue();
	            String ng = (String) item.getKey();
	            if(freq >= threshold)
	            	frequentNGrams.add(ng);
	        }
	        return frequentNGrams;
	  }
	  
	  public List<List<String>> combineNGrams(List<List<String>> words, List<String> ngrams){
		  
		  List<List<String>> combined = new ArrayList<>();
		  for(List<String> doc : words) {
			  String s = "";
			  for(String w : doc) {
				  s+= w+" ";
			  }
			  s.trim();
			  for(String ng: ngrams) {
				  s = s.replaceAll(ng, ng.replaceAll(" ", "_"));

			  }
			  combined.add(Arrays.asList(s.split(" ")));			  
		  }
		  return combined;
	  }
	  
	  public List<List<String>> pipeline(String dir, int n, int threshold) throws IOException{
		  
			List<String> ngrams = generateAllNGramsFromDataset(dir, n, threshold);
			return combineNGrams(lemmatize(filterStopWords(combineMultiWordNamedEntities(dir), dir),dir),ngrams);
			
		
	  }

	
}

