import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {

    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;
    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }
    private int getBatteryDischargeCapacity(int hours) {
        return hours * hours;
    }

    public int demandedGigawatts(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour) {
        int total=0;
        for (int hours : amountOfEnergyDemandsArrivingPerHour) {
            total += hours;
        }
        return total;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }


    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP() {
        int N = amountOfEnergyDemandsArrivingPerHour.size();
        int[] sol = new int[N + 1];
        int[] totalSatisfiedDemand = new int[N + 1];
        ArrayList<ArrayList<Integer>> hours = new ArrayList<>(N + 1);

        sol[0] = 0;
        totalSatisfiedDemand[0] = 0;
        hours.add(new ArrayList<>());

        //Find the optimal solution for each hour
        for (int j = 1; j <= N; j++) {
            int maxSol = 0;
            int maxTotalSatisfiedDemand = 0;
            ArrayList<Integer> bestHours = new ArrayList<>();
            // Search for the best solution from previous hours
            for (int i = 0; i < j; i++) {
                int capacity = getBatteryDischargeCapacity(j - i);
                int satisfiedDemand = Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), capacity);
                int currSol = sol[i] + satisfiedDemand;
                int currTotalSatisfiedDemand = totalSatisfiedDemand[i] + satisfiedDemand;
                if ((currSol > maxSol) || (currSol == maxSol && (currTotalSatisfiedDemand > maxTotalSatisfiedDemand))) {
                    maxSol = currSol;
                    maxTotalSatisfiedDemand = currTotalSatisfiedDemand;
                    bestHours = new ArrayList<>(hours.get(i));
                    bestHours.add(j);
                }
            }
            // Update if a new maximum is found
            sol[j] = maxSol;
            totalSatisfiedDemand[j] = maxTotalSatisfiedDemand;
            hours.add(bestHours);
        }

        int totalDemand = demandedGigawatts(amountOfEnergyDemandsArrivingPerHour);
        int unsatisfiedDemand = totalDemand - totalSatisfiedDemand[N];
        return new OptimalPowerGridSolution(sol[N], hours.get(N), unsatisfiedDemand, totalDemand);
    }
}
