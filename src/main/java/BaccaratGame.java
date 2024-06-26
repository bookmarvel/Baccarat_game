/*!
    @file BaccaratGame.java
*/

// Name: Chris Wood, NetID:cwood35

// Name: Ana Theys,  NetID:athey3

import javafx.animation.PauseTransition;
import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*! 
	@brief The class to play the game of Baccarat.
	Contains the code required to create the UI elements and call the 
			functions from the BaccaratGameLogic class.
*/
public class BaccaratGame extends Application {
	ArrayList<Card> playerHand; /**< The player's hand of cards, stored in an ArrayList. */
	ArrayList<Card> bankerHand; /**< The banker's hand of cards, stored in an ArrayList. */
	BaccaratDealer theDealer; /**< An instace of the BaccaratDealer class to call the dealer functions. */
	BaccaratGameLogic gameLogic; /**< An instance of the BaccaratGameLogic class to call the logic functions. */
	double currentBet; /**< How much money the player has currently bet on the round. */
	double totalWinnings; /**< How much money the player has won (or lost) over the course of the game so far. */
	String betPlacedOn; /**< Who the player has placed their bet on. Either "Player", "Banker", or "Draw". */

	// Start scene objects
	Text titleText;
	Button startGame;
	VBox layout;

	// Menu bar objects
	MenuBar mainMenuBar;
	Menu menuMenu;
	MenuItem itemExit;
	MenuItem itemFreshStart;
	MenuBar mainMenuBar2;
	Menu menuMenu2;
	MenuItem itemExit2;
	MenuItem itemFreshStart2;

	// Betting scene objects
	BorderPane bettingBorderPane;
	VBox leftBox;
	VBox centerBox;
	HBox inputsBox;
	HBox buttonsBox;
	TextField betAmount;
	Button resetBets;
	Text selectBetText;
	Button selectPlayer;
	Button selectTie;
	Button selectBanker;
	Text currentBetText;
	TextField currentBetDisplay;
	Text totalWinningsText;
	TextField totalWinningsDisplay;
	TextField emptyText;
	Button playButton;

	// Play scene objects
	Text playerCardHeader, bankerCardHeader, playerCardFooter, bankerCardFooter;
	HBox playerHandBox, bankerHandBox;
	VBox playerCardBox, bankerCardBox;
	TextField playerWinningsTextField, currentBetTextField;
	ListView<String> resultsListView;
	ObservableList<String> resultsList;
	ImageView[] playerCards, bankerCards;
	Button dealAndPlayAgainButton;
	VBox playSceneElements, topCentralElements, bottomCentralElements;
	HBox bottomElements;
	EventHandler<ActionEvent> replayEvent, bankerDrawEvent, playerDrawEvent, firstDrawEvent;
	PauseTransition firstDealPause, secondDealPause, thirdDealPause;
	static final double dealBetweenCardDelay = 0.33;
	static final int cardWidth = 120;
	static final int cardHeight = 175;

	Map<String, ArrayList<Image>> cardMap; /**< Map of images of all 52 playing cards. Suite is map key, card number is ArrayList index. */


	// ----------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();

		primaryStage.setTitle("Baccarat");

		populateCardMap();

		createMenuBar();
		createMenuBar2();

		Scene startScene = createStartScene();
		Scene bettingScene = createBettingScene();
		Scene playScene = createPlayScene();

		// Start scene event handler
		startGame.setOnAction(e-> {
			primaryStage.setScene(bettingScene);
			resetBettingScene();
		});

		// Menu bar event handlers
		itemExit.setOnAction(e->System.exit(0));
		itemExit2.setOnAction(e->System.exit(0));
		itemFreshStart.setOnAction(e->{
			totalWinnings = 0;
			resetPlayScene();
			primaryStage.setScene(startScene);
		});
		itemFreshStart2.setOnAction(e->{
			totalWinnings = 0;
			resetPlayScene();
			primaryStage.setScene(startScene);
		});

		// Betting scene event handlers
		playButton.setOnAction(e -> {
			playerWinningsTextField.setText("Player total winnings: $" + totalWinnings);
			primaryStage.setScene(playScene);
		});

