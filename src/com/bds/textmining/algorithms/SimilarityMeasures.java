package com.bds.textmining.algorithms;

import java.util.*;
import java.lang.IllegalArgumentException;
import java.lang.Math;

public class SimilarityMeasures {
	
	public static double euclideanDistance(List<Double> l1, List<Double> l2) {
		if(l1.size() != l2.size()) {
			throw new IllegalArgumentException("input vectors must have same dimensions.");
		}
		double sum = 0.0;
		Iterator<Double> it1 = l1.iterator();
		Iterator<Double> it2 = l2.iterator();
		while(it1.hasNext()) {
			double diff = it2.next() - it1.next();
			sum += diff*diff;
		}
		sum = Math.sqrt(sum);
		return sum;
	}
	
	public static double cosineSimilarity(List<Double> l1, List<Double>l2) {
		if(l1.size() != l2.size()) {
			throw new IllegalArgumentException("input vectors have same dimensions.");
		}
		Iterator<Double> it1 = l1.iterator();
		Iterator<Double> it2 = l2.iterator();
		double dotprod = 0.0;
		double norm1 = 0.0;
		double norm2 = 0.0;
		while(it1.hasNext()) {
			double e1 = it1.next();
			double e2 = it2.next();
			dotprod += e1*e2;
			norm1 += e1*e1;
			norm2 += e2*e2;
		}
		double cossim = 0.0;
		if(norm1!=0 && norm2!=0)
		 cossim = dotprod / (Math.sqrt(norm1) * Math.sqrt(norm2));
		return cossim;
	}
	
}
