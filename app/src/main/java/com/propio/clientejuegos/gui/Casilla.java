package com.propio.clientejuegos.gui;

/**
 * Created by JoseAntonio on 20/03/2016.
 */
public class Casilla {
    private int Valor;
    private boolean Protegida;

    public void setValor(int numeroAPoner) {
        this.Valor=numeroAPoner;
    }

    public int getValor() {
        return Valor;
    }

    public boolean isProtegida() {
        return Protegida;
    }

    public void protejer() {
        Protegida=true;
    }
}
