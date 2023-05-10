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
/*
    Clase pública Adaptador_pokemons. Extiende de RecyclerView.

    Esta clase forma el adaptador utilizado para mostrar un listado de pokemons recogidos de la base
    de datos dentro de un RecyclerView.
 */
public class Adaptador_pokemons extends RecyclerView.Adapter<Adaptador_pokemons.ViewHolder> {
    private int layout;
    private Context contexto;
    private View.OnClickListener onClickListener;
    private List<?> listado;
    /*
        Constructor con parámetros de la clase Adaptador_pokemons
     */
    public Adaptador_pokemons(int layout, Context contexto, List<?> listado) {
        this.layout = layout;
        this.contexto = contexto;
        this.listado = listado;
    }
    /*
        Método público setOnClickListener. Recibe como parametro un objeto de tipo view.
        Este es utilizado para habilitar el pulsado sobre un elemento del RecyclerView para
        que realice una funcion en concreto.
        No devuelve ningún valor
     */
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
    /*
        Clase pública ViewHolder. Extiendo de RecyclerView.

        Esta representa los objetos que contendrá cada uno de los elementos del RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ImageView tipo1;
        ImageView tipo2;
        ImageView imagen;
        /*
            Constructor con parámetros de la clase ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.pokemon);
            tipo1= itemView.findViewById(R.id.tipo1);
            tipo2= itemView.findViewById(R.id.tipo2);
            imagen= itemView.findViewById(R.id.imagen_elemento);
        }
        public void mostrarElementos(Pokemon p){
            nombre.setText(p.getNombre());
            Glide.with(contexto).load(p.getTipo1()).into(tipo1);
            Glide.with(contexto).load(p.getTipo2()).into(tipo2);
            Glide.with(contexto).load(p.getImagen()).into(imagen);
        }
    }
}