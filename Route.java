package win;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Route {

    private final List<City> chromosome;
    private final double distance;
    AlgoGenetique ag;
    
    
    public double getDistance() {
        return this.distance;
    }
    
    public Route(AlgoGenetique a,final List<City> chromosome) {
    	ag=a;
        this.chromosome = Collections.unmodifiableList(chromosome);
        this.distance = calculateDistance();
    }
    
  
    
 
    /*private Route(final List<City> chromosome) {
    	
        this.chromosome = Collections.unmodifiableList(chromosome);
        this.distance = calculateDistance();
    }*/

    public Route create(final City[] points) {
        final List<City> genes = Arrays.asList(Arrays.copyOf(points, points.length));
        Collections.shuffle(genes);
        return new Route(ag,genes);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(final City gene : this.chromosome) {
            builder.append(gene.toString()).append((" : "));
        }
        return builder.toString();
    }

    List<City> getChromosome() {
        return this.chromosome;
    }

    double calculateDistance() {
        double total = 0.0f;
        for(int i = 0; i < this.chromosome.size() - 1; i++) {
            total += this.chromosome.get(i).distance(this.chromosome.get(i+1));
        }
        total += this.chromosome.get(chromosome.size()-1).distance(this.chromosome.get(0));
        return total;
    }

    Route[] crossOver(final Route other) {

        final List<City>[] myDNA = AlgoGenetique.split(this.chromosome);
        final List<City>[] otherDNA = AlgoGenetique.split(other.getChromosome());

        final List<City> firstCrossOver = new ArrayList<>(myDNA[0]);

        for(City gene : otherDNA[0]) {
            if(!firstCrossOver.contains(gene)) {
                firstCrossOver.add(gene);
            }
        }

        for(City gene : otherDNA[1]) {
            if(!firstCrossOver.contains(gene)) {
                firstCrossOver.add(gene);
            }
        }

        final List<City> secondCrossOver = new ArrayList<>(otherDNA[1]);

        for(City gene : myDNA[0]) {
            if(!secondCrossOver.contains(gene)) {
                secondCrossOver.add(gene);
            }
        }

        for(City gene : myDNA[1]) {
            if(!secondCrossOver.contains(gene)) {
                secondCrossOver.add(gene);
            }
        }

        if(firstCrossOver.size() != ag.CITIES.length ||
           secondCrossOver.size() != ag.CITIES.length) {
            throw new RuntimeException("oops!");
        }

        return new Route[] {
                new Route(ag,firstCrossOver),
                new Route(ag,secondCrossOver)
        };
    }

    Route mutate() {
        final List<City> copy = new ArrayList<>(this.chromosome);
        int indexA = AlgoGenetique.randomIndex(copy.size());
        int indexB = AlgoGenetique.randomIndex(copy.size());
        while(indexA == indexB) {
            indexA = AlgoGenetique.randomIndex(copy.size());
            indexB = AlgoGenetique.randomIndex(copy.size());
        }
        Collections.swap(copy, indexA, indexB);
        return new Route(ag,copy);
    }


}
