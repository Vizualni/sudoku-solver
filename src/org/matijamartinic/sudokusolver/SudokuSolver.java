package org.matijamartinic.sudokusolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.matijamartinic.sudoku.PairXY;
import org.matijamartinic.sudoku.SudokuBoard;
import org.matijamartinic.sudoku.SudokuBoardSmaller;
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
		long ukupnoVrijeme = 0;
		while ((line = br.readLine()) != null) {
			SudokuSolver ss = SudokuSolver.createFromString(line);
			
			long startTime = System.nanoTime();
			if(ss.solve()){
				c++;
				long endTime = System.nanoTime();
				System.out.println(c + " time: " + (endTime- startTime)/1000000000.0 + " s");
				ukupnoVrijeme += endTime- startTime;
				//ss.solution.print();
			}else{
				System.out.println("nije");
			}
		}
		System.out.println("rjeseno: "+c + ", ukupno vrijeme: "+ukupnoVrijeme/1000000000.0 + " s");
		br.close();
		
		
	}

	private SudokuBoardSmaller board = null;
	private SudokuBoardSmaller solution = null;
	private boolean cancel = false;
	
	public SudokuSolver(SudokuBoardSmaller board){
		if(board==null){
			throw new IllegalArgumentException();
		}
		this.board = board;
		this.cancel = false;
		//this.board.print();
	}
	
	public boolean solve(){	
		//this.board.print();
		solution = SudokuSolver.solveUsingDFS(this.board);
		return solution!=null;
		
	}
	
	public String getSolution(){
		StringBuilder sol = new StringBuilder();
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				sol.append(this.solution.getNumbers(i, j).toArray()[0]);
			}
		}
		return sol.toString();
	}
	
	public void print(){
		this.board.print();
	}
	
	public static SudokuSolver createFromString(String line){
		return new SudokuSolver(SudokuBoardSmaller.createFromString(line));
	}
	
	private static SudokuBoardSmaller solveUsingDFS(SudokuBoardSmaller board){
		// pronaci neko polje gdje ih ima najmanje onih mogucih
		// staviti to jedno polje 
		// provjeriti ako ima kontradikcije
		// ako ima, onda se vracam u stanje prije i vrtim dalje
		// ako nema, onda je to mozda dobra kombinacija
		// i od tih "dobrih" kombinacija idem na vrh loopa
		// i trazim dalje ako je u redu sve
		//System.out.println(board.getSize());
		if(board.getSize()==SudokuBoardSmaller.SIZE2){
			//board.print();
			return board;
		}
		
		PairXY minimumField = board.getMinimumField();
		int x = minimumField.x;
		int y = minimumField.y;
		//System.out.println(""+x + " " + y);
		HashSet<Byte> possibleNumbers = board.getNumbers(x, y);
		SudokuBoardSmaller copyBoard = null;
		for(int possibleNumber: possibleNumbers){
			copyBoard = board.copy();

			if(copyBoard.setNumber((byte) possibleNumber, x, y)==false){
				continue;
			}
			SudokuBoardSmaller possibleSolution = solveUsingDFS(copyBoard);
			if(possibleSolution!=null){
				return possibleSolution;
			}
			
		}
		return null;
	}
	
	
	public void cancelAction(){
		this.cancel=true;
	}
	
}
