package core.nucleo;

import java.util.ArrayList;
import java.util.List;

import core.nucleo.interfaces.MudancaStatus;

public class Dispositivo {
    public static String LIGADO = "ligado";
    public static String DESLIGADO = "desligado";
    public static String DESCONHECIDO = "desconhecido";
    public static String LED = "LED";
    public static String LAMPADA = "Lampada";
    public static String FITALED = "FitaLed";
    public static String CORTINA = "Cortina";
    private int id;
    private int idDependencia;
    private String nome;
    private String tipo = LED;
    private String descricao;
    private String status = DESCONHECIDO;
    private int posicao;
   /* private List<MudancaStatus> mudancaStatusList = new ArrayList<>();

    private void notificarMudancaStatus() {
        for (MudancaStatus mudancaStatus : mudancaStatusList) {
            mudancaStatus.atualizar();
        }
    }

    public void addMudancaStatus(MudancaStatus mudancaStatus){
        mudancaStatusList.add(mudancaStatus);
    }*/

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }
    public void setStatus(int status) {
        if(status==0)
            this.status = DESLIGADO;
        else if(status==1)
            this.status = LIGADO;
        else
            this.status = DESCONHECIDO;
    }
    public int getIdDependencia() {
        return idDependencia;
    }

    public void setIdDependencia(int idDependencia) {
        this.idDependencia = idDependencia;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
