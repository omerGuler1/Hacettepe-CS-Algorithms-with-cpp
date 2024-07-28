import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        Map<Station, List<Edge>> adjacencyList = new HashMap<>();
        Map<Station, Double> shortestPaths = new HashMap<>();
        Map<Station, Station> previous = new HashMap<>();
        PriorityQueue<Station> queue = new PriorityQueue<>(Comparator.comparing(shortestPaths::get));

        // Initialize the graph with all stations mentioned in the lines
        for (TrainLine line : network.lines) {
            for (Station station : line.trainLineStations) {
                adjacencyList.putIfAbsent(station, new ArrayList<>());
                for (TrainLine innerLine : network.lines) {
                    for (Station innerStation : innerLine.trainLineStations) {
                        if (!station.equals(innerStation)) {
                            double walkTime = computeWalkTime(station, innerStation, network.averageWalkingSpeed);
                            adjacencyList.get(station).add(new Edge(innerStation, walkTime, false)); // Walking connection
                        }
                    }
                }
            }
        }

        // Manually add startPoint and destinationPoint if they are not part of any train line
        adjacencyList.putIfAbsent(network.startPoint, new ArrayList<>());
        adjacencyList.putIfAbsent(network.destinationPoint, new ArrayList<>());

        // Walking connection start to finish
        double walkTimeo = computeWalkTime(network.startPoint, network.destinationPoint, network.averageWalkingSpeed);
        adjacencyList.get(network.startPoint).add(new Edge(network.destinationPoint, walkTimeo, false));

        // Establish connections between consecutive stations on the same line and walking paths
        for (TrainLine line : network.lines) {
            List<Station> stations = line.trainLineStations;
            for (int i = 0; i < stations.size(); i++) {
                if (i < stations.size() - 1) {
                    adjacencyList.get(stations.get(i)).add(new Edge(stations.get(i + 1), computeTrainTime(stations.get(i), stations.get(i + 1), network.averageTrainSpeed), true));
                    adjacencyList.get(stations.get(i + 1)).add(new Edge(stations.get(i), computeTrainTime(stations.get(i + 1), stations.get(i), network.averageTrainSpeed), true));
                }
            }
        }

        // Add walking paths from the start point to all stations
        for (TrainLine line : network.lines) {
            for (Station station : line.trainLineStations) {
                double walkTime = computeWalkTime(network.startPoint, station, network.averageWalkingSpeed);
                adjacencyList.get(network.startPoint).add(new Edge(station, walkTime, false));
            }
        }

        // Add walking paths from all stations to the destination point
        for (TrainLine line : network.lines) {
            for (Station station : line.trainLineStations) {
                double walkTime = computeWalkTime(station, network.destinationPoint, network.averageWalkingSpeed);
                adjacencyList.get(station).add(new Edge(network.destinationPoint, walkTime, false));
            }
        }

        // Initialize Dijkstra's Algorithm
        adjacencyList.keySet().forEach(station -> {
            shortestPaths.put(station, Double.MAX_VALUE);
            previous.put(station, null);
        });
        shortestPaths.put(network.startPoint, 0.0);
        queue.add(network.startPoint);

        while (!queue.isEmpty()) {
            Station current = queue.poll();
            double currentDistance = shortestPaths.get(current);

            for (Edge edge : adjacencyList.get(current)) {
                Station neighbor = edge.destination;
                double weight = edge.weight;
                if (currentDistance + weight < shortestPaths.get(neighbor)) {
                    shortestPaths.put(neighbor, currentDistance + weight);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Backtrack to find the route
        List<RouteDirection> routeDirections = new ArrayList<>();
        Station step = network.destinationPoint;
        while (step != null && previous.get(step) != null) {
            Station from = previous.get(step);
            double duration = shortestPaths.get(step) - (from == null ? 0 : shortestPaths.get(from));
            // duration = Math.round(duration * 100.0) / 100.0;
            boolean isTrainRide = checkIfTrainRide(from, step, network);
            routeDirections.add(0, new RouteDirection(from.description, step.description, duration, isTrainRide));
            step = from;
        }
        return routeDirections;
    }

    private double computeWalkTime(Station start, Station end, double speed) {
        return start.coordinates.distanceTo(end.coordinates) / speed;
    }

    private double computeTrainTime(Station start, Station end, double speed) {
        return start.coordinates.distanceTo(end.coordinates) / (speed);
    }

    private boolean checkIfTrainRide(Station from, Station to, HyperloopTrainNetwork network) {
        return network.lines.stream().anyMatch(line -> line.trainLineStations.contains(from) && line.trainLineStations.contains(to));
    }

    public void printRouteDirections(List<RouteDirection> directions) {
        double totalMinutes = directions.stream().mapToDouble(dir -> dir.duration).sum();
        int roundedTotalMinutes = (int) Math.round(totalMinutes);
        System.out.println("The fastest route takes " + roundedTotalMinutes + " minute(s).");
        System.out.println("Directions");
        System.out.println("----------");

        int stepNum = 1;
        for (RouteDirection direction : directions) {
            String travelMode = direction.trainRide ? "Get on the train" : "Walk";
            System.out.println(stepNum++ + ". " + travelMode + " from \"" + direction.startStationName + "\" to \"" + direction.endStationName + "\" for " + String.format("%.2f", direction.duration) + " minutes.");
        }
    }
}

class Edge {
    Station destination;
    double weight;
    boolean trainRide;

    Edge(Station destination, double weight, boolean trainRide) {
        this.destination = destination;
        this.weight = weight;
        this.trainRide = trainRide;
    }
}