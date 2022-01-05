import java.util.ArrayList;

public class Restaurant {

    ArrayList<Customer> seatedLine;

    public Restaurant(){
         seatedLine = new ArrayList<>();
    }

    public void enqueue(Customer c){ seatedLine.add(seatedLine.size(),c);
    }

    public Customer dequeue(){
        Customer c = seatedLine.get(0);
        seatedLine.remove(0);
        return c;
    }

    public Customer peek(){
        return seatedLine.get(0);
    }

    public int size(){
        return seatedLine.size();
    }

    public boolean isEmpty(){
        return seatedLine.size() == 0;
    }

    public String toString(){
        String [] foodList = {"Cheeseburger","Steak","Grilled Cheese","Chicken Tenders","Chicken Wings"};
        String response = " {";
        if(seatedLine.size() != 0){
            for (int i = 0; i < seatedLine.size(); i++){
//                System.out.println(i);
//                System.out.println(seatedLine);
//                System.out.println(seatedLine.get(i).getOrderNumber());
                response += "[#"+seatedLine.get(i).getOrderNumber()+", "
                        +seatedLine.get(i).getFood()+", "
                        +seatedLine.get(i).getTimeToServe()+" min. ]";
                if(i != seatedLine.size() -1){
                    response += ", ";
                }
            }
            response += "}";
            return response;
        }
        return "";
    }

}