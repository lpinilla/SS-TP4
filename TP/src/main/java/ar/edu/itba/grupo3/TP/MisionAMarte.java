package ar.edu.itba.grupo3.TP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class MisionAMarte {

    //masas en 10^24
    private final double spaceStationHeight = 1500 * Math.pow(10, 3); // m
    private final double spaceStationOrbitalSpeed = 7.12 * Math.pow(10, 3); // m /s
    private final double launchSpeed = 8.0 * Math.pow(10, 3); // m / s
    private final double gravitationalConstant =  6.693 * Math.pow(10,-11); // m^3 / (kg * s^2)
    private double deltaT;
    private FileHandler fileHandler;

    //objetos
    private List<Particle> objects;
    //nave espacial
    private Particle spaceShuttle;

    private int saveFreq;

    public MisionAMarte(String startingMonth, double deltaT, int saveFreq){
        //sun
        this.objects = new ArrayList<>();
        selectStartingMonth(startingMonth);
        this.deltaT = deltaT;
        this.saveFreq = saveFreq;
        this.fileHandler = new FileHandler("resources/mision_a_marte");
    }

    public void selectStartingMonth(String month) {
        this.objects.add(new Particle(0, 0.0, 0.0,
                0.0, 0.0,
                696000 * Math.pow(10,3), 1988500.0 * Math.pow(10,24), 0.0));
        switch (month){
            //1/1/20
            case "enero":
                //tierra
                this.objects.add(new Particle(1, -2.488497169862896 * Math.pow(10,10), 1.449783471212823* Math.pow(10,11),
                        -2.984892046591452 * Math.pow(10,4),-5.162374739569864 * Math.pow(10,3),
                        6378.137 * Math.pow(10, 3), 5.97219 * Math.pow(10, 24), 0.0));
                //marte
                this.objects.add(new Particle(2, -1.974852867957307 * Math.pow(10,11), -1.325074306424199 * Math.pow(10,11),
                        1.440720082952704 * Math.pow(10,4), -1.804659323598330 * Math.pow(10,4),
                        3389.92 * Math.pow(10, 3), 6.4171 * Math.pow(10, 24), 0.0));
                break;
            //2/1/20
            case "enero2":
                //tierra
                this.objects.add(new Particle(1,  -2.745980229220612 * Math.pow(10,10), 1.445097591346515* Math.pow(10,11),
                        -2.975208159467520 * Math.pow(10,4),-5.684204848406660 * Math.pow(10,3),
                        6378.137 * Math.pow(10, 3), 5.97219 * Math.pow(10, 24), 0.0));
                //marte
                this.objects.add(new Particle(2, -1.962332428065216 * Math.pow(10,11), -1.340617546911285 * Math.pow(10,11),
                        1.457517951128304 * Math.pow(10,4), -1.793286938216979 * Math.pow(10,4),
                        3389.92 * Math.pow(10, 3), 6.4171 * Math.pow(10, 24), 0.0));
                break;
            //1/6/20
            case "junio":
                //tierra
                this.objects.add(new Particle(1, -5.014824835036334 * Math.pow(10,10), -1.431715570585378 * Math.pow(10,11),
                        2.762681880274097 * Math.pow(10,4) ,-9.946939080083073 * Math.pow(10,3),
                        6378.137 * Math.pow(10, 3), 5.97219 * Math.pow(10, 24), 0.0));
                //marte
                this.objects.add(new Particle(2, 9.368383080176222 * Math.pow(10,10), -1.887390255355240 * Math.pow(10,11),
                        2.261977282742894 * Math.pow(10,4), 1.285278029310825 * Math.pow(10,4),
                        3389.92 * Math.pow(10, 3), 6.4171 * Math.pow(10, 24), 0.0));
                break;
            //28/9/20
            case "septiembre":
                //earth
                this.objects.add(new Particle(1, 1.493188929636662 * Math.pow(10,11), 1.318936357931255* Math.pow(10,10),
                        -3.113279917782445 * Math.pow(10,3),2.955205189256462 * Math.pow(10,4),
                        6378.137 * Math.pow(10, 3), 5.97219 * Math.pow(10, 24), 0.0));
                //mars
                this.objects.add(new Particle(2, 2.059448551842169* Math.pow(10,11), 4.023977946528339* Math.pow(10,10),
                        -3.717406842095575* Math.pow(10,3), 2.584914078301731 * Math.pow(10,4),
                        3389.92 * Math.pow(10, 3), 6.4171 * Math.pow(10, 24), 0.0));
                break;
        }
    }

    //calcular la fuerza resultante entre este objeto y todos los demás
    public double[] calculateForces(Particle p, List<Particle> p2){
        double[] ret = new double[] {0.0, 0.0};
        double dist, normal;
        for (Particle par : p2){
            if(p.equals(par)) continue;
            dist = p.distanceToParticle(par);
            normal = gravitationalConstant * p.getMass() * par.getMass() / (dist * dist);
            ret[0] += normal * (par.getX() - p.getX()) / dist;
            ret[1] += normal * (par.getY() - p.getY()) / dist;
        }
        return ret;
    }

    public void calculateAccelerations(){
        for(Particle p : objects){
            double[] forces = calculateForces(p, objects);
            p.setAx(forces[0] / p.getMass());
            p.setAy(forces[1] / p.getMass());
        }
    }

    public void calculateFutureAccelerations(){
        for(Particle p : objects){
            double[] forces = calculateForces(p, objects);
            p.setFutAx(forces[0] / p.getMass());
            p.setFutAy(forces[1] / p.getMass());
        }
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
            double[] pred_vs = new double[2];
            pred_vs[0] = p.getVx() + ( (3.0/2) * p.getAx() - (1.0/2) * p.getPrevAx()) * deltaT;
            pred_vs[1] = p.getVy() + ( (3.0/2) * p.getAy() - (1.0/2) * p.getPrevAy()) * deltaT;
            aux.add(pred_vs);
        }
        return aux;
    }

    public void calculateCorrectedVelocities(List<double[]> predicted){
        for(int i = 0; i < predicted.size(); i++){
            Particle p = objects.get(i);
            p.setVx(p.getVx() + ( (1d/3) * p.getFutAx() + (5d/6) * p.getAx() - (1d/6) * p.getPrevAx()) * deltaT);
            p.setVy(p.getVy() + ( (1d/3) * p.getFutAy() + (5d/6) * p.getAy() - (1d/6) * p.getPrevAy()) * deltaT);
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

    public void runSimulation(double totalTime){
        for(long i = 0; i < (totalTime / deltaT); i++){
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void spawnShuttle(){
        Particle sun = objects.stream().filter(p -> p.getId() == 0).findFirst().get();
        Particle earth = objects.stream().filter(p -> p.getId() == 1).findFirst().get();
        double d = spaceStationHeight + earth.getRadius();
        double angle = sun.angleBetweenParticle(earth);
        double xpos = earth.getX() + Math.cos(angle) * d;
        double ypos = earth.getY() + Math.sin(angle) * d;
        double vx = earth.getVx() + Math.cos(angle) * (launchSpeed + spaceStationOrbitalSpeed);
        double vy = earth.getVy() + Math.sin(angle) * (launchSpeed + spaceStationOrbitalSpeed);
        double radius = 100;
        double mass = 5 * Math.pow(10, 5);
        spaceShuttle = new Particle(3,  xpos, ypos, vx, vy, radius, mass, 0.0);
        objects.add(spaceShuttle);
    }

    public void findBestDayToLaunch(String startingMonth, int maxDays, double iterations){
        IntStream.range(0, maxDays).forEach(i -> sendShuttle(startingMonth, i, iterations));
    }

    public void sendShuttle(String startingMonth, int daysToLaunch, double totalTime){
        objects.clear();
        selectStartingMonth(startingMonth);
        Particle mars = objects.stream().filter(p -> p.getId() == 2).findFirst().get();
        //esperar daysToLaunch días hasta el despegue
        for(long i = 0; i < daysToLaunch * saveFreq; i++){
            evolveSystem();
            if(i % saveFreq == 0){
                fileHandler.savePositionIndexed(objects,
                        "viajes/" + daysToLaunch + "-journey", i / saveFreq);
            }
        }
        spawnShuttle();
        for(long i = 0; i < (totalTime / deltaT); i++){
            evolveSystem();
            if(i % saveFreq == 0){
                fileHandler.savePositionIndexed(objects,
                        "viajes/" + daysToLaunch + "-journey", (i + daysToLaunch * saveFreq) /saveFreq);
                fileHandler.saveData("viajes/" + daysToLaunch + "-day.tsv",
                        (double)i / saveFreq, spaceShuttle.distanceToParticle(mars));
            }
        }
    }

    public void recordFlightSpeed(long launchDay, double iterations){
        long stepsToLaunch = launchDay * saveFreq;
        //avanzar el sistema hasta el día de despegue
        for(long i = 0; i < stepsToLaunch; i++) evolveSystem();
        //spawnear la nave
        spawnShuttle();
        //crear lista auxiliar para poder imprimir
        List<Particle> aux = new ArrayList<>();
        //agregar la nave a la lista de objetos
        aux.add(spaceShuttle);
        //evolucionar el sistema con el resto del tiempo
        for(long i = launchDay; i < (iterations / deltaT); i ++){
            evolveSystem();
            if(i % saveFreq == 0)fileHandler.saveSpeed(aux);
        }
    }


    public void recordFlightVelocity(long daysToLaunch, double iterations){
        long stepsToLaunch = daysToLaunch * saveFreq;
        //avanzar el sistema hasta el día de despegue
        for(long i = 0; i < stepsToLaunch; i++) evolveSystem();
        //spawnear la nave
        spawnShuttle();
        //crear lista auxiliar para poder imprimir
        List<Particle> aux = new ArrayList<>();
        //agregar la nave a la lista de objetos
        aux.add(spaceShuttle);
        //evolucionar el sistema con el resto del tiempo
        for(long i = daysToLaunch; i < (iterations / deltaT); i++){
            evolveSystem();
            if(i % saveFreq == 0){
                fileHandler.saveVelocity(aux, "vec_velocidades");
            }
        }
    }


}
