
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

package com.arp.solitaire.Board;

import com.arp.solitaire.Game.UndoMove;

import java.util.ArrayList;

/**
 * Routines used to control the movement and placement of balls
 * of the game board.
 * 
 * @author Adrian Panton
 */
public class BallControl {

	// How game has ended.
	public static final int GAME_IN_PLAY = 0;
	public static final int GAME_FAILED = 1;
	public static final int GAME_WON = 2;
	public static final int GAME_NEARLY = 3;
	
	public static final int NO_BALL_SELECTED = -1;
	private static final int NO_MOVE = -1;
	
	// Number of hole in the middle.
	private static final int MIDDLE_HOLE = 16;
	
	// List of balls that are placed on the game board.
	private ArrayList<BallInfo> ballList = new ArrayList<>();

	private int selectedBall; // Ball that been select by player.
	private int ballCounter;  // Number balls left on board.
	
	// List of hole numbers ball jumps when moving up.
	private static final int upJump[] = { -1, -1, -1, 0, 1, 2, -1, -1, 3, 4, 5,
			-1, -1, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 22, 23,
			24, 27, 28, 29 };

	// List of hole numbers ball to move to when moving up.
	private static final int upMoveTo[] = { -1, -1, -1, -1, -1, -1, -1, -1, 0,
			1, 2, -1, -1, -1, -1, 3, 4, 5, -1, -1, 6, 7, 8, 9, 10, 11, 12, 15,
			16, 17, 22, 23, 24 };

	// List of hole numbers ball jumps when moving right.
	private static final int rightJump[] = { 1, 2, -1, 4, 5, -1, 7, 8, 9, 10,
			11, 12, -1, 14, 15, 16, 17, 18, 19, -1, 21, 22, 23, 24, 25, 26, -1,
			28, 29, -1, 31, 32, -1 };

	// List of hole numbers ball to move to when moving right.
	private static final int rightMoveTo[] = { 2, -1, -1, 5, -1, -1, 8, 9, 10,
			11, 12, -1, -1, 15, 16, 17, 18, 19, -1, -1, 22, 23, 24, 25, 26, -1,
			-1, 29, -1, -1, 32, -1, -1 };

	// List of hole numbers ball jumps when moving down.
	private static final int jumpDown[] = { 3, 4, 5, 8, 9, 10, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, -1, -1, 27, 28, 29, -1, -1,
			30, 31, 32, -1, -1, -1 };

	// List of hole numbers ball to move to when moving down.
	private static final int moveToDown[] = { 8, 9, 10, 15, 16, 17, 20, 21, 22,
			23, 24, 25, 26, -1, -1, 27, 28, 29, -1, -1, -1, -1, 30, 31, 32, -1,
			-1, -1, -1, -1, -1, -1, -1 };

	// List of hole numbers ball jumps when moving left.
	private static final int leftJump[] = { -1, 0, 1, -1, 3, 4, -1, 6, 7, 8, 9,
			10, 11, -1, 13, 14, 15, 16, 17, 18, -1, 20, 21, 22, 23, 24, 25, -1,
			27, 28, -1, 30, 31 };

	// List of hole numbers ball to move to when moving left.
	private static final int leftMoveTo[] = { -1, -1, 0, -1, -1, 3, -1, -1, 6,
			7, 8, 9, 10, -1, -1, 13, 14, 15, 16, 17, -1, -1, 20, 21, 22, 23,
			24, -1, -1, 27, -1, -1, 30 };

	public BallControl() {

		// Generator a list of balls one for each hole and
		// initialise.
		for (int n = 0; n < BoardView.NUMBER_OF_HOLES; n++) {
			BallInfo ball = new BallInfo();
			
			ball.setUpJump(upJump[n]);
			ball.setUpMoveTo(upMoveTo[n]);
			ball.setRightJump(rightJump[n]);
			ball.setRightMoveTo(rightMoveTo[n]);
			ball.setDownJump(jumpDown[n]);
			ball.setDownMoveTo(moveToDown[n]);
			ball.setLeftJump(leftJump[n]);
			ball.setLeftMoveTo(leftMoveTo[n]);

			ballList.add(ball);
		}
		
		resetBalls();
	}
	
