package Calc;

public class ChiSquare {
	int ChiSquare=0;
	public ChiSquare(int[] actual,int[] expected){
		ChiSquare=doChiSquare(actual,expected);
	}
	public int doChiSquare(int[] actual,int[] expected){// 2 arrays 26 long representing the expect
		int ChiSquare=0;//default value of chi square is always changed in the next block
		for(int i=0;i<actual.length;i++){//goes through each letter of the alphabet
				if(expected[i]==0){//to avoid incorrect divide by zero 
					expected[i]=1;
				}
				ChiSquare+=(Math.pow((actual[i]-expected[i]), 2)/expected[i]);//chi square formula
		}
		return ChiSquare;//sum of chi square formula done on all letters returned
	}
	public int getValue(){
		return ChiSquare;
	}
}
