import java.util.Map;
import java.util.Arrays;

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

	/*
	 * Alters the passed gamestate based on the given commands passed as an 
	 * array of Strings, where command[x] is a space-seperated string where the 
	 * first word is the command and the following words are the parameters as
	 * string representations.
	 * 
	 * For each command, the commands[x][0] indicates the xth command to use, 
	 * and all following elements of commands[x][y > 0] are the parameters 
	 * necessary.
	 * 
	 * Accepts these commands: 
	 * - 'add_armies A B' to add B armies to country A
	 * - 'attack A B C' to send C armies from country A to attack country B
	 * - 'fortify A B C' to send C armies from country A to fortify country B
	 * - 'trade_cards A B' to trade in cards A for player B, where A is a Card[] represented
	 *   by a string of the format 'A1 A2, B1 B2, C1 C2' such that x1 is x's territory
	 *   and x2 is x's value. This pair matches the output of Card.toString().
	 * - 'win_check' to check for a win condition
	 * 
	 * In the event any of the commands return something, its output will be printed to
	 * the system output stream (console by default).
	 */
	public GameState update_debug(GameState currState, String[][] commands){
	    // TODO: finish debugging command interpreter
	    for (int i = 0; i < commands.length; i++){
	        switch (commands[i][0]){
	            case "add_armies":
	                currState.addArmy(
	                       Integer.parseInt(commands[i][1]), 
	                       Integer.parseInt(commands[i][2])
	                );
	                break;
	            case "attack":
	                try {
                        int[][] attack_output = currState.attackCountry(
                               Integer.parseInt(commands[i][1]),
                               Integer.parseInt(commands[i][2]),
                               Integer.parseInt(commands[i][3])
                        );
                        System.out.println("OUTPUT: " 
                               + commands[i][0] 
                               + ": " 
                               + Arrays.deepToString(attack_output)
                        );
                    } catch (Exception e){
                        System.out.print("EXCEPTION ON ITEM " + i + ": " + command[i][0]);
                        System.out.println(e);
                    }
	                break;
	            case "fortify":
	                int fortify_output = currState.fortifyCountry(
	                       Integer.parseInt(commands[i][1]),
	                       Integer.parseInt(commands[i][2]),
	                       Integer.parseInt(commands[i][3])
	                );
	                System.out.println("OUTPUT: "
	                       + commands[i][0]
	                       + ": "
	                       + fortify_output
	                );
	                break;
	            case "trade_cards":
	                // Need to convert the array of strings into an array of Card objects
	                String[] card_strings = commands[i][1].split(",");
	                Card[] cards = new Card[card_strings.length];
	                for (int j = 0; i < card_strings.length; i++){
	                    cards[j] = new Card(card_strings[j]);
	                }
	                currState.tradeCards(cards, Integer.parseInt(commands[i][2]));
	                break;
	            case "win_check":
	                System.out.println("OUTPUT: "
	                       + commands[i][0]
	                       + ": "
	                       + currState.winCheck()
	                );
	                break;
	            default:
	                System.out.println("DID NOT RECOGNIZE COMMAND: " + commands[i][0]);
	                break;
	        }
	    }
	    return currState;
    }

    // TODO: Add comments
	public int[][] attackCountry(GameState curState, int attack_id, int defend_id, int num_units) throws Exception{
		try {
			if (board.territoryIsAdjacent(attack_id, defend_id))
				return curState.attackCountry(attack_id, defend_id, num_units);
		} catch (Exception e) {
			throw e;
		}
		throw new Exception("Territories must be adjacent to attack");
	}

	// TODO: Add comments
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
