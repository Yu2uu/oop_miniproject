import java.util.ArrayList;
import org.json.JSONArray;

public class User {
    private String name; // might remove name from class
    // private String password;
    private ArrayList<String> information = new ArrayList<String>();
    private ArrayList<CryptoCurrency> currency_list = new ArrayList<CryptoCurrency>();

    public User(String name){
	    this.name = name;
        this.information = new ArrayList<String>();
    }

    public String getName(){
	    return this.name;
    }

    public ArrayList<String> get_information(){
        return this.information;
    }

    public ArrayList<CryptoCurrency> get_currencies_list() {
        return this.currency_list;
    }

    // Used to create buttons 
    public void set_information(){
        information.add("currency_ticker");
        information.add("name");
        information.add("price");
        information.add("marketcap");
        information.add("circulating_supply");
    }

    public void add_currency(CryptoCurrency currency){
        this.currency_list.add(currency);
    }
    
}
