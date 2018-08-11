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

/**
 * Store the information need for a solution move.
 * 
 * @author Adrian Panton
 */
public class SolutionMove {

	private int oldPosition;	 // Place where ball was before move.
	private int jumped;      // The ball that was jumped to move to new position.
	private int newPosition; // Place to where ball was move to.
	private int arrowDirection; // Direction to point to make the move.
	
	/* Setter routines. */
	
	void setOldPosition(int oldPosition) {
		this.oldPosition = oldPosition;
	}
	
	void setJumped(int jumped) {
		this.jumped = jumped;
	}
	
	void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}
	
	void setArrowDirection(int arrowDirection) {
		this.arrowDirection = arrowDirection;
	}
	
	/* Getter routines. */
	
	public int getOldPosition() {
		return oldPosition;
	}
	
	public int getJumped() {
		return jumped;
		}
		
	int getNewPosition() {
		return newPosition;
	}

	public int getArrowDirection() {
		return arrowDirection;
	}
}