package Calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class VigenereCipher {
	private HashMap<String,String> decryptions= new HashMap<String,String>();
	private HashMap<Integer, ArrayList<String>> shifts= new HashMap<Integer,ArrayList<String>>();
	private int[] chiNums= new int[0];
	private int chiIndex=0;
	public static void main(String[]args){
		//System.out.println(CodeCalc.isStringWord("ARANOMSTRINGOFLETTERSANDNUMBERSTHATDONOTMATTERWHATHECOMBINATIONIS"));
		long timeStart=System.currentTimeMillis();
		//System.out.println(CodeCalc.isStringWord("abothsrpodulafsyshemcolleradiograamatwccidherisedpymabychwldrsningchoclsupstihutegsympolstorlsttefsingtearofohherzettsrstvissmsteaisibesssncehhesomeagthezettsrsupstihuticnsygtem"));
		new VigenereCipher("AKAYDHMMUGCSOYLPTMECSMOEELTNHBSBUTRPAGDTTLFCEJUPNVYLNTLJSBS","");
		long end=System.currentTimeMillis();
		long time=end-timeStart;
		System.out.println(time/1000+":"+time%1000);
	}
	public VigenereCipher(String cipherText,String contains){
		int size=0;
		for(int i=0;i<5;i++){
			size+=Math.pow(26, i);
		}
		chiNums=new int[size];
		for(int i=0;i<4;i++){
			doVigenereCipher(cipherText,i+1,contains);
		}
		this.printChi();
	}
	//public VigenereCipher(String cipherText, String key){
		//System.out.println(deVigenereCipher(cipherText,keyToInt(key)));
	//}
	//String regex="(?<=\\G.{"+keyLength+"})";
	//String[] text=cipherText.split(regex);
	public void doVigenereCipher(String cipherText, int keyLength,String contains){
		int[] key=new int[keyLength];//generates array of number representing keyword
		boolean continues=true;//setting loop
		while(continues){//loops until all keys have been tested
			try {
				key=incrementKey(key);//increments key such that every possible key is tested
				chiIndex++;//setting array to store data
				String text=deVigenereCipher(cipherText,key);//getting plaintext
				if(text.contains(contains)){
				int chi=new ChiSquare(CodeCalc.letterFreq(text),CodeCalc.expectedFreq(text, CodeCalc.EnglishFrequency)).getValue();//calculates chi
				decryptions.put(keyToString(key), text);//all other stuff is to do with saving data
				if(shifts.containsKey(chi)){
					shifts.get(chi).add(keyToString(key));
				}
				else{
					shifts.put(chi, new ArrayList<String>());
					shifts.get(chi).add(keyToString(key));
				}
				chiNums[chiIndex]=chi;
				}
			} catch (Exception e) {continues=false;}
		}
	}
	public void printChi(){
		Arrays.sort(chiNums);
		int lastI=0;
		int index=0;
		int count=0;
		for(int i:chiNums){
			if(i!=0){
			if(i==lastI){
				index++;
			}
			else{
				index=0;
			}
			long timeStart=System.nanoTime();
			//System.out.println(decryptions.get(shifts.get(i).get(index)));
			if(CodeCalc.isStringWord(decryptions.get(shifts.get(i).get(index)))){
				count++;
			System.out.println((""+decryptions.get(shifts.get(i).get(index))).toUpperCase()+"\tshift"+shifts.get(i).get(index)+"\tchi"+i);
			}
			long end=System.nanoTime();
			long time=end-timeStart;
			//System.out.println(time/1000000000+":"+time%1000000000/1000000+":"+time%1000000/1000);
			lastI=i;
			
			}
			if(count>10){
				break;
			}
			
		}
	}
	public ArrayList<String[]> getChi(int amount){
		Arrays.sort(chiNums);
		ArrayList<String[]> returnA= new ArrayList<String[]>(amount);
		int lastI=0;
		int index=0;
		int count=0;
		for(int i:chiNums){
			if(i!=0){
			if(i==lastI){
				index++;
			}
			else{
				index=0;
			}
			
			String[] s=new String[]{(""+decryptions.get(shifts.get(i).get(index))),shifts.get(i).get(index),""+i};
			returnA.add(s);
			lastI=i;
			count++;
			}
			
			if(count>amount){
				break;
			}
			
		}
		return returnA;
	}
	public int[] incrementKey(int[] key){
		for(int i=key.length-1;i>=0;i--){//going from end of the array to the front
			if(key[i]==25){//if the current index of the key is at the limit of K=25
				key[i]=0;//changes it back to zero, goes to first num from end that is <25
			}
			else{
				key[i]++;//increments the current index of the key if it's not at the limit
				return key;//gives integer representation of keyword. 
			}
		}
		return null;//when all keys have been tested
	}
	public String[] recombine(String[] text){
		String[] recombine=new String[text[0].length()];
		Arrays.fill(recombine, "");
		for(int i=0;i<text[0].length();i++){
			for(String texts:text){
				try {
					recombine[i]+=texts.charAt(i);
				} catch (Exception e) {}
			}
		}
		return recombine;
	}
	
	public static ArrayList<int[]> generateKeys(int keyLength){
		ArrayList<int[]> keys= new ArrayList<int[]>();
		for(int i=0;i<26;i++){
			keys.add(new int[keyLength]);
			keys.get(i)[0]=i;//sets first value of key from 0-25
		}
		for(int i=1;i<keyLength;i++){
			addKey(i,keys);
		}
		return keys;
	}
	public static ArrayList<int[]> addKey(int depth,ArrayList<int[]> keys){
		int max=keys.size();
			for(int d=0;d<max;d++){
				keys.get(d)[depth]=0;
				for(int c=0;c<26;c++){
					//System.out.println("loop"+d+"   "+c+"   "+keys.size());
					int[] keyClone=keys.get(d).clone();
					keyClone[depth]=c;
					keys.add(keyClone);
				}
			}
		
		return keys;
	}
	private String deVigenereCipher(String msg,int[] key){
		char[] text=msg.toLowerCase().toCharArray();
		int index=0;
		int length=key.length;
		String s="";
		for(int i=0;i<text.length;i++){
			char c = (char)(text[i] - key[index]);
			if (c< 'a')
				s += (char)(text[i] + (26-key[index]));
			else
				s += (char)(text[i] - key[index]);
			if(index<length-1){
				index++;
			}
			else{
				index=0;
			}
		}
		return s;
	}
	private int[] keyToInt(String key){
		int[] keya= new int[key.length()];
		char[] text=key.toUpperCase().toCharArray();
		for(int i=0;i<key.length();i++){
			keya[i]=CodeCalc.charToInt(text[i]);
		}
		return keya;
	}
	private String keyToString(int[] key){
		String s="";
		for(int i=0;i<key.length;i++){
			s+=CodeCalc.intToChar(key[i]);
		}
		return s;
	}
	private String deCipher(String msg, int shift){
		String s = "";
		int len = msg.length();
		for(int x = 0; x < len; x++){
			char c = (char)(msg.charAt(x) - shift);
			if (c< 'a')
				s += (char)(msg.charAt(x) + (26-shift));
			else
				s += (char)(msg.charAt(x) - shift);
		}
		return s;
	}
}
