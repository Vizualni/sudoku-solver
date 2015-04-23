package org.matijamartinic.sudokusolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.matijamartinic.sudoku.PairXY;
import org.matijamartinic.sudoku.SudokuBitField;
import org.matijamartinic.sudoku.SudokuBoardSmaller;

public class SudokuSolver {
	
	public static void main(String []args) throws IOException{
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("/tmp/1616.dat")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		String line = null;
		int c=0;
		long ukupnoVrijeme = 0; 
		PrintWriter pw = new PrintWriter("/tmp/solutions.dat");
		while ((line = br.readLine()) != null) {
			SudokuSolver ss = SudokuSolver.createFromString(line);
			
			long startTime = System.nanoTime();
			if(ss.solve()){
				long endTime = System.nanoTime();
				c++;
				System.out.println(c + " time: " + (endTime- startTime)/1000000000.0 + " s");
				ukupnoVrijeme += endTime- startTime;
				pw.println(ss.solution.toLine());
				ss.solution.print();
			}else{
				System.out.println("nije");
			}
		}
		pw.close();
		System.out.println("rjeseno: "+c + ", ukupno vrijeme: "+ukupnoVrijeme/1000000000.0 + " s");
		br.close();
		
		
	}

	private SudokuBoardSmaller board = null;
	private SudokuBoardSmaller solution = null;
	
	public SudokuSolver(SudokuBoardSmaller board){
		if(board==null){
			throw new IllegalArgumentException();
		}
		this.board = board;
		//this.board.print();
	}
	
	public boolean solve(){	
		//this.board.print();
		if(this.board.solveEasyOnes()==false){
			return false;
		}
		solution = SudokuSolver.solveUsingDFS(this.board);
		return solution!=null;
		
	}
	
	public String getSolution(){
		StringBuilder sol = new StringBuilder();
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				sol.append(SudokuBitField.getNumber(this.solution.getNumbers(i, j)) + "");
			}
		}
		return sol.toString();
	}
	
	public void print(){
		this.board.print();
	}
	
	public static SudokuSolver createFromString(String line){
		System.out.println(line);
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
		int possibleNumbers = board.getNumbers(x, y);
		SudokuBoardSmaller copyBoard = null;
		for(int i=1; i<=SudokuBitField.getAllNumbers(); i=i<<1){
			int possibleNumber = possibleNumbers & i;
			if(possibleNumber==0){
				continue;
			}
			copyBoard = board.copy();

			if(copyBoard.setNumber(possibleNumber, x, y)==false){
				continue;
			}
			SudokuBoardSmaller possibleSolution = solveUsingDFS(copyBoard);
			if(possibleSolution!=null){
				return possibleSolution;
			}
			
		}
		return null;
	}
	
	
}
