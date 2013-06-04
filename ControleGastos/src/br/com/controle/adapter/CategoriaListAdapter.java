package br.com.controle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.controle.dominio.Categoria;
import br.com.controle.enumerator.CATEGORIAENUM;
import br.com.controle.principal.R;
import java.util.List;

/**
 * @author igor.santos
 */
public class CategoriaListAdapter extends ArrayAdapter<Categoria> {

    private CategoriaHolder mHolder;
    private LayoutInflater mInflater;

    public CategoriaListAdapter(Context ctx, List<Categoria> itens) {
        super(ctx, R.layout.categoria_list, itens);
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Categoria item = getItem(position);
        if (view == null || view.getTag() == null) {
            view = mInflater.inflate(R.layout.categoria_list, null);
            mHolder = new CategoriaHolder();
            mHolder.mNomeCategoriaText = (TextView) view.findViewById(R.id.nomeCategoriaText);
            mHolder.mTipoCategoriaText = (TextView) view.findViewById(R.id.tipoCategoriaText);
            mHolder.mTipoImage = (ImageView) view.findViewById(R.id.tipoImage);
            view.setTag(mHolder);
        } else {
            mHolder = (CategoriaHolder) view.getTag();
        }
        mHolder.mNomeCategoriaText.setText(item.getDescricao());
        mHolder.mTipoCategoriaText.setText(item.getTipo());
        if (item.getTipo().equals(CATEGORIAENUM.ENTRADA)) {
            mHolder.mTipoImage.setImageResource(R.drawable.ic_entrada);
        } else if (item.getTipo().equals(CATEGORIAENUM.SAIDA)) {
            mHolder.mTipoImage.setImageResource(R.drawable.ic_saida);
        }
        return view;
    }

    private class CategoriaHolder {

        TextView mNomeCategoriaText;
        TextView mTipoCategoriaText;
        ImageView mTipoImage;
    }
}
