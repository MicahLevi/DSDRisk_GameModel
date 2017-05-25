import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main (String[] args){
		String mapFile = "maps/Classic.txt";
		String mapImg = "imgs/Classic.jpg";
		String numPlayer = "5";
		Player[] players = new Player[5];
		for (int i = 0; i<5; i++) {
			players[0] = new Player("Player_" + i, i);
		}
		
		ModelController model = new ModelController(mapFile, mapImg, players, 0);
		model.playTurn(state);
		
	}
}
