import com.core.Depot;
import com.core.Problem;
import com.core.Trip;

import java.time.Duration;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        runInstanceOfMDVSProblem();
        //runTests();
    }

    public static void runTests(){
        Depot d1 = new Depot("d1", 5);
        Depot d2 = new Depot("d2", 6);

        Trip t1 = new Trip("t1", LocalTime.parse("18:00"), LocalTime.parse("20:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("13:00"), LocalTime.parse("17:30") );

       // System.out.println(route.isFeasible());

    }


    public static void runInstanceOfMDVSProblem(){
        Problem problem = new Problem();
        Depot d1 = new Depot("d1", 1);
        Depot d2 = new Depot("d2", 6);

        Trip t1 = new Trip("t1", LocalTime.parse("18:00"), LocalTime.parse("20:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("13:00"), LocalTime.parse("17:30") );
        Trip t3 = new Trip("t3", LocalTime.parse("12:00"), LocalTime.parse("17:00") );
        Trip t4 = new Trip("t4", LocalTime.parse("11:00"), LocalTime.parse("11:30") );


        problem.addLocation(d1);
        problem.addLocation(d2);

        problem.addLocation(t1);
        problem.addLocation(t2);
        problem.addLocation(t3);
        problem.addLocation(t4);

        problem.setCost(d1, t1, Duration.ofMinutes(40));
        problem.setCost(d1, t2, Duration.ofMinutes(5));
        problem.setCost(d1, t3, Duration.ofMinutes(10));
        problem.setCost(d1, t4, Duration.ofMinutes(15));

        problem.setCost(t1, d1, Duration.ofMinutes(2));
        problem.setCost(t2, d1, Duration.ofMinutes(20));
        problem.setCost(t3, d1, Duration.ofMinutes(6));
        problem.setCost(t4, d1, Duration.ofMinutes(5));

        problem.setCost(d2, t1, Duration.ofMinutes(60));
        problem.setCost(d2, t2, Duration.ofMinutes(20));
        problem.setCost(d2, t3, Duration.ofMinutes(40));
        problem.setCost(d2, t4, Duration.ofMinutes(5));

        problem.setCost(t1, d2, Duration.ofMinutes(9));
        problem.setCost(t2, d2, Duration.ofMinutes(50));
        problem.setCost(t3, d2, Duration.ofMinutes(60));
        problem.setCost(t4, d2, Duration.ofMinutes(25));

        problem.setCost(t1, t2, Duration.ofMinutes(5));
        problem.setCost(t1, t3, Duration.ofMinutes(7));
        problem.setCost(t1, t4, Duration.ofMinutes(2));

        problem.setCost(t2, t1, Duration.ofMinutes(25));
        problem.setCost(t2, t3, Duration.ofMinutes(7));
        problem.setCost(t2, t4, Duration.ofMinutes(9));

        problem.setCost(t3, t1, Duration.ofMinutes(15));
        problem.setCost(t3, t2, Duration.ofMinutes(9));
        problem.setCost(t3, t4, Duration.ofMinutes(13));

        problem.setCost(t4, t1, Duration.ofMinutes(14));
        problem.setCost(t4, t2, Duration.ofMinutes(10));
        problem.setCost(t4, t3, Duration.ofMinutes(8));


        //problem.print();
        try{
            problem.getSolution().print();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
