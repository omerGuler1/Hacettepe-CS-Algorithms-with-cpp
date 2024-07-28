import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String demandsData = new String(Files.readAllBytes(Paths.get(args[0])));
        ArrayList<Integer> demands = new ArrayList<>(Arrays.asList(Arrays.stream(demandsData.split(" "))
                .map(Integer::parseInt)
                .toArray(Integer[]::new)));

        PowerGridOptimization optimization = new PowerGridOptimization(demands);
        OptimalPowerGridSolution solution = optimization.getOptimalPowerGridSolutionDP();

        String dischargeHours = solution.getHoursToDischargeBatteriesForMaxEfficiency().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        System.out.println("The total number of demanded gigawatts: " + solution.getDemandedGigawatts());
        System.out.println("Maximum number of satisfied gigawatts: " + solution.getmaxNumberOfSatisfiedDemands());
        System.out.println("Hours at which the battery bank should be discharged: " + dischargeHours);
        System.out.println("The number of unsatisfied gigawatts: " + solution.getUnsatisfiedDemands());
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");


        System.out.println("##MISSION ECO-MAINTENANCE##");
        List<String> lines = Files.readAllLines(Paths.get(args[1]));
        String[] firstLine = lines.get(0).split(" ");
        int availableESVs = Integer.parseInt(firstLine[0]);
        int ESVcapacity = Integer.parseInt(firstLine[1]);

        ArrayList<Integer> mteDemands = new ArrayList<>(Arrays.asList(Arrays.stream(lines.get(1).split(" "))
                .map(Integer::parseInt)
                .toArray(Integer[]::new)));


        OptimalESVDeploymentGP deployment = new OptimalESVDeploymentGP(mteDemands);
        int minESVs = deployment.getMinNumESVsToDeploy(availableESVs, ESVcapacity);

        if(minESVs != -1) {
            System.out.println("The minimum number of ESVs to deploy: " + minESVs);
        }

        ArrayList<ArrayList<Integer>> tasksAssigned = deployment.getMaintenanceTasksAssignedToESVs();
        if (minESVs != -1) {
            for (int i = 0; i < tasksAssigned.size(); i++) {
                System.out.println("ESV " + (i + 1) + " tasks: " + tasksAssigned.get(i));
            }
        } else {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        }

        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
