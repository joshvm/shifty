package ca.mohawk.jdw.shifty.desktopclient.ui;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.event.response.Response;
import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseCode;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

public final class UI {

    private UI(){}

    public static String css(final String name){
        return UI.class.getResource("css/" + name + ".css").toExternalForm();
    }

    public static Node emptyWidth(final int width){
        final Pane pane = new Pane();
        pane.setMaxWidth(width);
        pane.setMinWidth(width);
        pane.setPrefWidth(width);
        return pane;
    }

    public static Node emptyHeight(final int height){
        final Pane pane = new Pane();
        pane.setMaxHeight(height);
        pane.setMinHeight(height);
        pane.setPrefHeight(height);
        return pane;
    }

    public static void alert(final Alert.AlertType type,
                             final String title,
                             final String header,
                             final String contentFmt,
                             final Object... contentArgs){
        Platform.runLater(() -> {
            final Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(String.format(contentFmt, contentArgs));
            alert.show();
        });
    }

    public static void alertResponse(final Response response){
        String codeName;
        String msg;
        Alert.AlertType type = Alert.AlertType.WARNING;
        if(response.code().getClass().equals(ResponseCode.class)){
            codeName = response.code() == ResponseCode.ERROR ? "Error" : "Unknown";
            msg = response.code() == ResponseCode.ERROR ? "Server Error - Could Not Complete Request"
                    : "Unknown Response Code";
            type = Alert.AlertType.ERROR;
        }else{
            codeName = response.code().getClass().getSimpleName().replace("ResponseCode", "");
            msg = String.format("%s (ID: %d)", response.code(), response.code().id());
        }
        alert(type, "Response", codeName, msg);
    }
}
