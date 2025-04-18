package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Pagina_Inicio extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button51;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicio);

        // Inicializar elementos
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button51 = findViewById(R.id.button51);

        // Abrir el menú lateral al presionar el botón
        button51.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Opciones de la barra lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_pasti) {
                    startActivity(new Intent(Pagina_Inicio.this, Personalizacion_Dispositivo.class));
                } else if (id == R.id.nav_agenda) {
                    startActivity(new Intent(Pagina_Inicio.this, Agenda.class));
                } else if (id == R.id.nav_calendario) {
                    startActivity(new Intent(Pagina_Inicio.this, Calendario.class));
                } else if (id == R.id.nav_medicamentos) {
                    startActivity(new Intent(Pagina_Inicio.this, Medicamentos.class));
                } else if (id == R.id.nav_dispositivo) {
                    startActivity(new Intent(Pagina_Inicio.this, Mi_Dispositivo.class));
                } else if (id == R.id.nav_info) {
                    startActivity(new Intent(Pagina_Inicio.this, Mi_Informacion.class));
                } else if (id == R.id.nav_config) {
                    startActivity(new Intent(Pagina_Inicio.this, Configuracion.class));
                } else if (id == R.id.nav_soporte) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com"));
                    startActivity(intent);
                }

                // Desmarcar ítem y cerrar menú
                item.setChecked(false);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Botones del contenido principal
        findViewById(R.id.button35).setOnClickListener(v ->
                startActivity(new Intent(Pagina_Inicio.this, Mi_Dispositivo.class)));

        findViewById(R.id.button36).setOnClickListener(v ->
                startActivity(new Intent(Pagina_Inicio.this, Medicamentos.class)));

        findViewById(R.id.button37).setOnClickListener(v ->
                startActivity(new Intent(Pagina_Inicio.this, Mi_Informacion.class)));

        findViewById(R.id.button38).setOnClickListener(v ->
                startActivity(new Intent(Pagina_Inicio.this, Calendario.class)));

        findViewById(R.id.button39).setOnClickListener(v ->
                startActivity(new Intent(Pagina_Inicio.this, Agenda.class)));
    }
}
