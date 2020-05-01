package tech.eliezer.meukumbu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import tech.eliezer.meukumbu.R;
import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;

public class MainActivity extends IntroActivity {
    private FirebaseAuth autenticacao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setButtonBackVisible(false);
        setButtonNextVisible(false);
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                //para nao fechar automaticamente
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                //para nao fechar automaticamente
                .canGoForward(false)
                .build());


    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

    public void btCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));


    }

    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // autenticacao.signOut();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();

        }

    }

    public void abrirTelaPrincipal() {

        startActivity(new Intent(this, PrincipalActivity.class)


        );
        finish();
    }
}
