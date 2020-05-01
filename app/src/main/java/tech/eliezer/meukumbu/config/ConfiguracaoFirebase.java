package tech.eliezer.meukumbu.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    //automaticamete sera o mesmo independetemente das intancias


    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {

        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;

    }

    public static DatabaseReference getFirebaseDatabase() {
        if ( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }
}
