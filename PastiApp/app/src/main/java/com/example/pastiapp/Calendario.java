package com.example.pastiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import android.graphics.drawable.GradientDrawable;

public class Calendario extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button11, buttonAddH;
    private CalendarView calendarView;
    private LinearLayout contenedorLista;
    private String fechaSeleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button11 = findViewById(R.id.button11);
        buttonAddH = findViewById(R.id.buttonAddH);
        calendarView = findViewById(R.id.calendarView);
        contenedorLista = findViewById(R.id.contenedorMedicamentos);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        fechaSeleccionada = sdf.format(new Date(calendarView.getDate()));
        mostrarMedicamentosDelDia(fechaSeleccionada);

        button11.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        buttonAddH.setOnClickListener(v -> mostrarFormularioAgendar(null));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month + 1, year);
            mostrarMedicamentosDelDia(fechaSeleccionada);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) startActivity(new Intent(this, Pagina_Inicio.class));
            else if (id == R.id.nav_pasti) startActivity(new Intent(this, Personalizacion_Dispositivo.class));
            else if (id == R.id.nav_agenda) startActivity(new Intent(this, Agenda.class));
            else if (id == R.id.nav_medicamentos) startActivity(new Intent(this, Medicamentos.class));
            else if (id == R.id.nav_dispositivo) startActivity(new Intent(this, Mi_Dispositivo.class));
            else if (id == R.id.nav_info) startActivity(new Intent(this, Mi_Informacion.class));
            else if (id == R.id.nav_config) startActivity(new Intent(this, Configuracion.class));
            else if (id == R.id.nav_soporte) startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com")));

            item.setChecked(false);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void mostrarFormularioAgendar(MedicamentoAgendado medExistente) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_agendar_medicamento, null);

        Spinner spinnerMedicamentos = view.findViewById(R.id.spinnerMedicamentos);
        EditText editFrecuencia = view.findViewById(R.id.editFrecuencia);
        EditText editDuracionDias = view.findViewById(R.id.editDuracionDias);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupFrecuencia);
        Button btnGuardar = view.findViewById(R.id.btnAgregarAgenda);

        // 🔴 NUEVO: Botón para eliminar si se edita
        Button btnEliminar = new Button(this);
        btnEliminar.setText("Eliminar");
        btnEliminar.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        btnEliminar.setTextColor(getResources().getColor(android.R.color.white));

        // Cargar medicamentos disponibles
        SharedPreferences prefs = getSharedPreferences("medicamentos", MODE_PRIVATE);
        String json = prefs.getString("lista", null);
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<ArrayList<Medicamento>>() {}.getType();
        ArrayList<Medicamento> lista = gson.fromJson(json, tipoLista);
        if (lista == null) lista = new ArrayList<>();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (Medicamento m : lista) {
            spinnerAdapter.add(m.getNombre());
        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(spinnerAdapter);

        if (medExistente != null) {
            btnGuardar.setText("Modificar");
            spinnerMedicamentos.setSelection(spinnerAdapter.getPosition(medExistente.getNombre()));
            editFrecuencia.setText(String.valueOf(medExistente.getFrecuencia()));
            editDuracionDias.setText(String.valueOf(medExistente.getDuracion()));
            radioGroup.check(medExistente.isPorHoras() ? R.id.radioHoras : R.id.radioDias);
        }

        btnGuardar.setOnClickListener(v -> {
            int selectedRadio = radioGroup.getCheckedRadioButtonId();
            String frecuenciaTexto = editFrecuencia.getText().toString().trim();
            String duracionTexto = editDuracionDias.getText().toString().trim();

            if (frecuenciaTexto.isEmpty() || duracionTexto.isEmpty() || selectedRadio == -1 || fechaSeleccionada == null) {
                Toast.makeText(this, "Completa todos los campos y selecciona una fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            int frecuencia = Integer.parseInt(frecuenciaTexto);
            int dias = Integer.parseInt(duracionTexto);
            String nombreMed = spinnerMedicamentos.getSelectedItem().toString();
            boolean esPorHoras = (selectedRadio == R.id.radioHoras);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                Date fecha = formato.parse(fechaSeleccionada);
                if (fecha != null) calendar.setTime(fecha);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences agendaPrefs = getSharedPreferences("agenda_medicamentos", MODE_PRIVATE);
            SharedPreferences.Editor editor = agendaPrefs.edit();

            for (int i = 0; i < dias; i++) {
                String fecha = formato.format(calendar.getTime());
                Set<String> set = new HashSet<>(agendaPrefs.getStringSet(fecha, new HashSet<>()));

                if (medExistente != null) {
                    for (String item : set) {
                        MedicamentoAgendado m = new Gson().fromJson(item, MedicamentoAgendado.class);
                        if (m.getNombre().equals(medExistente.getNombre()) &&
                                m.getFrecuencia() == medExistente.getFrecuencia() &&
                                m.getDuracion() == medExistente.getDuracion() &&
                                m.isPorHoras() == medExistente.isPorHoras()) {
                            set.remove(item);
                            break;
                        }
                    }
                }

                MedicamentoAgendado nuevo = new MedicamentoAgendado(nombreMed, frecuencia, dias, esPorHoras);
                set.add(new Gson().toJson(nuevo));
                editor.putStringSet(fecha, set);
                calendar.add(Calendar.DATE, 1);
            }

            editor.apply();
            Toast.makeText(this, "Medicamento " + (medExistente != null ? "modificado" : "agendado") + " correctamente", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            mostrarMedicamentosDelDia(fechaSeleccionada);
        });

        // 🔴 Acción del botón Eliminar
        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("¿Eliminar medicamento?")
                    .setMessage("¿Estás seguro de eliminar este medicamento agendado?")
                    .setPositiveButton("Sí", (dialogo, which) -> {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        try {
                            Date fecha = formato.parse(fechaSeleccionada);
                            if (fecha != null) calendar.setTime(fecha);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        SharedPreferences agendaPrefs = getSharedPreferences("agenda_medicamentos", MODE_PRIVATE);
                        SharedPreferences.Editor editor = agendaPrefs.edit();

                        for (int i = 0; i < medExistente.getDuracion(); i++) {
                            String fecha = formato.format(calendar.getTime());
                            Set<String> set = new HashSet<>(agendaPrefs.getStringSet(fecha, new HashSet<>()));

                            for (String item : set) {
                                MedicamentoAgendado m = new Gson().fromJson(item, MedicamentoAgendado.class);
                                if (m.getNombre().equals(medExistente.getNombre()) &&
                                        m.getFrecuencia() == medExistente.getFrecuencia() &&
                                        m.getDuracion() == medExistente.getDuracion() &&
                                        m.isPorHoras() == medExistente.isPorHoras()) {
                                    set.remove(item);
                                    break;
                                }
                            }

                            editor.putStringSet(fecha, set);
                            calendar.add(Calendar.DATE, 1);
                        }

                        editor.apply();
                        Toast.makeText(this, "Medicamento eliminado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        mostrarMedicamentosDelDia(fechaSeleccionada);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Agregar botón eliminar al final del formulario si es edición
        if (medExistente != null) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutRootAgenda);
            layout.addView(btnEliminar);
        }

        dialog.setContentView(view);
        dialog.show();
    }



    private void mostrarMedicamentosDelDia(String fecha) {
        contenedorLista.removeAllViews();

        SharedPreferences prefs = getSharedPreferences("agenda_medicamentos", MODE_PRIVATE);
        Set<String> datos = prefs.getStringSet(fecha, new HashSet<>());

        if (datos.isEmpty()) {
            TextView sinDatos = new TextView(this);
            sinDatos.setText("No hay medicamentos agendados para esta fecha.");
            sinDatos.setPadding(20, 20, 20, 20);
            contenedorLista.addView(sinDatos);
            return;
        }

        for (String jsonMed : datos) {
            MedicamentoAgendado m = new Gson().fromJson(jsonMed, MedicamentoAgendado.class);

            LinearLayout tarjeta = new LinearLayout(this);
            tarjeta.setOrientation(LinearLayout.VERTICAL);
            tarjeta.setPadding(40, 30, 40, 30);
            tarjeta.setElevation(8);

            GradientDrawable fondo = new GradientDrawable();
            fondo.setColor(0xFFCCE5FF);
            fondo.setCornerRadius(32f);
            tarjeta.setBackground(fondo);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(m.getNombre());
            tvNombre.setTextSize(18);
            tvNombre.setTextColor(getResources().getColor(android.R.color.black));
            tvNombre.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvDosis = new TextView(this);
            tvDosis.setText("Frecuencia: cada " + m.getFrecuencia() + (m.isPorHoras() ? " horas" : " días"));
            tvDosis.setTextSize(16);
            tvDosis.setTextColor(getResources().getColor(android.R.color.black));

            tarjeta.addView(tvNombre);
            tarjeta.addView(tvDosis);

            tarjeta.setOnClickListener(v -> mostrarFormularioAgendar(m));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(40, 30, 40, 30);
            tarjeta.setLayoutParams(params);

            contenedorLista.addView(tarjeta);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
