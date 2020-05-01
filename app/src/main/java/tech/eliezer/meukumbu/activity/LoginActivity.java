package tech.eliezer.meukumbu.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.net.ConnectException;

import tech.eliezer.meukumbu.R;
import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;
import tech.eliezer.meukumbu.model.Usuario;


public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private ProgressBar progressBarCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        progressBarCarregando = findViewById(R.id.progressBarCarregando);
        progressBarCarregando.setVisibility(View.GONE);

        getSupportActionBar().setTitle("Login");


    }


    public void entrar(View view) {

        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {
                usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                validarLogin();



            } else {
                Toast.makeText(LoginActivity.this, "Preencha a Senha", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(LoginActivity.this, "Preencha o E-mail", Toast.LENGTH_SHORT).show();
        }


    }

    public void validarLogin() {
        progressBarCarregando.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            abrirTelaPrincipal();

                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "E-mail e senha nao correpondem a um usuario cadastrado";
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuario nao esta cadastrado!";
                                progressBarCarregando.setVisibility(View.GONE);
                            }

                            catch (Exception e) {
                                excecao = "Erro ao fazer login :" + e.getMessage();
                                e.printStackTrace();

                            }
                            Toast.makeText(LoginActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();
                            progressBarCarregando.setVisibility(View.GONE);
                        }


                    }
                });


    }
    public void criarConta(View view){
        startActivity(new Intent(this, CadastroActivity.class));
        finish();
    }


    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
        progressBarCarregando.setVisibility(View.GONE);
    }

   /* public void verificarConexao() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

        } else {
            Toast.makeText(LoginActivity.this, "Nao há conexão com a internet",
                    Toast.LENGTH_SHORT).show();
        }
    }*/
}
