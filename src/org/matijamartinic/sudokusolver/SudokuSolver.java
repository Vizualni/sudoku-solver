package org.matijamartinic.sudokusolver;

import org.matijamartinic.sudoku.SudokuBoard;
import org.matijamartinic.sudoku.SudokuField;
import org.matijamartinic.sudoku.exception.SudokuIncorrectSetup;

public class SudokuSolver {

	private SudokuBoard board = null;
	private boolean cancel = false;
	public SudokuSolver(SudokuBoard board){
		if(board==null){
			throw new IllegalArgumentException();
		}
		this.board = board;
		this.cancel = false;
	}
	
	public boolean solve() throws SudokuIncorrectSetup{	
		
		if(board.isValid() == false){
			throw new SudokuIncorrectSetup();
		}
		
		return true;
		
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
			return true;
		}
		
		if(this.cancel == true){
			return false;
		}
		
		SudokuField minimumField = board.getMinimumField();
		for(int possibleNumber: minimumField.getPossibleNumbers()){
			this.board.enterIntoNewState(); // something like copying board
			minimumField.setMaybeNumber(possibleNumber);
			this.board.calculateNewBoardStatus();
			this.solveEasyOnes();
			if(this.board.isValid()==false){
				this.board.goBackOneState();
			}else{
				if(this.solveUsingDFS()){
					return true;
				}
			}
		}
		return false;
	}
	
	public void cancelAction(){
		this.cancel=true;
	}
		
	private void solveEasyOnes(){
		
	}
}
