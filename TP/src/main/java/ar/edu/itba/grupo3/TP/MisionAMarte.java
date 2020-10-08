package ar.edu.itba.grupo3.TP;

import java.util.List;

public class MisionAMarte {

    //masas en 10^24
    private final double spaceStationHeight = 1500; // km
    private final double spaceStationOrbitalSpeed = 7.12; // km /s
    private final double gravitationalConstant = 6.67430e-8; // km^3 / (kg * s^2)
    private double deltaT;

    //cuerpos estelares
    private Particle sun, earth, mars;
    //nave espacial
    private Particle spaceShuttle;

    public MisionAMarte(double deltaT){
        this.sun = new Particle(0.0, 0.0,
                0.0, 0.0,
                696000.0, 1988500.0, 0.0);
        this.earth = new Particle(1.493188929636662E+08, 1.318936357931255E+07,
                -3.113279917782445E+00,2.955205189256462E+01,
                6371.01, 5.97219, 0.0);
        this.mars = new Particle(2.059448551842169E+08, 4.023977946528339E+07,
                -3.717406842095575E+00, 2.584914078301731E+01,
                3389.92, 6.4171, 0.0);
        this.deltaT = deltaT;
    }

    public void evolveSystem(){
        //calcular aceleración
        //desplazar
        //calcular velocidad predictiva
        //calcular la fuerza en la nueva posición
        //calcular velocidad corregida
    }

    public double[] calculateForces(Particle p, List<Particle> p2){
        double[] ret = new double[] {0.0, 0.0};
        double dist, normal;
        for (Particle par : p2){
            dist = p.distanceToParticle(par);
            normal = gravitationalConstant * (p.getMass() * par.getMass() / (dist * dist));
            ret[0] += normal * ( (par.getX() - p.getX()) / dist);
            ret[1] += normal * ((par.getY() - p.getY()) / dist);
        }
        return ret;
    }

    //public double calculateAcceleration(double x, double v, double m){
    //    return calculateForce(x, v) / m;
    //}

    //public Double[] predictBeeman(double r, double prev_r, double v, double prev_v, double m){
    //    Double[] ret = new Double[2];
    //    double accel = calculateAcceleration(r, v, m);
    //    double prevAccel = calculateAcceleration(prev_r, prev_v, m);
    //    //calculate r
    //    ret[0] = r + v * deltaT + ((2/3.0) * accel - (1/6.0) * prevAccel) * deltaT * deltaT;
    //    //predecir v
    //    double predictedV = v + ((3/2.0) * accel - (1/2.0) * prevAccel) * deltaT;
    //    double nextAccel = calculateAcceleration(ret[0], predictedV, m);
    //    //calculate v
    //    ret[1] = v + ((1/3.0) * nextAccel + (5/6.0) * accel - (1/6.0) * prevAccel) * deltaT;
    //    return ret;
    //}

}
