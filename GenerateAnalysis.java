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

//Add some more charts
//Call a method to delete all charts once program closes for data security

public class GenerateAnalysis {
	JFrame frame;
	
	public void purchaseCategoryPie() throws IOException {
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
	       "Expenditure per Category (GBP)",   // chart title
	       dataset,          // data
	       true,             // include legend
	       true,
	       false);
	    
	    //Adjust the font size
	    PiePlot plot = (PiePlot) chart.getPlot();
	    int fontSize = 30; //the size of the fonts
	    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, fontSize));
	    
	    int width = 848;   // Width of the image
	    int height = 480;  // Height of the image
	    File pieChart = new File("PieChart.png"); 
	    ChartUtilities.saveChartAsPNG(pieChart, chart ,width ,height);
	    showGraph("PieChart.png");
	}
	
	public void showGraph(String graphfile) {
		  frame = new JFrame();
		  ImageIcon icon = new ImageIcon(graphfile);
		  JLabel label = new JLabel(icon);
		  frame.add(label);
		  frame.setDefaultCloseOperation
		         (JFrame.HIDE_ON_CLOSE);
		  frame.pack();
		  frame.setVisible(true);
	}
	
	public void purchasesOverTimePeriodBar(int timePerod) {
		//Need purchases to be implemented with valid dates to make this work
		//Make work for weekly?, monthly, yearly purchases.
	}
	
	public void regularOutgoingsPie() throws IOException {
DefaultPieDataset dataset = new DefaultPieDataset( );
		
		ResultSet rs = RetrieveAndStore.readAllRecords("tblOutgoings");
		try {
			while (rs.next()) // Loop through the resultset
			{
				//Add each category to the dataset for the pie chart
				dataset.setValue(rs.getString("OutgoingName"), rs.getInt("OutgoingAmount"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    JFreeChart chart = ChartFactory.createPieChart(
	       "Outgoings per Month (GBP)",   // chart title
	       dataset,          // data
	       true,             // include legend
	       true,
	       false);
	    
	    //Adjust the font size
	    PiePlot plot = (PiePlot) chart.getPlot();
	    int fontSize = 30; //the size of the fonts
	    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, fontSize));
	    
	    int width = 848;   // Width of the image
	    int height = 480;  // Height of the image
	    File pieChart = new File("PieChart2.png"); 
	    ChartUtilities.saveChartAsPNG(pieChart, chart ,width ,height);
	    showGraph("PieChart2.png");
	}
	
	public void mainMenu () throws IOException {
		purchaseCategoryPie(); //Categories they spend in
		regularOutgoingsPie(); //The regular outgoings as a portion such as rent, bills, netflix, ...
		//purchasesOverTimePeriodBar(1); //Comparison of purchases per unit of time decided by user
    }
}
