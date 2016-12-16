package ca.mohawk.jdw.shifty.desktopclient.myshifts;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MyShiftsPane extends BorderPane {

    private static class ShiftCell extends ListCell<Shift> {

        @Override
        protected void updateItem(final Shift shift, final boolean empty){
            super.updateItem(shift, empty);
            if(shift == null)
                return;
            Platform.runLater(() -> setGraphic(new MyShiftPane(shift)));
        }
    }

//    private final TilePane tiles;
    private final ListView<Shift> list;

    private final TextArea selectedInfoArea;

    public MyShiftsPane(){
        getStylesheets().add(UI.css("contentPane"));

        final Label placeholder = new Label("No Shifts");
        placeholder.getStyleClass().add("placeholderLabel");

        list = new ListView<>(client.myShifts().sorted(Comparator.comparing(Shift::startTimestamp)));
        list.setCellFactory(param -> new ShiftCell());
        list.setPlaceholder(placeholder);

        selectedInfoArea = new TextArea();
        selectedInfoArea.setEditable(false);
        selectedInfoArea.setWrapText(true);
        selectedInfoArea.setPrefRowCount(5);

        list.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, o, n) -> {
                    if(n != null){
                        final LocalDateTime startDateTime = n.startTimestamp().toLocalDateTime();
                        final LocalDateTime finishDateTime = n.finishTimestamp().toLocalDateTime();
                        selectedInfoArea.setText(
                                String.format(
                                        "Starts: %s @ %s%nFinishes: %s @ %s%n%n%s",
                                        startDateTime.format(DateTimeFormatter.ISO_DATE),
                                        startDateTime.format(DateTimeFormatter.ISO_TIME),
                                        finishDateTime.format(DateTimeFormatter.ISO_DATE),
                                        finishDateTime.format(DateTimeFormatter.ISO_TIME),
                                        n.description()
                                )
                        );
                    }else
                        selectedInfoArea.setText("");
                });

        setCenter(list);
        setBottom(selectedInfoArea);
    }

}
