package com.android.victor.congresoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.victor.congresoapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderTalleres extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolderTalleres(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context ctx, String fecha, String hora, String titulo, String descripcion, String sala){
        TextView vFecha = mView.findViewById(R.id.tFecha);
        TextView vHora= mView.findViewById(R.id.tHora);
        TextView vTitulo = mView.findViewById(R.id.tTitulo);
        TextView vDescripcion = mView.findViewById(R.id.tDescripcion);
        TextView vSala = mView.findViewById(R.id.tSala);
        //Enviar los datos a la vista....
        vFecha.setText(fecha);
        vHora.setText(hora);
        vTitulo.setText(titulo);
        vDescripcion.setText(descripcion);
        vSala.setText(sala);
    }
}
