package ar.edu.itba.grupo3.TP;

import java.util.ArrayList;
import java.util.List;

public class MisionAMarte {

    //masas en 10^24
    private final double spaceStationHeight = 1500; // km
    private final double spaceStationOrbitalSpeed = 7.12; // km /s
    private final double gravitationalConstant = 6.67430e-11; // m^3 / (kg * s^2)
    private double deltaT;
    private FileHandler fileHandler;

    //objetos
    private List<Particle> objects;
    //nave espacial
    private Particle spaceShuttle;

    public MisionAMarte(double deltaT){
        //sun
        this.objects = new ArrayList<>();
        this.objects.add(new Particle(0, 0.0, 0.0,
                0.0, 0.0,
                696000.0, 1988500.0, 0.0));
        //earth
        this.objects.add(new Particle(1, 1.493188929636662E+08, 1.318936357931255E+07,
                -3.113279917782445E+00,2.955205189256462E+01,
                6371.01, 5.97219, 0.0));
        //mars
        this.objects.add(new Particle(2, 2.059448551842169E+08, 4.023977946528339E+07,
                -3.717406842095575E+00, 2.584914078301731E+01,
                3389.92, 6.4171, 0.0));
        this.deltaT = deltaT;
        this.fileHandler = new FileHandler("resources/mision_a_marte");
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

    public void calculateAccelerations(){
        List<Particle> aux = new ArrayList<>(objects);
        for(Particle p : objects){
            //eliminar al objeto de la lista
            aux.remove(p);
            //calcular la fuerza resultante entre este objeto y todos los demás
            double[] forces = calculateForces(p, aux);
            p.setAx(forces[0] / p.getMass());
            p.setAy(forces[1] / p.getMass());
            //volver a agregar al objeto a la lista
            aux.add(p);
        }
    }


    public void calculateFutureAccelerations(){
        List<Particle> aux = new ArrayList<>(objects);
        for(Particle p : objects){
            //eliminar al objeto de la lista
            aux.remove(p);
            //calcular la fuerza resultante entre este objeto y todos los demás
            double[] forces = calculateForces(p, aux);
            p.setFutAx(forces[0] / p.getMass());
            p.setFutAy(forces[1] / p.getMass());
            //volver a agregar al objeto a la lista
            aux.add(p);
        }
    }

    public void moveObjects(){
        double aux;
        for(Particle p : objects){
            if(p.getId() == 0) continue;
            aux = p.getX() + p.getVx() * deltaT + ((2d/3) * p.getAx() - (1d/6) * p.getPrevAx())* deltaT*deltaT;
            p.setX(aux);
            aux = p.getY() + p.getVy() * deltaT + ((2d/3) * p.getAy() - (1d/6) * p.getPrevAy())* deltaT*deltaT;
            p.setY(aux);
        }
    }

    public void calculatePredictedVelocities(){
        double aux;
        for(Particle p : objects){
            aux = p.getVx() + ( (3d/2) * p.getAx() - (1d/2) * p.getPrevAx()) * deltaT;
            p.setVx(aux);
            aux = p.getVy() + ( (3d/2) * p.getAy() - (1d/2) * p.getPrevAy()) * deltaT;
            p.setVy(aux);
        }
    }

    public void calculateCorrectedVelocities(){
        double aux;
        for(Particle p : objects){
            aux = p.getVx() + ( (1d/3) * p.getFutAx() + (5d/6) * p.getAx() - (1d/6) * p.getPrevAx()) * deltaT;
            p.setVx(aux);
            aux = p.getVy() + ( (1d/3) * p.getFutAy() + (5d/6) * p.getAy() - (1d/6) * p.getPrevAy()) * deltaT;
            p.setVy(aux);
        }
    }

    public void evolveSystem(){
        //calcular aceleración
        calculateAccelerations();
        //desplazar
        moveObjects();
        //calcular velocidad predictiva
        calculatePredictedVelocities();
        //calcular la fuerza en la nueva posición
        calculateFutureAccelerations();
        //calcular velocidad corregida
        calculateCorrectedVelocities();
    }

    public void runSimulation(double iterations){
        List<Particle> stelar = new ArrayList<>(objects);
        stelar.removeIf(p -> p.getId() == 0);
        for(long i = 0; i < (iterations / deltaT); i++){
            evolveSystem();
            fileHandler.savePositionIndexed(stelar, "test_run", i);
        }
    }

}
