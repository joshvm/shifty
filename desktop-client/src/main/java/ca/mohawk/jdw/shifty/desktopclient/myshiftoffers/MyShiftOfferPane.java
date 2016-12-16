package ca.mohawk.jdw.shifty.desktopclient.myshiftoffers;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MyShiftOfferPane extends BorderPane {

    private final ShiftOffer offer;

    public MyShiftOfferPane(final ShiftOffer offer){
        this.offer = offer;
        getStyleClass().add("bordered");

        final LocalDateTime startDateTime = offer.shift().startTimestamp().toLocalDateTime();
        final LocalDateTime finishDateTime = offer.shift().finishTimestamp().toLocalDateTime();
        final Label dateLabel = new Label(startDateTime.format(DateTimeFormatter.ISO_DATE));
        if(!startDateTime.toLocalDate().equals(finishDateTime.toLocalDate()))
            dateLabel.setText(dateLabel.getText() + " - " + finishDateTime.format(DateTimeFormatter.ISO_DATE));
        dateLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final Label rangeLabel = new Label(String.format("%s - %s", startDateTime.format(DateTimeFormatter.ISO_TIME), finishDateTime.format(DateTimeFormatter.ISO_TIME)));
        BorderPane.setAlignment(rangeLabel, Pos.CENTER);
        rangeLabel.getStyleClass().add("cellLabel");

        final BorderPane center = new BorderPane();
        center.setCenter(dateLabel);
        center.setBottom(rangeLabel);

        final Button cancelButton = new Button("Cancel");
        BorderPane.setAlignment(cancelButton, Pos.TOP_RIGHT);
        BorderPane.setMargin(cancelButton, new Insets(0, 5, 0, 0));
        cancelButton.setOnAction(e -> client.cancelMyShiftOffer(offer));

        setCenter(center);
        setRight(cancelButton);
        setBottom(new ShiftOfferRequestsPane(offer));
    }
}
