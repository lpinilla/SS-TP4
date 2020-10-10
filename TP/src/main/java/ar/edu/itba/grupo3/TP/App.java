package ar.edu.itba.grupo3.TP;

public class App
{
    public static void main( String[] args )
    {
        //OsciladorAmortiguado osciladorAmortiguado = new OsciladorAmortiguado(0.001, 2, 5.0);
        //osciladorAmortiguado.runAnalytic();
        MisionAMarte misionAMarte = new MisionAMarte(100.0, 1000);
        //misionAMarte.runSimulation(3.154 * Math.pow(10,7));
        misionAMarte.findBestDate(3.154 * Math.pow(10,7));
    }
}
