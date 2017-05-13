import java.util.ArrayList;

public class GameMap {
	private String 		name;
	private Continent[] continents;
	
	public class Territory {
		private int		id;
		private String  name;
		private int 	xPos;
		private int 	yPos;
		private ArrayList<Territory> borders;
		
		public Territory (int territoryId, String territoryName, int xPosition, int yPosition) {
			id		= territoryId;
			name    = territoryName;
			xPos    = xPosition;
			yPos    = yPosition;
			borders = new ArrayList<Territory>(); 	//we need to make note of this because
													//in our specs, we say this is a list of
													//ints, but I like this better	
		}
		
		public ArrayList<Territory> getBorders() {
			return borders;
		}
		
		public void addBorder(Territory borderTerritory) {	//do we need to do this or is this for GameSetup???
			borders.add(borderTerritory);
		}
	}

	public class Continent {
		private String name;
		private String color;
		private Territory[] territories;
		
		public Continent(String continentName, String continentColor, Territory[] continentTerritories) {
			name		= continentName;
			color		= continentColor;
			territories = continentTerritories;
		}
	}
	
	public Territory getTerritory(int id) {
		for (int i=0; i<continents.length; i++) {
			for (int j=0; j<continents[i].territories.length; j++) {
				if (continents[i].territories[j].id == id) {
					return continents[i].territories[j];
				}
			}
		}
		return null;
	}
	
	
	public boolean isAdjacent(int ter1_id, int ter2_id) {
		Territory t = getTerritory(ter1_id);
		ArrayList<Territory> b = t.getBorders();
		for (int i=0; i<b.size(); i++) {
			if (b.get(i).id == ter2_id)
				return true;
		}
		return false;
	}
	
	public GameMap() {
		//TODO: This should read in the JSON map or mapfile
		//this should read in just the map object that GameSetup deals with
	}
}
