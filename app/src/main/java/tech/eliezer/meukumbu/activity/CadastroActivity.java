package tech.eliezer.meukumbu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import tech.eliezer.meukumbu.R;
import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;
import tech.eliezer.meukumbu.helper.Base64Custom;
import tech.eliezer.meukumbu.model.Usuario;


public class CadastroActivity extends AppCompatActivity {
    public Button botaoCadastrar;
    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private ProgressBar progressBarCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campoNome = findViewById(R.id.editCadastroNome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        progressBarCarregando = findViewById(R.id.progressBarCarregando);
        progressBarCarregando.setVisibility(View.GONE);

        getSupportActionBar().setTitle("Cadastro");
    }

    public void cadastrar(View view) {

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoNome.isEmpty()) {
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {

                    usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario();

                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroActivity.this,
                        "Preencha o email!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastroActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }

    }


    public void cadastrarUsuario() {
        progressBarCarregando.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String idUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                            usuario.setIdUsuario( idUsuario );
                            usuario.salvar();
                            finish();


                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Digite uma senha mais forte";


                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Por favor digite um e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastar usuário:" + e.getMessage();
                                e.printStackTrace();

                            }
                            Toast.makeText(CadastroActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();

                        }
                        progressBarCarregando.setVisibility(View.GONE);
                    }
                });


    }
}
