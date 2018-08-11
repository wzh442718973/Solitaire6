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

package com.arp.solitaire;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arp.solitaire.Board.BallControl;
import com.arp.solitaire.Board.BoardView;
import com.arp.solitaire.Game.Game;
import com.arp.solitaire.Game.GameTimer;
import com.arp.solitaire.dialogs.GameOverDialog;
import com.arp.solitaire.dialogs.SaveLoadDialog;
import com.arp.solitaire.dialogs.WarningDialog;
import com.arp.solitaire.dialogs.WarningDialogUtil;

/**
 * Now display a game of solitaire and allow the player
 * to play the game.
 *
 * @author Adrian Panton
 */
public class SolitaireActivity extends FragmentActivity implements OnClickListener, BoardView.onBoardListener,
        Game.onGameListener, GameTimer.onGameTimerListener, WarningDialog.OnWarningDialogListener,SaveLoadDialog.LoadSaveListener{

    // Interval to flash the chosen ball in milliseconds.
    private static final int FLASH_INTERVAL = 500;

    // List of end of game messages.
    private static final String NO_MOVES_TITLE = "No More Moves";
    private static final String NO_MOVES_MESSAGE = "Sorry you have failed to solve"
            + " the puzzle as no moves are available.";
    private static final String GAME_WON_TITLE = "Well Done";
    private static final String GAME_WON_MESSAGE = "Congratulation you manage to solve the puzzle.";
    private static final String ALMOST_TITLE = "Bad Luck";
    private static final String ALMOST_MESSAGE = "You were close to solving the puzzle "
            + "Better luck next time.";

    private BoardView mBoardView;
    private Game mGame;
    private GameTimer mGameTimer;

    LinearLayout buttonLayer1;
    LinearLayout buttonLayer2;
    LinearLayout buttonLayer3;
    LinearLayout buttonLayer4;

    Button mHelpButton;

    String mFilename;

    //WarningDialogUtil warningDialog = new WarningDialogUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitaire);

        // Set to portrait mode only.
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mBoardView = findViewById(R.id.boardView);
        mBoardView.setBoardListener(this);

        findViewById(R.id.undoButton)
                .setOnClickListener(this);

        findViewById(R.id.restartGameButton)
                .setOnClickListener(this);

        findViewById(R.id.solutionButton)
                .setOnClickListener(this);

        mHelpButton = findViewById(R.id.helpButton);
        mHelpButton.setOnClickListener(this);

        findViewById(R.id.loadButton)
                .setOnClickListener(this);

        findViewById(R.id.saveButton)
                .setOnClickListener(this);

        findViewById(R.id.backButton)
                .setOnClickListener(this);

        findViewById(R.id.forwardButton)
                .setOnClickListener(this);

        findViewById(R.id.resetSolutionButton)
                .setOnClickListener(this);

        findViewById(R.id.gameButton)
                .setOnClickListener(this);

        buttonLayer1 = findViewById(R.id.buttonLayer1);
        buttonLayer2 = findViewById(R.id.buttonLayer2);
        buttonLayer3 = findViewById(R.id.buttonLayer3);
        buttonLayer4 = findViewById(R.id.buttonLayer4);

        setupGameModeButtons();

        mGame = new Game();
        mGame.setGameListener(this);
        mGame.startGame();

        displayHelpMode();
    }

    @Override
    public void onResume() {
        super.onResume();

        mGameTimer = new GameTimer(FLASH_INTERVAL);
        mGameTimer.setTimerListener(this);

        if (mGameTimer != null) mGameTimer.timerOn();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGameTimer != null) mGameTimer.timerOff();
    }

    @Override
    public void onBackPressed() {
       WarningDialogUtil dialog = new WarningDialogUtil();
       dialog.quitWaring();
       dialog.show(getSupportFragmentManager(), WarningDialog.TAG);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.undoButton:
                if (mGame != null) mGame.undoMove();
                break;

            case R.id.restartGameButton:
                WarningDialogUtil warningDialog = new WarningDialogUtil();
                warningDialog.restartGameWarning();
                warningDialog.show(getSupportFragmentManager(), WarningDialog.TAG);
                break;

            case R.id.solutionButton:
                setupSolutionModeButtons();
                if (mGame != null) mGame.startSolution();
                break;

            case R.id.helpButton:
                if (mGame != null) mGame.toggleHelpMode();
                displayHelpMode();
                break;

            case R.id.loadButton:
                // Now show loading dialog box.
                SaveLoadDialog loadDialog = new SaveLoadDialog();
                loadDialog.setSavingOrLoading(SaveLoadDialog.LOADING_GAME);
                loadDialog.show(getSupportFragmentManager(), SaveLoadDialog.TAG);
                break;

            case R.id.saveButton:
                // Now show saving dialog box.
                SaveLoadDialog saveDialog = new SaveLoadDialog();
                saveDialog.setSavingOrLoading(SaveLoadDialog.SAVING_GAME);
                saveDialog.show(getSupportFragmentManager(), SaveLoadDialog.TAG);

                break;

            case R.id.backButton:
                if (mGame != null) mGame.solutionBack();
                break;

            case R.id.forwardButton:
                if (mGame != null) mGame.solutionForward();
                break;

            case R.id.resetSolutionButton:
                if (mGame != null) mGame.startSolution();
                break;

            case R.id.gameButton:
                WarningDialogUtil dialog = new WarningDialogUtil();
                dialog.endSolutionWaring();
                dialog.show(getSupportFragmentManager(), WarningDialog.TAG);
                break;
        }

    }

    /**
     * Now display whether help is on or off in help button.
     */
    private void displayHelpMode() {

        if (mGame != null) {
            if (mGame.getHelpMode())
                mHelpButton.setText(getResources().getString(R.string.helpOff));
            else
                mHelpButton.setText(getResources().getString(R.string.helpOn));
        }
    }

    /**
     * Setup solitaire buttons to play game.
     */
    private void setupGameModeButtons() {

        if (buttonLayer1 != null) buttonLayer1.setVisibility(View.VISIBLE);
        if (buttonLayer2 != null) buttonLayer2.setVisibility(View.VISIBLE);
        if (buttonLayer3 != null) buttonLayer3.setVisibility(View.GONE);
        if (buttonLayer4 != null) buttonLayer4.setVisibility(View.GONE);
    }

    /**
     * Setup solitaire buttons to show solution.
     */
    private void setupSolutionModeButtons() {

        if (buttonLayer1 != null) buttonLayer1.setVisibility(View.GONE);
        if (buttonLayer2 != null) buttonLayer2.setVisibility(View.GONE);
        if (buttonLayer3 != null) buttonLayer3.setVisibility(View.VISIBLE);
        if (buttonLayer4 != null) buttonLayer4.setVisibility(View.VISIBLE);
    }

	/* Callback routine from board. */

    @Override
    public void holeTouched(int holeTouched) {

        if (mGame != null) mGame.updateGame(holeTouched);
    }

	/* Callback routines from game.*/

    @Override
    public void updateBoard(int[] ballList) {

        if (mBoardView != null) mBoardView.setHoles(ballList);
    }

    /**
     * Display game ended message.
     *
     * @param how game has ended.
     */
    @Override
    public void gameEnded(int how) {

        GameOverDialog gameOverDialog = new GameOverDialog();

        switch(how) {
            case BallControl.GAME_WON:
                gameOverDialog.setTitleText(GAME_WON_TITLE);
                gameOverDialog.setMessageText(GAME_WON_MESSAGE);
                break;

            case BallControl.GAME_NEARLY:
                gameOverDialog.setTitleText(ALMOST_TITLE);
                gameOverDialog.setMessageText(ALMOST_MESSAGE);
                break;

            case BallControl.GAME_FAILED:
                gameOverDialog.setTitleText(NO_MOVES_TITLE);
                gameOverDialog.setMessageText(NO_MOVES_MESSAGE);
                break;
        }

        gameOverDialog.show(getSupportFragmentManager(), GameOverDialog.TAG);
    }

    public void gameSaveFailed() {
        WarningDialogUtil warningDialog = new WarningDialogUtil();
        warningDialog.saveFailedWarning();
        warningDialog.show(getSupportFragmentManager(), WarningDialog.TAG);
    }

    public void gameLoadFailed() {
        WarningDialogUtil warningDialog = new WarningDialogUtil();
        warningDialog.loadFailedWarning();
        warningDialog.show(getSupportFragmentManager(), WarningDialog.TAG);
    }

	/* Callback routines from game timer. */

    @Override
    public void timerDone() {

        if (mGame != null) mGame.flashBall();
    }

	/* Callback routines from warning dialog. */

    @Override
    public void buttonOne(int id) {

    }

    @Override
    public void buttonTwo(int id) {

        // Now check which warning dialog called back.
        switch(id) {
            case WarningDialogUtil.RESTART_GAME_ID:
                if (mGame != null) mGame.startGame();
                return;

            case WarningDialogUtil.OVERWRITE_GAME_ID:
                if (mGame != null)
                    mGame.saveGame(getWindow().getContext(), mFilename);

                return;

            case WarningDialogUtil.END_SOLUTION_ID:
                // Now return to game after showing solution.
                setupGameModeButtons();
                if (mGame != null) mGame.restartGame();
                break;

            case WarningDialogUtil.QUIT_ID:
                finish();
                break;
        }

    }

    @Override
    public void buttonThree(int id) {

        // Now check which warning dialog called back.
        switch (id) {
            case WarningDialogUtil.END_SOLUTION_ID:
                // Restart game from where solution is.
                setupGameModeButtons();
                if (mGame != null) mGame.continueGame();
                break;
        }
    }

	/* Callback routines from save load dialog. */

    @Override
    public void loadGame(String filename, boolean exist) {

        if (exist) {
            if (mGame != null)
                mGame.loadGame(getWindow().getContext(), filename);
        } else {
            WarningDialogUtil warningDialog = new WarningDialogUtil();
            warningDialog.noSavedGameWarning();
            warningDialog.show(getSupportFragmentManager(), WarningDialog.TAG);
        }
    }

    @Override
    public void saveGame(String filename, boolean exist, int slotNumber) {

        mFilename = filename;

        if (exist) {
            WarningDialogUtil warningDialog = new WarningDialogUtil();
            warningDialog.overWriteWarning(slotNumber);
            warningDialog.show(getSupportFragmentManager(), WarningDialog.TAG);
        } else {
            if (mGame != null)
                mGame.saveGame(getWindow().getContext(), filename);
        }

    }
}
