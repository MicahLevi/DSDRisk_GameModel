import java.util.ArrayList;

public class GameMap {
	private String 		name;
	private Continent[] continents;
	
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
	public Continent[] getContinents(){
		return continents;
	}
	public GameMap() {
		//TODO: This should read in the JSON map or mapfile
		//this should read in just the map object that GameSetup deals with
	}
}
