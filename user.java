import java.util.ArrayList;

import org.json.JSONArray;

public class user {
    private String name; // might remove name from class
    // private String password;
    private ArrayList<String> information = new ArrayList<String>();
    private ArrayList<crypto_currency> currency_list = new ArrayList<crypto_currency>();

    public user(String name){
	    this.name = name;
        this.information = new ArrayList<String>();
    }

    public String getName(){
	    return this.name;
    }

    public ArrayList<String> get_information(){
        return this.information;
    }

    public ArrayList<crypto_currency> get_currencies_list() {
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

    public void add_currency(crypto_currency currency){
        this.currency_list.add(currency);
    }
    
}
