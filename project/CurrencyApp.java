import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CurrencyApp extends Frame{
  private Panel info_panel;
  private Panel toolbar;
  private Panel options;
  private static TextArea infoArea = new TextArea("CyptoTrading App \n- Select a currency to veiw information about it \n - More information is avaialbe to premium users",10 , 35 , TextArea.SCROLLBARS_NONE);
  private User current_user;
  private CryptoCurrency selected_coin;
  private String username;
  private String password;
  
  // Overriding print function to output text
  public void print(String text){
    infoArea.setText(text);
  }  

  /**  
   * 
   * TODO ADD VALIDATION TO ADD CURRENCY BUTTON
   * 
   * TODO ADD NEW INFO BUTTON NORMAL ACCOUNT
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
    TextArea outputConsole = new TextArea("Login here, Click the new account button to create a new account.",2 , 18 , TextArea.SCROLLBARS_NONE);
    outputConsole.setEditable(false);
    outputConsole.setFont(new Font("Monospaced", Font.BOLD, 14));
    Login loginWindow = new Login();
    Label usernameLabel = new Label(" Username");
    TextField usernameField = new TextField(10); 
    Label passwordLabel = new Label(" Password"); 
    TextField passwordField = new TextField(10); 
    loginWindow.add(outputConsole);
    loginWindow.add(usernameLabel);
    loginWindow.add(usernameField);
    loginWindow.add(passwordLabel); 
    loginWindow.add(passwordField);
    loginWindow.setTitle("Login");
    Button login = new Button("Login");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        username = usernameField.getText();
        password = passwordField.getText();
        usernameField.setText("");
        passwordField.setText("");
        if (FileIO.fileSearch("credentials.csv", username) ) {
          if (FileIO.fileSearch("credentials.csv", username + " Password: " + hash(password)) && (!username.equals(""))){
            outputConsole.setText("Logging in...");
            successfulLogin();
            loginWindow.closeWindow();
          } else {
            outputConsole.setText("Login unsuccessful");
          }
        } else {
          outputConsole.setText("Sorry that username was not found, please retry");
        }
			}
		});
    loginWindow.add(login);
    Button newAccountBtn = new Button("New Account");
		newAccountBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        TextArea outputArea = new TextArea("Create account below, the account type options are normal and premium",2 , 18 , TextArea.SCROLLBARS_NONE);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        Login createAccount = new Login();
        Label userLabel = new Label(" Username ");
        TextField newUsernameField = new TextField(10); 
        Label passLabel = new Label(" Password "); 
        TextField newPasswordField = new TextField(10); 
        Label accountLabel = new Label(" Account "); 
        TextField accountTypeField = new TextField(10); 
        createAccount.add(outputArea);
        createAccount.add(userLabel);
        createAccount.add(newUsernameField);
        createAccount.add(passLabel); 
        createAccount.add(newPasswordField);
        createAccount.add(accountLabel);
        createAccount.add(accountTypeField);
        Button newAccount = new Button("Create account");
		    newAccount.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            username = newUsernameField.getText();
            password = newPasswordField.getText();
            String accountType = accountTypeField.getText();
            if (FileIO.fileSearch("credentials.csv", username)){
              outputArea.setText("Sorry that username is already taken can you choose another");
            } else if (password.isEmpty()){
              outputArea.setText("Password field empty, Please enter a value");
            } else if (accountType.equals("normal") || accountType.equals("premium")){  
              // Keep asking for account type till valid response is entered
              String credentials = "\n Username: " + username + " Password: " + hash(password) + " Account-Type: " + accountType;
              FileIO.writeFile("credentials.csv", credentials);
              outputArea.setText("Account created successfully");
              createAccount.closeWindow();
            } else {
              outputArea.setText("Please choose a correct account type");
            }
          }
        });
        createAccount.add(newAccount);
        createAccount.activate();
      }
		});
    loginWindow.add(newAccountBtn);
    loginWindow.activate();
  }

public void successfulLogin(){

  // polymorphism below
  if (FileIO.fileSearch("credentials.csv", "Username: " + username + " Password: " + hash(password) + " Account-Type: premium")){
    current_user = new PremiumUser(username);
  } else {
    current_user = new User(username);
  }
  
  // Create initial buttons
  current_user.addCurrency(selected_coin = new CryptoCurrency("BTC"));
  current_user.addCurrency(new CryptoCurrency("ETH"));
  current_user.addCurrency(new CryptoCurrency("XRP"));
  
  // create main window
  toolbar = new Panel();
  options = new Panel();
  toolbar.setLayout(new FlowLayout());
  toolbar.setVisible(true);
  options.setBackground(new Color(  210, 205, 234  ));
  toolbar.setBackground(new Color( 210, 205, 234 ));
  this.add(toolbar);
  this.add(options);

  Button addCurrencyBtn=new Button("Add Crypto Currency");
  addCurrencyBtn.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent evt) {		
      Prompt addCurrencyWindow = new Prompt();
      Label tickerLabel = new Label("Input currency ticker (eg BNB, ADA)");
      TextField inputTicker = new TextField(); 
      Label empty = new Label(""); 
      addCurrencyWindow.setSize(300,200);
      addCurrencyWindow.add(tickerLabel);
      addCurrencyWindow.add(empty); 
      addCurrencyWindow.add(inputTicker);
      addCurrencyWindow.addSubmitListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt) {
            String inputString = inputTicker.getText();
            addCurrency(inputString);
          }
        }
      );
      addCurrencyWindow.activate();
    }
  });
  options.add(addCurrencyBtn);

  // Load in currencies
  loadCurrencies();

  // Update buttons to contain latest info
  Button refresh = new Button("Refresh info");
  refresh.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        loadCurrencies();
      }
  });
  options.add(refresh);

  infoArea.setEditable(false);
  infoArea.setFont(new Font("SansSerif", Font.BOLD, 20));
  this.add(infoArea, BorderLayout.PAGE_END);	

  // Information buttons panel
  info_panel = new Panel();
  info_panel.setLayout(new GridLayout(0,2, 10, 10));
  info_panel.setBackground(new Color(217, 212, 217));
  info_panel.setVisible(true);
  this.add(info_panel, BorderLayout.PAGE_END);
  
  current_user.setInformation();
  Color buttonColor = new Color(255, 237, 217);

  // THis is a function to create buttons depending on an arraylist
  for ( String information : current_user.getInformation()){
    switch (information){
      case "price":
        Button price = new Button("Price");
        price.setBackground(buttonColor);
        price.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            print("1 " + selected_coin.getName() + " is equivalent to \u00A3" + selected_coin.getPrice());
          }
        });
        info_panel.add(price);
        break;
      case "marketcap":
        Button marketcap = new Button("Market cap");
        marketcap.setBackground(buttonColor);
        marketcap.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            print("The market cap of " + selected_coin.getName() + " is currently " + selected_coin.getMarketcap());
          }
        });
        info_panel.add(marketcap);
        break;
      case "circulating_supply":
        Button supply = new Button("Circulating supply");
        supply.setBackground(buttonColor);
        supply.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            print("The current circulating supply of " + selected_coin.getName() + " is currently " + selected_coin.getCirculatingSupply());
          }
        });
        info_panel.add(supply);
        break;
      case "price_changes":
        Button price_changes = new Button("Price Changes");
        price_changes.setBackground(buttonColor);
        price_changes.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            print("The price of 1 " + selected_coin.getName() + " coin has changed by " + selected_coin.getPriceChange() + " in the last hour");
          }
        });
        info_panel.add(price_changes);
        break;
      case "marketcap_changes":
        Button marketcap_changes = new Button("Marketcap Changes");
        marketcap_changes.setBackground(buttonColor);
        marketcap_changes.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            print("The market cap of " + selected_coin.getName() + " is " + selected_coin.getMarketcap());
          }
        });
        info_panel.add(marketcap_changes);
        break;
      case "high":
        Button high = new Button("High");
        high.setBackground(buttonColor);
        high.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent evt){
            print("The highest price of 1 " + selected_coin.getName() + " of all time is \u00A3" + selected_coin.getHighest());
          }
        });
        info_panel.add(high);
        break;
    }
  }
  this.setTitle("Currency App");
  this.setBackground(new Color(217, 212, 217));
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
