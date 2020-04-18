import java.io.IOException;

public class QuickSort {
	private static int partition(String arr[][], int low, int high, int sortBy) 
    { 
        float pivot = Float.parseFloat(arr[high][sortBy]);  
        int i = (low-1); // index of smaller element 
        for (int j=low; j<high; j++) 
        { 
            // If current element is smaller than the pivot 
            if (Float.parseFloat(arr[j][sortBy]) < pivot) 
            { 
                i++; 
  
                // swap arr[i] and arr[j] 
                String temp[] = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        // swap arr[i+1] and arr[high] (or pivot) 
        String temp[] = arr[i+1]; 
        arr[i+1] = arr[high]; 
        arr[high] = temp; 
  
        return i+1; 
    } 
  
  
    /* The main function that implements QuickSort() 
      arr[] --> Array to be sorted, 
      low  --> Starting index, 
      high  --> Ending index */
    public static void sort(String arr[][], int low, int high, int sortBy) throws IOException 
    { 
        if (low < high) 
        { 
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int pi = partition(arr, low, high, sortBy); 
  
            // Recursively sort elements before 
            // partition and after partition 
            sort(arr, low, pi-1, sortBy); 
            sort(arr, pi+1, high, sortBy); 
        } 
    }
}
