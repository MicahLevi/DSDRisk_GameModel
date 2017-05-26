import java.util.ArrayList;

public class Territory {
		public int		id;
		private String  name;
		private int 	xPos;
		private int 	yPos;
		public ArrayList<Integer> borderlist;
		//public ArrayList<Territory> borders;
		
		public Territory (int territoryId, String territoryName, int xPosition, int yPosition) {
			id		= territoryId;
			name    = territoryName;
			xPos    = xPosition;
			yPos    = yPosition;
			borderlist = new ArrayList<Integer>();
			//borders = new ArrayList<Territory>(); 	//we need to make note of this because
													//in our specs, we say this is a list of
													//ints, but I like this better	
		}
		
		/*public ArrayList<Territory> getBorders() {
			return borders;
		}
		
		public void addBorder(Territory borderTerritory) {	//do we need to do this or is this for GameSetup???
			borders.add(borderTerritory);
		}*/
	}