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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arp.solitaire.R;

/**
 * Now display a warning dialog box on the screen.
 * 
 * @author Adrian Panton
 */
public class WarningDialog extends DialogFragment implements OnClickListener {

	public static final String TAG = "warningDialog";
	
	// Types of warning dialog boxes.
	public static final int OK = 0;
	public static final int YES = 1;
	public static final int CANCEL_OK = 2;
	public static final int NO_YES = 3;
	public static final int CANCEL_YES_NO = 4;
	public static final int CUSTOM_ONE_BUTTON = 5;
	public static final int CUSTOM_TWO_BUTTON = 6;
	public static final int CUSTOM_THREE_BUTTON = 7;

	// List of standard button text messages.
	private static final String YES_TEXT = "Yes";
	private static final String NO_TEXT = "No";
	private static final String OK_TEXT = "OK";
	private static final String CANCEL_TEXT = "Cancel";
	
	// List of key to save data for such things as screen rotations.
	private static final String TITLE_KEY = "title_message_key";
	private static final String MESSAGE_KEY = "message_key";
	private static final String TYPE_KEY = "type_key";
	private static final String BUTTON1_KEY = "Button1_Key";
	private static final String BUTTON2_KEY = "Button2_Key";
	private static final String BUTTON3_KEY = "Button3_Key";
	private static final String ID_KEY = "id_key";
	
	private String mTitleText = "";   // Text to show as title.
	private String mMessageText = ""; // Text to display as warning message.

	private String mButton1Text = ""; // Text of button 1.
	private String mButton2Text = ""; // Text of button 2.
	private String mButton3Text = ""; // Text of button 3.

	private int mType = OK; // Type of dialog box to show.

	private int mCallerID = -1; // ID for fragment that called warning dialog.

	// Activity to where routines call back to.
	private OnWarningDialogListener mListener = null;

	/**
	 * Callback interface through which the fragment or active can report the
	 * task's progress and results back to the Activity.
	 */
	public interface OnWarningDialogListener {
		void buttonOne(int id);
		void buttonTwo(int id);
		void buttonThree(int id);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Dialog dialog = new Dialog(getActivity());
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent);

