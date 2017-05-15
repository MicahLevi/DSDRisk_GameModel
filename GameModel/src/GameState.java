import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	public class Army {
		public int owner_id;
		public int num_armies;
		public Army() {
			owner_id = 0;
			num_armies = 0;
		}
		public Army(int player_id, int armies) {
			owner_id   = player_id;
			num_armies = armies;
		}
	}
	public GameState(){
		
	}
	public GameState(long id, Player[] playerArray) {
		
	}
	
	public void addArmy(int country_id, int num_units) {
		Army locArmy = army_distribution.get(country_id);
		locArmy.num_armies += num_units;
		army_distribution.replace(country_id, locArmy);
	}
	
	
	/**
	 * allows one player to attack another with a given number of units
	 * 
	 * @param attack_id
	 * @param defend_id
	 * @param num_units
	 */
	public int[][] attackCountry(int attack_id, int defend_id, int num_units) throws Exception
	{
		Army attack = army_distribution.get(attack_id);
		Army defend = army_distribution.get(defend_id);
		int[] atk_dice = new int[0];
		int[] def_dice = new int[0];
		if (attack.owner_id == defend.owner_id)
		{
			throw new Exception("Cannot attack your own territory");
		}
		if (attack.num_armies < num_units || num_units < 1
			|| attack.num_armies < 2)
			throw new Exception("Invalid number of units for attack");
		int atk_units = attack.num_armies;
		int def_units = defend.num_armies;
		//For now we attack until there is a winner
		while (atk_units > 0 && def_units > 0)
		{
			//Determine number of dice for attacker
			switch (num_units)
			{
				case 1: case 2:	// roll 1 die
					atk_dice = new int[]{(int)(Math.random()*6+1)};
					break;
				case 3:			// roll 2 dice
					atk_dice = new int[]{(int)(Math.random()*6+1),
					                     (int)(Math.random()*6+1)};
					Arrays.sort(atk_dice);
					break;
				default:		// roll 3 dice
					atk_dice = new int[] {(int)(Math.random()*6+1),
					                      (int)(Math.random()*6+1),
					                      (int)(Math.random()*6+1)};
					break;
			}
			//Determine number of dice for defender
			switch (def_units)
			{
				case 1:
					def_dice = new int[]{(int)(Math.random()*6+1)};
					break;
				default:
					def_dice = new int[]{(int)(Math.random()*6+1),
										 (int)(Math.random()*6+1)};
					Arrays.sort(def_dice);
					break;
			}
			int i = 0;
			
			while (i<atk_dice.length && i<def_dice.length)
			{
				if (atk_dice[i] > def_dice[i])
					def_units--;
				else
					atk_units--;
			}
		}
		//Attacking territory always loses the units it sends
		attack.num_armies -= num_units;
		
		//Defending territory goes to the winner
		if (def_units > 0)
		{
			defend.num_armies = def_units;
		}
		else
		{
			defend.num_armies = atk_units;
			defend.owner_id = attack.owner_id;
		}
		
		army_distribution.replace(attack_id, attack);
		army_distribution.replace(defend_id, defend);
		return new int[][]{atk_dice, def_dice};
	}
	
	/**
	 * fortifies territories. checks for issues as well as a feature
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
	public boolean tradeCards(Card[] cards, int playerId) {
		if(verifyCardTurnin(cards))
		{
			for(Card c: cards){
				players[playerId].removeCard(c);
			}
			return true;
		}
		return false;
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
	/**
	 * checks to see if card type are all the same or all different
	 * returns a bool
	 * 
	 * @param cards
	 * @return bool
	 */
	private boolean verifyCardTurnin(Card[] cards)
	{
		int x = cards[0].getType();
		int y = cards[1].getType();
		int z = cards[2].getType();
		if(x==y&&y==z)
			return true;
		else if(x!=y&&x!=z&&y!=z)
			return true;
		else
			return false;
		
	}
	/**
	 * Checks the continents for complete ownership and returns
	 * the amount of armies for each complete group
	 * 
	 * @param owner
	 * @param map
	 * @return
	 */
	public int continentAdder(int owner, GameMap map) {
		Collection<Army> territories = army_distribution.values();
		int counter = 0;
		Continent[] contList = map.getContinents();
		boolean innerCheck = true;
		for(Continent c: contList)
		{
			for(Territory t: c.territories)
			{
				if(army_distribution.get(t.id).owner_id!=owner){
					innerCheck = false;
					break;
				}
			}
			if(innerCheck)
				counter+=c.groupVal;
		}
		return counter;
		
	}
	/**
	 * Does the calculation for how many armies to get based on territory
	 * @param owner
	 * @return
	 */
	public int territoryAdder(int owner)
	{
		Collection<Army> territories = army_distribution.values();
		int counter = 0;
		for(Army a: territories)
		{
			if(a.owner_id==owner)
				counter++;
		}
		counter/=3;
		if(counter<3)
			counter = 3;
		return counter;
	}
	
	// TODO: Clean getters and setters
	public Map<Integer, Army> getArmy_distribution() {
		return army_distribution;
	}
	public void setArmy_distribution(Map<Integer, Army> army_distribution) {
		this.army_distribution = army_distribution;
	}
	public Player[] getPlayers() {
		return players;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public long getGame_id() {
		return game_id;
	}
	public void setGame_id(long game_id) {
		this.game_id = game_id;
	}
	public int getPlayer_turn() {
		return player_turn;
	}
	public void setPlayer_turn(int player_turn) {
		this.player_turn = player_turn;
	}
	public int getGame_phase() {
		return game_phase;
	}
	public void setGame_phase(int game_phase) {
		this.game_phase = game_phase;
	}
	public int getWinner() {
		return winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}
	public Card[] getDeck() {
		return deck;
	}
	public void setDeck(Card[] deck) {
		this.deck = deck;
	}

}

//enum GamePhase{DEPLOY, ATTACK, REINFORCE};