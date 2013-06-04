package br.com.controle.principal;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import br.com.controle.adapter.CategoriaListAdapter;
import br.com.controle.dominio.Categoria;
import br.com.controle.exceptions.DaoException;
import br.com.controle.persistencia.CategoriaDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * @author igor.santos
 */
public class CategoriaListActivity extends ListActivity {

    private CategoriaListAdapter adapter;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        registerForContextMenu(getListView());
    }

    @Override
    protected void onResume() {
        adapter = new CategoriaListAdapter(this, listarCategorias());
        setListAdapter(adapter);
        super.onResume();
    }

    private List<Categoria> listarCategorias() {
        try {
            CategoriaDAO dao = new CategoriaDAO(this);
            return dao.findAllCategorias();
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
            return new ArrayList<Categoria>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.categoria_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(this, CategoriaActivity.class));
                break;
        }
        return true;
    }
}