		return dialog;
	}
	  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dialog_warning, container, false);

		// Restore dialog variables.
		if (savedInstanceState != null) {
			mTitleText = savedInstanceState.getString(TITLE_KEY);
			mMessageText = savedInstanceState.getString(MESSAGE_KEY);
			mType = savedInstanceState.getInt(TYPE_KEY, OK);
			mButton1Text = savedInstanceState.getString(BUTTON1_KEY);
			mButton2Text = savedInstanceState.getString(BUTTON2_KEY);
			mButton3Text = savedInstanceState.getString(BUTTON3_KEY);
			mCallerID = savedInstanceState.getInt(ID_KEY);
		}

		setupButtonsText(mType);

		// Set title text.
		((TextView) v.findViewById(R.id.WarningTitleTextView))
			.setText(mTitleText);

		// Set warning message text.
		((TextView) v.findViewById(R.id.WarningTextTextView))
				.setText(mMessageText);
		
		// Setup warning button 1.
		Button button1 = v.findViewById(R.id.WarningButton3_1);
		button1.setText(mButton1Text);
		button1.setOnClickListener(this);

		// Setup warning button 2.
		Button button2 = v.findViewById(R.id.WarningButton3_2);
		button2.setText(mButton2Text);
		button2.setOnClickListener(this);

		// Setup warning button 3.
		Button button3 = v.findViewById(R.id.WarningButton3_3);
		button3.setText(mButton3Text);
		button3.setOnClickListener(this);

		// Setup warning button 4.
		Button button4 = v.findViewById(R.id.WarningButton2_1);
		button4.setText(mButton1Text);
		button4.setOnClickListener(this);
				
		// Setup warning button 5.
		Button button5 = v.findViewById(R.id.WarningButton2_2);
		button5.setText(mButton2Text);
		button5.setOnClickListener(this);
		
		// Setup warning button 6.
		Button button6 = v.findViewById(R.id.WarningButton1_1);
		button6.setText(mButton1Text);
		button6.setOnClickListener(this);
		
		// Hide buttons that are not needed.
		LinearLayout oneButtonLayer = v.findViewById(R.id.OneButtonLayout);
		LinearLayout twoButtonLayer = v.findViewById(R.id.TwoButtonLayout);
		LinearLayout threeButtonLayer = v.findViewById(R.id.ThreeButtonLayout);
	
		switch (mType) {
		case CANCEL_YES_NO:
		case CUSTOM_THREE_BUTTON:
			// Setup for three button warning dialog.
			oneButtonLayer.setVisibility(View.GONE);
			twoButtonLayer.setVisibility(View.GONE);
			break;
			
		case CANCEL_OK:
		case NO_YES:
		case CUSTOM_TWO_BUTTON:
			// Setup for two button warning dialog.
			oneButtonLayer.setVisibility(View.GONE);
			threeButtonLayer.setVisibility(View.GONE);
			break;
			
		case OK:
		case YES:
		case CUSTOM_ONE_BUTTON:
			// Setup for one button warning dialog.
			twoButtonLayer.setVisibility(View.GONE);
			threeButtonLayer.setVisibility(View.GONE);
			break;
		}
		
		return v;
	}

	/**
	 * Hold reference to calling fragment or activity to we can report results
	 * and progress. This is first method called after configuration change. So
	 * we have new reference to instant of fragment being created.
	 */
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnWarningDialogListener) {
			mListener = (OnWarningDialogListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnWarningDialogListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/* ( Now handle buttons that been pressed on dialog. */
	@Override
	public void onClick(View v) {

		dismiss(); // Now dismiss dialog.

		// Check to see if callback routines are implemented if not skip.
		if (mListener == null) return;

		// Now check which button has been pressed.
		switch (v.getId()) {
		case R.id.WarningButton3_1:
		case R.id.WarningButton2_1:
		case R.id.WarningButton1_1:
			mListener.buttonOne(mCallerID);
			break;

		case R.id.WarningButton3_2:
		case R.id.WarningButton2_2:
			mListener.buttonTwo(mCallerID);
			break;

		case R.id.WarningButton3_3:
			mListener.buttonThree(mCallerID);
			break;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(TITLE_KEY, mTitleText);
		outState.putString(MESSAGE_KEY, mMessageText);
		outState.putInt(TYPE_KEY, mType);
		outState.putString(BUTTON1_KEY, mButton1Text);
		outState.putString(BUTTON2_KEY, mButton2Text);
		outState.putString(BUTTON3_KEY, mButton3Text);
		outState.putInt(ID_KEY, mCallerID);
	}
	
	/** Now set dialog type to display.
	 * 
	 * @param type of warning dialog box to be display.
	 */
	private void setupButtonsText(int type) {

		switch (type) {
		case OK:
			// Setup OK dialog.
			mButton1Text = OK_TEXT;
			mButton2Text = null;
			mButton3Text = null;
			break;

		case YES:
			// Setup YES dialog
			mButton1Text = YES_TEXT;
			mButton2Text = null;
			mButton3Text = null;
			break;

		case CANCEL_OK:
			// Setup OK CANCEL dialog.
			mButton1Text = CANCEL_TEXT;
			mButton2Text = OK_TEXT;
			mButton3Text = null;
			break;

		case NO_YES:
			// Setup YES NO dialog.
			mButton1Text = NO_TEXT;
			mButton2Text = YES_TEXT;
			mButton3Text = null;
			break;

		case CANCEL_YES_NO:
			// Setup YES NO CANCEL dialog.
			mButton1Text = CANCEL_TEXT;
			mButton2Text = YES_TEXT;
			mButton3Text = NO_TEXT;
			break;
		}
	}

	/**
	 *  Now set title message.
	 * 
	 * @param titleText = new title text.
	 */
	public void setTitle(String titleText) {
		mTitleText = titleText;
	}

	/**
	 *  Now set warning message.
	 * 
	 * @param messageText = new warning message.
	 */
	public void setMessage(String messageText) {
		mMessageText = messageText;
	}
	
	/**
	 *  Now set type warning dialog to show.
	 * 
	 * @param type = type of warning dialog box to show.
	 */
	public void setType(int type) {
		mType = type;
	}
	
	/**
	 * Set text for button 1 when using custom warning dialog.
	 * 
	 * @param text - text to display in button. 
	 */
	public void setButton1Text(String text) {
		mButton1Text = text;
	}
	
	/**
	 * Set text for button 2 when using custom warning dialog.
	 * 
	 * @param text - text to display in button. 
	 */
	public void setButton2Text(String text) {
		mButton2Text = text;
	}
	
	/**
	 * Set text for button 3 when using custom warning dialog.
	 * 
	 * @param text - text to display in button. 
	 */
	public void setButton3Text(String text) {
		mButton3Text = text;
	}
	
	/**
	 * Now set which activity of fragment called dialog.
	 * 
	 * @param ID of routine that call warning dialog.
	 */
	public void setCallerID(int ID) {
		mCallerID = ID;
	}
}
