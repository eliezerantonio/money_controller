package tech.eliezer.meukumbu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import tech.eliezer.meukumbu.R;
import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;
import tech.eliezer.meukumbu.helper.Base64Custom;
import tech.eliezer.meukumbu.helper.DateCustom;
import tech.eliezer.meukumbu.model.Movimentacao;
import tech.eliezer.meukumbu.model.Usuario;


public class DespesasActivity extends AppCompatActivity {

    Double valorRecuperado;
    String dataEscolhida;
    private TextInputEditText campoData;
    private TextInputEditText campoDescricao;
    private TextInputEditText campoCategoria;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private Double despesaTotal;
    private Double despesaAtualizada;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);
        campoCategoria = findViewById(R.id.editDespesaCategoria);
        campoData = findViewById(R.id.editDespesaData);
        campoDescricao = findViewById(R.id.editDespesaDescricao);
        campoValor = findViewById(R.id.editDespesaValor);

        campoData.setText(DateCustom.dataActual());
        recuperarDespesaTotal();

    }

    public void salvarDespesa(View view) {


        if (validarCamposDespesa()) {

            movimentacao = new Movimentacao();
            dataEscolhida = campoData.getText().toString();
            valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setTipo("d");
            despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);

            movimentacao.salvar(dataEscolhida);
            finish();

           /* String emailUsuario = autenticacao.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);


            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);






                    if (valorRecuperado < usuario.getReceitaTotal()) {

                        despesaAtualizada = despesaTotal + valorRecuperado;
                        atualizarDespesa(despesaAtualizada);

                        movimentacao.salvar(dataEscolhida);
                        finish();
                    } else {
                        Toast.makeText(DespesasActivity.this, "Erro, Saldo insuficiente", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

        }
    }


    public boolean validarCamposDespesa() {
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

    public void recuperarDespesaTotal() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);


        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa) {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}
