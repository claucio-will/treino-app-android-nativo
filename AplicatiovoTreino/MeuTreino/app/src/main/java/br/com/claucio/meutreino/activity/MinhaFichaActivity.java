package br.com.claucio.meutreino.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.adapter.TreinoAdapter;
import br.com.claucio.meutreino.model.Treino;

public class MinhaFichaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Activity context;

    private static final String TAG = "firebase_log";
    private static final String FIREBASE_KEY = "Meus Treinos";
    private ListView listView;
    private List<Treino> treinosList;
    private DatabaseReference database;

    private TreinoAdapter adapter;

    private TextView txtNenhumItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_ficha);

        verficarInternet();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Exercicios");
        toolbar.setSubtitle("Ficha de musculação");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY);
        listView = findViewById(R.id.listViewTreinos);
        txtNenhumItem = findViewById(R.id.txtNenhumItem);
        txtNenhumItem.setVisibility(View.INVISIBLE);
        treinosList = new ArrayList<>();

        // Método de recuperar o dados e colocar no ListView
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();//------
        String usuarioId = currentUser.getUid();
        database = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY).child(usuarioId).child("Musculação");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                treinosList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Treino treinos = ds.getValue(Treino.class);
                    treinosList.add(treinos);
                }

                adapter = new TreinoAdapter(treinosList,getApplicationContext() );
                listView.setAdapter(adapter);

                if (treinosList.isEmpty()) {
                    txtNenhumItem.setVisibility(View.VISIBLE);
                }

                if (!treinosList.isEmpty()) {
                    txtNenhumItem.setVisibility(View.INVISIBLE);
                    return;
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verficarInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            //aletBuilder.setMessage(R.string.erro_network);

        } else {
            AlertDialog.Builder aletBuilder = new AlertDialog.Builder(context);
            // aletBuilder.setTitle("Aviso!");
            aletBuilder.setMessage(R.string.erro_network);
            aletBuilder.show();
        }

    }


}
