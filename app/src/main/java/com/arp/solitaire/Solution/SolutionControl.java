/*
 * Copyright (c) 2017.
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */


package com.arp.solitaire.Solution;

import com.arp.solitaire.Board.BallInfo;
import com.arp.solitaire.Board.BoardView;
import com.arp.solitaire.Game.UndoMove;

import java.util.ArrayList;

/**
 * Class which controls the movement when in solution mode.
 *
 * @author Adrian Panton
 */
public class SolutionControl {

	// Number of hole in the middle.
	private static final int MIDDLE_HOLE = 16;
		
	// Ball Arrow equates for solution to show direction of move.
	private static final int UP_ARROW = BoardView.BALL_UP;
	private static final int RIGHT_ARROW = BoardView.BALL_RIGHT;
	private static final int DOWN_ARROW = BoardView.BALL_DOWN;
	private static final int LEFT_ARROW = BoardView.BALL_LEFT;

	private static final boolean SOLUTION_ENDED = true;
	private static final boolean  SOLUTION_NOT_ENDED = false;
	
	// Used to store the list of moves for solution to game.
	private ArrayList<SolutionMove> solutionList = new ArrayList<>();
	
	// List of balls that are placed on the game board.
	private ArrayList<BallInfo> ballList = new ArrayList<>();
	
	private int solutionPointer; // Used to point to solution move within solution list.
	
	// If true solution reached the end.
	private boolean solutionReachedEnd;

	private ArrayList<UndoMove> undoMoveList = new ArrayList<>();
	
	public SolutionControl() {
		
		// Setup Solution move list.
		int[] solutionSelect = {14, 27, 20, 23, 25, 32, 17, 30, 32, 8, 0,
				5, 17, 29, 27, 15, 6, 20, 22, 12, 9, 26, 12, 2, 0, 7, 9, 11, 25,
				23, 4};

		int[] solutionMoveTo = {16, 15, 22, 21, 23, 24, 29, 32, 24, 22, 8,
				17, 29, 27, 15, 3, 20, 22, 24, 10, 11, 12, 10, 0, 8, 9, 11, 25, 23,
				9, 16};

		int[] solutionBallJumped = {15, 22, 21, 22, 24, 29, 24, 31, 29, 15, 3,
				10, 24, 28, 22, 8, 13, 21, 23, 11, 10, 19, 11, 1, 3, 8, 10, 18, 24,
				16, 9};

		int[] solutionArrowList = {RIGHT_ARROW, UP_ARROW, RIGHT_ARROW,
				LEFT_ARROW, LEFT_ARROW, UP_ARROW, DOWN_ARROW, RIGHT_ARROW,
				UP_ARROW, DOWN_ARROW, DOWN_ARROW, DOWN_ARROW, DOWN_ARROW,
				LEFT_ARROW, UP_ARROW, UP_ARROW, DOWN_ARROW, RIGHT_ARROW,
				RIGHT_ARROW, LEFT_ARROW, RIGHT_ARROW, UP_ARROW, LEFT_ARROW,
				LEFT_ARROW, DOWN_ARROW, RIGHT_ARROW, RIGHT_ARROW, DOWN_ARROW,
				LEFT_ARROW, UP_ARROW, DOWN_ARROW};

		for (int n = 0; n < solutionArrowList.length; n++) {
			SolutionMove solutionMove = new SolutionMove();
			
			solutionMove.setOldPosition(solutionSelect[n]);
			solutionMove.setNewPosition(solutionMoveTo[n]);
			solutionMove.setJumped(solutionBallJumped[n]);
			solutionMove.setArrowDirection(solutionArrowList[n]);
			
			solutionList.add(solutionMove);
		}
		
		// Now initial ballList Array.
		for (int n = 0; n < BoardView.NUMBER_OF_HOLES; n++) {
			BallInfo ball = new BallInfo();
		
			ballList.add(ball);
		}
		
		// Now create an undo list for solution
		for (int n = 0; n < solutionBallJumped.length; n++) {
		
			UndoMove undoMove = new UndoMove();
			undoMove.setJumped(solutionBallJumped[n]);
			undoMove.setNewPosition(solutionMoveTo[n]);
			undoMove.setOldPosition(solutionSelect[n]);
			undoMoveList.add(undoMove);
		}
		
		UndoMove undoMove = new UndoMove();
		undoMoveList.add(undoMove);
		
		resetBalls();
	}
	
	/**
	 * Now place balls into initial position on solitaire board.
	 */
	public void resetBalls() {
	
		// Setup balls to initial values.
		for (BallInfo ball : ballList) {
			ball.setPresent(BallInfo.PRESENT);
		}

		// Middle hole contains no ball.
		ballList.get(MIDDLE_HOLE).setPresent(BallInfo.NOT_PRESENT);
			
		solutionPointer = 0;
		solutionReachedEnd = SOLUTION_NOT_ENDED;
	}
	
	/**
	 * Get a list of ball graphics.
	 * 
	 * @return list of ball graphics to display.
	 */
	public int[] getBallGraphicList() {
		
		int balls[] = new int[ballList.size()];
		
		for (int n = 0; n < balls.length; n++)
			if (ballList.get(n).isPresent()) 
				balls[n] = BoardView.BALL;
			else
				balls[n] = BoardView.NO_BALL;
		return balls;
	}
	
	/**
	 * Move solution forward by one move.
	 */
	public void forward() {
	
		// Now set whether balls are present or not for this move.
		SolutionMove move = solutionList.get(solutionPointer);
		ballList.get(move.getOldPosition()).setPresent(BallInfo.NOT_PRESENT);
		ballList.get(move.getJumped()).setPresent(BallInfo.NOT_PRESENT);
		ballList.get(move.getNewPosition()).setPresent(BallInfo.PRESENT);
		
		solutionPointer ++; // Move forward one move in solution.
		
		// Check for end of solution list.
		if (solutionPointer >= solutionList.size()) {
			solutionPointer = solutionList.size() - 1;
			solutionReachedEnd = SOLUTION_ENDED;
		}
	}
	
	/**
	 * Move solution backward by one move.
	 */
	public void backward() {
			
		if (solutionReachedEnd == SOLUTION_ENDED ) {
			solutionReachedEnd = SOLUTION_NOT_ENDED;
			
		} else {
			solutionPointer --; // Move back one move in solution.
		}
		
		// Check whether we reached the beginning of solution if so quit.
		if (solutionPointer < 0) {
			solutionPointer = 0;
			return;
		}
				
		// Now set whether balls are present or not for this move.
		SolutionMove move = solutionList.get(solutionPointer);
		ballList.get(move.getOldPosition()).setPresent(BallInfo.PRESENT);
		ballList.get(move.getJumped()).setPresent(BallInfo.PRESENT);
		ballList.get(move.getNewPosition()).setPresent(BallInfo.NOT_PRESENT);	
	
	}
	
	/**
	 * Now get the current move to make next in the solution.
	 *  
	 * @return solution move to make next.
	 */
	public SolutionMove getCurrentMove() {
		return solutionList.get(solutionPointer);
	}
	
	/**
	 * Get whether solution reached the end.
	 * 
	 * @return true is reached the end.
	 */
	public boolean isSolutionFinished() {
		return solutionReachedEnd;
	}
	
	/** Return undo move list. */
	public ArrayList<UndoMove> getUndoMoveList() {
		return undoMoveList;
	}
	
	public int getSolutionPointer() {
		return solutionPointer;
	}
}
