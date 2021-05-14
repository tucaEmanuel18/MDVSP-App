import com.core.Depot;
import com.core.LocationPair;
import com.core.Problem;
import com.core.Trip;

import java.time.Duration;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem();
        Depot d1 = new Depot("d1", 5);
        Depot d2 = new Depot("d2", 6);
        Depot d3 = new Depot("d3", 7);


        Trip t1 = new Trip("t1", LocalTime.parse("20:02"), LocalTime.parse("21:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("15:20"), LocalTime.parse("17:50") );
        Trip t3 = new Trip("t3", LocalTime.parse("12:00"), LocalTime.parse("18:00") );
        Trip t4 = new Trip("t4", LocalTime.parse("00:00"), LocalTime.parse("00:30") );


        problem.addLocation(d1);
        problem.addLocation(d2);

        problem.addLocation(t1);
        problem.addLocation(t2);
        problem.addLocation(t3);
        problem.addLocation(t4);

        problem.setPairCost(d1, t1, Duration.ofMinutes(15));
        problem.setPairCost(d1, t2, Duration.ofMinutes(15));
        problem.setPairCost(d1, t3, Duration.ofMinutes(15));
        problem.setPairCost(d1, t4, Duration.ofMinutes(15));

        problem.setPairCost(d2, t1, Duration.ofMinutes(15));
        problem.setPairCost(d2, t2, Duration.ofMinutes(15));
        problem.setPairCost(d2, t3, Duration.ofMinutes(15));
        problem.setPairCost(d2, t4, Duration.ofMinutes(15));

        problem.setPairCost(t1, t2, Duration.ofMinutes(15));
        problem.setPairCost(t1, t3, Duration.ofMinutes(15));
        problem.setPairCost(t1, t4, Duration.ofMinutes(15));

        problem.setPairCost(t2, t3, Duration.ofMinutes(28));
        problem.setPairCost(t2, t4, Duration.ofMinutes(15));

        problem.setPairCost(t3, t4, Duration.ofMinutes(15));


       problem.print();


        System.out.println(problem.getPairCost(t2, t3));
    }
}
