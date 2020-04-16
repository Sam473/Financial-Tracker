//For sql
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

//Imports for image viewing
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

//Imports for graph production
import java.awt.Font;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

//Change this so it works with our data
//Add some more charts
//Call a method to delete all charts once program closes for data security

public class GenerateAnalysis {
	
	public void CategoryPie() throws IOException {
		DefaultPieDataset dataset = new DefaultPieDataset( );
		
		ResultSet rs = RetrieveAndStore.readAllRecords("tblCategory");
		try {
			while (rs.next()) // Loop through the resultset
			{
				//Add each category to the dataset for the pie chart
				dataset.setValue(rs.getString("CategoryName"), rs.getInt("Expenditure"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    JFreeChart chart = ChartFactory.createPieChart(
	       "Categories",   // chart title
	       dataset,          // data
	       true,             // include legend
	       true,
	       false);
	    
	    //Adjust the font size
	    PiePlot plot = (PiePlot) chart.getPlot();
	    int fontSize = 30; //the size of the fonts
	    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, fontSize));
	    
	    int width = 1280;   /* Width of the image */
	    int height = 720;  /* Height of the image */ 
	    File pieChart = new File("PieChart.png"); 
	    ChartUtilities.saveChartAsPNG(pieChart, chart ,width ,height);
	    showGraph("PieChart.png");
	}
	
	public void showGraph(String graphfile) {
		  JFrame frame = new JFrame();
		  ImageIcon icon = new ImageIcon(graphfile);
		  JLabel label = new JLabel(icon);
		  frame.add(label);
		  frame.setDefaultCloseOperation
		         (JFrame.EXIT_ON_CLOSE);
		  frame.pack();
		  frame.setVisible(true);
	}
	
	
	public void mainMenu () throws IOException {
		CategoryPie();
	
    }
}
