package QuickFood;

public class FoodItem {
	//class atttributes
	public String name;
	
	public double price;
	
	public int quantity;
	
	//parameterised class constructor
	public FoodItem(String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
		
	//default empty class constructor
	public FoodItem() {}
	
}
