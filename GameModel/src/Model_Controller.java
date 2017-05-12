import java.util.Map;

import com.google.gson.Gson;

public class Model_Controller {
	
	private Board board;
	private Gson gson;
	/**
	 * Have GameSetup already create the board and pass it in
	 * 
	 * @param initBoard
	 */
	public void initGame(Board initBoard)
	{
		board = initBoard;
	}
	/**
	 * We get passed our already pre-defined map with borders
	 * set up already and have GameSetup deal with it.
	 * 
	 * @param aMap
	 * @param rules
	 */
	public void initGame(GameMap aMap,Rule[] rules, Player[] players)
	{
		board = new Board(0,aMap,rules, players);
	}
	
	/**
	 * init without rules from GameSetup. create own rules array
	 * 
	 * @param aMap
	 * @param players
	 */
	public void initGame(GameMap aMap, Player[] players)
	{
		Rule[] rules = new Rule[10];
		rules[0] = new Rule(0,0,players); //initialize the rules ourselves given parameters
		board = new Board(0,aMap,rules, players);
	}
	
	/**
	 * function that handles updating the GameState
	 * Takes in an object. 
	 * If string, convert to GameState obj
	 * else, cast obj to gamestate
	 * @param currState
	 * @return
	 */
	public void initGame(Object pkg)
	{
		SetupPackage locPkg;
		if(pkg == String.class)
			//we must clarify what we are getting from game setup to move forward here
			locPkg = gson.fromJson((String) pkg, SetupPackage.class);
		else
			locPkg = (SetupPackage)pkg;
		board = new Board(0,locPkg.map,locPkg.rules,locPkg.players);
	}
	
	public GameState update(Object currState)
	{
		GameState locState;
		if(currState == String.class)
			locState = gson.fromJson((String) currState, GameState.class);
		else
			locState = (GameState) currState;
		return locState;
		
		//do we want to return a json??
	}
	
	public int[][] attackCountry(GameState curState, int attack_id, int defend_id, int num_units) throws Exception{
		try {
			if (board.territoryIsAdjacent(attack_id, defend_id))
				return curState.attackCountry(attack_id, defend_id, num_units);
		} catch (Exception e) {
			throw e;
		}
		throw new Exception("Territories must be adjacent to attack");
	}
	
	public void fortifyCountry(GameState curState, int from_id, int to_id, int num_units) throws Exception {
		int status = -3;
		if (board.territoryIsAdjacent(from_id, to_id))
			status = curState.fortifyCountry(from_id, to_id, num_units);
		switch (status) {
			case -1:
				throw new Exception("Fortifying to wrong territory");
			case -2:
				throw new Exception("Fortifying: too many units requested to move");
			case -3:
				throw new Exception("Fortifying: territories must be adjacent");
		}
		
	}
	
	public Model_Controller()
	{
		gson = new Gson();
	}
	
}
