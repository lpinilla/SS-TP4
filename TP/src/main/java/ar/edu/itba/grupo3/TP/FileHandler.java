package ar.edu.itba.grupo3.TP;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class FileHandler {

    private String staticInputFile;
    private String dynamicInputfile;
    private String dynamicFile;
    private String hitFreqFile;
    private String dcmFile;
    private String velocity;
    private String hitTimes;

    public FileHandler(){
        staticInputFile = dynamicInputfile = dynamicFile = hitFreqFile = dcmFile = "";
    }

    public FileHandler(String staticfile, String dynamicInputfile, String dynamicFile, String hitFreqFile, String dcmFile,String velocity,String hitTimes){
        this.staticInputFile = staticfile;
        this.dynamicInputfile = dynamicInputfile;
        this.dynamicFile = dynamicFile;
        this.hitFreqFile = hitFreqFile;
        this.dcmFile = dcmFile;
        this.velocity=velocity;
        this.hitTimes=hitTimes;
    }

    public SimInfo loadData(){
        SimInfo info = loadStaticFile();
        return loadDynamicFile(info);
    }

    public SimInfo loadStaticFile()  {
        SimInfo ret = new SimInfo();
        List<Particle> allParticles = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(staticInputFile)));
            String s;
            ret.setN(Integer.parseInt(br.readLine()));
            ret.setL(Integer.parseInt(br.readLine()));
            //particles
            int index = 0;
            while ((s = br.readLine()) != null) {
                String[] rad_prop = s.split("\t");
                allParticles.add(
                        new Particle(
                                Double.parseDouble(rad_prop[0]),
                                Double.parseDouble(rad_prop[1]),
                                index));
                index++;
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        ret.setAllParticles(allParticles);
        return ret;
    }

    public SimInfo loadDynamicFile(SimInfo info) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(dynamicInputfile)));
            String s;
            int time = Integer.parseInt(br.readLine()); //ignore first line
            //particles
            int index = 0;
            Particle aux;
            while ((s = br.readLine()) != null) {
                String[] position = s.split("\t");
                aux = info.getAllParticles().get(index);
                aux.setX(Double.parseDouble(position[0]));
                aux.setY(Double.parseDouble(position[1]));
                aux.setVx(Double.parseDouble(position[2]));
                aux.setVy(Double.parseDouble(position[3]));
                index++;
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return info;
    }

    public void saveDynamic(SimInfo i, int n) {
        String fileOutputPath = "resources/dynamic.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileOutputPath), true) );
            writer.write(Integer.toString(i.getN()+4));
            writer.newLine();
            writer.write(Integer.toString(n));
            writer.newLine();
            //pongo en las esquinas solo para fijar el cuadro de simulacion
            String builder1 = String.format(Locale.US, "%6.7e", 0.0)+ "    " +
                    String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0);
            writer.write(builder1);
            writer.newLine();String builder2 = String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", (double) i.getL()) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0);
            writer.write(builder2);
            writer.newLine();String builder3 = String.format(Locale.US, "%6.7e", (double) i.getL()) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0);
            writer.write(builder3);
            writer.newLine();String builder4 = String.format(Locale.US, "%6.7e", (double) i.getL()) + "    " +
                    String.format(Locale.US, "%6.7e", (double) i.getL()) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0) + "    " +
                    String.format(Locale.US, "%6.7e", 0.0);
            writer.write(builder4);
            writer.newLine();
            for(Particle p : i.getAllParticles()){
                String builder = String.format(Locale.US, "%6.7e", p.getX()) + "    " +
                        String.format(Locale.US, "%6.7e", p.getY()) + "    " +
                        String.format(Locale.US, "%6.7e", p.getRadius()) + "    " +
                        String.format(Locale.US, "%6.7e", Math.sqrt(Math.pow(p.getVx(),2)+Math.pow(p.getVy(),2)));
                writer.write(builder);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveHitFreq(int hitCount){
        String fileOutputPath = hitFreqFile;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileOutputPath), true));
            writer.write(Integer.toString(hitCount));
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveVelocity(List<Particle> l ){
        String fileOutputPath = velocity;
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileOutputPath), true));
            for(Particle p:l){
                writer.write(String.format(Locale.US, "%6.7e", (p.realSpeed())));
                writer.newLine();

            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            System.out.println(e.getMessage());

        }
    }

    public void saveHitTime(double time){
        String fileOutputPath = hitTimes;
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileOutputPath), true));
            writer.write(time+" ");
            writer.flush();
            writer.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }


    public void saveDCM(double displacement,double timer){
        String fileOutputPath = dcmFile;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileOutputPath), true));
            writer.write(String.format(Locale.US, "%6.7e", (displacement*displacement)));
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveBigParticlePath(Particle p){
        String fileOutputPath = "resources/bigParticlePathV"+p.getSpeed()+"M"+p.getMass()+".txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileOutputPath), true) );
            String builder = String.format(Locale.US, "%6.7e", p.getX()) + "    " +
                    String.format(Locale.US, "%6.7e", p.getY());
            writer.write(builder);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}