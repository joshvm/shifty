package ca.mohawk.jdw.shifty.desktopclient.myshiftofferrequests;

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

public class MyShiftOfferRequestsPane extends BorderPane {

    private static class ShiftOfferRequestCell extends ListCell<ShiftOffer.Request> {

        @Override
        protected void updateItem(final ShiftOffer.Request request, final boolean empty){
            super.updateItem(request, empty);
            if(request == null)
                return;
            setGraphic(new MyShiftOfferRequestPane(request));
        }
    }

//    private final TilePane tiles;
    private final ListView<ShiftOffer.Request> list;

    private final TextArea selectedInfoArea;

    public MyShiftOfferRequestsPane(){
        getStylesheets().add(UI.css("contentPane"));

        final Label placeholder = new Label("No Shift Offer Requests");
        placeholder.getStyleClass().add("placeholderLabel");

        list = new ListView<>(client.myShiftOfferRequests().sorted(Comparator.comparing(o -> o.offer().shift().startTimestamp())));
        list.setCellFactory(param -> new ShiftOfferRequestCell());
        list.setPlaceholder(placeholder);

        selectedInfoArea = new TextArea();
        selectedInfoArea.setEditable(false);
        selectedInfoArea.setWrapText(true);
        selectedInfoArea.setPrefRowCount(5);

        list.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, o, n) -> {
                    if(n != null){
                        final LocalDateTime startDateTime = n.offer().shift().startTimestamp().toLocalDateTime();
                        final LocalDateTime finishDateTime = n.offer().shift().finishTimestamp().toLocalDateTime();
                        selectedInfoArea.setText(
                                String.format(
                                        "Starts: %s @ %s%nFinishes: %s @ %s%nOffered By: %s %s%n%n%s",
                                        startDateTime.format(DateTimeFormatter.ISO_DATE),
                                        startDateTime.format(DateTimeFormatter.ISO_TIME),
                                        finishDateTime.format(DateTimeFormatter.ISO_DATE),
                                        finishDateTime.format(DateTimeFormatter.ISO_TIME),
                                        n.offer().offeringEmployee().firstName(),
                                        n.offer().offeringEmployee().lastName(),
                                        n.offer().shift().description()
                                )
                        );
                    }else
                        selectedInfoArea.setText("");
                });

        setCenter(list);
        setBottom(selectedInfoArea);
    }

}
