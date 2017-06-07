import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;

public class GUIRiskGame extends JPanel implements Runnable{
	//the two following lists will contain countries
	//and the button which corresponds to the country

	public ArrayList<GUICountry> countries = new ArrayList<GUICountry>();
	public ArrayList<JButton> myButtons = new ArrayList<JButton>();
	private JPanel bottomPanel = new JPanel();
	boolean oneIsClicked = false;
	boolean twoIsClicked = false;
	boolean threeIsClicked = false;
	boolean fourIsClicked = false;
	boolean fiveIsClicked = false;
	int threeCount = 0;
	//for testing
	int aGlobalClicked = -1;
	//globals
	int turnPhase = 0;
	int selectedTerritory = -1;
	int numUnits = 1;
	boolean nextPhase = false;
	boolean cancelSelect = false;
	
	
	public Color [] myCols = {Color.red,Color.orange,Color.green,Color.blue,Color.yellow,Color.magenta};


	
	public void spawnGame(String mapFile, String mapImg, String numPlayer){
		System.out.println("Spawn game function called ");
		System.out.println("The mapFile is " + mapFile);
		System.out.println("The mapImg is " + mapImg);
		System.out.println("The selected num of players is " + numPlayer);

		JFrame frame = new JFrame(" +++ Risky Business +++ ");
		frame.setSize(900,700);
		frame.setResizable(true);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		bottomPanel.setLayout(null);
		bottomPanel.setBounds(0, 490, 900, 170);
		bottomPanel.setBackground(Color.blue);
		
		//get the map image
		try{
			JPanel mapPanel = new JPanel(){
				BufferedImage image = ImageIO.read(new File(mapImg));
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(image, 0, 0, 880, 560, this);
				}
			};
			mapPanel.setLayout(null);
			frame.setContentPane(mapPanel);

		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("There is no img for the map");
		}
		/** 
		read in a file with map information and spawn the gui according to the parameters given in the file.
		create a button for each country which will be placed according to the map file
		 */
		
