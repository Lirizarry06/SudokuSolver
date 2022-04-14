/* Program: Sudoku
   Author: Luis Irizarry
   Date: 8 December 2021
   Desc: This class is a Sudoku board that can solve itself algorithmically and 
   		 serves as the main GUI component of the program.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SudokuPanel extends JPanel implements MouseListener, KeyListener {
	private static final long serialVersionUID = 3805920965704099010L;
	private int[][] board = new int[9][9];	//internal representation of the sudoku board as a 2-d array
    private int[][] startBoard = new int[9][9]; //internal representation of the starting board
    final int WIDTH = 500;  	//width of the panel
    final int HEIGHT = 500; 	//height of the panel
    Point currentCell;			//cell that has been selected/highlighted for modification 
    boolean solved = false;		//state: whether board is solved

    //post: constructs an empty Sudoku board
    public SudokuPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
    }

    //post: renders the Sudoku board in the GUI
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        final int B_WIDTH = WIDTH - 20;		//board width
        final int B_HEIGHT = HEIGHT - 20;	//board height
        
        //draw board
        g.setColor(Color.WHITE);
        g.fillRect(10, 10, B_WIDTH - B_WIDTH % 9, B_HEIGHT - B_HEIGHT % 9);

        //draw grid lines
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= 9; i++) {
            g.drawLine(B_WIDTH / 9 * i + 10, 10,
            		B_WIDTH / 9 * i + 10, B_HEIGHT + 10 - B_HEIGHT % 9);
            g.drawLine(10, B_HEIGHT / 9 * i + 10,
            		B_WIDTH + 10 - B_WIDTH % 9, B_HEIGHT / 9 * i + 10);
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i <= 9; i+= 3) {
            g.drawLine(B_WIDTH / 9 * i + 10, 10,
            		B_WIDTH / 9 * i + 10, B_HEIGHT + 10 - B_HEIGHT % 9);
            g.drawLine(10, B_HEIGHT / 9 * i + 10,
            		B_WIDTH + 10 - B_WIDTH % 9, B_HEIGHT / 9 * i + 10);
        }

        //draw numbers in cells
		g.setFont(g.getFont().deriveFont(Font.BOLD, 24));
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] > 0) {
                	//swap colors for pre-filled vs. post-filled numbers
                	if (startBoard[i][j] == board[i][j] && startBoard[i][j] != 0) {
                		g.setColor(Color.BLACK);
                	} else {
                		g.setColor(Color.BLUE);
                	}
                    g.drawString(String.valueOf(board[i][j]), B_WIDTH / 9 * j + 10 + B_WIDTH / 22,
                        B_HEIGHT / 9 * i + 10 + B_HEIGHT / 26 * 2);
                }
            }
        }
        
        //highlight currently selected cell
        if (currentCell != null) {
        	 g.setColor(Color.ORANGE);
             g.drawRect(B_WIDTH / 9 * currentCell.x + 10, B_HEIGHT / 9 * currentCell.y + 10,
            		 B_WIDTH / 9, B_HEIGHT / 9);
             g.drawRect(B_WIDTH / 9 * currentCell.x + 11, B_HEIGHT / 9 * currentCell.y + 11,
            		 B_WIDTH / 9 - 2, B_HEIGHT / 9 - 2);
        }
    }

    //helper method for solve()
    public void solve() {
    	if (!solved) {
    		//Copy board to startBoard
            for (int i = 0; i < board.length; i++) {
            	startBoard[i] = board[i].clone();
            }
            solve(0);
            solved = true;
            repaint();
    	}
    }

    //post: solves the sudoku puzzle recursively using dynamic programming.
    //The index parameter represents one element of the 2D array using a single number instead of two, for the
    //sake of being able to simply increment this number upon each recursive call to move to the next element. 
    private boolean solve(int index) {
        int indexX = index % 9;			//row of the 2D array index is located in
        int indexY = index / 9;			//column of the 2D array index is located in
        if (index != 81) {  //recursive case; unfilled cells remain
            boolean solved = false;
            Iterator<Integer> itr = findLegal(indexX, indexY).iterator();
            if (board[indexY][indexX] != 0) { //current cell is pre-filled
                solved = solve(index + 1);
            } else {
                while (itr.hasNext() && !solved) { //current cell not pre-filled and board is not solved
                    board[indexY][indexX] = itr.next();
                    solved = solve(index + 1);
                    if (!solved) {
                        board[indexY][indexX] = 0;
                    }
                }
            }
            return solved;
        } else {    //base case; all cells have been filled
            return true;
        }
    }

    //post: returns a set containing the numbers that can legally occupy the given cell on the board
    public HashSet<Integer> findLegal(int indexX, int indexY) {
    	HashSet<Integer> illegal = new HashSet<>();	//contains numbers that cannot legally fill the cell
        HashSet<Integer> result = new HashSet<>();	//contains all numbers that are not in illegal
        
        //add all numbers in this cell's row to illegal
        for (int i = 0; i < 9; i++) {
            if(board[indexY][i] > 0) {
                illegal.add(board[indexY][i]);
            }
        }
        //add all numbers in this cell's column to illegal
        for (int i = 0; i < 9; i++) {
            if (board[i][indexX] > 0) {
                illegal.add(board[i][indexX]);
            }
        }
        //add all numbers in this cell's square to illegal
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	int num = board[i + (3 * (indexY / 3))][j + (3 * (indexX / 3))];
                if (num > 0) {
                    illegal.add(num);
                }
            }
        }
        //add any numbers 1-9 that aren't in illegal to result, return
        for (int i = 1; i <= 9; i++) {
            if (!illegal.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }
    
    //post: resets to a blank board and sets solved to false
    public void clearBoard() {
    	int[][] emptyBoard = new int[9][9];
    	int[][] emptyStartBoard = new int[9][9];
    	board = emptyBoard;
    	startBoard = emptyStartBoard;
    	solved = false;
    	repaint();
    }
    
    //post: removes solved numbers, reverting to typed numbers only
    public void clearSolution() {
    	board = Arrays.stream(startBoard)
    		.map(int[]::clone)
    		.toArray(int[][]::new);
    	solved = false;
    	repaint();
    }

    //highlights the clicked cell and makes it interactive
	public void mouseClicked(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		if (p.getX() > 10 && p.getX() < WIDTH - 10) {
			if (p.getY() > 10 && p.getX() < HEIGHT - 10) {
				currentCell = new Point((p.x - 10) / ((WIDTH - 10) / 9), 
						(p.y - 10) / ((HEIGHT - 10) / 9));
			}
		}
		requestFocus();
		repaint();
	}
	
	//allows modification of current cell if the board is not in solved state
	public void keyPressed(KeyEvent e) {
		if (!solved) {
			//Delete
			if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				board[currentCell.y][currentCell.x] = 0;
				startBoard[currentCell.y][currentCell.x] = 0;
			}
			
			//Number keys
			if (e.getKeyCode() == KeyEvent.VK_1) {
				board[currentCell.y][currentCell.x] = 1;
				startBoard[currentCell.y][currentCell.x] = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_2) {
				board[currentCell.y][currentCell.x] = 2;
				startBoard[currentCell.y][currentCell.x] = 2;
			}
			if (e.getKeyCode() == KeyEvent.VK_3) {
				board[currentCell.y][currentCell.x] = 3;
				startBoard[currentCell.y][currentCell.x] = 3;
			}
			if (e.getKeyCode() == KeyEvent.VK_4) {
				board[currentCell.y][currentCell.x] = 4;
				startBoard[currentCell.y][currentCell.x] = 4;
			}
			if (e.getKeyCode() == KeyEvent.VK_5) {
				board[currentCell.y][currentCell.x] = 5;
				startBoard[currentCell.y][currentCell.x] = 5;
			}
			if (e.getKeyCode() == KeyEvent.VK_6) {
				board[currentCell.y][currentCell.x] = 6;
				startBoard[currentCell.y][currentCell.x] = 6;
			}
			if (e.getKeyCode() == KeyEvent.VK_7) {
				board[currentCell.y][currentCell.x] = 7;
				startBoard[currentCell.y][currentCell.x] = 7;
			}
			if (e.getKeyCode() == KeyEvent.VK_8) {
				board[currentCell.y][currentCell.x] = 8;
				startBoard[currentCell.y][currentCell.x] = 8;
			}
			if (e.getKeyCode() == KeyEvent.VK_9) {
				board[currentCell.y][currentCell.x] = 9;
				startBoard[currentCell.y][currentCell.x] = 9;
			}
		}
		//Arrow keys
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			currentCell.y = Math.max(currentCell.y - 1, 0);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentCell.y = Math.min(currentCell.y + 1, 8);
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentCell.x = Math.max(currentCell.x - 1, 0);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentCell.x = Math.min(currentCell.x + 1, 8);
		}
		repaint();
	}
	
	//Unused methods declared in implemented interfaces
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

}