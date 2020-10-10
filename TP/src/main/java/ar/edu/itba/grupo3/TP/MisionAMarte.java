package ar.edu.itba.grupo3.TP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class MisionAMarte {

    //masas en 10^24
    private final double spaceStationHeight = 1500 * Math.pow(10, 3); // m
    private final double spaceStationOrbitalSpeed = 7.12 * Math.pow(10, 3); // m /s
    private final double launchSpeed = 8 * Math.pow(10, 3); // m / s
    private final double gravitationalConstant =  6.67408 * Math.pow(10,-11); // m^3 / (kg * s^2)
    private double deltaT;
    private FileHandler fileHandler;

    //objetos
    private List<Particle> objects;
    //nave espacial
    private Particle spaceShuttle;

    private int saveFreq;

    public MisionAMarte(double deltaT, int saveFreq){
        //sun
        this.objects = new ArrayList<>();
        this.objects.add(new Particle(0, 0.0, 0.0,
                0.0, 0.0,
                696000 * Math.pow(10,3), 1988500.0 * Math.pow(10,24), 0.0));
        //earth
        //fecha del tp
        this.objects.add(new Particle(1, 1.493188929636662 * Math.pow(10,11), 1.318936357931255* Math.pow(10,10),
                -3.113279917782445 * Math.pow(10,3),2.955205189256462 * Math.pow(10,4),
                6378.137 * Math.pow(10, 3), 5.97219 * Math.pow(10, 24), 0.0));
        //1ero de junio 2020
        //this.objects.add(new Particle(1, -5.014824835036334 * Math.pow(10,10), -1.431715570585378 * Math.pow(10,11),
        //        2.762681880274097 * Math.pow(10,4) ,-9.946939080083073 * Math.pow(10,3),
        //        6378.137 * Math.pow(10, 3), 5.97219 * Math.pow(10, 24), 0.0));
        //mars
        this.objects.add(new Particle(2, 2.059448551842169* Math.pow(10,11), 4.023977946528339* Math.pow(10,10),
                -3.717406842095575* Math.pow(10,3), 2.584914078301731 * Math.pow(10,4),
                3389.92 * Math.pow(10, 3), 6.4171 * Math.pow(10, 24), 0.0));
        //this.objects.add(new Particle(2, 9.368383080176222 * Math.pow(10,10), -1.887390255355240 * Math.pow(10,11),
        //        2.261977282742894 * Math.pow(10,4), 1.285278029310825 * Math.pow(10,4),
        //        3389.92 * Math.pow(10, 3), 6.4171 * Math.pow(10, 24), 0.0));
        this.deltaT = deltaT;
        this.saveFreq = saveFreq;
        this.fileHandler = new FileHandler("resources/mision_a_marte");
    }


    public double[] calculateForces(Particle p, List<Particle> p2){
        double[] ret = new double[] {0.0, 0.0};
        double dist, normal, theta;
        for (Particle par : p2){
            if(p.equals(par)) continue;
            dist = p.distanceToParticle(par);
            normal = gravitationalConstant * (p.getMass() * par.getMass() / (dist * dist));
            //theta = p.angleBetweenParticle(par);
            //ret[0] += normal * Math.cos(theta);
            //ret[1] += normal * Math.sin(theta);
            ret[0] += normal * ( (par.getX() - p.getX()) / dist);
            ret[1] += normal * ((par.getY() - p.getY()) / dist);
        }
        return ret;
    }

    public void calculateAccelerations(){
        List<Particle> aux = new ArrayList<>(objects);
        for(Particle p : objects){
            //eliminar al objeto de la lista
            //aux.remove(p);
            //calcular la fuerza resultante entre este objeto y todos los demás
            double[] forces = calculateForces(p, aux);
            p.setAx(forces[0] / p.getMass());
            p.setAy(forces[1] / p.getMass());
            //volver a agregar al objeto a la lista
            //aux.add(p);
        }
        //objects.sort(Comparator.comparing(Particle::getId));
    }


    public void calculateFutureAccelerations(){
        List<Particle> aux = new ArrayList<>(objects);
        for(Particle p : objects){
            //eliminar al objeto de la lista
            //aux.remove(p);
            //calcular la fuerza resultante entre este objeto y todos los demás
            double[] forces = calculateForces(p, aux);
            p.setFutAx(forces[0] / p.getMass());
            p.setFutAy(forces[1] / p.getMass());
            //volver a agregar al objeto a la lista
            //aux.add(p);
        }
        //objects.sort(Comparator.comparing(Particle::getId));
    }

    public void moveObjects(){
        double aux;
        for(Particle p : objects){
            if(p.getId() == 0) continue;
            aux = p.getX() + p.getVx() * deltaT + ((2.0/3) * p.getAx() - (1.0/6) * p.getPrevAx())* deltaT*deltaT;
            p.setX(aux);
            aux = p.getY() + p.getVy() * deltaT + ((2.0/3) * p.getAy() - (1.0/6) * p.getPrevAy())* deltaT*deltaT;
            p.setY(aux);
        }
    }

    public List<double[]> calculatePredictedVelocities(){
        List<double[]> aux = new ArrayList<>();
        for(Particle p : objects){
            double[] pred_vxs = new double[2];
            pred_vxs[0] = p.getVx() + ( (3.0/2) * p.getAx() - (1.0/2) * p.getPrevAx()) * deltaT;
            //p.setVx(aux);
            pred_vxs[1] = p.getVy() + ( (3.0/2) * p.getAy() - (1.0/2) * p.getPrevAy()) * deltaT;
            //p.setVy(aux);
            aux.add(pred_vxs);
        }
        return aux;
    }

    public void calculateCorrectedVelocities(List<double[]> predicted){
        double aux;
        for(int i = 0; i < predicted.size(); i++){
            Particle p = objects.get(i);
            aux = p.getVx() + ( (1.0/3) * p.getFutAx() + (5.0/6) * p.getAx() - (1.0/6) * p.getPrevAx()) * deltaT;
            p.setVx(aux);
            aux = p.getVy() + ( (1.0/3) * p.getFutAy() + (5.0/6) * p.getAy() - (1.0/6) * p.getPrevAy()) * deltaT;
            p.setVy(aux);
        }
    }

    public void evolveSystem(){
        //calcular aceleración
        calculateAccelerations();
        //desplazar
        moveObjects();
        //calcular velocidad predictiva
        List<double[]> predicted = calculatePredictedVelocities();
        //calcular la fuerza en la nueva posición
        calculateFutureAccelerations();
        //calcular velocidad corregida
        calculateCorrectedVelocities(predicted);
    }

    public void runSimulation(double iterations){
        for(long i = 0; i < (iterations / deltaT); i++){
            evolveSystem();
            if(i % saveFreq == 0) fileHandler.savePositionIndexed(objects, "test_run", i);
        }
    }

    public void findBestDate(double iterations){
        Particle earth = objects.stream().filter(p -> p.getId() == 1).findFirst().get();
        Particle mars = objects.stream().filter(p -> p.getId() == 2).findFirst().get();
        for(long i = 0; i < (iterations / deltaT); i++){
            evolveSystem();
            if(i % saveFreq == 0){
                fileHandler.saveData("resources/mision_a_marte/distances", (double) i / saveFreq,
                        earth.distanceToParticle(mars));
            }
        }
    }

    public void spawnShuttle(){
        Particle sun = objects.stream().filter(p -> p.getId() == 0).findFirst().get();
        Particle earth = objects.stream().filter(p -> p.getId() == 1).findFirst().get();
        double[] velVersor = earth.velocityVersor();
        //invertir el versor normal para obtener la posición de despegue de la nave
        velVersor[1] *= -1;
        double d = spaceStationHeight + earth.getRadius();
        double angle = sun.angleBetweenParticle(earth);
        double xpos = earth.getX() + Math.cos(angle) * d;
        double ypos = earth.getY() + Math.sin(angle) * d;
        double vx = earth.getVx() + Math.cos(angle) * (launchSpeed + spaceStationOrbitalSpeed);
        double vy = earth.getVy()+ Math.sin(angle) * (launchSpeed + spaceStationOrbitalSpeed);
        double radius = 100;
        double mass = 5 * Math.pow(10, 5);
        spaceShuttle = new Particle(3,  xpos, ypos, vx, vy, radius, mass, 0.0);
        objects.add(spaceShuttle);
    }

    public void sendShuttle(double iterations){
        Particle earth = objects.stream().filter(p -> p.getId() == 1).findFirst().get();
        Particle mars = objects.stream().filter(p -> p.getId() == 2).findFirst().get();
        int stepsToLaunch = 8 * saveFreq;
        //esperar a que llegue
        for(long i = 0; i < stepsToLaunch; i++){
            evolveSystem();
            if(i % saveFreq == 0) fileHandler.savePositionIndexed(objects, "viaje", i / saveFreq);
        }
        spawnShuttle();
        for(long i = 9 * saveFreq; i < (iterations / deltaT); i ++){
            evolveSystem();
            if(i % saveFreq == 0) fileHandler.savePositionIndexed(objects, "viaje", i / saveFreq);
        }
    }

}
