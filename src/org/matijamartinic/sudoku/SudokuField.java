package org.matijamartinic.sudoku;

import java.util.HashSet;
import java.util.Set;



public class SudokuField {
	
	private int fieldValue;
	private int maybeNumber;
	private Set<Integer> possibleNumbers = new HashSet<Integer>();
	
	public SudokuField(int value) throws IllegalArgumentException{
		this.checkForNumber(value);
		this.fieldValue = value;
	}
	
	public boolean setMaybeNumber(int number) throws IllegalArgumentException{
		this.checkForNumber(number);
		if(possibleNumbers.contains(number) && this.fieldValue==0){
			this.maybeNumber = number;
			return true;
		}
		return false;
	}
	
	public void addPossibleNumber(int number){
		this.checkForNumber(number);
		this.possibleNumbers.add(number);
	}
	
	private void checkForNumber(int number) throws IllegalArgumentException{
		if(number<0 || number>9){
			throw new IllegalArgumentException();
		}
	}
	
	
}
