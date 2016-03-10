package com.maco.clientejuegos.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import com.maco.clientejuegos.domain.ParcelableMessage;
import com.maco.clientejuegos.domain.Store;
import com.maco.clientejuegos.gui.IMessageDealerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import edu.uclm.esi.common.jsonMessages.GetMessagesMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessagesBuilder;
import edu.uclm.esi.common.jsonMessages.MessageList;

/**
 * Created by Maco on 23/2/16.
 */

public class MessageRecoverer implements Runnable {
    private final Proxy proxy;
    private boolean detenido;
    private GetMessagesMessage getMessages;
    private JSONMessage mensajesPendientes;
    private Handler handler;
    private IMessageDealerActivity activity;
    private static MessageRecoverer yo;


    private MessageRecoverer(final IMessageDealerActivity activity) {
        this.proxy=Proxy.get();
        this.getMessages=new GetMessagesMessage(Store.get().getUser().getEmail());
        this.activity=activity;
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle=msg.getData();
                ParcelableMessage pm= (ParcelableMessage) bundle.get("message");
                try {
                    JSONObject jso= pm.getJsm();
                    JSONMessage jsm= JSONMessagesBuilder.build(jso);
                    MessageRecoverer.this.activity.showMessage(jsm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public static MessageRecoverer get(final IMessageDealerActivity activity){
        if (yo == null){
            yo = new MessageRecoverer(activity);
        }
        return yo;
    }


    public void setActivity(IMessageDealerActivity activity) {
        this.activity=activity;
    }

    @Override
    public void run() {
        Looper.prepare();

        while (!detenido) {
            try {
                mensajesPendientes = proxy.doGet("GetMensajes.action", getMessages);
                if (mensajesPendientes.getType().equals(MessageList.class.getSimpleName())) {
                    MessageList ml = (MessageList) mensajesPendientes;
                    for (int i = 0; i < ml.size(); i++) {
                        JSONObject jsm = ml.get(i);
                        Message msg = buildMessage(jsm);
                        handler.sendMessage(msg);
                    }
                }
                Thread.sleep(5000);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private Message buildMessage(JSONObject jsm) {
        Bundle bundle=new Bundle();
        ParcelableMessage pm=new ParcelableMessage(jsm);
        bundle.putParcelable("message", pm);
        Message msg=new Message();
        msg.setData(bundle);
        return msg;
    }

    public void detener() {
        this.detenido=true;
    }


}