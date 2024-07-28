import java.util.ArrayList;

/**
 * Class representing the optimal solution for power grid optimization.
 */
public class OptimalPowerGridSolution {
    private int maxNumberOfSatisfiedDemands;
    private ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency;

    public OptimalPowerGridSolution(int maxNumberOfSatisfiedDemands, ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency) {
        this.maxNumberOfSatisfiedDemands = maxNumberOfSatisfiedDemands;
        this.hoursToDischargeBatteriesForMaxEfficiency = hoursToDischargeBatteriesForMaxEfficiency;
    }

    private int unsatisfiedDemands;
    private int demandedGigawatts;

    public OptimalPowerGridSolution(int maxNumberOfSatisfiedDemands, ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency, int unsatisfiedDemands, int demandedGigawatts) {
        this.maxNumberOfSatisfiedDemands = maxNumberOfSatisfiedDemands;
        this.hoursToDischargeBatteriesForMaxEfficiency = hoursToDischargeBatteriesForMaxEfficiency;
        this.unsatisfiedDemands = unsatisfiedDemands;
        this.demandedGigawatts = demandedGigawatts;
    }

    public int getUnsatisfiedDemands() {
        return unsatisfiedDemands;
    }

    public int getDemandedGigawatts() {
        return demandedGigawatts;
    }

    public int getmaxNumberOfSatisfiedDemands() {
        return maxNumberOfSatisfiedDemands;
    }

    public ArrayList<Integer> getHoursToDischargeBatteriesForMaxEfficiency() {
        return hoursToDischargeBatteriesForMaxEfficiency;
    }
}