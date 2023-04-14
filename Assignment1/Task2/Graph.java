import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class Graph {

    public static void main(String[] args) {

        // create sample data
        ArrayList<Double> xValues = new ArrayList<Double>();
        xValues.add(1.0);
        xValues.add(2.0);
        xValues.add(3.0);
        xValues.add(4.0);
        xValues.add(5.0);
        
        ArrayList<Double> yValues = new ArrayList<Double>();
        yValues.add(2.0);
        yValues.add(4.0);
        yValues.add(6.0);
        yValues.add(8.0);
        yValues.add(10.0);
        
        // create dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < xValues.size(); i++) {
            dataset.addValue(yValues.get(i), "Y", xValues.get(i));
        }

        // create chart
        org.jfree.chart.JFreeChart chart = ChartFactory.createLineChart(
            "Line Chart", // chart title
            "X", // x axis label
            "Y", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL,
            true, // include legend
            true, // tooltips
            false // urls
        );

        // customize chart
        chart.setBackgroundPaint(Color.white);

        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = new org.jfree.chart.renderer.category.LineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        // display chart
        ChartFrame frame = new ChartFrame("Line Chart Example", chart);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);

    }

}
