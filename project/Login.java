import java.awt.*;
import java.awt.event.*;
public class Login extends Prompt {
    
    private Panel panel = new Panel();
    public Login(){	
		this.setLayout(new java.awt.FlowLayout(10,40,20));
        panel.setLayout(new GridLayout(0,1, 5, 5));
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt) {
				((Frame)(evt.getSource())).dispose();
			}
		});	
    }

    public void closeWindow() {    
        dispose();    
    }    

    public void activate(){	
        this.setBackground(new Color(217, 217, 217));
        this.add(panel);
        this.setSize(300, 300);
		this.setLocationRelativeTo(null); // Centers the window on the screen
		this.setVisible(true);
    }
}
