package org.matijamartinic.sudoku;


import java.util.HashMap;


public class SudokuBoardSmaller {
	
	private int [][]numbers;
	
	final public static int SIZE=9;
	final public static int SIZE2 = SudokuBoardSmaller.SIZE*SudokuBoardSmaller.SIZE;
	
	private int size = 0;
	private boolean isset[][] = new boolean[SIZE][SIZE];
	
	private static int[][][][]  units = null;
	
	private static void calculateUnits(){
		if(SudokuBoardSmaller.units!=null){
			return;
		}
		
		units = new int[SIZE][SIZE][SIZE+SIZE-2+4][2];
		for (int i = 0; i < SIZE; i++) {
			//units.add(new ArrayList< ArrayList<Integer[]> >(SIZE));
			for (int j = 0; j < SIZE; j++) {
				calculateFor(i, j);
			}
		}
	}
	
	private static void calculateFor(int x, int y){
		int c =0;
		int[]intic;
		for(int i=0; i<SIZE; i++){
			if(i!=y){
				intic = new int[2];
				intic[0] = new Integer(x);
				intic[1] = new Integer(i);
				units[x][y][c++] = intic;
			}
		}
		
		for(int i=0; i<SIZE; i++){
			if(i!=x){
				intic = new int[2];
				intic[0] = new Integer(i);
				intic[1] = new Integer(y);
				units[x][y][c++] = intic;
			}
		}
		int startX = (x/3)*3; int startY = (y/3)*3;
		int endX = startX+3; int endY = startY+3;
		
		for(int i = startX; i<endX; i++){
			for(int j = startY; j<endY; j++){
				if(!(i==x || j==y)){
					intic = new int[2];
					intic[0] = new Integer(i);
					intic[1] = new Integer(j);
					units[x][y][c++] = intic;
				}
			}
		}
	}
	
	public static HashMap<Character, Integer> table16 = null;
	
	public static int[][] getUnits(int x, int y){
		if(units==null){
			calculateUnits();
		}
		return units[x][y];
	}
	
	public SudokuBoardSmaller(int [][]numbers){
		this.numbers = numbers;
		for (int i = 0; i < isset.length; i++) {
			for (int j = 0; j < isset[i].length; j++) {
				isset[i][j]=false;
			}
		}
		setTable16();
	}
	
	public static void setTable16(){
		table16 = new HashMap<Character, Integer>();
		for(char a='A',c=1; a<='P'; a++, c++){
			table16.put(Character.valueOf(a), Integer.valueOf(c));
		}
	}
	
	public SudokuBoardSmaller(int [][]copyNums, boolean[][] copySets) {
		this(copyNums);
		this.isset = copySets;
	}

	private void set(int x, int y){
		this.isset[x][y] = true;
	}
	
	public boolean isSet(int x, int y){
		return this.isset[x][y];
	}
	
	public int getNumbers(int x, int y){
		return this.numbers[x][y];
	}
		
	public boolean setNumber(int numberBit, int x, int y){
		// bla bla x is less than 9...so as y
		if(this.isSet(x, y) || SudokuBitField.isEmpty(this.numbers[x][y])){
			return false;
		}
		this.numbers[x][y] = numberBit;
		this.set(x, y);
		boolean result = this.recalculate(numberBit, x, y);
		if(result==false){
			return false;
		}
		
		for(int []br: SudokuBoardSmaller.getUnits(x, y)){
			int xs = br[0], ys = br[1];
			if(SudokuBitField.getSize(this.numbers[xs][ys])==1 && this.isSet(xs, ys)==false){
				if(this.setNumber(this.numbers[xs][ys], xs, ys)==false){
					return false;
				}
			}
		}
		
		return true;//this.solveEasyOnes();
	}
	
