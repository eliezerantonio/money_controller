package tech.eliezer.meukumbu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import tech.eliezer.meukumbu.R;
import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;
import tech.eliezer.meukumbu.config.UsuarioFirebase;
import tech.eliezer.meukumbu.helper.Base64Custom;
import tech.eliezer.meukumbu.helper.DateCustom;
import tech.eliezer.meukumbu.helper.NumberCustom;
import tech.eliezer.meukumbu.model.Movimentacao;
import tech.eliezer.meukumbu.model.Usuario;


public class ReceitasActivity extends AppCompatActivity {
    private TextInputEditText campoData;
    private TextInputEditText campoDescricao;
    private TextInputEditText campoCategoria;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private Double receitaTotal;
    private Double receitaAtualizada;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);


        campoCategoria = findViewById(R.id.editReceitaCategoria);
        campoData = findViewById(R.id.editReceitaData);
        campoDescricao = findViewById(R.id.editReceitaDescricao);
        campoValor = findViewById(R.id.editReceitaValor);

        campoData.setText(DateCustom.dataActual());
        recuperarReceitaTotal();


    }
    public void salvarReceita(View view) {


        if (validarCamposReceita()) {



            movimentacao = new Movimentacao();
            String dataEscolhida = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setTipo("r");


            receitaAtualizada = receitaTotal+ valorRecuperado;

            atualizarReceita(receitaAtualizada);


            movimentacao.salvar(dataEscolhida);
            finish();
        }
    }
    public boolean validarCamposReceita() {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {

                    if (!textoDescricao.isEmpty()) {

                        return true;
                    } else {
                        Toast.makeText(this, "Preencha a categoria!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else {
                    Toast.makeText(this, "Preencha a categoria!", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(this, "Preencha a data!", Toast.LENGTH_SHORT).show();
                return false;

            }

        } else {
            Toast.makeText(this, "Preencha o valor!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public void recuperarReceitaTotal() {
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);


        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void atualizarReceita(Double receita){
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }



}
