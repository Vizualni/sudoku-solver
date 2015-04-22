package org.matijamartinic.sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;



public class SudokuField {
	
	private int fieldValue = 0;
	private int maybeNumber = 0;
	private int x, y;
	private boolean changed = false;
	private int possibleNumbersSize = 0;
	private Stack<Integer> history = null;
	
	private Set<Integer> possibleNumbers = new HashSet<Integer>();
	private Set<Integer> blackListedNumbers = new HashSet<Integer>();
	
	public SudokuField(int value, int x, int y) throws IllegalArgumentException{
		this.x = x;
		this.y = y;
		this.checkForNumber(value);
		this.fieldValue = value;
		history = new Stack<Integer>();
	}
	
	public String getXY(){
		return x + ", "+ y;
	}
	
	public boolean setMaybeNumber(int number) throws IllegalArgumentException{
		this.checkForNumber(number);
		if(possibleNumbers.contains(number) && this.isFieldSet()==false){
			this.maybeNumber = number;
			possibleNumbers.remove(number);
			possibleNumbersSize--;
			return true;
		}
		return true;
	}
	
	private void checkForNumber(int number) throws IllegalArgumentException{
		if(number<0 || number>9){
			throw new IllegalArgumentException();
		}
	}
	
	public Set<Integer> getPossibleNumbers(){
		return new HashSet<Integer>(this.possibleNumbers);
	}
	
	public int getPossibleNumbersSize(){
		if(this.possibleNumbers.size()==0 && this.isMaybeSet()==false){
		}
		return this.possibleNumbers.size();
	}
	
	public boolean isFieldSet(){
		return this.fieldValue!=0;
	}
	
	public boolean isMaybeSet(){
		return this.maybeNumber!=0;
	}
	
	public boolean hasNumberSet(){
		return this.isFieldSet() || this.isMaybeSet();
	}

	public void enterNewState() {
		if(this.isFieldSet()){
			return;
		}
		history.push(this.maybeNumber);
		//this.applyBlacklisted();
		
	}

	public void goBackOneStep() {
		if(this.isFieldSet()){
			return;
		}
		this.maybeNumber = history.pop();
		this.applyBlacklisted();
		
	}
	
	public void resetPossibleNumbers(){
		if(this.isFieldSet()){
			return;
		}
		this.possibleNumbers = new HashSet<Integer>(SudokuBoard.allNumbers);
		//this.applyBlacklisted();
	}
	
	public void addBlackListed(int n){
		this.blackListedNumbers.add(n);
		this.applyBlacklisted();
		if(this.maybeNumber == n){
			//this.maybeNumber = 0;
		}
	}
	
	public boolean removeNumberFromPossibleNumbers(int number){
		if(this.possibleNumbers.contains(number)){
			this.possibleNumbers.remove(number);
			this.possibleNumbersSize--;
			return true;
		}
		return false;
	}
	
	public int getCurrentNumber(){
		if(this.isFieldSet()){
			return fieldValue;
		}
		return this.maybeNumber;
	}
	
	@Override
	public String toString(){
		return this.getCurrentNumber() + "";// + this.possibleNumbers;
	}

	public void applyBlacklisted() {
		this.possibleNumbers.removeAll(this.blackListedNumbers);
		this.possibleNumbersSize = this.possibleNumbers.size();		
	}
	
	
}
