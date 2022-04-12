import java.util.ArrayList;

public class User{
    private String name; // might remove name from class
    protected ArrayList<String> information = new ArrayList<String>(); // BRUNO QUESTION ABOUT THIS PRETECTED VARAIBLE IN PREM USER CLASS
    private ArrayList<CryptoCurrency> currency_list = new ArrayList<CryptoCurrency>();

    public User(String name){
	    this.name = name;
    }

    public String getName(){
	    return this.name;
    }

    public ArrayList<String> getInformation(){
        return this.information;
    }

    public ArrayList<CryptoCurrency> getCurrencyList() {
        return this.currency_list;
    }

    // Used to create buttons 
    public void setInformation(){
        information.add("currency_ticker");
        information.add("name");
        information.add("price");
        information.add("marketcap");
        information.add("circulating_supply");
    }

    public void addCurrency(CryptoCurrency currency){
        this.currency_list.add(currency);
    }
    
}
