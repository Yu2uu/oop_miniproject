/**
 * A abstract class used so the implemenation of fiat and cryptocurrencies can be done easier 
 * Fiat currencies such as USD and EUR not currently supported.
 */
public abstract class Currency{
    protected String currency_ticker;
    protected String name;
    protected Double price; // currencies price against GBP
    protected String marketcap; // currencies marketcap
    protected String priceChange;
    

    public String getName(){
        return this.name;
    }

    public String getCurrencyTicker(){
        return this.currency_ticker;
    }

    public Double getPrice(){
        return this.price;
    }

    public String getMarketcap(){
        return this.marketcap;
    }

    public String getPriceChange(){
        return this.priceChange;
    }
}
