import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// class gui{
//     public static void main(String args[]){
//        JFrame frame = new JFrame("My First GUI");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(900,600);
//        JPanel panel1 = new JPanel();
//        panel1.setLayout( new BorderLayout() );
//        JPanel subPanel = new JPanel();
//        JButton button1 = new JButton("Press");
//        JButton button2 = new JButton("Stocks");
//        subPanel.add(button1);
//        subPanel.add(button2);  // Adds Button to content pane of frame
//        panel1.add(subPanel, BorderLayout.PAGE_START);
//        frame.getContentPane().add(panel1);
//        frame.setVisible(true);
//     }
// }

public class window extends Frame{

    private Button submit;
    
    public window(){	
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
