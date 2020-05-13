package br.com.claucio.meutreino.model;

import com.google.firebase.database.Exclude;

public class Treino {

    private String treinoId;
    private String grupoMuscular;
    private String nomeExercicio;
    private String nomeUsuario;


    public Treino() {

    }

    public Treino(String treinoId,String grupoMuscular, String nomeExercicio) {
        this.treinoId = treinoId;
        this.grupoMuscular = grupoMuscular;
        this.nomeExercicio = nomeExercicio;
    }

    public Treino(String treinoId, String grupoMuscular, String nomeExercicio, String nomeUsuario) {
        this.treinoId = treinoId;
        this.grupoMuscular = grupoMuscular;
        this.nomeExercicio = nomeExercicio;
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTreinoId() {
        return treinoId;
    }


    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }


    public String getNomeExercicio() {
        return nomeExercicio;
    }

}
