package br.com.controle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.controle.dominio.Lancamento;
import br.com.controle.enumerator.SITUACAOENUM;
import br.com.controle.principal.R;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author igor.santos
 */
public class LancamentoaListAdapter extends ArrayAdapter<Lancamento> {

    private LancamentoHolder mHolder;
    private LayoutInflater mInflater;
    private NumberFormat numberFormat;
    private SimpleDateFormat dateFormat;

    public LancamentoaListAdapter(Context ctx, List<Lancamento> itens) {
        super(ctx, R.layout.lancamento_list, itens);
        mInflater = LayoutInflater.from(ctx);
        numberFormat = NumberFormat.getCurrencyInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Lancamento item = getItem(position);
        if (view == null || view.getTag() == null) {
            view = mInflater.inflate(R.layout.lancamento_list, null);
            mHolder = new LancamentoHolder();
            mHolder.mTituloLancamentoText = (TextView) view.findViewById(R.id.tituloLancamentoText);
            mHolder.mValorLancamentoText = (TextView) view.findViewById(R.id.valorLancamentoText);
            mHolder.mVencimentoLancamentoText = (TextView) view.findViewById(R.id.vencimentoLancamentoText);
            mHolder.mSituacaoImage = (ImageView) view.findViewById(R.id.situacaoImage);
            view.setTag(mHolder);
        } else {
            mHolder = (LancamentoHolder) view.getTag();
        }
        mHolder.mTituloLancamentoText.setText(item.getTitulo());
        mHolder.mValorLancamentoText.setText(numberFormat.format(item.getValor()));
        mHolder.mVencimentoLancamentoText.setText(dateFormat.format(item.getVencimento()));
        if (item.getSituacao().equals(SITUACAOENUM.ABERTO)) {
            mHolder.mSituacaoImage.setImageResource(R.drawable.ic_aberto);
        } else {
            mHolder.mSituacaoImage.setImageResource(R.drawable.ic_fechado);
        }
        return view;
    }

    private class LancamentoHolder {

        TextView mTituloLancamentoText;
        TextView mValorLancamentoText;
        TextView mVencimentoLancamentoText;
        ImageView mSituacaoImage;
    }
}
