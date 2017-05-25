import java.util.ArrayList;

public class Continent {
		public String name;
		public String color;
		public ArrayList<Territory> territories;
		public int groupVal;  //how many armies you get by controlling this area.
		
		public Continent(String continentName, String numArmies, String continentColor){
			name = continentName;
			groupVal = Integer.parseInt(numArmies);
			color = continentColor;	
			territories = new ArrayList<Territory>();
		}
		
		/**
		 * sets the group value for how many armies you get by controlling this area.
		 * 
		 * @param groupVal
		 */
		public void setGroupVal(int groupVal){
			groupVal = 0;
		}
	}