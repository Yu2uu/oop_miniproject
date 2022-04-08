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
  
  // Overriding print function to output text
  public void print(String text){
    infoArea.setText(text);
  }  

  /**  
   *  CHANGE CRYPTO APP NAME TO CURRENCY APP -- Done
   * 
   * TODO ADD CURRENCY INFO INTO A OBJECT TO REDUCE LOAD TIMES, DO OVERRIDING AND INHERITANCE --
   * 
   * TODO ADD BUTTONS UNDER OUTPUT WINDOW TO SELECT INFO WANTED
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

  public void add_currency(String ticker) {
    CryptoCurrency new_coin = new CryptoCurrency(ticker.toUpperCase());
    current_user.get_currencies_list().add(new_coin);
    load_currencies();
  }

  public void load_currencies(){
    toolbar.removeAll();
    for (int i = 0; i < current_user.get_currencies_list().size(); i++) {
      int currency_index = i; // needed for arraylist get as it needs a effectivly final varaible
      Button currency = new Button(current_user.get_currencies_list().get(currency_index).get_currency_ticker());
      currency.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          // TODO replace coin name
          CryptoCurrency coin = current_user.get_currencies_list().get(currency_index);
          selected_coin = coin;
          print("The currency is " + coin.get_name() + "\nSelect the options below for more information.");            
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
    current_user.add_currency(selected_coin = new CryptoCurrency("BTC")); // TODO PRBLM HERE
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

    load_currencies();

    Button refresh = new Button("Refresh info");
    refresh.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          load_currencies();
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
    current_user.set_information();
    for ( String information : current_user.get_information()){
      switch (information){
        case "price":
          Button price = new Button("Price");
          price.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("1 " + selected_coin.get_name() + " is equivalent to £" + selected_coin.get_price());
            }
          });
          info_panel.add(price);
          break;
        case "marketcap":
          Button marketcap = new Button("Market cap");
          marketcap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The market cap of " + selected_coin.get_name() + " is currently " + selected_coin.get_marketcap());
            }
          });
          info_panel.add(marketcap);
          break;
        case "circulating_supply":
          Button supply = new Button("Circulating supply");
          supply.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              print("The current circulating supply of " + selected_coin.get_name() + " is currently " + selected_coin.get_circulating_supply());
            }
          });
          info_panel.add(supply);
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
