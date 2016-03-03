package com.maco.clientejuegos.gui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.maco.clientejuegos.R;
import com.maco.clientejuegos.domain.Store;
import com.maco.clientejuegos.domain.User;
import com.maco.clientejuegos.http.Proxy;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.LoginMessage;
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class LoginActivity extends AppCompatActivity {
    private LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Store.get().setCurrentContext(this);
    }

    public void login(View view) {
        if (loginTask!=null)
            return;
        EditText etEmail= (EditText) this.findViewById(R.id.etEmail);
        EditText etPwd= (EditText) this.findViewById(R.id.etPwd);
        this.loginTask=new LoginTask(this, etEmail.getText().toString(), etPwd.getText().toString());
        this.loginTask.execute();
        JSONMessage resultadoLogin= null;
        try {
            resultadoLogin = loginTask.get();
            if (resultadoLogin.getType().equals(ErrorMessage.class.getSimpleName())) {
                ErrorMessage em=(ErrorMessage) resultadoLogin;
                Store.get().toast(em.getText());
            } else if (resultadoLogin.getType().equals(OKMessage.class.getSimpleName())) {
                OKMessage okM=(OKMessage) resultadoLogin;
                try {
                    int idUser=Integer.parseInt(okM.getAdditionalInfo().get(0).toString());
                    User user=new User(etEmail.getText().toString(), idUser);
                    Store.get().setUser(user);
                    Intent intent=new Intent(this, WaitingAreaActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Store.get().toast("Respuesta del servidor desconocida: " + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Store.get().toast("Tarea interrumpida: " + e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Store.get().toast("Error en ejecución: " + e.getMessage());
        }
        loginTask=null;
    }

    class LoginTask extends AsyncTask<Void, Void, JSONMessage> {
        private final Context ctx;
        private String email;
        private String pwd;

        LoginTask(Context ctx, String email, String pwd) {
            this.ctx=ctx;
            this.email=email;
            this.pwd=pwd;
        }

        @Override
        protected JSONMessage doInBackground(Void... voids) {
            LoginMessage lm=new LoginMessage(email, pwd, "User2016");
            Proxy proxy= Proxy.get();
            try {
                JSONMessage resultadoLogin=proxy.doPost("Login.action", lm);
                return resultadoLogin;
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
}
