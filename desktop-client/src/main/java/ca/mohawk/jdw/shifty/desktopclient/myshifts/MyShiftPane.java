package ca.mohawk.jdw.shifty.desktopclient.myshifts;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MyShiftPane extends BorderPane {

    private final Shift shift;

    private final Button offerButton;

    public MyShiftPane(final Shift shift){
        this.shift = shift;
        getStyleClass().add("bordered");

        final LocalDateTime startDateTime = shift.startTimestamp().toLocalDateTime();
        final LocalDateTime finishDateTime = shift.finishTimestamp().toLocalDateTime();
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

        offerButton = new Button("Offer");
        BorderPane.setAlignment(offerButton, Pos.TOP_RIGHT);
        BorderPane.setMargin(offerButton, new Insets(0, 5, 0, 0));
        offerButton.setOnAction(e -> client.offerMyShift(shift));

        client.myShiftOffers().list()
                .addListener((ListChangeListener<ShiftOffer>) c -> {
                    updateColors();
                });

        setCenter(center);
        setRight(offerButton);

        updateColors();
    }

    private void updateColors(){
        final ShiftOffer offer = client.myShiftOffers().forId(shift.id());
        Platform.runLater(() -> {
            getStyleClass().removeAll("green", "yellow", "red", "orange");
            if(offer != null){
                getStyleClass().add("yellow");
                offerButton.setText("Offered");
                offerButton.setDisable(true);
            }else{
                offerButton.setText("Offer");
                offerButton.setDisable(false);
            }
        });
    }
}
