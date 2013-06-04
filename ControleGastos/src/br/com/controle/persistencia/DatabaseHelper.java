package br.com.controle.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.controle.dominio.Categoria;
import br.com.controle.dominio.Conta;
import br.com.controle.dominio.Lancamento;
import br.com.controle.dominio.Usuario;

/**
 * @author igor.santos
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE = "controlegastos.db";
    private static final int VERSION = 1;
    
    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ").append(Conta.TABLE).append("(")
                .append(Conta.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(Conta.BANCO).append(" TEXT, ")
                .append(Conta.AGENCIA).append(" TEXT, ")
                .append(Conta.NUMERO).append(" TEXT, ")
                .append(Conta.TIPO).append(" TEXT, ")
                .append(Conta.SALDOINICIAL).append(" NUMERIC(10,2), ")
                .append(Conta.SALDOATUAL).append(" NUMERIC(10,2) );");
        db.execSQL(query.toString());
        
        query = new StringBuilder();
        query.append("CREATE TABLE ").append(Categoria.TABLE).append("(")
                .append(Categoria.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(Categoria.DESCRICAO).append(" TEXT, ")
                .append(Categoria.TIPO).append(" TEXT );");
        db.execSQL(query.toString());
        
        query = new StringBuilder();
        query.append("CREATE TABLE ").append(Lancamento.TABLE).append("(")
                .append(Lancamento.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(Lancamento.TITULO).append(" TEXT, ")
                .append(Lancamento.VENCIMENTO).append(" TEXT, ")
                .append(Lancamento.PAGAMENTO).append(" TEXT, ")
                .append(Lancamento.CONTA_FK).append(" INTEGER, ")
                .append(Lancamento.CATEGORIA_FK).append(" INTEGER, ")
                .append(Lancamento.VALOR).append(" NUMERIC(10,2), ")
                .append(Lancamento.OBSERVACAO).append(" TEXT, ")
                .append(Lancamento.SITUACAO).append(" TEXT, ")
                .append("FOREIGN KEY (").append(Lancamento.CONTA_FK).append(") REFERENCES ")
                .append(Conta.TABLE).append(" (").append(Conta.ID).append("), ")
                .append("FOREIGN KEY (").append(Lancamento.CATEGORIA_FK).append(") REFERENCES ")
                .append(Categoria.TABLE).append(" (").append(Categoria.ID).append(") ); ");
        db.execSQL(query.toString());
        
        query = new StringBuilder();
        query.append("CREATE TABLE ").append(Usuario.TABLE).append(" (")
                .append(Usuario.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(Usuario.PASSWORD).append(" TEXT NOT NULL, ")
                .append(Usuario.EMAIL).append(" TEXT );");
        db.execSQL(query.toString());
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
