package com.example.autificationsql;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hocin Student
 */
public class mainController {

    @FXML
    private TableView<User> userTable = new TableView<>();
    @FXML
    private TableColumn<User, Integer> idCol;
    @FXML
    private TableColumn<User, String> nameCol;
    @FXML
    private TableColumn<User, String> birthCol;
    @FXML
    private TableColumn<User, String> adressCol;
    @FXML
    private TableColumn<User, String> emailCol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    public Stage addStage = new Stage();
    public Stage editStage = new Stage();


    ObservableList<User>  UserList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
            loadDate();
    }




    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void getAddView() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Scene scene = new Scene(parent);

        addStage.setScene(scene);

        if(!addStage.isShowing()) {
              addStage.show();
            }
        else {
              addStage.setIconified(false);
              addStage.toFront();
        }
    }

    @FXML
    void refreshTable() throws SQLException {
            UserList.clear();

            query = "SELECT * FROM `new_table`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                UserList.add(new  User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDate("birth"),
                        resultSet.getString("adress"),
                        resultSet.getString("email")));
                userTable.setItems(UserList);

            }





    }

    @FXML
    void getEditView() {

            try {


                User user = userTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("hello-view.fxml"));
                loader.load();

                HelloController addStudentController = loader.getController();
                addStudentController.setUpdate(true);
                addStudentController.setTextField(user.getId(), user.getName(),
                        user.getBirth().toLocalDate(), user.getAdress(), user.getEmail());
                Parent parent = loader.getRoot();
                editStage.setScene(new Scene(parent));
                editStage.show();
            } catch (IOException e) {
                System.out.println("IOException");
            }catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Select the row to Edit");
                alert.showAndWait();
            }
    }

    @FXML
    void getDeleteView() {

            try {
                User user = userTable.getSelectionModel().getSelectedItem();
                DataBaseHandler dataBaseHandler = new DataBaseHandler();
                connection = dataBaseHandler.getDbConnection();
                query = "DELETE FROM `new_table` WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(user.getId()));
                preparedStatement.execute();
                refreshTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Select the row to Delete");
                alert.showAndWait();
            }


    }

    private void loadDate() throws SQLException, ClassNotFoundException {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        connection = dataBaseHandler.getDbConnection();


        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birth"));
        adressCol.setCellValueFactory(new PropertyValueFactory<>("adress"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));


    }

}