package com.example.autificationsql;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.time.LocalDate;

public class HelloController {

    @FXML
    private TextField adressFld;

    @FXML
    private DatePicker birthFld;

    @FXML
    private TextField emailFld;

    @FXML
    private TextField nameFld;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    private boolean update;
    int userId;

    @FXML
    void clean(MouseEvent event) {
        clean();
    }

    @FXML
    void save() throws SQLException, ClassNotFoundException {

        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        connection = dataBaseHandler.getDbConnection();

        String name = nameFld.getText();
        String birth = String.valueOf(birthFld.getValue());
        String adress = adressFld.getText();
        String email = emailFld.getText();

        if(
                (nameFld.getText()==null||name.isEmpty())||
                        (birthFld.getValue()==null||birth.isEmpty())||
                                (adressFld.getText()==null||adress.isEmpty())||
                                        (emailFld.getText()==null||email.isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All Data");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();
        }
    }

    private void clean() {
        nameFld.setText(null);
        birthFld.setValue(null);
        adressFld.setText(null);
        emailFld.setText(null);
    }

    private void insert() throws SQLException {

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,nameFld.getText());
        preparedStatement.setString(2, String.valueOf(birthFld.getValue()));
        preparedStatement.setString(3,adressFld.getText());
        preparedStatement.setString(4,emailFld.getText());
        preparedStatement.execute();

    }

    private void getQuery() {
        if (!update) {

            query = "INSERT INTO `new_table`( `name`, `birth`, `adress`, `email`) VALUES (?,?,?,?)";

        }else{
            query = "UPDATE `new_table` SET "
                    + "`name`=?,"
                    + "`birth`=?,"
                    + "`adress`=?,"
                    + "`email`= ? WHERE id = '"+userId+"'";
        }
    }

    public void setUpdate(boolean b) {
        this.update = b;
    }

    public void setTextField(int id, String name, LocalDate toLocalDate, String adress, String email) {
        userId = id;
        nameFld.setText(name);
        birthFld.setValue(toLocalDate);
        adressFld.setText(adress);
        emailFld.setText(email);
    }


}
