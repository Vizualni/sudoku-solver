package org.matijamartinic.sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;



public class SudokuField {
	
	private int fieldValue = 0;
	private int maybeNumber;
	private boolean changed = false;
	
	private Stack<Integer> history = null;
	
	private Set<Integer> possibleNumbers = new HashSet<Integer>();
	
	public SudokuField(int value) throws IllegalArgumentException{
		this.checkForNumber(value);
		this.fieldValue = value;
		history = new Stack<Integer>();
	}
	
	public boolean setMaybeNumber(int number) throws IllegalArgumentException{
		this.checkForNumber(number);
		if(possibleNumbers.contains(number) && this.fieldValue==0){
			this.maybeNumber = number;
			this.changed = true;
			return true;
		}
		return false;
	}
	
	public void addPossibleNumber(int number){
		this.possibleNumbers.add(number);
	}
	
	private void checkForNumber(int number) throws IllegalArgumentException{
		if(number<0 || number>9){
			throw new IllegalArgumentException();
		}
	}
	
	public Set<Integer> getPossibleNumbers(){
		return this.possibleNumbers;
	}
	
	public boolean isFieldSet(){
		return this.fieldValue!=0;
	}

	public void enterNewState() {
		if(this.isFieldSet()){
			return;
		}
		history.push(this.maybeNumber);
		this.maybeNumber = 0;
		
	}

	public void goBackOneStep() {
		if(this.isFieldSet()){
			return;
		}
		this.maybeNumber = history.pop();
		
	}
	
	public void resetPossibleNumbers(){
		this.possibleNumbers.clear();
	}
	
	public int getCurrentNumber(){
		if(this.isFieldSet()){
			return fieldValue;
		}
		return this.maybeNumber;
	}
	
	
}
