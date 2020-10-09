package ar.edu.itba.grupo3.TP;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //OsciladorAmortiguado osciladorAmortiguado = new OsciladorAmortiguado(0.001, 2, 5.0);
        //osciladorAmortiguado.run();
        MisionAMarte misionAMarte = new MisionAMarte(1d);
        misionAMarte.runSimulation(86400);
    }
}
