import java.util.ArrayList;

public class GameMap {
	private String 		name;
	private Continent[] continents;
	
	public class Territory {
		private String  name;
		private int 	xPos;
		private int 	yPos;
		private ArrayList<Territory> borders;
		
		public Territory (String territoryName, int xPosition, int yPosition) {
			name    = territoryName;
			xPos    = xPosition;
			yPos    = yPosition;
			borders = new ArrayList<Territory>(); 	//we need to make note of this because
													//in our specs, we say this is a list of
													//ints, but I like this better
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
	
	public GameMap() {
		//TODO: This should read in the JSON map or mapfile
		//this should read in just the map object that GameSetup deals with
	}
}
