import java.awt.Color;
import javax.swing.JButton;

public class GUICountry {

	//name,cont,but,id,armies,own
	private JButton button;
	private int id;
	private int armies = 0;
	private Color col;
	
	//default constructor
	public GUICountry(JButton b, int ID){
		button = b;
		id = ID;
	}
	
	//test constructor
	public GUICountry(Color inColor, int a){
		col = inColor;
		armies = a;
	}
	
	public void setButton(JButton inButton){button = inButton;}
	public void setArmies(int a){String setA = Integer.toString(a);	this.button.setText(setA);}
	
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
	
	public void setButtonColor(Color newColor){
		this.button.setBackground(newColor);
		this.col = newColor;
	}
	
	public Color getButtonColor(){return col;}
	public int getID(){return this.id;}
	public int getArmies(){return this.armies;}

}
