package com.uasz.edt.v2025;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.uasz.edt.v2025.model.Classe;
import com.uasz.edt.v2025.model.Etudiant;
import com.uasz.edt.v2025.model.modelAdapter.DialogListeClassesAdapter;
import com.uasz.edt.v2025.model.utilitaire.Constantes;
import com.uasz.edt.v2025.model.utilitaire.DataConverter;
import com.uasz.edt.v2025.model.utilitaire.JsonToObjectConverter;
import com.uasz.edt.v2025.model.webservices.OperationsGEDT;
import com.uasz.edt.v2025.model.webservices.RetoursOperationsGEDT;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class CreerCompteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPrenomInput, mNomInput, mReferenceInput, mDateNaissanceInput,
            mClasseInput, mEmailConnexionInput, mMotDePassConnexionInput;
    private Button mCreerCompteButton;
    private ImageView mCreerCompteRetour;
    private RadioGroup mSexe;
    private RadioButton mHomme, mFemme;

    private DatePickerDialog datePickerDialog;
    private AwesomeValidation awesomeValidation;

    private List<Classe> classeList;

    private OperationsGEDT mOperationsGEDT;
    private RetoursOperationsGEDT mRetoursOperationsGEDT;
    private JsonToObjectConverter mJsonToObjectConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creer_compte);

        mOperationsGEDT = new OperationsGEDT();
        mJsonToObjectConverter = new JsonToObjectConverter();

        initView();
        setDateTimeField();
        addValidationToViews();
    }

    private void initView() {
        mEmailConnexionInput = findViewById(R.id.activity_creer_compte_emailInput);
        mMotDePassConnexionInput = findViewById(R.id.activity_creer_compte_motDePassInput);
        mCreerCompteButton = findViewById(R.id.activity_creer_compte_Button);
        mPrenomInput = findViewById(R.id.activity_creer_compte_prenomInput);
        mNomInput = findViewById(R.id.activity_creer_compte_nomInput);
        mDateNaissanceInput = findViewById(R.id.activity_creer_compte_date_naissanceInput);
        mReferenceInput = findViewById(R.id.activity_creer_compte_referenceInput);
        mClasseInput = findViewById(R.id.activity_creer_compte_classeInput);
        mSexe = findViewById(R.id.activity_creer_compte_sexe);
        mHomme = findViewById(R.id.activity_creer_compte_homme);
        mFemme = findViewById(R.id.activity_creer_compte_femme);
        mCreerCompteRetour = findViewById(R.id.creer_compte_retour);

        mCreerCompteButton.setOnClickListener(this);
        mClasseInput.setOnClickListener(this);
        mDateNaissanceInput.setOnClickListener(this);
        mCreerCompteRetour.setOnClickListener(this);
    }

    private void setDateTimeField() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme, (view, year, month, day) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            mDateNaissanceInput.setText(DataConverter.toString(selected.getTime(), DataConverter.DateType.SHORT));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void addValidationToViews() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        awesomeValidation.setColor(Color.RED);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_prenomInput, RegexTemplate.NOT_EMPTY, R.string.erreur_prenom);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_nomInput, RegexTemplate.NOT_EMPTY, R.string.erreur_nom);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_referenceInput, RegexTemplate.NOT_EMPTY, R.string.erreur_reference);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_date_naissanceInput, RegexTemplate.NOT_EMPTY, R.string.erreur_date_naissance);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_classeInput, RegexTemplate.NOT_EMPTY, R.string.erreur_classe);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_emailInput, Patterns.EMAIL_ADDRESS, R.string.erreur_email);
        awesomeValidation.addValidation(this, R.id.activity_creer_compte_motDePassInput, Constantes.Regex.REGEX_MOTDEPASSE, R.string.erreur_password);
    }

    public void afficherDialogListeClasses(Activity activity) {
        mRetoursOperationsGEDT = mOperationsGEDT.recuperer_liste_classes(CreerCompteActivity.this);
        classeList = mJsonToObjectConverter.liste_classes_converter(mRetoursOperationsGEDT.getDataAsArray());

        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_afficher_liste_classes);

        Button cancel = dialog.findViewById(R.id.dialog_liste_classes_cancelButton);
        cancel.setOnClickListener(v -> dialog.dismiss());

        ListView list = dialog.findViewById(R.id.dialog_list_classesView);
        DialogListeClassesAdapter adapter = new DialogListeClassesAdapter(this, classeList);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Classe classe = classeList.get(position);
            mClasseInput.setText(classe.getNiveauClasse().getCode_niveau() + " - " + classe.getCodeClasse());
            dialog.dismiss();
        });

        dialog.show();
    }

    private Etudiant recupererFormulaireEtInstancierEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(mNomInput.getText().toString());
        etudiant.setPrenom(mPrenomInput.getText().toString());
        etudiant.setReference(mReferenceInput.getText().toString());
        try {
            etudiant.setDateNaissance(DataConverter.toDate(mDateNaissanceInput.getText().toString(), DataConverter.DateType.SHORT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        etudiant.setEmail(mEmailConnexionInput.getText().toString());
        etudiant.setPassword(mMotDePassConnexionInput.getText().toString());

        String[] split = mClasseInput.getText().toString().split(" - ");
        if (split.length == 2) {
            String niveau = split[0];
            String code = split[1];
            etudiant.setClasse(mJsonToObjectConverter.getClasseFromCodeAndNiveau(code, niveau, classeList));
        }

        int selectedId = mSexe.getCheckedRadioButtonId();
        RadioButton selectedRadio = findViewById(selectedId);
        etudiant.setSexe(selectedRadio.getText().toString());

        return etudiant;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_creer_compte_classeInput) {
            if (mOperationsGEDT.serviceInternet.connexionDisponible(getApplicationContext())) {
                afficherDialogListeClasses(this);
            } else {
                Toast.makeText(this, R.string.erreur_internet_off, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.activity_creer_compte_date_naissanceInput) {
            datePickerDialog.show();
        } else if (id == R.id.creer_compte_retour) {
            startActivity(new Intent(this, MainActivity2.class));
        } else if (id == R.id.activity_creer_compte_Button) {
            if (awesomeValidation.validate()) {
                if (mOperationsGEDT.serviceInternet.connexionDisponible(getApplicationContext())) {
                    Etudiant etudiant = recupererFormulaireEtInstancierEtudiant();
                    mRetoursOperationsGEDT = mOperationsGEDT.ajouterEtudiant(etudiant);
                    if (mRetoursOperationsGEDT.getValeurRetourOperationsGEDT() == Constantes.ValeurRetourOperationsGEDT.VALEUR_CREATED) {
                        Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity2.class));
                    } else {
                        Toast.makeText(this, "Erreur : le compte existe déjà ou les données sont invalides", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.erreur_internet_off, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
