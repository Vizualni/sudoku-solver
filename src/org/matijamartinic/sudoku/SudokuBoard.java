package org.matijamartinic.sudoku;

import java.util.Vector;

public class SudokuBoard {
	
	final public int SIZE = 9;
	private int numberOfFields = 0;
	private Vector<Vector<SudokuField>> board = new Vector<Vector<SudokuField>>();
	
	public SudokuBoard(int numbers[][]) throws IllegalArgumentException{
		this.checkForInputBoard(numbers);
		Vector<SudokuField>row = null;
		for(int rowArray[]: numbers){
			row = new Vector<SudokuField>();
			for(int number: rowArray){
				row.add(new SudokuField(number));
				this.numberOfFields+= number>0?1:0;
			}
			this.board.add(row);
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
		return this.numberOfFields;
	}
	
	/**
	 * 
	 */
	public void enterIntoNewState(){
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				this.get(x, y).enterNewState();
			}
		}
	}
	
	public void goBackOneState(){
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				this.get(x, y).goBackOneStep();
			}
		}
	}
	
	public void calculateNewBoardStatus(){
		
	}
	
	public SudokuField getMinimumField(){
		SudokuField min = this.get(0, 0);
		
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				if(this.get(x,y).getPossibleNumbers().size()<min.getPossibleNumbers().size()){
					min = this.get(x,y);
				}
			}
		}
		
		return min;
	}
	
	public boolean isValid(){
		return true;
	}
	
	public boolean isSolved(){
		if(this.getNumberOfFields()==this.SIZE*this.SIZE){
			return isValid();
		}
		return false;
	}
	
	
}
