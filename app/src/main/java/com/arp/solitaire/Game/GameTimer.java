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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer routines used to flash a ball on the board.
 * 
 * @author Adrian Panton
 */
public class GameTimer {

	private Timer mTimer = null;
	private TimerTask mTimerTask = null;

	private int interval;
	
	// Use this instance of the interface to deliver action events.
	private onGameTimerListener mListener = null;

	public interface onGameTimerListener {
		void timerDone();
	}

	public GameTimer(int interval) {
	
		this.interval = interval;
		
		mTimer = new Timer(); // Now create timer

		if (mTimerTask == null) { // Check to see if timer already been set.
			mTimerTask = new TimerTask() { // Set new timer task.
				public void run() {
					mListener.timerDone();
				}
			};
		}
	}
	
	/** Set the calling fragment or activity */
	public void setTimerListener(onGameTimerListener listener) {
		mListener = listener;
	}

	/** Now turn timer on. */
	public void timerOn() {

		// Set timer intervals.
		if (mTimer != null)
			mTimer.schedule(mTimerTask, interval, interval);
	}

	/** Now turn timer off. */
	public void timerOff() {
		if (mTimer != null) mTimer.cancel();
	}
}
