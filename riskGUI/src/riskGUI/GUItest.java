package riskGUI;

import java.awt.Color;

public class GUItest {
	public static void main (String[] args){
		String mapFile = "maps/Classic.txt";
		String mapImg = "imgs/Classic.jpg";
		String numPlayer = "5";
		RiskGUI myGUI = new RiskGUI();
		myGUI.spawnGame(mapFile, mapImg, numPlayer);
		int testInt = 888;

		Color [] myCols = {Color.red,Color.orange};
		myGUI.countries.get(3).changeButtonColor(myCols[1]);
		String america = "America";
		String russia = "Russia";
		myGUI.countries.get(3).incrementArmies();
		myGUI.countries.get(3).setArmies(testInt);
		
		
		//these function calls are for testing
		//myGUI.pick();
		//myGUI.placingArmies(5);
		//myGUI.pickAttacker();
		//myGUI.pickDefender();
		//myGUI.confirmAnnihilate("America","Russia");
		//myGUI.confirmAnnihilate(america, russia);
		//myGUI.move("America","Russia");
		//myGUI.setFortSrc();
		//myGUI.setFortDest();
		//myGUI.notTurn();
		
		for (int i = 6; i>=1; i--)
		{
			myGUI.showRoll("Western United States","Eastern United States",i,i,i,i,i);
		}
		//myGUI.showCards(100,1,2,3,4,5,6,7,8,9);
		
	}
}
