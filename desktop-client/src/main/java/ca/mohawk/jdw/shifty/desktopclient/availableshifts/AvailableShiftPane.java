package ca.mohawk.jdw.shifty.desktopclient.availableshifts;

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
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class AvailableShiftPane extends BorderPane {

    private final ShiftOffer availableShift;

    private final Button requestButton;

    public AvailableShiftPane(final ShiftOffer availableShift){
        this.availableShift = availableShift;
        getStyleClass().add("bordered");

        final LocalDateTime startDateTime = availableShift.shift().startTimestamp().toLocalDateTime();
        final LocalDateTime finishDateTime = availableShift.shift().finishTimestamp().toLocalDateTime();
        final Label dateLabel = new Label(startDateTime.format(DateTimeFormatter.ISO_DATE));
        if(!startDateTime.toLocalDate().equals(finishDateTime.toLocalDate()))
            dateLabel.setText(dateLabel.getText() + " - " + finishDateTime.format(DateTimeFormatter.ISO_DATE));
        dateLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final Label rangeLabel = new Label(String.format("%s - %s", startDateTime.format(DateTimeFormatter.ISO_TIME), finishDateTime.format(DateTimeFormatter.ISO_TIME)));
        BorderPane.setAlignment(rangeLabel, Pos.CENTER);
        rangeLabel.getStyleClass().add("cellLabel");

        final Label employeeLabel = new Label(String.format("Offered By: %s %s", availableShift.offeringEmployee().firstName(), availableShift.offeringEmployee().lastName()));
        BorderPane.setAlignment(employeeLabel, Pos.CENTER);
        employeeLabel.getStyleClass().add("cellLabel");

        final VBox fields = new VBox();
        fields.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(fields, Pos.CENTER);
        fields.setSpacing(2);
        fields.getChildren().addAll(rangeLabel, employeeLabel);

        final BorderPane center = new BorderPane();
        BorderPane.setAlignment(center, Pos.CENTER);
        center.setCenter(dateLabel);
        center.setBottom(fields);

        requestButton = new Button("Request");
        BorderPane.setAlignment(requestButton, Pos.TOP_RIGHT);
        BorderPane.setMargin(requestButton, new Insets(0, 5, 0, 0));
        requestButton.setOnAction(e -> client.requestToTakeShift(availableShift));

        client.myShiftOfferRequests().list()
                .addListener((ListChangeListener<ShiftOffer.Request>) c -> {
                    updateColors();
                });

        setCenter(center);
        setRight(requestButton);

        updateColors();
    }

    private void updateColors(){
        final ShiftOffer.Request request = client.myShiftOfferRequests().forId(availableShift.id());
        Platform.runLater(() -> {
            getStyleClass().removeAll("red", "green", "orange", "yellow");
            if(request != null){
                requestButton.setText("Requested");
                requestButton.setDisable(true);
                getStyleClass().add("yellow");
            }else{
                requestButton.setText("Request");
                requestButton.setDisable(false);
            }
        });
    }
}
