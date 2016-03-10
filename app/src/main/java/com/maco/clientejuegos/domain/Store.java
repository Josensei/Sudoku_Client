package com.maco.clientejuegos.domain;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.maco.clientejuegos.gui.IMessageDealerActivity;
import com.maco.clientejuegos.gui.WaitingAreaActivity;
import com.maco.clientejuegos.http.MessageRecoverer;

import org.json.JSONObject;

public class Store {
    private static Store yo;

    private User user;
    private int idGame;
    private int idMatch;
    private Context ctx;

    private Store() {
    }

    public static Store get() {
        if (yo==null)
            yo=new Store();
        return yo;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public User getUser() {
        return this.user;
    }

    public void setGame(int idGame) {
        this.idGame=idGame;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setMatch(int idMatch) {
        this.idMatch=idMatch;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public Context getCurrentContext() {
        return this.ctx;
    }

    public void setCurrentContext(Context ctx) {
        this.ctx=ctx;
    }

    public void toast(String msg) {
        try {
            Toast.makeText(this.ctx, msg, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Log.e("clienteJuegos", e.getMessage());
        }
    }


}