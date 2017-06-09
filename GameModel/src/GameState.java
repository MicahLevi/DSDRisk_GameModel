import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class GameState {
	
	private TerritoryInfo[]		map; //this should be int if we are passing ints in
	private Player[] 			players;
	private int 				turnToken;
	private int 				gamePhase;
	private int					winner;
	private Card[] 				deck;
	
	public GameState(){
		
	}
	public GameState(GameMap aMap, Player[] playerArray) {
		map = aMap.convertToTerritoryInfoArray();
		players = playerArray; 
		turnToken = 0;
		gamePhase = 1;
		winner = -1;
		deck = new Card[5];
		for(int i = 0; i<deck.length;i++)
			deck[i] = new Card();
	}
	
	public void addArmy(int country_id, int num_units) {
		map[country_id].num_armies+=num_units;
	}
	
	public boolean isOwner(int countryId, int ownerId)
	{
		if(map[countryId].owner_id==ownerId)
			return true;
		return false;
	}
	
	public int getOwner(int countryId)
	{
		return map[countryId].owner_id;
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
		TerritoryInfo attack = map[attack_id];
		TerritoryInfo defend = map[defend_id];
		int[] atk_dice = new int[0];
		int[] def_dice = new int[0];
		System.out.println(attack.owner_id + " from " + attack_id + " with " + attack.num_armies + " armies attacking "
						 + defend.owner_id + " from " + defend_id + " with " + defend.num_armies + " armies defending ");
		
		if (attack.owner_id == defend.owner_id)
		{
			throw new Exception("Cannot attack your own territory");
		}
		if (attack.num_armies < num_units || num_units < 1
			|| attack.num_armies < 2)
			throw new Exception("Invalid number of units for attack");
		int atk_units = num_units;
		int def_units = defend.num_armies;
		//For now we attack until there is a winner
		System.out.println("Calculating Winner");
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
					atk_dice = reverseArray(atk_dice); //Because Java is terrible at sorting desc
					break;
				default:		// roll 3 dice
					atk_dice = new int[] {(int)(Math.random()*6+1),
					                      (int)(Math.random()*6+1),
					                      (int)(Math.random()*6+1)};
					Arrays.sort(atk_dice);
					atk_dice = reverseArray(atk_dice);
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
					def_dice = reverseArray(def_dice);
					break;
			}
			int i = 0;
			
			while (i<atk_dice.length && i<def_dice.length)
			{
				if (atk_dice[i] > def_dice[i])
					def_units--;
				else
					atk_units--;
				i++;
			}
		}
		//Attacking territory always loses the units it sends
		attack.num_armies -= num_units;
		System.out.println("Setting Values");
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
		
		map[attack_id] = attack;
		map[defend_id] = defend;
		System.out.println("Attack Returning");
		int[][] retDice = new int[2][3];
		for (int x=0; x<3; x++) {
			if (atk_dice.length > x)
				retDice[0][x] = atk_dice[x];
			else
				retDice[0][x] = 0;
			
			if (def_dice.length > x)
				retDice[1][x] = def_dice[x];
			else
				retDice[1][x] = 0;
		}
		return retDice;
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
		TerritoryInfo fromArmy = map[from_id];
		TerritoryInfo toArmy = map[to_id];
		if(fromArmy.owner_id == toArmy.owner_id)
		{
			if((fromArmy.num_armies-num_units)<1)
				return -2;
			else
			{
				fromArmy.num_armies -= num_units;
				toArmy.num_armies += num_units;
				map[from_id] = fromArmy;
				map[to_id]= toArmy;
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
				if(isOwner(c.getTerritory(),playerId))
					map[c.getTerritory()].num_armies+=2;
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
		for(TerritoryInfo army: map) {
			if(owner == -1){
				if(army.owner_id==-1)
					return -1;
				else
					owner = army.owner_id;
			}
			else
				if(owner != army.owner_id)
					return -1;
		}
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
	 * @param map
	 * @return
	 */
	public int continentAdder(GameMap aMap) {
		int counter = 0;
		Continent[] contList = aMap.getConts().toArray(new Continent[aMap.getConts().size()]);
		boolean innerCheck = true;
		for(Continent c: contList)
		{
			for(Territory t: c.territories)
			{
				if(map[t.id].owner_id!=turnToken){
					innerCheck = false;
					break;
				}
			}
			if(innerCheck)
				counter+=c.groupVal;
		}
		return counter;
		
	}
	public boolean allClaimed() {
		for (TerritoryInfo t : map){
			if (t.owner_id == -1)
				return false;
		}
		return true;
	}
	
	public int[] reverseArray(int[] a) {
		for (int i = 0; i<a.length/2; i++) {
			int temp = a[i];
			a[i] = a[a.length-i-1];
			a[a.length-i-1] = temp;
		}
		return a;
	}
	
	/**
	 * Does the calculation for how many armies to get based on territory
	 * @return
	 */
	public int territoryAdder()
	{
		int counter = 0;
		for(TerritoryInfo a: map)
		{
			if(a.owner_id==turnToken)
				counter++;
		}
		counter/=3;
		if(counter<3)
			counter = 3;
		return counter;
	}
	public void incrementGamePhase(){
		gamePhase++;
	}
	
	public boolean isPlayerConquered(int playerId){
		return ownsLand(playerId);
	}
	
	private boolean ownsLand(int playerId){
		for(TerritoryInfo a: map)
		{
			if(a.owner_id==playerId)
				return true;
		}
		return false;
	}
	public int getTotalDeployedArmies() {
		int count = 0;
		for(TerritoryInfo t: map){
			count+=t.num_armies;
		}
		return count;
	}
	public void transferCards(int fromPlayerId, int toPlayerId) {
		players[toPlayerId].addAllCards(players[fromPlayerId].conquerHand());
	}
	
	
	// TODO: Clean getters and setters
	public TerritoryInfo[] getmap() {
		return map;
	}
	public void setmap(TerritoryInfo[] map) {
		this.map = map;
	}
	public Player[] getPlayers() {
		return players;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public int getturnToken() {
		return turnToken;
	}
	public void setturnToken(int turnToken) {
		this.turnToken = turnToken;
	}
	public int getgamePhase() {
		return gamePhase;
	}
	public void setgamePhase(int gamePhase) {
		this.gamePhase = gamePhase;
	}
	public Card[] getDeck() {
		return deck;
	}
	public void setDeck(Card[] deck) {
		this.deck = deck;
	}
	public int getWinner() {
		return winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}

}

//enum GamePhase{DEPLOY, ATTACK, REINFORCE};