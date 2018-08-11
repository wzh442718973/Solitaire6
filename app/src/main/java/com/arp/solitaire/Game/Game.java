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

package com.arp.solitaire.Game;

import android.content.Context;

import com.arp.solitaire.Board.BallControl;
import com.arp.solitaire.Board.BallInfo;
import com.arp.solitaire.Board.BoardView;
import com.arp.solitaire.Solution.SolutionControl;
import com.arp.solitaire.Solution.SolutionMove;
import com.arp.solitaire.io.GameSaveData;
import com.arp.solitaire.io.SolitaireIO;

/**
 * Play a game of solitaire.
 * 
 * @author Adrian Panton
 */
public class Game {
	
	// ID at beginning of game save file.
	private static final String FILE_ID = "SOL" ; 
	
	private static final boolean GAME_MODE = true;      // Set game mode.
	private static final boolean SOLUTION_MODE = false; // Set solution mode.
	
	private static final boolean HELP_ON = true;   // Turn help on.
	private static final boolean HELP_OFF = false; // Turn help off. 
	
	private static final boolean BALL_ON = true;
	private static final boolean BALL_OFF = false;
		
	// Which true we are in game mode, false in solution mode.
	private boolean mGameMode; 
	
	// When true help mode is o.
	private boolean mHelpMode;
	
	// If true selected ball display or false selected ball not shown.
	private boolean mFlashOnOff;
		
	// Routines for ball movement in game mode.
	private BallControl mBallControl = new BallControl();
	
	// Routines for undo game movements.
	private UndoBufferControl mUndoBufferControl = new UndoBufferControl();
	
	// Routines for ball movement in solution mode.
	private SolutionControl mSolutionControl = new SolutionControl();
	
	// Use this instance of the interface to deliver action events.
	private onGameListener mListener = null;

	public interface onGameListener {
		void updateBoard(int[] ballList);
		void gameEnded(int how);
		void gameSaveFailed();
		void gameLoadFailed();
	}
	
	public Game() {
		
		mHelpMode = HELP_OFF; 
	}
	
	/** Set the calling fragment of activity  */
	public void setGameListener(onGameListener listener) {
		mListener = listener;
	}
	
	/** Now start game. */
	public void startGame() {
		
		mGameMode = GAME_MODE;
		
		mBallControl.resetBalls();
		mUndoBufferControl.resetBuffer();
		
		redrawBalls();
			
	}
	
	/** Now start solution. */
	public void startSolution() {
	
		mGameMode = SOLUTION_MODE;
		
		mSolutionControl.resetBalls();
		
		redrawBalls();
	}

	/** Restart Game/ */
	public void restartGame() {
	
		mGameMode = GAME_MODE;
		
		redrawBalls();
		
	}
	
	/** Now continue game from where user ended solution. */
	public void continueGame() {

		mGameMode = GAME_MODE;
			
		int solutionPointer = mSolutionControl.getSolutionPointer();
		int ballCount = BoardView.NUMBER_OF_HOLES - 1 - solutionPointer;
		
		mBallControl.restoreGame(mSolutionControl.getBallGraphicList(), ballCount);
				
		
		mUndoBufferControl.restoreUndoBuffer(mSolutionControl.getUndoMoveList(),
				solutionPointer);

		redrawBalls();
	}
	
	/** Undo player move. */
	public void undoMove() {
		
		// Get data need to undo a move.
		UndoMove undoMove = mUndoBufferControl.popMove();
		
		// Check to see if there a move to undo if not quit.
		if (undoMove == null) return;
		
		mBallControl.undoMove(undoMove);
		redrawBalls();
	}
	
	/**
	 *  Now toggle Help Mode on of off.
	 *  Help is drawing arrows on balls to show which
	 *  direction you can move in.  
	 */
	public void toggleHelpMode() {
		
		if (mHelpMode)
			mHelpMode = HELP_OFF;
		else
			mHelpMode = HELP_ON;
		
		redrawBalls();
	}
	
	/**
	 * Get whether help is on or off.
	 * 
	 * @return true if help mode is on.
	 */
	public boolean getHelpMode() {
		return mHelpMode;
	}
	
