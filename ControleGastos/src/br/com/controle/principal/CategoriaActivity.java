package br.com.controle.principal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.controle.adapter.ToastManager;
import br.com.controle.dominio.Categoria;
import br.com.controle.enumerator.CATEGORIAENUM;
import br.com.controle.exceptions.DaoException;
import br.com.controle.exceptions.ServiceException;
import br.com.controle.persistencia.CategoriaDAO;
import br.com.controle.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author igor.santos
 */
public class CategoriaActivity extends Activity {
    
    EditText descricaoText;
    Spinner tipoCategoriaSpinner;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.categoria_form);
        
        List<String> tipos = new ArrayList<String>();
        tipos.add(CATEGORIAENUM.SAIDA);
        tipos.add(CATEGORIAENUM.ENTRADA);
        tipoCategoriaSpinner = (Spinner) findViewById(R.id.tipoCategoriaSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCategoriaSpinner.setAdapter(adapter);
        
        descricaoText = (EditText) findViewById(R.id.descricaoText);
        
    }
    
    public void categoriaOnClick(View view) {
        if (view.getId() == R.id.salvar) {
            try {
                Categoria categoria = validaCampos();
                CategoriaDAO dao = new CategoriaDAO(this);
                categoria = dao.saveOrUpdate(categoria);
                if (categoria != null) {
                    ToastManager.show(this, getResources().getString(R.string.sucesso_msg), ToastManager.INFORMATION);
                    finish();
                }
            } catch (DaoException ex) {
                Log.e("[LOG] APP", ex.getMessage());
            } catch (ServiceException ex) {
                ToastManager.show(this, ex.getMessage(), ToastManager.WARNING);
            }
        } else if (view.getId() == R.id.cancelar) {
            finish();
        }
    }
    
    private Categoria validaCampos() throws ServiceException {
        if (descricaoText.getText().toString().isEmpty()) {
            descricaoText.setBackgroundColor(getResources().getColor(R.color.error));
            throw new ServiceException(getResources().getString(R.string.error_campos_vazios));
        }
        Categoria categoria = new Categoria();
        categoria.setDescricao(StringUtils.capitalize(descricaoText.getText().toString()));
        categoria.setTipo(tipoCategoriaSpinner.getSelectedItem().toString());
        return categoria;
    }
}
