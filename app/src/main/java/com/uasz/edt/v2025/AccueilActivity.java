package com.uasz.edt.v2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonInscription;
    private Button mButtonConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // le layout que tu m'as montré avec les deux boutons

        initView();
    }

    private void initView() {
        mButtonInscription = (Button) findViewById(R.id.button); // bouton "S'inscrire"
        mButtonConnexion = (Button) findViewById(R.id.button2);   // bouton "Se connecter"

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
