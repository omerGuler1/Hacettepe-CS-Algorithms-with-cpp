import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;


class Main {
    public static void main(String args[]) throws IOException {
        int[] data = readFile(args[0]);
        int[] copyOfData = Arrays.copyOf(data, data.length);

        int[] inputSizes = new int[] {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        int[][] lastSortedData = SortTester.calculateDataForSorting(copyOfData, inputSizes);
        System.out.println("-------------------------");
        calculateDataForSearching(data, inputSizes, lastSortedData);
    }

    public static int[] readFile(String fileName) throws IOException {
        int[] dataList = new int[250000];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < dataList.length) {
                String[] cols = line.split(",");
                if (!cols[6].equals(" Flow Duration")) {
                    dataList[i] = Integer.parseInt(cols[6]);
                    i++;
                }
            }
        }
        return Arrays.copyOf(dataList, 250000);
    }


    // plots related graphs with sorting algorithms
    public static void calculateDataForSearching(int[] data, int[] inputSizes, int[][] lastSortedData) throws IOException {

        ArrayList<Integer> randomLinearSearch = new ArrayList<>();
        ArrayList<Integer> sortedLinearSearch = new ArrayList<>();
        ArrayList<Integer> binarySearch = new ArrayList<>();
        double[][] yAxis = new double[3][10];
        getTimes(data, inputSizes, randomLinearSearch, sortedLinearSearch, binarySearch, yAxis, lastSortedData);
    }

    public static void getTimes (int[] data,
                                    int[] inputSizes,
                                    ArrayList<Integer> timeLinearSearchWithRandomData,
                                    ArrayList<Integer> timeLinearSearchWithSortedData,
                                    ArrayList<Integer> timeBinarySearch,
                                    double[][] yAxis,
                                    int[][] lastSortedData) throws IOException {

        System.out.println("LINEAR SEARCH WITH RANDOM DATA");
        for (int size : inputSizes)
            Search.testLinearSearch(data, size, timeLinearSearchWithRandomData);
        for (int i = 0; i < 10; i++) {
            yAxis[0][i] = timeLinearSearchWithRandomData.get(i);
        }
        System.out.println("-------------------------");

        System.out.println("LINEAR SEARCH WITH SORTED DATA");

        int k=0;
        for (int size : inputSizes){
            Search.testLinearSearch(lastSortedData[k], size, timeLinearSearchWithSortedData);
            k++;
        }
        for (int i = 0; i < 10; i++) {
            yAxis[1][i] = timeLinearSearchWithSortedData.get(i);
        }
        System.out.println("-------------------------");

        System.out.println("BINARY SEARCH WITH SORTED DATA");
        int l=0;
        for (int size : inputSizes){
            Search.testBinarySearch(lastSortedData[l], size, timeBinarySearch);
            l++;
        }
        for (int i = 0; i < 10; i++) {
            yAxis[2][i] = timeBinarySearch.get(i);
        }

        String[] searches = {"Linear search (random data)", "Linear search (sorted data)", "Binary search (sorted data)"};
        showAndSaveChart("Searching Test", searches, inputSizes, yAxis,0);
    }

    public static void showAndSaveChart(String title, String[] data, int[] xAxis, double[][] yAxis, int param) throws IOException {
        // Create Chart
        String str = "";
        if (param==0){
            str = "Time in Nanoseconds";
        } else
            str = "Time in Milliseconds";

        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle(str).xAxisTitle("Input Size").build();


        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        for(int i = 0; i < data.length; i++) {
            chart.addSeries(data[i], doubleX, yAxis[i]);
        }

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
}
