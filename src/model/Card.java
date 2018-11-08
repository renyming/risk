package model;


import common.CardType;


public class Card  {

    CardType cardType;
    private Country territory;

    public Card(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Country getTerritory() {
        return territory;
    }


    public void setTerritory(Country territory) {
        this.territory = territory;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Card)) {
            return false;
        }

        Card card = (Card) obj;
        return card.getCardType().toString().equalsIgnoreCase(cardType.toString())
                && card.getTerritory().equals(territory);
    }

}
