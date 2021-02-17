package com.example.audiolibros_zeuz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class AdaptadorLibro extends RecyclerView.Adapter<AdaptadorLibro.ViewHolder> {
    private LayoutInflater inflater;
    protected Vector<Libro>vectorLibros;
    private Context contexto;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener=onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener=onLongClickListener;

    }


    public AdaptadorLibro(Context contexto, Vector<Libro>vectorLibros){
        inflater=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vectorLibros=vectorLibros;
        this.contexto=contexto;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= inflater.inflate(R.layout.elemento_selector,null);
        v.setOnClickListener(this.onClickListener);
        v.setOnLongClickListener(this.onLongClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro= vectorLibros.elementAt(position);
        holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.setText(libro.titulo);

    }

    @Override
    public int getItemCount() {
        return vectorLibros.size();
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener=onLongClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView portada;
        public TextView titulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portada=(ImageView)itemView.findViewById(R.id.portada);
            portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            titulo=(TextView)itemView.findViewById(R.id.titulo);

        }
    }
}
