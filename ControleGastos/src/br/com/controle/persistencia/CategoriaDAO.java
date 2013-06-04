package br.com.controle.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.controle.dominio.Categoria;
import br.com.controle.exceptions.DaoException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author igor.santos
 */
public class CategoriaDAO extends DatabaseHelper {

    public CategoriaDAO(Context ctx) {
        super(ctx);
    }

    public Categoria saveOrUpdate(Categoria categoria) throws DaoException {
        Categoria res = null;
        SQLiteDatabase base = null;
        try {
            base = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Categoria.DESCRICAO, categoria.getDescricao());
            values.put(Categoria.TIPO, categoria.getTipo());

            if (categoria.getId() != 0) {
                int i = base.update(Categoria.TABLE, values, Categoria.ID + "=" + categoria.getId(), null);
                if (i == 1) {
                    res = categoria;
                }
            } else {
                int i = (int) base.insert(Categoria.TABLE, Categoria.ID, values);
                if (i != -1) {
                    categoria.setId(i);
                    res = categoria;
                }
            }
            base.close();
            return res;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public List<Categoria> findAllCategorias() throws DaoException {
        SQLiteDatabase base;
        List<Categoria> categorias = new ArrayList<Categoria>();
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Categoria.TABLE);

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToNext()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Categoria categoria = new Categoria();
                    categoria.setId(cursor.getInt(0));
                    categoria.setDescricao(cursor.getString(1));
                    categoria.setTipo(cursor.getString(2));
                    categorias.add(categoria);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            base.close();
            return categorias;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public Categoria findCategoriaById(int id) throws DaoException {
        SQLiteDatabase base;
        Categoria categoria = null;
        try {
            base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Categoria.TABLE).append(" WHERE ")
                    .append(Categoria.ID).append(" = ").append(id);

            Cursor cursor = base.rawQuery(query.toString(), null);
            if (cursor != null && cursor.moveToFirst()) {
                categoria = new Categoria();
                categoria.setId(cursor.getInt(0));
                categoria.setDescricao(cursor.getString(1));
                categoria.setTipo(cursor.getString(2));
            }
            cursor.close();
            base.close();
            return categoria;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }
}