package QuickFood;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class Main {
	//gloval variable and constants declarations
	public static String driverName;
	
	public static final String DRIVERS_PATH = "drivers.txt", 
			SENTINEL_VAL = "x", 
			INVOICE_PATH = "invoice.txt";
	
	public static Customer theCustomer;

	public static void main(String[] args) {
		try {
			CaptureOrder();
			
			//checking whether the order can be completed with a valid driver and producing an appropriate invoice
			if(AssignDriver(theCustomer.restaurant.location)) {
				GenInvoice();
			} else
				GenInvalidInvoice();
		} catch (Exception e) {
			System.out.println("An error occurred processing your order!\nError: "+e.getMessage());
		}

	}
	
	//method that will be used to capture all information about the user and their order
	public static void CaptureOrder() {		
		try {
			//instantiating the customer object
			theCustomer = new Customer(new ArrayList<FoodItem>(), new Restaurant());
			theCustomer.itemOrder = new ArrayList<>();
			
			//variable declarations
			FoodItem item;
			
			Scanner inScan = new Scanner(System.in);
			
			int itemsCount = 0, currItemQuantity = 0;
			
			String currItemName = "";
			
			double currItemPrice = 0;
			
			//prompting the user for all neccessary order information
			System.out.println("Welcome to Quick Food's delivery app!");
			System.out.print("Enter your order number: ");
			theCustomer.orderNum = inScan.nextLine();
			
			System.out.print("Enter your name: ");
			theCustomer.name = inScan.nextLine();
			
			System.out.print("Enter your email address: ");
			theCustomer.email = inScan.nextLine();
			
			System.out.print("Enter your contact number: ");
			theCustomer.contactNum = inScan.nextLine();
			
			System.out.print("Enter your location (city): ");
			theCustomer.location = inScan.nextLine();
			
			theCustomer.restaurant = new Restaurant();
			
			System.out.print("Enter the restaurant's name: ");
			theCustomer.restaurant.name = inScan.nextLine();
			
			System.out.print("Enter the restaurant's location: ");
			theCustomer.restaurant.location = inScan.nextLine();
			
			System.out.print("Enter the restaurant's contact number: ");
			theCustomer.restaurant.contactNum = inScan.nextLine();
			
			System.out.println("Enter 'x' when prompted for the item's name to stop entering new items");
					
			//continually prompting the user to add items until a sentinel value is entered
			while(!currItemName.equals(SENTINEL_VAL)) {
				System.out.print("Order item "+(itemsCount+1)+" name: ");
				currItemName = inScan.nextLine();
				System.out.println();
				
				if(currItemName.equals(SENTINEL_VAL))
					break;
				
				System.out.print("Order item "+(itemsCount+1)+" quantity: ");
				currItemQuantity = Integer.parseInt(inScan.nextLine());
				System.out.println();
				
				System.out.print("Order item "+(itemsCount+1)+" price: ");
				currItemPrice = Double.parseDouble(inScan.nextLine());
				System.out.println();
				
				item = new FoodItem(currItemName, currItemPrice, currItemQuantity);
				theCustomer.itemOrder.add(item);
				++itemsCount;
			}
			
			System.out.print("Enter the special prep instructions: ");
			theCustomer.instructions = inScan.nextLine();
			
			System.out.print("Enter your address: ");
			theCustomer.address = inScan.nextLine();
			
			//calculating the total cost of their order before closing the input scanner
			theCustomer.CalcTotal();
			inScan.close();
		} catch (Exception e) {
			System.out.println("An error occurred capturing your order!\nError: "+e.getMessage());
		}

	}
	
	//method that will be used to create an invoice for the user in the form of a text file
	public static void GenInvoice() {
		try {
				Formatter f = new Formatter(INVOICE_PATH);
				
				//formatting and writing information to the invoice document
				f.format("%s %s", "Order number: "+theCustomer.orderNum, "\r\n");
				f.format("%s %s", "Customer: "+theCustomer.name, "\r\n");
				f.format("%s %s", "Email: "+theCustomer.email, "\r\n");
				f.format("%s %s", "Phone number: "+theCustomer.contactNum, "\r\n");
				f.format("%s %s", "Location: "+theCustomer.location, "\r\n\r\n");
				f.format("%s %s", "You have ordered the following from "+theCustomer.restaurant.name+" in "+theCustomer.restaurant.location, "\r\n\r\n");
				
				//adding a new line for each item ordered
				for(int x = 0; x < theCustomer.itemOrder.size(); x++) {
					f.format("%s %s", theCustomer.itemOrder.get(x).quantity+" x "+theCustomer.itemOrder.get(x).name +"( R"+
							theCustomer.itemOrder.get(x).price+" )", "\r\n");
				}
				
				f.format("%s", "\r\n");
				
				f.format("%s %s", "Special instructions: "+theCustomer.instructions, "\r\n\r\n");
				f.format("%s %s", "Total: R"+theCustomer.total, "\r\n\r\n");
				f.format("%s %s", driverName+" is nearest to the restaurant and so he will be delivering your order to you at:", "\r\n\r\n");
				f.format("%s %s", theCustomer.address, "\r\n\r\n");
				f.format("%s %s", "If you need to contact the restaurant, their number is "+theCustomer.restaurant.contactNum, "\r\n");
		
				//closing the formatter when finished
				f.close();
			} catch(Exception e) {
				System.out.println("Error occurred while creating file!");
			}
	}
	
	//method that assigns a valid driver from the drivers list to the order and returns a boolean value indicating whether a valid driver has been found
	public static boolean AssignDriver(String location) {
		//variable declarations
		String[] driverInfo;
		
		String driverInfoLine = "";
		
		boolean driverFound = false;
		
		//resetting the driver name
		driverName = "";
		
		try {
			File driverFile = new File(DRIVERS_PATH);
			
			Scanner inScan = new Scanner(driverFile);
			
			//looping through each driver in the file until a valid one in the same area as the restaurant is found
			while(inScan.hasNext())
			{
				driverInfoLine = inScan.nextLine();
				driverInfo = driverInfoLine.split(","); //Reading correctly
				
				//assigning the valid driver when found and terminating the loop
				if((location.trim()).equals(driverInfo[1].trim()))
				{
					System.out.println("Driver found: "+driverInfo[0]+"\nLocation: "+driverInfo[1]);
					driverName = driverInfo[0];
					
					driverFound = true;
					break;
				}
			}
			
			inScan.close();
		} catch(FileNotFoundException e) {
			System.out.println("Error: File not found!");
		}

		return driverFound;
	}
	
	//method that will generate an alternative invoice informing the user that their order could not be completed
	public static void GenInvalidInvoice() {
		try {
			Formatter f = new Formatter(INVOICE_PATH);
			
			f.format("%s", "Sorry! Our drivers are too far away from you to be able to deliver to your location");		
			
			f.close();
		} catch(Exception e) {
			System.out.println("Error occurred while creating file!");
		}
	}
}
