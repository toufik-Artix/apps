package win;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class AlgoGenetique {
   
    private final static Random R = new Random(10000);

    public final City[] CITIES;

    public AlgoGenetique(final City[] data) {
    	CITIES=data;
        //throw new RuntimeException("No!");
    }

    /*private  City[] generateData1(final int numDataPoints) {
        final City[] data = new City[numDataPoints];
        for(int i = 0; i < numDataPoints; i++) {
            data[i] = new City(AlgoGenetique.randomIndex(World.WIDTH),
                                  AlgoGenetique.randomIndex(World.HEIGHT));
        }
        return data;
    }*/

    static int randomIndex(final int limit) {
        return R.nextInt(limit);
    }

    static<T> List<T>[] split(final List<T> list) {
        final List<T> first = new ArrayList<>();
        final List<T> second = new ArrayList<>();
        final int size = list.size();
        final int partitionIndex = 1 + AlgoGenetique.randomIndex(list.size());
        IntStream.range(0, size).forEach(i -> {
            if(i < (size+1)/partitionIndex) {
                first.add(list.get(i));
            } else {
                second.add(list.get(i));
            }
        });
        return (List<T>[]) new List[] {first, second};
    }


}
