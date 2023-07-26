
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

import android.app.Dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arp.solitaire.R;

/**
 * Dialog display information from GPS devices.
 *  
 * @author Adrian Panton
 */
public class GameOverDialog extends DialogFragment implements OnClickListener {

	public static final String TAG = "gameOverDialog";
   
	private String gameEndTitle = "";
	private String gameEndMessage = "";

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

		View v = inflater.inflate(R.layout.dialog_solitaire_game_over, container, false);
		
		v.findViewById(R.id.gameOverOKButton)
		.setOnClickListener(this);
		
		((TextView) v.findViewById(R.id.gameOverTitleextView))
		.setText(gameEndTitle);
		
		((TextView) v.findViewById(R.id.gameOverTextView))
		.setText(gameEndMessage);
		
		return v;
	}
	
	@Override
	public void onClick(View v) {
	
		switch(v.getId()) {
		case R.id.gameOverOKButton:
			dismiss();
			break;
			
		}
	}
	
	/* Set dialog box title text. */
	public void setTitleText(String titleText) {
		gameEndTitle = titleText;
	}
	
	/* Set text to display for game end text. */
	public void setMessageText(String messageText) {
		gameEndMessage = messageText;
	}
}
