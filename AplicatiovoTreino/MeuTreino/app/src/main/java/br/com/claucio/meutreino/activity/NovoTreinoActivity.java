package br.com.claucio.meutreino.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import br.com.claucio.meutreino.alertMessage.AlerterCustom;
import br.com.claucio.meutreino.model.Treino;

public class NovoTreinoActivity extends AppCompatActivity {

    private Toolbar toolbarCriarTreino;
    //Constantes
    private static final String FIREBASE_KEY = "Meus Treinos";
    private static final String TAG = "firebase_log";


    //Views
    private EditText edtTreino;
    private Spinner spinner;
    private Button btnSalvar;
    private ListView listView;
    private TextView txtNenhumItem;
    //private TextView txtDataTreino;

    //Firebase
    private DatabaseReference database;
    private FirebaseAuth auth;

    private List<Treino> treinosList;
    private TreinoAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_treino);


        toolbarCriarTreino = findViewById(R.id.toolbarCriarTreino);
        toolbarCriarTreino.setTitle("Criar treino");
        toolbarCriarTreino.setSubtitle("Crie ou edite seu treino abaixo.");
        setSupportActionBar(toolbarCriarTreino);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        database = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY);

        edtTreino = findViewById(R.id.edtTreino);
        spinner = findViewById(R.id.sppiner);
        listView = findViewById(R.id.listView);
        btnSalvar = findViewById(R.id.btnSalvar);
        //txtDataTreino = view.findViewById(R.id.txtDataTreino);
        txtNenhumItem = findViewById(R.id.txtNenhumItem);
        txtNenhumItem.setVisibility(View.INVISIBLE);

        treinosList = new ArrayList<>();

        // Evento de click do ListView
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Treino treinos = treinosList.get(i);
                showUpdateDialog(treinos.getTreinoId(), treinos.getNomeExercicio());
                return false;
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTreinos();
                clearEditText();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        database.keepSynced(true);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
                adapter = new TreinoAdapter(treinosList, getApplicationContext());
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

        database.keepSynced(true);
    }


    private void addTreinos() {

        String treino = edtTreino.getText().toString();
        String spinnerTreino = spinner.getSelectedItem().toString();


        if (treino.isEmpty()) {
            edtTreino.setError("Digite o nome do Exercicio");
            AlerterCustom.criarAlerter(this, "ERRO!", "Nome do exercicio necessário ", android.R.color.holo_red_dark);

        } else if (!verficarInternet()) {
            AlerterCustom.criarAlerter(this, "ERRO!", "Não tem coneção com a internet ", android.R.color.holo_red_dark);

        } else {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String usuarioNome = currentUser.getDisplayName();

            String id = database.push().getKey();
            Treino treinos = new Treino(id, spinnerTreino, treino, usuarioNome);
            database.child(id).setValue(treinos);
            String textoAlert = "Treino de " + spinnerTreino + " Adicionado";
            AlerterCustom.criarAlerter(this, "Sucesso", textoAlert, android.R.color.holo_green_dark);
        }
    }

    // Dialog de update
    private void showUpdateDialog(final String treinoId, String treinoNome) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(NovoTreinoActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        dialog.setView(dialogView);

        //final TextView textName = dialogView.findViewById(R.id.txt_name);
        final EditText edtTextName = dialogView.findViewById(R.id.edt_text_nome);
        final Spinner spinnerDialog = dialogView.findViewById(R.id.spinner_dialog);
        final Button btnUpdate = dialogView.findViewById(R.id.btn_update);
        final Button btnDelete = dialogView.findViewById(R.id.btn_delete);


        dialog.setTitle("Atualizar o Treino: " + treinoNome);
        edtTextName.setText(treinoNome);

        final AlertDialog alertDialog = dialog.create();


        alertDialog.show();


        //Botão do dialog que atualiza os dados
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String treino = edtTextName.getText().toString().trim();
                String grupoMuscular = spinnerDialog.getSelectedItem().toString();

                if (TextUtils.isEmpty(treino)) {
                    edtTextName.setError("Digite o treino");
                    return;
                }
                updateTreino(treinoId, grupoMuscular, treino);
                alertDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTreino(treinoId);
                alertDialog.dismiss();


            }
        });

    }

    // Botão do dialog que apaga os dados
    private void deleteTreino(String treinoId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();//------
        String usuarioId = currentUser.getUid();
        final DatabaseReference treinoReference = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY).child(usuarioId).child("Musculação").child(treinoId);

        AlertDialog.Builder dialog = new AlertDialog.Builder(NovoTreinoActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_apagar, null);
        dialog.setView(dialogView);
        final ImageView btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        final Button btnExcluir = dialogView.findViewById(R.id.btnExcluir);

        final AlertDialog alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treinoReference.removeValue();
                Toast.makeText(NovoTreinoActivity.this, "Treino Removido com sucesso!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    private boolean updateTreino(String id, String grupoMuscular, String nomeTreino) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String usuarioId = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY).child(usuarioId).child("Musculação").child(id);
        Treino treinos = new Treino(id, grupoMuscular, nomeTreino);
        databaseReference.setValue(treinos);
        Toast.makeText(NovoTreinoActivity.this, "Treino atualizado", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void clearEditText() {
        edtTreino.setText(null);
    }

    private boolean verficarInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(NovoTreinoActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_network, null);

            //TextView txtTtile = dialogView.findViewById(R.id.txtTitle);
            TextView txtErroMessage = dialogView.findViewById(R.id.txtMessageErro);
            //txtTtile.setText(R.string.erro);
            txtErroMessage.setText(R.string.erro_salvar_dados_internet);

            dialog.setView(dialogView);

            dialog.setPositiveButton("Tente Novamente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            final AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }

        return false;
    }

}
