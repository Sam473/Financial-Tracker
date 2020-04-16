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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
	
	public void purchasesOverTimePeriodBar() throws IOException {
		//Need purchases to be implemented with valid dates to make this work
		//Make work for weekly?, monthly, yearly purchases.
		String timePeriod;
		int weeks[] = new int[10];
		int months[] = new int[12];
		int years[] = new int[5];
		System.out.println(
				"Time Period:\n1. Weekly\n2. Monthly\n3. Yearly\n4. Quit to main menu");
		String input = App.userIn.readLine();
		
		switch(input) {
		case "1":
			timePeriod = "Weekly"; //Categories they spend in
			break;
		case "2":
			timePeriod = "Monthly"; //The regular outgoings as a portion such as rent, bills, netflix, ...
			break;
		case "3":
			timePeriod = "Yearly"; //Comparison of purchases per unit of time decided by user
			break;
		}
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		
		ResultSet rs = RetrieveAndStore.readAllRecords("tblCategory");
		try {
			while (rs.next()) // Loop through the resultset
			{
				String purchaseDate = rs.getString("PurchaseDate");
				int amount = rs.getInt("PurchaseAmount");
				
				switch(timePeriod) {
				case "Weekly":
					
					break;
				case "Monthly":
					//add amount to array index of the month-1
					int month = Integer.parseInt(purchaseDate.substring(3, 5));
					months[month-1] += amount;
					break;
				case "Yearly":
					break;
				default:
					return;
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		for (int i = 0; i<11; i++) {
			dataset.addValue
		}
		dataset.addValue(1, rs.getString("CategoryName"), rs.getInt("Expenditure"));

		
		
		
	      JFreeChart barChart = ChartFactory.createBarChart(
	    	         "Purchases Comparison", 
	    	         timePeriod, "Value", 
	    	         dataset,PlotOrientation.VERTICAL, 
	    	         true, true, false);
	    	         
	    	      int width = 640;    /* Width of the image */
	    	      int height = 480;   /* Height of the image */ 
	    	      File BarChart = new File( "BarChart.jpeg" ); 
	    	      ChartUtilities.saveChartAsJPEG( BarChart , barChart , width , height );
		
		
		
		
		
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
		boolean loop = true;
		while (loop) {
			System.out.println(
					"Would you like a graph of:\n1. Categories of Purchases\n2. Regular Outgoing Amounts\n3. Compare Purchase Amounts over a Time Period\n4. Quit to main menu");
			String input = App.userIn.readLine();

			switch (input) { // Use user input to decide which action to complete
			case "1":
				purchaseCategoryPie(); //Categories they spend in
				break;
			case "2":
				regularOutgoingsPie(); //The regular outgoings as a portion such as rent, bills, netflix, ...
				break;
			case "3":
				purchasesOverTimePeriodBar(); //Comparison of purchases per unit of time decided by user
				break;
			case "4":
				loop = false;
			default: // Filter out invalid inputs
				System.out.println("Not an option, try again");
			}
		}
    }
}
