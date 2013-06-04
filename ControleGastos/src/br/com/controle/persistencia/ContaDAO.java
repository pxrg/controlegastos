package br.com.controle.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.controle.dominio.Conta;
import br.com.controle.exceptions.DaoException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author igor.santos
 */
public class ContaDAO extends DatabaseHelper {

    public ContaDAO(Context ctx) {
        super(ctx);
    }

    public Conta saveOrUpdate(Conta conta) throws DaoException {
        Conta res = null;
        SQLiteDatabase base = null;
        try {
            base = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Conta.BANCO, conta.getBanco());
            values.put(Conta.AGENCIA, conta.getAgencia());
            values.put(Conta.NUMERO, conta.getNumero());
            values.put(Conta.TIPO, conta.getTipo());
            values.put(Conta.SALDOINICIAL, conta.getSaldoinicial());
            values.put(Conta.SALDOATUAL, conta.getSaldoatual());

            if (conta.getId() != 0) {
                int i = base.update(Conta.TABLE, values, Conta.ID + "=" + conta.getId(), null);
                if (i == 1) {
                    res = conta;
                }
            } else {
                int i = (int) base.insert(Conta.TABLE, Conta.ID, values);
                if (i != -1) {
                    conta.setId(i);
                    res = conta;
                }
            }
            base.close();
            return res;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public List<Conta> findAllContas() throws DaoException {
        SQLiteDatabase base;
        List<Conta> contas = new ArrayList<Conta>();
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Conta.TABLE);

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Conta conta = new Conta();
                    conta.setId(cursor.getInt(0));
                    conta.setBanco(cursor.getString(1));
                    conta.setAgencia(cursor.getString(2));
                    conta.setNumero(cursor.getString(3));
                    conta.setTipo(cursor.getString(4));
                    conta.setSaldoinicial(cursor.getFloat(5));
                    conta.setSaldoatual(cursor.getFloat(6));
                    contas.add(conta);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            base.close();
            return contas;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public double calculaSaldoTotalAtual() throws DaoException {
        double res = 0.0;
        SQLiteDatabase base;
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT SUM(").append(Conta.SALDOATUAL).append(") FROM ").append(Conta.TABLE);

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToFirst()) {
                res = cursor.getDouble(0);
            }
            cursor.close();
            base.close();
            return res;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public Conta findContaById(int id) throws DaoException {
        SQLiteDatabase base;
        Conta conta = null;
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Conta.TABLE).append(" WHERE ")
                    .append(Conta.ID).append(" = ").append(id);

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToFirst()) {
                conta = new Conta();
                conta.setId(cursor.getInt(0));
                conta.setBanco(cursor.getString(1));
                conta.setAgencia(cursor.getString(2));
                conta.setNumero(cursor.getString(3));
                conta.setTipo(cursor.getString(4));
                conta.setSaldoinicial(cursor.getFloat(5));
                conta.setSaldoatual(cursor.getFloat(6));
            }
            cursor.close();
            base.close();
            return conta;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }
}
