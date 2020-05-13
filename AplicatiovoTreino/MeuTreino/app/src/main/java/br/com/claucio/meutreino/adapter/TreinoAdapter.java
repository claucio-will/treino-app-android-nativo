package br.com.claucio.meutreino.adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.model.Treino;


public class TreinoAdapter extends BaseAdapter {

    private Context context;
    private List<Treino> treinosList;
   // private Activity activity;

    public TreinoAdapter(List<Treino> treinos,Context context) {
        this.treinosList = treinos;
        this.context = context;

    }

    @Override
    public int getCount() {
        return treinosList.size();
    }

    @Override
    public Object getItem(int i) {
        return treinosList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = context.getLayoutInflater();
        View view = View.inflate(context,R.layout.treino_adapter, null);

        TextView txtGrupoMuscular = view.findViewById(R.id.txtGrupoMuscular);
        TextView txtNomeExercicio = view.findViewById(R.id.txtNomeExercicios);

        Treino treinos = treinosList.get(position);

        txtGrupoMuscular.setText(treinos.getGrupoMuscular());
        txtNomeExercicio.setText(treinos.getNomeExercicio());

        return view;
    }
}