		selectPlayer.setOnAction(e-> {
			if (!betAmount.getText().isEmpty()) {
				currentBet = Double.parseDouble(betAmount.getText());
				betPlacedOn = "Player";
				currentBetDisplay.setText("$" + currentBet + " on " + betPlacedOn);
				currentBetTextField.setText("Current Bet: $" + currentBet + " on " + betPlacedOn);
				selectTie.setDisable(true);
				selectBanker.setDisable(true);
				playButton.setDisable(false);
			}
		});

		selectTie.setOnAction(e-> {
			if (!betAmount.getText().isEmpty()) {
				currentBet = Double.parseDouble(betAmount.getText());
				betPlacedOn = "Draw";
				currentBetDisplay.setText("$" + currentBet + " on " + "Tie");
				currentBetTextField.setText("Current Bet: $" + currentBet + " on Tie");
				selectPlayer.setDisable(true);
				selectBanker.setDisable(true);
				playButton.setDisable(false);
			}
		});

		selectBanker.setOnAction(e-> {
			if (!betAmount.getText().isEmpty()) {
				currentBet = Double.parseDouble(betAmount.getText());
				betPlacedOn = "Banker";
				currentBetDisplay.setText("$" + currentBet + " on " + betPlacedOn);
				currentBetTextField.setText("Current Bet: $" + currentBet + " on " + betPlacedOn);
				selectPlayer.setDisable(true);
				selectTie.setDisable(true);
				playButton.setDisable(false);
			}
		});

		resetBets.setOnAction(e->resetBettingScene());

		// Play scene event handlers

		firstDealPause.setOnFinished(e ->{
			playerCards[1].setImage(cardMap.get(playerHand.get(1).suite).get(playerHand.get(1).value));
			//Display score for player
			playerCardFooter.setText("Score: " + gameLogic.handTotal(playerHand));
			secondDealPause.play();
		});

		secondDealPause.setOnFinished(e ->{
			bankerCards[0].setImage(cardMap.get(bankerHand.get(0).suite).get(bankerHand.get(0).value));
			thirdDealPause.play();
		});

		thirdDealPause.setOnFinished(e -> {
			bankerCards[1].setImage(cardMap.get(bankerHand.get(1).suite).get(bankerHand.get(1).value));
			//Display score for banker
			bankerCardFooter.setText("Score: " + gameLogic.handTotal(bankerHand));

			dealAndPlayAgainButton.setDisable(false);
			//Figure out if more cards need to be drawn
			if(gameLogic.handTotal(playerHand) >= 8 || gameLogic.handTotal(bankerHand) >= 8){//Natural win
				endRound();
			}
			else if(gameLogic.evaluatePlayerDraw(playerHand)){
				dealAndPlayAgainButton.setOnAction(playerDrawEvent);
				dealAndPlayAgainButton.setText("Draw for Player");
			}
			else if(gameLogic.evaluateBankerDraw(bankerHand, null)){
				dealAndPlayAgainButton.setOnAction(bankerDrawEvent);
				dealAndPlayAgainButton.setText("Draw for Banker");
			}
			else{
				endRound();
			}
		});

		this.replayEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetBettingScene();
				resetPlayScene();

