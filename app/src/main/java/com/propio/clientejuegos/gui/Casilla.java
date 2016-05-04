package com.propio.clientejuegos.gui;

/**
 * Created by JoseAntonio on 20/03/2016.
 */
public class Casilla {
    private int valor;
    private boolean protegida;

    public Casilla(int valor){
        this.valor=valor;
    }

    public void setValor(int numeroAPoner) {
        this.valor =numeroAPoner;
    }

    public int getValor() {
        return valor;
    }

    public boolean isProtegida() {
        return protegida;
    }

    public void proteger() {
        protegida =true;
    }
}
