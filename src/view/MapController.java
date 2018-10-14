package view;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MapController {
    public View view;

    public AnchorPane pane_map;
    public Button button_save_edited_map;
    public Button button_quit_to_menu;

    public void initialize(View view) {
        this.view = view;
        pane_map.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    createCountry(e.getX(), e.getY());
                }
            }
        });
    }

    public void createCountry(double cursorX, double cursorY) {
        view.createCountry(cursorX, cursorY);
    }

    public void backToMenu() {
        view.showMenuStage();
    }
}
