# MDVSP-App
_Tiberiu Guțu, Țuca Emanuel_

**Enunt:*
MDVSP: Multi Depot Vehicle Scheduling Problem
- Cercetare, Algoritmica
- Studierea problemi MDVSP si implementarea unui scenariu practic, de
exemplu: o firma de transport persoane are un numar de garaje, fiecare garaj
are un numar de masini si trebuie sa planifice calatoriile pentru urmatoarea zi;
fiecare calatorie are definite locatiile de start si destinatie, intervalul de timp in
care trebuie sa fie onorata; aplicatia trebuie sa planifice calatoriile,
minimizand distanta totala parcursa (sau numarul de masini folosite);


**1. Algorithm:** 

Problem: 
- set of depots
- set of trips
- cost(i, j) -> time needed for a vehicle to travel from the end point of Trip i to the start point of Trip j
             -> time needed for a vehicle to travel from Depot i to start point of Trip j
             -> time nedded for a vehicle to travel from end point of Trip i to a Depot j

Depot:
- name
- number of available vechicles

Trip: 
- name
- starting time
- ending time
- tripCost -> time needed for a vehicle to travel from the start point of this trip to its end point

Steps: 
I. Map our problem to a minimum-cost circulation problem
II. Solve the new problem with the successive shortest path algorithm with capacity scaling
III. Repair the obtained solution with Kuhn Munkres, minimal weight bipartit perfect matching


I. Map our problem to a minimum-cost circulation problem 

-> For each depot Di, add two nodes in V(G), representing a source and a sink; 
      the capacity for source is number of available vehicles (the supply); 
      the capacity for sink is negate of number of available vehicles (the demand)
       
   -> For each trip Ti add two node in V(G), representing the trip startPoint and trip endPoint
      the capacity of these nodes is 0 (are transit nodes)
      
   -> Add edges:
      - Between depotSource and every trip startPoint -> lowerBound: 0 | upperBound: 1
      - Between trip startPoint end trip endPoint -> lowerBound: 1 | upperBound: 1
      - Between trip i endPoint end trip j startPoint -> lowerBound 0 | upperBound: 1 !! (*) If is _feasible Edge_
      - Between trip endPoint end depotSink -> lowerBound 0 | upperBound 1
      - Between depotSource i and depotSink i -> lowerBound 0 | upperBound: i.numberOfAvaibleVehicles 
                                                    (to permit to the rest of vehicles to cross the network)
                                                    
 (*) Let be i, j two trips; We say that ij is a _feasible Edge_ if i.EndingTime + cost(i, j) < j.startingTIme                                                    
 
 
 
II. Solve the new problem with the successive shortest path algorithm with capacity scaling (Using JgraphT)
-> this solution is not complete beacause permit to have routes which start from a depot and finish in another depot
This is the reason for we need to repair this solution 


III. Repair the obtained solution with Kuhn Munkres, minimal weight bipartit perfect matching
-> from the obtained solution we can extract a list of routes like: di - ta -tb -tc ... tq - dj, where i == j or i != j
If in route R i != j we say that R is an **infeasible Route**.

-> Repairing one subtour: changing the source depot, or the second depot (with the corresponding penalty cost)

![repairing_one_subtour](https://user-images.githubusercontent.com/59021794/119504819-03f2d000-bd75-11eb-959c-86c0044b0de6.png)

-> Repairing a pair of infeasible subtours: 

Suppose now that we have two infeasible subtours
      P1: i-ta, ta-tb,...,tp-j 
      P2: j-tx, tx-ty,...,tq-i
If we cand find a pair (h, k) 1 <= h <= p, 1 <= k <= q such that th->tk, tk-1 -> th + 1 than, we can replace the above pair of infeasible but compatible subtours by the following pair of feasible subtours
      P1' = i-ta, ta-tb, ... th-tk ... tq i
      P2' = j-tx, tx-ty, ..., tk-1 - th + 1 ... tp-j
      
 ![repairing_pair_of_subtours](https://user-images.githubusercontent.com/59021794/119508129-25a18680-bd78-11eb-944a-31cbe44b4750.png)

-> To find if a infeasible route should repair self or in pair(and with which other routes) we will create two bipartite graph as follows:

    S = {P: P is an infeasible subtour}
    T = {P':P is an infeasible soubtour}
The set of edges is: {P1P2' if is a compatible pair of infeasible subtours} U {PP': P is an infeasible subtour}

In first bipartiteGraph we set edges PP' weight =  penalty cost of changing source depot
In second biparatiteGraph we set edges PP'  weight = penalty cost of changing sink depot
In both bipartiteGraphs we set edges P1P2' weight = the pair (h, k) with minimum penalty cost

We will compute minimal weight bipartit perfect matching with Kuhn Munkres algorithm for both bipartiteGraphs and choose the graph which has minimum weight. 

After this we can repair the infeasible edges with selfFixation or PairFixation conformable to the matching obtained result.


2. **MDVSP-API**

-> POST "/MDVSP/depot" : add a new depot in problem
      {
        "name" = "d2",
        "numberOfVehicles" = 5,
        "latitude" : 2454.343435,
        "longitute" : 657685.2333
      }

-> POST "MDVSP/trip" : add a new trip in problem
      Body:
  {
    "name": "TestTrip",
    "startingTime": "12:30:00",
    "startPosition": {
        "latitude": 222.33,
        "longitude": 5555.245
    },
    "endPosition": {
        "latitude": 123.123,
        "longitude": 321.321
    }
}

-> GET "/MDVSP": get list of routes that represent the solution of actual state of Problem
  
  Response Like :
  {
    "routes" : [ 
         {
            "locations": ["d3", "d5", "d7", "d3"],
            "cost" : 873
          },
          {
            "locations": ["d3", "d5", "d7", "d3"],
            "cost" : 873
          }
      ]
  "totalCost" : 234567 
}


3. *User Interface:*

-> React Map GL : based on  MapBox
![interfata0](https://user-images.githubusercontent.com/59021794/119515238-5dabc800-bd7e-11eb-804d-184844ec3063.png)
![interfata1](https://user-images.githubusercontent.com/59021794/119515257-61d7e580-bd7e-11eb-8b90-1607b68ade1f.png)


4. **References:**

[1] For MDVSP algorithm 
- Emanuel Florentin OLARIU, Cristian FRASINARU (2020) - Multiple-Depot Vehicle Scheduling Problem Heuristics
