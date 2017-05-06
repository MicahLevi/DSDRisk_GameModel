import java.util.Map;

public class GameState {
	
	private Map<String, Army> 	army_distribution;
	private Player[] 			players;
	private long 				game_id;
	private int 				player_turn;
	private GamePhase 			game_phase;
	private int 				winner;
	private Card[] 				deck;
	
	private class Army {
		int owner_id;
		int num_armies;
		
		public Army(int player_id, int armies) {
			owner_id   = player_id;
			num_armies = armies;
		}
	}
	
	public GameState(long id, Player[] playerArray) {

	}
	
	public void addArmy(int country_id, int num_units) {
		
	}
	
	public void attackCountry(int attack_id, int defend_id, int num_units) {
		
	}
	
	public void fortifyCountry(int from_id, int to_id, int num_units) {
		
	}
	
	public void tradeCards(Card[] cards) {
		
	}
	
}

enum GamePhase{DEPLOY, ATTACK, REINFORCE};