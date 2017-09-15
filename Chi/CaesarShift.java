package Calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CaesarShift {
	private HashMap<Integer,String> decryptions= new HashMap<Integer,String>();
	private HashMap<Integer, ArrayList<Integer>> shifts= new HashMap<Integer,ArrayList<Integer>>();
	private int[] chiNums=new int[25];
	public static void main(String[]args){
		new CaesarShift("GXGTJUSHATINULRKZZKXYZUZKYZINOYWAGXKGTJOZYLXKWAKTIEGTGREYOY").printChi();

	}
	public CaesarShift(String cipherText){
		double[] expecteds=CodeCalc.EnglishFrequency;//List of decimal numbers representing frequency of letters
		int[] expected= new int[26];//integer number representing expected number of each letter that will occur in text
		int length=cipherText.length();//used to control for loop
		for(int i=0;i<expected.length;i++){
			expected[i]=(int) (expecteds[i]/100*length);//calculates expected number of each letter using frequency formula
		}
		for(int shift=1;shift<=25;shift++){//for each type of shift, s, from 1-25
			ChiSquare chi= new ChiSquare(CodeCalc.shiftFreq(CodeCalc.letterFreq(cipherText), shift),expected);//calculate chi square
			decryptions.put(shift, deCipher(cipherText.toLowerCase(),shift));//rest is all to store values
			if(shifts.containsKey(chi.getValue())){
				shifts.get(chi.getValue()).add(shift);
			}
			else{
				shifts.put(chi.getValue(), new ArrayList<Integer>());
				shifts.get(chi.getValue()).add(shift);
			}
			chiNums[shift-1]=chi.getValue();
		}
		Arrays.sort(chiNums);
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
	public void printChi(){
		int lastI=0;
		int index=0;
		for(int i:chiNums){
			if(i==lastI){
				index++;
			}
			else{
				index=0;
			}
			System.out.println(decryptions.get(shifts.get(i).get(index)).toUpperCase()+"\tshift"+shifts.get(i).get(index)+"\tchi"+i);
			lastI=i;
			
		}
	}
}
