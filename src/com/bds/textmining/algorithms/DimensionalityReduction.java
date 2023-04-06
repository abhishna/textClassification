package com.bds.textmining.algorithms;

import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class DimensionalityReduction {
	
	public static double[][] _PCA(List<List<Double>> input, int nComponents) {
		
		double[][] features = new double[input.size()][input.get(0).size()];
		
		double[][] out = new double[input.size()][nComponents];
		
		for(int i = 0; i < input.size(); i++) {
		    for(int j = 0; j <input.get(0).size(); j++) {
		    	features[i][j] = input.get(i).get(j);
		    }
		}
		
		RealMatrix X = MatrixUtils.createRealMatrix(features);
		
		Covariance c = new Covariance(X);
		
		RealMatrix C = c.getCovarianceMatrix();
		
		EigenDecomposition ed = new EigenDecomposition(C);
		
		RealMatrix V = ed.getV();
		
		RealMatrix PC = X.multiply(V);
		
		PC.copySubMatrix(0,out.length-1,0,nComponents-1,out);
				
		return out;
	}
	
}
