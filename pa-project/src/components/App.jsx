import React, { useState, useEffect } from "react";
import ReactMapGL, { Marker, Popup } from "react-map-gl";
import { useForm } from "react-hook-form";
import LineTo from "react-lineto";
import Button from "@material-ui/core/Button";

var ids = require("short-id");

function createTripMarker(name, type, startTime, coordinates) {
  var latitude = coordinates.split("/")[0];
  var longitude = coordinates.split("/")[1];
  //Function to create trip marker Object
  if (type === "start") {
    return {
      name: name,
      type: type,
      time: startTime,
      lat: latitude,
      long: longitude,
    };
  } else {
    return {
      name: name,
      type: type,
      time: null,
      lat: latitude,
      long: longitude,
    };
  }
}

function createTripPair(name, start_coordinates, end_coordinates) {
  return {
    name: name,
    start_coords: start_coordinates,
    end_coords: end_coordinates,
  };
}

function createDepotMarker(name, availableCars, coordinates) {
  //Function to create depot trip marker Object
  var latitude = coordinates.split("/")[0];
  var longitude = coordinates.split("/")[1];
  return {
    name: name,
    availableCars: availableCars,
    lat: latitude,
    long: longitude,
  };
}

function App() {
  const { register, handleSubmit } = useForm();
  const [isFormSubmitted, SetIsFormSubmitted] = useState(false);
  const [depotsMarkers, SetDepotsMarkers] = useState([]);
  const [tripsMarkers, SetTripsMarkers] = useState([]);
  const [tripPairs, SetTripPairs] = useState([]); //For drawing lines
  const [viewport, setViewport] = useState({
    latitude: 47.159809,
    longitude: 27.5872,
    width: "100vw",
    height: "100vh",
    zoom: 10,
  });
  const [selectedNode, setSelectedNode] = useState(null); //Selected nodes objects
  const [linesBetweenTrips, SetLinesBetweenTrips] = useState([]);
  const [solutionLines, SetSolutionLines] = useState([]);

  useEffect(() => {
    updateLinesBetweenTrips(); // This is be executed when `loading` state changes
  }, [tripPairs]);

  const onSubmitTrip = (data, e) => {
    e.target.reset();
    SetIsFormSubmitted(true);
    SetTripsMarkers((previous) => {
      return [
        ...previous,
        createTripMarker(
          data.Trip_Name,
          "start",
          data.Trip_Starting_Time,
          data.Trip_Start_Map_Coordinates
        ),
        createTripMarker(
          data.Trip_Name,
          "finish",
          null,
          data.Trip_End_Map_Coordinates
        ),
      ];
    });
    SetTripPairs((previous) => {
      return [
        ...previous,
        createTripPair(
          data.Trip_Name,
          data.Trip_Start_Map_Coordinates,
          data.Trip_End_Map_Coordinates
        ),
      ];
    });
    //Create Trip Object to send to API Call
    let tripBodyData = {
      name: data.Trip_Name,
      startingTime: data.Trip_Starting_Time,
      startLatitude: data.Trip_Start_Map_Coordinates.split("/")[0],
      startLongitude: data.Trip_Start_Map_Coordinates.split("/")[1],
      finishLatitude: data.Trip_End_Map_Coordinates.split("/")[0],
      finishLongitude: data.Trip_End_Map_Coordinates.split("/")[1],
    };

    //Make API Call
    fetch("http://localhost:8001/MDVSP/trip", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
      body: JSON.stringify(tripBodyData),
    })
      .then((res) => res.json())
      .then((result) => {
        console.log(result);
      });
  };

  const onSubmitDepot = (data, e) => {
    e.target.reset();
    SetIsFormSubmitted(true);

    SetDepotsMarkers((previous) => {
      return [
        ...previous,
        createDepotMarker(
          data.Depot_Name,
          data.Depot_Available_Cars,
          data.Depot_Coordinates
        ),
      ];
    });

    //Create Depot Object to send to API Call
    let depotBodyData = {
      name: data.Depot_Name,
      numberOfVehicles: data.Depot_Available_Cars,
      latitude: data.Depot_Coordinates.split("/")[0],
      longitude: data.Depot_Coordinates.split("/")[1],
    };

    //Make API Call
    fetch("http://localhost:8001/MDVSP/depot", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
      body: JSON.stringify(depotBodyData),
    })
      .then((res) => res.json())
      .then((result) => {
        console.log(result);
      });
  };

  function addClicked() {
    SetIsFormSubmitted(false);
  }

  function updateLinesBetweenTrips() {
    //If we have at least a trip pair
    if (tripPairs.length > 0) {
      //For each trip update the Line
      SetLinesBetweenTrips((previous) => {
        //Empty the previous lines, no longer valid
        previous = [];
        tripPairs.forEach((tripPair) => {
          previous.push(tripPair.name);
        });
        return previous;
      });
    }
  }

  function handleResolveClick(event) {
    SetSolutionLines((previous) => {
      return [];
    });
    //Rest API Call that will return the solution
    fetch("http://localhost:8001/MDVSP", {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((result) => {
        //Parse the response
        var serverResponse = result;

        //Traverse all the routes and draw lines between them
        serverResponse.routes.forEach((route) => {
          //Get path
          var currentPath = route.locations; //["d1", "t2", "t5", "d1"]
          //Add Paths to the SolutionLines
          SetSolutionLines((previous) => {
            return [...previous, currentPath];
          });
        });
      })
      .then(
        solutionLines.forEach((path) => {
          console.log(path);
        })
      );
  }

  return (
    <div>
      <ReactMapGL
        {...viewport}
        mapboxApiAccessToken={
          "pk.eyJ1IjoidGliZXJpdTIyIiwiYSI6ImNrb3gyaWQ0MjBhcmYyb3Q2M2hqZXpiMjgifQ.9QkG4qgqjCc5FiMA3vaFtQ"
        }
        mapStyle="mapbox://styles/tiberiu22/ckox3edkh16hh17mh44ogelry"
        onViewportChange={(viewport) => {
          setViewport(viewport);
          updateLinesBetweenTrips();
        }}
      >
        <div className="solveButton" onClick={handleResolveClick}>
          Resolve
        </div>
        <form onSubmit={handleSubmit(onSubmitTrip)}>
          <input
            className="input__trip"
            id="input-1__trip"
            type="text"
            placeholder="T1"
            {...register("Trip_Name")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />

          <label htmlFor="input-1__trip">
            <span className="label-text__trip">Trip Name</span>
            <span className="nav-dot__trip"></span>
            <div className="signup-button-trigger__trip" onClick={addClicked}>
              Add Trip
            </div>
          </label>
          <input
            className="input__trip"
            id="input-2__trip"
            type="text"
            placeholder="12:00"
            {...register("Trip_Starting_Time")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />
          <label htmlFor="input-2__trip">
            <span className="label-text__trip">Trip Starting Time</span>
            <span className="nav-dot__trip"></span>
          </label>

          <input
            className="input__trip"
            id="input-3__trip"
            type="text"
            placeholder="47.5234/52.1221"
            {...register("Trip_Start_Map_Coordinates")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />
          <label htmlFor="input-3__trip">
            <span className="label-text__trip">
              Trip Start Map Coordinates (Latitude/Longitude)
            </span>
            <span className="nav-dot__trip"></span>
          </label>

          <input
            className="input__trip"
            id="input-4__trip"
            type="text"
            placeholder="47.5234/52.1221"
            {...register("Trip_End_Map_Coordinates")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />
          <label htmlFor="input-4__trip">
            <span className="label-text__trip">
              Trip End Map Coordinates (Latitude/Longitude)
            </span>
            <span className="nav-dot__trip"></span>
          </label>

          {!isFormSubmitted && (
            <button className="form-button__trip" type="submit">
              Add Trip On Map
            </button>
          )}

          <p className="tip__trip">Press Tab</p>
          <div className="signup-button__trip">Add Trip</div>
        </form>

        <form onSubmit={handleSubmit(onSubmitDepot)}>
          <input
            className="input__depot"
            id="input-1__depot"
            type="text"
            placeholder="D1"
            {...register("Depot_Name")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />

          <label htmlFor="input-1__depot">
            <span className="label-text__depot">Depot Name</span>
            <span className="nav-dot__depot"></span>
            <div className="signup-button-trigger__depot" onClick={addClicked}>
              Add Depot
            </div>
          </label>
          <input
            className="input__depot"
            id="input-2__depot"
            type="text"
            placeholder="2"
            {...register("Depot_Available_Cars")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />
          <label htmlFor="input-2__depot">
            <span className="label-text__depot">
              Number Of Available Depot Cars
            </span>
            <span className="nav-dot__depot"></span>
          </label>

          <input
            className="input__depot"
            id="input-3__depot"
            type="text"
            placeholder="47.5232 / 52.1233"
            {...register("Depot_Coordinates")}
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            }}
            required
          />
          <label htmlFor="input-3__depot">
            <span className="label-text__depot">
              Map Coordinates (Latitude/Longitude)
            </span>
            <span className="nav-dot__depot"></span>
          </label>

          {!isFormSubmitted && (
            <button className="form-button__depot" type="submit">
              Add Depot On Map
            </button>
          )}

          <p className="tip__depot">Press Tab</p>
          <div className="signup-button__depot ">Add Depot</div>
        </form>

        {tripsMarkers.map((trip) => (
          <Marker
            className={
              trip.type === "start"
                ? trip.name + "_start"
                : trip.name + "_finish"
            }
            key={ids.generate()}
            latitude={parseFloat(trip.lat)}
            longitude={parseFloat(trip.long)}
          >
            <button
              className="marker-btn"
              onClick={(e) => {
                e.preventDefault();
                setSelectedNode(trip);
              }}
            >
              {trip.type === "start" ? (
                <img
                  // className={trip.name + "_start"}
                  src="./assets/images/marker.png"
                  alt="marker-img"
                  style={{ width: "3rem", height: "3rem" }}
                  className={trip.name + "_start"}
                />
              ) : (
                <img
                  // className={trip.name + "_finish"}
                  src="./assets/images/markerFinish.png"
                  alt="marker-img"
                  style={{ width: "3rem", height: "3rem" }}
                  className={trip.name + "_finish"}
                />
              )}
            </button>
          </Marker>
        ))}

        {solutionLines.map((path) => {
          return path.map((value, index, elements) => {
            if (index === elements.length - 1) {
              //Ignore last element
            } else {
              //Line between currentelement and next one
              return (
                <LineTo
                  borderColor="red"
                  borderStyle="solid"
                  borderWidth="3"
                  fromAnchor="center"
                  toAnchor="center"
                  from={value.charAt(0) === "T" ? value + "_finish" : value}
                  to={
                    elements[index + 1].charAt(0) === "T"
                      ? elements[index + 1] + "_start"
                      : elements[index + 1]
                  }
                />
              );
            }
          });
        })}

        {linesBetweenTrips.map((linesBetweenTrip) => (
          <LineTo
            borderColor="#009933"
            borderStyle="dashed"
            borderWidth="3"
            fromAnchor="center"
            toAnchor="center"
            from={linesBetweenTrip + "_start"}
            to={linesBetweenTrip + "_finish"}
          />
        ))}
        {depotsMarkers.map((depot) => (
          <Marker
            key={ids.generate()}
            latitude={parseFloat(depot.lat)}
            longitude={parseFloat(depot.long)}
          >
            <button
              className="marker-btn"
              onClick={(e) => {
                e.preventDefault();
                setSelectedNode(depot);
              }}
            >
              <img
                src="./assets/images/garage.png"
                alt="garage-img"
                style={{ width: "25px", height: "25px" }}
                className={depot.name}
              />
            </button>
          </Marker>
        ))}
        {selectedNode ? (
          <Popup
            latitude={parseFloat(selectedNode.lat)}
            longitude={parseFloat(selectedNode.long)}
            onClose={() => {
              setSelectedNode(null);
            }}
          >
            <h3>Name : {selectedNode.name}</h3>
            {selectedNode.availableCars ? (
              <h3>Depot Available Cars : {selectedNode.availableCars}</h3>
            ) : null}
            {selectedNode.type ? (
              <h3>Trip Marker Type: {selectedNode.type}</h3>
            ) : null}
            {selectedNode.availableCars ? null : selectedNode.type ===
              "start" ? (
              <h3>Trip Starting Time: {selectedNode.time}</h3>
            ) : null}
          </Popup>
        ) : null}
      </ReactMapGL>
    </div>
  );
}

export default App;
