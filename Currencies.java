/**
 * A abstract class used so the implemenation of fiat and cryptocurrencies can be done easier 
 * Fiat currencies such as USD and EUR not currently supported.
 */
public abstract class currencies {
    protected String currency_ticker;
    protected String name;
    protected Double price; // currencies price against GBP
    protected String marketcap; // currencies marketcap
    protected String circulating_supply;

    public String get_name(){
        return this.name;
    }

    public String get_currency_ticker(){
        return this.currency_ticker;
    }

    public Double get_price(){
        return this.price;
    }

    public String get_marketcap(){
        return this.marketcap;
    }

    public String get_circulating_supply(){
        return this.circulating_supply;
    }
}
