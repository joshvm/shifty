package ca.mohawk.jdw.shifty.desktopclient.myactivitylogs;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ActivityLog;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MyActivityLogsPane extends BorderPane {

    private static class MyActivityLogListCell extends ListCell<ActivityLog> {

        @Override
        protected void updateItem(final ActivityLog log, final boolean empty){
            super.updateItem(log, empty);
            if(log == null)
                return;
            setGraphic(new MyActivityLogPane(log));
        }
    }

    private final TextField filterBox;

    private final ListView<ActivityLog> list;

    private final TextArea selectedInfoArea;

    public MyActivityLogsPane(){
        getStylesheets().add(UI.css("contentPane"));

        final Label placeholder = new Label("No Activity Logs");
        placeholder.getStyleClass().add("placeholderLabel");

        list = new ListView<>(client.myActivityLogs().sorted(Comparator.comparing(ActivityLog::timestamp)));
        list.setCellFactory(param -> new MyActivityLogListCell());
        list.setPlaceholder(placeholder);

        selectedInfoArea = new TextArea();
        selectedInfoArea.setEditable(false);
        selectedInfoArea.setWrapText(true);
        selectedInfoArea.setPrefRowCount(5);

        list.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, o, n) -> {
                    if(n != null){
                        selectedInfoArea.setText(
                                String.format(
                                        "Timestamp: %s %s%n%n%s",
                                        n.timestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE),
                                        n.timestamp().toLocalDateTime().format(DateTimeFormatter.ISO_TIME),
                                        n.message()
                                )
                        );
                    }else
                        selectedInfoArea.setText("");
                });

        filterBox = new TextField();
        filterBox.setPromptText("Filter Activity Logs");
        filterBox.textProperty().addListener(
                (ob, o, n) -> {
                    final String query = n.toLowerCase();
                    list.setItems(client.myActivityLogs().list()
                            .filtered(l -> l.message().toLowerCase().contains(query))
                            .sorted(Comparator.comparing(ActivityLog::timestamp))
                    );
                }
        );

        setTop(filterBox);
        setCenter(list);
        setBottom(selectedInfoArea);
    }
}
