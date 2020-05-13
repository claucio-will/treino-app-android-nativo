package br.com.claucio.meutreino.alertMessage;

import android.app.Activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.tapadoo.alerter.Alerter;

import br.com.claucio.meutreino.R;

public class AlerterCustom {

    public static void criarAlerter(Activity activity, String title, String texto, int color) {
        Alerter.create(activity)
                .setTitle(title)
                .setText(texto)
                .setBackgroundColor(color)
                .show();
    }

}
