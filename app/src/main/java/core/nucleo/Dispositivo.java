package core.nucleo;

public class Dispositivo {
    public static String LIGADO = "ligado";
    public static String DESLIGADO = "desligado";
    public static String DESCONHECIDO = "desconhecido";
    public static String LED = "LED";
    public static String LAMPADA = "Lampada";
    public static String FITALED = "FitaLed";
    public static String CORTINA = "Cortina";
    private int Id;
    private int IdDependencia;
    private String Nome;
    private String Tipo = LED;
    private String Descricao;
    private String Status = DESCONHECIDO;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getIdDependencia() {
        return IdDependencia;
    }

    public void setIdDependencia(int idDependencia) {
        IdDependencia = idDependencia;
    }
}
