
public class Card {
	private int territory; //territory
	private int type; //cannon,infantry,etc
	
	public Card(int territory_name, int number_of_units) {
		territory = territory_name;
		type = number_of_units;
	}
	
	public Card(String string_rep){
	    String[] values = string_rep.split(" ");
	    territory = Integer.parseInt(values[0]); 
	    type = Integer.parseInt(values[1]);
	}
	
	public String toString(){
	    return territory + " " + type;
	}
	
	public int getTerritory() {
		return territory;
	}

	public void setTerritory(int territory) {
		this.territory = territory;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

