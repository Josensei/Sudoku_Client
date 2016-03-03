package com.maco.clientejuegos.http;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.LoginMessage;

public class NetTask extends AsyncTask<Void, Void, JSONMessage> {
    private String resource;
    private JSONMessage jsonMessage;

    public NetTask(String resource, JSONMessage jsonMessage) {
        this.resource=resource;
        this.jsonMessage=jsonMessage;
    }

    @Override
    protected JSONMessage doInBackground(Void... voids) {
        Proxy proxy= Proxy.get();
        try {
            JSONMessage resultado=proxy.doPost(this.resource, this.jsonMessage);
            return resultado;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONMessage jsonMessage) {
        super.onPostExecute(jsonMessage);
    }
}