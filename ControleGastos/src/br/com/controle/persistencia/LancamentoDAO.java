package br.com.controle.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.controle.dominio.Conta;
import br.com.controle.dominio.Lancamento;
import br.com.controle.enumerator.CATEGORIAENUM;
import br.com.controle.enumerator.SITUACAOENUM;
import br.com.controle.exceptions.DaoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author igor.santos
 */
public class LancamentoDAO extends DatabaseHelper {

    public LancamentoDAO(Context ctx) {
        super(ctx);
    }

    public Lancamento saveOrUpdate(Lancamento lancamento) throws DaoException {
        Lancamento res = null;
        SQLiteDatabase base = null;
        try {
            base = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Lancamento.TITULO, lancamento.getTitulo());
            values.put(Lancamento.VENCIMENTO, lancamento.getVencimento().getTime());
            Date pagamento = lancamento.getPagamento();
            if (pagamento != null) {
                values.put(Lancamento.PAGAMENTO, pagamento.getTime());
            }
            values.put(Lancamento.CONTA_FK, lancamento.getConta().getId());
            values.put(Lancamento.CATEGORIA_FK, lancamento.getCategoria().getId());
            values.put(Lancamento.VALOR, lancamento.getValor());
            values.put(Lancamento.OBSERVACAO, lancamento.getObservacao());
            values.put(Lancamento.SITUACAO, lancamento.getSituacao());

            if (lancamento.getId() != 0) {
                int i = base.update(Lancamento.TABLE, values, Lancamento.ID + "=" + lancamento.getId(), null);
                if (i == 1) {
                    res = lancamento;
                }
            } else {
                int i = (int) base.insert(Lancamento.TABLE, Lancamento.ID, values);
                if (i != -1) {
                    lancamento.setId(i);
                    res = lancamento;
                    if (lancamento.getSituacao().equals(SITUACAOENUM.CONCLUIDO)) {
                        values = new ContentValues();
                        if (lancamento.getCategoria().getTipo().equals(CATEGORIAENUM.SAIDA)) {
                            values.put(Conta.SALDOATUAL, lancamento.getConta().getSaldoatual() - lancamento.getValor());
                        } else {
                            values.put(Conta.SALDOATUAL, lancamento.getConta().getSaldoatual() + lancamento.getValor());
                        }
                        base.update(Conta.TABLE, values, Conta.ID + " = " + lancamento.getConta().getId(), null);
                    }
                }
            }
            base.close();
            return res;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public List<Lancamento> findLancamentosFilter(String filtro) throws DaoException {
        SQLiteDatabase base;
        List<Lancamento> lancamentos = new ArrayList<Lancamento>();
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Lancamento.TABLE).append(" WHERE ").append(Lancamento.SITUACAO).append(" LIKE '%").append(filtro).append("%'");

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Lancamento lancamento = new Lancamento();
                    lancamento.setId(cursor.getInt(0));
                    lancamento.setTitulo(cursor.getString(1));
                    lancamento.setVencimento(new Date(cursor.getLong(2)));
                    long pagamento = cursor.getLong(3);
                    lancamento.setPagamento(pagamento != 0 ? new Date(pagamento) : null);
                    lancamento.getConta().setId(cursor.getInt(4));
                    lancamento.getCategoria().setId(cursor.getInt(5));
                    lancamento.setValor(cursor.getFloat(6));
                    lancamento.setObservacao(cursor.getString(7));
                    lancamento.setSituacao(cursor.getString(8));
                    lancamentos.add(lancamento);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            base.close();
            return lancamentos;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public List<Lancamento> findAllLancamentos() throws DaoException {
        SQLiteDatabase base;
        List<Lancamento> lancamentos = new ArrayList<Lancamento>();
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Lancamento.TABLE);

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Lancamento lancamento = new Lancamento();
                    lancamento.setId(cursor.getInt(0));
                    lancamento.setTitulo(cursor.getString(1));
                    lancamento.setVencimento(new Date(cursor.getLong(2)));
                    long pagamento = cursor.getLong(3);
                    lancamento.setPagamento(pagamento != 0 ? new Date(pagamento) : null);
                    lancamento.getConta().setId(cursor.getInt(4));
                    lancamento.getCategoria().setId(cursor.getInt(5));
                    lancamento.setValor(cursor.getFloat(6));
                    lancamento.setObservacao(cursor.getString(7));
                    lancamento.setSituacao(cursor.getString(8));
                    lancamentos.add(lancamento);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            base.close();
            return lancamentos;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public List<Lancamento> lancamentosAVencer(long data) throws DaoException {
        try {
            List<Lancamento> lancamentos = new ArrayList<Lancamento>();
            SQLiteDatabase base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Lancamento.TABLE)
                    .append(" WHERE ").append(Lancamento.VENCIMENTO).append(" = '")
                    .append(data).append("'");

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    long pagamento = cursor.getLong(3);
                    if (pagamento == 0) {
                        Lancamento lancamento = new Lancamento();
                        lancamento.setId(cursor.getInt(0));
                        lancamento.setTitulo(cursor.getString(1));
                        lancamento.setVencimento(new Date(cursor.getLong(2)));
                        lancamento.getConta().setId(cursor.getInt(4));
                        lancamento.getCategoria().setId(cursor.getInt(5));
                        lancamento.setValor(cursor.getFloat(6));
                        lancamento.setObservacao(cursor.getString(7));
                        lancamento.setSituacao(cursor.getString(8));
                        lancamentos.add(lancamento);
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
            base.close();
            return lancamentos;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }
}