				primaryStage.setScene(bettingScene);
			}
		};
		this.bankerDrawEvent = new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				//Draw card
				Card bankerDraw = theDealer.drawOne();
				bankerHand.add(bankerDraw);
				//Update displayed score
				bankerCardFooter.setText("Score: " + gameLogic.handTotal(bankerHand));
				//Display card
				bankerCards[2].setImage(cardMap.get(bankerDraw.suite).get(bankerDraw.value));

				endRound();
			}
		};
		this.playerDrawEvent = new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				//Draw card
				Card playerDraw = theDealer.drawOne();
				playerHand.add(playerDraw);
				//Update displayed score
				playerCardFooter.setText("Score: " + gameLogic.handTotal(playerHand));
				//Display card
				playerCards[2].setImage(cardMap.get(playerDraw.suite).get(playerDraw.value));

				Button thisButton = (Button)event.getSource();

				if(gameLogic.evaluateBankerDraw(bankerHand, playerDraw)) {
					thisButton.setOnAction(bankerDrawEvent);
					thisButton.setText("Draw for Banker");
				}
				else{
					endRound();
				}
			}
		};
		this.firstDrawEvent = new EventHandler<>(){
			@Override
			public void handle(ActionEvent event) {
				//Deal cards
				theDealer.shuffleDeck();
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();

				//Display cards
				playerCards[0].setImage(cardMap.get(playerHand.get(0).suite).get(playerHand.get(0).value));
				firstDealPause.play();

				((Button)event.getSource()).setDisable(true);
			}
		};

		dealAndPlayAgainButton.setOnAction(firstDrawEvent);

		// Lights, camera, ACTION

		primaryStage.setScene(startScene);
		primaryStage.show();

	} // end start()


	/*! 
		@brief Populates the map with images of the playing cards. 
	*/
	public void populateCardMap(){
		cardMap = new TreeMap<>();
		String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

		for(String suit: suits){
			cardMap.put(suit, new ArrayList<>(14));
			cardMap.get(suit).add(null);//Added so that card value matches index
			for(int i = 1; i <= 13; i++){
				cardMap.get(suit).add(new Image(i + "_of_" + suit.toLowerCase() + ".png"));
			}
		}
	}


	//-----Start scene-----//
	/*! 
		@brief The code to create and configure the UI elements for the start scene. 
	*/
	public Scene createStartScene() {
		titleText = new Text("Baccarat");
		// titleText.setStyle("-fx-font-size: 150");
		titleText.setStyle("-fx-font: 150 Verdana");
		titleText.setFill(Color.WHITE);
		titleText.setStroke(Color.BLACK);
		titleText.setStrokeWidth(5);

		startGame = new Button("Start");
		startGame.setId("play-button");
		startGame.setEffect(new DropShadow());

		layout = new VBox(50, titleText, startGame);
		layout.setAlignment(Pos.CENTER);
		layout.setId("background");

		Scene startScene = new Scene(layout, 1080, 720);
		startScene.getStylesheets().add(String.valueOf(this.getClass().getResource("style.css")));

		return startScene;
	} // end createStartScene()


	//-----Menu Bar-----//
	/*! 
		@brief The code to create and configure the UI elements for the menu bar for the start scene. 
	*/
	public void createMenuBar() {
		mainMenuBar = new MenuBar();

		menuMenu = new Menu("Menu");
		menuMenu.setStyle("-fx-font-size: 18");
		itemExit = new MenuItem("Exit");
		itemExit.setStyle("-fx-font-size: 18");
		itemFreshStart = new MenuItem("Fresh Start");
		itemFreshStart.setStyle("-fx-font-size: 18");

		menuMenu.getItems().addAll(itemFreshStart, itemExit);

		mainMenuBar.getMenus().add(menuMenu);

	}
	/*! 
		@brief The code to create and configure the UI elements for the menu bar for the betting scene. 
	*/
	public void createMenuBar2() {
		mainMenuBar2 = new MenuBar();

		menuMenu2 = new Menu("Menu");
		menuMenu2.setStyle("-fx-font-size: 18");
		itemExit2 = new MenuItem("Exit");
		itemExit2.setStyle("-fx-font-size: 18");
		itemFreshStart2 = new MenuItem("Fresh Start");
		itemFreshStart2.setStyle("-fx-font-size: 18");

		menuMenu2.getItems().addAll(itemFreshStart2, itemExit2);

		mainMenuBar2.getMenus().add(menuMenu2);

	}


	//-----Betting scene-----//
	/*! 
		@brief The code to create and configure the UI elements for the betting scene. 
	*/
	public Scene createBettingScene() {
		// left side
		selectBetText = new Text("Add to bet:");
		selectBetText.setId("display-bet-text");
		currentBetText = new Text("Current bet:");
		currentBetText.setId("display-bet-text");
		totalWinningsText = new Text("Total winnings:");
		totalWinningsText.setId("display-bet-text");

		leftBox = new VBox(28, selectBetText, currentBetText, totalWinningsText);
		leftBox.setAlignment(Pos.CENTER);

		// center
		betAmount = new TextField();
		betAmount.setPromptText("Enter amount to bet");
		betAmount.setStyle("-fx-font-size: 24");

		resetBets = new Button("Reset bets");
		resetBets.setStyle("-fx-font-size: 24");
		inputsBox = new HBox(10, betAmount, resetBets);

		selectPlayer = new Button("Player");
		selectPlayer.setStyle("-fx-font-size: 24");
		selectTie = new Button("Tie");
		selectTie.setStyle("-fx-font-size: 24");
		selectBanker = new Button("Banker");
		selectBanker.setStyle("-fx-font-size: 24");
		buttonsBox = new HBox(10, selectPlayer, selectTie, selectBanker);

		currentBetDisplay = new TextField("None selected");
		currentBetDisplay.setStyle("-fx-font-size: 24");
		currentBetDisplay.setEditable(false);
		totalWinningsDisplay = new TextField("$"+totalWinnings);
		totalWinningsDisplay.setStyle("-fx-font-size: 24");
		totalWinningsDisplay.setEditable(false);
		emptyText = new TextField("hi");
		emptyText.setStyle("-fx-font-size: 24");
		emptyText.setEditable(false);
		emptyText.setVisible(false);

		centerBox = new VBox(20, inputsBox, buttonsBox, currentBetDisplay, totalWinningsDisplay, emptyText);
		centerBox.setAlignment(Pos.CENTER);


		// right side
		playButton = new Button("Play");
		playButton.setDisable(true);
		playButton.setId("play-button");

		Insets inset = new Insets(5);
		bettingBorderPane = new BorderPane();
		bettingBorderPane.setId("background");
		bettingBorderPane.setTop(mainMenuBar2);
		bettingBorderPane.setLeft(leftBox);
		bettingBorderPane.setCenter(centerBox);
		bettingBorderPane.setRight(playButton);
		BorderPane.setAlignment(playButton, Pos.CENTER);
		BorderPane.setMargin(leftBox, inset);
		BorderPane.setMargin(centerBox, inset);
		BorderPane.setMargin(playButton, inset);

		Scene bettingScene = new Scene(bettingBorderPane, 1080, 720);
		bettingScene.getStylesheets().add(String.valueOf(this.getClass().getResource("style.css")));

		return bettingScene;
	} // end createBettingScene()


	/*! 
		@brief The code to reset the betting scene for the next round. 
	*/
	private void resetBettingScene() {
		betAmount.clear();
		selectPlayer.setDisable(false);
		selectTie.setDisable(false);
		selectBanker.setDisable(false);
		currentBetDisplay.setText("None selected");
		totalWinningsDisplay.setText("$" + totalWinnings);
		playButton.setDisable(true);
	}


	//-----Play scene-----//
	/*! 
		@brief The code to create and configure the UI elements for the play scene. 
	*/
	public Scene createPlayScene(){
		//Pause transitions
		firstDealPause = new PauseTransition(Duration.seconds(dealBetweenCardDelay));
		secondDealPause = new PauseTransition(Duration.seconds(dealBetweenCardDelay));
		thirdDealPause = new PauseTransition(Duration.seconds(dealBetweenCardDelay));

		//Header and footer text for hands
		playerCardHeader = new Text("Player");
		bankerCardHeader = new Text("Banker");
		playerCardFooter = new Text("Score: 0");
		bankerCardFooter = new Text("Score: 0");
		playerCardHeader.setId("display-play-text");
		bankerCardHeader.setId("display-play-text");
		playerCardFooter.setId("display-play-text");
		bankerCardFooter.setId("display-play-text");

		//Image views for cards
		playerCards = new ImageView[3];
		bankerCards = new ImageView[3];
		for(int i = 0; i < 3; i ++) {
			playerCards[i] = new ImageView();
			playerCards[i].setFitWidth(cardWidth);
			playerCards[i].setFitHeight(cardHeight);
			bankerCards[i] = new ImageView();
			bankerCards[i].setFitWidth(cardWidth);
			bankerCards[i].setFitHeight(cardHeight);
		}

		//Boxes for displaying cards and scores
		playerHandBox = new HBox(playerCards[0], playerCards[1], playerCards[2]);
		bankerHandBox = new HBox(bankerCards[0], bankerCards[1], bankerCards[2]);
		playerCardBox = new VBox(playerCardHeader, playerHandBox, playerCardFooter);
		bankerCardBox = new VBox(bankerCardHeader, bankerHandBox, bankerCardFooter);

		//Top textFields
		playerWinningsTextField = new TextField("Player total winnings: $" + totalWinnings);
		currentBetTextField = new TextField("Current Bet: $" + currentBet + " on " + betPlacedOn);
		playerWinningsTextField.setMaxSize(512, 32);
		currentBetTextField.setMaxSize(512, 32);
		playerWinningsTextField.setEditable(false);
		currentBetTextField.setEditable(false);
		playerWinningsTextField.setStyle("-fx-font-size: 20");
		currentBetTextField.setStyle("-fx-font-size: 20");

		//Button
		dealAndPlayAgainButton = new Button("Deal");
		dealAndPlayAgainButton.setPrefSize(512, 48);
		dealAndPlayAgainButton.setStyle("-fx-font-size: 24");

		//Results display
		resultsListView = new ListView<>();
		resultsList = FXCollections.observableArrayList();
		resultsListView.setStyle("-fx-font-size: 16");
		resultsListView.setPrefHeight(125);

		//Put everything in nice boxes
		topCentralElements = new VBox(50, playerWinningsTextField, currentBetTextField);
		bottomCentralElements = new VBox(dealAndPlayAgainButton, resultsListView);
		bottomElements = new HBox(5,playerCardBox, bottomCentralElements, bankerCardBox);
		playSceneElements = new VBox(80, topCentralElements, bottomElements);
		topCentralElements.setAlignment(Pos.CENTER);
		bottomCentralElements.setPadding(new Insets(0, 0, 45, 0));
		bankerCardBox.setAlignment(Pos.CENTER_LEFT);
		playerCardBox.setAlignment(Pos.CENTER_LEFT);
		bottomCentralElements.setAlignment(Pos.BOTTOM_CENTER);

		//Root BorderPane
		BorderPane playSceneRoot = new BorderPane();
		playSceneRoot.setTop(mainMenuBar);
		playSceneRoot.setCenter(playSceneElements);
		BorderPane.setMargin(playSceneElements, new Insets(80,5,100,5));
		playSceneRoot.setId("background");

		Scene playScene = new Scene(playSceneRoot, 1080, 720);
		playScene.getStylesheets().add(String.valueOf(this.getClass().getResource("style.css")));

		return playScene;
	}// end createPlayScene


	/*! 
		@brief The code to reset the play scene for the next round. 
	*/
	private void resetPlayScene() {
		playerHand = null;
		bankerHand = null;

		for(ImageView cardImage : playerCards) {
			cardImage.setImage(null);
		}
		for(ImageView cardImage : bankerCards) {
			cardImage.setImage(null);
		}

		playerCardFooter.setText("Score: 0");
		bankerCardFooter.setText("Score: 0");

		resultsList.clear();
		resultsListView.setItems(resultsList);

		Button thisButton = dealAndPlayAgainButton;
		thisButton.setText("Deal");
		thisButton.setOnAction(firstDrawEvent);
	}


	/*! 
		@brief Evaluates the amount won or lost. 
		@returns The winnings. A loss is represented as winning a negative amount.
	*/
	public double evaluateWinnings(){
		double winnings;
		String winner = gameLogic.whoWon(playerHand, bankerHand);

		if(betPlacedOn.equals(winner)){
			if(betPlacedOn.equals("Player"))
				winnings = currentBet;
			else if(betPlacedOn.equals("Banker"))
				winnings = currentBet * 0.95;
			else //Betting on tie
				winnings = currentBet * 8;
		}
		else {
			winnings = -currentBet;
		}
		return winnings;
	} // end evaluateWinnings()


	/*! 
		@brief Called whenever a round ends.  
		Evaluates the winnings, displays the result, and sets up button for replaying the game.
	*/
	public void endRound(){
		//Evaluate winnings
		totalWinnings += evaluateWinnings();
		playerWinningsTextField.setText("Player total winnings: $" + totalWinnings);

		//Display results
		resultsList.add("Player Total: " + gameLogic.handTotal(playerHand) +
				" Banker Total: " + gameLogic.handTotal(bankerHand));
		String winner = gameLogic.whoWon(playerHand, bankerHand);

		if(winner.equals("Draw")) {
            resultsList.add("Tie");
        } else {
            resultsList.add(winner + " wins");
        }

		if(this.betPlacedOn.equals("Draw"))
		{
			if (winner.equals(betPlacedOn)) {
				resultsList.add("Congrats you bet Tie! You won!");
			} else {
				resultsList.add("Sorry, you bet Tie! You lost your bet!");
			}
		}else {
			if (winner.equals(betPlacedOn)) {
				resultsList.add("Congrats you bet " + this.betPlacedOn + "! You won!");
			} else {
				resultsList.add("Sorry, you bet " + this.betPlacedOn + "! You lost your bet!");
			}
		}
		resultsListView.setItems(resultsList);
		//Set button for replaying
		dealAndPlayAgainButton.setOnAction(replayEvent);
		dealAndPlayAgainButton.setText("Replay");
	} // end endRound()

	
} // end class
