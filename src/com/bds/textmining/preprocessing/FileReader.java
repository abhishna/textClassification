package com.bds.textmining.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReader {
	
	public String extractFolderName(String path) {
		int count = 0, start=0, end = 0;
		for(int i=0;i<path.length();i++) {
			if(path.charAt(i)=='/') {
				count++;
				if(count==3)
					start = i;
				if(count==4)
					end = i;
			}
				
		}
		return path.substring(start+1, end);
	}
	
	
	public List<String> readArticle(String fileName, boolean readAsLowerCase) throws IOException {
		
		FileInputStream fileStream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

		String line;
		List<String> document = new ArrayList<>();
		int l = 0;
		while ((line = br.readLine()) != null) {
			if(readAsLowerCase) {
				String cleanedLine = line.replaceAll("\\p{Punct}", "").toLowerCase();
				document.addAll(Arrays.asList(cleanedLine.split(" ")));
			}
			else {
				document.addAll(Arrays.asList(line.replaceAll("\\p{Punct}", "").split(" ")));
			}
			l++;
		}
		fileStream.close();
		document.removeAll(Collections.singleton(""));
		document.removeAll(Collections.singleton(" "));
		return document;
	}
	
	public Map<String,String> readCsv(String fileName) throws IOException{

		FileInputStream fileStream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

		String line;
		List<String> document = new ArrayList<>();
		Map<String,String> m = new HashMap<String,String>();
		while ((line = br.readLine()) != null) {
			String s[] = line.split(",");
			m.put(s[0], s[1]);
		}
		return m;
	}
	
	public void getFilesHelper(File[] contents, int ind,  List<String> files, String path) {
        if (ind == contents.length)
            return;
 
        if (contents[ind].isFile())
            files.add(path+contents[ind].getName());
 
        else if (contents[ind].isDirectory()) {
        	getFilesHelper(contents[ind].listFiles(), 0, files, path+contents[ind].getName()+"/");
        }
 
        getFilesHelper(contents, ++ind, files, path);
	}
	
	public List<String> getFilesFromDirectory(String dir){
		File fdir = new File(dir);
		List<String> files = new ArrayList<>();

		if (fdir.exists() && fdir.isDirectory()) {
			File contents[] = fdir.listFiles();
			getFilesHelper(contents, 0, files, dir);
		}
		return files;
	}

}
