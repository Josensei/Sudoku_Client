package com.propio.clientejuegos.gui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.maco.clientejuegos.domain.Store;
import com.maco.clientejuegos.gui.PartidaActivity;
import com.maco.clientejuegos.http.NetTask;
import com.propio.clientejuegos.jsonMessages.SudokuMovementMessage;
import com.propio.clientejuegos.jsonMessages.SudokuWinnerMessage;

/**
 * Created by Maco on 24/2/16.
 */
public class PartidaView extends View {
    private Paint bordeGrueso, bordeFino;
    private float topTeclado;
    private float leftTeclado=0.05f*ScreenParameters.screenWidth;
    private float rightTeclado =0.95f*ScreenParameters.screenWidth;
    private float anchoTecla=(rightTeclado -leftTeclado)/10;
    private int[][] board;
    private boolean partidaFinalizada;
    private String mensaje;


    /** Cosas del jugador **/
    private Casilla[][] tableroPropio;
    private Paint numerosFijosPropios, numerosQuePone, fondoCeldaSeleccionada;
    private int mPropia;
    private float textSizePropio=60;
    private float leftPropio, rightPropio, topPropio, bottomPropio, anchoPropio, altoPropio;
    private int colCeldaACambiar=-1, rowCeldaACambiar=-1;
    private int numeroAPoner = -1;


    /** Cosas del contrincante **/
    private Casilla[][] tableroAjeno;
    private Paint numerosFijosAjenos;
    private int mAjena;
    private float textSizeAjeno=30;
    private float leftAjeno, rightAjeno, topAjeno, bottomAjeno;


