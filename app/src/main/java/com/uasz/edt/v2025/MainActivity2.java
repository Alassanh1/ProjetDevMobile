package com.uasz.edt.v2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uasz.edt.v2025.model.utilitaire.JsonToObjectConverter;
import com.uasz.edt.v2025.model.webservices.OperationsGEDT;
import com.uasz.edt.v2025.model.webservices.RetoursOperationsGEDT;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailConnexionInput;
    private EditText mMotDePassConnexionInput;
    private Button mConnectionButton;
    private TextView mInscriptionLink;

    private OperationsGEDT mOperationsGEDT;
    private RetoursOperationsGEDT mRetoursOperationsGEDT;
    private JsonToObjectConverter mJsonToObjectConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mOperationsGEDT = new OperationsGEDT();
        mJsonToObjectConverter = new JsonToObjectConverter();

        initView();
    }

    private void initView() {
        mEmailConnexionInput = findViewById(R.id.activity_main_emailInput);
        mMotDePassConnexionInput = findViewById(R.id.activity_main_motDePassInput);
        mConnectionButton = findViewById(R.id.activity_main_connectionButton);
        mInscriptionLink = findViewById(R.id.activity_main_inscriptionLien);

        mConnectionButton.setOnClickListener(this);
        mInscriptionLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_main_inscriptionLien) {
            Intent creerCompteActivity = new Intent(MainActivity2.this, CreerCompteActivity.class);
            startActivity(creerCompteActivity);
        } else if (v.getId() == R.id.activity_main_connectionButton) {
            // Pour le moment : à compléter plus tard
            Toast.makeText(this, "Fonction de connexion à implémenter", Toast.LENGTH_SHORT).show();
        }
    }
}
