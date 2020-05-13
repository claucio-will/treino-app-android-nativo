package br.com.claucio.meutreino.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.alertMessage.AlerterCustom;
import br.com.claucio.meutreino.model.ConfiguracaoFirebase;
import br.com.claucio.meutreino.model.Usuario;


public class LoginActivity extends AppCompatActivity {



    private EditText edtSenha;
    private EditText edtEmail;

    private TextView txtLogin;

    private Usuario usuario;
    private FirebaseAuth auth;

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_nova);

        txtLogin = findViewById(R.id.txtLogin);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        String titleErro = getResources().getString(R.string.erro);
        String mensagemInicioLogin = getResources().getString(R.string.mensagem_erro_login_inicio);



        verficarInternet(titleErro, mensagemInicioLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, TelaInicioActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void btnLogin(View view) {
        usuario = new Usuario();
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());

        if (edtEmail.getText().toString().isEmpty() || edtSenha.getText().toString().isEmpty()) {
            AlerterCustom.criarAlerter(this, "Campos vázio", "Preencha os dois campos email e senha, por favor", android.R.color.holo_red_dark);
            return;
        }

        login();
    }

    public void txtCriarConta(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    private void login() {
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, TelaInicioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    clearEdt();

                } else {
                    String erroLoginExcption = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroLoginExcption = "Esse E-mail não existe ou foi desativado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroLoginExcption = "Sua Senha está incorreta";
                    } catch (Exception e) {
                        String titleErro = getResources().getString(R.string.erro);
                        String mensagemLogin = getResources().getString(R.string.mensagem_erro_login);;
                        verficarInternet(titleErro,mensagemLogin);
                    }
                    AlerterCustom.criarAlerter(LoginActivity.this, "Erro", erroLoginExcption, android.R.color.holo_red_dark);
                }
            }
        });
    }

    private void clearEdt() {
        edtEmail.setText(null);
        edtSenha.setText(null);
    }

    private boolean verficarInternet(String title, String mensagem) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_network, null);

            //TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
            TextView txtErroMessage = dialogView.findViewById(R.id.txtMessageErro);

            //txtTitle.setText(title);
            txtErroMessage.setText(mensagem);

            dialog.setView(dialogView);

            dialog.setPositiveButton("Tente Novamente mais tarde", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   finish();
                }
            });

            final AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }

        return false;
    }

    public void imgCirculo(View view) {

        Intent intent = new Intent(this, CadastroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View,String>(txtLogin, "txtLogin");
        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent);
    }
}
