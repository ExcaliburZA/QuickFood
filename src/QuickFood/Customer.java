package QuickFood;

import java.util.ArrayList;

public class Customer {
	//class atttributes
	public String orderNum, name, contactNum, address, location, email, instructions;
	
	public ArrayList<FoodItem> itemOrder;
	
	public Restaurant restaurant;
	
	public double total;	

	//default empty class constructor
	public Customer() {}	
	
	//partially parameterised constructor 
	public Customer(ArrayList<FoodItem> itemOrder, Restaurant restaurant) {
		this.itemOrder = itemOrder; 
		this.restaurant = restaurant;
	}
	
	//method that will calculate and store the total cost of the user's order
	public void CalcTotal() {
		total = 0;
		
		for(int x = 0; x < itemOrder.size(); x++) {
			total += (itemOrder.get(x).price) * (itemOrder.get(x).quantity);
		}
	}
	
}
