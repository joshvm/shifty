package ca.mohawk.jdw.shifty.desktopclient.pendingshiftchanges;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.PendingShiftChange;
import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class PendingShiftChangePane extends BorderPane {

    private final PendingShiftChange pendingShiftChange;

    public PendingShiftChangePane(final PendingShiftChange pendingShiftChange){
        this.pendingShiftChange = pendingShiftChange;
        getStyleClass().add("bordered");

        final LocalDateTime startDateTime = pendingShiftChange.shift().startTimestamp().toLocalDateTime();
        final LocalDateTime finishDateTime = pendingShiftChange.shift().finishTimestamp().toLocalDateTime();
        final Label dateLabel = new Label(startDateTime.format(DateTimeFormatter.ISO_DATE));
        if(!startDateTime.toLocalDate().equals(finishDateTime.toLocalDate()))
            dateLabel.setText(dateLabel.getText() + " - " + finishDateTime.format(DateTimeFormatter.ISO_DATE));
        dateLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final Label rangeLabel = new Label(String.format("%s - %s", startDateTime.format(DateTimeFormatter.ISO_TIME), finishDateTime.format(DateTimeFormatter.ISO_TIME)));
        BorderPane.setAlignment(rangeLabel, Pos.CENTER);
        rangeLabel.getStyleClass().add("cellLabel");

        final BorderPane shiftTimePane = new BorderPane();
        shiftTimePane.setCenter(dateLabel);
        shiftTimePane.setBottom(rangeLabel);

        final Label offeringLabel = new Label(String.format("Offered By: %s %s", pendingShiftChange.offeringEmployee().firstName(), pendingShiftChange.offeringEmployee().lastName()));
        offeringLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final Label requestingLabel = new Label(String.format("Requested By: %s %s", pendingShiftChange.requestingEmployee().firstName(), pendingShiftChange.requestingEmployee().lastName()));
        requestingLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final VBox fields = new VBox();
        BorderPane.setAlignment(fields, Pos.CENTER);
        fields.setSpacing(2);
        fields.setAlignment(Pos.CENTER);
        fields.getChildren().addAll(offeringLabel, shiftTimePane, requestingLabel);

        final Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(
                e -> {
                    client.answerPendingShiftChange(pendingShiftChange, RequestAnswer.ACCEPTED);
                }
        );

        final Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(
                e -> {
                    client.answerPendingShiftChange(pendingShiftChange, RequestAnswer.DENIED);
                }
        );

        final HBox buttons = new HBox();
        buttons.setSpacing(2);
        BorderPane.setAlignment(buttons, Pos.CENTER);
        buttons.getChildren().addAll(acceptButton, rejectButton);

        setCenter(fields);
        setRight(buttons);
    }
}
