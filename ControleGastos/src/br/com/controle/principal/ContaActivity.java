package br.com.controle.principal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.controle.adapter.Mask;
import br.com.controle.adapter.ToastManager;
import br.com.controle.dominio.Conta;
import br.com.controle.enumerator.CONTAENUM;
import br.com.controle.exceptions.DaoException;
import br.com.controle.exceptions.ServiceException;
import br.com.controle.persistencia.ContaDAO;
import br.com.controle.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author igor.santos
 */
public class ContaActivity extends Activity {

    EditText bancoText, agenciaText, numeroContaText, saldoInicialText, saldoAtualText;
    Spinner tipoContaSpinner;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.conta_form);

        List<String> tipos = new ArrayList<String>();
        tipos.add(CONTAENUM.CORRENTE);
        tipos.add(CONTAENUM.POUPANCA);
        tipoContaSpinner = (Spinner) findViewById(R.id.tipoContaSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoContaSpinner.setAdapter(adapter);

        bancoText = (EditText) findViewById(R.id.bancoText);
        agenciaText = (EditText) findViewById(R.id.agenciaText);
        numeroContaText = (EditText) findViewById(R.id.numeroContaText);
        saldoInicialText = (EditText) findViewById(R.id.saldoInicialText);
        saldoInicialText.addTextChangedListener(Mask.monetario(saldoInicialText));
        saldoAtualText = (EditText) findViewById(R.id.saldoAtualText);
    }

    public void contaOnClick(View view) {
        if (view.getId() == R.id.salvar) {
            try {
                Conta conta = validaCampos();
                ContaDAO dao = new ContaDAO(this);
                conta = dao.saveOrUpdate(conta);
                if (conta != null) {
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

    private Conta validaCampos() throws ServiceException {
        String banco = bancoText.getText().toString();
        String saldoInicial = Mask.unmask(saldoInicialText.getText().toString());
        if (banco.isEmpty() || saldoInicial.isEmpty()) {
            if (banco.isEmpty()) {
                bancoText.setBackgroundColor(getResources().getColor(R.color.error));
            }
            if (saldoInicial.isEmpty()) {
                saldoInicialText.setBackgroundColor(getResources().getColor(R.color.error));
            }
            throw new ServiceException(getResources().getString(R.string.error_campos_vazios));
        }
        Conta conta = new Conta();
        conta.setBanco(StringUtils.capitalize(banco));
        conta.setAgencia(agenciaText.getText().toString());
        conta.setNumero(numeroContaText.getText().toString());
        conta.setTipo(tipoContaSpinner.getSelectedItem().toString());
        conta.setSaldoinicial(Float.valueOf(saldoInicial));
        String saldoAtual = saldoAtualText.getText().toString();
        if (saldoAtual.isEmpty()) {
            conta.setSaldoatual(Float.valueOf(saldoInicial));
        } else {
            conta.setSaldoatual(Float.valueOf(saldoAtual));
        }
        return conta;
    }
}
