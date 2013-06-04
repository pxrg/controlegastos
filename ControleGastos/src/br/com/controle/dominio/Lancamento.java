package br.com.controle.dominio;

import java.io.Serializable;
import java.util.Date;

/**
 * @author igor.santos
 */
public class Lancamento implements Serializable {

    public static final String TABLE = "LANCAMENTOS";
    public static final String ID = "ID";
    private int id;
    public static final String TITULO = "TITULO";
    private String titulo;
    public static final String VENCIMENTO = "VENCIMENTO";
    private Date vencimento;
    public static final String VALOR = "VALOR";
    private Date pagamento;
    public static final String PAGAMENTO = "PAGAMENTO";
    private float valor;
    public static final String OBSERVACAO = "OBSERVACAO";
    private String observacao;
    public static final String SITUACAO = "SITUACAO";
    private String situacao;
    public static final String CATEGORIA_FK = "CATEGORIA_ID";
    private Categoria categoria;
    public static final String CONTA_FK = "CONTA_ID";
    private Conta conta;

    public Lancamento() {
    }

    public Lancamento(int id, String titulo, Date vencimento, float valor, String observacao, String situacao) {
        this.id = id;
        this.titulo = titulo;
        this.vencimento = vencimento;
        this.valor = valor;
        this.observacao = observacao;
        this.situacao = situacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public Date getPagamento() {
        return pagamento;
    }

    public void setPagamento(Date pagamento) {
        this.pagamento = pagamento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return SITUACAOENUM.ABERTO || SITUACAOENUM.FECHADO
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * SITUACAOENUM.ABERTO || SITUACAOENUM.FECHADO
     *
     * @param situacao
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Categoria getCategoria() {
        if (categoria == null) {
            categoria = new Categoria();
        }
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Conta getConta() {
        if (conta == null) {
            conta = new Conta();
        }
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }
}
