package ca.mohawk.jdw.shifty.desktopclient.myshiftoffers;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class ShiftOfferRequestsPane extends BorderPane {

    private static class ShiftOfferRequestCell extends ListCell<ShiftOffer.Request> {

        @Override
        protected void updateItem(final ShiftOffer.Request request, final boolean empty){
            super.updateItem(request, empty);
            if(request == null)
                return;
            setGraphic(new ShiftOfferRequestPane(request));
        }
    }

    private final ShiftOffer offer;

    private final ListView<ShiftOffer.Request> list;

    public ShiftOfferRequestsPane(final ShiftOffer offer){
        this.offer = offer;

        final Label placeholder = new Label("No Requests");
        placeholder.getStyleClass().add("placeholderLabel");

        list = new ListView<>(offer.requests().list());
        list.setPrefHeight(100);
        list.setPlaceholder(placeholder);
        list.setCellFactory(param -> new ShiftOfferRequestCell());

        setCenter(list);
    }
}
