package ca.mohawk.jdw.shifty.desktopclient.myshiftoffers;

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

public class MyShiftOffersPane extends BorderPane {

    private static class ShiftOfferCell extends ListCell<ShiftOffer> {

        @Override
        protected void updateItem(final ShiftOffer offer, final boolean empty){
            super.updateItem(offer, empty);
            if(offer == null)
                return;
            setGraphic(new MyShiftOfferPane(offer));
        }
    }

//    private final TilePane tiles;
    private final ListView<ShiftOffer> list;

    private final TextArea selectedInfoArea;

    public MyShiftOffersPane(){
        getStylesheets().add(UI.css("contentPane"));

        final Label placeholder = new Label("No Shift Offers");
        placeholder.getStyleClass().add("placeholderLabel");

        list = new ListView<>(client.myShiftOffers().sorted(Comparator.comparing(o -> o.shift().startTimestamp())));
        list.setCellFactory(param -> new ShiftOfferCell());
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
                                        "Starts: %s @ %s%nFinishes: %s @ %s%n%d Requests%n%n%s",
                                        startDateTime.format(DateTimeFormatter.ISO_DATE),
                                        startDateTime.format(DateTimeFormatter.ISO_TIME),
                                        finishDateTime.format(DateTimeFormatter.ISO_DATE),
                                        finishDateTime.format(DateTimeFormatter.ISO_TIME),
                                        n.requests().list().size(),
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
