package org.matijamartinic.sudoku;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class SudokuBoard implements Iterator<SudokuField>, Iterable<SudokuField>{
	
	final static public int SIZE = 9;
	final public static Set<Integer> allNumbers = new HashSet<Integer>();
	private int numberOfFields = 0;
	private Vector<Vector<SudokuField>> board = new Vector<Vector<SudokuField>>();
	
	private int iteratorX, iteratorY;
	
	public SudokuBoard(int numbers[][]) throws IllegalArgumentException{
		this.checkForInputBoard(numbers);
		Vector<SudokuField>row = null;
		int x=0, y=0;
		for(int rowArray[]: numbers){
			row = new Vector<SudokuField>();
			for(int number: rowArray){
				row.add(new SudokuField(number, x, y));
				this.numberOfFields+= number>0?1:0;
				y = (y+1)%9;
			}
			x++;
			this.board.add(row);
		}
		// Filling allNumbers
		for(int i=1; i<=9; i++){
			this.allNumbers.add(i);
		}
		this.calculateNewBoardStatus();
		
		
	}
	
	public SudokuField get(int x, int y) throws IllegalArgumentException{
		if(x<0 || y<0 || x>=SIZE || y>=SIZE){
			throw new IllegalArgumentException();
		}
		return this.board.get(x).get(y);
	}
	
	private void checkForInputBoard(int numbers[][]) throws IllegalArgumentException{
		if(numbers.length!=SIZE){
			throw new IllegalArgumentException();
		}
		for(int row[]: numbers){
			if(row.length!=SIZE){
				throw new IllegalArgumentException();
			}
		}
		return; // everything is okay
	}
	
	public int getNumberOfFields(){
		int counter = 0;
		for(SudokuField n: this){
			if(n.getCurrentNumber()>0){
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * 
	 */
	public void enterIntoNewState(){
		for(SudokuField field: this){
			field.enterNewState();
		}
	}
	
	public void goBackOneState(){
		for(SudokuField field: this){
			field.goBackOneStep();
		}
	}
	
	public void resetFields(){
		int counter = 0;
		for(SudokuField field: this){
			counter++;
			field.resetPossibleNumbers();
		}
	}
	
	public void calculateNewBoardStatus(){
		this.resetFields();
		
		// first for each row
		this.calculateNewBoardStatusRow();
		
		// columns
		this.calculateNewBoardStatusColumn();
		
		// 3x3 boxes
		this.calculateNewBoardStatus3x3Box();
	}
	
	private void calculateNewBoardStatusRow(){
		Set<Integer> set = new HashSet<Integer>();;
		for(int x=0; x<this.SIZE; x++){
			set.clear();
			for(int y=0; y<this.SIZE; y++){ // finding all numbers
				int value = this.get(x, y).getCurrentNumber();
				if(value>0){
					set.add(value);
				}
			}
			for(int y=0; y<this.SIZE; y++){ // finding all numbers
				for(int number: set){
					this.get(x, y).removeNumberFromPossibleNumbers(number);
				}
			}
			
		}
	}
	
	private void calculateNewBoardStatusColumn(){
		Set<Integer> set = new HashSet<Integer>();
		for(int x=0; x<this.SIZE; x++){
			set.clear();
			for(int y=0; y<this.SIZE; y++){ // finding all numbers
				int value = this.get(y, x).getCurrentNumber();
				if(value>0){
					set.add(value);
				}
			}
			for(int y=0; y<this.SIZE; y++){ // finding all numbers
				for(int number: set){
					this.get(y, x).removeNumberFromPossibleNumbers(number);
				}
			}
			
		}
	}
	
	private void calculateNewBoardStatus3x3Box(){
		Vector<Set<Integer>> boxes = new Vector<Set<Integer>>();
		for (int i = 0; i < this.SIZE; i++) {
			boxes.add(new HashSet<Integer>());
		}
		
		int index;
		SudokuField field;
		for (int x = 0; x < this.SIZE; x++) {
			for (int y = 0; y < this.SIZE; y++) {
				index = (x/3)*3 + y/3;
				field = this.get(x, y);
				boxes.get(index).add(field.getCurrentNumber());
			}
		}
		
		for (int x = 0; x < this.SIZE; x++) {
			for (int y = 0; y < this.SIZE; y++) {
				index = (x/3)*3 + y/3;
				field = this.get(x, y);
				
				for(int number: boxes.get(index)){
					field.removeNumberFromPossibleNumbers(number);
				}
			}
		}
		
	}
	
	public SudokuField getMinimumField(){
		SudokuField min = null;
		
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				if( this.get(x,y).hasNumberSet() == false &&
					(
						min==null ||
						this.get(x,y).getPossibleNumbersSize()<min.getPossibleNumbersSize()
					)
				  ){
					min = this.get(x,y);
				}
				//System.out.println(this.get(x, y).getPossibleNumbers().size() + " "+min.getPossibleNumbers().size());
			}
		}
		//System.out.println("milodar" + min.getPossibleNumbers());
		return min;
	}
	
	public boolean isValid(){
		this.calculateNewBoardStatus();
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				if(this.get(x, y).isFieldSet()==false && this.get(x, y).isMaybeSet()==false && this.get(x, y).getPossibleNumbersSize()==0){
					return false;
				}
			}
		}
		if(true)
		return true;
		
		Set<Integer> one = null;
		for(int x=0; x<this.SIZE; x++){
			one = new HashSet<Integer>();
			for(int y=0; y<this.SIZE; y++){
				int value = this.get(x, y).getCurrentNumber();
				if(value>0){
					if(one.contains(value)){
						return false;
					}
					one.add(value);
				}
			}
		}
		
		for(int x=0; x<this.SIZE; x++){
			one = new HashSet<Integer>();
			for(int y=0; y<this.SIZE; y++){
				int value = this.get(y, x).getCurrentNumber();
				if(value>0){
					if(one.contains(value)){
						return false;
					}
					one.add(value);
				}
			}
		}
		
		Vector<Set<Integer>> boxes = new Vector<Set<Integer>>();
		for (int i = 0; i < this.SIZE; i++) {
			boxes.add(new HashSet<Integer>());
		}
		
		int index;
		SudokuField field;
		for (int x = 0; x < this.SIZE; x++) {
			for (int y = 0; y < this.SIZE; y++) {
				index = (x/3)*3 + y/3;
				field = this.get(x, y);
				int value = field.getCurrentNumber();
				if(value>0){
					if(boxes.get(index).contains(field.getCurrentNumber())){
						return false;
					}
					boxes.get(index).add(field.getCurrentNumber());
				}
			}
		}
		
		return true;
	}
	
	public boolean isSolved(){
		//System.out.println(this.getNumberOfFields());
		if(this.getNumberOfFields()==this.SIZE*this.SIZE){
			return isValid();
		}
		return false;
	}
	
	
	public void print(){
		for(int x=0; x<this.SIZE; x++){
			if(x>0 && x%3==0){
				System.out.println("-----------------------");
			}
			for(int y=0; y<this.SIZE; y++){
				if(y>0 && y%3==0){
					System.out.print("| ");
				}
				System.out.print(this.get(x,y)+" ");
			}
			System.out.println();
			
		}
		System.out.println();
	}
	
	////////////////
	/// ITERATOR ///
	////////////////
	
	private void resetIterator(){
		this.iteratorX = this.iteratorY = 0;
	}
	
	@Override
	public boolean hasNext() {
		return this.iteratorX<9 && this.iteratorY<9;
	}

	@Override
	public void remove() {
		// OH RLY?
		// NO!
	}

	@Override
	public SudokuField next() {
		SudokuField f = this.get(this.iteratorX, this.iteratorY);
		if(++this.iteratorY>=9){
			this.iteratorX++;
			this.iteratorY=0;
		}
		return f;
	}
	
	@Override
	public Iterator<SudokuField> iterator() {
		this.resetIterator();
		return this;
	}
	
}
