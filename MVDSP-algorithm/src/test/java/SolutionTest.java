import com.core.Depot;
import com.core.Problem;
import com.core.Solution;
import com.core.Trip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionTest {
    private Problem problem;

    @BeforeEach
    public void setUp(){
        problem = new Problem();
    }

    @Test
    @DisplayName("Test1.1: 1 depot with 1 vehicle | 2 trips")
    public void given1DepotAnd2TripsWhenHaveEnoughVehiclesThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 1);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("13:00"), LocalTime.parse("14:00") );

        problem.addLocation(d1);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(20));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(30));

        problem.setPairCost(t1, t2, Duration.ofMinutes(20));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(70, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 60 (10 + 20 + 30)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    @Test
    @DisplayName("Test1.2.1: 1 depot with 2 vehicle | 2 trips")
    public void given1DepotAnd2TripsWhenEdgeBetweenTripsIsInfeasibleAndHaveEnoughVehiclesThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 2);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("12:15"), LocalTime.parse("14:00") );

        problem.addLocation(d1);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(20));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(30));

        problem.setPairCost(t1, t2, Duration.ofMinutes(20));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(80, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 80 (10 + 20 + 20 + 30)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //TODO assert when fail
    @Test
    @DisplayName("Test1.2.2: 1 depot with 1 vehicle | 2 trips")
    public void given1DepotAnd2TripsWhenEdgeBetweenTripsIsInfeasibleAndHaventEnoughVehiclesThenCrahs(){
        Depot d1 = new Depot("d1", 1);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("12:15"), LocalTime.parse("14:00") );

        problem.addLocation(d1);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(20));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(30));

        problem.setPairCost(t1, t2, Duration.ofMinutes(20));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(80, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 80 (10 + 20 + 20 + 30)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test1.3.1: 1 depot with 2 vehicle | 2 trips")
    public void given1DepotAnd2TripsWhenEdgeBetweenTripsIsFeasibleButNotOptimalAndHaveTwoVehiclesThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 2);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("14:00"), LocalTime.parse("15:00") );

        problem.addLocation(d1);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(10));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(15));

        problem.setPairCost(t1, t2, Duration.ofMinutes(20));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(55, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 55 (10 + 20 + 10 + 15)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test1.3.2: 1 depot with 1 vehicle | 2 trips")
    public void given1DepotAnd2TripsWhenEdgeBetweenTripsIsFeasibleButNotOptimalAndHaveOneVehiclesThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 1);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("14:00"), LocalTime.parse("15:00") );

        problem.addLocation(d1);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(10));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(15));

        problem.setPairCost(t1, t2, Duration.ofMinutes(20));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(115, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 55 (10 + 20 + 10 + 15)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }



    @Test
    @DisplayName("Test2.1: 2 depot with 1 - 1 vehicles | 2 trips")
    public void given2DepotAnd2TripsWhenEdgeBetweenTripsIsFeasibleButNotOptimalAndHaveOneOneVehiclesThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 1);
        Depot d2 = new Depot("d2", 2);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("14:00"), LocalTime.parse("15:00") );

        problem.addLocation(d1);
        problem.addLocation(d2);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(30));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(45));

        problem.setPairCost(d2, t1, Duration.ofMinutes(30));
        problem.setPairCost(d2, t2, Duration.ofMinutes(10));

        problem.setPairCost(t1, d2, Duration.ofMinutes(45));
        problem.setPairCost(t2, d2, Duration.ofMinutes(20));

        problem.setPairCost(t1, t2, Duration.ofMinutes(50));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(60, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 60 (10 + 20 + 10 + 20)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Assertul meu nu e bun
    @Test
    @DisplayName("Test2.1.2: 2 depot with 1 - 1 vehicles | 2 trips")
    public void given2DepotAnd2TripsWhenEdgeBetweenTripsIsFeasibleAndHaveOneOneVehiclesThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 1);
        Depot d2 = new Depot("d2", 2);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("12:40"), LocalTime.parse("14:00") );

        problem.addLocation(d1);
        problem.addLocation(d2);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(30));

        problem.setPairCost(t1, d1, Duration.ofMinutes(50));
        problem.setPairCost(t2, d1, Duration.ofMinutes(45));

        problem.setPairCost(d2, t1, Duration.ofMinutes(40));
        problem.setPairCost(d2, t2, Duration.ofMinutes(10));

        problem.setPairCost(t1, d2, Duration.ofMinutes(45));
        problem.setPairCost(t2, d2, Duration.ofMinutes(15));

        problem.setPairCost(t1, t2, Duration.ofMinutes(5));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(60, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 60 (10 + 20 + 10 + 20)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    // Assertul meu nu e bun
    @Test
    @DisplayName("Test2.1.2: 2 depot with 1 - 1 vehicles | 2 trips")
    public void given2DepotAnd2TripsWhenIsOptimalToChangeDepotsThenGetOptimumSolution(){
        Depot d1 = new Depot("d1", 2);
        Depot d2 = new Depot("d2", 2);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("14:00"), LocalTime.parse("15:00") );

        problem.addLocation(d1);
        problem.addLocation(d2);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(30));

        problem.setPairCost(t1, d1, Duration.ofMinutes(20));
        problem.setPairCost(t2, d1, Duration.ofMinutes(10));

        problem.setPairCost(d2, t1, Duration.ofMinutes(30));
        problem.setPairCost(d2, t2, Duration.ofMinutes(10));

        problem.setPairCost(t1, d2, Duration.ofMinutes(10));
        problem.setPairCost(t2, d2, Duration.ofMinutes(20));

        problem.setPairCost(t1, t2, Duration.ofMinutes(50));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            solution.print();
            assertEquals(60, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 60 (10 + 20 + 10 + 20)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }



    @Test
    @DisplayName("Test2.1.2: 2 depot with 1 - 1 vehicles | 2 trips")
    public void proba(){
        Depot d1 = new Depot("d1", 2);
        Depot d2 = new Depot("d2", 2);

        Trip t1 = new Trip("t1", LocalTime.parse("12:00"), LocalTime.parse("12:30") );
        Trip t2 = new Trip("t2", LocalTime.parse("12:40"), LocalTime.parse("15:00") );

        problem.addLocation(d1);
        problem.addLocation(d2);
        problem.addLocation(t1);
        problem.addLocation(t2);

        problem.setPairCost(d1, t1, Duration.ofMinutes(10));
        problem.setPairCost(d1, t2, Duration.ofMinutes(30));

        problem.setPairCost(t1, d1, Duration.ofMinutes(40));
        problem.setPairCost(t2, d1, Duration.ofMinutes(45));

        problem.setPairCost(d2, t1, Duration.ofMinutes(500));
        problem.setPairCost(d2, t2, Duration.ofMinutes(10));

        problem.setPairCost(t1, d2, Duration.ofMinutes(45));
        problem.setPairCost(t2, d2, Duration.ofMinutes(20));

        problem.setPairCost(t1, t2, Duration.ofMinutes(5));
        problem.setPairCost(t2, t1, Duration.ofMinutes(90));
        try{
            Solution solution = problem.getSolution();
            assertEquals(60, solution.getCostWithTripsCostZero(),
                    "The optimum cost should be 60 (10 + 20 + 10 + 20)");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }



}
