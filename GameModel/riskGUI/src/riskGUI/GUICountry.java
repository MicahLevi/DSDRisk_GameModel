package riskGUI;

import java.awt.Color;
import javax.swing.JButton;

public class GUICountry {

	//name,cont,but,id,armies,own
	private String name;
	private int continent;
	private JButton button;
	private int id;
	private int armies = 0;
	private String owner = "";
	private boolean isOwned;
	
	public GUICountry(String n, int c, JButton b, int ID){
		name = n;
		continent = c;
		button = b;
		id = ID;
	}
	
	public void setName(String inName){name = inName; }
	public void setOwner(String inOwner){owner = inOwner;}
	public void setContinent(int inContinent){continent = inContinent;}
	public void setButton(JButton inButton){button = inButton;}
	public void setID(int inID){id = inID;}
	public void setIsOwned(){isOwned = true;}
	public void setArmies(int a){
		String setA = Integer.toString(a);
		this.button.setText(setA);
		//ImageIcon image = new ImageIcon("imgs/"+newColor+".jpg");
		//this.button.setIcon(image);
	}
	
	public void incrementArmies(){
		String buttonText = this.button.getText();
		int buttonTextInt = Integer.parseInt(buttonText);
		buttonTextInt = buttonTextInt + 1;
		String incrementedText = Integer.toString(buttonTextInt);
		this.button.setText(incrementedText);
	}
	public void decrementArmies(){
		String buttonText = this.button.getText();
		int buttonTextInt = Integer.parseInt(buttonText);
		buttonTextInt = buttonTextInt - 1;
		String incrementedText = Integer.toString(buttonTextInt);
		this.button.setText(incrementedText);
		
	}
	
	public void changeButtonColor(Color newColor){
		if(true){//this may be used to identify which buttons color is to change????
			this.button.setBackground(newColor);
		}
	}
	
	public String getName(){return this.name;}
	public String getOwner(){return this.owner;}
	public int getContinent(){return this.continent;}
	public JButton getButton(){return this.button;}
	public int getID(){return this.id;}
	public boolean getIsOwned(){return isOwned;}
	public int getArmies(){return this.armies;}

}
