package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
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

    // NUEVO: RecyclerView y lista
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
        buttonAdd.setOnClickListener(v -> mostrarFormularioMedicamento());

        // NUEVO: Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerMedicamentos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaMedicamentos = cargarMedicamentos();

        adapter = new MedicamentoAdapter(listaMedicamentos, new MedicamentoAdapter.OnItemClickListener() {
            @Override
            public void onEditar(int position) {
                Toast.makeText(Medicamentos.this, "Editar: " + listaMedicamentos.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                // Próximo paso: abrir formulario en modo edición
            }

            @Override
            public void onEliminar(int position) {
                listaMedicamentos.remove(position);
                guardarListaActualizada();
                adapter.notifyItemRemoved(position);
            }
        });

        recyclerView.setAdapter(adapter);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Pagina_Inicio.class));
            } else if (id == R.id.nav_pasti) {
                startActivity(new Intent(Medicamentos.this, Personalizacion_Dispositivo.class));
            } else if (id == R.id.nav_agenda) {
                startActivity(new Intent(Medicamentos.this, Agenda.class));
            } else if (id == R.id.nav_calendario) {
                startActivity(new Intent(Medicamentos.this, Calendario.class));
            } else if (id == R.id.nav_medicamentos) {
                // Ya estás aquí
            } else if (id == R.id.nav_dispositivo) {
                startActivity(new Intent(Medicamentos.this, Mi_Dispositivo.class));
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(Medicamentos.this, Mi_Informacion.class));
            } else if (id == R.id.nav_config) {
                startActivity(new Intent(Medicamentos.this, Configuracion.class));
            } else if (id == R.id.nav_soporte) {
                Intent navegador = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com"));
                startActivity(navegador);
            }

            item.setChecked(false);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void mostrarFormularioMedicamento() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_medicamento, null);

        EditText editNombre = bottomSheetView.findViewById(R.id.editNombre);
        EditText editDosis = bottomSheetView.findViewById(R.id.editDosis);
        EditText editFechaCaducidad = bottomSheetView.findViewById(R.id.editFechaCaducidad);
        Spinner spinnerTipo = bottomSheetView.findViewById(R.id.spinnerTipo);
        Spinner spinnerUnidad = bottomSheetView.findViewById(R.id.spinnerUnidad);
        Button btnGuardar = bottomSheetView.findViewById(R.id.btnGuardar);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,
                R.array.tipos_medicamentos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterUnidad = ArrayAdapter.createFromResource(this,
                R.array.unidades_dosis, android.R.layout.simple_spinner_item);
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidad.setAdapter(adapterUnidad);

        Runnable validarFormulario = () -> {
            boolean completo = !editNombre.getText().toString().trim().isEmpty()
                    && !editDosis.getText().toString().trim().isEmpty()
                    && !editFechaCaducidad.getText().toString().trim().isEmpty()
                    && spinnerTipo.getSelectedItemPosition() >= 0
                    && spinnerUnidad.getSelectedItemPosition() >= 0;

            btnGuardar.setEnabled(completo);
            btnGuardar.setBackgroundTintList(getResources().getColorStateList(
                    completo ? android.R.color.holo_green_dark : android.R.color.darker_gray
            ));
        };

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarFormulario.run();
            }
        };

        editNombre.addTextChangedListener(watcher);
        editDosis.addTextChangedListener(watcher);
        editFechaCaducidad.addTextChangedListener(watcher);

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validarFormulario.run();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validarFormulario.run();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnGuardar.setOnClickListener(v -> {
            if (btnGuardar.isEnabled()) {
                String nombre = editNombre.getText().toString().trim();
                String dosis = editDosis.getText().toString().trim();
                String unidad = spinnerUnidad.getSelectedItem().toString();
                String fecha = editFechaCaducidad.getText().toString().trim();
                String tipo = spinnerTipo.getSelectedItem().toString();

                Medicamento nuevoMedicamento = new Medicamento(nombre, dosis, unidad, fecha, tipo);
                guardarMedicamento(nuevoMedicamento);

                // Actualiza RecyclerView en tiempo real
                listaMedicamentos.add(nuevoMedicamento);
                adapter.notifyItemInserted(listaMedicamentos.size() - 1);

                Toast.makeText(Medicamentos.this, "Guardado: " + nuevoMedicamento.getNombre(), Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(Medicamentos.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
