package com.maco.clientejuegos.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maco.clientejuegos.R;
import com.maco.clientejuegos.domain.Store;
import com.maco.clientejuegos.http.MessageRecoverer;
import com.maco.clientejuegos.http.NetTask;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JoinGameMessage;
import edu.uclm.esi.common.jsonMessages.LoginMessageAnnouncement;
import edu.uclm.esi.common.jsonMessages.SudokuBoardMessage;

public class WaitingAreaActivity extends AppCompatActivity implements IMessageDealerActivity {
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_area);
        this.layout=(LinearLayout) findViewById(R.id.layoutWA);
        Store.get().setCurrentContext(this);
        MessageRecoverer messageRecoverer =  MessageRecoverer.get(this);
        messageRecoverer.setActivity(this);
        Thread t = new Thread(messageRecoverer);
        t.start();
        JoinGameMessage jgm = new JoinGameMessage(Store.get().getUser().getIdUser(),3);
        NetTask task = new NetTask("JoinGame.action",jgm);
        task.execute();
    }

    @Override
    public void showMessage(JSONMessage jsm) {
       if (jsm.getType().equals(LoginMessageAnnouncement.class.getSimpleName())) {
           TextView tv = new TextView(this);
           tv.setText("Ha llegado "+((LoginMessageAnnouncement)jsm).getEmail());
           this.layout.addView(tv);
       }
        if (jsm.getType().equals(SudokuBoardMessage.class.getSimpleName())) {
            SudokuBoardMessage sbm= (SudokuBoardMessage) jsm;
            String casillas = sbm.getBoard();
            Store.get().setMatch(sbm.getIdMatch());
            Store.get().setGame(3);
            Intent intent = new Intent(this, PartidaActivity.class);
            intent.putExtra("board", casillas);
            intent.putExtra("jugador1",sbm.getUser1());
            intent.putExtra("jugador2", sbm.getUser2());
            startActivity(intent);
        }

    }
}
