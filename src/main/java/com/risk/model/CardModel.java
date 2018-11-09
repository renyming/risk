package com.risk.model;

import javafx.scene.control.CheckBox;
import java.util.*;

/**
 *  Observable of CardView
 *  Save the info that CardView needed
 */
public class CardModel extends Observable {

    private static CardModel instance;
    private Player currentPlayer;
    private String invalidInfo;
    private int invalidInfoNum = -1;

    private final String validType = "Card Exchange Finished";
    private final String invalidTypeOne = "Invalid Card Combination";
    private final String invalidTypeTwo = "Please Select 3 Cards Only";
    private final String invalidTypeThree = "Current Player Owning More Than 5 Cards";
    private final List<String> invalidTypes = Arrays.asList(validType,invalidTypeOne,invalidTypeTwo,invalidTypeThree);

    /**
     * Singleton patten, to get the only instance of card model
     * @return cardModel
     */
    public static CardModel getInstance() {
        if (null == instance) instance = new CardModel();
        return instance;
    }

    /**
     *  Method of notification
     */
    public void update() {
        setChanged();
        notifyObservers();
    }

    /**
     * Method of display
     */
    public void display(){

    }

    /**
     * Method of hide
     */
    public void hide(){

    }

    /**
     * Set current player
     * @param currentPlayer currentPlayer
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Get current Player
     * @return currentPlayer
     */
    public Player getCurrentPlayer() { return currentPlayer; }

    /**
     * Retrieve selected cards from checkbox
     * @param cards cards
     * @param checkboxes where to get cards
     * @return list of card
     */
    public List<Card> retrieveSelectedCardsFromCheckbox(List<Card> cards, CheckBox[] checkboxes) {
        int counter = 0;
        List<Card> selectedCards = new ArrayList<>();
        for (CheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selectedCards.add(cards.get(counter));
            }
            counter++;
        }
        if(selectedCards.size() != 3) {
            setInvalidInfo(2);
            update();

        }
        return selectedCards;
    }

    /**
     * Model call this to help user to identify the invalid action which is performed during a phase
     * i.e. invalidInfo = "Select one of your own countries", "There is no path between ...", etc
     * @param invalidInfoType is the user invalid action info type
     */
    void setInvalidInfo(int invalidInfoType) {
        invalidInfoNum = invalidInfoType;
        this.invalidInfo = invalidTypes.get(invalidInfoType);
    }

    /**
     * Model call this to set invalid info during each phase
     * @return the invalid info
     */
    public String getInvalidInfo() { return invalidInfo; }

    public boolean readyToQuit() {
        invalidInfo = null;
        return invalidInfoNum != 3;
    }

    public boolean finishExchange() { return invalidInfoNum == 0; }

}
