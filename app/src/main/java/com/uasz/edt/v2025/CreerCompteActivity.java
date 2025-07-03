package com.uasz.edt.v2025;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uasz.edt.v2025.model.Classe;
import com.uasz.edt.v2025.model.modelAdapter.DialogListeClassesAdapter;
import com.uasz.edt.v2025.model.utilitaire.DataConverter;
import com.uasz.edt.v2025.model.utilitaire.JsonToObjectConverter;
import com.uasz.edt.v2025.model.webservices.*;

import java.util.Calendar;
import java.util.List;

public class CreerCompteActivity extends AppCompatActivity implements View.OnClickListener {
    /* *** Références des éléments graphiques de l'écran d'accueil dans l'activité *** */
    private EditText mPrenomInput;
    private EditText mNomInput;
    private EditText mReferenceInput;
    private EditText mDateNaissanceInput;
    private EditText mClasseInput;
    private EditText mEmailConnexionInput;
    private EditText mMotDePassConnexionInput;
    private Button mCreerCompteButton;
    private ImageView mCreerCompteRetour;
    private RadioGroup mSexe;
    private RadioButton mHomme;
    private RadioButton mFemme;

    //************ Pour la date : calendrier ***********//
    private DatePickerDialog datePickerDialog;

    List<Classe> classeList;

    private OperationsGEDT mOperationsGEDT;
    private RetoursOperationsGEDT mRetoursOperationsGEDT;
    private JsonToObjectConverter mJsonToObjectConverter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creer_compte);
        mOperationsGEDT = new OperationsGEDT();
        mJsonToObjectConverter = new JsonToObjectConverter();

        initView();

        //********** Date calendrier ************//
        setDateTimeField();
        //********** Date calendrier ************//
    }

    private void initView() {
        mEmailConnexionInput = (EditText) findViewById(R.id.activity_creer_compte_emailInput);
        mMotDePassConnexionInput = (EditText) findViewById(R.id.activity_creer_compte_motDePassInput);
        mCreerCompteButton = (Button) findViewById(R.id.activity_creer_compte_Button);
        mPrenomInput = (EditText) findViewById(R.id.activity_creer_compte_prenomInput);
        mNomInput = (EditText) findViewById(R.id.activity_creer_compte_nomInput);
        mDateNaissanceInput = (EditText) findViewById(R.id.activity_creer_compte_date_naissanceInput);
        mReferenceInput = (EditText) findViewById(R.id.activity_creer_compte_referenceInput);
        mClasseInput = (EditText) findViewById(R.id.activity_creer_compte_classeInput);
        mSexe = (RadioGroup) findViewById(R.id.activity_creer_compte_sexe);
        mHomme = (RadioButton) findViewById(R.id.activity_creer_compte_homme);
        mFemme = (RadioButton) findViewById(R.id.activity_creer_compte_femme);

        mCreerCompteRetour = (ImageView) findViewById(R.id.creer_compte_retour);

        mCreerCompteButton.setOnClickListener((View.OnClickListener) this);
        mClasseInput.setOnClickListener((View.OnClickListener) this);
        mCreerCompteRetour.setOnClickListener((View.OnClickListener) this);
        mDateNaissanceInput.setOnClickListener((View.OnClickListener) this);
        /*mSexe.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) this);
        mHomme.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        mFemme.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);*/
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        //newCalendar.
        datePickerDialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            mDateNaissanceInput.setText(DataConverter.toString(newDate.getTime(),DataConverter.DateType.SHORT));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    //*********** Dialog des classes *******************//
    public void afficherDialogListeClasses(Activity activity){
        mRetoursOperationsGEDT  = mOperationsGEDT.recuperer_liste_classes(CreerCompteActivity.this);
        classeList = mJsonToObjectConverter.liste_classes_converter(mRetoursOperationsGEDT.getDataAsArray());

        final Dialog dialogListeClasses = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListeClasses.setCancelable(false);
        dialogListeClasses.setContentView(R.layout.dialog_afficher_liste_classes);

        Button cancelButton = dialogListeClasses.findViewById(R.id.dialog_liste_classes_cancelButton);
        cancelButton.setOnClickListener(v -> dialogListeClasses.dismiss());

        ListView listClassesView = dialogListeClasses.findViewById(R.id.dialog_list_classesView);
        DialogListeClassesAdapter adapter = new DialogListeClassesAdapter(this, classeList);
        listClassesView.setAdapter(adapter);

        listClassesView.setOnItemClickListener((parent, view, position, id) -> {
            Classe classe = mJsonToObjectConverter.liste_classes_converter(mRetoursOperationsGEDT.getDataAsArray()).get(position);
            mClasseInput.setText(classe.getNiveauClasse().getCode_niveau() + " - " + classe.getCodeClasse());
            dialogListeClasses.dismiss();
        });
        dialogListeClasses.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mClasseInput.getId()){
            try {
                if (mOperationsGEDT.serviceInternet.connexionDisponible(getApplicationContext())) {
                    afficherDialogListeClasses(CreerCompteActivity.this);
                }else{
                    Toast.makeText(CreerCompteActivity.this,
                            R.string.erreur_internet_off,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                Toast.makeText(CreerCompteActivity.this,
                        e.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}