package com.maco.clientejuegos.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maco.clientejuegos.R;
import com.maco.clientejuegos.domain.Store;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Store.get().setCurrentContext(this);
    }

    public void login(View view) {
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
    public void createAccount(View view){
        Intent intent=new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
