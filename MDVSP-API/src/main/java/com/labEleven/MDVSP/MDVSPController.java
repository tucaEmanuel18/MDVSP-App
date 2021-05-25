package com.labEleven.MDVSP;


import com.MDVSP.core.Problem;
import com.MDVSP.core.Solution;
import com.labEleven.dto.SolutionDTO;
import com.labEleven.pojo.DepotPOJO;
import com.labEleven.pojo.Position;
import com.labEleven.pojo.ProblemPOJO;
import com.labEleven.pojo.TripPOJO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/MDVSP")
public class MDVSPController {

    private ProblemPOJO problem;

    public MDVSPController() {
        problem = new ProblemPOJO();
    }

    @CrossOrigin
    @PostMapping(value = "/depot")
    public Response createDepot(@RequestBody DepotPOJO newDepot) {
        System.out.println("Name: " + newDepot.getName());
        System.out.println("Available Cars: " + newDepot.getNumberOfVehicles());
        System.out.println("Position: " + newDepot.getPosition());

        problem.addDepot(newDepot);

        return new Response(200, "Depot added successfully.");
    }

    @CrossOrigin
    @PostMapping(value = "/trip")
    public Response createTrip(@RequestBody TripPOJO newTrip) {
        System.out.println("Name: " + newTrip.getName());
        System.out.println("Starting Time: " + newTrip.getStartingTime());
        System.out.println("Ending Time: " + newTrip.getEndingTime());
        System.out.println("Start Position: " + newTrip.getStartPosition());
        System.out.println("End Position: " + newTrip.getEndPosition());

        problem.addTrip(newTrip);
        return new Response(200, "Trip added successfully.");
    }

    @CrossOrigin
    @GetMapping()
    public SolutionDTO resolve(){
        Solution solution = problem.resolve();
        return new SolutionDTO(solution);
    }
}
/*
Solution{
    solutionSize: 3
    route0 : {
        size: 4
        0 : d3
        1 : t5
        2 : t7
        3 : d3
        cost : 874;
        ? costWithoutTrips : 234;
    },
    route1 : {
    },
    route2 : {
    },

    totalCost: 82334;
}
*/






////

/*@CrossOrigin
    @GetMapping("/depot")
    public DepotPOJO getTestDepot(){
        if(depot == null){
            depot = new DepotPOJO("TestDepot", 5, 222.33, 5555.245);
        }
        return depot;
    }*/

  /*  @CrossOrigin
    @GetMapping("/trip")
    public TripPOJO getTestTrip(){
        if(trip == null){
            trip = new TripPOJO("TestTrip", LocalTime.parse("00:00"),
                    new Position(222.33, 5555.245), new Position(123.123, 321.321));
        }
        return trip;
    }*/
