package com.example.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;

public class DBUtils {
    public static void ChangeScene(ActionEvent action, String username, String favChannel, String title, String fxmlfile){
        Parent root = null;

        if(username != null && favChannel != null ){
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlfile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setLabel(username, favChannel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlfile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }
    public static void signUp(String username, String password, String favChannel,ActionEvent actionEvent){
    Connection connection = null;
    PreparedStatement prInsert = null;
    PreparedStatement CheckUser = null;
    ResultSet resultSet = null;

    try{
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/usersdata", "root" , "sandwish#");
        CheckUser = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        CheckUser.setString(1,username);
        resultSet = CheckUser.executeQuery();

        if(resultSet.isBeforeFirst()){
            System.out.println("User already exists!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You cannot sign up with this username");
            alert.show();
        }
        else{
            prInsert = connection.prepareStatement("INSERT INTO users (username, password, favBloger VALUES (?, ?, ?)");
            prInsert.setString(1,username);
            prInsert.setString(2,password);
            prInsert.setString(3,favChannel);

            prInsert.executeUpdate();

            ChangeScene(actionEvent,username,favChannel,"Welcome","LoggedIn.fxml");

        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        if(resultSet != null){
            try{
                resultSet.close();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

        if(CheckUser != null){
            try{
                CheckUser.close();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        if(prInsert != null){
            try{
                prInsert.close();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        if(connection != null){
            try{
                connection.close();
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }

    }
    public static void logIn(ActionEvent actionEvent,String username, String password){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/usersdata", "root" , "sandwish#");
            ps = connection.prepareStatement("SELECT password,favBloger WHERE username = ?");
            ps.setString(1,username);
            resultSet = ps.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("Incorrect information is typed");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter correct username and password");
                alert.show();
            }
            else{
                while(resultSet.next()){
                    String respass = resultSet.getString("password");
                    String resfavC = resultSet.getString("favBloger");
                    if(respass.equals(password)){
                        ChangeScene(actionEvent,resfavC,username,"Welcome","LoggedIn.fxml");
                    }
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if(resultSet != null){
                try{
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(ps != null){
                try{
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
