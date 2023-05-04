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
import com.example.tfg_raul.clases.Caza;
import java.util.List;

public class Adaptador_cazas extends RecyclerView.Adapter<Adaptador_cazas.ViewHolder> {
    private int layout;
    private Context contexto;
    private View.OnClickListener onClickListener;
    private List<?> listado;
    public Adaptador_cazas(int layout, Context contexto, List<?> listado) {
        this.layout = layout;
        this.contexto = contexto;
        this.listado = listado;
    }
    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener= onClickListener;
    }
    @NonNull
    @Override
    public Adaptador_cazas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento= LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_caza, parent, false);
        elemento.setOnClickListener(onClickListener);
        return new ViewHolder(elemento);
    }
    @Override
    public void onBindViewHolder(@NonNull Adaptador_cazas.ViewHolder holder, int position) {
        Caza c= (Caza) listado.get(position);
        holder.mostrarElementos(c);
    }
    @Override
    public int getItemCount() {
        return listado.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ImageView imagen;
        TextView tiempo;
        TextView intentos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.nombre_pokemon_caza);
            imagen= itemView.findViewById(R.id.imagen_elemento_caza);
            tiempo= itemView.findViewById(R.id.tiempo_caza);
            intentos= itemView.findViewById(R.id.intentos_caza);
        }
        public void mostrarElementos(Caza c){
            nombre.setText(c.getNombre());
            Glide.with(contexto).load(c.getImagen()).into(imagen);
            tiempo.setText(c.getTiempo());
            intentos.setText(c.getIntentos());
        }
    }
}
