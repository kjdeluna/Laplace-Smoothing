import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import java.io.File;

public class BagOfWords{
	private HashMap<String, Integer> dict;
	private int wordCount;
	private int fileCount;
	public BagOfWords(){
		this.wordCount = 0;
		this.dict = new HashMap<String, Integer>(){
			@Override
			public Integer get(Object key){
				if(!this.containsKey(key)) return 0;
				else return super.get(key);
			}
		};
		this.fileCount = 0;
	}

	public int getWordCount(){
		return this.wordCount;
	}
	public void readFile(String filename){
		String[] tokens;
		String word;
		try{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			while((currentLine = br.readLine()) != null){
				tokens = currentLine.split("\\s");
				for(String token:tokens){
					// Lowercase all . Replace all non-alphanumeric characters
					word = token.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
					if(!word.equals("")){
						this.wordCount++;
						if(this.dict.containsKey(word)){
							this.dict.put(word, (this.dict.get(word)) + 1);
						}
						else{
							this.dict.put(word, 1);
						}
					}
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void printHashMapContents(){
		// Print contents
	   Iterator it = this.dict.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	    }
	    System.out.println(this.dict.size());
	    System.out.println(this.wordCount);
	}

	public HashMap<String, Integer> getDict(){
		// Get dictionary
		return this.dict;
	}

	public int getDictSize(){
		return this.dict.size();
	}

	public void resetAll(){
		// To clear the whole contents
		this.dict.clear();
		this.wordCount = 0;
	}
    public int getFileCount(){
        return this.fileCount;
    }
	public void setFileCount(int count){
		this.fileCount = count; 	
	}
}