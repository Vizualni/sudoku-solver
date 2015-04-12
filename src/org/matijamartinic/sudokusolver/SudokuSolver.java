package org.matijamartinic.sudokusolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.matijamartinic.sudoku.SudokuBoard;
import org.matijamartinic.sudoku.SudokuField;
import org.matijamartinic.sudoku.exception.SudokuIncorrectSetup;

public class SudokuSolver {
	
	public static void main(String []args) throws IOException{
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("/tmp/sudokus.dat")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		String line = null;
		int c=0;
		while ((line = br.readLine()) != null) {
			SudokuSolver ss = SudokuSolver.createFromString(line);
			
			try {
				if(ss.solve()){
					c++;
					System.out.println(c);
				}else{
					System.out.println("nije");
				}
				//ss.print();
			} catch (SudokuIncorrectSetup e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ss.print();
		}
		System.out.println("rjeseno: "+c);
		br.close();
		
		
	}

	private SudokuBoard board = null;
	private boolean cancel = false;
	
	public SudokuSolver(SudokuBoard board){
		if(board==null){
			throw new IllegalArgumentException();
		}
		this.board = board;
		this.cancel = false;
		//this.board.print();
	}
	
	public boolean solve() throws SudokuIncorrectSetup{	
		
		if(board.isValid() == false){
			//throw new SudokuIncorrectSetup();
		}
		solveEasyOnes();
		return this.solveUsingDFS();
		
	}
	
	public void print(){
		this.board.print();
	}
	
	private boolean solveUsingDFS(){
		// pronaci neko polje gdje ih ima najmanje onih mogucih
		// staviti to jedno polje 
		// provjeriti ako ima kontradikcije
		// ako ima, onda se vracam u stanje prije i vrtim dalje
		// ako nema, onda je to mozda dobra kombinacija
		// i od tih "dobrih" kombinacija idem na vrh loopa
		// i trazim dalje ako je u redu sve
		
		if(this.board.isSolved()==true){
			//System.out.println("WTF");
			return true;
		}
		
		if(this.cancel == true){
			return false;
		}
		SudokuField minimumField = board.getMinimumField();
		Set<Integer> possibleNumbers = new HashSet<Integer>(minimumField.getPossibleNumbers());
		for(int possibleNumber: possibleNumbers){
			this.board.enterIntoNewState(); // something like copying board
			minimumField.setMaybeNumber(possibleNumber);
			this.board.calculateNewBoardStatus();
			//this.solveEasyOnes();
			//this.board.calculateNewBoardStatus();
			if(this.board.isValid()==false){
				//System.out.println("efektivo usao ovdje");
				//print();
				this.board.goBackOneState();
				this.board.calculateNewBoardStatus();
				continue;
				//print();
			}
			if(this.solveUsingDFS()){
				return true;
			}else{
				this.board.goBackOneState();
				this.board.calculateNewBoardStatus();
			}
			
		}
		return false;
	}
	
	public void cancelAction(){
		this.cancel=true;
	}
		
	private void solveEasyOnes(){
		SudokuField one;
		boolean atLeastOne = false;
		for(int x=0; x<this.board.SIZE; x++){
			for(int y=0; y<this.board.SIZE; y++){
				one = this.board.get(x,y);
				if(one.getPossibleNumbers().size()==1 && one.hasNumberSet()==false){
					Integer number = (Integer)one.getPossibleNumbers().toArray()[0];
					one.setMaybeNumber(number);
					atLeastOne = true;
				}
			}
		}
		if(atLeastOne){
			this.board.calculateNewBoardStatus();
			//print();
			solveEasyOnes();
		}
	}
	
	
	public static SudokuSolver createFromString(String numbers){
		if(numbers.length()!=SudokuBoard.SIZE*SudokuBoard.SIZE){
			throw new IllegalArgumentException();
		}
		int c = 0;
		int nums[][] = new int[9][9];
		for(int x=0; x<SudokuBoard.SIZE; x++){
			for (int y = 0; y < SudokuBoard.SIZE; y++) {
				nums[x][y] = Integer.parseInt(Character.toString(numbers.charAt(c)));
				c++; // you wish
			}
		}
		return new SudokuSolver(new SudokuBoard(nums));
	}
	
}
