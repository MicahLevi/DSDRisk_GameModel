import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main (String[] args){
		String mapFileGui = "maps/Classic.txt";
		String mapFileBoard = "maps/world.map";
		String mapImg = "imgs/Classic.jpg";
		String numPlayer = "5";
		Player[] players = new Player[2];
		for (int i = 0; i<2; i++) {
			players[i] = new Player("Player_" + i, i);
		}
		
		ArrayList<ModelController> allModels = new ArrayList<ModelController>();
		for(int i = 0; i<players.length;i++)
		{
			allModels.add(new ModelController());
			allModels.get(i).initGame(mapFileGui,mapFileBoard, mapImg, players, i);
		}
		GameState testState = allModels.get(0).getGameState();
		while(true)
			for(int i = 0; i<players.length;i++){
				testState = allModels.get(i).playTurn(testState);
			}
		/*try {
			allModels.get(0).saveObjToFile("GameState.json", allModels.get(0).getGameState());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*try {
			model.saveObjToFile("MapFile.json", model.getBoard().getGameMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
