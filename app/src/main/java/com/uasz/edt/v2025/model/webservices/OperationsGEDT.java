package com.uasz.edt.v2025.model.webservices;

import android.app.Activity;
import android.os.AsyncTask;


import com.uasz.edt.v2025.model.Etudiant;
import com.uasz.edt.v2025.model.utilitaire.Constantes;

import java.util.HashMap;
import java.util.Map;

/**
 * Créé par Dr Cissé, le 22/05/2023 à 02:12
 */
public class OperationsGEDT {
    public ServiceInternet serviceInternet = new ServiceInternet();
    private RetoursOperationsGEDT mRetoursOperationsGEDT;

    /*
    gettes and setters
    */
    public RetoursOperationsGEDT getmRetoursOperationsGEDT() {
        return mRetoursOperationsGEDT;
    }

    public void setmRetoursOperationsGEDT(RetoursOperationsGEDT mRetoursOperationsGEDT) {
        this.mRetoursOperationsGEDT = mRetoursOperationsGEDT;
    }

    /*
    Méthodes d'interrogations de l'API GEDT
     */
    public RetoursOperationsGEDT verifier_etudiant(String mail, String pwd, Activity activity){
        new VerifierEtudiantAsyncTask(mail, pwd, activity).execute();
        while (this.mRetoursOperationsGEDT==null){
            //On attend jusqua la fin de l'opération de vérification par GEDT. Penser à afficher un message ici
        }
        return this.mRetoursOperationsGEDT;
    }

    public RetoursOperationsGEDT recuperer_liste_classes(Activity activity){
        new RecupererListeClassesAsyncTask(activity).execute();
        while (this.mRetoursOperationsGEDT==null){
            //On attend jusqua la fin de l'opération de vérification par GEDT. Penser à afficher un message ici
        }
        return this.mRetoursOperationsGEDT;
    }

    public RetoursOperationsGEDT ajouterEtudiant(Etudiant etudiant){
        new AjouterEtudiantAsyncTask(etudiant).execute();
        while (this.mRetoursOperationsGEDT==null){
            //On attend jusqua la fin de l'opération de vérification par GEDT. Penser à afficher un message ici
        }
        return this.mRetoursOperationsGEDT;
    }




    /*
   Classes gérant les tâches (asynchrones) d'accès à l'API GEDT
    */
    private class VerifierEtudiantAsyncTask extends AsyncTask<String, String, String> {
        private String mailEtudiant;
        private String pwdEtudiant;
        private Activity activity;

        public VerifierEtudiantAsyncTask(String mailEtudiant, String pwdEtudiant, Activity activity) {
            this.mailEtudiant = mailEtudiant;
            this.pwdEtudiant = pwdEtudiant;
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {

            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_MAIL_ETUDIANT, this.mailEtudiant);
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_PWD_ETUDIANT, this.pwdEtudiant);

            RetoursOperationsGEDT retoursOperationsGEDT = serviceInternet.requetteHttp(Constantes.UrlMappingGEDT.WS_VERIFIER_ETUDIANT, "POST", httpParams);
            if (retoursOperationsGEDT.getValeurRetourOperationsGEDT() == Constantes.ValeurRetourOperationsGEDT.VALEUR_ECHEC){
                mRetoursOperationsGEDT = new RetoursOperationsGEDT(Constantes.ValeurRetourOperationsGEDT.VALEUR_ECHEC, Constantes.MessageRetourOperationsGEDT.MESSAGE_ECHEC, null);
            } else{
                if (retoursOperationsGEDT.getValeurRetourOperationsGEDT() == Constantes.ValeurRetourOperationsGEDT.VALEUR_NO_DATA){
                    mRetoursOperationsGEDT = new RetoursOperationsGEDT(Constantes.ValeurRetourOperationsGEDT.VALEUR_NO_DATA, Constantes.MessageRetourOperationsGEDT.MESSAGE_NO_DATA, null);
                }else{
                    mRetoursOperationsGEDT = new RetoursOperationsGEDT(Constantes.ValeurRetourOperationsGEDT.VALEUR_SUCCESS, Constantes.MessageRetourOperationsGEDT.MESSAGE_SUCCES, retoursOperationsGEDT.getDataAsArray());;
                }
            }
            return null;
        }
    }

    private class RecupererListeClassesAsyncTask extends AsyncTask<String, String, String> {
        private Activity activity;

        public RecupererListeClassesAsyncTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            RetoursOperationsGEDT retoursOperationsGEDT = serviceInternet.requetteHttp(Constantes.UrlMappingGEDT.WS_LISTE_CLASSES, "GET", null);
            if (retoursOperationsGEDT.getValeurRetourOperationsGEDT() == Constantes.ValeurRetourOperationsGEDT.VALEUR_ECHEC){
                mRetoursOperationsGEDT = new RetoursOperationsGEDT(Constantes.ValeurRetourOperationsGEDT.VALEUR_ECHEC, Constantes.MessageRetourOperationsGEDT.MESSAGE_ECHEC, null);
            } else{
                if (retoursOperationsGEDT.getValeurRetourOperationsGEDT() == Constantes.ValeurRetourOperationsGEDT.VALEUR_NO_DATA){
                    mRetoursOperationsGEDT = new RetoursOperationsGEDT(Constantes.ValeurRetourOperationsGEDT.VALEUR_NO_DATA, Constantes.MessageRetourOperationsGEDT.MESSAGE_NO_DATA, null);
                }else{
                    mRetoursOperationsGEDT = new RetoursOperationsGEDT(Constantes.ValeurRetourOperationsGEDT.VALEUR_SUCCESS, Constantes.MessageRetourOperationsGEDT.MESSAGE_SUCCES, retoursOperationsGEDT.getDataAsArray());;
                }
            }
            return null;
        }
    }

    private class AjouterEtudiantAsyncTask extends AsyncTask<String, String, String> {
        private Etudiant etudiant;

        public AjouterEtudiantAsyncTask(Etudiant etudiant) {
            this.etudiant = etudiant;
        }

        @Override
        protected String doInBackground(String... strings) {
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_NOM_ETUDIANT, this.etudiant.getNom());
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_PRENOM_ETUDIANT, this.etudiant.getPrenom());
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_MAIL_ETUDIANT, this.etudiant.getEmail());
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_PWD_ETUDIANT, this.etudiant.getPassword());
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_CODE_CLASSE, this.etudiant.getClasse().getCodeClasse());
            httpParams.put(Constantes.NomsParametresGEDT.PRAM_CODE_NIVEAU, this.etudiant.getClasse().getNiveauClasse().getCode_niveau());

            RetoursOperationsGEDT retoursOperationsGEDT = serviceInternet.requetteHttp(Constantes.UrlMappingGEDT.WS_AJOUT_ETUDIANT, "POST", httpParams);
            System.out.println("rrrrrrrrrrrrrrrrrrr " + retoursOperationsGEDT.getValeurRetourOperationsGEDT());
            mRetoursOperationsGEDT = new RetoursOperationsGEDT(retoursOperationsGEDT.getValeurRetourOperationsGEDT(), "", null);
            return null;
        }
    }
}