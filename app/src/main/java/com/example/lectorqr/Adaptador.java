package com.example.lectorqr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectorqr.InfEvento;
import com.example.lectorqr.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.lectorqr.Valores.*;

public class Adaptador extends RecyclerView.Adapter<Adaptador.PersonViewHolder> implements Filterable {

    private Context  mContext;
    private List<InfEvento> evento;
    private List<InfEvento> eventoFull;

    private OnItemClickListener mListener;


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public InfEvento ievento;
        Context contex;
        CardView card;
        TextView name, fecha, id, hora, ubicacion;

        public PersonViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card);
            name = (TextView) itemView.findViewById(R.id.name);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            hora = (TextView) itemView.findViewById(R.id.hora);
            ubicacion = (TextView) itemView.findViewById(R.id.ubicacion);
            id = (TextView) itemView.findViewById(R.id.id);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public Adaptador(Context context, List<InfEvento> lista){
        mContext = context;
        this.evento = lista;
        this.eventoFull = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_evento, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, final int i) {
        final InfEvento currentItem = evento.get(i);
        personViewHolder.ievento = currentItem;

        String name = currentItem.getNombre();
        String fecha = currentItem.getFecha();
        final String id = String.valueOf(currentItem.getId());
        String hora = currentItem.getHora();
        String ubi = currentItem.getUbicacion();


        personViewHolder.name.setText(name);
        personViewHolder.fecha.setText(fecha);
        personViewHolder.id.setText(id);
        personViewHolder.hora.setText(hora);
        personViewHolder.ubicacion.setText(ubi);

        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = i;
                    if (position != RecyclerView.NO_POSITION){
                        // mListener.onItemClick(position);

                      //  Toast.makeText(v.getContext(),id,Toast.LENGTH_SHORT).show();
                        Intent detail = new Intent(v.getContext(), EventoVista.class);
                        Bundle extras = new Bundle();
                        extras.putString(JSON_SONG, gson.toJson(personViewHolder.ievento));
                        extras.putString("id" , id);
                        detail.putExtras(extras);
                        v.getContext().startActivity(detail);//Inicia la actividad


                    }
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return evento.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InfEvento> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eventoFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InfEvento item : eventoFull) {
                    if (item.getNombre().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            evento.clear();
            evento.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}
