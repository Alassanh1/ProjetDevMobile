package com.uasz.edt.v2025;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.uasz.edt.v2025.model.utilitaire.Constantes;
import com.uasz.edt.v2025.model.utilitaire.JsonToObjectConverter;
import com.uasz.edt.v2025.model.webservices.OperationsGEDT;
import com.uasz.edt.v2025.model.webservices.RetoursOperationsGEDT;

import java.io.Serializable;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailConnexionInput;
    private EditText mMotDePassConnexionInput;
    private Button mConnectionButton;
    private TextView mInscriptionLink;

    private OperationsGEDT mOperationsGEDT;
    private RetoursOperationsGEDT mRetoursOperationsGEDT;
    private JsonToObjectConverter mJsonToObjectConverter;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mOperationsGEDT = new OperationsGEDT();
        mJsonToObjectConverter = new JsonToObjectConverter();

        initView();
        addValidationToViews();
    }

    private void initView() {
        mEmailConnexionInput = findViewById(R.id.activity_main_emailInput);
        mMotDePassConnexionInput = findViewById(R.id.activity_main_motDePassInput);
        mConnectionButton = findViewById(R.id.activity_main_connectionButton);
        mInscriptionLink = findViewById(R.id.activity_main_inscriptionLien);

        mConnectionButton.setOnClickListener(this);
        mInscriptionLink.setOnClickListener(this);
    }

    private void addValidationToViews() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        awesomeValidation.setColor(Color.RED);

        awesomeValidation.addValidation(this, R.id.activity_main_emailInput, Patterns.EMAIL_ADDRESS, R.string.erreur_email);
        awesomeValidation.addValidation(this, R.id.activity_main_motDePassInput, Constantes.Regex.REGEX_MOTDEPASSE, R.string.erreur_password);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_main_inscriptionLien) {
            Intent creerCompteActivity = new Intent(MainActivity2.this, CreerCompteActivity.class);
            startActivity(creerCompteActivity);
        } else if (id == R.id.activity_main_connectionButton) {
            if (awesomeValidation.validate()) {
                if (mOperationsGEDT.serviceInternet.connexionDisponible(getApplicationContext())) {
                    mRetoursOperationsGEDT = mOperationsGEDT.verifier_etudiant(
                            mEmailConnexionInput.getText().toString(),
                            mMotDePassConnexionInput.getText().toString(),
                            MainActivity2.this
                    );

                    if (mRetoursOperationsGEDT.getValeurRetourOperationsGEDT() == Constantes.ValeurRetourOperationsGEDT.VALEUR_SUCCESS) {
                        Intent afficherEmploiActivity = new Intent(MainActivity2.this, AfficherEmploiDuTempsActivity.class);
                        afficherEmploiActivity.putExtra("listeCours", (Serializable) mJsonToObjectConverter.liste_cours_converter(mRetoursOperationsGEDT.getDataAsArray()));
                        startActivity(afficherEmploiActivity);
                    } else {
                        Toast.makeText(MainActivity2.this, mRetoursOperationsGEDT.getMessageRetourOperationsGEDT(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity2.this, R.string.erreur_internet_off, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.texte_exit)
                .setMessage(R.string.texte_confirmer_exit)
                .setPositiveButton(R.string.texte_oui, (dialog, which) -> {
                    finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton(R.string.texte_non, null)
                .show();
    }
}