	/**
	 * Now place balls into initial position on solitaire board.
	 */
	public void resetBalls() {
	
		ballCounter = BoardView.NUMBER_OF_HOLES - 1;
		selectedBall = NO_BALL_SELECTED;
		
		// Setup balls to initial values.
		for (BallInfo ball: ballList) {
			ball.setPresent(BallInfo.PRESENT);
		}
		
		// Middle hole contains no ball.
		ballList.get(MIDDLE_HOLE).setPresent(BallInfo.NOT_PRESENT);
	
	}
	
	/**
	 * Now check to see if ball selected can made a legal move.
	 * 
	 * @param holeNumber to check for a legal move.
	 * 
	 * @return true if a valid can be made.
	 */
	public boolean checkValidMove(int holeNumber) {
	
		BallInfo ball = ballList.get(holeNumber);
		
		ball.setNoValidMoves();
		
		/* Check to see if ball can move up. */
		int upBallMoveTo = ball.getUpMoveTo();
		int upBallJump = ball.getUpJump();
		
		if (upBallMoveTo != NO_MOVE && upBallJump != NO_MOVE) {
			if (!ballList.get(upBallMoveTo).isPresent() && 
					ballList.get(upBallJump).isPresent()) {
				ball.setUpMoveValid(true); // Set left move valid.
			}
		}
		
		/* Check to see if ball can move right. */
		int rightBallMoveTo = ball.getRightMoveTo();
		int rightBallJump = ball.getRightJump();
		
		if (rightBallMoveTo != NO_MOVE && rightBallJump != NO_MOVE) {
			if (!ballList.get(rightBallMoveTo).isPresent() && 
					ballList.get(rightBallJump).isPresent()) {
				ball.setRightMoveValid(true); // Set left move valid.
			}
		}
		
		/* Check to see if ball can move down. */
		int downBallMoveTo = ball.getDownMoveTo();
		int downBallJump = ball.getDownJump();
		
		if (downBallMoveTo != NO_MOVE && downBallJump != NO_MOVE) {
			if (!ballList.get(downBallMoveTo).isPresent() && 
					ballList.get(downBallJump).isPresent()) {
				ball.setDownMoveValid(true); // Set left move valid.
			}
		}
		
		/* Check to see if ball can move left. */
		int leftBallMoveTo = ball.getLeftMoveTo();
		int leftBallJump = ball.getLeftJump();
		
		if (leftBallMoveTo != NO_MOVE && leftBallJump != NO_MOVE) {
			if (!ballList.get(leftBallMoveTo).isPresent() && 
					ballList.get(leftBallJump).isPresent()) {
				ball.setLeftMoveValid(true); // Set left move valid.
			}
		}
		
		boolean validMove = ball.hasValidMove();
		
		if (validMove)
			selectedBall = holeNumber;
		else
			selectedBall = NO_BALL_SELECTED;
		
		return validMove;

	}

	/**
	 * Move ball to new position on board.
	 * 
	 * @param oldPosition of ball to move.
	 * @param jumped the ball being jumped.
	 * @param newPosition new position of ball.
	 */
	public void moveBall(int oldPosition, int jumped, int newPosition) {
		
		ballCounter--; // Decrease number of balls on board.
		selectedBall = NO_BALL_SELECTED;

		ballList.get(oldPosition).setPresent(false);
		ballList.get(jumped).setPresent(false);
		ballList.get(newPosition).setPresent(true);
	}
	
