package br.com.claucio.meutreino.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.claucio.meutreino.R;
import br.com.claucio.meutreino.alertMessage.AlerterCustom;
import br.com.claucio.meutreino.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private static final int REQUESTE_CODE = 0;

    private RelativeLayout rlayout;
    private Animation animation;

    private Button btnFoto;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtRepetirSenha;
    private Toolbar toolbarCadastro;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nova);
        //------------------------------------------------------------------------ inicio
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        rlayout = findViewById(R.id.rlayout);
//        animation = AnimationUtils.loadAnimation(this, R.anim.animacao_tela_login);
//        rlayout.setAnimation(animation);
        //------------------------------------------------------------------------ fim

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtRepetirSenha = findViewById(R.id.edtRepetirSenha);
//        toolbarCadastro = findViewById(R.id.toolbarCadastro);
//
//
//        toolbarCadastro = findViewById(R.id.toolbarCadastro);
//        //toolbarCadastro.setTitle("Voltar");
//
//        //setSupportActionBar(toolbarCadastro);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnCriar(View view) {
        usuario = new Usuario();
        usuario.setNome(edtNome.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());

        if (edtNome.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() || edtSenha.getText().toString().isEmpty() || edtRepetirSenha.getText().toString().isEmpty()) {
            AlerterCustom.criarAlerter(this, "Campos vázio", "Preencha todos os campos", android.R.color.holo_red_dark);
            return;
        }

        String senha = edtSenha.getText().toString();
        String senhaRepetir = edtRepetirSenha.getText().toString();
        if (senha.equals(senhaRepetir)){
            criarConta();
        }else{
            AlerterCustom.criarAlerter(this,"ERRO", "As duas senhas devem ser iguais", android.R.color.holo_red_dark);

        }

    }

    private void criarConta() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Sucesso ao criar conta", Toast.LENGTH_SHORT).show();
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar();
                    abrirUsuarioLogado();
                } else {
                    String erroException = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroException = "Digite uma senha mais forte, contendo numeros e caracteres";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroException = "O E-mail digitado é invalido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroException = "Esse E-mail já está em uso";
                    } catch (Exception e) {
                        verficarInternet();
                    }
                    AlerterCustom.criarAlerter(CadastroActivity.this, "Erro", erroException, android.R.color.holo_red_dark);
                }


            }
        });

    }

    private void abrirUsuarioLogado() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean verficarInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            dialogErro();
        }

        return false;
    }

    private void dialogErro() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_network, null);

        // TextView txtTitle  =  dialogView.findViewById(R.id.txtTitle);
        TextView txtErroMessage = dialogView.findViewById(R.id.txtMessageErro);

        // txtTitle.setText(R.string.erro);
        txtErroMessage.setText(R.string.mensagem_erro_cadastro);


        dialog.setView(dialogView);

        dialog.setPositiveButton("Tente Novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void txtVoltar(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

