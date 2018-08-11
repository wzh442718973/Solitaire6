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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Handles the drawing of a ball
 * 
 * @author Adrian Panton
 */
class Hole {

	final static int NOT_TOUCHED = -1;
	
	private Bitmap graphic = null; // Graphic ball to draw.

	private RectF destRect = new RectF(0, 0, 0, 0); // Area to draw ball in.
	private RectF touchRect = new RectF(0, 0, 0, 0); // Area for touching ball.

	private int holeNumber; // The hole number this is on the boarded.
	
	/** 
	 * Now setup where to draw ball on board.
	 * 
	 * @param x 		X position to start drawing ball.
	 * @param y			Y position to start drawing ball.
	 * @param width		Width of ball to draw.
	 * @param height	Height of ball to draw.
	 */
	void setHoleDrawPosition(int x, int y, int width, int height) {
		// Now setup position to draw ball on screen.

		// Now setup were to display ball on grid.
		destRect.top = y;
		destRect.left = x;
		destRect.right = x + width;
		destRect.bottom = y + height;
	}
	
	/**
	 * Now work out where hole touch area is with screen.
	 * 
	 *  Please note call setHoleDrawPosition() before calling
	 *  this routine.
	 *  
	 * @param xScale Hole width scaling to fit board on screen.
	 * @param yScale Hole height scaling to fit board on screen.
	 */
	void setBallTouchArea(float xScale, float yScale) {

		touchRect.top = (destRect.top - 3) * xScale;
		touchRect.left = (destRect.left - 3) * yScale;
		touchRect.right = (destRect.right + 3) * xScale;
		touchRect.bottom = (destRect.bottom + 3) * yScale;
	}

	/**
	 * Now set graphic that the hole contains
	 * 
	 * @param bitmap New graphic to display.
	 */
	void setBitmap(Bitmap bitmap) {
		graphic = bitmap;
	}

	/**
	 * Set the hole number this on the board.
	 * 
	 * @param number of hole.
	 */
	void setHoleNumber(int number) {
		this.holeNumber = number;
	}
	
	/**
	 * Now draw graphic within hole.
	 * 
	 * @param canvas Canvas to draw hole graphic on.
	 */
	void drawBall(Canvas canvas) {
		
		// Check there is ball graphic to draw.
		if(graphic != null)
			canvas.drawBitmap(graphic, null, destRect, null);
	}

	/**
	 * Now check if hole area been touched by user.
	 * 
	 * @param x	X touched point.
	 * @param y Y touched point.
	 * 
	 * @return the number of hole is touched or NOT_TOUCHED.
	 */
	int isChosen(float x, float y) {
		
		if (x > touchRect.left && x < touchRect.right && y > touchRect.top
				&& y < touchRect.bottom)
			return holeNumber;
		
		return NOT_TOUCHED;
	}
}
