package br.com.controle.principal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import br.com.controle.adapter.LancamentoaListAdapter;
import br.com.controle.dominio.Categoria;
import br.com.controle.dominio.Conta;
import br.com.controle.dominio.Lancamento;
import br.com.controle.enumerator.SITUACAOENUM;
import br.com.controle.exceptions.DaoException;
import br.com.controle.persistencia.CategoriaDAO;
import br.com.controle.persistencia.ContaDAO;
import br.com.controle.persistencia.LancamentoDAO;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author igor.santos
 */
public class PrincipalActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    TextView saldoAtualText;
    ListView lancamentos;
    private Conta conta;
    private Lancamento item;
    private String filtro;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.principal);

        saldoAtualText = (TextView) findViewById(R.id.saldoAtualText);
        lancamentos = (ListView) findViewById(R.id.lancamentosList);
        lancamentos.setOnItemClickListener(this);

        registerForContextMenu(lancamentos);
    }

    @Override
    protected void onResume() {
        saldoAtualText.setText(saldoTotalAtual());
        super.onResume();
    }

    private String saldoTotalAtual() {
        try {
            ContaDAO dao = new ContaDAO(this);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            return numberFormat.format(dao.calculaSaldoTotalAtual());
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(this, LancamentoActivity.class));
                break;
            case R.id.listar:
                abrirFiltroDialog();
                break;
        }
        return true;
    }

    private void abrirFiltroDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_filtro_lancamento);
        dialog.setCancelable(true);
        dialog.setTitle("Filtro");

        final Spinner filtro_lancamento = (Spinner) dialog.findViewById(R.id.filtro_lancamento);
        List<String> filtros = new ArrayList<String>();
        filtros.add("Todos");
        filtros.add("Vencimento Hoje");
        filtros.add(SITUACAOENUM.CONCLUIDO);
        filtros.add(SITUACAOENUM.ABERTO);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filtros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtro_lancamento.setAdapter(adapter);

        Button confirmar = (Button) dialog.findViewById(R.id.confirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filtro = filtro_lancamento.getSelectedItem().toString().replace("Todos", "");
                if (filtro.equals("Vencimento Hoje")) {
                    listarLancamentos(filtro, 1);
                    dialog.dismiss();
                } else {
                    listarLancamentos(filtro, 0);
                    dialog.dismiss();
                }
            }
        });
        Button cancelar = (Button) dialog.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void listarLancamentos(String filtro, int i) {
        try {
            LancamentoDAO dao = new LancamentoDAO(this);
            LancamentoaListAdapter adapter = null;
            if (i == 0) {
                adapter = new LancamentoaListAdapter(this, dao.findLancamentosFilter(filtro));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String venc = sdf.format(new Date());
                adapter = new LancamentoaListAdapter(this, dao.lancamentosAVencer(sdf.parse(venc).getTime()));
            }
            lancamentos.setAdapter(adapter);
        } catch (ParseException ex) {  
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
        }
    }

    public void onItemClick(AdapterView<?> av, View view, int i, long l) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.activity_lancamento);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_visualizar_lancamento);
        try {
            item = (Lancamento) av.getItemAtPosition(i);
            ((TextView) dialog.findViewById(R.id.tituloText)).setText(item.getTitulo());
            ((TextView) dialog.findViewById(R.id.vencimentoText)).setText(new SimpleDateFormat("dd/MM/yyyy").format(item.getVencimento()));

            final ContaDAO daoConta = new ContaDAO(this);
            conta = daoConta.findContaById(item.getConta().getId());
            CategoriaDAO daoCategoria = new CategoriaDAO(this);
            Categoria categoria = daoCategoria.findCategoriaById(item.getCategoria().getId());
            ((TextView) dialog.findViewById(R.id.contaBancoText)).setText(conta.toString());
            ((TextView) dialog.findViewById(R.id.descricaoCategoriaText)).setText(categoria.getDescricao() + " - " + categoria.getTipo());
            ((TextView) dialog.findViewById(R.id.valorText)).setText(NumberFormat.getCurrencyInstance().format(item.getValor()));
            ((TextView) dialog.findViewById(R.id.observacaoText)).setText(item.getObservacao());
            ((TextView) dialog.findViewById(R.id.situacaoText)).setText(item.getSituacao());
            Button pagar = (Button) dialog.findViewById(R.id.pagarButton);
            if (item.getPagamento() != null) {
                ((TextView) dialog.findViewById(R.id.pagamentoText)).setText(new SimpleDateFormat("dd/MM/yyyy").format(item.getPagamento()));
                pagar.setVisibility(View.GONE);
            } else {
                pagar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        item.setSituacao(SITUACAOENUM.CONCLUIDO);
                        item.setPagamento(new Date());
                        conta.setSaldoatual(conta.getSaldoatual() - item.getValor());
                        try {
                            LancamentoDAO dao = new LancamentoDAO(getApplicationContext());
                            dao.saveOrUpdate(item);
                            daoConta.saveOrUpdate(conta);
                            dialog.dismiss();
                            listarLancamentos(filtro, 0);
                            onResume();
                        } catch (DaoException ex) {
                            Log.e("[LOG] APP", ex.getMessage());
                        }
                    }
                });
            }
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
        }
        dialog.show();
    }

    public boolean onItemLongClick(AdapterView<?> av, View view, int i, long l) {
        view.showContextMenu();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Opções");
        getMenuInflater().inflate(R.menu.lancamento_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pagar) {
            LancamentoDAO dao = new LancamentoDAO(this);
        }
        return super.onContextItemSelected(item);
    }
}
