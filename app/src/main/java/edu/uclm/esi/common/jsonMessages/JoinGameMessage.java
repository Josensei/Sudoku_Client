package edu.uclm.esi.common.jsonMessages;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JoseAntonio on 03/03/2016.
 */
public class JoinGameMessage extends JSONMessage{
    @JSONable
    private int idUser;
    @JSONable
    private int idGame;

    public JoinGameMessage (int idUser,int idGame){
        super(true);
        this.idUser = idUser;
        this.idGame = idGame;
    }
    public JoinGameMessage (JSONObject jso) throws JSONException{
        this(Integer.parseInt(jso.get("idUser").toString()),Integer.parseInt(jso.get("idGame").toString()));
    }

    public int getIdUser() {return idUser;}


    public int getIdGame() { return idGame; }
}
