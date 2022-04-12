import java.util.ArrayList;

public class PremiumUser extends User{

    private String name; // might remove name from class
    // private ArrayList<String> information = new ArrayList<String>();   QUESTION BELOW

    public PremiumUser(String name){
        super(name);
    }

    @Override
    public void setInformation(){
        information.add("currency_ticker");
        information.add("name");
        information.add("price");
        information.add("marketcap");
        information.add("circulating_supply");
        information.add("rank");
        information.add("price_changes");
        information.add("marketcap_changes");
        information.add("high");
    }


    /**
     * 
     * QUESTION BRUNO -- Should i have the information varaible as a protected variable and use the getInformation method in the superclass
     * or should I leave it a private variable and override the getter to access the information varaible in this class??
     * 
     */
    // @Override
    // public ArrayList<String> getInformation(){
    //     return this.information;
    // }
}
    

