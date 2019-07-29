package com.android.victor.congresoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.victor.congresoapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderExpositores extends RecyclerView.ViewHolder {

    View mView;
    public ViewHolderExpositores(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context ctx, String name, String description, String image){
        TextView Name= mView.findViewById(R.id.rName);
        TextView Detail = mView.findViewById(R.id.rDescription);
        ImageView ImageIv = mView.findViewById(R.id.rImageView);
        //Enviar los datos a la vista....
        Name.setText(name);
        Detail.setText(description);
        Picasso.get().load(image).into(ImageIv);
    }
}
