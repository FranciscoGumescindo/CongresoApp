package com.android.victor.congresoapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.victor.congresoapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderEventos extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolderEventos(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context ctx, String titulo, String fecha, String description, String image){
        TextView Title= mView.findViewById(R.id.eTitulo);
        TextView Fecha = mView.findViewById(R.id.eFecha);
        TextView Detalle = mView.findViewById(R.id.eDescription);
        ImageView Image = mView.findViewById(R.id.eImageView);
        //Enviar los datos a la vista....
        Title.setText(titulo);
        Fecha.setText(fecha);
        Detalle.setText(description);
        Picasso.get().load(image).into(Image);
    }
}
