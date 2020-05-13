package br.com.claucio.meutreino.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.model.Treino;
import br.com.claucio.meutreino.model.Usuario;
import br.com.claucio.meutreino.novasActivity.MinhaFichaActivity;
import br.com.claucio.meutreino.novasActivity.NovoTreinoActivity;
import br.com.claucio.meutreino.novasActivity.OutrosTreinosActivity;
import br.com.claucio.meutreino.novasActivity.PesoMedidasActivity;

public class TelaInicioActivity extends AppCompatActivity {

    private Context context;
    private TextView txtEmailUsuario;


    //Firebase
    private FirebaseAuth firebaseAuth;

    private static final String FIREBASE_KEY = "Meus Treinos";
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);

        verficarInternet();

        firebaseAuth = FirebaseAuth.getInstance();
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);

        recuperarUsuario();

    }

    private void verficarInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
        } else {
            exibirAlertDialog();
        }
    }

    private void exibirAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_network, null);

        //TextView txtTile = dialogView.findViewById(R.id.txtTitle);
        TextView txtErroMesage = dialogView.findViewById(R.id.txtMessageErro);

        // txtTile.setText(R.string.aviso);
        txtErroMesage.setText(R.string.erro_network);


        dialog.setView(dialogView);
        dialog.create().show();
    }

    private void recuperarUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getDisplayName();
            String email = user.getEmail();
            txtEmailUsuario.setText(email);

        }

    }


    public void buttonMeuTreino(View view) {
        Intent intent = new Intent(this, MinhaFichaActivity.class);
        startActivity(intent);

    }

    public void buttonCriarTreino(View view) {
        Intent intent = new Intent(this, NovoTreinoActivity.class);
        startActivity(intent);
    }

    public void buttonOutrosTreinos(View view) {
        Intent intent = new Intent(this, OutrosTreinosActivity.class);
        startActivity(intent);
    }


    public void buttonPesoMedidas(View view) {
        Intent intent = new Intent(this, PesoMedidasActivity.class);
        startActivity(intent);
    }


    public void txtSair(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
