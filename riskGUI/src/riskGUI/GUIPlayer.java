package riskGUI;

import java.util.ArrayList;

public class GUIPlayer {
	private int playerID;
	private ArrayList<GUICard> cards = new ArrayList<GUICard>();
	private ArrayList<GUICountry> countries = new ArrayList<GUICountry>();
	private int armiesLeft = 0;
	private boolean isPlaying;
	private String color;
	
	public GUIPlayer(int ID, String c){playerID = ID; color = c;}
	
	public int GetID(){return playerID;}
	public ArrayList<GUICard> GetCards(){return cards;}
	public ArrayList<GUICountry> GetCountrues(){return countries;}
	public int GetRemArmies(){return armiesLeft;}
	public boolean isPlaying(){return isPlaying;}
	
	public void addCountry(GUICountry c){countries.add(c.getID(),c);}
	public void takeCountry(GUICountry c){countries.remove(c.getID());}
	public boolean doesOwn(GUICountry c){return true;}
	
	public void setRemArmies(int a){armiesLeft = a;}
	public void decRemArmies(){armiesLeft--;}
	public void setIsPlaying(boolean i){isPlaying = i;}
}