	public boolean solveEasyOnes(){
		

		for(int x=0; x<SudokuBoardSmaller.SIZE; x++){
			for(int y=0; y<SudokuBoardSmaller.SIZE; y++){
				if(SudokuBitField.isEmpty(this.getNumbers(x, y))){
					return false;
				}
				if(SudokuBitField.getSize(this.getNumbers(x, y))==1 && this.isSet(x, y)==false){
					return this.setNumber(this.getNumbers(x, y), x, y);
				}
			}
		}
		return true;
	}
	
	public void print(){
		System.out.println(this.toString());
	}
	
	public String toLine(){
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < SudokuBoardSmaller.SIZE; x++) {
			for (int y = 0; y < SudokuBoardSmaller.SIZE; y++) {
				sb.append(SudokuBitField.toString(this.getNumbers(x, y)));
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int x=0; x<SudokuBoardSmaller.SIZE; x++){
			if(x>0 && x%3==0){
				sb.append("-----------------------\n");
			}
			for(int y=0; y<SudokuBoardSmaller.SIZE; y++){
				if(y>0 && y%3==0){
					sb.append("| ");
				}
				sb.append((SudokuBitField.toString(this.numbers[x][y]))+" ");
			}
			sb.append("\n");
			
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public PairXY getMinimumField(){
		int min = -1;
		int minSize = 999;
		PairXY p = new PairXY();

		
		for(int x=0; x<SudokuBoardSmaller.SIZE; x++){
			for(int y=0; y<SudokuBoardSmaller.SIZE; y++){
				if(min==-1){
					if(this.isSet(x, y)==false){
						min = this.numbers[x][y];
						p.x=x;
						p.y=y;
					}
					continue;
				}
				if(SudokuBitField.getSize(this.getNumbers(x, y)) < minSize && this.isSet(x, y)==false){
					min = this.numbers[x][y];
					minSize = SudokuBitField.getSize(this.getNumbers(x, y));
					p.x=x;
					p.y=y;
				}
				if(min!=-1 && minSize<=2){
					return p;
				}
			}
		}
		
		return p;
	}

	private boolean recalculate(int numberBit, int x, int y){
		//recalculate rows, cols and 3x3's
		for(int []br: SudokuBoardSmaller.getUnits(x, y)){
			int xs = br[0], ys = br[1];
			this.numbers[xs][ys] = SudokuBitField.removeBitFromBitField(this.numbers[xs][ys], numberBit);
			if(SudokuBitField.isEmpty(this.numbers[xs][ys])){
				return false;
			}
		}		
				
		return true;
	}
	
	
	public SudokuBoardSmaller copy(){
		int [][]copyNums = new int[SudokuBoardSmaller.SIZE][SudokuBoardSmaller.SIZE];
		boolean [][]copySets = new boolean[SIZE][SIZE];
		
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				copyNums[i][j] = this.numbers[i][j];
				copySets[i][j] = this.isset[i][j];
			}
		}
		
		return new SudokuBoardSmaller(copyNums, copySets);
	}
	
	private void setSize(){
		size = 0;
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				if(this.isSet(i, j)){
					size++;
				}
			}
		}
	}
	
	public int getSize(){
		setSize();
		return size;
	}

	public static SudokuBoardSmaller createFromString(String line) {
		int [][]copyNums = new int[SudokuBoardSmaller.SIZE][SudokuBoardSmaller.SIZE];
		if(table16==null){
			setTable16();
		}
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){
				copyNums[i][j] = SudokuBitField.getAllNumbers();
			}
		}
		
		SudokuBoardSmaller sss = new SudokuBoardSmaller(copyNums);
		
		for(int i=0; i<SudokuBoardSmaller.SIZE; i++){
			for(int j=0; j<SudokuBoardSmaller.SIZE; j++){

				int index = i*SudokuBoardSmaller.SIZE + j; //fuckit!!
				if(line.charAt(index)!='0'){
					int num = Character.getNumericValue(line.charAt(index));
					sss.setNumber(1<<(num-1), i, j);
				}
				
			}
		}
		sss.setSize();
		//System.out.println("PRIJE ");
		//sss.print();
		return sss;
	}
}
