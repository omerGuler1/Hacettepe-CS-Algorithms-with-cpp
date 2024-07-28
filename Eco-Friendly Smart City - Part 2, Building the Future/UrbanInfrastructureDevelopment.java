import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for (Project project : projectList) {
            int[] schedule = project.getEarliestSchedule();
            project.printSchedule(schedule);
        }
    }


    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList projectNodes = document.getElementsByTagName("Project");
            for (int i = 0; i < projectNodes.getLength(); i++) {
                Node pNode = projectNodes.item(i);
                if (pNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element projectElement = (Element) pNode;
                    String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                    List<Task> tasks = new ArrayList<>();

                    NodeList taskNodes = projectElement.getElementsByTagName("Task");
                    for (int j = 0; j < taskNodes.getLength(); j++) {
                        Node tNode = taskNodes.item(j);
                        if (tNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element taskElement = (Element) tNode;
                            int taskId = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                            String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                            int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());
                            List<Integer> dependencies = new ArrayList<>();

                            NodeList dependencyNodes = taskElement.getElementsByTagName("DependsOnTaskID");
                            for (int k = 0; k < dependencyNodes.getLength(); k++) {
                                dependencies.add(Integer.parseInt(dependencyNodes.item(k).getTextContent()));
                            }

                            Task task = new Task(taskId, description, duration, dependencies);
                            tasks.add(task);
                        }
                    }

                    Project project = new Project(projectName, tasks);
                    projectList.add(project);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }
}
