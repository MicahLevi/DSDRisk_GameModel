public class Rule {
	public int ruleId;
	public int setting;
	public Player[] turn_order;
	
	public Rule(int id, int aSetting, Player[] players)
	{
		ruleId = id;
		setting = 0;
		turn_order = null;
		switch(id) {
			case 0: turn_order = players;
					break;
		}
	}
}