package br.com.controle.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.controle.dominio.Usuario;
import br.com.controle.exceptions.DaoException;

/**
 * @author Igor
 */
public class UsuarioDAO extends DatabaseHelper {

    public UsuarioDAO(Context ctx) {
        super(ctx);
    }

    public boolean savePasswordUser(Usuario usuario) throws DaoException {
        try {
            boolean retorno = false;
            SQLiteDatabase base = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Usuario.PASSWORD, usuario.getPassword());
            long verifica = base.insert(Usuario.TABLE, Usuario.ID, values);
            if (verifica != 0) {
                retorno = true;
            }
            base.close();
            return retorno;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public Usuario findUsuario() throws DaoException {
        try {
            SQLiteDatabase base = getReadableDatabase();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(Usuario.TABLE);

            Cursor cursor = base.rawQuery(query.toString(), null);
            Usuario user = null;
            if (cursor != null && cursor.moveToFirst()) {
                user = new Usuario(cursor.getString(1));
            }
            cursor.close();
            base.close();
            return user;
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        }
    }
}
