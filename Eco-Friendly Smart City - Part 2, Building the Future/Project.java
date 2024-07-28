import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int[] schedule = getEarliestSchedule();
        int projectDuration = 0;
        for (int i = 0; i < schedule.length; i++) {
            projectDuration = Math.max(projectDuration, schedule[i] + tasks.get(i).getDuration());
        }
        return projectDuration;
    }


    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        int numTasks = tasks.size();
        int[] schedule = new int[numTasks]; // Start times for each task
        int[] latestCompletion = new int[numTasks]; // Latest completion times (to determine project duration)

        // Initialize with impossible start times
        Arrays.fill(schedule, -1);

        class ScheduleCalculator {
            public int computeEarliestStart(int taskId) {
                if (schedule[taskId] != -1) {
                    return schedule[taskId]; // Already computed
                }

                Task task = tasks.get(taskId);
                int startTime = 0;
                for (Integer depId : task.getDependencies()) {
                    int depCompletionTime = computeEarliestStart(depId) + tasks.get(depId).getDuration();
                    startTime = Math.max(startTime, depCompletionTime); // Task can start after all dependencies are completed
                }
                schedule[taskId] = startTime;
                latestCompletion[taskId] = startTime + task.getDuration();
                return startTime;
            }
        }

        // Calculate start times for all tasks using the inner class
        ScheduleCalculator calculator = new ScheduleCalculator();
        for (int i = 0; i < numTasks; i++) {
            if (schedule[i] == -1) {
                calculator.computeEarliestStart(i);
            }
        }

        return schedule;
    }



    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
