import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


   public class manufacture {


    private static ObservableList<ObservableList<String>> data;
     static TableView tableView = new TableView<>();
     static boolean firstimecreating = true;
     static List<String> list_of_primary = new ArrayList<String>();



    public static void Builddata(String name,String type,String city,String country) throws SQLException{

        try{

         data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM manufacture ";

         boolean firstime = true;

         

         if (!name.isEmpty()) {
            if(firstime){ query += " WHERE name = '" + name + "'"; firstime=false; }
            else{ query += " AND    name  = '" + name + "'"; firstime=false;}
        }

        if (!type.isEmpty()) {
            if(firstime){ query += " WHERE type = '" + type + "'"; firstime=false; }
            else{ query += " AND    type  = '" + type + "'"; firstime=false;}
        }

         if (!city.isEmpty()) {
            if(firstime){ query += " WHERE city = '" + city + "'"; firstime=false; }
            else{ query += " AND    city  = '" + city + "'"; firstime=false;}
        }

         if (!country.isEmpty()) {
            if(firstime){ query += " WHERE country = '" + country + "'"; firstime=false; }
            else{ query += " AND    country  = '" + country + "'"; firstime=false;}
        }

        


        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);


         if(firstimecreating) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j));
                        }
                    });

                    tableView.getColumns().add(col);
                }
                firstimecreating=false;
            }



            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

        tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on name Data");
        }
    
    }


    private static void insertData(String name, String type, String city, String country) throws SQLException {
        
        data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "INSERT INTO manufacture (name, type, city, country) VALUES ('" + name + "', '" + type + "', '" + city + "', '" + country + "')";


    Statement stmt = c.createStatement();
    stmt.execute(query); 
    Builddata("", "", "", "");
  
}

    private static void showAlert(String title, String content) {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle(title);
     alert.setHeaderText(null);
     alert.setContentText(content);
     alert.showAndWait();
        }
   
    private static void updateData(String name, String type, String city, String country) throws SQLException {
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
    String query = "UPDATE manufacture SET type = ?, city = ?, Country = ? WHERE name = ?";
    
    try (PreparedStatement pstmt = c.prepareStatement(query)) {
        pstmt.setString(1, type);
        pstmt.setString(2, city);
        pstmt.setString(3, country);
        pstmt.setString(4, name);

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) { 
            showAlert("Update Successful", "Record updated successfully!");
            Builddata("", "", "", ""); 
        } else {
            showAlert("Update Failed", "Failed to update record. Make sure the record exists.");
        }
    }

    }
   
    private static void deleteData(String name) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "DELETE FROM manufacture WHERE name = ?";

        try (PreparedStatement pstmt = c.prepareStatement(query)) {
            pstmt.setString(1, name);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                showAlert("Delete Successful", name + " deleted successfully!");
                list_of_primary.remove(name);
                Builddata("", "", "", "");
            } else {
                showAlert("Delete Failed", "Failed to delete record. Make sure the record exists.");
            }
        }
    }

     



    public static VBox start3() throws Exception {


            Label label2 = new Label("name:");
            TextField namefield = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, namefield);
            hb2.setSpacing(10);

            Label label3 = new Label("type:");
            TextField typefield = new TextField ();
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(label3, typefield);
            hb3.setSpacing(10);

            HBox style1 = new HBox();
            style1.getChildren().addAll(hb2,hb3);
            style1.setSpacing(25);
            style1.setAlignment(Pos.CENTER);

            Label label4 = new Label("city:");
            TextField citylfield = new TextField ();
            HBox hb4 = new HBox();
            hb4.getChildren().addAll(label4, citylfield);
            hb4.setSpacing(10);

            Label label5 = new Label("country:");
            TextField countrylfield = new TextField ();
            HBox hb5 = new HBox();
            hb5.getChildren().addAll(label5, countrylfield);
            hb5.setSpacing(10);

            HBox style2 = new HBox();
            style2.getChildren().addAll(hb4,hb5);
            style2.setSpacing(25);
            style2.setAlignment(Pos.CENTER);


            
            Button search = new Button("Search");
            Button insert = new Button("Insert");
            Button update = new Button("Update");
            Button delete = new Button("Delete");
            HBox btns = new HBox();
            btns.setSpacing(14);
            btns.setAlignment(Pos.CENTER);
            btns.getChildren().addAll(search,insert,update,delete);


            search.setOnAction(e ->{
                try {
                    Builddata(namefield.getText(),typefield.getText(),citylfield.getText(),countrylfield.getText());
                    namefield.clear();typefield.clear();citylfield.clear();countrylfield.clear();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            insert.setOnAction(e ->{

                if (list_of_primary.contains(namefield.getText())) {
                    showAlert("Used primary Key", "This primary Key is already used make sure to fill another name. ");
                }else{
                    try {
                        insertData(namefield.getText(), typefield.getText(), citylfield.getText(), countrylfield.getText());
                        list_of_primary.add(namefield.getText());
                        Car.list.add(namefield.getText());
                        device.list_of_foreign.add(namefield.getText());
                        namefield.clear();typefield.clear();citylfield.clear();countrylfield.clear();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                

            });

            update.setOnAction(e ->{
                if (list_of_primary.contains(namefield.getText())){
                    try {
                        updateData(namefield.getText(), typefield.getText(), citylfield.getText(), countrylfield.getText());
                        namefield.clear();typefield.clear();citylfield.clear();countrylfield.clear();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    showAlert("Not available name field", "Please make sure to fill a Available Name field");
                }
            });

            delete.setOnAction(e ->{
                if (list_of_primary.contains(namefield.getText())) {
                    try {
                        deleteData(namefield.getText());
                        Car.list.remove(namefield.getText());
                        list_of_primary.remove(namefield.getText());
                        namefield.clear();typefield.clear();citylfield.clear();countrylfield.clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }else{
                    showAlert("Wrong Primary Key", "Please make sure to put a Available Name Field.");
                }
            });

            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT name FROM manufacture");
            while (res1.next()) {
                list_of_primary.add(res1.getString(1));
            }

            VBox v1 = new VBox();
            v1.setSpacing(12);
            VBox.setMargin(style1, new Insets(10));
            v1.getChildren().addAll(style1,style2,btns,tableView);
            return v1;
        }

  
           
    }

   
    


