import java.util.Collection;
import java.util.Map;

public class GameState {
	
	private Map<Integer, Army> 	army_distribution; //this should be int if we are passing ints in
	private Player[] 			players;
	private long 				game_id;
	private int 				player_turn;
	private int 				game_phase;
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
		Army locArmy = army_distribution.get(country_id);
		locArmy.num_armies += num_units;
		army_distribution.replace(country_id, locArmy);
	}
	
	public void attackCountry(int attack_id, int defend_id, int num_units) {
		
	}
	
	/**
	 * fortifies territories. checks for issues as well as a feature
	 * returns:

	 * 
	 * @param from_id
	 * @param to_id
	 * @param num_units
	 * @return 1 = successful. 
	 * -1 = fortifying to wrong territory
	 * -2 = too many units requested to move
	 */
	public int fortifyCountry(int from_id, int to_id, int num_units) {
		Army fromArmy = army_distribution.get(from_id);
		Army toArmy = army_distribution.get(from_id);
		if(fromArmy.owner_id == toArmy.owner_id)
		{
			if((fromArmy.num_armies-num_units)<1)
				return -2;
			else
			{
				fromArmy.num_armies -= num_units;
				toArmy.num_armies += num_units;
				army_distribution.replace(from_id, fromArmy);
				army_distribution.replace(to_id, toArmy);
				return 1;
			}
		}
		return -1;
	}
	
	/**
	 * removes selected from player
	 * 
	 * @param cards
	 * @param playerId
	 */
	public void tradeCards(Card[] cards, int playerId) {
		for(Card c: cards){
			players[playerId].removeCard(c);
		}
		
	}
	/**
	 * checks if someone owns all the territories
	 * 
	 * @return id of winner if true,
	 * 			-1 if no winner
	 */
	public int winCheck() {
		int owner = -1;
		Collection<Army> armies = army_distribution.values();
		Army[] arr = (Army[]) armies.toArray();
		for(Army army: arr) {
			if(owner == -1)
				owner = army.owner_id;
			else
			{
				if(owner != army.owner_id)
					return -1;
			}
		}
		winner = owner;
		return owner;
	}
}

//enum GamePhase{DEPLOY, ATTACK, REINFORCE};