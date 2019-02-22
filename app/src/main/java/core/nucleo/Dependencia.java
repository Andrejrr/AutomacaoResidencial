package core.nucleo;

public class Dependencia {
    private int id;
    private String nome;
    private String tipo;
    private int numeroDispositivos;

    public Dependencia(){

    }

    public Dependencia(String nome, String tipo, int numeroDispositivos) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.numeroDispositivos = numeroDispositivos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumeroDispositivos() {
        return numeroDispositivos;
    }

    public void setNumeroDispositivos(int numeroDispositivos) {
        this.numeroDispositivos = numeroDispositivos;
    }
}
