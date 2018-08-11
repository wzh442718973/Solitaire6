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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arp.solitaire.R;
import com.arp.solitaire.io.SolitaireIO;

import java.util.Locale;

/**
 * Display a dialog box which allows the user to chose a 
 * game to load.
 * 
 * @author Adrian Panton
 */
public class SaveLoadDialog  extends DialogFragment implements OnClickListener {

	public static final String TAG = "loadDialog";
	
	public static final boolean SAVING_GAME = true;
	public static final boolean LOADING_GAME = false;
	
	private static final String LOAD_TITLE = "Load Game";
	private static final String SAVE_TITLE = "Save Game";
		
	// If true display saving dialog, false display loading dialog.
	private boolean savingOrLoading;
	
	// List filename to save game to.
	private static final String gameFilename[] = {"game01.sol", "game02.sol", "game03.sol", "game04.sol", 
			"game05.sol", "game06.sol", "game07.sol", "game08.sol", "game09.sol", "game10.sol" };

	// A List of whether file exists or not true = exists.
	private Boolean[] fileExistList = new Boolean[gameFilename.length];
	
	private String[] menuItems = new String[gameFilename.length];
	
	LoadSaveListener mListener;

	/**
	 * Callback interface through which the fragment can report the task's
	 * progress and results back to the Activity.
	 */
	public interface LoadSaveListener {
		void loadGame(String filename, boolean exist);
		void saveGame(String filename, boolean exist, int slotNumber);
	}

	/**
	 * Hold reference to calling fragment or activity to we can report results
	 * and progress. This is first method called after configuration change. So
	 * we have new reference to instant of fragment being created.
	 */
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof LoadSaveListener) {
			mListener = (LoadSaveListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement LoadSaveListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
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
		
		View v = inflater.inflate(R.layout.dialog__solitaire_save_load, container, false);

		v.findViewById(R.id.saveLoadCancelButton)
				.setOnClickListener(this);
		
		// Now set title text of dialog box.
		TextView titleTextView = (TextView) v.findViewById(R.id.saveLoadTitleTextView);
		if (savingOrLoading)
			titleTextView.setText(SAVE_TITLE);
		else 
			titleTextView.setText(LOAD_TITLE);
		
		// Check whether files exists.
		for (int n = 0; n < gameFilename.length; n++) {
			String slotNumber = String.format(Locale.UK, "Slot %02d ", n + 1);
			if (SolitaireIO.fileExist(gameFilename[n], getContext())) {
				menuItems[n] = slotNumber + "- Used ";
				fileExistList[n] = true;
			} else {
				menuItems[n] = slotNumber + "- Empty";
				fileExistList[n] = false;
			}
		}
		
		// Setup list view.
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				getContext(), R.layout.item_solitaire_save_load_dialog,
				R.id.SaveLoadListView, menuItems);

		ListView listView = (ListView) v
				.findViewById(R.id.saveLoadListView);
		
		listView.setAdapter(adapter);
		listView.setDividerHeight(2);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (savingOrLoading) {
					// Saving game.
					if (mListener != null) mListener.saveGame(gameFilename[position],
						fileExistList[position], position + 1);
					
				} else {
					// Loading game.
					if (mListener != null) mListener.loadGame(gameFilename[position],
							fileExistList[position]);
				}
				
				dismiss();
			}

		});


		return v;
	}


	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.saveLoadCancelButton:
			dismiss();
			break;
		}

	}
	
	/**
	 * Set whether we are saving or loading a game.
	 * 
	 * @param option true than we are saving a game,
	 *        false - loading a game.
	 */
	public void setSavingOrLoading(boolean option) {
		savingOrLoading = option;
	}
}