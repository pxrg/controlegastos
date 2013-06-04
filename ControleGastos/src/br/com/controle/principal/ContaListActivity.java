package br.com.controle.principal;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import br.com.controle.adapter.ContaListAdapter;
import br.com.controle.dominio.Conta;
import br.com.controle.exceptions.DaoException;
import br.com.controle.persistencia.ContaDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * @author igor.santos
 */
public class ContaListActivity extends ListActivity {

    private ContaListAdapter adapter;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        registerForContextMenu(getListView());
    }

    @Override
    protected void onResume() {
        adapter = new ContaListAdapter(this, listarContas());
        setListAdapter(adapter);
        super.onResume();
    }

    private List<Conta> listarContas() {
        try {
            ContaDAO dao = new ContaDAO(this);
            return dao.findAllContas();
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
            return new ArrayList<Conta>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conta_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(this, ContaActivity.class));
                break;
        }
        return true;
    }
}
