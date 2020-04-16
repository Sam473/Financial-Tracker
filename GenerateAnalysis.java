import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

//Change this so it works with our data
//Add some more charts
//See if i can open the charts in the program for user ease
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
	       
	    int width = 640;   /* Width of the image */
	    int height = 480;  /* Height of the image */ 
	    File pieChart = new File( "PieChart.jpeg" ); 
	    ChartUtilities.saveChartAsJPEG(pieChart, chart ,width ,height);
	}
	
	public void mainMenu () throws IOException {
		CategoryPie();
	
    }
}
