import java.util.ArrayList;

public class premium_user extends user {

    private String name; // might remove name from class
    private ArrayList<String> information;

    public premium_user(String name){
        super(name);
    }

    public void set_information(){
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
    }
    

