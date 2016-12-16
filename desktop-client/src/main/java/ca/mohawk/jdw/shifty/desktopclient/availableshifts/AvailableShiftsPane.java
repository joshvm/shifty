package ca.mohawk.jdw.shifty.desktopclient.availableshifts;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class AvailableShiftsPane extends BorderPane {

    private static class AvailableShiftCell extends ListCell<ShiftOffer> {

        @Override
        protected void updateItem(final ShiftOffer availableShift, final boolean empty){
            super.updateItem(availableShift, empty);
            if(availableShift == null)
                return;
            setGraphic(new AvailableShiftPane(availableShift));
        }
    }

    private final ListView<ShiftOffer> list;

    private final TextArea selectedInfoArea;

    public AvailableShiftsPane(){
        getStylesheets().add(UI.css("contentPane"));

        final Label placeholder = new Label("No Available Shifts");
        placeholder.getStyleClass().add("placeholderLabel");

        list = new ListView<>(client.availableShifts().sorted(Comparator.comparing(s -> s.shift().startTimestamp())));
        list.setCellFactory(param -> new AvailableShiftCell());
        list.setPlaceholder(placeholder);

        selectedInfoArea = new TextArea();
        selectedInfoArea.setEditable(false);
        selectedInfoArea.setWrapText(true);
        selectedInfoArea.setPrefRowCount(5);

        list.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, o, n) -> {
                    if(n != null){
                        final LocalDateTime startDateTime = n.shift().startTimestamp().toLocalDateTime();
                        final LocalDateTime finishDateTime = n.shift().finishTimestamp().toLocalDateTime();
                        selectedInfoArea.setText(
                                String.format(
                                        "Offered By: %s %s%nStarts: %s @ %s%nFinishes: %s @ %s%n%n%s",
                                        n.offeringEmployee().firstName(),
                                        n.offeringEmployee().lastName(),
                                        startDateTime.format(DateTimeFormatter.ISO_DATE),
                                        startDateTime.format(DateTimeFormatter.ISO_TIME),
                                        finishDateTime.format(DateTimeFormatter.ISO_DATE),
                                        finishDateTime.format(DateTimeFormatter.ISO_TIME),
                                        n.shift().description()
                                )
                        );
                    }else
                        selectedInfoArea.setText("");
                });

        setCenter(list);
        setBottom(selectedInfoArea);
    }
}
