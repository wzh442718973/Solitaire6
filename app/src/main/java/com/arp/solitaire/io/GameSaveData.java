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

package com.arp.solitaire.io;

import com.arp.solitaire.Game.UndoMove;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Data needed to be saved to restore a game of solitaire.
 * 
 * @author Adrian Panton
 */
public class GameSaveData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileID;
	private int ballCount;
	private int[] graphicList;
	private int undoBufferPointer;
	private ArrayList<UndoMove> undoMoves;
	
	/* Setter routines. */
	
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	
	public void setBallCount(int ballCount) {
		this.ballCount = ballCount;
	}
	
	public void setGraphicList(int[] graphicList) {
		this.graphicList = graphicList;
	}
	
	public void setUndoBufferPointer(int undoBufferPointer) {
		this.undoBufferPointer = undoBufferPointer;
	}
	
	public void setUndoMoves(ArrayList<UndoMove> undoMoves) {
		this.undoMoves = undoMoves;
	}
	
	/* Getter routines. */
	
	public String getFileID() {
		return fileID;
	}
	
	public int getBallCount() {
		return ballCount;
	}
	
	public int[] getGraphicList() {
		return graphicList;
	}
	
	public int getUndoBufferPointer() {
		return undoBufferPointer;
	}

	public ArrayList<UndoMove> getUndoMoves() {
		return undoMoves;
	}
}
