package com.propio.clientejuegos.jsonMessages;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

/**
 * Created by JoseAntonio on 05/04/2016.
 */
public class SudokuMovementMessage extends JSONMessage {
    @JSONable
    private int row;
    @JSONable
    private int col;
    @JSONable
    private int value;
    @JSONable
    private int idUser;
    @JSONable
    private int idMatch;
    @JSONable
    private int idGame;




    public SudokuMovementMessage(int row, int col, int value,int idUser, int idMatch, int idGame) {
        super(true);
        this.setRow(row);
        this.setCol(col);
        this.setValue(value);
        this.idUser=idUser;
        this.idMatch=idMatch;
        this.idGame = idGame;

    }

    public SudokuMovementMessage(JSONObject jso) throws JSONException {
        this(jso.getInt("row"), jso.getInt("col"), jso.getInt("value"), jso.getInt("idUser"), jso.getInt("idMatch"),jso.getInt("idGame"));
    }



    public int getRow() { return row; }

    public void setRow(int row) { this.row = row; }

    public int getCol() { return col; }

    public void setCol(int col) { this.col = col; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }

    public int getIdUser() {
        return idUser;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
}