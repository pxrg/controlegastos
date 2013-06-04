package br.com.controle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.controle.dominio.Conta;
import br.com.controle.principal.R;
import br.com.controle.utils.StringUtils;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author Igor
 */
public class ContaListAdapter extends BaseAdapter {

    private Context mCtx;
    private List<Conta> mItens;
    private NumberFormat numberFormat;

    public ContaListAdapter(Context ctx, List<Conta> itens) {
        mCtx = ctx;
        mItens = itens;
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    public int getCount() {
        return mItens.size();
    }

    public Object getItem(int i) {
        return mItens.get(i);
    }

    public long getItemId(int i) {
        return mItens.get(i).getId();
    }

    public View getView(int i, View view, ViewGroup vg) {
        ContaHolder holder;
        Conta item = (Conta) getItem(i);

        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(mCtx).inflate(R.layout.conta_list, vg, false);
            holder = new ContaHolder();
            holder.mBancoImage = (ImageView) view.findViewById(R.id.bancoImage);
            holder.mNomeBancoText = (TextView) view.findViewById(R.id.nomeBancoText);
            holder.mSaldoAtualText = (TextView) view.findViewById(R.id.saldoAtualText);
            view.setTag(holder);
        } else {
            holder = (ContaHolder) view.getTag();
        }

        if (item.getBanco().toLowerCase().contains("santander")) {
            holder.mBancoImage.setImageResource(R.drawable.logo_santander);
        } else if (item.getBanco().toLowerCase().contains("bradesco")) {
            holder.mBancoImage.setImageResource(R.drawable.logo_bradesco);
        } else if (item.getBanco().toLowerCase().contains("caixa")) {
            holder.mBancoImage.setImageResource(R.drawable.logo_caixa);
        } else if (StringUtils.replaceAcentos(item.getBanco()).toLowerCase().contains("itau")) {
            holder.mBancoImage.setImageResource(R.drawable.logo_itau);
        } else if (item.getBanco().toLowerCase().contains("brasil")) {
            holder.mBancoImage.setImageResource(R.drawable.logo_brasil);
        }
        holder.mNomeBancoText.setText(item.getBanco());
        holder.mSaldoAtualText.setText(numberFormat.format(item.getSaldoatual()));
        if (item.getSaldoatual() < 0) {
            holder.mSaldoAtualText.setTextColor(view.getResources().getColor(R.color.error));
        }

        return view;
    }

    private class ContaHolder {

        ImageView mBancoImage;
        TextView mNomeBancoText;
        TextView mSaldoAtualText;
    }
}
