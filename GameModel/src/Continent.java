public class Continent {
		public String name;
		public String color;
		public Territory[] territories;
		public int groupVal;  //how many armies you get by controlling this area.
		
		public Continent(String continentName, String continentColor, Territory[] continentTerritories) {
			name		= continentName;
			color		= continentColor;
			territories = continentTerritories;
			groupVal 	= 0;
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