		try {
			File file = new File(mapFile);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] singleLine = line.split("\\s+");
				JButton but = makeButton(singleLine[0], singleLine[1], singleLine[3], singleLine[4]);
				frame.add(but);
				int intID = Integer.parseInt(singleLine[0]); 
				GUICountry country = new GUICountry(but, intID);
				//there will be a button array which corresponds to the countries
				countries.add(country);
				myButtons.add(but);
			}	
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println("There was no image found for the selected map gui in spawnLocalGame");
			e.printStackTrace();
		}
		frame.add(bottomPanel);
		frame.setVisible(true);
	}

	public void pickInitCntrys()
	{
		bottomPanel.removeAll();
		JPanel pickCntrysP = new JPanel();
		pickCntrysP.setBackground(Color.lightGray);
		pickCntrysP.setLayout(null);
		pickCntrysP.setBounds(5, 5, 873, 160);
		
		JButton testPC = new JButton("Test");
		testPC.setBounds(750, 0, 120, 30);
		testPC.addActionListener( new ActionListener()
		{	
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testPC clicked");
				placingArmies(7);
			}
		});
		pickCntrysP.add(testPC);
		
		JLabel pickCntrysL = new JLabel("Select a country for initial ownership ");
		pickCntrysL.setForeground(Color.red);
		pickCntrysL.setSize(800,100);
		pickCntrysL.setLocation(60,10);
		pickCntrysL.setFont(new Font("Serif", Font.ITALIC, 50));
		pickCntrysP.add(pickCntrysL);
		bottomPanel.add(pickCntrysP);
		bottomPanel.repaint();
	}
	
	public void placingArmies(int armies)
	{
		bottomPanel.removeAll();
		JPanel placingArmiesP = new JPanel();
		placingArmiesP.setBackground(Color.lightGray);
		placingArmiesP.setLayout(null);
		placingArmiesP.setBounds(5, 5, 873, 160);
		
		JButton testPA = new JButton("Test");
		testPA.setBounds(750, 0, 120, 30);
		testPA.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testPA clicked");
				pickAttacker();
			}
		});
		placingArmiesP.add(testPA);
			
		JLabel placingArmiesL = new JLabel("You have " +armies+" armies left ");
		placingArmiesL.setForeground(Color.red);
		placingArmiesL.setSize(700,100);
		placingArmiesL.setLocation(60,10);
		placingArmiesL.setFont(new Font("Serif", Font.ITALIC, 50));
		placingArmiesP.add(placingArmiesL);
		bottomPanel.add(placingArmiesP);
		bottomPanel.repaint();
	}	
	
	public void pickAttacker()
	{
		bottomPanel.removeAll();
		JPanel attkSrcP = new JPanel();
		attkSrcP.setBackground(Color.lightGray);
		attkSrcP.setLayout(null);
		attkSrcP.setBounds(5, 5, 873, 160);
			
		JButton testAC = new JButton("Test");
		testAC.setBounds(750, 0, 120, 30);
		testAC.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testAC clicked");
				pickDefender();
			}
		});
		attkSrcP.add(testAC);
		
		JLabel attkSrcL = new JLabel("Select an attacking country ");
		attkSrcL.setSize(700,100);
		attkSrcL.setLocation(60,10);
		attkSrcL.setFont(new Font("Serif", Font.ITALIC, 50));
		attkSrcL.setForeground(Color.red);
		attkSrcP.add(attkSrcL);
		
		JButton endTurn = new JButton("End Attack Phase");
		endTurn.setBounds(700, 50, 160, 60);
		endTurn.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("attkSrcB2 clicked");
				nextPhase = true;
				selectedTerritory = 1;
				setFortSrc();					
			}
		});
		attkSrcP.add(endTurn);
		bottomPanel.add(attkSrcP);
		bottomPanel.repaint();
	}
		
	public void pickDefender()
	{
		bottomPanel.removeAll();
		JPanel defP = new JPanel();
		defP.setBackground(Color.lightGray);
		defP.setLayout(null);
		defP.setBounds(5, 5, 873, 160);
		
		JButton testD = new JButton("Test");
		testD.setBounds(750, 0, 120, 30);
		testD.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testD clicked");
				confirmAnnihilate("Eastern United States","Western United States");
			}
		});
		defP.add(testD);
		
		JLabel defL = new JLabel("Select a defender ");
		defL.setSize(700,100);
		defL.setLocation(60,10);
		defL.setFont(new Font("Serif", Font.ITALIC, 50));
		defL.setForeground(Color.red);
		defP.add(defL);
		
		JButton defB2 = new JButton("Cancel");
		defB2.setBounds(700, 50, 90, 60);

		defB2.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("defB2 clicked");
				pickAttacker();
				cancelSelect = true;
			}
		});
		defP.add(defB2);
		bottomPanel.add(defP);
		bottomPanel.repaint();
	}
	
	public void confirmAnnihilate(String a, String b)
	{
		bottomPanel.removeAll();
		JPanel annihilateP = new JPanel();
		annihilateP.setBackground(Color.lightGray);
		annihilateP.setLayout(null);
		annihilateP.setBounds(5, 5, 873, 160);
			
		JButton testA = new JButton("Test");
		testA.setBounds(750, 0, 120, 30);
		testA.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testA clicked");
				fortifyNum("Eastern United States","Western United States");
			}
		});
		annihilateP.add(testA);
			
		JLabel annihilateL = new JLabel(a+" VS "+b, SwingConstants.CENTER);
		annihilateL.setForeground(Color.red);
		annihilateL.setSize(880,100);
		//annihilateL.setLocation(200,0);
		annihilateL.setFont(new Font("Serif", Font.ITALIC, 40));
		annihilateP.add(annihilateL);
		
		JButton annihilateB2 = new JButton("Annihilate");
		annihilateB2.setBounds(280, 90, 90, 60);
		annihilateB2.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("annihilateB2 clicked");
			}
		});
		annihilateP.add(annihilateB2);
			
		JButton annihilateB3 = new JButton("Cancel");
		annihilateB3.setBounds(580, 90, 90, 60);
		annihilateB3.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("annihilateB3 clicked");
				pickAttacker();
			}
		});
		annihilateP.add(annihilateB3);
		bottomPanel.add(annihilateP);
		bottomPanel.repaint();
	}
	public void fortifyNum(String src, String dest){
		bottomPanel.removeAll();
		JPanel moveP = new JPanel();
		moveP.setBackground(Color.lightGray);
		moveP.setLayout(null);
		moveP.setBounds(5, 5, 873, 160);
		JLabel move = new JLabel(src+" to "+dest, SwingConstants.CENTER);
		move.setForeground(Color.red);
		move.setSize(800,100);
		//move.setLocation(10,0);
		move.setFont(new Font("Serif", Font.ITALIC, 40));
		moveP.add(move);
		
		JButton testM = new JButton("Test");
		testM.setBounds(750, 0, 120, 30);
		testM.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testM clicked");
				setFortSrc();
			}
		});
		
		JButton moveMin = new JButton("Move Min");
		moveMin.setBounds(200, 100, 120, 30);
		moveMin.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("moveMin button clicked");
			}
		});
		
		JButton moveHalf = new JButton("Move Half");
		moveHalf.setBounds(400, 100, 120, 30);
		moveHalf.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("moveHalf button clicked");
			}
		});
		
		JButton moveMax = new JButton("Move Max");
		moveMax.setBounds(600, 100, 120, 30);
		testM.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("moveMax button clicked");
			}
		});
		moveP.add(testM);
		moveP.add(moveMin);
		moveP.add(moveMax);
		moveP.add(moveHalf);
		bottomPanel.add(moveP);
		bottomPanel.repaint();
	}
	
	public void setFortSrc()
	{
		bottomPanel.removeAll();
		JPanel fortSrcP = new JPanel();
		fortSrcP.setBackground(Color.lightGray);
		fortSrcP.setLayout(null);
		fortSrcP.setBounds(5, 5, 873, 160);
		
		JButton testFS = new JButton("Test");
		testFS.setBounds(750, 0, 120, 30);
		testFS.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testFS clicked");
				setFortDest();
			}
		});
		fortSrcP.add(testFS);
		
		JLabel fortSrc = new JLabel("Select a country to fortify from ");
		fortSrc.setSize(700,100);
		fortSrc.setLocation(60,10);
		fortSrc.setFont(new Font("Serif", Font.ITALIC, 50));
		fortSrc.setForeground(Color.red);
		fortSrcP.add(fortSrc);
		
		JButton attkSrcB2 = new JButton("End Turn");
		attkSrcB2.setBounds(700, 50, 90, 60);
		attkSrcB2.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("endTurn clicked");
			}
		});
		
		bottomPanel.add(attkSrcB2);
		bottomPanel.add(fortSrcP);
		bottomPanel.repaint();
	}
	
	public void setFortDest()
	{
		bottomPanel.removeAll();
		JPanel fortDestP = new JPanel();
		fortDestP.setBackground(Color.lightGray);
		fortDestP.setLayout(null);
		fortDestP.setBounds(5, 5, 873, 160);
		
		JButton testFD = new JButton("Test");
		testFD.setBounds(750, 0, 120, 30);
		testFD.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testFD clicked");
				notTurn();
			}
		});
		fortDestP.add(testFD);
		
		JLabel fortDest = new JLabel("Select a country to fortify to ");
		fortDest.setSize(700,100);
		fortDest.setLocation(60,10);
		fortDest.setFont(new Font("Serif", Font.ITALIC, 50));
		fortDest.setForeground(Color.red);
		fortDestP.add(fortDest);
		
		JButton attkSrcB = new JButton("Cancel");
		attkSrcB.setBounds(700, 50, 90, 60);
		attkSrcB.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("cancel clicked in select fortDest");
				setFortSrc();
			}
		});
		bottomPanel.add(attkSrcB);
		bottomPanel.add(fortDestP);
		bottomPanel.repaint();
	}					
	
	public void notTurn()
	{						
		bottomPanel.removeAll();
		JPanel notTurnP = new JPanel();
		notTurnP.setBackground(Color.lightGray);
		notTurnP.setLayout(null);
		notTurnP.setBounds(5, 5, 873, 160);
		
		JButton testNTB = new JButton("Test");
		testNTB.setBounds(750, 0, 120, 30);
		testNTB.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testNTB clicked");
				showRoll("Eastern United States","Western United States",6,6,6,6,6);
			}
		});
		notTurnP.add(testNTB);
		
		JLabel notYourTurn = new JLabel("IT IS NOT YOUR TURN");
		notYourTurn.setSize(700,100);
		notYourTurn.setLocation(60,10);
		notYourTurn.setFont(new Font("Serif", Font.ITALIC, 50));
		notYourTurn.setForeground(Color.red);
		notTurnP.add(notYourTurn);
		bottomPanel.add(notTurnP);
		bottomPanel.repaint();
	}
	
	public void showRoll(String a, String d,int d1,int d2, int d3, int d4, int d5)
	{
		System.out.println("showRoll() was called");
		bottomPanel.removeAll();
		String one = Integer.toString(d1);
		String two = Integer.toString(d2);
		String three = Integer.toString(d3);
		String four = Integer.toString(d4);
		String five = Integer.toString(d5);
		String test = Integer.toString(0);
			
		JPanel diceP = new JPanel();
		diceP.setBackground(Color.lightGray);
		diceP.setLayout(null);
		diceP.setBounds(5, 5, 873, 160);
			
		JButton testDB = new JButton("Test");
		testDB.setBounds(750, 0, 120, 30);
		testDB.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("testDB clicked");
				showCards(8,1,2,3,4,5,6,7,8,9);
			}
		});
		
		JButton testButton = new JButton("Show dice");
		testButton.setBounds(750, 60, 120, 30);
		testButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int one,two,three,four,five;
				one = 1; two = 2;three = 3;four = 4;five = 5;
				showRoll("Eastern United States","Western United States",one,two,three,four,five);

			}
		});

		JButton retreat = new JButton("Retreat");
		retreat.setBounds(750, 120, 120, 30);
		retreat.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("diceB clicked");
				pickAttacker();
			}
		});
		
		JLabel attkCntry = new JLabel(a+" -VS- "+d, SwingConstants.CENTER);
		attkCntry.setForeground(Color.red);
		attkCntry.setSize(880,50);
		//attkCntry.setLocation(10,0);
		attkCntry.setFont(new Font("Serif", Font.ITALIC, 40));
		diceP.add(attkCntry);

			
		JLabel die1 = new JLabel();
		die1.setLocation(50, 70);
		die1.setSize(80, 80);
		diceP.add(die1);
		die1.setIcon(new ImageIcon(new ImageIcon("imgs/"+one+".jpg").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));

		JLabel die2 = new JLabel();
		die2.setLocation(170, 70);
		die2.setSize(80, 80);
		diceP.add(die2);
		die2.setIcon(new ImageIcon(new ImageIcon("imgs/"+two+".jpg").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));

		JLabel die3 = new JLabel();
		die3.setLocation(290, 70);
		die3.setSize(80, 80);
		diceP.add(die3);
		die3.setIcon(new ImageIcon(new ImageIcon("imgs/"+three+".jpg").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));

		JLabel die4 = new JLabel();
		die4.setLocation(490, 70);
		die4.setSize(80, 80);
		diceP.add(die4);
		die4.setIcon(new ImageIcon(new ImageIcon("imgs/"+four+".jpg").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));

		JLabel die5 = new JLabel();
		die5.setLocation(610, 70);
		die5.setSize(80, 80);
		diceP.add(die5);
		die5.setIcon(new ImageIcon(new ImageIcon("imgs/"+five+".jpg").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
		
		diceP.add(testDB);
		diceP.add(retreat);
		diceP.add(testButton);
		bottomPanel.add(diceP);
		bottomPanel.repaint();
		try {
		    Thread.sleep(1000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}

	}
			
	public void showCards(int tradeVal, int c1,int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9){
		//while it is possible to have nine cards at once, 
		//we're only going to display five of them
		//tradeVal is to display how many armies they will get if they trade
		
		bottomPanel.removeAll();
		String tradeValue = Integer.toString(tradeVal);
		String first = Integer.toString(c1);
		String second = Integer.toString(c2);
		String third = Integer.toString(c3);
		String fourth = Integer.toString(c4);
		String fifth = Integer.toString(c5);
		String sixth = Integer.toString(c6);
		String seventh = Integer.toString(c7);
		String eightth = Integer.toString(c8);
		String nineth = Integer.toString(c9);
		
		JPanel cardP = new JPanel();
		cardP.setBackground(Color.lightGray);
		cardP.setLayout(null);
		cardP.setBounds(5, 5, 873, 160);

		JButton test = new JButton("Test");
		test.setBounds(750, 0, 120, 30);
		test.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("diceB clicked");
				pickInitCntrys();
			}
		});
		
		JButton trade = new JButton("Trade");
		trade.setBounds(750, 100, 120, 30);
		trade.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("The trade button in show cards was clicked");
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.setBounds(750, 100, 120, 30);
		cancel.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("The cancel button in show cards was clicked");

			}
		});
		
		if (c5 > -1){//if the player has to trade thier cards
			JLabel mustTrade = new JLabel("Must trade now");
			mustTrade.setForeground(Color.red);
			mustTrade.setSize(270,50);
			mustTrade.setLocation(525,0);
			mustTrade.setFont(new Font("Serif", Font.ITALIC, 40));
			cardP.add(mustTrade);
		}else{
			JLabel canTrade = new JLabel("Trade now for");
			canTrade.setForeground(Color.red);
			canTrade.setSize(240,50);
			canTrade.setLocation(525,0);
			canTrade.setFont(new Font("Serif", Font.ITALIC, 40));
			cardP.add(canTrade);
		}
		
		JLabel armies = new JLabel("for " + tradeValue + " armies");
		armies.setForeground(Color.red);
		armies.setSize(250,50);
		armies.setLocation(525,50);
		armies.setFont(new Font("Serif", Font.ITALIC, 40));
		cardP.add(armies);
		
		ImageIcon icon1, icon2, icon3, icon4, icon5;
		try{
			icon1 = new ImageIcon(ImageIO.read(new File("imgs/card"+first+".jpg")));
			if(c1 != -1){
				Image img = icon1.getImage();  
				Image newimg = img.getScaledInstance(90, 130, java.awt.Image.SCALE_SMOOTH);
				icon1 = new ImageIcon(newimg);
				JButton card1Button = new JButton(icon1);
				card1Button.setBounds(15, 15, 90, 130);
				card1Button.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(oneIsClicked == false && threeCount < 3){
							card1Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+first+"Crossed.jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							oneIsClicked = true;
							System.out.println("card in slot 1 selected");
							threeCount++;
						}
						else if(oneIsClicked == true)
						{
							oneIsClicked = false;
							card1Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+first+".jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							System.out.println("card in slot 1 unselected");
							threeCount--;
						}
					}
				});
				cardP.add(card1Button);
			}
			
			if(c2 != -1){
				icon2 = new ImageIcon(ImageIO.read(new File("imgs/card"+second+".jpg")));
				Image img = icon2.getImage() ;  
				Image newimg = img.getScaledInstance(90, 130, java.awt.Image.SCALE_SMOOTH);
				icon2 = new ImageIcon(newimg);
				JButton card2Button = new JButton(icon2);
				card2Button.setBounds(120, 15, 90, 130);
				card2Button.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(twoIsClicked == false && threeCount < 3){
							card2Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+second+"Crossed.jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							twoIsClicked = true;
							System.out.println("card in slot 2 selected");
							threeCount++;
						}
						else if(twoIsClicked == true)
						{
							twoIsClicked = false;
							card2Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+second+".jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							System.out.println("card in slot 2 unselected");
							threeCount--;
						}					
					}
				});
				cardP.add(card2Button);
			}
			
			if(c3 != -1){
				icon3 = new ImageIcon(ImageIO.read(new File("imgs/card"+third+".jpg")));
				Image img = icon3.getImage() ;  
				Image newimg = img.getScaledInstance(90, 130, java.awt.Image.SCALE_SMOOTH);
				icon3 = new ImageIcon(newimg);
				JButton card3Button = new JButton(icon3);
				card3Button.setBounds(225, 15, 90, 130);
				card3Button.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(threeIsClicked == false && threeCount < 3){
							card3Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+third+"Crossed.jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							threeIsClicked = true;
							System.out.println("card in slot 3 selected");
							threeCount++;
						}
						else if(threeIsClicked == true)
						{
							threeIsClicked = false;
							card3Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+third+".jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							System.out.println("card in slot 3 unselected");
							threeCount--;
						}
					}
				});
				cardP.add(card3Button);
			}
			
			if(c4 != -1){
				icon4 = new ImageIcon(ImageIO.read(new File("imgs/card"+fourth+".jpg")));
				Image img = icon4.getImage() ;  
				Image newimg = img.getScaledInstance(90, 130, java.awt.Image.SCALE_SMOOTH);
				icon4 = new ImageIcon(newimg);
				JButton card4Button = new JButton(icon4);
				card4Button.setBounds(330, 15, 90, 130);
				card4Button.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(fourIsClicked == false && threeCount < 3){
							card4Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+fourth+"Crossed.jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							fourIsClicked = true;
							System.out.println("card in slot 4 selected");
							threeCount++;
						}
						else if(fourIsClicked == true)
						{
							fourIsClicked = false;
							card4Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+fourth+".jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							System.out.println("card in slot 4 unselected");
							threeCount--;
						}
					}
				});
				cardP.add(card4Button);
			}
			
			if(c5 != -1){
				icon5 = new ImageIcon(ImageIO.read(new File("imgs/card"+fifth+".jpg")));
				Image img = icon5.getImage() ;  
				Image newimg = img.getScaledInstance(90, 130, java.awt.Image.SCALE_SMOOTH);
				icon5 = new ImageIcon(newimg);
				JButton card5Button = new JButton(icon5);
				card5Button.setBounds(435, 15, 90, 130);
				card5Button.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(fiveIsClicked == false && threeCount < 3){
							card5Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+fifth+"Crossed.jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							fiveIsClicked = true;
							System.out.println("card in slot 5 selected");
							threeCount++;
						}
						else if(fiveIsClicked == true)
						{
							fiveIsClicked = false;
							card5Button.setIcon(new ImageIcon(new ImageIcon("imgs/card"+fifth+".jpg").getImage().getScaledInstance(90, 130, Image.SCALE_DEFAULT)));
							System.out.println("card in slot 5 unselected");
							threeCount--;
						}
					}
				});
				cardP.add(card5Button);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("No img for card button in showCards()");
		}
				
		cardP.add(test);

		if(c5 > -1)//it they have to trade
			cardP.add(trade);
		else{
			cardP.add(trade);
			cardP.add(cancel);
		}
		bottomPanel.add(cardP);
		bottomPanel.repaint();
	}

	public JButton makeButton(String countryID, String name ,String x, String y){
		//JButton but = makeButton(id, name, x, y);

		int ID = Integer.parseInt(countryID);
		int xx = Integer.parseInt(x);
		int yy = Integer.parseInt(y);
				
		JButton myButton = new JButton("0");
		myButton.setBounds(xx, yy, 60, 20);
		//myButton.setContentAreaFilled(false);
		myButton.setHorizontalTextPosition(JButton.CENTER);
		myButton.setVerticalTextPosition(JButton.CENTER);
		myButton.setBorderPainted(false);
		//myButton.setEnabled(false);
		myButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
		    {
				int i = myButtons.indexOf(e.getSource());
				aGlobalClicked = i;
				selectedTerritory = i;
				System.out.println(name + " clicked");
			}
		});
		return myButton;
	}


	public void updateMap(TerritoryInfo[] in){
		int colorId = 0;
		for (int i = 0; i < countries.size(); i++) {
			colorId = in[i].owner_id;
			if(colorId != -1)
				countries.get(i).setButtonColor(myCols[in[i].owner_id]);
			countries.get(i).setArmies(in[i].num_armies);
			//countries.get(3).setArmies(testInt);
			  //clonedCopy.add(obj.clone());
			}
	}

	//this is the thread function
	@Override
	public void run() {
		//this is what combines the threads. "this refers to it being the child process"
		synchronized(this){
			System.out.println("im here");
			//notify();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				System.out.println("Locked!");
				//notify();
				wait();
				System.out.println("Freed!");
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			//this is an infinite loop to keep checking for updates
			while(true){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//check to see if the global was clicked(something was clicked/flagged)
				//System.out.println(selectedTerritory);
				if(selectedTerritory!=-1){
					System.out.println("aGlobal: " + selectedTerritory);
					//notify the parent that something was clicked
					notify();
					System.out.println("waiting inner...");
					//wait for parent to respond with notify
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println("beep..."+aGlobalClicked);
			}
		}	
	}
}
