public class Customer {
    private static int totalCustomers = 0;
    private int orderNumber;
    private int priceOfFood;
    private int timeArrived;
    private int timeToServe;
    private String food;

    public Customer(){
        totalCustomers++;
    }

    public String toString(){
        return "";
    } // combine with toString from Restaurant

    public static int getTotalCustomers() {
        return totalCustomers;
    }

    public int getPriceOfFood() {
        return priceOfFood;
    }

    public String getFood() {
        return food;
    }

    public void setPriceOfFood(int priceOfFood) {
        this.priceOfFood = priceOfFood;
    }

    public int getTimeToServe() {
        return timeToServe;
    }

    public void setTimeToServe(int timeToServe) {
        this.timeToServe = timeToServe;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getTimeArrived() {
        return timeArrived;
    }

    public void setTimeArrived(int timeArrived) {
        this.timeArrived = timeArrived;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public static void setTotalCustomers(int tc) {
        totalCustomers = tc;
    }
}
