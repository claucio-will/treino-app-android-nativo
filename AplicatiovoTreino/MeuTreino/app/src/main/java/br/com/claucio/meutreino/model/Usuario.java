package br.com.claucio.meutreino.model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String nome;
    private String id;
    private String email;
    private String senha;

    public Usuario() {
    }

   public void salvar(){
       DatabaseReference reference = ConfiguracaoFirebase.getFirebase();
       reference.child("usuarios").child(getId()).setValue(this);
   }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}