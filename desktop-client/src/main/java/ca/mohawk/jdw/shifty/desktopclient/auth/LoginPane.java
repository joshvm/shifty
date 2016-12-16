package ca.mohawk.jdw.shifty.desktopclient.auth;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.auth.LoginData;
import ca.mohawk.jdw.shifty.clientapi.auth.LoginResponse;
import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseCode;
import ca.mohawk.jdw.shifty.desktopclient.DesktopClient;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class LoginPane extends BorderPane {

    private final ShiftyClient.Config config;
    private final TitlePane titlePane;

    private final TextField userBox;
    private final PasswordField passBox;

    private final Button loginButton;

    public LoginPane(final ShiftyClient.Config config){
        this.config = config;
        getStylesheets().add(UI.css("loginPane"));

        final Label titleLabel = new Label("Shifty");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        titleLabel.getStyleClass().add("title");

        titlePane = new TitlePane(config);

        final Label promptUserLabel = new Label("Username/ID: ");
        promptUserLabel.getStyleClass().add("promptLabel");

        userBox = new TextField();

        final Label promptPassLabel = new Label("Password: ");
        promptPassLabel.getStyleClass().add("promptLabel");

        passBox = new PasswordField();

        final Label loginDetailsLabel = new Label("Login Details");
        loginDetailsLabel.getStyleClass().add("loginDetails");
        BorderPane.setAlignment(loginDetailsLabel, Pos.CENTER);

        final Separator separator = new Separator(Orientation.HORIZONTAL);
        BorderPane.setAlignment(separator, Pos.CENTER);

        final BorderPane loginDetailsPane = new BorderPane();
        loginDetailsPane.setCenter(loginDetailsLabel);
        loginDetailsPane.setBottom(separator);

        final GridPane fields = new GridPane();
        fields.setAlignment(Pos.CENTER);
        GridPane.setHalignment(promptUserLabel, HPos.RIGHT);
        GridPane.setMargin(promptUserLabel, new Insets(0, 2, 0, 0));
        fields.add(promptUserLabel, 1, 1);
        fields.add(userBox, 2, 1, 2, 1);
        fields.addRow(2, UI.emptyHeight(3));
        GridPane.setHalignment(promptPassLabel, HPos.RIGHT);
        fields.add(promptPassLabel, 1, 3);
        fields.add(passBox, 2, 3, 2, 1);

        final BorderPane fieldsPane = new BorderPane();
        BorderPane.setMargin(loginDetailsPane, new Insets(0, 0, 3, 0));
        fieldsPane.setTop(loginDetailsPane);
        fieldsPane.setCenter(fields);

        loginButton = new Button("Login");
        BorderPane.setAlignment(loginButton, Pos.CENTER);
        loginButton.getStyleClass().add("loginButton");
        loginButton.setOnAction(e -> {
            final String user = userBox.getText();
            final String pass = passBox.getText();
            final LoginData data = new LoginData(user, pass);
            ResponseCode code = null;
            try{
                final LoginResponse response = ShiftyClient.login(config, data);
                code = response.code();
                if(code != ResponseCode.SUCCESS)
                    throw new Exception("Unsuccessful login: " + code.id());
                userBox.setText("");
                passBox.setText("");
                DesktopClient.toMainScene(response.client());
            }catch(Exception err){
                err.printStackTrace();
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Login Error");
                if(code == null || code == ResponseCode.ERROR){ //error processing login request
                    errorAlert.setContentText("Error sending login request");
                }else if(code == ResponseCode.UNKNOWN){
                    errorAlert.setContentText("Unknown login response");
                }else{
                    if(code instanceof LoginResponse.Code)
                        errorAlert.setContentText(code.toString());
                    else
                        errorAlert.setContentText("Response: " + code.id());
                }
                errorAlert.show();
            }
        });

        final BorderPane bottom = new BorderPane();
        BorderPane.setMargin(bottom, new Insets(0, 0, 5, 0));
        bottom.setCenter(fieldsPane);
        BorderPane.setMargin(loginButton, new Insets(5, 0, 0, 0));
        bottom.setBottom(loginButton);

        setTop(titleLabel);
        setCenter(titlePane);
        setBottom(bottom);
    }
}
