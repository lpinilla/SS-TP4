package ar.edu.itba.grupo3.TP;

import java.util.ArrayList;
import java.util.List;

public class OsciladorAmortiguado {

    private double delta_t;
    private final double k = Math.pow(10, 4);
    private final double gamma = 100;
    private double total_time;
    private FileHandler fileHandler;

    public OsciladorAmortiguado(double delta_t, double total_time){
        this.delta_t = delta_t;
        this.total_time = total_time;
        this.fileHandler = new FileHandler("resources");
    }


    public double calculateForce(double x, double v, double m){
        return -k * x - gamma * v;
    }

    public double calculateAcceleration(double x, double v, double m){
        return calculateForce(x, v, m) / m;
    }

    public double analytic(double a, double m, double t){
        return a * Math.exp( -(gamma / (2* m)) * t) *
               Math.cos(Math.sqrt( (k / m) - (gamma * gamma / (4 * m * m))) * t);
    }

    public Double[] predictEulerVelocity(double r, double v, double m, double force){
        Double[] ret = new Double[2];
        //calculate v
        ret[0] = v + (this.delta_t / m) * force;
        //calculate r
        ret[1] = r + this.delta_t * ret[0] + this.delta_t * this.delta_t * force / (2 * m);
        return ret;
    }

    public void run(){
        double amplitud = 1.0;
        List<Particle> particleList = new ArrayList<>();
        Particle p = new Particle(1.0,0.0,
                -amplitud * this.gamma / 2.0, 0.0,
                0.0, 70.0, 0.0);
        particleList.add(p);
        double mass = p.getMass();
        for(int t = 0; t < (this.total_time / delta_t); t++){
            fileHandler.savePosition(particleList);
            p.setX(analytic(calculateAcceleration(p.getX(), p.getVx(), mass), mass, t));
        }
    }
}
