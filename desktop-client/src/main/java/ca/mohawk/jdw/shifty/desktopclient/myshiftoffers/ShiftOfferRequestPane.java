package ca.mohawk.jdw.shifty.desktopclient.myshiftoffers;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class ShiftOfferRequestPane extends BorderPane {

    private final ShiftOffer.Request request;

    public ShiftOfferRequestPane(final ShiftOffer.Request request){
        this.request = request;

        final Label nameLabel = new Label(String.format("%s %s", request.employee().firstName(), request.employee().lastName()));
        nameLabel.getStyleClass().addAll("cellLabel", "cellBiggerLabel");

        final Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> client.answerRequestFromMyShiftOffer(request, RequestAnswer.ACCEPTED));

        final Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> client.answerRequestFromMyShiftOffer(request, RequestAnswer.DENIED));

        final Label statusLabel = new Label();
        statusLabel.textProperty().bind(request.answerProperty().asString("(%s)"));
        statusLabel.getStyleClass().add("cellLabel");

        final HBox labels = new HBox();
        labels.setAlignment(Pos.CENTER);
        labels.setSpacing(2);
        BorderPane.setAlignment(labels, Pos.CENTER);
        labels.getChildren().addAll(nameLabel, statusLabel);

        final HBox buttons = new HBox();
        buttons.setSpacing(2);
        BorderPane.setAlignment(buttons, Pos.CENTER);
        buttons.getChildren().addAll(acceptButton, rejectButton);

        request.answerProperty().addListener(
                (ob, o, n) -> {
                    getStyleClass().removeAll("red", "green", "orange", "yellow");
                    buttons.setVisible(true);
                    switch(n){
                        case ACCEPTED:
                            getStyleClass().add("green");
                            buttons.setVisible(false);
                            break;
                        case DENIED:
                            getStyleClass().add("red");
                            buttons.setVisible(false);
                            break;
                    }
                }
        );

        final RequestAnswer answer = request.answer();
        request.answer(answer == RequestAnswer.ACCEPTED ? RequestAnswer.DENIED : RequestAnswer.ACCEPTED);
        request.answer(answer);

        setCenter(labels);
        setRight(buttons);
    }
}
