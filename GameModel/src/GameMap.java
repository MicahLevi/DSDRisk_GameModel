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
			borders = new ArrayList<Territory>();
		}
		
		public void addBorder(Territory borderTerritory) {
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
	}
}
