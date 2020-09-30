package ar.edu.itba.grupo3.TP;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class OsciladorAmortiguado {

    private double deltaT;
    private double saveFactor;
    private final double k = Math.pow(10, 4);
    private final double gamma = 100;
    private double totalTime;
    private FileHandler fileHandler;

    public OsciladorAmortiguado(double deltaT, double saveFactor, double totalTime){
        this.deltaT = deltaT;
        this.saveFactor = saveFactor;
        this.totalTime = totalTime;
        this.fileHandler = new FileHandler("resources");
    }

    public double calculateForce(double x, double v){
        return -k * x - (gamma * v);
    }

    public double calculateAcceleration(double x, double v, double m){
        return calculateForce(x, v) / m;
    }

    public double analytic(double amp, double m, double t){
        return amp * Math.exp( -(gamma / (2* m)) * t) *
               Math.cos(Math.sqrt( (k / m) - (gamma * gamma / (4 * m * m))) * t);
    }

    public Double[] predictEuler(double r, double v, double m){
        Double[] ret = new Double[2];
        double force = calculateForce(r, v);
        //calculate v
        ret[1] = v + (this.deltaT / m) * force;
        //calculate r
        ret[0] = r + this.deltaT * ret[1] + this.deltaT * this.deltaT * force / (2 * m);
        return ret;
    }

    public void run(){
        double amplitud = 1.0;
        List<Particle> particleList = new ArrayList<>();
        Particle p = new Particle(1.0,0.0,
                -amplitud * this.gamma / (2.0 * 70.0), 0.0,
                0.0, 70.0, 0.0);
        particleList.add(p);
        double mass = p.getMass();
        for(int t = 0; t < (this.totalTime / deltaT); t++){
            if(t % saveFactor == 0) fileHandler.savePosition(particleList);
            //p.setX(analytic(amplitud, mass, t * deltaT));
            Double[] prediction = predictEuler(p.getX(), p.getVx(), mass);
            p.setX(prediction[0]);
            p.setVx((prediction[1]));
        }
    }


}
