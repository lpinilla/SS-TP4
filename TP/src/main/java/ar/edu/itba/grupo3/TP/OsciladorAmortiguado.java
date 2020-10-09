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
    private double[] gearPredictorArr;
    private double[] alphas;
    private FileHandler fileHandler;

    public OsciladorAmortiguado(double deltaT, double saveFactor, double totalTime){
        this.deltaT = deltaT;
        this.saveFactor = saveFactor;
        this.totalTime = totalTime;
        this.gearPredictorArr = new double[6];
        for(int i = 0; i < 6; i++) gearPredictorArr[i] = Math.pow(deltaT, i) / factorial(i);
        this.alphas = new double[] { (3 / 16.0), (251/360.0), 1.0, (11/18.0), (1/6.0), (1/60.0)};
        this.fileHandler = new FileHandler("resources");
    }

    private float factorial(int n){
        if(n == 0 || n == 1) return 1;
        return n * factorial(n -1);
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

    public Double[] predictEuler(Particle p){
        double r = p.getX();
        double v = p.getVx();
        double m = p.getMass();
        Double[] ret = new Double[2];
        double force = calculateForce(r, v);
        //calculate v
        ret[1] = v + (this.deltaT / m) * force;
        //calculate r
        ret[0] = r + this.deltaT * ret[1] + this.deltaT * this.deltaT * force / (2 * m);
        return ret;
    }

    public Double[] predictBeeman(Particle p){
        double r = p.getX();
        double v = p.getVx();
        double m = p.getMass();
        Double[] ret = new Double[2];
        double accel = calculateAcceleration(r, v, m);
        double prevAccel = calculateAcceleration(p.getPrevX(), p.getPrevVx(), m);
        //calculate r
        ret[0] = r + v * deltaT + ((2/3.0) * accel - (1/6.0) * prevAccel) * deltaT * deltaT;
        //predecir v
        double predictedV = v + ((3/2.0) * accel - (1/2.0) * prevAccel) * deltaT;
        double nextAccel = calculateAcceleration(ret[0], predictedV, m);
        //calculate v
        ret[1] = v + ((1/3.0) * nextAccel + (5/6.0) * accel - (1/6.0) * prevAccel) * deltaT;
        return ret;
    }

    public Double[] calculateInitialDerivs(Particle p, int n){
        Double[] ret = new Double[n+1];
        ret[0] = p.getX();
        ret[1] = p.getVx();
        double km = -k / p.getMass();
        ret[2] = km * p.getX();
        ret[3] = km * ret[1];
        ret[4] = km * km * p.getX();
        ret[5] = km * km * ret[1];
        return ret;
    }

    public Double[] makePrediction(Double[] derivs){
        Double[] ret = derivs.clone();
        double aux;
        for(int i = 0; i < ret.length; i++){
            aux = 0;
            for(int j = 0; j < ret.length - i; j++) aux += derivs[i + j] * gearPredictorArr[j];
            ret[i] = aux;
        }
        return ret;
    }

    public Double evaluateAcceleration(Double r0, Double r1, Double r2, Particle p){
        double accel = calculateAcceleration(r0, r1, p.getMass());
        return (accel - r2) * deltaT * deltaT / 2;
    }

    public Double[] gearPredictor(Double[] derivs, Particle p){
        Double[] predictions = makePrediction(derivs);
        double deltaR2 = evaluateAcceleration(predictions[0], predictions[1], predictions[2], p);
        for(int i = 0; i < predictions.length; i++){
            predictions[i] += alphas[i] * deltaR2 * factorial(i) / Math.pow(deltaT, i);
        }
        return predictions;
    }

    public void runAll(){
        runAnalytic();
        runEuler();
        runBeeman();
        runGear();
    }


    public void runAnalytic(){
        double amplitud = 1.0;
        List<Particle> particleList = new ArrayList<>();
        Particle p = new Particle(0, 1.0,0.0,
                -amplitud * this.gamma / (2.0 * 70.0), 0.0,
                0.0, 70.0, 0.0);
        particleList.add(p);
        double mass = p.getMass();
        for(int t = 0; t < (this.totalTime / deltaT); t++){
            if(t % saveFactor == 0) fileHandler.savePosition(particleList, "analytic");
            p.setX(analytic(amplitud, mass, t * deltaT));
        }
    }


    public void runEuler(){
        double amplitud = 1.0;
        List<Particle> particleList = new ArrayList<>();
        Particle p = new Particle(0, 1.0,0.0,
                -amplitud * this.gamma / (2.0 * 70.0), 0.0,
                0.0, 70.0, 0.0);
        particleList.add(p);
        for(int t = 0; t < (this.totalTime / deltaT); t++){
            if(t % saveFactor == 0) fileHandler.savePosition(particleList, "euler");
            Double[] prediction = predictEuler(p);
            p.setX(prediction[0]);
            p.setVx((prediction[1]));
        }
    }


    public void runBeeman(){
        double amplitud = 1.0;
        List<Particle> particleList = new ArrayList<>();
        Particle p = new Particle(0, 1.0,0.0,
                -amplitud * this.gamma / (2.0 * 70.0), 0.0,
                0.0, 70.0, 0.0);
        particleList.add(p);
        for(int t = 0; t < (this.totalTime / deltaT); t++){
            if(t % saveFactor == 0) fileHandler.savePosition(particleList, "beeman");
            Double[] prediction = predictBeeman(p);
            p.setX(prediction[0]);
            p.setVx((prediction[1]));
        }
    }


    public void runGear(){
        double amplitud = 1.0;
        List<Particle> particleList = new ArrayList<>();
        Particle p = new Particle(0, 1.0,0.0,
                -amplitud * this.gamma / (2.0 * 70.0), 0.0,
                0.0, 70.0, 0.0);
        particleList.add(p);
        Double[] prediction = calculateInitialDerivs(p, 5);
        for(int t = 0; t < (this.totalTime / deltaT); t++){
            if(t % saveFactor == 0) fileHandler.savePosition(particleList, "gearPredictorCorrector");
            prediction = gearPredictor(prediction, p);
            p.setX(prediction[0]);
            p.setVx((prediction[1]));
        }
    }

}
