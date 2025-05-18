package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Medicamentos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button12;
    private Button buttonAdd;

    private RecyclerView recyclerView;
    private MedicamentoAdapter adapter;
    private ArrayList<Medicamento> listaMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button12 = findViewById(R.id.button12);
        buttonAdd = findViewById(R.id.buttonAdd);

        button12.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        buttonAdd.setOnClickListener(v -> mostrarFormularioAgregar());

        recyclerView = findViewById(R.id.recyclerMedicamentos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaMedicamentos = cargarMedicamentos();

        adapter = new MedicamentoAdapter(listaMedicamentos, position -> mostrarFormularioEditar(position));
        recyclerView.setAdapter(adapter);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) startActivity(new Intent(this, Pagina_Inicio.class));
            else if (id == R.id.nav_pasti) startActivity(new Intent(this, Personalizacion_Dispositivo.class));
            else if (id == R.id.nav_agenda) startActivity(new Intent(this, Agenda.class));
            else if (id == R.id.nav_calendario) startActivity(new Intent(this, Calendario.class));
            else if (id == R.id.nav_dispositivo) startActivity(new Intent(this, Mi_Dispositivo.class));
            else if (id == R.id.nav_info) startActivity(new Intent(this, Mi_Informacion.class));
            else if (id == R.id.nav_config) startActivity(new Intent(this, Configuracion.class));
            else if (id == R.id.nav_soporte) startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com")));

            item.setChecked(false);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void mostrarFormularioAgregar() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_medicamento, null);

        EditText editNombre = view.findViewById(R.id.editNombre);
        EditText editDosis = view.findViewById(R.id.editDosis);
        EditText editFecha = view.findViewById(R.id.editFechaCaducidad);
        Spinner spinnerTipo = view.findViewById(R.id.spinnerTipo);
        Spinner spinnerUnidad = view.findViewById(R.id.spinnerUnidad);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.tipos_medicamentos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterUnidad = ArrayAdapter.createFromResource(this, R.array.unidades_dosis, android.R.layout.simple_spinner_item);
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidad.setAdapter(adapterUnidad);

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String dosis = editDosis.getText().toString().trim();
            String fecha = editFecha.getText().toString().trim();
            String tipo = spinnerTipo.getSelectedItem().toString();
            String unidad = spinnerUnidad.getSelectedItem().toString();

            if (!nombre.isEmpty() && !dosis.isEmpty() && !fecha.isEmpty()) {
                Medicamento nuevo = new Medicamento(nombre, dosis, unidad, fecha, tipo);
                guardarMedicamento(nuevo);
                listaMedicamentos.add(nuevo);
                adapter.notifyItemInserted(listaMedicamentos.size() - 1);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void mostrarFormularioEditar(int posicion) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_editar_medicamento, null);

        EditText editNombre = view.findViewById(R.id.editNombre);
        EditText editDosis = view.findViewById(R.id.editDosis);
        EditText editFecha = view.findViewById(R.id.editFechaCaducidad);
        Spinner spinnerTipo = view.findViewById(R.id.spinnerTipo);
        Spinner spinnerUnidad = view.findViewById(R.id.spinnerUnidad);
        Button btnModificar = view.findViewById(R.id.btnModificar);
        Button btnEliminar = view.findViewById(R.id.btnEliminar);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.tipos_medicamentos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterUnidad = ArrayAdapter.createFromResource(this, R.array.unidades_dosis, android.R.layout.simple_spinner_item);
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidad.setAdapter(adapterUnidad);

        Medicamento m = listaMedicamentos.get(posicion);
        editNombre.setText(m.getNombre());
        editDosis.setText(m.getDosis());
        editFecha.setText(m.getFecha());
        spinnerTipo.setSelection(adapterTipo.getPosition(m.getTipo()));
        spinnerUnidad.setSelection(adapterUnidad.getPosition(m.getUnidad()));

        btnModificar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String dosis = editDosis.getText().toString().trim();
            String fecha = editFecha.getText().toString().trim();
            String tipo = spinnerTipo.getSelectedItem().toString();
            String unidad = spinnerUnidad.getSelectedItem().toString();

            if (!nombre.isEmpty() && !dosis.isEmpty() && !fecha.isEmpty()) {
                Medicamento actualizado = new Medicamento(nombre, dosis, unidad, fecha, tipo);
                listaMedicamentos.set(posicion, actualizado);
                guardarListaActualizada();
                adapter.notifyItemChanged(posicion);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("¿Eliminar medicamento?")
                    .setMessage("Esta acción no se puede deshacer.")
                    .setPositiveButton("Sí", (d, w) -> {
                        listaMedicamentos.remove(posicion);
                        guardarListaActualizada();
                        adapter.notifyItemRemoved(posicion);
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void guardarMedicamento(Medicamento nuevoMedicamento) {
        SharedPreferences prefs = getSharedPreferences("medicamentos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonGuardado = prefs.getString("lista", null);

        ArrayList<Medicamento> lista;
        if (jsonGuardado != null) {
            Type tipoLista = new TypeToken<ArrayList<Medicamento>>() {}.getType();
            lista = gson.fromJson(jsonGuardado, tipoLista);
        } else {
            lista = new ArrayList<>();
        }

        lista.add(nuevoMedicamento);
        String jsonNuevo = gson.toJson(lista);
        editor.putString("lista", jsonNuevo);
        editor.apply();
    }

    private ArrayList<Medicamento> cargarMedicamentos() {
        SharedPreferences prefs = getSharedPreferences("medicamentos", MODE_PRIVATE);
        String json = prefs.getString("lista", null);
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<ArrayList<Medicamento>>() {}.getType();
        ArrayList<Medicamento> lista = gson.fromJson(json, tipoLista);
        return (lista != null) ? lista : new ArrayList<>();
    }

    private void guardarListaActualizada() {
        SharedPreferences prefs = getSharedPreferences("medicamentos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaMedicamentos);
        editor.putString("lista", json);
        editor.apply();
    }
}
