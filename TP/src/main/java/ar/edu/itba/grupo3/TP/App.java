package ar.edu.itba.grupo3.TP;

public class App
{
    public static void main( String[] args )
    {
        //OsciladorAmortiguado osciladorAmortiguado = new OsciladorAmortiguado(0.001, 2, 5.0);
        //osciladorAmortiguado.runAnalytic();
        MisionAMarte misionAMarte = new MisionAMarte(100d, 1000);
        misionAMarte.runSimulation(3.154e+7);
    }
}
