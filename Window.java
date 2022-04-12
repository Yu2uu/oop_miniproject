import java.awt.*;
import java.awt.event.*;


public class Window extends Frame{

    private Button submit;
    
    public Window(){	
		this.setLayout(new GridLayout(3,2));
		submit = new Button("Submit");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt) {
				((Frame)(evt.getSource())).dispose();
			}
		});	
    }

    public void addSubmitListener(ActionListener listener){
		submit.addActionListener(listener);
    }    

    public void activate(){	
        this.add(submit);
		this.pack(); // Resizes to tightly fit all its components
		this.setLocationRelativeTo(null); // Centers the window on the screen
		this.setVisible(true);
    }
}
