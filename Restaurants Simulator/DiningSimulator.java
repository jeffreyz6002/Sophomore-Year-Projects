import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class DiningSimulator {
    private ArrayList <Restaurant> restaurants = new ArrayList<>();
    private ArrayList <Customer> reverseList;
    private int chefs;
    private int duration;
    private double arrivalProb;
    private int maxCustomerSize;
    private int numRestaurants;
    private int customerLost;
    private int totalServiceTime;
    private int customersServed;
    private int profit;

    public DiningSimulator(int numRestaurants, int maxCustomerSize, double arrivalProb, int chefs, int duration){
        this.numRestaurants = numRestaurants;
        this.maxCustomerSize = maxCustomerSize;
        this.arrivalProb = arrivalProb;
        this.chefs = chefs;
        this.duration = duration;
        customerLost = 0;
        totalServiceTime = 0;
        customersServed = 0;
        profit = 0;
    }

    public static void main(String [] args){

        Scanner stdin = new Scanner(System.in);
        boolean tryAgain = true;
        String tryAgainResponse;

        while(tryAgain == true){
            System.out.println("Starting Simulator...");
            System.out.print("Enter the number of restaurants: ");
            int numRestaurants = stdin.nextInt();
            stdin.nextLine();
            System.out.print("Enter the maximum number of customers a restaurant can serve: ");
            int maxCustomerSize = stdin.nextInt();
            stdin.nextLine();
            System.out.print("Enter the arrival probability of a customer: ");
            double arrivalProb = stdin.nextDouble();
            stdin.nextLine();
            System.out.print("Enter the number of chefs: ");
            int chefs = stdin.nextInt();
            stdin.nextLine();
            System.out.print("Enter the number of simulation units: ");
            int duration =  stdin.nextInt();
            stdin.nextLine();
            System.out.println();

            DiningSimulator ds = new DiningSimulator(numRestaurants, maxCustomerSize, arrivalProb, chefs, duration);
            ds.simulate();

            System.out.print("Do you want to try again simulation? (y/n): ");
            tryAgainResponse = stdin.nextLine();
            if(!tryAgainResponse.equals("y")){
                tryAgain = false;
            }
        }

        System.out.println("Program terminating normally...");

        
    }

    public double simulate(){
        String [] foodList = {"Cheeseburger","Steak","Grilled Cheese","Chicken Tenders","Chicken Wings"};
        int [] foodPrice = {15,25,10,10,20};
        int [] foodTime = {25,30,15,25,30};
        double randomNum;
        int customerNumber = 0;
        int foodChoice, extraChefs, subMinutes;
        reverseList = new ArrayList<>();
        Customer c;

        if(chefs > 3){
            extraChefs = chefs - 3;
            subMinutes = extraChefs * 5;
            for(int i = 0; i < foodTime.length; i++){
                foodTime[i] = foodTime[i] - subMinutes;
            }
        }
        
        for(int i = 0; i < numRestaurants; i++){
            restaurants.add(new Restaurant());
        }

        for(int j = 0; j < duration; j++){
            String chosenFoodMsg = "";
            System.out.println("Time: " +(j+1));

            // If TimeToServe <= 0
            // Dequeue to another queue and queue back

                for(int i = 0; i < numRestaurants; i++){
                    if(restaurants.get(i).size() > 0){
                        reverseList = new ArrayList<>();
                        int size = restaurants.get(i).size();
                        for(int k = 0; k < size; k++){ // size never becomes 1
                            if(restaurants.get(i).peek().getTimeToServe() <= 0){
                                System.out.println("Customer #"+restaurants.get(i).peek().getOrderNumber()+ " " +
                                        "has enjoyed their food! $"+restaurants.get(i).peek().getPriceOfFood()+
                                        " profit.");
                                profit += restaurants.get(i).peek().getPriceOfFood();
                                Customer left = restaurants.get(i).dequeue();
                                customersServed++;
                                totalServiceTime += (j*5) - left.getTimeArrived();
                                size--;
                                k--;
                            }
                            else{
                                reverseList.add(restaurants.get(i).dequeue());
                            }
                        }

                        if(reverseList.size() != 0){
                            for(int k = 0; k < reverseList.size(); k++){
                                restaurants.get(i).enqueue(reverseList.get(k));
                            }
                        }
                    }
                }

            for(int i = 0; i < numRestaurants; i++){
                randomNum = Math.random()*1;
                if(arrivalProb >= randomNum){ // Customer Arrives
                    c = new Customer();
                    customerNumber++;
                    foodChoice = randInt(0,4);
                    c.setOrderNumber(customerNumber);
                    c.setFood(foodList[foodChoice]);
                    c.setTimeToServe(foodTime[foodChoice]);
                    c.setPriceOfFood(foodPrice[foodChoice]);
                    c.setTimeArrived(j*5);
                    if(restaurants.get(i).size() < maxCustomerSize){
                        restaurants.get(i).enqueue(c);
                        System.out.println("Customer #"+customerNumber+ " has entered Restaurant "+(i+1) +".");
                        chosenFoodMsg += "Customer #"+customerNumber+ " has been seated with order \"" +foodList[foodChoice]+"\".\n";
                    }
                    else{ // Restaurant is full
                        customerLost++;
                        System.out.println("Customer #"+customerNumber +" cannot be seated at Restaurant " +(i+1)+". They have left the restaurant.");
                    }
                }
                else{ // Customer Does Not Arrive
                    //System.out.println("not arrived");
                }
            }

            System.out.println(chosenFoodMsg);

            // Display Customers Per Restaurant
            for(int i = 0; i < numRestaurants; i++){
                if(!restaurants.get(i).isEmpty()) {
                    System.out.println("R" + (i+1) + ": " + restaurants.get(i).toString());
                }
            }

            // Minus Time Per Loop
            for(int i = 0; i < numRestaurants; i++){
                if(restaurants.get(i).size() != 0) {
                    for(int k = 0; k < restaurants.get(i).size(); k++){
                        restaurants.get(i).seatedLine.get(k).setTimeToServe(restaurants.get(i).seatedLine.get(k).getTimeToServe()-5);
                    }
                }
            }
            System.out.println();
        }

        System.out.println("Simulation ending...");
        System.out.println();

        System.out.println("Total customer time: " +totalServiceTime);
        System.out.println("Total customers served: " +customersServed);
        double timeLapse = (double)totalServiceTime/customersServed;
        timeLapse *= 100;
        int temp = (int) timeLapse;
        timeLapse = temp/100.0;
        System.out.println("Average customer time lapse: " + timeLapse  + " minutes per order");
        System.out.println("Total Profit: $"+profit);
        System.out.println("Customers that left: " +customerLost);
        return timeLapse;
    }

    public int randInt(int minVal, int maxVal){
        return (int)(minVal + Math.random()*(maxVal-minVal+1));
    }

}
