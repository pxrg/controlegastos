package br.com.controle.principal;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * @author igor.santos
 */
public class TabControleGastos extends TabActivity implements TabHost.OnTabChangeListener{

    private static TabHost tabHost;
//    private ContaListAdapter adapter;
    private static final String TAB_LANCAMENTO = "TAB_LANCAMENTO";
    private static final String TAB_CATEGORIAS = "TAB_CATEGORIAS";
    private static final String TAB_CONTAS = "TAB_CONTAS";
    
    private Menu menu;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.tabs_gastos);

        Resources res = getResources();
        tabHost = getTabHost();
        TabSpec tab;
        Intent intent;

        intent = new Intent(this, PrincipalActivity.class);
        tab = tabHost.newTabSpec(TAB_LANCAMENTO)
                .setIndicator(res.getString(R.string.activity_lancamento),
                res.getDrawable(android.R.drawable.ic_btn_speak_now))
                .setContent(intent);
        tabHost.addTab(tab);

        intent = new Intent(this, CategoriaListActivity.class);
        tab = tabHost.newTabSpec(TAB_CATEGORIAS)
                .setIndicator(res.getString(R.string.activity_categoria),
                res.getDrawable(android.R.drawable.ic_btn_speak_now))
                .setContent(intent);
        tabHost.addTab(tab);

        intent = new Intent(this, ContaListActivity.class);
        tab = tabHost.newTabSpec(TAB_CONTAS)
                .setIndicator(res.getString(R.string.activity_conta),
                res.getDrawable(android.R.drawable.ic_btn_speak_now))
                .setContent(intent);
        tabHost.addTab(tab);
        tabHost.setOnTabChangedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        setOptionMenu(tabHost.getCurrentTabTag());
        return super.onCreateOptionsMenu(menu);
    }
    
    public void onTabChanged(String string) {
        setOptionMenu(string);
        onPrepareOptionsMenu(menu);
        closeOptionsMenu();
    }
    
    private void setOptionMenu(String activity){
        this.menu.clear();
        if (activity.equals(TAB_CATEGORIAS)) {
            getMenuInflater().inflate(R.menu.categoria_menu, menu);
        }else if(activity.equals(TAB_CONTAS)){
            getMenuInflater().inflate(R.menu.conta_menu, menu);
        }else if(activity.equals(TAB_LANCAMENTO)){
            getMenuInflater().inflate(R.menu.principal_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String activity = tabHost.getCurrentTabTag();
        if (activity.equals(TAB_CATEGORIAS)) {
            ((CategoriaListActivity) getCurrentActivity()).onOptionsItemSelected(item);
        }else if(activity.equals(TAB_CONTAS)){
            ((ContaListActivity) getCurrentActivity()).onOptionsItemSelected(item);
        }else if(activity.equals(TAB_LANCAMENTO)){
            ((PrincipalActivity) getCurrentActivity()).onOptionsItemSelected(item);
        }
        return true;
    }
    
}
