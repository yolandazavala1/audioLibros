package com.example.audiolibros_zeuz;

import android.content.Context;

import java.util.Vector;

public class AdaptadorLibrosFiltro extends AdaptadorLibro {
private Vector<Libro> vectorSinFiltro;
private Vector<Integer> indiceFiltro;
private String busqueda="";
private String genero="";
private Boolean novedad=false;
private Boolean leido=false;

    public Vector<Libro> getVectorSinFiltro() {
        return vectorSinFiltro;
    }

    public void setVectorSinFiltro(Vector<Libro> vectorSinFiltro) {
        this.vectorSinFiltro = vectorSinFiltro;
    }

    public Vector<Integer> getIndiceFiltro() {
        return indiceFiltro;
    }

    public void setIndiceFiltro(Vector<Integer> indiceFiltro) {
        this.indiceFiltro = indiceFiltro;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {

        this.busqueda = busqueda.toLowerCase();
        recalculaFiltro();
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
        recalculaFiltro();
    }

    public Boolean getNovedad() {
        return novedad;
    }

    public void setNovedad(Boolean novedad) {
        this.novedad = novedad;
        recalculaFiltro();
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
        recalculaFiltro();
    }

    public AdaptadorLibrosFiltro(Context contexto, Vector<Libro> vectorLibros) {
        super(contexto, vectorLibros);
        vectorSinFiltro=vectorLibros;
        recalculaFiltro();
    }
    public void recalculaFiltro(){
        vectorLibros= new Vector<Libro>();
        indiceFiltro= new Vector<Integer>();
        for (int i=0;i<vectorSinFiltro.size();i++){
            Libro libro=vectorSinFiltro.elementAt(i);
            if((libro.titulo.toLowerCase().contains(busqueda)|libro.autor.toLowerCase().contains(busqueda))&&(libro.genero.startsWith(genero))&&(!novedad||(novedad&&libro.novedad))&&(!leido||(leido&&libro.leido))){
                vectorLibros.add(libro);
                indiceFiltro.add(i);

            }

        }

    }
    public Libro getItem(int pos){
        return  vectorSinFiltro.elementAt(indiceFiltro.elementAt(pos));

    }
    public long getItemId(int pos){

        return indiceFiltro.elementAt(pos);

    }
    public void borrar(int pos){
        vectorSinFiltro.remove((int)getItemId(pos));
        recalculaFiltro();

    }
    public void insertar(Libro libro){
        vectorSinFiltro.add(libro);
        recalculaFiltro();

    }

}
