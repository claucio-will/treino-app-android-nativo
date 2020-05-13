package br.com.claucio.meutreino.model;

public class Luta {

    private String id;
    private String nome;
    private String diaSemana;
    private String hora;

    public Luta() {
    }

    public Luta(String nome, String diaSemana, String hora) {
        this.nome = nome;
        this.diaSemana = diaSemana;
        this.hora = hora;
    }

    public Luta(String id, String nome, String diaSemana, String hora) {
        this.id = id;
        this.nome = nome;
        this.diaSemana = diaSemana;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
