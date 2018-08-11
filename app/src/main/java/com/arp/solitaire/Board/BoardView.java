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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.arp.solitaire.R;

import java.util.ArrayList;

/**
 * This class draw the game board and handles the users touches
 * on the game board.
 * 
 * @author Adrian Panton
 */
public class BoardView extends View {

	//********* The number ordering of ball placement on board. *********/
	//       00 01 02
	//       03 04 05
	// 06 07 08 09 10 11 12
	// 13 14 15 16 17 18 19
	// 20 21 22 23 24 25 26
	//       27 28 29
	//       30 31 32
	/********************************************************************/
	
	// Number of holes of board.
	public static final int NUMBER_OF_HOLES = 33;
	public static final int BOARD_TOUCHED = -1;
	
	public static final int NO_BALL = 0;
	public static final int BALL = 1;
	public static final int BALL_UP = 2;
	public static final int BALL_RIGHT = 3;
	public static final int BALL_DOWN = 4;
	public static final int BALL_LEFT = 5;
	public static final int BALL_PICKED = 6;
	
	// List of bitmaps that been loaded.
	private Bitmap mBoardBitmap = null; // Board bitmap used in game.
	private Bitmap mBallBitmap = null;	// Ball to draw in board hole.
	private Bitmap mBallUpBitmap = null;
	private Bitmap mBallRightBitmap = null;
	private Bitmap mBallDownBitmap = null;
	private Bitmap mBallLeftBitmap = null;
	private Bitmap mBallPickedBitmap = null;
	
	private Bitmap mGameBoardBitmap = null;	// Bitmap to draw game onto.
	private Canvas mGameBoardCanvas = null; // Canvas used for drawing bitmap.
	private Rect mGameBoardAreaRect = new Rect(); // Size of canvas to draw game on to.
	
	private Rect mDrawAreaRect = new Rect(); // Area of actual screen to draw bitmap on to.

	private ArrayList<Hole> mHoles = new ArrayList<>(); // List of holes on board.
	
	// List of x positions for holes on board.
	private static final int holeX[] = { 112, 152, 192, 112, 152, 192, 32, 72,
			112, 152, 192, 232, 272, 32, 72, 112, 152, 192, 232, 272, 32, 72,
			112, 152, 192, 232, 272, 112, 152, 192, 112, 152, 192 };

	// List of y positions for holes on board.
	private static final int holeY[] = { 30, 30, 30, 70, 70, 70, 110, 110, 110,
			110, 110, 110, 110, 150, 150, 150, 150, 150, 150, 150, 190, 190,
			190, 190, 190, 190, 190, 230, 230, 230, 270, 270, 270 };

	// Use this instance of the interface to deliver action events.
	private onBoardListener mListener = null;

	public interface onBoardListener {
		void holeTouched(int holeTouched);
	}
	
	public BoardView(Context context) {
		super(context);
		setupView();
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView();
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupView();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// Create a square size view using the smallest size 
		// to create the square view.
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		int temp = Math.min(width, height);
        height = temp;
        width = temp;

		// Set the dimensions.
		setMeasuredDimension(width, height);
	}
	
	@Override
	public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {

		// Calculate area to draw compass on taking in to account padding.
		mDrawAreaRect.top = getPaddingTop();
		mDrawAreaRect.bottom = height - getPaddingBottom();
		mDrawAreaRect.left = getPaddingLeft();
		mDrawAreaRect.right = width - getPaddingRight();
	
		// Calculate the touch ares for holes.
		float scaleX = (float)(mDrawAreaRect.right - mDrawAreaRect.left)
				/ (float) mBoardBitmap.getWidth();
		float scaleY = (float) (mDrawAreaRect.bottom - mDrawAreaRect.top)
				/ (float) mBoardBitmap.getHeight();
		
		for (Hole hole: mHoles)
			hole.setBallTouchArea(scaleX, scaleY);
		
	}

	@Override
	public boolean performClick() {
		super.performClick();

		return false;
	}

	/** Now handle game board being touched */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		performClick();
	
		// Check to see if user touching screen.
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			int holeNumber;
			
