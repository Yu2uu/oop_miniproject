import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyApp extends Frame{
  private Panel info_panel;
  private Panel toolbar;
  private static TextArea infoArea = new TextArea("CyptoTrading App \n- Select a currency to veiw information about it \n - More information is avaialbe to premium users");
  private User current_user;
  
    // Overriding print function to output text
    public static void print(String text){
      infoArea.setText(text);
      }  

    /**  
     *  CHANGE CRYPTO APP NAME TO CURRENCY APP -- Done
     * 
     * TODO ADD CURRENCY INFO INTO A OBJECT TO REDUCE LOAD TIMES, DO OVERRIDING AND INHERITANCE --
     * 
     * TODO ADD BUTTONS UNDER OUTPUT WINDOW TO SELECT INFO WANTED
     * 
     * TODO ADD REFRESH BUTTON TO GET UP TO DATE INFO
     * 
     *  MAYBE MAKE IT SO ONLY ONE BUTTON IS NEEDED AND IS LOOPED THROUGH FOR ALL CURRENCIES -- Done
     * 
     *  ADD A ADD CURRENCY BUTTON -- Done
     * 
     * TODO ADD VALIDATION TO ADD CURRENCY BUTTON
     * 
     *  RENAME CLASSES TO FOLLOW GUIDLINES -- Done
     * 
     *  HANDLE EXCEPTIONS -- Done
    */
    public static JSONObject send_request(String params)throws IOException, InterruptedException, JSONException{
      JSONObject data = ApiAccess.send_request(params);
      return data;
    }

    // MD5 Hash a string to keep stored passwords secret
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
        return hshtxt; 
      }
      catch (NoSuchAlgorithmException error) {  
        throw new RuntimeException(error);  
      }  
    }
    
    public static String inputString(String message) {
      System.out.println(message);
      Scanner scanner = new Scanner(System.in);
      String answer = scanner.nextLine(); 
      return answer;
    }

    public void add_currency(String ticker) {
      CryptoCurrency coin = new CryptoCurrency(ticker);
      Button btn = new Button(ticker);	
      btn.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          print("The currency is " + coin.get_name() + " \n " + ticker + " to GBP: £" + coin.get_price());
        }
      });
      toolbar.add(btn);	
      this.setVisible(true);
    }

    /**
     * Enter class constructer below with frame builder
     */
    public CurrencyApp(){
      this.setLayout(new FlowLayout());

        // window thing = new window();
        // thing.activate(); will do this once base is done
      String account_creation, account_type = "";
      do {account_creation = inputString("Do you have a account with us? \n Input Yes or No").toUpperCase();}
      while(!(account_creation.equals("YES") || account_creation.equals("NO")));
      while(true){
        if (account_creation.equals("NO")){
          String username = inputString("What is your username");
          if (FileIO.fileSearch("credentials.csv", username)){
            System.out.println("Sorry that username is already taken can you choose another");
            continue;
          } else {
            current_user = new User(username);
            String password =  hash(inputString("What is your password"));

            // Keep asking for account type till valid response is entered
            do {account_type = inputString("What is your account type (normal/premium)");} 
            while (!(account_type.equals("normal") || account_type.equals("premium")));

            String credentials = "\n Username: " + username + " Password: " + password + " Account Type: " + account_type;
            FileIO.writeFile("credentials.csv", credentials);
            System.out.println("\nLogging in");
            break;
          }
        
        } else if (account_creation.equals("YES")) {
          // Verify client details
          String username = inputString("What is your username");
          if (FileIO.fileSearch("credentials.csv", username)){
            String password = inputString("What is your password");
            if (FileIO.fileSearch("credentials.csv", username + " Password: " + hash(password))){
              System.out.println("Login successful");
              current_user = new User(username);
              // TODO sort out this user shit
              break;
            } else {
              System.out.println("Login unsuccessful");
            }
          } else {
            System.out.println("Sorry that username was not found, please retry");
          }
        }
      }
      
      // Create initial buttons
      current_user.add_currency(new CryptoCurrency("BTC")); // TODO PRBLM HERE
      current_user.add_currency(new CryptoCurrency("ETH"));
      current_user.add_currency(new CryptoCurrency("XRP"));

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
      
      for (int i = 0; i < current_user.get_currencies_list().size(); i++) {
        int currency_index = i; // needed for arraylist get as it needs a effectivly final varaible
        Button currency = new Button(current_user.get_currencies_list().get(currency_index).get_currency_ticker());
        currency.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            // TODO replace coin name
            CryptoCurrency coin = current_user.get_currencies_list().get(currency_index);
            print("The currency is " + coin.get_name() + " \n BTC to GBP: £" + coin.get_price());
            
          }
        });
        toolbar.add(currency);
      }

      infoArea.setEditable(false);
		  this.add(infoArea,  BorderLayout.PAGE_END);	

      WindowCloser wc = new WindowCloser();
      this.addWindowListener(wc);
      this.setSize(600,500);
      this.setLocationRelativeTo(null); // Centers the window on the screen
      this.setVisible(true);

      info_panel = new Panel();
      info_panel.setLayout(new GridLayout(0,1));
      info_panel.setVisible(true);
      
      this.add(info_panel);
        
    }

    public static void main(String[] args){
		new CurrencyApp();
  }
}
