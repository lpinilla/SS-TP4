package ar.edu.itba.grupo3.TP;

public class App
{
    public static void main(String[] args )
    {
        //OsciladorAmortiguado osciladorAmortiguado = new OsciladorAmortiguado(0.001, 2, 5.0);
        //osciladorAmortiguado.runAnalytic();
        MisionAMarte misionAMarte = new MisionAMarte("enero", 100.0,
                864, 8.0 * Math.pow(10, 3));
        //correr la simulación solamente con los planetas
        //misionAMarte.runSimulation(3.154 * Math.pow(10,7));
        //encontrar el mejor día para despegue
        //misionAMarte.findBestDate(1.577 * Math.pow(10,7));
        //misionAMarte.findBestDayToLaunch("enero", 365, 3.154 * Math.pow(10,7));
        //guardar la rapidez de un viaje lanzado en X días por Y tiempo
        //misionAMarte.recordFlightSpeed(276, 3.154 * Math.pow(10,7));
        //guardar la velocidad (vector) de un viaje lanzado en X días por Y tiempo
        //misionAMarte.recordFlightVelocity(276, 3.154 * Math.pow(10,7));
        //lanzar el cohete después de X días por Y tiempo sólo para animar
        //misionAMarte.sendShuttle("enero", 276, 3.154 * Math.pow(10,7));
        //encontrar un deltaT adecuado
        //misionAMarte.findDeltaT(1000, 10);
    }
}
