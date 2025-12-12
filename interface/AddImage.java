//Java Program to Add Image in Jframe 
import javax.swing.*;
import java.awt.*;



public class AddImage extends JFrame {
    public static void main(String[] args) {

        JButton button1 = new JButton("One");
        JButton button2 = new JButton("Two");
        JButton button3 = new JButton("Three");
        JButton button4 = new JButton("Four");
        JButton button5 = new JButton("Five");
        JButton button6 = new JButton("Six");
        Icon icon = new ImageIcon("camera.png");
        JButton button7 = new JButton(icon);
        Box box = Box.createVerticalBox();

        box.add(button1);
        box.add(button2);
        box.add(button3);
        box.add(button4);
        box.add(button5);
        box.add(button6);
        box.add(button7);

        JFrame frame = new JFrame(); //JFrame Creation       
        frame.setTitle("Add Image"); //Add the title to frame
        frame.setLayout(null); //Terminates default flow layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminate program on close button

        //Sets the position of the frame
        
        Container c = frame.getContentPane(); //Gets the content layer
        JLabel label = new JLabel(); //JLabel Creation
        label.setIcon(new ImageIcon("house_plant.png")); //Sets the image to be displayed as an icon
        Dimension size = label.getPreferredSize(); //Gets the size of the image
        label.setBounds(0, 0, size.width, size.height); //Sets the location of the image
        frame.setBounds(0, 0, size.width+30, size.height+45); //Sets the location of the image
        
        frame.setLocationByPlatform(true);
 
        c.add(label); //Adds objects to the container
        c.add(box);
        frame.setVisible(true); // Exhibit the frame
    } 
}