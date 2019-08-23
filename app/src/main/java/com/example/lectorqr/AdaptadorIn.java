package com.example.lectorqr;

import android.content.Context;
import android.content.Intent;
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

public class AdaptadorIn extends RecyclerView.Adapter<AdaptadorIn.PersonViewHolder> implements Filterable {

    private Context  mContext;
    private List<InfInvitados> evento;
    private List<InfInvitados> eventoFull;

    private OnItemClickListener mListener;


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public InfInvitados ievento;
        Context contex;
        CardView card;
        TextView name, estatus, id, correo, ubicacion;

        public PersonViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cardInv);
            name = (TextView) itemView.findViewById(R.id.name_invitado);
            correo = (TextView) itemView.findViewById(R.id.correo);
            estatus = (TextView) itemView.findViewById(R.id.estatus);
            id = (TextView) itemView.findViewById(R.id.idIn);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorIn(Context context, List<InfInvitados> lista){
        mContext = context;
        this.evento = lista;
        this.eventoFull = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_invitados, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder personViewHolder, final int i) {
        final InfInvitados currentItem = evento.get(i);
        personViewHolder.ievento = currentItem;

        String name = currentItem.getNombre();
        String correo = currentItem.getcorreo();
        String id = String.valueOf(currentItem.getId());
        String estatus = currentItem.getestatus();



        personViewHolder.name.setText(name);
        personViewHolder.estatus.setText(estatus);
        personViewHolder.id.setText(id);
        personViewHolder.correo.setText(correo);

        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = i;
                    if (position != RecyclerView.NO_POSITION){
                        // mListener.onItemClick(position);
                    /*
                        Toast.makeText(v.getContext(),"click",Toast.LENGTH_SHORT).show();

                        Intent detail = new Intent(v.getContext(), EventoVista.class);
                        detail.putExtra(JSON_SONG, gson.toJson(personViewHolder.ievento));
                        v.getContext().startActivity(detail);//Inicia la actividad
*/

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
            List<InfInvitados> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eventoFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InfInvitados item : eventoFull) {
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
