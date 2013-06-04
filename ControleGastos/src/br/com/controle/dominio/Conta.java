package br.com.controle.dominio;

/**
 * @author igor.santos
 */
public class Conta {

    public static final String TABLE = "CONTAS";
    public static final String ID = "ID";
    private int id;
    public static final String BANCO = "BANCO";
    private String banco;
    public static final String AGENCIA = "AGENCIA";
    private String agencia;
    public static final String NUMERO = "NUMERO";
    private String numero;
    public static final String TIPO = "TIPO";
    private String tipo;
    public static final String SALDOINICIAL = "SALDO_INICIAL";
    private float saldoinicial;
    public static final String SALDOATUAL = "SALDO_ATUAL";
    private float saldoatual;

    public Conta() {
    }

    public Conta(int id, String banco, String agencia, String numero, String tipo, float saldoinicial, float saldoatual) {
        this.id = id;
        this.banco = banco;
        this.agencia = agencia;
        this.numero = numero;
        this.tipo = tipo;
        this.saldoinicial = saldoinicial;
        this.saldoatual = saldoatual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return CONTAENUM.CORRENTE e CONTAENUM.POUPANCA
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * CONTAENUM.CORRENTE e CONTAENUM.POUPANCA
     * @param tipo 
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getSaldoinicial() {
        return saldoinicial;
    }

    public void setSaldoinicial(float saldoinicial) {
        this.saldoinicial = saldoinicial;
    }

    public float getSaldoatual() {
        return saldoatual;
    }

    public void setSaldoatual(float saldoatual) {
        this.saldoatual = saldoatual;
    }

    @Override
    public String toString() {
        return agencia + " - " + banco;
    }
}
