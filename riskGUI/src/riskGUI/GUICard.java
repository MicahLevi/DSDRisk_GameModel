package riskGUI;

public class GUICard {
	private int cardID;//will be used for image name etc
	private int type;//there are four types man, horse, cannon, wild
	private int ownerID;//dont know if ill need this
	
	public GUICard(int id, int t){cardID = id;type = t;}
	
	public void setOwnerID(int id){ownerID = id;} //people take othe peoples cards
	
	public int geID(){return cardID;}
	public int getType(){return type;}
	public int getOwnerID(){return ownerID;}

}

	
	
	
	