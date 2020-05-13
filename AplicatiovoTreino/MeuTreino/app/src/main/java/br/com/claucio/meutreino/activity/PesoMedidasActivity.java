package br.com.claucio.meutreino.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.claucio.meutreino.R;

public class PesoMedidasActivity extends AppCompatActivity {

    private Toolbar toolbarMedidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peso_medidas);

        toolbarMedidas = findViewById(R.id.toolbarMedidas);
        toolbarMedidas = findViewById(R.id.toolbarMedidas);
        toolbarMedidas.setTitle("Peso medidas");
        toolbarMedidas.setSubtitle("Meu peso e minhas medidas");

        setSupportActionBar(toolbarMedidas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dados:
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
