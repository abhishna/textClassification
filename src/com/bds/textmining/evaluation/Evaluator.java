package com.bds.textmining.evaluation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Evaluator {
	
	public int[][] computeConfusionMatrix(String[] predicted, String[] actual, int toPrint) {
	
	    int c=0;
	    Map<String, Integer> ind = new HashMap<String, Integer>();
	    for(int i=0; i<actual.length;i++) {
	    	if(!ind.containsKey(actual[i]))
	    		ind.put(actual[i], c++);
	    	if(!ind.containsKey(predicted[i]))
	    		ind.put(predicted[i], c++);
	    }
	    int [][] confMatrix = new int[ind.keySet().size()][ind.keySet().size()];
	    for(int i=0;i<ind.keySet().size();i++) {
	    	for(int j=0;j<ind.keySet().size();j++) {
	    		confMatrix[i][j]=0;
	    	}
	    }

	    for(int i=0; i<actual.length; i++) {
	          String pred = predicted[i];
	          String act = actual[i];
	          int x = ind.get(predicted[i]);
	          int y = ind.get(actual[i]);
	          confMatrix[x][y] += 1;
	    }
	    
	    if(toPrint==1) {
	        System.out.println("\t\t\t\t\t Confusion matrix :");
	
		    System.out.println("\t\t\t\t\t    Actual Labels");
	    	System.out.print("\t\t\t\t________________________________________");
	
		    for(int i=0;i<ind.keySet().size()/2;i++) {
		    	System.out.print("________________________________________");
	
		    }
	
		    System.out.print("\n\t Predicted Labels |");
		    
		    Set<String> classes = ind.keySet();
	    	System.out.print("\t\t");
	
		    for(String cl: classes) {
		    	System.out.print(cl + "\t\t\t");
		    }
		    System.out.println();
		    System.out.print("\t\t\t\t________________________________________");
	
		    for(int i=0;i<ind.keySet().size()/2;i++)
			    System.out.print("________________________________________");
	
	
		    
	    	System.out.println("\n\n");
	    	int p=0;
	    	for(String cl: classes) {
		    	System.out.print("\t\t\t" + cl + " |");
		    	for(int i=0;i<ind.keySet().size();i++) {
		    		System.out.print("\t\t" + confMatrix[p][i]+"\t");
		    	}
		    	System.out.println("\n");
		    	p++;
	
	    	}
	    }
    	return confMatrix;

	}
	
	public double computeMetrics(String[] predicted, String[] actual, Boolean verbose) {
		int correct=0;
		for(int i=0;i<predicted.length;i++) {
			if(predicted[i].compareTo(actual[i])==0)
				correct++;
		}
		double accuracy = (correct*1.0/predicted.length)*100;
		if(verbose) {
			Map<String, Integer> ind = new HashMap<String, Integer>();
			int c=0;
		    for(int i=0; i<actual.length;i++) {
		    	if(!ind.containsKey(actual[i]))
		    		ind.put(actual[i], c++);
		    	if(!ind.containsKey(predicted[i]))
		    		ind.put(predicted[i], c++);
		    }
		    int numClasses = ind.keySet().size();
		    int[][] confMatrix = computeConfusionMatrix(predicted, actual, 0);
		    for(String cl:ind.keySet()) {
		    	int rc = ind.get(cl);
		    	int tp = 0, tn = 0, fp = 0, fn = 0;
		    	for(int i=0;i<numClasses;i++) {
		    		if(i!=rc) {
		    			fp += confMatrix[rc][i];
		    			fn += confMatrix[i][rc];
		    		}
		    	}
		    	tp = confMatrix[rc][rc];
		    	for(int i=0;i<numClasses;i++) {
		    		for(int j = 0;j<numClasses;j++) {
		    			if(i!=rc && j !=rc)
		    				tn+=confMatrix[i][j];
		    		}
		    	}
		    	System.out.println("class : "+cl+" : ");
		    	
		    	double precision = (tp/(double)(tp+fp));
		    	precision = (double) Math.round(precision * 100) / 100;
		    	double recall = (tp/(double)(tp+fn));
		    	recall = (double) Math.round(recall * 100) / 100;
		    	double f1 = 2*precision*recall/(precision+recall);
		    	f1 = (double) Math.round(f1 * 100) / 100;
		    	System.out.println("Precision = "+String.valueOf(precision));
		    	System.out.println("Recall = "+String.valueOf(recall)+"\n");
		    	System.out.println("F1 Score = "+String.valueOf(f1)+"\n");	
		    }
		}
	    return accuracy;
	}
}
