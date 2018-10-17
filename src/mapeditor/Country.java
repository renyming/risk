package mapeditor;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Country extends AnchorPane {

    private static int cID=0;
    private int ID;
    private CountryController countryController;

    public Country(){
        this.ID=++cID;
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Country.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countryController = fxmlLoader.getController();
        countryController.initialize();
        setVisible(true);
    }

    public int getCountryId(){
        return ID;
    }

    public Point2D getLocation(){
        return new Point2D(this.getLayoutX(),this.getLayoutY());
    }

}
