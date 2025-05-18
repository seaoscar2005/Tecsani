package com.example.pastiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onEditar(int position);
        void onEliminar(int position);
    }

    private ArrayList<Medicamento> lista;
    private OnItemClickListener listener;

    public MedicamentoAdapter(ArrayList<Medicamento> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombre, tvDosis, tvFecha;
        public ImageButton btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDosis = itemView.findViewById(R.id.tvDosis);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(Medicamento medicamento, OnItemClickListener listener, int position) {
            tvNombre.setText(medicamento.getNombre());
            tvDosis.setText("Dosis: " + medicamento.getDosis() + " " + medicamento.getUnidad());
            tvFecha.setText("Fecha: " + medicamento.getFecha());

            btnEditar.setOnClickListener(v -> listener.onEditar(position));
            btnEliminar.setOnClickListener(v -> listener.onEliminar(position));
        }
    }

    @Override
    public MedicamentoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicamento_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medicamento medicamento = lista.get(position);
        holder.bind(medicamento, listener, position);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
