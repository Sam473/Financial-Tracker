/**
 * Class to validate user inputs across the whole project
 * @author Sam
 */
public abstract class Validation {

	/**
	 * Check if an input is a valid integer
	 * @param toValidate to be validated
	 * @return true if integer conversion succeeds, false otherwise
	 */
	public static boolean isInteger(String toValidate) {
		try { //Try to convert input to integer
			Integer.parseInt(toValidate);
			return true;
		} catch (NumberFormatException e){
			System.out.println("Please enter an integer"); //Notify user of invalid input
			return false;
		}
	}

	public static boolean isDouble(String toValidate) {
		try{
			Double.parseDouble(toValidate);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Please enter a number");
			return false;
		}
	}

	/**
	 * Check if an input is in a valid range
	 * @param lower bound of range (inclusive)
	 * @param upper bound of range (inclusive)
	 * @param toValidate to be validated
	 * @return true if in range, false otherwise
	 */
	public static boolean isRangeValid(int lower, int upper, int toValidate) {
		//validate range for integers
		if (toValidate <= upper && toValidate >= lower) {
			return true;
		}
		else {
			System.out.println("Please enter an integer in the specified range"); //Notify user of invalid input
			return false;
		}
	}

}
