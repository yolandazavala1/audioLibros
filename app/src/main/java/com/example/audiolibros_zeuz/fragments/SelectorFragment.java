package com.example.audiolibros_zeuz.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiolibros_zeuz.AdaptadorLibro;
import com.example.audiolibros_zeuz.AdaptadorLibrosFiltro;
import com.example.audiolibros_zeuz.Aplicacion;
import com.example.audiolibros_zeuz.Libro;
import com.example.audiolibros_zeuz.MainActivity;
import com.example.audiolibros_zeuz.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Vector;

public class SelectorFragment extends Fragment {
    private Activity actividad;
    private RecyclerView recyclerView;
    private AdaptadorLibrosFiltro adaptadorLibro;
    private Vector<Libro> vectorLibros;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad = (Activity) context;
            Aplicacion app = (Aplicacion) actividad.getApplication();
            adaptadorLibro = app.getAdaptador();
            vectorLibros = app.getVectorLibros();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View vista = inflater.inflate(R.layout.fragment_selector, container, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(actividad, 2));
        recyclerView.setAdapter(adaptadorLibro);
        adaptadorLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) actividad).mostrarDetalle((int)adaptadorLibro.getItemId(recyclerView.getChildAdapterPosition(v)));

            }
        });

        adaptadorLibro.setOnItemLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                final int id = recyclerView.getChildAdapterPosition(v);
                AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
                CharSequence[] opciones = {"Compartir", "Borrar", "Insertar"};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // opcion compartir.
                            case 0:
                                Libro libro = vectorLibros.elementAt(id);
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                                i.putExtra(Intent.EXTRA_TEXT, libro.autor);
                                startActivity(Intent.createChooser(i, "Compartir"));
                                break;
                            case 1:// borrar
                                Snackbar.make(v, "¿Estas Seguro de Eliminar?", Snackbar.LENGTH_LONG).setAction("SI", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        adaptadorLibro.borrar(id);
                                        adaptadorLibro.notifyDataSetChanged();
                                    }
                                }).show();
                                break;
                            case 2: //insertar
                                Snackbar.make(v, "Cancion Insertada", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int pos= recyclerView.getChildAdapterPosition(v);
                                        adaptadorLibro.insertar((Libro)adaptadorLibro.getItem(pos));

                                    }
                                }).show();
                                //cancionVector.add(cancionVector.elementAt(id));
                                //adaptadorCancion.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                menu.create().show();


                return true;
            }
        });


        return vista;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_selector,menu);



        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.menu_ultimo){
            ((MainActivity)actividad).irUltimoVisitado();
            return  true;

        }else if(id==R.id.menu_buscar){
            return  true;

        }
        return super.onOptionsItemSelected(item);
    }
}