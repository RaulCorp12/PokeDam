package com.example.tfg_raul.utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg_raul.R;
import com.example.tfg_raul.clases.Pokemon;

import java.util.List;

public class Adaptador_pokemons extends RecyclerView.Adapter<Adaptador_pokemons.ViewHolder> {

    private int layout;
    private Context contexto;
    private View.OnClickListener onClickListener;
    private List<?> listado;

    public Adaptador_pokemons(int layout, Context contexto, List<?> listado) {
        this.layout = layout;
        this.contexto = contexto;
        this.listado = listado;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener= onClickListener;
    }

    @NonNull
    @Override
    public Adaptador_pokemons.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento= LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_pokedex, parent, false);
        elemento.setOnClickListener(onClickListener);
        return new ViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_pokemons.ViewHolder holder, int position) {
        Pokemon p= (Pokemon) listado.get(position);
        holder.mostrarElementos(p);
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        TextView tipo1;
        TextView tipo2;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.pokemon);
            tipo1= itemView.findViewById(R.id.tipo1);
            tipo2= itemView.findViewById(R.id.tipo2);
            imagen= itemView.findViewById(R.id.imagen_elemento);
        }

        public void mostrarElementos(Pokemon p){
            nombre.setText(p.getNombre());
            tipo1.setText(p.getTipo1());
            tipo2.setText(p.getTipo2());
            Glide.with(contexto).load(p.getImagen()).into(imagen);
        }
    }
}
