import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
public class ModelController {
	
	private Board board;
	private Gson gson;
	private GameState locState;
	private int self;
	private GUIRiskGame gui;
	private int placingArmies;
	private TerritoryInfo storedTerritory;
	private int heldId = -1;
	
	private boolean attackerSelected = false;
	
	public ModelController(){
		gson = new Gson();
	}
	
	/**
	 * Have GameSetup already create the board and pass it in
	 * 
	 * @param initBoard
	 */
	public void initGame(Board initBoard, int me)
	{
		board = initBoard;
		self = me;
	}
	/**
	 * We get passed our already pre-defined map with borders
	 * set up already and have GameSetup deal with it.
	 * 
	 * @param aMap
	 * @param rules
	 */
	public void initGame(String mapFile, String mapImg, Rule[] rules, Player[] players, int me)
	{
		board = new Board(0,mapFile,rules, players);
		self = me;
		gui = new GUIRiskGame();
		placingArmies = 35 - (players.length-3)*5;
		
		gui.spawnGame(mapFile, mapImg, String.valueOf(players.length));
		locState = new GameState(board.getGameMap(),players);
	}
	
	
	
	//FIXME: This version is probably worthless unless we find another way to pass rules
	/**
	 * init without rules from GameSetup. create own rules array
	 * 
	 * @param aMap
	 * @param players
	 */
	public void initGame(String mapFileGui,String mapFileBoard, String mapImg, Player[] players,int me)
	{
		Rule[] rules = new Rule[10];
		rules[0] = new Rule(0,0,players); //initialize the rules ourselves given parameters
		self = me;
		gui = new GUIRiskGame();
		gui.spawnGame(mapFileGui, mapImg, String.valueOf(players.length));
		new Thread(gui).start();
		board = new Board(0,mapFileBoard,rules, players);
		locState = new GameState(board.getGameMap(),players);
	}
	/**
	 * function that handles updating the GameState
	 * Takes in an object. 
	 * If string, convert to GameState obj
	 * else, cast obj to gamestate
	 * @param currState
	 * @return
	 */
	public void initGame(Object pkg, int me)
	{
		SetupPackage locPkg;
		if(pkg == String.class)
			//we must clarify what we are getting from game setup to move forward here
			locPkg = gson.fromJson((String) pkg, SetupPackage.class);
		else
			locPkg = (SetupPackage)pkg;
		board = new Board(0,null,locPkg.rules,locPkg.players);
		self = me;
		gui = new GUIRiskGame();
		//gui.spawnGame(null, mapImg, String.valueOf(players.length));
	}
	
	
	public GameState playTurn(Object currState){
		//TODO: get currstate properly through json functions
		locState = (GameState) parseObj(currState,GameState.class);
		if(locState==null)
		{
			System.out.println("failed to get state");
			return null;
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		synchronized(gui){
			System.out.println("and here");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				//infinite loop. not necessary but shows how this works with checking with responses
				while(true){
					//System.out.println("ping");
					Thread.sleep(200);
					System.out.println(locState.getgamePhase());
					gui.updateMap(locState.getmap());
					switch (locState.getgamePhase()) {
						case 0:
						case 1: // Territory Select
							//get initial pool for player
							if(board.getArmyPool()==0)
								board.setTestInitArmyPool();
							gui.pickInitCntrys();
							
							//wait to receive button input from gui
							System.out.println("waiting...");
							gui.notify();
							gui.wait();
							System.out.println("response!");
							
							//get selected territory and add army to that spot IF OWNED
							if(!addArmy(gui.selectedTerritory,1,-1)){
								System.out.println("must select unowned country!");
								break;
							}

							//remove added army from pool
							//board.removeFromArmyPool(1);
							//check if all territories are claimed
							if (locState.allClaimed()) {
								System.out.println("Incrementing Game Phase");
								gui.turnPhase++;				//why does gui have a turnPhase object?
								locState.incrementGamePhase();	//this makes sense
							}
							//added army to board. now end turn
							System.out.println("Ending Turn");
							gui.notTurn();
							gui.selectedTerritory = -1;
							gui.updateMap(locState.getmap());
							//gui.notify();
							return locState;
						case 2: // Territory Placement
							gui.placingArmies(board.getArmyPool());
							
							//wait to receive button input from gui
							System.out.println("waiting...");
							gui.notify();
							gui.wait();
							System.out.println("response!");
							
							//attempt to add army
							if(!addArmy(gui.selectedTerritory,gui.numUnits,self)){
								System.out.println("must select owned country!");
								break;
							}
							
							//if successful
							board.removeFromArmyPool(gui.numUnits);
							System.out.println("total deployed: " + locState.getTotalDeployedArmies() + " init army: " +board.initArmyPool);
							if (locState.getTotalDeployedArmies()==((board.initArmyPool*locState.getPlayers().length)+board.getGameMap().convertToTerritoryInfoArray().length)) {
								gui.turnPhase++;	//??
								locState.incrementGamePhase();
							}
							
							//end turn without updating gamePhase for others
							gui.notTurn();
							gui.selectedTerritory = -1;
							gui.updateMap(locState.getmap());
							return locState;
						case 3: // Deploy
							//initialize how many armies the player gets at the start of their turn
							if (board.getArmyPool() == 0)
								board.addArmyPool(getTurnStartArmies());
							//set up gui
							gui.placingArmies(board.getArmyPool());
							
							System.out.println("waiting...");
							gui.notify();
							gui.wait();
							System.out.println("response!");
							//attempt to add armies
							if(!addArmy(gui.selectedTerritory,gui.numUnits,self)){
								System.out.println("must select owned country!");
								break;
							}
							//if successful
							board.removeFromArmyPool(gui.numUnits);
							if (board.getArmyPool() == 0) {
								gui.turnPhase++;	//??
								locState.incrementGamePhase();
								//gui.numUnits = -1; //??
								gui.selectedTerritory = -1;
								gui.pickAttacker();
							}
							gui.updateMap(locState.getmap());
							break;
						case 4: // Attack
							if (gui.nextPhase) {
								gui.turnPhase++;	//??
								locState.incrementGamePhase();
								gui.nextPhase = false;
								gui.setFortSrc();
							}
							else if(!attackerSelected){
								//wait to receive button input from gui
								System.out.println("waiting for attacker...");
								gui.notify();
								gui.wait();
								System.out.println("response!");
								if(locState.isOwner(gui.selectedTerritory, self) && locState.getmap()[gui.selectedTerritory].num_armies>1)
								{
									attackerSelected=true;
									storedTerritory = locState.getmap()[gui.selectedTerritory];
									//switch to defender
									gui.pickDefender();
								}
								else{
									System.out.println("please pick a country you own!");
								}
								break;
							}
							else {
								System.out.println("waiting for defender...");
								gui.notify();
								gui.wait();
								System.out.println("response!");
								if (gui.cancelSelect)
								{
									gui.pickAttacker();
									attackerSelected = false;
									storedTerritory = null;
									gui.cancelSelect = false;
									gui.selectedTerritory = -1;
								}
								else
								{
									//if defending country is not owned by you and is adjacent to where you are attacking from
									if(!locState.isOwner(gui.selectedTerritory, self) && board.territoryIsAdjacent(storedTerritory.country_id, gui.selectedTerritory))
									{
										TerritoryInfo def = locState.getmap()[gui.selectedTerritory];
										System.out.println("Attacking...");
										int[][] dice = attackCountry(storedTerritory.country_id, gui.selectedTerritory, storedTerritory.num_armies-1);
										System.out.println("Showing Roll...");
										gui.showRoll(locState.getPlayers()[self].getname(),
													locState.getPlayers()[def.owner_id].getname(),
													dice[0][0], dice[0][1], dice[0][2],
													dice[1][0], dice[1][1]);
										System.out.println("Done Attacking");
										storedTerritory = null;
										gui.selectedTerritory = -1;
										attackerSelected = false;
										gui.pickAttacker();
									}
									else{
										System.out.println("please pick valid country");
										System.out.println(storedTerritory.country_id + " adjacent to " + gui.selectedTerritory + board.territoryIsAdjacent(storedTerritory.country_id, gui.selectedTerritory));
										System.out.println(board.getGameMap().getTerritory(storedTerritory.country_id).name + " " + storedTerritory.country_id);
										System.out.println(board.getGameMap().getTerritory(storedTerritory.country_id).borderlist.toString());
										System.out.println(board.getGameMap().getTerritory(gui.selectedTerritory).name + " " + gui.selectedTerritory);
										System.out.println(board.getGameMap().getTerritory(gui.selectedTerritory).borderlist.toString());
									}
								}
							}
							gui.updateMap(locState.getmap());
							break;
						case 5: // Fortify
							
							System.out.println("waiting...");
							gui.notify();
							gui.wait();
							System.out.println("response!");
							if(heldId == -1){
								if(locState.isOwner(gui.selectedTerritory, self)){
									heldId = gui.selectedTerritory;
									gui.setFortDest();
								}
								else{
									System.out.println("please select a territory you own");
								}
								gui.selectedTerritory = -1;
							}
							else{
								if(locState.isOwner(gui.selectedTerritory, self)&&board.territoryIsAdjacent(heldId, gui.selectedTerritory)){
									if(!fortifyCountry(heldId, gui.selectedTerritory, locState.getmap()[heldId].num_armies/2))
									{
										System.out.println("please select valid country");
										gui.selectedTerritory = -1;
										break;
									}
									locState.incrementGamePhase();
									//storedTerritory = null;
									gui.selectedTerritory = -1;
									heldId = -1;
								}
								else{
									System.out.println("Please select valid country");
								}
								gui.selectedTerritory = -1;
							}
							gui.updateMap(locState.getmap());
							break;
						case 6: // end turn
							System.out.println("Ending Turn");
							gui.notTurn();
							locState.setgamePhase(3);//set to deploy
							gui.selectedTerritory = -1;//set control for gui back to -1
							//System.out.println("passing control back to gui");
							//gui.notify();
							return locState;
					}
					gui.selectedTerritory = -1;
					System.out.println("passing control back to gui");
					//gui.notify();
				}
			/*} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return locState;
	}
	
	/*public GameState playTurn(Object currState)
	{
		locState = (GameState) parseObj(currState,GameState.class);
		if(locState==null)
		{
			System.out.println("error parsing object");
			return null;
		}
		if(locState.getturnToken()!=self)
		{
			//updateGui;
			gui.notTurn();
			return locState;
		}
		setInitialArmies();
		gui.updateTerritories(locState.getmap());

		int phase = locState.getturnToken();
		while (phase != 6) {
			switch (phase) {
				case 1: // Territory Select
					int claimed = gui.pickInitCntrys();
					locState.addArmy(claimed, 1);
					if (locState.allTerritoriesClaimed())
						locState.incrementGamePhase();
				case 2: // Territory Placement
					int numArmies = 35 - (locState.getPlayers().length-3)*5;
					gui.placingArmies(numArmies);
					//FIXME: How does the gui return the army distribution?
				case 3: // Deploy
					//FIXME: Where did we store the players hands?
					Card[] hand = locState.getHand(self);
					gui.showCards(hand);
					
					//FIXME: need to get the cards they select
					
					int num_units = locState.tradeCards(cards, self);
					
					//FIXME: Do we use placingArmies again?
					gui.placingArmies(num_units);
					
					//FIXME: How do we know when to end the deploy phase?
					locState.incrementGamePhase();
				case 4: // Attack
					//FIXME: I think these should return an int for the selected territory?
					int atkId = gui.pickAttacker();
					int defId = gui.pickDefender();
					gui.confirmAnnihilate(locState.getmap()[atkId].country_id, locState.getmap()[defId].country_id);
					
					
					int[][] roll = locState.attackCountry(atkId, defId, locState.getmap()[atkId].num_armies - 1);
					//FIXME: Probably easier to pass the rolls as int[][]
					gui.showRoll(roll);
					
					//FIXME: How do we know when they end attack phase?
					locState.incrementGamePhase();
				case 5: // Fortify
					//FIXME: Need to return an int for the src and dest
					int src = gui.setFortSrc();
					int dest = gui.setFortDest();
					
					gui.fortifyNum(locState.getmap()[src].country_id, locState.getmap()[dest].country_id);
					
					
				default:
					throw new Exception("Gamestate had invalid game phase: " +phase);
			}
		}
		gui.notTurn();
		////////////////////////
		//gui controller here
		////////////////////////
		return locState;
	}
	*/
	
	private boolean addArmy(int selectedTerritory,int numUnits, int me){
		if(locState.isOwner(selectedTerritory,me)){
			locState.addArmy(selectedTerritory, numUnits);
			locState.getmap()[selectedTerritory].owner_id=self;
		}
		else
			return false;
		return true;
	}
	private void setInitialArmies() {
		board.addArmyPool(getTurnStartArmies());
	}
	private Object parseObj(Object obj, Class<GameState> aClass) {
		Object aObj = null;
		if(obj == String.class) {
			try {
				aObj = gson.fromJson((String) obj, aClass);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else
			return obj;
		return aObj;
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
	/* 
	 * public GameState update_debug(GameState currState, String[][] commands){
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
    
    
*/
    // TODO: Add comments
	
	public int[][] attackCountry(int attack_id, int defend_id, int num_units) throws Exception{
		try {
			if (board.territoryIsAdjacent(attack_id, defend_id))
				return locState.attackCountry(attack_id, defend_id, num_units);
		} catch (Exception e) {
			throw e;
		}
		throw new Exception("Territories must be adjacent to attack");
	}

	// TODO: Add comments
	public boolean fortifyCountry(int from_id, int to_id, int num_units) throws Exception {
		int status = -3;
		if (board.territoryIsAdjacent(from_id, to_id))
			status = locState.fortifyCountry(from_id, to_id, num_units);
		switch (status) {
			case -1:
				//throw new Exception("Fortifying to wrong territory");
				System.out.println("Fortifying to wrong territory");
			case -2:
				//throw new Exception("Fortifying: too many units requested to move");
				System.out.println("Fortifying: too many units requested to move");
			case -3:
				//throw new Exception("Fortifying: territories must be adjacent");
				System.out.println("Fortifying: territories must be adjacent");
			return false;
		}
		return true;
	}

	/**
	 * gets the amount of armies someone can place at the start of their turn
	 * 
	 * @param owner
	 * @param gameState
	 * @return
	 */
	private int getTurnStartArmies(){
		int counter = 0;
		counter += locState.territoryAdder();
		//TODO: FIX THIS
		//counter += locState.continentAdder(board.getGameMap());
		return counter;
	}
	
	public boolean isOwner(int countryId, int ownerId){
		return locState.isOwner(countryId, ownerId);
	}
	
	public boolean isAdjacent(int countryFrom, int countryTo){
		return board.territoryIsAdjacent(countryFrom, countryTo);
	}
	/**
	 * if adding armies fails, returns false, otherwise true and adds
	 * armies to pool
	 * 
	 * @param cards
	 * @param playerId
	 * @return
	 */
	private boolean addArmiesFromCards(Card[] cards, int playerId){
		int counter = getArmiesFromCards(cards,playerId);
		if(counter==-1)
			return false;
		else
		{
			board.addArmyPool(counter);
			return true;
		}
	}
	
	/**
	 * Trades cards in and returns corresponding armies.
	 * if trade in isn't valid, it returns -1 and doesn't
	 * trade cards.
	 * 
	 * @param cards
	 * @param playerId
	 * @return
	 */
	private int getArmiesFromCards(Card[] cards, int playerId){
		if(locState.tradeCards(cards, playerId)){
			return board.getArmiesFromCardTurnIn();
		}
		else
			return -1;

	}
	
	public boolean forceCardTurnInCheck(){
		return getNumberOfCards() >= 5;
	}
	
	private void transferCards(int fromPlayerId, int toPlayerId){
		locState.transferCards(fromPlayerId,toPlayerId);
	}
	
	private int getNumberOfCards(){
		return locState.getPlayers()[locState.getturnToken()].getHand().size();
	}
	
	public void saveObjToFile(String fileName, Object obj) throws IOException{
		BufferedWriter bf = new BufferedWriter(new FileWriter(fileName));
		JsonParser parser = new JsonParser();
		Gson aGson = new GsonBuilder().setPrettyPrinting().create();
		String json = aGson.toJson(obj);
		JsonElement el = parser.parse(json);
		String jsonPretty = aGson.toJson(el);
		bf.write(jsonPretty);
		bf.close();
	}
	public Board getBoard(){
		return board;
	}
	public GameState getGameState(){
		return locState;
	}
	
}
