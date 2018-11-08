package controller;

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
    private CardModel cardModel;



    public void init(Model model, CardView card, MapController mapController) {
        this.model = model;
        this.card = card;
        this.mapController = mapController;
    }

    @FXML
    private void cancelCardView(ActionEvent event) {
        Stage stage = (Stage) cancelCardView.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void checkTrade(ActionEvent event) {
        trade.setDisable(false);
        textToShow.setText(null);
        List<Card> selectedCards = cardModel.retrieveSelectedCardsFromCheckbox(
                model.getCurrentPlayer().getPlayerCardList(),cbs);

        if (selectedCards.size() == 3) {
            boolean flag = cardModel.checkTradePossible(selectedCards);

            if (flag) {
                cardModel.setCardsExchangeable(selectedCards);
                Stage stage = (Stage) trade.getScene().getWindow();
                stage.close();
            } else {
                textToShow.setText("Invalid Combination.");
                trade.setDisable(false);
                return;
            }
        } else {
            textToShow.setText("Select only 3 cardModel");
            return;
        }

    }


    public void autoInitializeController() {

        currentPlayerName.setText("Cards of " + Model.getCurrentPlayer().getName());
        for(int i=0;i<3;i++){
            Model.getCurrentPlayer().getPlayerCardList().add(new Card(CardType.ARTILLERY));
            Model.getCurrentPlayer().getPlayerCardList().add(new Card(CardType.CAVALRY));
            Model.getCurrentPlayer().getPlayerCardList().add(new Card(CardType.INFANTRY));
        }
        playerCards = Model.getCurrentPlayer().getPlayerCardList();

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
            cardModel.setCardsExchangeable(cardss);
        }
    }
}