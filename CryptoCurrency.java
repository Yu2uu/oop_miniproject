import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class CryptoCurrency extends Currency{
    private JSONObject data;
    // Ticker is the shorthand for a currency such as GBP or BTC
    public CryptoCurrency(String ticker) {
        // Get information from the api and parse it 
        JSONObject data;
        try {
            data = ApiAccess.send_request("ids=" + ticker + "&interval=1h&convert=GBP");
            double price = Double.parseDouble(data.getString("price"));
            // Rounding price to 2 decimal places
            price = Math.round(price * 100.0) / 100.0;
            this.currency_ticker = ticker;
            this.price = price;
            this.name = data.getString("name");
            // Had to treat marketcap and supply as string because the api returned none parseable characters 
            this.marketcap = data.getString("market_cap"); 
            this.circulating_supply = data.getString("circulating_supply");
            this.data = data;
        } catch (IOException | InterruptedException | JSONException e) {
            // Error occured
            e.printStackTrace();
        }
    }
}
