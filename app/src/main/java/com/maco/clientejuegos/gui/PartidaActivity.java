package com.maco.clientejuegos.gui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


import com.maco.clientejuegos.R;
import com.maco.clientejuegos.domain.Store;
import com.maco.clientejuegos.http.MessageRecoverer;
import com.maco.clientejuegos.http.NetTask;
import com.maco.clientejuegos.http.Proxy;
import com.propio.clientejuegos.gui.PartidaView;
import com.propio.clientejuegos.gui.ScreenParameters;
import com.propio.clientejuegos.jsonMessages.SudokuMovementAnnouncementMessage;
import com.propio.clientejuegos.jsonMessages.SudokuMovementMessage;
import com.propio.clientejuegos.jsonMessages.SudokuWinnerMessage;

import edu.uclm.esi.common.jsonMessages.JSONMessage;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PartidaActivity extends AppCompatActivity implements IMessageDealerActivity {
    private PartidaView view;

    public void onBackPressed(){
        SudokuMovementMessage movement=new SudokuMovementMessage(-1, -1, -1, Store.get().getUser().getIdUser(), Store.get().getIdMatch(),Store.get().getIdGame());
        NetTask nt=new NetTask("SendMovement.action", movement);
        nt.execute();
        super.onBackPressed();
        return;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ScreenParameters.screenHeight=dm.heightPixels;
        ScreenParameters.screenWidth = dm.widthPixels;
        MessageRecoverer.get(this).setActivity(this);

        view=new PartidaView(this);
        String board=getIntent().getStringExtra("board");

        view.setBoard(board);
       this.setContentView(view);

    }

    @Override
    public void showMessage(JSONMessage jsm){
        if(jsm.getType().equals(SudokuMovementAnnouncementMessage.class.getSimpleName())){
            SudokuMovementAnnouncementMessage smam=(SudokuMovementAnnouncementMessage)jsm;
            view.setCasilla(smam.getRow(),smam.getCol(),smam.getValue());
        }
        if(jsm.getType().equals(SudokuWinnerMessage.class.getSimpleName())){
            SudokuWinnerMessage swm = (SudokuWinnerMessage)jsm;
            view.showVictory(swm);
        }
    }
}
