package ca.mohawk.jdw.shifty.desktopclient.myshiftofferrequests;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MyShiftOfferRequestPane extends BorderPane {

    private final ShiftOffer.Request request;

    public MyShiftOfferRequestPane(final ShiftOffer.Request request){
        this.request = request;
        getStyleClass().add("bordered");

        final LocalDateTime startDateTime = request.offer().shift().startTimestamp().toLocalDateTime();
        final LocalDateTime finishDateTime = request.offer().shift().finishTimestamp().toLocalDateTime();
        final Label dateLabel = new Label(startDateTime.format(DateTimeFormatter.ISO_DATE));
        if(!startDateTime.toLocalDate().equals(finishDateTime.toLocalDate()))
            dateLabel.setText(dateLabel.getText() + " - " + finishDateTime.format(DateTimeFormatter.ISO_DATE));
        dateLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final Label rangeLabel = new Label(String.format("%s - %s", startDateTime.format(DateTimeFormatter.ISO_TIME), finishDateTime.format(DateTimeFormatter.ISO_TIME)));
        BorderPane.setAlignment(rangeLabel, Pos.CENTER);
        rangeLabel.getStyleClass().add("cellLabel");

        final Label employeeLabel = new Label(String.format("Offered By: %s %s", request.offer().offeringEmployee().firstName(), request.offer().offeringEmployee().lastName()));
        BorderPane.setAlignment(employeeLabel, Pos.CENTER);
        employeeLabel.getStyleClass().add("cellLabel");

        final Label statusLabel = new Label();
        statusLabel.textProperty().bind(request.answerProperty().asString("Status: %s"));
        BorderPane.setAlignment(statusLabel, Pos.CENTER);
        statusLabel.getStyleClass().add("cellLabel");

        final VBox fields = new VBox();
        fields.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(fields, Pos.CENTER);
        fields.setSpacing(2);
        fields.getChildren().addAll(rangeLabel, employeeLabel, statusLabel);

        final BorderPane center = new BorderPane();
        BorderPane.setAlignment(center, Pos.CENTER);
        center.setCenter(dateLabel);
        center.setBottom(fields);

        final Button cancelButton = new Button("Cancel");
        BorderPane.setAlignment(cancelButton, Pos.TOP_RIGHT);
        BorderPane.setMargin(cancelButton, new Insets(0, 5, 0, 0));
        cancelButton.setOnAction(e -> client.cancelRequestToTakeShift(request));

        request.answerProperty().addListener(
                (ob, o, n) -> {
                    getStyleClass().removeAll("red", "green", "yellow", "orange");
                    switch(n){
                        case ACCEPTED:
                            getStyleClass().add("green");
                            cancelButton.setText("Accepted");
                            cancelButton.setDisable(true);
                            break;
                        case DENIED:
                            getStyleClass().add("red");
                            break;
                        default:
                            cancelButton.setText("Cancel");
                            cancelButton.setDisable(false);
                    }
                }
        );

        final RequestAnswer answer = request.answer();
        request.answer(answer == RequestAnswer.ACCEPTED ? RequestAnswer.DENIED : RequestAnswer.ACCEPTED);
        request.answer(answer);

        setCenter(center);
        setRight(cancelButton);
    }
}
