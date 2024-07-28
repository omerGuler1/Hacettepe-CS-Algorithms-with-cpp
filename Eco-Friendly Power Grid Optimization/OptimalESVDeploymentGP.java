import java.util.ArrayList;
import java.util.Collections;

/**
 * This class accomplishes Mission Eco-Maintenance
 */
public class OptimalESVDeploymentGP
{
    private ArrayList<Integer> maintenanceTaskEnergyDemands;
    private ArrayList<Integer> remainingCapacities;

    /*
     * Should include tasks assigned to ESVs.
     * For the sample input:
     * 8 100
     * 20 50 40 70 10 30 80 100 10
     *
     * The list should look like this:
     * [[100], [80, 20], [70, 30], [50, 40, 10], [10]]
     *
     * It is expected to be filled after getMinNumESVsToDeploy() is called.
     */
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
        this.remainingCapacities = new ArrayList<>();
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {
        return maintenanceTaskEnergyDemands;
    }

    /**
     *
     * @param maxNumberOfAvailableESVs the maximum number of available ESVs to be deployed
     * @param maxESVCapacity the maximum capacity of ESVs
     * @return the minimum number of ESVs required using first fit approach over reversely sorted items.
     * Must return -1 if all tasks can't be satisfied by the available ESVs
     */
    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) {
        Collections.sort(maintenanceTaskEnergyDemands, Collections.reverseOrder());

        // if any demand exceeds the ESV capacity
        if (!maintenanceTaskEnergyDemands.isEmpty() && maintenanceTaskEnergyDemands.get(0) > maxESVCapacity) {
            return -1;
        }

        for (int demand : maintenanceTaskEnergyDemands) {
            boolean placed = false;
            for (int i = 0; i < remainingCapacities.size(); i++) {
                if (remainingCapacities.get(i) >= demand) {
                    remainingCapacities.set(i, remainingCapacities.get(i) - demand);
                    maintenanceTasksAssignedToESVs.get(i).add(demand);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                if (maintenanceTasksAssignedToESVs.size() < maxNumberOfAvailableESVs) {
                    remainingCapacities.add(maxESVCapacity - demand);
                    ArrayList<Integer> newTaskList = new ArrayList<>();
                    newTaskList.add(demand);
                    maintenanceTasksAssignedToESVs.add(newTaskList);
                } else {
                    return -1; // Not enough capacity or ESVs available
                }
            }
        }
        return maintenanceTasksAssignedToESVs.size();
    }
}