			// Check to see if hole has been touched.
			for (Hole hole: mHoles) {
				if ((holeNumber = hole.isChosen(event.getX(), event.getY())) != Hole.NOT_TOUCHED) {
					if (mListener != null)	mListener.holeTouched(holeNumber);	
					return true; // Quit as we find a hole that been touched.
				}
			}
			
			// Now return that board been touched as no holes were found to been touched.
			if (mListener != null)	mListener.holeTouched(BOARD_TOUCHED);
			return true;
		} 
		
		return false;
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mGameBoardCanvas.drawBitmap(mBoardBitmap, null, mGameBoardAreaRect, null);
		
		// Now draw balls on to board.
		for (Hole hole: mHoles) {
			hole.drawBall(mGameBoardCanvas);
		}
		
		canvas.drawBitmap(mGameBoardBitmap, null, mDrawAreaRect, null);
	}
	
	/** Set the calling fragment or activity  */
	public void setBoardListener(onBoardListener listener) {
		mListener = listener;
	}

	/** Setup board view **/
	private void setupView() {
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false; // Stop auto scaling of bitmaps.

		// Now load ball graphics.
		mBallBitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.bm_solitaire_ball, options);
		 mBallUpBitmap = BitmapFactory.decodeResource(getResources(),
					R.mipmap.bm_solitaire_ball_up, options);
		mBallRightBitmap =  BitmapFactory.decodeResource(getResources(),
				R.mipmap.bm_solitaire_ball_right, options);
		mBallDownBitmap =  BitmapFactory.decodeResource(getResources(),
				R.mipmap.bm_solitaire_ball_down, options);
		mBallLeftBitmap =  BitmapFactory.decodeResource(getResources(),
				R.mipmap.bm_solitaire_ball_left, options);
		mBallPickedBitmap =  BitmapFactory.decodeResource(getResources(),
				R.mipmap.bm_solitaire_ball_picked, options);
		
		// Load board graphic.
		mBoardBitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.bm_solitaire_board, options);
		
		// Setup bitmap and canvas to draw on for game board.
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types

		// Create a MUTABLE bitmap to draw to for game play area.
		mGameBoardBitmap = Bitmap.createBitmap(mBoardBitmap.getWidth(),
				mBoardBitmap.getHeight(), conf);

		// / Create canvas to draw on and colour to transparent.
		mGameBoardCanvas = new Canvas(mGameBoardBitmap);
		mGameBoardCanvas.drawARGB(0, 0, 0, 0);
		
		// Set size game board.
		mGameBoardAreaRect.top = 0;
		mGameBoardAreaRect.left = 0;
		mGameBoardAreaRect.right = mGameBoardCanvas.getWidth();
		mGameBoardAreaRect.bottom = mGameBoardCanvas.getHeight();

		// Now initialise holes.
		int ballWidth = mBallBitmap.getWidth();
		int ballHeight = mBallBitmap.getHeight();
		
		for (int n = 0; n < holeX.length; n++) {
			Hole hole = new Hole();
			hole.setHoleDrawPosition(holeX[n], holeY[n], ballWidth, ballHeight);
			
			hole.setHoleNumber(n);
			
			mHoles.add(hole);
		}
	}
	
	/**
	 * Set balls into holes.
	 *
	 * @param ballList a list a ball graphics to display.
	 */
	public void setHoles(int[] ballList) {
		
		int holeNumber = 0;
		
		for (Integer graphic: ballList) {
			Bitmap ballBitmap;
	
			// Get which bitmap to display in hole.
			switch (graphic) {
			case BALL:
				ballBitmap = mBallBitmap;
				break;
				
			case BALL_UP:
				ballBitmap = mBallUpBitmap;
				break;
				
			case BALL_RIGHT:
				ballBitmap = mBallRightBitmap;
				break;
				
			case BALL_DOWN:
				ballBitmap = mBallDownBitmap;
				break;
				
			case BALL_LEFT:
				ballBitmap = mBallLeftBitmap;
				break;
				
			case BALL_PICKED:
				ballBitmap = mBallPickedBitmap;
				break;
		
			default:
				ballBitmap = null;
			}
		
			// Set the graphic to display.
			mHoles.get(holeNumber).setBitmap(ballBitmap);
			
			holeNumber++;
		}
		
		postInvalidate();
	}
}
