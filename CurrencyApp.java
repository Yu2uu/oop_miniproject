import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CurrencyApp extends Frame{
  private Panel info_panel;
  private Panel toolbar;
  private static TextArea infoArea = new TextArea("CyptoTrading App \n- Select a currency to veiw information about it \n - More information is avaialbe to premium users");
  private User current_user;
  private CryptoCurrency selected_coin;
  private String username;
  private String password;
  
  // Overriding print function to output text
  public void print(String text){
    infoArea.setText(text);
  }  

  /**  
   *  CHANGE CRYPTO APP NAME TO CURRENCY APP -- Done
   * 
   *  ADD CURRENCY INFO INTO A OBJECT TO REDUCE LOAD TIMES, DO OVERRIDING AND INHERITANCE -- Done
   * 
   *  ADD BUTTONS UNDER OUTPUT WINDOW TO SELECT INFO WANTED -- Done
   * 
   *  ADD REFRESH BUTTON TO GET UP TO DATE INFO -- DONE
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
   * 
   * TODO HANDLE EXCEPTION FOR INCORRECT ADDED CURRENCY
   * 
   * TODO ADD CURRENCY ARRAYLIST TO CREDENTIALS SAVE FILE
  */

  // MD5 Hash a string to keep stored passwords secret
  public String hash(String text){
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
  
  public String inputString(String message) {
    System.out.println(message);
    Scanner scanner = new Scanner(System.in);
    String answer = scanner.nextLine(); 
    return answer;
  }

  public void addCurrency(String ticker) {
    CryptoCurrency new_coin = new CryptoCurrency(ticker.toUpperCase());
    current_user.getCurrencyList().add(new_coin);
    loadCurrencies();
  }

  public void loadCurrencies(){
    toolbar.removeAll();
    for (int i = 0; i < current_user.getCurrencyList().size(); i++) {
      int currency_index = i; // needed for arraylist get as it needs a effectivly final varaible
      Button currency = new Button(current_user.getCurrencyList().get(currency_index).getCurrencyTicker());
      currency.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          // TODO replace coin name
          CryptoCurrency coin = current_user.getCurrencyList().get(currency_index);
          selected_coin = coin;
          print("The currency is " + coin.getName() + "\nSelect the options below for more information.");            
        }
      });
      toolbar.add(currency);
    }
    this.setVisible(true); 
  }
  
  public CurrencyApp(){
    this.setLayout(new FlowLayout());

    String account_creation, account_type = "";
    do {account_creation = inputString("Do you have a account with us? \n Input Yes or No").toUpperCase();}
    while(!(account_creation.equals("YES") || account_creation.equals("NO")));
    while(true){
      if (account_creation.equals("NO")){
        username = inputString("What is your username");
        if (FileIO.fileSearch("credentials.csv", username)){
          System.out.println("Sorry that username is already taken can you choose another");
          continue;
        } else {
          password =  hash(inputString("What is your password"));

          // Keep asking for account type till valid response is entered
          do {account_type = inputString("What is your account type (normal/premium)");} 
          while (!(account_type.equals("normal") || account_type.equals("premium")));

          String credentials = "\n Username: " + username + " Password: " + password + " Account-Type: " + account_type;
          FileIO.writeFile("credentials.csv", credentials);
          System.out.println("\nLogging in");
          break;
        }
      
      } else if (account_creation.equals("YES")) {
        // Verify client details by searching credentials file for them
        username = inputString("What is your username");
        if (FileIO.fileSearch("credentials.csv", username)){
          password = inputString("What is your password");
          if (FileIO.fileSearch("credentials.csv", username + " Password: " + hash(password))){
            System.out.println("Login successful");
            break;
          } else {
            System.out.println("Login unsuccessful");
          }
        } else {
          System.out.println("Sorry that username was not found, please retry");
        }
      }
    }

    if (FileIO.fileSearch("credentials.csv", "Username: " + username + " Password: " + hash(password) + " Account-Type: premium")){
      current_user = new PremiumUser(username);
    } else {
      current_user = new User(username);
    }
    
    // Create initial buttons
    current_user.addCurrency(selected_coin = new CryptoCurrency("BTC")); // TODO PRBLM HERE
    current_user.addCurrency(new CryptoCurrency("ETH"));
    current_user.addCurrency(new CryptoCurrency("XRP"));
    
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
              addCurrency(inputString);
            }
          }
        );
        acp.activate();
      }
    });
    this.add(addCurrencyButton);

    loadCurrencies();

    Button refresh = new Button("Refresh info");
    refresh.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          loadCurrencies();
        }
    });
    this.add(refresh);

    infoArea.setEditable(false);
    this.add(infoArea,  BorderLayout.PAGE_END);	

    // Info buttons panel
    info_panel = new Panel();
    info_panel.setLayout(new GridLayout(0,1));
    info_panel.setVisible(true);
    this.add(info_panel, BorderLayout.PAGE_END);
    
    current_user.setInformation();

    for ( String information : current_user.getInformation()){
      switch (information){
        case "price":
          Button price = new Button("Price");
          price.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("1 " + selected_coin.getName() + " is equivalent to £" + selected_coin.getPrice());
            }
          });
          info_panel.add(price);
          break;
        case "marketcap":
          Button marketcap = new Button("Market cap");
          marketcap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The market cap of " + selected_coin.getName() + " is currently " + selected_coin.getMarketcap());
            }
          });
          info_panel.add(marketcap);
          break;
        case "circulating_supply":
          Button supply = new Button("Circulating supply");
          supply.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The current circulating supply of " + selected_coin.getName() + " is currently " + selected_coin.getCirculatingSupply());
            }
          });
          info_panel.add(supply);
          break;
        case "price_changes":
          Button price_changes = new Button("Price Changes");
          price_changes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The price of 1 " + selected_coin.getName() + " coin has changed by " + selected_coin.getCirculatingSupply() + " in the last hour");
            }
          });
          info_panel.add(price_changes);
          break;
        case "marketcap_changes":
          Button marketcap_changes = new Button("Marketcap Changes");
          marketcap_changes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The market cap of" + selected_coin.getName() + " is " + selected_coin.getMarketcap());
            }
          });
          info_panel.add(marketcap_changes);
          break;
        case "high":
          Button high = new Button("High");
          high.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The highest price of 1 " + selected_coin.getName() + " of all time is £" + selected_coin.getHighest());
            }
          });
          info_panel.add(high);
          break;
      }
    }

    WindowCloser wc = new WindowCloser();
    this.addWindowListener(wc);
    this.setSize(600,500);
    this.setLocationRelativeTo(null); // Centers the window on the screen
    this.setVisible(true);

  }

  public static void main(String[] args){
		new CurrencyApp();
  }
}
