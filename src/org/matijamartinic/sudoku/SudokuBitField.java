package org.matijamartinic.sudoku;

public class SudokuBitField {

	public static int setNumber(int bitField, int number){ return setBitToBitField(bitField, (1<<(number-1))); }
	
	public static int removeNumber(int bitField, int number){ return removeBitFromBitField(bitField, (1<<(number-1))); }
	
	public static int getSize(int bitField){
		int i = bitField;
		i = i - ((i >> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
		return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;	
	}
	
	public static int getAllNumbers(){ return 0x0000ffff; }

	public static int setBitToBitField(int bitField, int bitToSet){ return bitField | bitToSet; }

	public static int removeBitFromBitField(int bitField, int bitToRemove){return bitField & ~bitToRemove;}
	
	public static boolean isEmpty(int bitField){return bitField==0;}
	
	public static int getNumber(int bitField){
		for(int i=1; i<=SudokuBoardSmaller.SIZE; i++)
			if((bitField & (1<<(i-1)))>0){
				return i;
		}
		return 0;
	}
	
	public static String toString(int bitField){
		String res="";
		for(int i=1; i<=SudokuBoardSmaller.SIZE; i++)
			if((bitField & (1<<(i-1)))>0){
				res +=i;
		}
		return res;
	}
	
}