	/**
	 *  Now load a saved game. 
	 * 
	 *  @param context of activity.
	 *  @param filename name of save game file to load.
	 */
	public void loadGame(Context context, String filename) {
		
		// Now load game file data.
		GameSaveData gameSaveData = SolitaireIO.loadGame(context, filename);
		
		// Check to see if game data was loaded quit if not.
		if (gameSaveData == null) {
			if (mListener != null) mListener.gameLoadFailed();
			return;
		}
			
		// File ID to see if it's a valid file file if not quit.
		if (!gameSaveData.getFileID().contentEquals(FILE_ID)) {
			if (mListener != null) mListener.gameLoadFailed();
			return;
		}
		
		mBallControl.restoreGame(gameSaveData.getGraphicList(),
				gameSaveData.getBallCount());
		
		mUndoBufferControl.restoreUndoBuffer(gameSaveData.getUndoMoves(),
				gameSaveData.getUndoBufferPointer());
		
		redrawBalls();
			
	}
	
	/** 
	 * Now save game. 
	 *
	 * @param context of activity.
	 * @param filename name of file to save game to.
	 */
	public void saveGame(Context context, String filename) {
	
		// Now create data to save for game.
		GameSaveData gameSaveData = new GameSaveData();
		gameSaveData.setFileID(FILE_ID);
		gameSaveData.setBallCount(mBallControl.getBallCounter());
		gameSaveData.setGraphicList(mBallControl.getBallGraphicList());
		gameSaveData.setUndoBufferPointer(mUndoBufferControl.getUndoBufferPointer());
		gameSaveData.setUndoMoves(mUndoBufferControl.getUndoBuffer());
		
		if (!SolitaireIO.saveGame(context, filename, gameSaveData))
			if (mListener != null) mListener.gameSaveFailed();
	}
	
	/** Move solution back one move. */
	public void solutionBack() {
		
		mSolutionControl.backward();
		redrawBalls();
	}
	
	/** Move solution forward one move. */
	public void solutionForward() {
		
		mSolutionControl.forward();
		redrawBalls();
	}
	
	/** Redraw balls on game board. */
	private void redrawBalls() {
	
		// Redraw balls depending on what mode the game is in.
		if (mGameMode)
			redrawBallsGame();
		else
			redrawBallSolution();
	}
	
	/** 
	 * Now flash the selected ball on and off.
	 * Called by the timer routine
     */
	public void flashBall() {
		
		if (mFlashOnOff)
			mFlashOnOff = BALL_OFF;
		else
			mFlashOnOff = BALL_ON;
		
		redrawBalls();
	}
	
	/**
	 * Now move ball to new position if move is legal.
	 * 
	 * @param selectedHole to where to move to.
	 */
	private void doMove(int selectedHole) {

		// Selected ball position.
		int oldPosition = mBallControl.getSelectedBall();
		
		// Get ball that been selected.
		BallInfo ball = mBallControl.getBallList().get(oldPosition);
		
		// Check if move is valid for up move if so do move.
		if (ball.isUpMoveValid() && selectedHole == ball.getUpMoveTo()) {
			int jumpBall = ball.getUpJump();
			mBallControl.moveBall(oldPosition, jumpBall, selectedHole);
			mUndoBufferControl.saveMove(oldPosition, jumpBall, selectedHole);
			checkGamaEnded();
			return;
		}
			
		// Check if move is valid for right move if so do move.
		if (ball.isRightMoveValid() && selectedHole == ball.getRightMoveTo()) {
			int jumpBall = ball.getRightJump();
			mBallControl.moveBall(oldPosition, jumpBall, selectedHole);
			mUndoBufferControl.saveMove(oldPosition, jumpBall, selectedHole);
			checkGamaEnded();
			return;
		}		
			
		// Check if move is valid for down move if so do move.
		if (ball.isDownMoveValid() && selectedHole == ball.getDownMoveTo()) {
			int jumpBall = ball.getDownJump();
			mBallControl.moveBall(oldPosition, jumpBall, selectedHole);
			mUndoBufferControl.saveMove(oldPosition, jumpBall, selectedHole);
			checkGamaEnded();
			return;
		}		

		// Check if move is valid for left move if so do move.
		if (ball.isLeftMoveValid() && selectedHole == ball.getLeftMoveTo()) {
			int jumpBall = ball.getLeftJump();
			mBallControl.moveBall(oldPosition, jumpBall, selectedHole);
			mUndoBufferControl.saveMove(oldPosition, jumpBall, selectedHole);
			checkGamaEnded();
			return;
		}		

		// Now cancel move.
		mBallControl.setSelectedBall(BallControl.NO_BALL_SELECTED);
	}
	
