package br.com.controle.principal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.controle.adapter.Mask;
import br.com.controle.adapter.ToastManager;
import br.com.controle.dominio.Categoria;
import br.com.controle.dominio.Conta;
import br.com.controle.dominio.Lancamento;
import br.com.controle.enumerator.PERIODOENUM;
import br.com.controle.enumerator.SITUACAOENUM;
import br.com.controle.exceptions.DaoException;
import br.com.controle.exceptions.ServiceException;
import br.com.controle.persistencia.CategoriaDAO;
import br.com.controle.persistencia.ContaDAO;
import br.com.controle.persistencia.LancamentoDAO;
import br.com.controle.utils.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author igor.santos
 */
public class LancamentoActivity extends Activity {

    EditText parcelasText, tituloText, valorText, observacaoText, intervaloText;
    CheckBox replicarCheck;
    Button vencimentoText, pagamentoText;
    Spinner categoriaSpinner, contaSpinner, situacaoSpinner, periodoSpinner;
    int ano, mes, dia;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.lancamento_form);

        Calendar calendario = Calendar.getInstance();
        ano = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH);
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        vencimentoText = (Button) findViewById(R.id.vencimentoText);
        vencimentoText.setText(StringUtils.formatDate(dia, mes + 1, ano));
        pagamentoText = (Button) findViewById(R.id.pagamentoText);
        pagamentoText.setText(StringUtils.formatDate(dia, mes + 1, ano));

        parcelasText = (EditText) findViewById(R.id.parcelasText);
        intervaloText = (EditText) findViewById(R.id.intervaloText);
        tituloText = (EditText) findViewById(R.id.tituloText);
        valorText = (EditText) findViewById(R.id.valorText);
        valorText.addTextChangedListener(Mask.monetario(valorText));
        observacaoText = (EditText) findViewById(R.id.observacaoText);

        categoriaSpinner = (Spinner) findViewById(R.id.categoriaSpinner);
        contaSpinner = (Spinner) findViewById(R.id.contaSpinner);

        List<String> situacoes = new ArrayList<String>();
        situacoes.add(SITUACAOENUM.CONCLUIDO);
        situacoes.add(SITUACAOENUM.ABERTO);
        situacaoSpinner = (Spinner) findViewById(R.id.situacaoSpinner);
        ArrayAdapter adapterSituacao = new ArrayAdapter(this, android.R.layout.simple_spinner_item, situacoes);
        adapterSituacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        situacaoSpinner.setAdapter(adapterSituacao);
        situacaoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> av, View view, int i, long l) {
                if (av.getItemAtPosition(i).toString().equals(SITUACAOENUM.ABERTO)) {
                    pagamentoText.setVisibility(View.INVISIBLE);
                } else {
                    pagamentoText.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> av) {
            }
        });

        List<String> periodos = new ArrayList<String>();
        periodos.add(PERIODOENUM.MES);
        periodos.add(PERIODOENUM.DIA);
        periodoSpinner = (Spinner) findViewById(R.id.periodoSpinner);
        ArrayAdapter adapterPeriodo = new ArrayAdapter(this, android.R.layout.simple_spinner_item, periodos);
        adapterPeriodo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodoSpinner.setAdapter(adapterPeriodo);

        replicarCheck = (CheckBox) findViewById(R.id.replicarCheck);

        try {
            ArrayAdapter<Conta> adapterConta = new ArrayAdapter<Conta>(this, android.R.layout.simple_spinner_item, listarContas());
            adapterConta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            contaSpinner.setAdapter(adapterConta);

            ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_item, listarCategorias());
            adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoriaSpinner.setAdapter(adapterCategoria);
        } catch (ServiceException ex) {
            ToastManager.show(this, ex.getMessage(), ToastManager.WARNING);
            finish();
        }
    }

    private List<Categoria> listarCategorias() throws ServiceException {
        try {
            CategoriaDAO dao = new CategoriaDAO(this);
            List<Categoria> categorias = dao.findAllCategorias();
            if (!categorias.isEmpty()) {
                return categorias;
            } else {
                throw new ServiceException(getResources().getString(R.string.cadastrar_categoria));
            }
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
            return new ArrayList<Categoria>();
        }
    }

    private List<Conta> listarContas() throws ServiceException {
        try {
            ContaDAO dao = new ContaDAO(this);
            List<Conta> contas = dao.findAllContas();
            if (!contas.isEmpty()) {
                return contas;
            } else {
                throw new ServiceException(getResources().getString(R.string.cadastrar_conta));
            }
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
            return new ArrayList<Conta>();
        }
    }

    public void lancamentoOnClick(View view) {
        if (view.getId() == R.id.salvar) {
            try {
                List<Lancamento> lancamentos = validaCampos();
                LancamentoDAO dao = new LancamentoDAO(this);
                for (Lancamento lancamento : lancamentos) {
                    dao.saveOrUpdate(lancamento);
                }
                ToastManager.show(this, getResources().getString(R.string.sucesso_msg), ToastManager.INFORMATION);
                finish();
            } catch (ServiceException ex) {
                ToastManager.show(this, ex.getMessage(), ToastManager.WARNING);
            } catch (DaoException ex) {
                Log.e("[LOG] APP", ex.getMessage());
            }
        } else if (view.getId() == R.id.cancelar) {
            finish();
        }
    }

    private List<Lancamento> validaCampos() throws ServiceException {
        String titulo = tituloText.getText().toString();
        String valor = Mask.unmask(valorText.getText().toString());
        if (titulo.isEmpty() || valor.isEmpty()) {
            if (titulo.isEmpty()) {
                tituloText.setBackgroundColor(getResources().getColor(R.color.error));
            }
            if (valor.isEmpty()) {
                valorText.setBackgroundColor(getResources().getColor(R.color.error));
            }
            throw new ServiceException(getResources().getString(R.string.error_campos_vazios));
        }
        String intervalo = intervaloText.getText().toString();
        int periodo = 0;
        if (replicarCheck.isChecked() && !intervalo.isEmpty()) {
            periodo = Integer.valueOf(intervalo);
        }
        int replicar = 1;
        if (replicarCheck.isChecked() && !parcelasText.getText().toString().isEmpty()) {
            replicar = Integer.valueOf(parcelasText.getText().toString()) + 1;
        }
        List<Lancamento> lancamentos = new ArrayList<Lancamento>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < replicar; i++) {
                Lancamento lancamento = new Lancamento();
                lancamento.setTitulo(StringUtils.capitalize(titulo));
                lancamento.setConta((Conta) contaSpinner.getSelectedItem());
                lancamento.setCategoria((Categoria) categoriaSpinner.getSelectedItem());
                lancamento.setValor(Float.valueOf(valor));
                lancamento.setObservacao(observacaoText.getText().toString());
                calendar.setTime(sdf.parse(vencimentoText.getText().toString()));
                if (i > 0) {
                    lancamento.setSituacao(SITUACAOENUM.ABERTO);
                    if (periodoSpinner.getSelectedItem().equals(PERIODOENUM.MES)) {
                        calendar.add(Calendar.MONTH, periodo * i);
                        lancamento.setVencimento(calendar.getTime());
                    } else if (periodoSpinner.getSelectedItem().equals(PERIODOENUM.DIA)) {
                        calendar.add(Calendar.DAY_OF_MONTH, periodo * i);
                        lancamento.setVencimento(calendar.getTime());
                    }
                } else {
                    lancamento.setSituacao((String) situacaoSpinner.getSelectedItem());
                    lancamento.setVencimento(calendar.getTime());
                    if (situacaoSpinner.getSelectedItem().equals(SITUACAOENUM.CONCLUIDO)) {
                        lancamento.setPagamento(new SimpleDateFormat("dd/MM/yyyy").parse(pagamentoText.getText().toString()));
                    }
                }
                lancamentos.add(lancamento);
            }
        } catch (ParseException ex) {
            Log.e("[LOG] APP", ex.getMessage());
        }
        return lancamentos;
    }

    public void replicarOnClick(View view) {
        if (replicarCheck.isChecked()) {
            parcelasText.setEnabled(true);
            intervaloText.setEnabled(true);
        } else {
            parcelasText.setEnabled(false);
            intervaloText.setEnabled(false);
        }
    }

    public void dateOnClick(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog dateDialog;
        if (R.id.vencimentoText == id) {
            dateDialog = new DatePickerDialog(this, devolveDateListener(R.id.vencimentoText), ano, mes, dia);
            dateDialog.setTitle(getResources().getString(R.string.vencimento));
            return dateDialog;
        } else if (R.id.pagamentoText == id) {
            dateDialog = new DatePickerDialog(this, devolveDateListener(R.id.pagamentoText), ano, mes, dia);
            dateDialog.setTitle(getResources().getString(R.string.pagamento));
            return dateDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener devolveDateListener(int view) {
        final Button widget = (Button) findViewById(view);
        return new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String data = StringUtils.formatDate(dayOfMonth, monthOfYear + 1, year);
                widget.setText(data);
            }
        };
    }
}
