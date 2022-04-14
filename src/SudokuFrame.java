/* Program: SudokuFrame
   Author: Luis Irizarry
   Date: 11 Dec 2021
   Desc: This program serves as a frame to house the GUI of the Sudoku class.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuFrame extends JFrame {

	private static final long serialVersionUID = 4188422702549779850L;
	SudokuPanel sud;
	
	public SudokuFrame() {
		sud = new SudokuPanel();
		
    	//Frame settings
    	this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku Solver");
        this.setVisible(true);
        this.setResizable(false);
        
        //Solve button
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sud.solve();
			}
		});
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(solveButton);
        
        //Clear board button
        JButton clearBoardButton = new JButton("Clear Board");
        clearBoardButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		sud.clearBoard();
        	}
        });
        buttonPanel.add(clearBoardButton);
        
        //Clear solution button
        JButton clearSolutionButton = new JButton("Clear Solution");
        clearSolutionButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		sud.clearSolution();
        	}
        });
        buttonPanel.add(clearSolutionButton);
        
        //Add components to frame
        this.add(sud, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
    }
}