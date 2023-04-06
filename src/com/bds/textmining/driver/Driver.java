package com.bds.textmining.driver;

import java.io.IOException;
import java.util.*;

import com.bds.textmining.preprocessing.*;
import com.bds.textmining.algorithms.*;
import com.bds.textmining.evaluation.Evaluator;
import com.bds.textmining.plotter.Plotter;

import com.bds.textmining.vectorization.Vectorizer;

public class Driver {

	public static void main(String[] args) throws IOException {
		String testdir = "src/data/unknown/";
		String traindir = "src/data/labeled/";
		KNN knn = new KNN();
		knn.fit(traindir);
		FileReader f = new FileReader();
		Evaluator e = new Evaluator();
		Map<String,String> actual = f.readCsv("src/data/labels.csv");
		int kMax = f.getFilesFromDirectory(traindir).size();
		double maxCosine=0;
		int kmaxCosine=0;
		System.out.println("Using cosine similarity :");
		for(int k=kMax;k>=1;k--) {
		 Map<String,String> predicted = knn.predict(testdir, "cosine", k, false);
		 
		 String[] p = new String[predicted.size()];
		 String[] a = new String[actual.size()];
		 int i=0;
		 for(String key: actual.keySet()) {
			 a[i]=actual.get(key);
			 p[i]=predicted.get(testdir + key);
			 i++;
		 }
		 double accuracy = e.computeMetrics(p, a, false);
		 if(accuracy>maxCosine)
		 {
			 maxCosine = accuracy;
			 kmaxCosine = k;
		 }
		 System.out.println("For k = "+String.valueOf(k)+" ,accuracy = "+String.valueOf(accuracy)+" %");
		 
		}
		System.out.println("Using cosine similarity : K value with maximum accuracy of "+String.valueOf(maxCosine) +"%" + " is "+String.valueOf(kmaxCosine));
		System.out.println();
		System.out.println("Using Euclidean Distance :");
		double maxEuc=0;
		int kmaxEuc=0;
		for(int k=kMax;k>=1;k--) {
		 Map<String,String> predicted = knn.predict(testdir, "euclidean", k, false);
		 
		 String[] p = new String[predicted.size()];
		 String[] a = new String[actual.size()];
		 int i=0;
		 for(String key: actual.keySet()) {
			 a[i]=actual.get(key);
			 p[i]=predicted.get(testdir + key);
			 i++;
		 }
		 double accuracy = e.computeMetrics(p, a, false);
		 if(accuracy>maxEuc)
		 {
			 maxEuc = accuracy;
			 kmaxEuc = k;
		 }
		 System.out.println("For k = "+String.valueOf(k)+" ,accuracy = "+String.valueOf(accuracy)+" %");
		 
		}
		System.out.println("Using Euclidean Distance : K value with maximum accuracy of "+String.valueOf(maxEuc) +"%" + " is "+String.valueOf(kmaxEuc));
		
		if(maxCosine>=maxEuc) {
			System.out.println("choosing cosine similarity with k = "+String.valueOf(kmaxCosine) + " as the best model");
			 Map<String,String> predicted = knn.predict(testdir, "cosine", kmaxCosine, false);
			 System.out.println("predictions : ");
			 for(String fl : actual.keySet()) {
				 System.out.println("predicted class for file "+fl+" is "+predicted.get(testdir+fl)+" and actual label is "+actual.get(fl));
			 }
			 String[] p = new String[predicted.size()];
			 String[] a = new String[actual.size()];
			 int i=0;
			 for(String key: actual.keySet()) {
				 a[i]=actual.get(key);
				 p[i]=predicted.get(testdir + key);
				 i++;
			 }
			 e.computeConfusionMatrix(p, a, 1);
			 e.computeMetrics(p, a, true);
			 
			 System.out.println("Running Fuzzy KNN with best K value found above using cosine similarity : ");
			 Map<String,String> predictedFuzzy = knn.predict(testdir, "cosine", kmaxCosine, true);
			 Map<String,Boolean> visited = new HashMap<>();
			 for(String file : predictedFuzzy.keySet()) {
				 String name = file.split(" ")[0];
				 for(String f1 : predictedFuzzy.keySet()) {
					 if(predictedFuzzy.get(f1).contains(name)) {
						 if(visited.containsKey(f1.split(" ")[0])==false)
							 System.out.println(predictedFuzzy.get(f1));
					 }
				 }
				 visited.put(name, true);
				 System.out.println();
				
			 }
		}
		else {
			System.out.println("choosing Euclidean distance with k = "+String.valueOf(kmaxEuc) + " as the best model");
			 Map<String,String> predicted = knn.predict(testdir, "euclidean", kmaxEuc, false);
			 System.out.println("predictions : ");
			 for(String fl : actual.keySet()) {
				 System.out.println("predicted class for file "+fl+" is "+predicted.get(testdir+fl)+" and actual label is "+actual.get(fl));
			 }
			 String[] p = new String[predicted.size()];
			 String[] a = new String[actual.size()];
			 int i=0;
			 for(String key: actual.keySet()) {
				 a[i]=actual.get(key);
				 p[i]=predicted.get(testdir + key);
				 i++;
			 }
			 e.computeConfusionMatrix(p, a, 1);
			 e.computeMetrics(p, a, true);
			 System.out.println("Running Fuzzy KNN with best K value found above using euclidean distance: ");
			 Map<String,String> predictedFuzzy = knn.predict(testdir, "euclidean", kmaxEuc, true);
			 Map<String,Boolean> visited = new HashMap<>();
			 for(String file : predictedFuzzy.keySet()) {
				 String name = file.split(" ")[0];
				 for(String f1 : predictedFuzzy.keySet()) {
					 if(predictedFuzzy.get(f1).contains(name)) {
						 if(predictedFuzzy.get(f1).contains(name)) {
							 if(visited.containsKey(f1.split(" ")[0])==false)
								 System.out.println(predictedFuzzy.get(f1));
						 }
					 }
				 }
				 visited.put(name, true);
				 System.out.println();
				
			 }
		}
	}

}