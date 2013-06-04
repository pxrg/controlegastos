package br.com.controle.dominio;

/**
 * @author igor.santos
 */
public class Categoria {

    public static final String TABLE = "CATEGORIAS";
    public static final String ID = "ID";
    private int id;
    public static final String DESCRICAO = "DESCRICAO";
    private String descricao;
    public static final String TIPO = "TIPO";
    private String tipo;

    public Categoria() {
    }

    public Categoria(int id, String descricao, String tipo) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return CATEGORIAENUM.ENTRADA || CATEGORIAENUM.SAIDA 
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * CATEGORIAENUM.ENTRADA || CATEGORIAENUM.SAIDA
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
