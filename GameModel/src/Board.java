import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*Object containing information about the setup.
 * Contains information about number of players,
 * rules, win conditions, and card conditions.
 */
public class Board {
	private long 	 id;
	private GameMap  map;
	private Rule[]   rules;
	private Player[] players;
	private int cardTurninNumber;
	private int armyPool;
	
	public Board(long gameId, String mapName, Rule[] gameRules, Player[] gamePlayers) {
		id = gameId;
		
		try {
			map = new GameMap(mapName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = null;
		}
		rules   = gameRules;
		players = gamePlayers;
		cardTurninNumber = 0;
		armyPool = 0;
	}
	
	public boolean territoryIsAdjacent(int ter1_id, int ter2_id) {
		return map.isAdjacent(ter1_id, ter2_id);
	}
	
	public int getCardTurninNumber(){
		return cardTurninNumber;
	}
	public void incrementCardTurninNumber(){
		cardTurninNumber++;
	}
	
	///////////////////////
	public void addArmyPool(int adder){
		armyPool += adder;
	}
	public int getArmyPool(){
		return armyPool;
	}
	public boolean removeFromArmyPool(int remove){
		if(remove>armyPool)
			return false;
		armyPool -= remove;
		return true;
	}
	public void resetArmyPool(){
		armyPool = 0;
	}

	public GameMap getGameMap() {
		return map;
	}
	
	public Rule[] getRules(){
		return rules;
	}
	
	/**
	 * rule for translating card turn in to armies
	 * @return
	 */
	public int getArmiesFromCardTurnIn() {
		//what rule determines this?
		if(rules[0].setting==1)
		{
			incrementCardTurninNumber();
			return 3 * cardTurninNumber;
		}
		else
			return 15;
	}
	
	/**
	 * gets an initial amount of armies for placement. territories*3/players
	 * @return
	 */
	public void setTestInitArmyPool(){
		int pool = 0;
		for(Continent c: map.getConts())
			pool += c.territories.size();
		pool*=3;
		pool/=players.length;
		armyPool = pool;
		
	}

}

//enum Rule_Name {RULE1, RULE2, RULE3} removed