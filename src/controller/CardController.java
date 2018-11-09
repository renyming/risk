package controller;

import java.util.ArrayList;
import java.util.List;

import common.CardType;
import model.Card;
import model.CardModel;
import model.Model;
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



    public void init(Model model, CardView card, MapController mapController) {
        this.model = model;
        this.card = card;
        this.mapController = mapController;
    }

    @FXML
    private void cancelCardView(ActionEvent event) {
        model.quitCards();
        if(!CardModel.getInstance().getInvalidInfo().equals("you must exchange cards!")) {
            Stage stage = (Stage) cancelCardView.getScene().getWindow();
            stage.close();
        }else{
            textToShow.setText("more than 5.");
        }

    }


    @FXML
    private void checkTrade(ActionEvent event) {
        trade.setDisable(false);
        textToShow.setText(null);
        List<Card> selectedCards = CardModel.getInstance().retrieveSelectedCardsFromCheckbox(
                CardModel.getInstance().getCurrentPlayer().getPlayerCardList(),cbs);
        System.out.println("num slelecd"+ selectedCards.size());
        if (selectedCards.size() == 3) {

            model.trade((ArrayList<Card>) selectedCards);
            /*boolean flag = cardModel.checkTradePossible(selectedCards);

            if (flag) {
                cardModel.setCardsExchangeable(selectedCards);
                Stage stage = (Stage) trade.getScene().getWindow();
                stage.close();
            } else {
                textToShow.setText("Invalid Combination.");
                trade.setDisable(false);
                return;
            }*/
        } else {
            textToShow.setText("Select Only 3 Card");
            return;
        }

    }


    public void autoInitializeController() {
        cardVbox.getChildren().clear();
        currentPlayerName.setText("Cards of " + CardModel.getInstance().getCurrentPlayer().getName());
        playerCards = CardModel.getInstance().getCurrentPlayer().getPlayerCardList();

        if (playerCards.size() < 3) {
            trade.setDisable(true);
        } else {
            trade.setDisable(false);
        }
        loadAllCards();

        //tryCardTrade();
    }


    public void loadAllCards() {
        int numberOfCards = playerCards.size();
        System.out.println("number of current palyer cards "+numberOfCards);
        cbs = new CheckBox[numberOfCards];
        for (int i = 0; i < numberOfCards; i++) {
            cbs[i] = new CheckBox(
                    playerCards.get(i).getCardType().toString());
        }
        cardVbox.getChildren().addAll(cbs);
    }

    public void tryCardTrade() {
        playerCards = Model.getCurrentPlayer().getPlayerCardList();
        //List<Card> cardModel = cardModel.getValidCardComibination(playerCards);
        List<model.Card> cardss = playerCards;
        if (cardss != null && cardss.size() == 3) {
            CardModel.getInstance().setCardsExchangeable(cardss);
        }
    }
}