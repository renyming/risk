package controller;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.CardModel;
import model.Model;
import model.Player;
import view.CardView;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class CardController {

    @FXML private Button trade;
    @FXML private Button cancelCardView;
    @FXML private Label currentPlayerName;
    @FXML private Label textToShow;
    @FXML private VBox cardVbox;

    private List<model.Card> playerCards;
    private CheckBox[] cbs;
    private Model model;
    private CardView card;
    private MapController mapController;
    private Player currentPlayer;



    public void init(Model model, CardView card, MapController mapController) {
        this.model = model;
        this.card = card;
        this.mapController = mapController;
    }

    @FXML
    private void cancelCardView(ActionEvent event) {
        model.quitCards();
        if(CardModel.getInstance().readyToQuit()) {
            Stage stage = (Stage) cancelCardView.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    private void checkTrade(ActionEvent event) {
        trade.setDisable(false);
        textToShow.setText(null);
        currentPlayer = CardModel.getInstance().getCurrentPlayer();
        List<Card> selectedCards = CardModel.getInstance().retrieveSelectedCardsFromCheckbox
                (currentPlayer.getPlayerCardList(),cbs);
        if (selectedCards.size() == 3) {
        model.trade((ArrayList<Card>) selectedCards);
        }
    }

    public void autoInitializeController() {
        cardVbox.getChildren().clear();
        currentPlayer = CardModel.getInstance().getCurrentPlayer();
        currentPlayerName.setText("All Cards Of Player : " + currentPlayer.getName());
        textToShow.setStyle("-fx-text-fill: red");
        if(CardModel.getInstance().finishExchange()){
            textToShow.setStyle("-fx-text-fill: green");
        }
        textToShow.setText(CardModel.getInstance().getInvalidInfo());
        playerCards = currentPlayer.getPlayerCardList();

        if (playerCards.size() < 3) {
            trade.setDisable(true);
        } else {
            trade.setDisable(false);
        }
        loadAllCards();
    }


    public void loadAllCards() {
        int numberOfCards = playerCards.size();
        cbs = new CheckBox[numberOfCards];
        for (int i = 0; i < numberOfCards; i++) {
            cbs[i] = new CheckBox(
                    playerCards.get(i).getCardType().toString());
        }
        cardVbox.getChildren().addAll(cbs);
    }

}