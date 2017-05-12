/*Object containing information about the setup.
 * Contains information about number of players,
 * rules, win conditions, and card conditions.
 */
public class Board {
	private long 	 id;
	private GameMap  map;
	private Rule[]   rules;
	private Player[] players;
	
	
	public Board(long gameId, GameMap gameMap, Rule[] gameRules, Player[] gamePlayers) {
		id      = gameId;
		map     = gameMap;
		rules   = gameRules;
		players = gamePlayers;
	}
	
	public boolean territoryIsAdjacent(int ter1_id, int ter2_id) {
		return map.isAdjacent(ter1_id, ter2_id);
	}

}

//enum Rule_Name {RULE1, RULE2, RULE3} removed