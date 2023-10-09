package com.example.application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    @FXML
    private RadioButton rb_java;

    @FXML
    private RadioButton rb_c;

    @FXML
    private Button btn_signup;

    @FXML
    private Button btn_login;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_java.setToggleGroup(toggleGroup);
        rb_c.setToggleGroup(toggleGroup);

        rb_java.setSelected(true);

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String toggle = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    DBUtils.signUp(tf_username.getText(), tf_password.getText(),toggle,actionEvent);
                }
                else{
                    System.out.println("Please fill all the fields");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill all the fields");
                    alert.show();
                }
            }
        });
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.ChangeScene(actionEvent,null,null,"LogIn","sample.fxml");
            }
        });

    }
}
