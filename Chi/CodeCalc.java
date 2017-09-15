package Calc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import XML_Tests.Elements;
import XML_Tests.XMLStAXFile;

public class CodeCalc {
	public static double[] EnglishFrequency= new double[]{8.12,1.49,2.71,4.32,12.02,2.30,2.03,5.92,7.31,0.10,0.69,3.98,2.61,6.95,7.68,1.82,0.11,6.02,6.28,9.10,2.88,1.11,2.09,0.17,2.11,0.07};
	public static XMLStAXFile stax= new XMLStAXFile(new File("/Users/Allen/Desktop/words.xml"));
	public static long startTime;
	public static long timeOut=Long.parseLong("100000000000");
	public static boolean[] failed;
	public static int[] letterFreq(String cipherText){
		int[] freq= new int[26];
		Arrays.fill(freq, 0);
		char[] text=cipherText.toUpperCase().toCharArray();
		for(char c:text){
			try {
				freq[CodeCalc.charToInt(c)]++;
			} catch (Exception e) {}
		}
		return freq;
	}
	public static int[] expectedFreq(String cipherText,double[] expecteds){
		int[] expected= new int[26];
		int length=cipherText.length();
		for(int i=0;i<expected.length;i++){
			expected[i]=(int) (expecteds[i]/100*length);
		}
		return expected;
	}
	public static int charToInt(char c){
		switch (c){
		case 'A': return 0;
		case 'B': return 1;
		case 'C': return 2;
		case 'D': return 3;
		case 'E': return 4;
		case 'F': return 5;
		case 'G': return 6;
		case 'H': return 7;
		case 'I': return 8;
		case 'J': return 9;
		case 'K': return 10;
		case 'L': return 11;
		case 'M': return 12;
		case 'N': return 13;
		case 'O': return 14;
		case 'P': return 15;
		case 'Q': return 16;
		case 'R': return 17;
		case 'S': return 18;
		case 'T': return 19;
		case 'U': return 20;
		case 'V': return 21;
		case 'W': return 22;
		case 'X': return 23;
		case 'Y': return 24;
		case 'Z': return 25;
		}
		return -1;
	}
	public static char intToChar(int i){
		switch(i){
		case 0:return 'A';
		case 1:return 'B';
		case 2:return 'C';
		case 3:return 'D';
		case 4:return 'E';
		case 5:return 'F';
		case 6:return 'G';
		case 7:return 'H';
		case 8:return 'I';
		case 9:return 'J';
		case 10:return 'K';
		case 11:return 'L';
		case 12:return 'M';
		case 13:return 'N';
		case 14:return 'O';
		case 15:return 'P';
		case 16:return 'Q';
		case 17:return 'R';
		case 18:return 'S';
		case 19:return 'T';
		case 20:return 'U';
		case 21:return 'V';
		case 22:return 'W';
		case 23:return 'X';
		case 24:return 'Y';
		case 25:return 'Z';
		}
		return 'A';
	}
	public static int[] shiftFreq(int[] frequency,int shift){
		int[] freq= new int[26];
		int c=0;
		for(int i=shift;i<frequency.length;i++){
			freq[c]=frequency[i];
			c++;
		}
		int i=0;
		while(c<26){
			freq[c]=frequency[i];
			c++;
			i++;
		}
		return freq;
	}
	@SuppressWarnings("resource")
	public static void loadDictionary(File file) throws IOException{
		Elements doc= new Elements("dictionary");
		FileReader fileReader =  new FileReader(file);
		BufferedReader bufferedReader =  new BufferedReader(fileReader);
		long index=0;
		while(bufferedReader.ready()){
			index++;
			String alpha=bufferedReader.readLine();
			try {
				//start index
				if(index>0&&Character.isLetter(alpha.charAt(0))&&Character.isLetter(alpha.charAt(1))&&alpha.length()>1){
					String code=alpha.substring(0, 2);
					Elements codeKey;
					try {
						codeKey = doc.getChilds(code).get(0);
						codeKey.setText(codeKey.getText()+","+alpha);
					} catch (Exception e) {
						System.out.println(index);
						Elements ele=new Elements(code);
						ele.setText(""+alpha);
						doc.getChilds().add(ele);
					}
					
				}
				
				//index to
				if(index>50000){
					//break;//comment out to probe or go to end
				}
			} catch (Exception e) {}
		}
		XMLStAXFile stax= new XMLStAXFile();
		stax.writeNewXMLFile(new File("/Users/Allen/Desktop/words1.xml"));
		stax.startWriter();
		stax.writeElement(doc);
		stax.endWriter();
	}
	public static boolean isStringWord(String test){
		stax.readXMLFile();
		failed= new boolean[test.length()];
		test=test.toLowerCase();
		//case if it's an a/i, single char words, in pass on to test 
		try {
			if(test.startsWith("a")||test.startsWith("i")){
				if(test.length()==1){
					return true;
				}
				if(isStringWord(test,1)){
					return true;
				}
			}

			String code=test.substring(0, 2);
			String[] entries=getEntries(code);
			for(String entry:entries){
				if(test.startsWith(entry)){
					if(isStringWord(test,entry.length())){
						return true;
					}
				}
			}
		} catch (NullPointerException e) {}



		return false;
	}
	public static boolean isStringWord(String test, int index){
		test=test.toLowerCase();
		
		//case if it's an a/i, single char words, in pass on to test 
		try {
			if(failed[index]){
				return false;
			}
			if(test.startsWith("a",index)||test.startsWith("i",index)){
				if(test.length()-index==1){
					return true;
				}
				if(isStringWord(test,1+index)){
					return true;
				}
			}
			
				String code=test.substring(index, index+2);
				//System.out.print(code+".");
				String[] entries=getEntries(code);
				for(String entry:entries){
					if(test.startsWith(entry,index)){
						if(test.length()-index==entry.length()){
							return true;
						}
						else if(isStringWord(test,entry.length()+index)){
							return true;
						}
					}
				}
		} catch (Exception e) {}
		//System.out.println("false case");
		failed[index]=true;
		return false;
	}
	public static String[] getEntries(String code)throws NullPointerException{
		stax.resetReader();
		String[] test;
		try {
			Elements ele=stax.parseToElements(code).get(0);
			test = ele.getText().split(",");
			return test;
		} catch (Exception e) {
			return null;
		}
		
	}
	//old code to analyze an older dicitonary version
	/*public static ArrayList<String> getEntries(String code){
		stax.resetReader();
		ArrayList<String> test= new ArrayList<String>();
		ArrayList<Elements> eles = null;
		try {
			eles = stax.parseToElements(code).get(0).getChilds();
		for(Elements child:eles){
			test.add(child.getText());
		}
		} catch (Exception e) {}
		return test;
	}*/
}
