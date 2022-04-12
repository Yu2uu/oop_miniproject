import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class CryptoCurrency extends Currency{
    private JSONObject data;
    private String circulating_supply;
    private String highest;
    
    // Ticker is the shorthand for a currency such as GBP or BTC
    public CryptoCurrency(String ticker) {
        // Get information from the api and parse it into variables

        // a loop so that too many requests arent sent to the server, free api only allows 1 request per sec
        // After 5 loops it returns that the request was incorrect
        for (int i = 0; i < 5; i++) {
            try {
                this.data = ApiAccess.sendRequest("ids=" + ticker + "&interval=1h&convert=GBP");
                double price = Double.parseDouble(data.getString("price"));

                // Rounding price to 2 decimal places
                //price = Math.round(price * 100.0) / 100.0; 
                
                this.currency_ticker = ticker;
                this.price = price;
                this.name = data.getString("name");

                // Had to treat marketcap and supply as string because the api returned none parseable characters 
                this.marketcap = data.getString("market_cap"); 
                this.circulating_supply = data.getString("circulating_supply");
                this.highest = data.getString("high");
                break;
            } 
            catch (IOException | InterruptedException e) {
                // Error occured
                continue;
            }
            catch (JSONException e){
                
            }
        } 
              
    }

    public String getCirculatingSupply(){
        return this.circulating_supply;
    }

    public String getHighest(){
        return this.highest;
    }
    
}