	/** Set selected ball.*/
	public void setSelectedBall(int selectedBall) {
		this.selectedBall = selectedBall;
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
	 * Now undo a move on the board.
	 * 
	 * @param move undoMove data need to undo the move. 
	 */
	public void undoMove(UndoMove move ) {
		
		ballList.get(move.getNewPosition()).setPresent(BallInfo.NOT_PRESENT);
		ballList.get(move.getJumped()).setPresent(BallInfo.PRESENT);
		ballList.get(move.getOldPosition()).setPresent(BallInfo.PRESENT);
		
		ballCounter ++;
		selectedBall = NO_BALL_SELECTED;
	}
	
	/**
	 * Check to see if game has ended by checking whether any moves
	 * still can be made.
	 * 
	 * @return how game has ended or not ended 
	 */
	public int checkForGameEnd() {
	
		// Now check to see if only one ball remains then game has ended.
		if (ballCounter == 1) {
			
			// Check to see if ball is in the middle hole.
			if (ballList.get(MIDDLE_HOLE).isPresent() == BallInfo.PRESENT)
				return GAME_WON;
			else
				return GAME_NEARLY;
		}
		
		// Now check for any valid moves on the board.
		for (int n = 0; n < ballList.size(); n++) {
		
			// First check move is present in hole then if ball is 
			// present check if ball can make a valid move if so 
			// return game still in play.
			if (ballList.get(n).isPresent()) {

				BallInfo ball = ballList.get(n);
				
				/* Check to see if ball can move up. */
				int upBallMoveTo = ball.getUpMoveTo();
				int upBallJump = ball.getUpJump();
				
				if (upBallMoveTo != NO_MOVE && upBallJump != NO_MOVE) {
					if (!ballList.get(upBallMoveTo).isPresent() && 
							ballList.get(upBallJump).isPresent()) {
						return GAME_IN_PLAY; // 	Quit as we found a valid move.
					}
				}
		
				/* Check to see if ball can move right. */
				int rightBallMoveTo = ball.getRightMoveTo();
				int rightBallJump = ball.getRightJump();
				
				if (rightBallMoveTo != NO_MOVE && rightBallJump != NO_MOVE) {
					if (!ballList.get(rightBallMoveTo).isPresent() && 
							ballList.get(rightBallJump).isPresent()) {
						return GAME_IN_PLAY; // 	Quit as we found a valid move.
					}
				}
				
				/* Check to see if ball can move down. */
				int downBallMoveTo = ball.getDownMoveTo();
				int downBallJump = ball.getDownJump();
				
				if (downBallMoveTo != NO_MOVE && downBallJump != NO_MOVE) {
					if (!ballList.get(downBallMoveTo).isPresent() && 
							ballList.get(downBallJump).isPresent()) {
						return GAME_IN_PLAY; // 	Quit as we found a valid move.
					}
				}
				
				/* Check to see if ball can move left. */
				int leftBallMoveTo = ball.getLeftMoveTo();
				int leftBallJump = ball.getLeftJump();
				
				if (leftBallMoveTo != NO_MOVE && leftBallJump != NO_MOVE) {
					if (!ballList.get(leftBallMoveTo).isPresent() && 
							ballList.get(leftBallJump).isPresent()) {
						return GAME_IN_PLAY; // 	Quit as we found a valid move.
					}
				}
			
			}
		}
		
		return GAME_FAILED; // Default failed.
	}
	
	/** Get list of balls. */
	public ArrayList<BallInfo> getBallList() {
		return ballList;
	}
	
	/** Get ball selected. */
	public int getSelectedBall() {
		return selectedBall;
	}
	
	/** Get number of balls left on board. */
	public int getBallCounter() {
		return ballCounter;
	}

	/**
	 * Now restored balls on grid after load game.
	 * 
	 * @param ballGraphicList list of ball graphics to display.
	 * @param ballCount number of balls left.
	 */
	public void restoreGame(int[] ballGraphicList, int ballCount) {
	
		for (int n = 0; n < ballGraphicList.length; n++) {
			if (ballGraphicList[n] == BoardView.NO_BALL)
				ballList.get(n).setPresent(false);
			else 
				ballList.get(n).setPresent(true);
		}
	
		selectedBall = NO_BALL_SELECTED;
		ballCounter = ballCount;
	}
}
