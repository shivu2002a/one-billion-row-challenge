package Solutions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;
/* 
 * This Solution took 700ms for 45k rows
 */
public class InitialSolution {

    public static void main(String[] args) throws FileNotFoundException {
        var start = System.currentTimeMillis();
        onebrc();
        System.out.format("Took %,d ms\n", System.currentTimeMillis() - start); // 0.7s avg
        System.out.format("For 1b rows: %f s\n", (1000000000 / 45000) * 0.7);

    }

    public static void onebrc() throws FileNotFoundException {
        var filename = "weather_stations.csv";

        // DoubleSummaryStatistics contains (count, min, max, sum, avg)
        Map<String, DoubleSummaryStatistics> allStats = new BufferedReader(new FileReader(filename))
            .lines()
            .parallel()
            .collect(
                groupingBy(line -> line.substring(0, line.indexOf(";")),
                summarizingDouble(line -> parseDouble(line.substring(line.indexOf(";") + 1))))
            );
        System.out.println(allStats.get("Mumbai"));
        var result = allStats
            .entrySet()
            .stream()
            .collect(
                    Collectors.toMap(java.util.Map.Entry::getKey, 
                    e ->  {
                        var stats = e.getValue();
                        return String.format("Min: %.1f, Max: %.1f, Avg: %.1f", stats.getMin(), stats.getMax(), stats.getAverage());
                    },
                    (l, r) -> r,
                    TreeMap::new)
            );

        System.out.println(result.get("Mumbai"));
    }
    
}