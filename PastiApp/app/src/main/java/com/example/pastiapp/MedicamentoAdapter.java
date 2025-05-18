package com.example.pastiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final ArrayList<Medicamento> lista;
    private final OnItemClickListener listener;

    public MedicamentoAdapter(ArrayList<Medicamento> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombre, tvDosis, tvFecha;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDosis = itemView.findViewById(R.id.tvDosis);
            tvFecha = itemView.findViewById(R.id.tvFecha);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bind(Medicamento medicamento) {
            tvNombre.setText(medicamento.getNombre());
            tvDosis.setText("Dosis: " + medicamento.getDosis() + " " + medicamento.getUnidad());
            tvFecha.setText("Fecha de vencimiento: " + medicamento.getFecha());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicamento_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicamento medicamento = lista.get(position);
        holder.bind(medicamento);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
