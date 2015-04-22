package org.matijamartinic.sudoku;

import java.util.HashSet;
import java.util.Set;

public class SudokuBoardSmaller {
	
	private HashSet<Byte> [][]numbers;
	
	private HashSet<Byte> [][]solution = null;
	
	final public static int SIZE=9;
	final public static int SIZE2 = SudokuBoardSmaller.SIZE*SudokuBoardSmaller.SIZE;
	
	private int size = 0;
	private boolean isset[][] = new boolean[SIZE][SIZE];
	
	
	public SudokuBoardSmaller(HashSet<Byte> [][]numbers){
		this.numbers = numbers;
		for (int i = 0; i < isset.length; i++) {
			for (int j = 0; j < isset[i].length; j++) {
				isset[i][j]=false;
			}
		}
	}
	
	private void setSolution(HashSet<Byte> [][]solution) {
		this.solution = solution;
	}
	
	public SudokuBoardSmaller(HashSet<Byte>[][] copyNums, boolean[][] copySets) {
		this(copyNums);
		this.isset = copySets;
	}

	private void set(int x, int y){
		this.isset[x][y] = true;
	}
	
	public boolean isSet(int x, int y){
		return this.isset[x][y];
	}
	

	
	public HashSet<Byte> getNumbers(int x, int y){
		return this.numbers[x][y];
	}
		
	public boolean setNumber(byte number, int x, int y){
		// bla bla x is less than 9...so as y
		
		this.numbers[x][y] = new HashSet<Byte>();
		this.numbers[x][y].add(number);
		this.set(x, y);
		boolean result = this.recalculate(number, x, y);
		if(result==false){
			return false;
		}
		return this.solveEasyOnes();
	}
	
	private boolean solveEasyOnes(){
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				if(this.getNumbers(x, y).size()==1 && this.isSet(x, y)==false){
					return this.setNumber(((Byte)this.getNumbers(x, y).toArray()[0]).byteValue(), x, y);
				}
			}
		}
		return true;
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
				System.out.print(this.numbers[x][y]+" ");
			}
			System.out.println();
			
		}
		System.out.println();
	}
	
	public PairXY getMinimumField(){
		HashSet<Byte> min = null;
		int minX=0, minY=0;
		
		for(int x=0; x<this.SIZE; x++){
			for(int y=0; y<this.SIZE; y++){
				if(min==null){
					if(this.isSet(x, y)==false){
						min = this.numbers[x][y];
						minX=x;
						minY=y;
					}
					continue;
				}
				if(this.getNumbers(x, y).size()<min.size() && this.isSet(x, y)==false){
					min = this.numbers[x][y];
					minX=x;
					minY=y;
				}
				
			}
		}
		PairXY p = new PairXY();
		p.x=minX;
		p.y=minY;
		return p;
	}

	private boolean recalculate(byte number, int x, int y){
		//recalculate rows, cols and 3x3's
		
		for(int i=0; i<this.SIZE; i++){
			if(i!=y){
				this.numbers[x][i].remove((byte) number);
				if(this.numbers[x][i].size()==0){
					return false;
				}
			}
		}
		
		for(int i=0; i<this.SIZE; i++){
			if(i!=x){
				this.numbers[i][y].remove((byte) number);
				if(this.numbers[i][y].size()==0){
					return false;
				}
			}
		}
		int startX = (x/3)*3; int startY = (y/3)*3;
		int endX = startX+3; int endY = startY+3;
		
		for(int i = startX; i<endX; i++){
			for(int j = startY; j<endY; j++){
				if(!(i==x && j==y)){
					this.numbers[i][j].remove((byte) number);
					if(this.numbers[i][j].size()==0){
						return false;
					}
				}
			}
		}
		
		setSize();
		
		return true;
	}
	
	public boolean isValid(){
		return true;
	}
	
	public SudokuBoardSmaller copy(){
		HashSet<Byte>[][] hashSets = new HashSet[SudokuBoardSmaller.SIZE][SudokuBoardSmaller.SIZE];
		HashSet<Byte> [][]copyNums = hashSets;
		boolean [][]copySets = new boolean[SIZE][SIZE];
		
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				copyNums[i][j] = new HashSet<Byte>(this.numbers[i][j]);
				copySets[i][j] = this.isset[i][j];
			}
		}
		
		return new SudokuBoardSmaller(copyNums, copySets);
	}
	
	
	private void setSize(){
		size = 0;
		for(int i=0; i<this.SIZE; i++){
			for(int j=0; j<this.SIZE; j++){
				if(this.isSet(i, j)){
					size++;
				}
			}
		}
	}
	
	public int getSize(){
		return size;
	}

	public static SudokuBoardSmaller createFromString(String line) {
		HashSet<Byte> [][]copyNums = new HashSet[SudokuBoardSmaller.SIZE][SudokuBoardSmaller.SIZE];
		HashSet<Byte> all = new HashSet<Byte>();
		all.add((byte) 1); all.add((byte) 2); all.add((byte) 3);
		all.add((byte) 4); all.add((byte) 5); all.add((byte) 6);
		all.add((byte) 7); all.add((byte) 8); all.add((byte) 9);
		
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				int index = i*9 + j;
				copyNums[i][j] = new HashSet<Byte>(all);
			}
		}
		
		SudokuBoardSmaller sss = new SudokuBoardSmaller(copyNums);
		
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){

				int index = i*9 + j; //fuckit!!
				if(line.charAt(index)!='0'){
					int num = Integer.valueOf(Character.valueOf(line.charAt(index)).toString());
					sss.setNumber((byte) num, i, j);
				}
				
			}
		}
		sss.setSize();
		return sss;
	}
}