    public PartidaView(Context context) {
        super(context);

        bordeGrueso=new Paint();
        bordeGrueso.setColor(Color.BLACK);
        bordeGrueso.setStrokeWidth(12);
        fondoCeldaSeleccionada=new Paint();
        fondoCeldaSeleccionada.setColor(Color.GRAY);
        fondoCeldaSeleccionada.setStyle(Paint.Style.FILL);

        bordeFino=new Paint();
        bordeFino.setColor(Color.BLACK);
        bordeFino.setStrokeWidth(6);

        numerosFijosPropios =new Paint();
        numerosFijosPropios.setColor(Color.BLACK);
        numerosFijosPropios.setTextSize(textSizePropio);

        numerosQuePone =new Paint();
        numerosQuePone.setColor(Color.BLUE);
        numerosQuePone.setTextSize(textSizePropio);

        numerosFijosAjenos =new Paint();
        numerosFijosAjenos.setColor(Color.BLACK);
        numerosFijosAjenos.setTextSize(textSizeAjeno);

        mPropia=(int) numerosFijosPropios.measureText("M");
        mAjena=(int) numerosFijosAjenos.measureText("M");

        leftPropio=0.1f*ScreenParameters.screenWidth;
        rightPropio=0.9f*ScreenParameters.screenWidth;
        topPropio=0.05f*ScreenParameters.screenHeight;
        bottomPropio=0.5f*ScreenParameters.screenHeight;
        anchoPropio=(rightPropio-leftPropio)/9;
        altoPropio=(bottomPropio-topPropio)/9;
        tableroPropio=new Casilla[9][9];

        leftAjeno=0.15f*ScreenParameters.screenWidth;
        rightAjeno=0.85f*ScreenParameters.screenWidth;
        topAjeno=0.60f*ScreenParameters.screenHeight;
        bottomAjeno=0.95f*ScreenParameters.screenHeight;
        tableroAjeno=new Casilla[9][9];

        topTeclado=bottomPropio+(bottomPropio-topPropio)/18;
        leftTeclado=0.05f*ScreenParameters.screenWidth;
        rightTeclado =0.95f*ScreenParameters.screenWidth;
        board=new int[9][9];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!partidaFinalizada) {
            dibujarTableroPropio(canvas);
            dibujarTableroAjeno(canvas);

            marcarCeldaSeleccionada(canvas);

            escribirNumerosFijosPropios(canvas);
            escribirNumerosFijosAjenos(canvas);

            dibujarTeclado(canvas);
        } else {
            canvas.drawText(mensaje, 10, ScreenParameters.screenHeight/2, numerosFijosPropios);
        }
    }

    private void marcarCeldaSeleccionada(Canvas canvas) {
        if (colCeldaACambiar != -1) {
            float[] bordes= getBordersDeCasilla(colCeldaACambiar, rowCeldaACambiar);
            canvas.drawRect(bordes[0], bordes[1], bordes[2], bordes[3], fondoCeldaSeleccionada);
        }
    }

    private void marcarTeclaSeleccionada(Canvas canvas) {
        if (numeroAPoner != -1) {
            float[] bordes= getBordersDeTecla();
            canvas.drawRect(bordes[0], bordes[1], bordes[2], bordes[3], fondoCeldaSeleccionada);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (partidaFinalizada) {
            ((PartidaActivity) this.getContext()).finish();
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y=event.getY();
            if (x>=leftPropio && x<=rightPropio && y>=topPropio && y<=bottomPropio) {
                int col=(int) ((x-leftPropio)/anchoPropio);
                int fila=(int) ((y-topPropio)/altoPropio);
                if (this.tableroPropio[col][fila].isProtegida()) {
                    this.colCeldaACambiar=-1;
                    this.rowCeldaACambiar=-1;
                } else {
                    this.colCeldaACambiar = col;
                    this.rowCeldaACambiar = fila;
                    numeroAPoner = -1;
                }
                invalidate();
            } else if (x>=leftTeclado && x<=rightTeclado && y>=topTeclado && y<=topTeclado+anchoTecla) {
                this.numeroAPoner=(int) ((x-leftTeclado)/anchoTecla);
                if (this.colCeldaACambiar!=-1 && !tableroPropio[colCeldaACambiar][rowCeldaACambiar].isProtegida()) {
                    tableroPropio[colCeldaACambiar][rowCeldaACambiar].setValor(this.numeroAPoner);
                    SudokuMovementMessage movement=new SudokuMovementMessage(rowCeldaACambiar, colCeldaACambiar, numeroAPoner, Store.get().getUser().getIdUser(), Store.get().getIdMatch(),Store.get().getIdGame());
                    NetTask nt=new NetTask("SendMovement.action", movement);
                    nt.execute();
                }
                this.numeroAPoner = -1;
                invalidate();
            }
        }
        return true;
    }

    private float[] getBordersDeCasilla(int col, int fila) {
        float left=leftPropio+anchoPropio*col;
        left+= col==0 ? bordeGrueso.getStrokeWidth()/2 : bordeFino.getStrokeWidth()/2;

        float top=topPropio+altoPropio*fila;
        top+= fila==0 ? bordeGrueso.getStrokeWidth()/2 : bordeFino.getStrokeWidth()/2;

        float right=leftPropio+anchoPropio*(col+1);
        right-= col==8 ? bordeGrueso.getStrokeWidth()/2 : bordeFino.getStrokeWidth()/2;

        float bottom=topPropio+altoPropio*(fila+1);
        bottom-= fila==8 ? bordeGrueso.getStrokeWidth()/2 : bordeFino.getStrokeWidth()/2;

        float[] result={left, top, right, bottom};
        return  result;
    }

    private float[] getBordersDeTecla() {
        if (numeroAPoner!=-1) {
            float left = leftTeclado + anchoTecla * numeroAPoner;
            left += numeroAPoner == 0 ? bordeGrueso.getStrokeWidth() / 2 : bordeFino.getStrokeWidth() / 2;

            float top = topTeclado + bordeGrueso.getStrokeWidth();

            float right = leftTeclado + anchoTecla* (numeroAPoner + 1)- bordeGrueso.getStrokeWidth()/2;

            float bottom = topTeclado + anchoTecla * (numeroAPoner + 1) - bordeGrueso.getStrokeWidth()/2;

            float[] result = {left, top, right, bottom};
            return result;
        }
        return null;
    }

    private void dibujarTeclado(Canvas canvas) {
        char[] teclas=new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        bordeFino.setStyle(Paint.Style.STROKE);
        float col=leftTeclado;
        for (int i=0; i<teclas.length; i++) {
            canvas.drawRect(col, topTeclado, col+anchoTecla, topTeclado+anchoTecla, bordeFino);
            canvas.drawText(""+teclas[i], col+anchoTecla/2-mPropia/2, topTeclado+mPropia, numerosFijosPropios);
            col=col+anchoTecla;
        }
    }

    private void escribirNumerosFijosPropios(Canvas canvas) {
        float row;
        float col;
        for (int i=0; i<9; i++) {
            col=leftPropio+anchoPropio*(i+1)-(anchoPropio/2);
            for (int j=0; j<9; j++) {
                row=topPropio+altoPropio*(j+1)-(altoPropio/2)+mPropia/2;
                Casilla casilla = tableroPropio[i][j];
                int valor=casilla.getValor();
                if (valor!=0) {
                    if (casilla.isProtegida())
                        canvas.drawText("" + valor, col, row, numerosFijosPropios);
                    else
                        canvas.drawText("" + valor, col, row, numerosQuePone);
                }
            }
        }
    }

    private void dibujarTableroPropio(Canvas canvas) {
        float ancho=(rightPropio-leftPropio)/3;
        float alto=(bottomPropio-topPropio)/3;

        canvas.drawLine(leftPropio, topPropio, rightPropio, topPropio, bordeGrueso);
        canvas.drawLine(rightPropio, topPropio, rightPropio, bottomPropio, bordeGrueso);
        canvas.drawLine(leftPropio, topPropio, leftPropio, bottomPropio, bordeGrueso);
        canvas.drawLine(leftPropio, bottomPropio, rightPropio, bottomPropio, bordeGrueso);

        canvas.drawLine(leftPropio+ancho, topPropio, leftPropio+ancho, bottomPropio, bordeGrueso);
        canvas.drawLine(leftPropio+2*ancho, topPropio, leftPropio+2*ancho, bottomPropio, bordeGrueso);
        canvas.drawLine(leftPropio, topPropio+alto, rightPropio, topPropio+alto, bordeGrueso);
        canvas.drawLine(leftPropio, topPropio+2*alto, rightPropio, topPropio+2*alto, bordeGrueso);

        ancho=ancho/3;
        for (int i=1; i<=8; i++) {
            canvas.drawLine(leftPropio+ancho*i, topPropio, leftPropio+ancho*i, bottomPropio, bordeFino);
        }
        alto=alto/3;
        for (int i=1; i<=8; i++) {
            canvas.drawLine(leftPropio, topPropio+alto*i, rightPropio, topPropio+alto*i, bordeFino);
        }
    }

    private void escribirNumerosFijosAjenos(Canvas canvas) {
        float ancho=(rightAjeno-leftAjeno)/9;
        float alto=(bottomAjeno-topAjeno)/9;

        float row;
        float col;
        for (int i=0; i<9; i++) {
            col=leftAjeno+ancho*(i+1)-(ancho/2);
            for (int j=0; j<9; j++) {
                row=topAjeno+alto*(j+1)-(alto/2)+mAjena/2;
                Casilla casilla=tableroAjeno[i][j];
                int c=casilla.getValor();
                if (c!=0)
                    if (casilla.isProtegida())
                        canvas.drawText("" + c, col, row, numerosFijosAjenos);
                    else canvas.drawCircle(col, row, 20, numerosFijosAjenos);
            }
        }
    }

    private void dibujarTableroAjeno(Canvas canvas) {
        float ancho=(rightAjeno-leftAjeno)/3;
        float alto=(bottomAjeno-topAjeno)/3;

        canvas.drawLine(leftAjeno, topAjeno, rightAjeno, topAjeno, bordeGrueso);
        canvas.drawLine(rightAjeno, topAjeno, rightAjeno, bottomAjeno, bordeGrueso);
        canvas.drawLine(leftAjeno, topAjeno, leftAjeno, bottomAjeno, bordeGrueso);
        canvas.drawLine(leftAjeno, bottomAjeno, rightAjeno, bottomAjeno, bordeGrueso);

        canvas.drawLine(leftAjeno+ancho, topAjeno, leftAjeno+ancho, bottomAjeno, bordeGrueso);
        canvas.drawLine(leftAjeno+2*ancho, topAjeno, leftAjeno+2*ancho, bottomAjeno, bordeGrueso);
        canvas.drawLine(leftAjeno, topAjeno+alto, rightAjeno, topAjeno+alto, bordeGrueso);
        canvas.drawLine(leftAjeno, topAjeno+2*alto, rightAjeno, topAjeno+2*alto, bordeGrueso);

        ancho=ancho/3;
        for (int i=1; i<=8; i++) {
            canvas.drawLine(leftAjeno+ancho*i, topAjeno, leftAjeno+ancho*i, bottomAjeno, bordeFino);
        }
        alto=alto/3;
        for (int i=1; i<=8; i++) {
            canvas.drawLine(leftAjeno, topAjeno+alto*i, rightAjeno, topAjeno+alto*i, bordeFino);
        }
    }

    public void setBoard(String board) {
        int cont=0;
        for (int col=0; col<9; col++) {
            for (int row=0; row<9; row++) {
                int n=Character.getNumericValue(board.charAt(cont++));
                this.board[row][col]=n;
                this.tableroPropio[row][col]=new Casilla(n);
                if (n!=0)
                    this.tableroPropio[row][col].proteger();
                this.tableroAjeno[row][col]=new Casilla(n);
                this.tableroAjeno[row][col].proteger();
            }
        }
    }

    public void setCasilla(int row, int col, int valor) {
        this.tableroAjeno[col][row]=new Casilla(valor);
        invalidate();
    }

    public void showVictory(SudokuWinnerMessage swm) {
        partidaFinalizada=true;
        mensaje="Ganaste en " + swm.getTime() + " segundos";
        invalidate();
    }
}
