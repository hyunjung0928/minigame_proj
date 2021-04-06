package proj1;

import java.io.*;
import java.util.*;

public class WordManager {
	private static WordManager wordManager = null;

	private static Vector<String> wordVector = new Vector<String>();
	private String fileName = "words.txt";
	private File file = new File(fileName);


	// 생성자 얻기
	public static WordManager getWordManager() {
		if (wordManager == null) {
			wordManager = new WordManager();
		}
		return wordManager;
	}

	public WordManager() {
		fileRead();
	}

	public void fileRead() {
		FileReader fin = null;
		try {
			fin = new FileReader(file);
			
			BufferedReader out = new BufferedReader(fin);
			String line = "";
			while ((line = out.readLine()) != null) {
				wordVector.add(line);
			}
			
			fin.close();
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fileWrite(String word) {		
		try {
			BufferedWriter in = new BufferedWriter(new FileWriter(fileName, true));	
			PrintWriter pw = new PrintWriter(in, true);
		
			pw.write(word);
			pw.write("\n");
			
			pw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void vectorWrite(String word) {
		wordVector.add(word);
	}
	
	public Vector<String> getVector() {
		return wordVector;
	}
	
	public String getWord() {
		int size = wordVector.size();
		String word = wordVector.get((int)(Math.random()*size));
		
		return word;
	}
	
	
}
