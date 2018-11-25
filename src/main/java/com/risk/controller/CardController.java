package com.risk.controller;

import com.risk.model.*;
import com.risk.view.CardView;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle event when user interact with CardView
 */
public class CardController {

    @FXML private Button trade;
    @FXML private Button cancelCardView;
    @FXML private Button closeButton;
    @FXML private Label currentPlayerName;
    @FXML private Label textToShow;
    @FXML private VBox cardVbox;

    private List<Card> playerCards;
    private CheckBox[] cbs;
    private Model model;
    private CardView card;
    private MapController mapController;
    private Player currentPlayer;


    /**
     * Get corresponding reference of Model, CardView and MapController
     * @param model is the reference of Model
     * @param card is the reference of CardView
     * @param mapController is the reference of the MapController
     */
    public void init(Model model, CardView card, MapController mapController) {
        this.model = model;
        this.card = card;
        this.mapController = mapController;
    }

    /**
     * Handle cancelling CardView event
     * @param event the Action event
     */
    @FXML
    private void cancelCardView(ActionEvent event) {
        model.quitCards();
        if(CardModel.getInstance().readyToQuit()) {
            Stage stage = (Stage) cancelCardView.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * handle close card window by red button
     */
    public void closeRequest(){
        if(!cancelCardView.isDisable()) {
            model.quitCards();
            if (CardModel.getInstance().readyToQuit()) {
                Stage stage = (Stage) cancelCardView.getScene().getWindow();
                stage.close();
            }
        }else{
            System.out.println("just close");
        }
    }

    /**
     * if the exchange operation is valid
     * @param event the Action event
     */
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

    /**
     * initiate CardView
     */
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
        closeButton.setVisible(false);
        loadAllCards();
    }

    /**
     * show all the cards on the CardView
     */
    public void loadAllCards() {
        int numberOfCards = playerCards.size();
        cbs = new CheckBox[numberOfCards];
        for (int i = 0; i < numberOfCards; i++) {
            cbs[i] = new CheckBox(
                    playerCards.get(i).getCardType().toString());
        }
        cardVbox.getChildren().addAll(cbs);
    }

    /**
     * card button open read only card ex window
     */
    public void openReadOnlyCardWindow(){
        autoInitializeController();
        cancelCardView.setVisible(false);
        trade.setVisible(false);
        closeButton.setVisible(true);

    }

    /**
     *close card ex window without check current card numbers
     */
    public void closeReadOnlyCardWindow(){
        Stage stage = (Stage) cancelCardView.getScene().getWindow();
        stage.close();
    }
}