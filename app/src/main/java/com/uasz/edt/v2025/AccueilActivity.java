package com.uasz.edt.v2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonInscription;
    private Button mButtonConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mButtonInscription = findViewById(R.id.button); // bouton "S'inscrire"
        mButtonConnexion = findViewById(R.id.button2);   // bouton "Se connecter"

        mButtonInscription.setOnClickListener(this);
        mButtonConnexion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mButtonInscription.getId()) {
            Intent intent = new Intent(AccueilActivity.this, CreerCompteActivity.class);
            startActivity(intent);
        } else if (v.getId() == mButtonConnexion.getId()) {
            Intent intent = new Intent(AccueilActivity.this, MainActivity2.class);
            startActivity(intent);
        }
    }
}
