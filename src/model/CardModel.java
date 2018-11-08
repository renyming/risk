package model;

import common.CardType;
import javafx.scene.control.CheckBox;
import java.util.*;
import java.util.stream.Collectors;


public class CardModel extends Observable {

    private static CardModel instance;
    private Player currentPlayer;
    private Model model;
    private List<Card> cardsToBeExchange;
    private String invalidInfo;

    public static CardModel getInstance() {
        if (null == instance) instance = new CardModel();
        return instance;
    }

    public void update() {
        setChanged();
        notifyObservers();
    }

    public void display(){

    }

    public void hide(){

    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() { return currentPlayer; }



    public List<Card> retrieveSelectedCardsFromCheckbox(List<Card> cards, CheckBox[] checkboxes) {
        int counter = 0;
        List<model.Card> selectedCards = new ArrayList<>();
        for (CheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selectedCards.add(cards.get(counter));
            }
            counter++;
        }
        return selectedCards;
    }

    public List<model.Card> getValidCardComibination(List<model.Card> selectedCards) {
        HashMap<String, Integer> map = new HashMap<>();
        for (model.Card card : selectedCards) {
            if (map.containsKey(card.getCardType().toString())) {
                map.put(card.getCardType().toString(), map.get(card.getCardType().toString()) + 1);
            } else {
                map.put(card.getCardType().toString(), 1);
            }

        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= 3) {
                return selectedCards.stream().filter(t -> t.getCardType().toString().equals(entry.getKey()))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    public boolean checkTradePossible(List<model.Card> selectedCards) {
        boolean returnFlag = false;
        if (selectedCards.size() == 3) {
            int infantry = 0, cavalry = 0, artillery = 0;
            for (model.Card card : selectedCards) {
                if (card.getCardType().toString().equals(CardType.INFANTRY.toString())) {
                    infantry++;
                } else if (card.getCardType().toString().equals(CardType.CAVALRY.toString())) {
                    cavalry++;
                } else if (card.getCardType().toString().equals(CardType.ARTILLERY.toString())) {
                    artillery++;
                }
            }
            if ((infantry == 1 && cavalry == 1 && artillery == 1) || infantry == 3 || cavalry == 3 || artillery == 3) {
                returnFlag = true;
            }
        }
        return returnFlag;
    }

    public void setCardsExchangeable(List<model.Card> selectedCards) {
        setCardsToBeExchange(selectedCards);
        update();
    }
    public void setCardsToBeExchange(List<model.Card> cardsToBeExchange) {
        this.cardsToBeExchange = cardsToBeExchange;
    }


    /**
     * Model call this to help user to identify the invalid action which is performed during a phase
     * i.e. invalidInfo = "Select one of your own countries", "There is no path between ...", etc
     * @param invalidInfo is the user invalid action info
     */
    void setInvalidInfo(String invalidInfo) { this.invalidInfo = invalidInfo; }


    /**
     * Model call this to set invalid info during each phase
     * @return the invalid info
     */
    public String getInvalidInfo() { return invalidInfo; }

}
