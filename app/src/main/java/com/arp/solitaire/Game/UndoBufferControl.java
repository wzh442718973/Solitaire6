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

import com.arp.solitaire.Board.BoardView;

import java.util.ArrayList;

/**
 * Now control the undo moves.
 * 
 * @author Adrian Panton
 */
class UndoBufferControl {

	// Use to store move to to undo.
	private ArrayList<UndoMove> undoBuffer = new ArrayList<>();
	
	// Use to point to where to get and save moves to.
	private int undoBufferPointer; 
	
	UndoBufferControl() {
		
		// Now create a undo buffer for size needed.
		for (int n = 0; n < BoardView.NUMBER_OF_HOLES; n++) {
			UndoMove undoMove = new UndoMove();
			undoBuffer.add(undoMove);
		}
		
		resetBuffer();
	}

	/** Now reset undo buffer to start of buffer. */
	void resetBuffer() {
		undoBufferPointer = 0;
	}
	
	/**
	 * Now save player move into the undo buffer.
	 * 
	 * @param oldPosition of where ball was.
	 * @param jumped the ball that was jumped to move to the new position.
	 * @param newPosition new position of ball after the move.
	 */
	void saveMove(int oldPosition, int jumped, int newPosition) {
		
		// Check if we reach of buffer if so quit this should happen.
		if (undoBufferPointer >= BoardView.NUMBER_OF_HOLES) 
			return;
		
		// Get place where to store move within buffer.
		UndoMove undoMove = undoBuffer.get(undoBufferPointer);
		// Now store move.
		undoMove.setOldPosition(oldPosition);
		undoMove.setJumped(jumped);
		undoMove.setNewPosition(newPosition);
		
		undoBufferPointer ++; // Move to next place within buffer.
	}
	
	/**
	 * Now get the undo move data needed to undo a move.
	 * 
	 * @return the undo move data or null if failed to get data.
	 */
	UndoMove popMove() {
		
		undoBufferPointer --; // Move to where to get undo move data.
		
		// Check to see if we reach the beginning of buffer if so
		// reset pointer to start and quit with error.
		if (undoBufferPointer < 0) {
			undoBufferPointer = 0;
			return null;
		}
		
		return undoBuffer.get(undoBufferPointer);
	}
	
	/* Getter routines8. */
	int getUndoBufferPointer() {
		return undoBufferPointer;
	}
	
	ArrayList<UndoMove> getUndoBuffer() {
		return undoBuffer;
	}
	
	/**
	 * Now restore undo buffers after a load game.
	 * 
	 * @param undoBuffer   undo buffer array.
	 * @param undoBufferPointer position in undo buffer to restore.
	 */
	void restoreUndoBuffer(ArrayList<UndoMove> undoBuffer,  int undoBufferPointer) {
		this.undoBuffer = undoBuffer;
		this.undoBufferPointer = undoBufferPointer;
	}
}
