import java.util.Map;

public class Model_Controller {
	
	private Board board;
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
	 * 
	 * @param currState
	 * @return
	 */
	public GameState update(GameState currState)
	{
		return currState;
	}
	
	//??
	public Model_Controller()
	{
		
	}
	
}
