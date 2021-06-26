package win;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Population {

    private List<Route> population;
    private final int initialSize;
    AlgoGenetique ag;
    Route r;
    
    Population(AlgoGenetique a, final City[] points,
                  final int initialSize) {
    	ag=a;
    	final List<City> genes = Arrays.asList(Arrays.copyOf(points, points.length));
    	r=new Route(ag,genes);
        this.population = init(points, initialSize);
        this.initialSize = initialSize;
    }

    List<Route> getPopulation() {
        return this.population;
    }

    Route getAlpha() {
        return this.population.get(0);
    }

    private List<Route> init(final City[] points, final int initialSize) {
        final List<Route> eden = new ArrayList<>();
        for(int i = 0; i < initialSize; i++) {
            final Route chromosome = r.create(points);
            eden.add(chromosome);
        }
        return eden;
    }

    void update() {
        doCrossOver();
        doMutation();
        doSpawn();
        doSelection();
    }

    private void doSelection() {
        this.population.sort(Comparator.comparingDouble(Route::getDistance));
        this.population = this.population.stream().limit(this.initialSize).collect(Collectors.toList());
    }

    private void doSpawn() {
        IntStream.range(0, 1000).forEach(e -> this.population.add(r.create(ag.CITIES)));
    }

    private void doMutation() {
        final List<Route> newPopulation = new ArrayList<>();
        for(int i = 0; i < this.population.size()/10; i++) {
            final Route mutation = this.population.get(AlgoGenetique.randomIndex(this.population.size())).mutate();
            newPopulation.add(mutation);
        }
        this.population.addAll(newPopulation);
    }

    private void doCrossOver() {

        final List<Route> newPopulation = new ArrayList<>();
        for(final Route chromosome : this.population) {
            final Route partner = getCrossOverPartner(chromosome);
            newPopulation.addAll(Arrays.asList(chromosome.crossOver(partner)));
        }
        this.population.addAll(newPopulation);
    }

    private Route getCrossOverPartner(final Route chromosome) {
        Route partner = this.population.get(AlgoGenetique.randomIndex(this.population.size()));
        while(chromosome == partner) {
            partner = this.population.get(AlgoGenetique.randomIndex(this.population.size()));
        }
        return partner;
    }


}
