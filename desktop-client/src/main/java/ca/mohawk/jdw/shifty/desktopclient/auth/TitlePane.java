package ca.mohawk.jdw.shifty.desktopclient.auth;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TitlePane extends BorderPane {

    private final ShiftyClient.Config config;

    private final Label nameLabel;
    private final Label phoneLabel;
    private final Label emailLabel;

    public TitlePane(final ShiftyClient.Config config){
        this.config = config;
        getStylesheets().add(UI.css("titlePane"));

        final Tooltip addressTip = new Tooltip();
        addressTip.setText(
                String.format(
                        "%d %s\n%s, %s",
                        config.company().address().unit(),
                        config.company().address().street(),
                        config.company().address().city(),
                        config.company().address().country()
                )
        );

        nameLabel = new Label(config.company().name());
        nameLabel.getStyleClass().add("name");
        nameLabel.setTooltip(addressTip);

        phoneLabel = new Label(config.company().phone());

        emailLabel = new Label(config.company().email());

        final VBox labels = new VBox();
        labels.setAlignment(Pos.CENTER);
        labels.getChildren().addAll(nameLabel, phoneLabel, emailLabel);

        setCenter(labels);
    }
}
