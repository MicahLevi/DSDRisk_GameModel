import java.util.ArrayList;

public class Player {
	private String  name;
	private int 	id;
	private ArrayList<Card> hand;
	
	public Player(String player_name, int player_id) {
		name = player_name;
		id = player_id;
		hand = new ArrayList<Card>();
	}
	
	public int addCard(Card card){
		hand.add(card);
		return hand.size();
	}
	
	public int removeCard(Card card){
		hand.remove(card);
		return hand.size();
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
}
