package ca.mohawk.jdw.shifty.desktopclient.searchactivitylogs;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ActivityLog;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

public class SearchedActivityLogPane extends BorderPane {

    private final ActivityLog log;

    public SearchedActivityLogPane(final ActivityLog log){
        this.log = log;

        final Label label = new Label(String.format("[%s %s - %s %s] %s", log.timestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE), log.timestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME), log.employee().firstName(), log.employee().lastName(), log.message()));
        label.setTextAlignment(TextAlignment.LEFT);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        label.setAlignment(Pos.CENTER_LEFT);

        setCenter(label);
    }
}
