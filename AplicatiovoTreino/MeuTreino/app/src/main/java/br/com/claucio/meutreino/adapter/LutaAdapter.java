package br.com.claucio.meutreino.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.model.Luta;

public class LutaAdapter extends BaseAdapter {

    private List<Luta> lutaList;
    private Context context;

    private Context context2;

    public LutaAdapter(List<Luta> lutaList, Context context) {
        this.lutaList = lutaList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lutaList.size();
    }

    @Override
    public Object getItem(int i) {
        return lutaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        //LayoutInflater inflater = context.getLayoutInflater();
        View view = View.inflate(context, R.layout.luta_adapter, null);

        TextView txtGrupoMuscular = view.findViewById(R.id.txtGrupoMuscular);
        TextView txtNomeExercicio = view.findViewById(R.id.txtNomeExercicios);
        TextView txtHora = view.findViewById(R.id.txtHora);

        Luta luta = lutaList.get(i);

        txtGrupoMuscular.setText(luta.getNome());
        txtNomeExercicio.setText(luta.getDiaSemana());
        txtHora.setText(luta.getHora());

        return view;
    }
}
