import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameMap {
	private String 		name;
	private Continent[] continents;
	private ArrayList<Continent> conts;
	
	public Territory getTerritory(int id) {
		for (int i=0; i<conts.size(); i++) {
			for (int j=0; j<conts.get(i).territories.size(); j++) {
				if (conts.get(i).territories.get(j).id == id) {
					return conts.get(i).territories.get(j);
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
	
	
	public Territory getTerritoryID(ArrayList<Continent> continentsList, int id){
		Territory returnTerritory;
		int i, j, k;
		for(i = 0; i < continentsList.size(); i++){
			for(j = 0; j < continentsList.get(i).territories.size(); j++){
				if(continentsList.get(i).territories.get(j).id == id){
					returnTerritory = continentsList.get(i).territories.get(j);
					return returnTerritory;
				}
			}
		}
		return null;
	}
	
	public Continent[] getContinents(){
		return continents;
	}
	
	public GameMap(String mapname) throws FileNotFoundException, IOException{
		name = mapname;
		conts = new ArrayList<Continent>();
		int i, j, k;
		String path = "maps/" + mapname + ".map";
		String line = "";
		int mode = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			while((line = br.readLine()) != null){
				
				switch(mode){
				case 0: 
					if(line.equals("[continents]")){
						mode = 1;
						break;
					}else if(line.equals("[countries]")){
						mode = 2;
						break;
					}else if(line.equals("[borders]")){
						mode = 3;
						break;
					}else{
						break;
					}
				case 1:
					if(line.equals("")){
						mode = 0;
						break;
					}else{
						String[] splitCont = line.split(" ");
						Continent cont = new Continent(splitCont[0], splitCont[1], splitCont[2]);
						conts.add(cont);
						break;
					}
				case 2:
					if(line.equals("")){
						mode = 0;
						break;
					}else{
						String[] splitTerritory = line.split(" ");
						Territory territory = new Territory(Integer.parseInt(splitTerritory[0]),
															splitTerritory[1],
															Integer.parseInt(splitTerritory[3]),
															Integer.parseInt(splitTerritory[4]));
						conts.get(Integer.parseInt(splitTerritory[2]) - 1).territories.add(territory);
						break;
					}
				case 3:
					if(line.equals("")){
						mode = 0;
						break;
					}else{
						String[] splitBorders = line.split(" ");
						int territoryId = Integer.parseInt(splitBorders[0]);
						Territory tempTerr;
						outerloop:
						for(i = 0; i < conts.size(); i++){
							for(j = 0; j < conts.get(i).territories.size(); j++){
								if(territoryId == conts.get(i).territories.get(j).id){
									for(k = 1; k < splitBorders.length; k++){
										conts.get(i).territories.get(j).borderlist.add(Integer.parseInt(splitBorders[k]));
									}
									break outerloop;
								}
								
							}
						}
						break;
					}
				default:
					break;
				}
				
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		Territory tempTerritory;
		for(i = 0; i < conts.size(); i++){
			for(j = 0; j < conts.get(i).territories.size(); j++){
				for(k = 0; k < conts.get(i).territories.get(j).borderlist.size();k++){
					tempTerritory = getTerritoryID(conts, conts.get(i).territories.get(j).borderlist.get(k));
					conts.get(i).territories.get(j).borders.add(tempTerritory);
				}
			}
		}

			
		
	}
	
}
