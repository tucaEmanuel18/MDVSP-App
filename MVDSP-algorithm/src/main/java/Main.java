import com.core.Depot;
import com.core.Problem;
import com.core.Trip;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        runInstanceOfMDVSProblem();
    }


    public static void runInstanceOfMDVSProblem(){
        Problem problem = new Problem();
        Depot d1 = new Depot("d1", 5);
        Depot d2 = new Depot("d2", 6);

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

        problem.setPairCost(d1, t1, Duration.ofMinutes(16));
        problem.setPairCost(d1, t2, Duration.ofMinutes(16));
        problem.setPairCost(d1, t3, Duration.ofMinutes(16));
        problem.setPairCost(d1, t4, Duration.ofMinutes(16));

        problem.setPairCost(t1, d1, Duration.ofMinutes(16));
        problem.setPairCost(t2, d1, Duration.ofMinutes(16));
        problem.setPairCost(t3, d1, Duration.ofMinutes(16));
        problem.setPairCost(t4, d1, Duration.ofMinutes(16));

        problem.setPairCost(d2, t1, Duration.ofMinutes(16));
        problem.setPairCost(d2, t2, Duration.ofMinutes(16));
        problem.setPairCost(d2, t3, Duration.ofMinutes(16));
        problem.setPairCost(d2, t4, Duration.ofMinutes(16));

        problem.setPairCost(t1, d2, Duration.ofMinutes(16));
        problem.setPairCost(t2, d2, Duration.ofMinutes(16));
        problem.setPairCost(t3, d2, Duration.ofMinutes(16));
        problem.setPairCost(t4, d2, Duration.ofMinutes(16));

        problem.setPairCost(t1, t2, Duration.ofMinutes(16));
        problem.setPairCost(t1, t3, Duration.ofMinutes(16));
        problem.setPairCost(t1, t4, Duration.ofMinutes(16));

        problem.setPairCost(t2, t1, Duration.ofMinutes(16));
        problem.setPairCost(t2, t3, Duration.ofMinutes(16));
        problem.setPairCost(t2, t4, Duration.ofMinutes(16));

        problem.setPairCost(t3, t1, Duration.ofMinutes(16));
        problem.setPairCost(t3, t2, Duration.ofMinutes(16));
        problem.setPairCost(t3, t4, Duration.ofMinutes(16));

        problem.setPairCost(t4, t1, Duration.ofMinutes(16));
        problem.setPairCost(t4, t2, Duration.ofMinutes(16));
        problem.setPairCost(t4, t3, Duration.ofMinutes(16));


        problem.print();
        try{
            problem.resolve();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
