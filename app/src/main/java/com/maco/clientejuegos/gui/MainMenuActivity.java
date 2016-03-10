package com.maco.clientejuegos.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.maco.clientejuegos.R;
import com.maco.clientejuegos.domain.Store;
import com.maco.clientejuegos.http.Proxy;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Store.get().setCurrentContext(this);
    }

    public void login(View view) {
        Intent intent=new Intent(this, LoginActivity.class);
        EditText ip= (EditText) this.findViewById(R.id.iptext);
        String IP = ip.getText().toString();
        Proxy prx=Proxy.get();
        prx.setUrlServer(IP);
        startActivity(intent);

    }
    public void createAccount(View view){
        Intent intent=new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
