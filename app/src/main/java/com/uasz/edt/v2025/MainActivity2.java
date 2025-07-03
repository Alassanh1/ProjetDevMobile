package com.uasz.edt.v2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uasz.edt.v2025.model.utilitaire.JsonToObjectConverter;
import com.uasz.edt.v2025.model.webservices.OperationsGEDT;
import com.uasz.edt.v2025.model.webservices.RetoursOperationsGEDT;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmailConnexionInput;
    private EditText mMotDePassConnexionInput;
    private Button mConnectionButton;
    private TextView mInscriptionLink;
    //private ServiceInternet mServiceInternet;
    private OperationsGEDT mOperationsGEDT;
    private RetoursOperationsGEDT mRetoursOperationsGEDT;
    private JsonToObjectConverter mJsonToObjectConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        //mServiceInternet = new ServiceInternet();
        mOperationsGEDT = new OperationsGEDT();
        mJsonToObjectConverter = new JsonToObjectConverter();

        initView();
    }

    private void initView() {
        mEmailConnexionInput = (EditText) findViewById(R.id.activity_main_emailInput);
        mMotDePassConnexionInput = (EditText) findViewById(R.id.activity_main_motDePassInput);
        mConnectionButton = (Button) findViewById(R.id.activity_main_connectionButton);
        mInscriptionLink = (TextView) findViewById(R.id.activity_main_inscriptionLien);

        mConnectionButton.setOnClickListener((View.OnClickListener) this);
        mInscriptionLink.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mInscriptionLink.getId()){
            //System.out.println("Clici sur le lien");
            Intent creerCompteActivit = new Intent(MainActivity2.this, CreerCompteActivity.class);
            startActivity(creerCompteActivit);
        } else {
            System.out.println("Clici sur le bouton");
        }
    }
}