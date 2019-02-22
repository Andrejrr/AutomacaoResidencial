package core.auxiliares;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogConstrutor extends AlertDialog.Builder {
    private String titulo;
    private String menssagem;
    private AlertDialog dialog;
    private String positiveButtonTexto;

    public DialogConstrutor(@NonNull Context context, String titulo, String menssagem, String positiveButtonTexto) {
        super(context);
        setTitulo(titulo);
        setMenssagem(menssagem);
        this.positiveButtonTexto = positiveButtonTexto;
        this.setPositiveButton();
        this.setDialog(this.create());
        getDialog().show();
        menssagemCustomizada();


    }

    public DialogConstrutor(@NonNull Context context, String titulo, String menssagem) {
        super(context);
        setTitulo(titulo);
        setMenssagem(menssagem);
        setDialog(this.create());
        getDialog().show();
        menssagemCustomizada();

    }

    public DialogConstrutor(@NonNull Context context) {
        super(context);
        this.tituloCustomizado(" ");
        this.setMenssagem("   ");
        setDialog(this.create());
    }
    public DialogConstrutor(@NonNull Context context, View view, String titulo, String menssagem) {
        super(context);
        this.setView(view);
        this.tituloCustomizado(titulo);
        this.setMenssagem(menssagem);
        setDialog(this.create());
        getDialog().show();
        menssagemCustomizada();
    }

    public DialogConstrutor(Context context, View view) {
        super(context);
        this.setView(view);
        setDialog(this.create());
        getDialog().show();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        tituloCustomizado(titulo);
        this.titulo = titulo;
    }

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.setMessage(menssagem);
        this.menssagem = menssagem;
    }

    public String getPositiveButtonTexto() {
        return positiveButtonTexto;
    }

    public void setPositiveButtonTexto(String positiveButtonTexto) {
        this.positiveButtonTexto = positiveButtonTexto;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }
    public void fechar(){
        getDialog().dismiss();
    }
    public void tituloCustomizado(String titulo){
        TextView textView = new TextView(getContext());
        textView.setText(titulo);
        textView.setTextSize(1,22);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
       // final Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"font/century-gothic.ttf");
        //textView.setTypeface(typeface);
        setCustomTitle(textView);
    }
    public void menssagemCustomizada(){
        TextView textView = this.dialog.getWindow().findViewById(android.R.id.message);
        textView.setTextSize(1,14);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(Color.DKGRAY);
       // final Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"font/century-gothic.ttf");
        //textView.setTypeface(typeface);
    }

    private void setPositiveButton() {
        super.setPositiveButton(getPositiveButtonTexto(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
    }

    private void setPositiveButton(String textoBotao, DialogInterface.OnClickListener onClickListener) {
        super.setPositiveButton(textoBotao,onClickListener);
    }
    private void setNegativeButton(String textoBotao, DialogInterface.OnClickListener onClickListener) {
        super.setNegativeButton(textoBotao,onClickListener);
    }

}
