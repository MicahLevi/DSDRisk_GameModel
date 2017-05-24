import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GUItest {
	public static void main (String[] args){
		String mapFile = "maps/Classic.txt";
		String mapImg = "imgs/Classic.jpg";
		String numPlayer = "5";
		//GUISetup mySetup = new GUISetup();
		//mySetup.runGUI();
		GUIRiskGame myGUI = new GUIRiskGame();
		myGUI.spawnGame(mapFile, mapImg, numPlayer);
		int testInt = 888;

		Color [] myClrs = {Color.red,Color.magenta,Color.green,Color.blue,Color.yellow,Color.orange};
		//myGUI.countries.get(3).changeButtonColor(myClrs[1]);
		String america = "America";
		String russia = "Russia";
		//myGUI.countries.get(3).incrementArmies();
		//myGUI.countries.get(3).setArmies(testInt);
		
		ArrayList<GUICountry> testCountries = new ArrayList<GUICountry>();//create a list of trivial countries to test updateMap()
		for (int i = 0; i < 42; i++){
			int randomColor = ThreadLocalRandom.current().nextInt(0, 4);
			int randomArmies = ThreadLocalRandom.current().nextInt(0, 21);
			testCountries.add(new GUICountry(myClrs[randomColor], randomArmies));//use test constructor in GUICountry
		}
		TerritoryInfo[] territoryTestInfo = new TerritoryInfo[42];//create a list of trivial countries to test updateMap()
		for (int i = 0; i < 42; i++){
			int randomColor = ThreadLocalRandom.current().nextInt(0, 4);
			int randomArmies = ThreadLocalRandom.current().nextInt(0, 21);
			territoryTestInfo[i] = new TerritoryInfo(randomColor,randomArmies,i);//use test constructor in GUICountry
		}
		new Thread(myGUI).start();
		//while(true){
			synchronized(myGUI){
				try {
					//infinite loop. not necessary but shows how this works with checking with responses
					while(true){
						System.out.println("waiting...");
						//wait to receive button input from gui
						myGUI.wait();
						System.out.println("response!");
						//call update map function
						myGUI.updateMap(territoryTestInfo);
						System.out.println(myGUI.aGlobalClicked + " was clicked!");
						//reset button flag back to zero
						myGUI.aGlobalClicked = -1;
						System.out.println("pass back!");
						//notify gui that driver is done handling the information and it is free to run again
						myGUI.notify();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			}
		//}


		//myGUI.pickInitCntrys();
		//myGUI.placingArmies(5);
		//myGUI.pickAttacker();
		//myGUI.pickDefender();
		//myGUI.confirmAnnihilate("Peru","Brazil");
		//myGUI.confirmAnnihilate(america, russia);
		//myGUI.fortifyNum("Western United States","Eastern United States");
		//myGUI.setFortSrc();
		//myGUI.setFortDest();
		//myGUI.notTurn();
		/**
		for (int i = 6; i>=1; i--)
		{
			myGUI.showRoll("Western United States","Eastern United States",i,i,i,i,i);
		}
		*/
		//myGUI.showCards(100,1,2,3,4,5,6,7,8,9);
		//myGUI.update(testCountries);
		
	}
}