	/**
	 * Now update the game after player chosen a place on the board.
	 * Either try to make the move if player chosen a hole or if player
	 * touched the board or a invalid move then cancel the move.
	 * 
	 * @param selectedHole the hole number the player has chosen or
	 *  	BOARD_TOUCHED if player touched the board to cancel move.  
	 */
	public void updateGame(int selectedHole) {

		// Stop player from selecting balls in solution mode.
		if (mGameMode == SOLUTION_MODE ) return;
		
		// Check to see if board was touched if so cancel move if started.
		if (selectedHole != BoardView.BOARD_TOUCHED) {

			// Check whether hole touched has a ball present.
			if (mBallControl.getBallList().get(selectedHole).isPresent()) {
				// Ball is present so check if ball can make a valid move.

				mBallControl.checkValidMove(selectedHole);

			} else {
				// No ball present in the hole so try to move to hole.
				
				// Check to see if a ball been selected if not quit.
				if (mBallControl.getSelectedBall() != BallControl.NO_BALL_SELECTED)
					doMove(selectedHole);
			}

		} else {
			mBallControl.setSelectedBall(BallControl.NO_BALL_SELECTED); // Assume move invalid.
		}
		
		redrawBalls();
	}

	/**
	 * Now redraw balls on board when in game mode.
	 */
	private void redrawBallsGame() {
		
		int balls[] = mBallControl.getBallGraphicList();
		int selectedBall = mBallControl.getSelectedBall();
		// Check to see if a ball been selected.
		if (selectedBall != BallControl.NO_BALL_SELECTED ) {
			
			// If ball flashing check if we need to turn ball to do not draw.
			if (!mFlashOnOff)
				balls[selectedBall] = BoardView.NO_BALL;
			
			// Now check if help is on if so draw arrows on the balls.
			if (mHelpMode) {
				BallInfo ball = mBallControl.getBallList().get(selectedBall);
				
				if (ball.isUpMoveValid()) 
					balls[ball.getUpJump()] = BoardView.BALL_UP;
				
				if (ball.isRightMoveValid()) 
					balls[ball.getRightJump()] = BoardView.BALL_RIGHT;
				
				if (ball.isDownMoveValid()) 
					balls[ball.getDownJump()] = BoardView.BALL_DOWN;
				
				if (ball.isLeftMoveValid()) 
					balls[ball.getLeftJump()] = BoardView.BALL_LEFT;
			}
		}
		
		if (mListener != null) mListener.updateBoard(balls);
	}

	/**
	 * Now redraw balls on board when in solution mode.
	 */
	private void redrawBallSolution() {
		
		int balls[] = mSolutionControl.getBallGraphicList();
		
		if (!mSolutionControl.isSolutionFinished()) {
			SolutionMove move = mSolutionControl.getCurrentMove();
		
			// To display ball that going move or not display ball.
			if (mFlashOnOff)
				// Change graphic on to move to picked ball graphic. 
				balls[move.getOldPosition()] = BoardView.BALL_PICKED;
			else
				balls[move.getOldPosition()] = BoardView.NO_BALL;
		
			// Change the ball to be remove to arrow ball.
			balls[move.getJumped()] = move.getArrowDirection();
		}
		
		if (mListener != null) mListener.updateBoard(balls);
		
	}

	/**
	 * Check to see if game has ended if so then print a
	 * message depend on whether the player has solved 
	 * the puzzle or not solved the puzzle.
	 */
	private void checkGamaEnded() {
		
		int ended = mBallControl.checkForGameEnd();
		
		if (mListener != null && ended != BallControl.GAME_IN_PLAY)
				mListener.gameEnded(ended);
	}

}

