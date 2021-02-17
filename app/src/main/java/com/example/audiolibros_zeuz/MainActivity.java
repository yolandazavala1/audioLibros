package com.example.audiolibros_zeuz;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.audiolibros_zeuz.fragments.DetalleFragment;
import com.example.audiolibros_zeuz.fragments.PreferenciasFragment;
import com.example.audiolibros_zeuz.fragments.SelectorFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.*;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorLibrosFiltro adaptadorLibrosFiltro;
    private AppBarLayout appBarLayout;
    private  ActionBarDrawerToggle toggle;
    private TabLayout tabs;
    public static int idLibroEnReproduccion=0;
    public static  int getIdLibroEnReproduccionProgreso=0;
    DrawerLayout drawer;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //appBarLayout=(AppBarLayout)findViewById(R.id.appBarLayout);

        int idContenedor=(findViewById(R.id.contenedor_pequeno)!=null)?R.id.contenedor_pequeno:R.id.contenedor_izquierdo;
        SelectorFragment primerFragment= new SelectorFragment();
        getSupportFragmentManager().beginTransaction().add(idContenedor,primerFragment).commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabs= (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Todos"));
        tabs.addTab(tabs.newTab().setText("Nuevos"));
        tabs.addTab(tabs.newTab().setText("Leidos"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        adaptadorLibrosFiltro.setNovedad(false);
                        adaptadorLibrosFiltro.setLeido(false);
                        break;

                    case 1:
                        adaptadorLibrosFiltro.setNovedad(true);
                        adaptadorLibrosFiltro.setLeido(false);
                        break;
                    case 2:
                        adaptadorLibrosFiltro.setNovedad(false);
                        adaptadorLibrosFiltro.setLeido(true);
                        break;

                }
                adaptadorLibrosFiltro.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        adaptadorLibrosFiltro=((Aplicacion)getApplicationContext()).getAdaptador();


        // DRAWER
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        toggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        }
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void mostrarDetalle(int id) {
        DetalleFragment detalleFragment = (DetalleFragment)
                getSupportFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment != null) {
            detalleFragment.ponInfoLibro(id);
        } else {
            DetalleFragment nuevoFragment = new DetalleFragment();
            Bundle args = new Bundle();
            args.putInt(DetalleFragment.ARG_ID_LIBRO, id);
            nuevoFragment.setArguments(args);
            FragmentTransaction transaccion = getSupportFragmentManager()
                    .beginTransaction();
            transaccion.replace(R.id.contenedor_pequeno, nuevoFragment);
            transaccion.addToBackStack(null);
            transaccion.commit();
        }
        SharedPreferences pref=getSharedPreferences("com.example.audiolibros_zeuz",MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.putInt("ultimo",id);
        editor.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_preferencias) {
            //Toast.makeText(this,"Preferencias",Toast.LENGTH_SHORT).show();
            //Intent i= new Intent(this,PreferenciasActivity.class);
            //startActivity(i);
            abrePreferencias();
            return true;
        }else if (id == R.id.menu_acerca){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("MEnsaje de Acerca de");
            builder.setPositiveButton("ok",null);
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void irUltimoVisitado(){
        SharedPreferences pref= getSharedPreferences("com.example.audiolibros_zeuz",MODE_PRIVATE);
        int id=pref.getInt("ultimo",-1);
        if(id>=0){
            mostrarDetalle(id);
        }else{
            Toast.makeText(this,"Sin ultima vista",Toast.LENGTH_LONG);
        }
    }
    public void abrePreferencias(){

        int idContenedor=(findViewById(R.id.contenedor_pequeno)!=null)?R.id.contenedor_pequeno:R.id.contenedor_izquierdo;

        PreferenciasFragment preferenciasFragment= new PreferenciasFragment();
        getSupportFragmentManager().beginTransaction().replace(idContenedor,preferenciasFragment).addToBackStack(null).commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.nav_todos){
            adaptadorLibrosFiltro.setGenero("");
            adaptadorLibrosFiltro.notifyDataSetChanged();

        }else if (id==R.id.nav_epico){

            adaptadorLibrosFiltro.setGenero(Libro.G_EPICO);
            adaptadorLibrosFiltro.notifyDataSetChanged();

        }else if (id==R.id.nav_XIX){

            adaptadorLibrosFiltro.setGenero(Libro.G_SUSPENSE);
            adaptadorLibrosFiltro.notifyDataSetChanged();

        }
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        }else {

            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void mostrarElementos(boolean mostrar){

        appBarLayout.setExpanded(mostrar);
        toggle.setDrawerIndicatorEnabled(mostrar);
        if(mostrar){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            tabs.setVisibility(View.VISIBLE);

        }else{
            tabs.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(getApplicationContext(),MediaPlayerService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(),MediaPlayerService.class);
        stopService(intent);
    }
}
