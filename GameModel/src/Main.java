import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main (String[] args){
		String mapFileGui = "maps/Classic.txt";
		String mapFileBoard = "maps/ameroki.map";
		String mapImg = "imgs/Classic.jpg";
		String numPlayer = "5";
		Player[] players = new Player[5];
		for (int i = 0; i<5; i++) {
			players[i] = new Player("Player_" + i, i);
		}
		
		ModelController model = new ModelController();
		model.initGame(mapFileGui,mapFileBoard, mapImg, players, 0);
		
		/*try {
			model.saveObjToFile("GameState.json", model.getGameState());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			model.saveObjToFile("MapFile.json", model.getBoard().getGameMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
