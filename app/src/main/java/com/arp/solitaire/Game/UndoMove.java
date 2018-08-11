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

import java.io.Serializable;

/**
 * Store the information need for a move to be undone.
 * 
 * @author Adrian Panton
 */
public class UndoMove implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int oldPosition;	 // Place where ball was before move.
	private int jumped;      // The ball that was jumped to move to new position.
	private int newPosition; // Place to where ball was move to.
	
	/* Setter routines. */
	
	public void setOldPosition(int oldPosition) {
		this.oldPosition = oldPosition;
	}
	
	public void setJumped(int jumped) {
		this.jumped = jumped;
	}
	
	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}
	
	/* Getter routines. */
	
	public int getOldPosition() {
		return oldPosition;
	}
	
	public int getJumped() {
		return jumped;
		}
		
	public int getNewPosition() {
		return newPosition;
	}

}