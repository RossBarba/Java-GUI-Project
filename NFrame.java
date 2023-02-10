//Author: Ross Barba 
//Date Published: December 2, 2022

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class NFrame extends JFrame implements ActionListener {
	GData[] data = new GData[0];
	int counter = -1;
	
	JFrame errorFrame;
	JPanel jpBase, jpTop, jpBottom, jpMiddle;
	JLabel jl1, jl2, jl3, jl4;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JButton jb1, jb2;
	JMenuBar mb;
	JMenu a, b;
	JMenuItem a1, a2, b1, b2;
	JTextArea jta;
	
	public static void main(String[] args) {
		NFrame nf = new NFrame();
	}
	NFrame() {
		setTitle("Application 2");
		setSize(900, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		jpBase = new JPanel(); jpTop = new JPanel(); jpBottom = new JPanel(); jpMiddle = new JPanel();
		jl1 = new JLabel("name"); jl2 = new JLabel("red"); jl3 = new JLabel("green"); jl4 = new JLabel("blue");
		jtf1 = new JTextField(6); jtf2 = new JTextField(6); jtf3 = new JTextField(6); jtf4 = new JTextField(6);
		jb1 = new JButton("create"); jb1.addActionListener(this); jb2 = new JButton("list"); jb2.addActionListener(this);
		jta = new JTextArea();
		
		mb = new JMenuBar();
		setJMenuBar(mb);
		
		a = new JMenu("File");
		mb.add(a);
		a1 = new JMenuItem("Save");
		a.add(a1);
		a1.addActionListener(this);
		a2 = new JMenuItem("Load");
		a.add(a2);
		a2.addActionListener(this);
		
		b = new JMenu("View");
		mb.add(b);
		b1 = new JMenuItem("Previous");
		b.add(b1);
		b1.addActionListener(this);
		b2 = new JMenuItem("Next");
		b.add(b2);
		b2.addActionListener(this);
		
		add(jpBase); jpBase.setLayout(new BoxLayout(jpBase, BoxLayout.Y_AXIS));
		
		jpTop.add(jb1); jpTop.add(jb2);
		jpBase.add(jpTop);
		
		jpMiddle.setLayout(new GridLayout(4, 2));
		jpMiddle.add(jl1); jpMiddle.add(jtf1);
		jpMiddle.add(jl2); jpMiddle.add(jtf2);
		jpMiddle.add(jl3); jpMiddle.add(jtf3);
		jpMiddle.add(jl4); jpMiddle.add(jtf4);
		jpBase.add(jpMiddle);

		jpBottom.add(jta);
		jpBase.add(jpBottom);
		
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) {
			try {
				create();
				counter = data.length - 1;
				int red = Integer.parseInt(jtf2.getText());
				int green = Integer.parseInt(jtf3.getText());
				int blue = Integer.parseInt(jtf4.getText());
				Color c = new Color(red, green, blue);
				jpTop.setBackground(c);
			} 
			catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(errorFrame, "Please fill out all fields.");

			}
		}
		if (e.getSource() == jb2) {
			jta.setText("");
			list();
		}
		if (e.getSource() == a1) {
			save();
		}
		if (e.getSource() == a2) {
			load();
			jpTop.setBackground(null);
			counter = -1;
		}
		if (e.getSource() == b1) {
			if (counter == 0){
				JOptionPane.showMessageDialog(errorFrame, "Already displaying first color created.");
			}
			else {
				counter--;
				int red = data[counter].getR();
				int green = data[counter].getG();
				int blue = data[counter].getB();
				Color c = new Color(red, green, blue);
				jpTop.setBackground(c);
			}
		}
		if (e.getSource() == b2) {
			if (counter == data.length - 1){
				JOptionPane.showMessageDialog(errorFrame, "No color to display.");
			}
			else {
				counter++;
				int red = data[counter].getR();
				int green = data[counter].getG();
				int blue = data[counter].getB();
				Color c = new Color(red, green, blue);
				jpTop.setBackground(c);
			}
		}
		
		
	}
	public void add(GData input) {
		GData[] data2 = new GData[this.data.length+1];
		for(int i = 0; i < this.data.length; i++) {
			data2[i] = this.data[i];
		}
		data2[this.data.length] = input;
		this.data = data2;
	}
	
	public void create() throws NullPointerException {
		String name = jtf1.getText();
		int red = Integer.parseInt(jtf2.getText());
		int green = Integer.parseInt(jtf3.getText());
		int blue = Integer.parseInt(jtf4.getText());
		GData temp = new GData(name, red, green, blue);
		add(temp);
	}
	
	public void list() {
		if(this.data==null) System.out.println("null NData object");
		if(this.data.length == 0) System.out.println("no records to display");
		for(int i = 0; i < this.data.length; i++) {
		jta.append("index " + i + " name " + data[i].getName() + " r: " + data[i].getR() + " g: " + data[i].getG() + " b: " + data[i].getB());
		jta.append("\n\r");
		}
		}
	
	void save() {
		try {
			File outFile = new File("colors.dat");
			
			FileOutputStream outFileStream = new FileOutputStream(outFile);
			ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
			
			outObjectStream.writeObject(data);
			
			outObjectStream.close(); 
			outFileStream.close();
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(errorFrame, "Unexpected I/O problem.");

		}
	}
		
	void load() {
		try {
			File inFile = new File("colors.dat");
						
			FileInputStream inFileStream = new 	FileInputStream(inFile);
			ObjectInputStream inObjectStream = new 	ObjectInputStream(inFileStream);
			
			data = (GData[])inObjectStream.readObject();
			
			inFileStream.close();
			inObjectStream.close();
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(errorFrame, "Unexpected I/O problem.");
		}
		catch(ClassNotFoundException cnfe) {
			JOptionPane.showMessageDialog(errorFrame, "Class not found problem");

		}
}

class GData {
	private String name;
	private int r, g, b;
	
	GData(String str, int rValue, int gValue, int bValue) throws NullPointerException{
		this.setName(str);
		try{
			this.setR(rValue);
			this.setG(gValue);
			this.setB(bValue);
		}
		catch (NotRGBCodeException e) {
			JOptionPane.showMessageDialog(errorFrame, "one ore more of the rgb values have an input out of the 0-255 range");
		}
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String input) throws NullPointerException {
		int len = 0;
		
		try {
			len = input.length();
		}
		catch (NullPointerException e){
			JOptionPane.showMessageDialog(errorFrame, "name is null.");
		}
		if (len < 1) JOptionPane.showMessageDialog(errorFrame, "Please enter a name with a valid length");
		if (len >= 1) name = input;
	}
	
	public int getR() {
		return r;
	}
	
	public int getG() {
		return g;
	}
	
	public int getB() {
		return b;
	}
	
	public void setR(int input) throws NotRGBCodeException{
		if (input < 0 || input > 255) throw new NotRGBCodeException();
		if (input > 0 && input <= 255) r = input;
	}
	
	public void setG(int input) throws NotRGBCodeException {
		if (input < 0 || input > 255) throw new NotRGBCodeException();
		if (input >= 0 && input <= 255) g = input;
	}
	
	public void setB(int input) throws NotRGBCodeException {
		if (input < 0 || input > 255) throw new NotRGBCodeException();
		if (input >= 0 && input <= 255) b = input;
	}
	
	class NotRGBCodeException extends Exception {
		
	}
}
}

