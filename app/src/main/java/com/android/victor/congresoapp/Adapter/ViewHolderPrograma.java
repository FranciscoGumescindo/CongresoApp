package com.android.victor.congresoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.victor.congresoapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ViewHolderPrograma extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolderPrograma(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context ctx, String fecha, String horario, String descripcion){
        TextView pFechaTv= mView.findViewById(R.id.pFecha);
        TextView pHorarioTv = mView.findViewById(R.id.pHorario);
        TextView pDescripcionTv = mView.findViewById(R.id.pDescripcion);
        //Enviar los datos a la vista....
        pFechaTv.setText(fecha);
        pHorarioTv.setText(horario);
        pDescripcionTv.setText(descripcion);
    }
}
