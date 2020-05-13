package br.com.claucio.meutreino.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.adapter.LutaAdapter;
import br.com.claucio.meutreino.alertMessage.AlerterCustom;
import br.com.claucio.meutreino.model.Luta;

public class OutrosTreinosActivity extends AppCompatActivity {

    private static final String FIREBASE_KEY = "Meus Treinos";

    private EditText edtNomeLuta;
    private Spinner spnDias;
    private TextView txtHora;
    private ListView listViewLuta;
    private Button btnSalvarLuta;
    private TextView txtNenhumItem;

    private TextView txtResult;

    private List<Luta> lutaList;
    private LutaAdapter adapter;

    private DatabaseReference database;
    private FirebaseAuth auth;

    private int hora;
    private int minuto;

    private Context context;
    private Activity activity;

    private Toolbar toolbarOutrosTreinos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outros_treinos);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = currentUser.getUid();
        database = FirebaseDatabase.getInstance().getReference().child(FIREBASE_KEY).child(id);

        toolbarOutrosTreinos = findViewById(R.id.toolbarOutrosTreinos);
        toolbarOutrosTreinos.setTitle("Outros treinos");
        toolbarOutrosTreinos.setSubtitle("Ex: Lutas e treinos funcionais");

        setSupportActionBar(toolbarOutrosTreinos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNomeLuta  = findViewById(R.id.edtNomeLuta);
        spnDias      = findViewById(R.id.spnDias);
        listViewLuta = findViewById(R.id.listViewLuta);
        txtHora      = findViewById(R.id.txtHours);
        btnSalvarLuta = findViewById(R.id.btnSalvarLuta);
        txtNenhumItem = findViewById(R.id.txtNenhumItem);
        txtNenhumItem.setVisibility(View.INVISIBLE);


        lutaList = new ArrayList<>();

        // Evento de click do ListView
        listViewLuta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Luta luta = lutaList.get(i);
                deleteTreino(luta.getId());
                return false;
            }
        });

        btnSalvarLuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarTreinoLuta();
                edtNomeLuta.setText(null);
            }
        });

        // Definir hora
        txtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                hora = calendar.get(Calendar.HOUR);
                minuto = calendar.get(Calendar.MINUTE);


                TimePickerDialog pickerDialog = new TimePickerDialog(OutrosTreinosActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        txtHora.setText("Horário: " + i + ":" + i1);
                    }
                }, hora, minuto, false);
                pickerDialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        database.keepSynced(true);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String usuarioId = currentUser.getUid();
        database = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY).child(usuarioId).child("Outros Treinos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lutaList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Luta luta = ds.getValue(Luta.class);
                    lutaList.add(luta);
                }

                adapter = new LutaAdapter(lutaList, getApplicationContext());
                listViewLuta.setAdapter(adapter);

                if (lutaList.isEmpty()) {
                    txtNenhumItem.setVisibility(View.VISIBLE);
                }

                if (!lutaList.isEmpty()) {
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


    private void salvarTreinoLuta() {
        String nomeLuta = edtNomeLuta.getText().toString();
        String spinner = spnDias.getSelectedItem().toString();
        String hora = txtHora.getText().toString();

        if (nomeLuta.isEmpty()) {
            AlerterCustom.criarAlerter(this, "Opss...", "Digite o Treino ou Luta", android.R.color.holo_red_dark);
        } else if (verficarInternet()) {
            AlerterCustom.criarAlerter(this, "ERRO!", "Não tem coneção com a internet ", android.R.color.holo_red_dark);
        } else {
            String id = database.push().getKey();
            Luta luta = new Luta(id, nomeLuta, spinner, hora);
            database.child(id).setValue(luta);

            String textoAlert = "Treino de " + nomeLuta + " Adicionado";
            AlerterCustom.criarAlerter(this, "Sucesso", textoAlert, android.R.color.holo_green_dark);
        }
    }

    // Botão do dialog que apaga os dados
    private void deleteTreino(String treinoId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String usuarioId = currentUser.getUid();
        final DatabaseReference treinoReference = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY).child(usuarioId).child("Outros Treinos").child(treinoId);

        AlertDialog.Builder dialog = new AlertDialog.Builder(OutrosTreinosActivity.this);
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
                if (verficarInternet()) {
                    AlerterCustom.criarAlerter(OutrosTreinosActivity.this, "ERRO!", "Não tem coneção com a internet ", android.R.color.holo_red_dark);
                    alertDialog.dismiss();
                } else {
                    treinoReference.removeValue();
                    Toast.makeText(OutrosTreinosActivity.this, "Treino Removido com sucesso!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean verficarInternet() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return false;
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(OutrosTreinosActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_network, null);

            //TextView txtTtile = dialogView.findViewById(R.id.txtTitle);
            TextView txtErroMessage = dialogView.findViewById(R.id.txtMessageErro);
            //txtTtile.setText(R.string.erro);
            txtErroMessage.setText(R.string.erro_excluir_dados_internet);

            dialog.setView(dialogView);

            dialog.setPositiveButton("Tente Novamente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            final AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }

        return true;
    }

}
