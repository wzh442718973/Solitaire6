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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

/**
 * Routines for the loading and saving of solitaire files.
 * 
 * @author Adrian Panton
 */
public class SolitaireIO {

	// Directory where to save data to.
	private static final String GAMES_DIRECTORY = File.separator + "games" + File.separator;
	
	/**
	 * Now check whether a file exists.
	 * 
	 * @param filename of file to check to see if it exists.
	 * @param context of activity.
	 * 
	 * @return true if file exists.
	 */
	public static boolean fileExist(String filename, Context context) {
		
		File file = new File(context.getFilesDir() + GAMES_DIRECTORY, filename);
		return file.exists();
	}
	
	/**
	 * Now save game data.
	 * 
	 * @param context of activity.
	 * @param filename to save file to.
	 * @param gameSaveData the data object to save for game save.
	 * 
	 * @return true if file was saved.
	 */
	public static boolean saveGame(Context context, String filename, GameSaveData gameSaveData) {

		// Check filename is not null if so return not saved.
		if (filename == null) return false;
				
		File file = new File(context.getFilesDir() + GAMES_DIRECTORY, filename);

		// Now get path of where to save file.
		String path = file.getParent();

		// Check to see if directories exists if not create them.
		File directory = new File(path);
		// Now create directories.
		if (!directory.exists())
			// Check directories were created if not quit.
			if(!directory.mkdirs()) return false;

		// Now write save game data out.
		FileOutputStream fos;
		ObjectOutputStream oos;

		try {

			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(gameSaveData);
			oos.close();

		} catch (IOException e) {
			return false;
		}

		return true;
	}
	
	/**
	 * Now load the the data for a saved game need to restore the
	 * game board to saved position.
	 * 
	 * @param context of activity.
	 * @param filename to save file to.
	 * 
	 * @return with object with saved game data or null if failed.
	 */
	public static GameSaveData loadGame(Context context, String filename) {
		
		// Check filename is not null if so return not saved.
		if (filename == null) return null;
				
		File file = new File(context.getFilesDir() + GAMES_DIRECTORY, filename);
		
		FileInputStream fis;
		ObjectInputStream ois = null;
		GameSaveData gamesaveData;
		
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			gamesaveData = (GameSaveData) ois.readObject();
			ois.close();
			
		} catch (StreamCorruptedException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			try {
				ois.close();
			} catch (IOException e1) {
				return null;
			}
			
			return null;
		} 
	
		return gamesaveData;
	}
	
}
