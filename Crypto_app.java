import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class Crypto_app extends Frame{
  private Panel info_panel;
  private Panel toolbar;
  private static TextArea infoArea = new TextArea("CyptoTrading App \n- Select a currency to veiw information about it \n - More information is avaialbe to premium users");
  private user User;
  private String account_type;
  
    // Overriding print function to output text
    public static void print(String text){
      infoArea.setText(text);
      }  

    /**  
     * TODO CHANGE CRYPTO APP NAME TO CURRENCY APP
     * 
     * TODO ADD CURRENCY INFO INTO A OBJECT TO REDUCE LOAD TIMES, DO OVERRIDING AND INHERITANCE --
     * 
     * TODO ADD BUTTONS UNDER OUTPUT WINDOW TO SELECT INFO WANTED
     * 
     * TODO ADD REFRESH BUTTON TO GET UP TO DATE INFO
     * 
     * TODO MAYBE MAKE IT SO ONLY ONE BUTTON IS NEEDED AND IS LOOPED THROUGH FOR ALL CURRENCIES
     * 
     * TODO ADD A ADD CURRENCY BUTTON 
    */
    public static JSONObject send_request(String params)throws IOException, InterruptedException, JSONException{
      JSONObject data = api_get.send_request(params);
      return data;
  }

    // MD5 Hash a string to keep it secret
    public static String hash(String text){
      try {
        MessageDigest msgDst = MessageDigest.getInstance("MD5");  
        // the digest() method is invoked to compute the message digest  
        // from an input digest() and it returns an array of byte  
        byte[] msgArr = msgDst.digest(text.getBytes());  
          
        // getting signum representation from byte array msgArr  
        BigInteger bi = new BigInteger(1, msgArr);  
          
        // Converting into hex value  
        String hshtxt = bi.toString(16);  
          
        while (hshtxt.length() < 32) {  
          hshtxt = "0" + hshtxt;  
        }  
        return hshtxt; }
        catch (NoSuchAlgorithmException error)   
        {  
          throw new RuntimeException(error);  
        }  
      }
    
    public static String inputString(String message) {
      System.out.println(message);
      Scanner scanner = new Scanner(System.in);
      String answer = scanner.nextLine();
        
      return answer;
    }
    // TODO MAKE WORK
    public void add_currency(String ticker) {
      crypto_currency coin = new crypto_currency(ticker);
      Button btn = new Button(ticker);	
      btn.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          print("The currency is " + coin.get_name() + " \n BTC to GBP: £" + coin.get_price());
        }
      });
      toolbar.add(btn);	
      this.setVisible(true);
    }
    /**
     * Enter class constructer below with frame builder
     */
    public Crypto_app(){
      this.setLayout(new FlowLayout());

        // window thing = new window();
        // thing.activate(); will do this once base is done
      int account_creation = Integer.parseInt(inputString("Do you have a account with us? \n Input 1 for Yes and 0 for No")); 
      if (account_creation == 0){
        for (boolean taken = true; taken == true;){
          String username = inputString("What is your username");
          if (fileIO.fileSearch("credentials.csv", username)){
            System.out.println("Sorry that username is already taken can you choose another");
            taken = true;
          } else {
            taken = false;
            user User = new user(username);
            String password = inputString("What is your password");
            // User.setPassword(password);
            password = hash(password);
            // Keep asking for account type till valid response is entered
            do {String account_type = inputString("What is your account type (normal/premium)");} 
            while (!(account_type.equals("normal") || account_type.equals("premium")));
            String data = "\n Username: " + username + " Password: " + password + " Account Type: " + account_type;
            fileIO.writeFile("credentials.csv", data);
          }
        }
      } 
      else {
        boolean valid = false;
        while(valid == false){
          // Verify client details
          String username = inputString("What is your username");
          if (fileIO.fileSearch("credentials.csv", username)){
            String password = inputString("What is your password");
            if (fileIO.fileSearch("credentials.csv", username + " Password: " + hash(password))){
              System.out.println("Login successful");
              // TODO sort out this user shit
              User = new user(username);
              User.add_currency(new crypto_currency("BTC")); // TODO PRBLM HERE
              User.add_currency(new crypto_currency("ETH"));
              User.add_currency(new crypto_currency("XRP"));
              valid = true;
            } else {
              System.out.println("Login unsuccessful");
            }
          } else {
            System.out.println("Sorry that username was not found, please retry");
          }
        }
      }
      System.out.println();
      toolbar = new Panel();
      toolbar.setLayout(new FlowLayout());
      toolbar.setVisible(true);
      this.add(toolbar);

      Button addCurrencyButton=new Button("Add Crypto Currency");
      addCurrencyButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt) {		
          Prompt acp = new Prompt();
          Label banner = new Label("Input currency ticker (eg BNB, LANA)");
          TextField field = new TextField(); 
          Label empty = new Label(""); 
          // acp.setLayout(new FlowLayout());
          acp.setSize(300,200);
          acp.add(banner);
          acp.add(empty); 
          acp.add(field);
          acp.addSubmitListener(new ActionListener(){
              public void actionPerformed(ActionEvent evt) {
                String inputString = field.getText();
                add_currency(inputString);
              }
            }
          );
          acp.activate();
        }
      });
      this.add(addCurrencyButton);
      
      for (int i = 0; i < User.get_currencies_list().size(); i++) {
        int currency_index = i; // needed for arraylist get as it needs a effectivly final varaible
        Button currency = new Button(User.get_currencies_list().get(currency_index).get_currency_ticker());
        currency.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            // TODO replace coin name
            crypto_currency coin = User.get_currencies_list().get(currency_index);
            // Get information from the api and parse it 
            // JSONObject data = send_request("ids=BTC&interval=1h&convert=GBP");
            // double price = Double.parseDouble(data.getString("price"));
            // Rounding price to 2 decimal places
            // price = Math.round(price * 100.0) / 100.0;
            print("The currency is " + coin.get_name() + " \n BTC to GBP: £" + coin.get_price());
            
          }
        });
        toolbar.add(currency);
      }

      // Button Etherium = new Button("ETH");
      // Etherium.addActionListener(new ActionListener(){
      //   public void actionPerformed(ActionEvent evt){
      //     try {
      //       // Get information from the api and parse it 
      //       JSONObject data = send_request("ids=ETH&interval=1h&convert=GBP");
      //       double price = Double.parseDouble(data.getString("price"));
      //       // Rounding price to 2 decimal places
      //       price = Math.round(price * 100.0) / 100.0;
      //       print("ETH to GBP: £" + price);
      //     } catch (Exception e) {
      //       e.printStackTrace();
      //     }
      //   }
      // });
      //   toolbar.add(Etherium); 

      // Button Ripple = new Button("XRP");
      // Ripple.addActionListener(new ActionListener(){
      //   public void actionPerformed(ActionEvent evt){
      //     try {
      //       // Get information from the api and parse it 
      //       JSONObject data = send_request("ids=XRP&interval=1h&convert=GBP");
      //       double price = Double.parseDouble(data.getString("price"));
      //       // Rounding price to 2 decimal places
      //       price = Math.round(price * 100.0) / 100.0;
      //       print("XRP to GBP: £" + price);
      //     } catch (Exception e) {
      //       e.printStackTrace();
      //     }
      //   }
      // });
      // toolbar.add(Ripple);
      infoArea.setEditable(false);
		  this.add(infoArea,  BorderLayout.PAGE_END);	

      WindowCloser wc = new WindowCloser();
      this.addWindowListener(wc);
      this.setSize(600,500);// Self explanatory
      this.setLocationRelativeTo(null); // Centers the window on the screen
      this.setVisible(true);// Self explanatory

      info_panel = new Panel();
      info_panel.setLayout(new GridLayout(0,1));
      info_panel.setVisible(true);
      
      this.add(info_panel);
        
    }

    public static void main(String[] args){
		new Crypto_app();
    }
}
