package tech.eliezer.meukumbu.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;


public class Usuario {
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private double receitaTotal = 0.0;
    private double despesaTotal = 0.0;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }
}
