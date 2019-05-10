import java.util.Comparator;

//Base class for all products the store will sell
public class Product {
	private double price;
	private int quantity;
	private int sold;
	public Product(double initPrice, int initQuantity) {
		price = initPrice;
		quantity = initQuantity;
		sold = 0;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public double getPrice() {
		return price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getSold() {return sold;}
	// Returns the total revenue (price * amount) if there are at least amount items
	// in stock
	// Return 0 otherwise (i.e., there is no sale completed)
	public double sellUnits(int amount) {
		if (amount > 0 && quantity >= amount) {
			quantity -= amount;
			sold +=amount;
			return price * amount;
		}
		return 0.0;
	}
	
	public static Comparator<Product> getPolularComparator() {
		return new Comparator<Product> () {
			@Override
			public int compare(Product product1, Product product2) {
				return product2.getSold() - product1.getSold();
			}
		};
	}
}