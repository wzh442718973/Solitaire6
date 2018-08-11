
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

package com.arp.solitaire.dialogs;

import java.util.Locale;

/**
 * Routines to show varies warning dialog's.
 * 
 * @author Adrian Panton
 */
public class WarningDialogUtil extends WarningDialog {

	public static final int RESTART_GAME_ID = 1; // Restart game ID.
	public static final int OVERWRITE_GAME_ID = 2;
	public static final int END_SOLUTION_ID = 3;
	public static final int QUIT_ID = 4;

	private static final String RESTART_GAME_TITLE = "Restart Game";
	private static final String RESTART_GAME_TEXT = " Are you sure you want to restart this game ?";
	
	private static final String NO_SAVED_TITLE = "No Saved Game";
	private static final String NO_SAVED_MESSAGE = "You have chosen a slot where a saved game does not exist.";
	
	private static final String SAVE_FAILED_TITLE = "Save Failed";
	private static final String SAVE_FAILED_MESSAGE = "Sorry for some reason the game failed to be saved.";
	
	private static final String LOAD_FAILED_TITLE = "Load Failed";
	private static final String LOAD_FAILED_MESSAGE = "Sorry the loading of the game failed.";

	private static final String OVER_WRITE_TITLE = "Slot Used";
	private static final String OVER_WRITE_MESSAGE = "The game save slot is already has a saved game. " +
				"Are you sure you want to over the saved game ?";
	
	private static final String END_SOLUTION_TITLE = "End Solution";
	private static final String END_SOLUTION_MESSAGE = "Cancel - Stay in solution mode.\n" +
			"Return - return to the game. \n" +
			"Cont playing from solution position..";
	private static final String END_SOLUTION_BUTTON1 = "Cancel";
	private static final String END_SOLUTION_BUTTON2 = "Return";
	private static final String END_SOLUTION_BUTTON3 = "Cont.";

	private static final String QUIT_TITLE = "Quit Game";
	private static final String QUIT_MESSAGE = " Are you sure you want quit game ?";

	public void restartGameWarning() {
		setTitle(RESTART_GAME_TITLE);
		setMessage(RESTART_GAME_TEXT);
		setCallerID(RESTART_GAME_ID);
		setType(NO_YES);
	}
	
	public void noSavedGameWarning() {
		setTitle(NO_SAVED_TITLE);
		setMessage(NO_SAVED_MESSAGE);
		setType(OK);
	}
	
	public void saveFailedWarning() {
		setTitle(SAVE_FAILED_TITLE);
		setMessage(SAVE_FAILED_MESSAGE);
		setType(OK);
	}
	
	public void loadFailedWarning() {
		setTitle(LOAD_FAILED_TITLE);
		setMessage(LOAD_FAILED_MESSAGE);
		setType(OK);
	}
	
	public void overWriteWarning(int slotUsed) {
		
		String slotNumberText = String.format(Locale.UK, "Slot %02d chosen.\n\n ", slotUsed);
	
		setTitle(OVER_WRITE_TITLE);
		setMessage(slotNumberText + OVER_WRITE_MESSAGE);
		setCallerID(OVERWRITE_GAME_ID);
		setType(NO_YES);
	}
	
	public void endSolutionWaring() {
		
		setTitle(END_SOLUTION_TITLE);
		setMessage(END_SOLUTION_MESSAGE);
		setButton1Text(END_SOLUTION_BUTTON1);
		setButton2Text(END_SOLUTION_BUTTON2);
		setButton3Text(END_SOLUTION_BUTTON3);
		setCallerID(END_SOLUTION_ID);
		setType(CUSTOM_THREE_BUTTON);
	}

	public void quitWaring() {

		setTitle(QUIT_TITLE);
		setMessage(QUIT_MESSAGE);
		setCallerID(QUIT_ID);
		setType(NO_YES);
	}
}
