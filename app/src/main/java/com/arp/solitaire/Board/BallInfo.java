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

/**
 * Store the information need for a ball that place on the game
 * board.
 * 
 * @author Adrian Panton
 */
public class BallInfo {

	public static final boolean PRESENT = true;
	public static final boolean NOT_PRESENT = false;
	
	private boolean present = NOT_PRESENT;

	// Hole numbers which ball is moved to after move.
	private int upMoveTo;
	private int rightMoveTo;
	private int downMoveTo;
	private int leftMoveTo;
	
	// Hole numbers which are jump when ball is moved.
	private int upJump;
	private int rightJump;
	private int downJump;
	private int leftJump;
	
	// Used for setting whether a valid can occur is a direction
	// set to true if valid move.
	private boolean upMoveValid;
	private boolean rightMoveValid;
	private boolean downMoveValid;
	private boolean leftMoveValid;

	/**
	 * Set to no move are possible.
	 */
	void setNoValidMoves() {
		
		upMoveValid = false;
		rightMoveValid = false;
		downMoveValid = false;
		leftMoveValid = false;
	}

	/**
	 * Now check to see if a valid is possible.
	 * 
	 * @return true is valid is possible.
	 */
	boolean hasValidMove() {

		return upMoveValid || rightMoveValid || downMoveValid || leftMoveValid;

	}
	
	/* Setter routines. */
	
	public void setPresent(boolean present) {
		this.present = present;
	}
	
	void setUpMoveTo(int upMoveTo) {
		this.upMoveTo = upMoveTo;
	}
	
	void setRightMoveTo(int rightMoveTo) {
		this.rightMoveTo = rightMoveTo;
	}
	
	void setDownMoveTo(int downMoveTo) {
		this.downMoveTo = downMoveTo;
	}
	
	void setLeftMoveTo(int leftMoveTo) {
		this.leftMoveTo = leftMoveTo;
	}
	
	void setUpJump(int upJump) {
		this.upJump = upJump;
	}
	
	void setRightJump(int rightJump) {
		this.rightJump = rightJump;
	}
	
	void setDownJump (int downJump) {
		this.downJump = downJump;
	}
	
	void setLeftJump(int leftJump) {
		this.leftJump = leftJump;
	}

	void setUpMoveValid(boolean upMoveValid) {
		this.upMoveValid = upMoveValid;
	}

	void setRightMoveValid(boolean rightMoveValid) {
		this.rightMoveValid = rightMoveValid;
	}

	void setDownMoveValid(boolean downMoveValid) {
		this.downMoveValid = downMoveValid;
	}

	void setLeftMoveValid(boolean leftMoveValid) {
		this.leftMoveValid = leftMoveValid;
	}

	/* Getter routines. */

	public boolean isPresent() {
		return present;
	}
		
	public int getUpMoveTo() {
		return upMoveTo;
	}
	
	public int getRightMoveTo() {
		return rightMoveTo;
	}

	public int getDownMoveTo() {
		return downMoveTo;
	}
	
	public int getLeftMoveTo() {
		return leftMoveTo;
	}
	
	public int getUpJump() {
		return upJump;
	}
	
	public int getRightJump() {
		return rightJump;
	}

	public int getDownJump() {
		return downJump;
	}
	
	public int getLeftJump() {
		return leftJump;
	}
	
	public boolean isUpMoveValid() {
		return upMoveValid;
	}
	
	public boolean isRightMoveValid() {
		return rightMoveValid;
	}
	
	public boolean isDownMoveValid() {
		return downMoveValid;
	}
	
	public boolean isLeftMoveValid() {
		return leftMoveValid;
	}
}
