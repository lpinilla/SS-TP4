package ar.edu.itba.grupo3.TP;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SimInfo {

    private int N;
    private int L;
    private List<Particle> allParticles;
    private int hitsCount;

    public SimInfo(){
        this.N = 0;
        this.L = 0;
        this.allParticles = new ArrayList<>();
        this.hitsCount = 0;
    }


}