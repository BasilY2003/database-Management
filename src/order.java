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


   public class order {


    private static ObservableList<ObservableList<String>> data;
     static TableView tableView = new TableView<>();
     private static boolean firstimecreating = true;
     static List<String> list_of_primary = new ArrayList<String>();
     static List<String> list_of_customer = new ArrayList<String>();
     static List<String> list_of_car = new ArrayList<String>();



    public static void Builddata(String id,String date,String customer,String car) throws SQLException{

        try{

         data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM orders ";

         boolean firstime = true;

         

         if (!id.isEmpty()) {
            if(firstime){ query += " WHERE id = '" + id + "'"; firstime=false; }
            else{ query += " AND    id  = '" + id + "'"; firstime=false;}
        }

        if (!date.isEmpty()) {
            if(firstime){ query += " WHERE date = '" + date + "'"; firstime=false; }
            else{ query += " AND    date  = '" + date + "'"; firstime=false;}
        }

         if (!customer.isEmpty()) {
            if(firstime){ query += " WHERE customer = '" + customer + "'"; firstime=false; }
            else{ query += " AND    customer  = '" + customer + "'"; firstime=false;}
        }

         if (!car.isEmpty()) {
            if(firstime){ query += " WHERE car = '" + car + "'"; firstime=false; }
            else{ query += " AND    car  = '" + car + "'"; firstime=false;}
        }

        


        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);


        



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
            System.out.println("Error on id Data");
        }
    
    }

    private static void showAlert(String title, String content) {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle(title);
     alert.setHeaderText(null);
     alert.setContentText(content);
     alert.showAndWait();
        }

    private static void insertData(String id, String date, String customer, String car) throws SQLException {
        
            data = FXCollections.observableArrayList();
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            String query = "INSERT INTO orders (id, date, customer, car) VALUES ('" + id + "', '" + date + "', '" + customer + "', '" + car + "')";
    
    
        Statement stmt = c.createStatement();
        stmt.execute(query); 
        Builddata("", "", "", "");
      
    }

    private static void updateData(String id, String date, String customer, String car) throws SQLException {
    try {

        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
   
        String updateQuery = "UPDATE orders SET date=?, customer=?, car=? WHERE id=?";
        try (PreparedStatement preparedStatement = c.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, customer);
            preparedStatement.setString(3, car);
            preparedStatement.setString(4, id);
            preparedStatement.executeUpdate();

            Builddata("", "", "", "");

            System.out.println("Record with id " + id + " updated successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private static void deleteData(String id) throws SQLException {
       Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
       String query = "DELETE FROM orders WHERE id = ?";

    try (PreparedStatement pstmt = c.prepareStatement(query)) {
        pstmt.setString(1, id);
        int rowsDeleted = pstmt.executeUpdate();

        if (rowsDeleted > 0) {
            showAlert("Delete Successful", id + " deleted successfully!");
            list_of_primary.remove(id);
            Builddata("", "", "", "");
        } else {
            showAlert("Delete Failed", "Failed to delete record. Make sure the record exists.");
        }
    }
}




    public static VBox start3() throws Exception {


            Label label2 = new Label("id:");
            TextField idfield = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, idfield);
            hb2.setSpacing(10);

            Label label3 = new Label("date:");
            TextField datefield = new TextField ();
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(label3, datefield);
            hb3.setSpacing(10);

            HBox style1 = new HBox();
            style1.getChildren().addAll(hb2,hb3);
            style1.setSpacing(25);
            style1.setAlignment(Pos.CENTER);

            Label label4 = new Label("customer:");
            TextField customerlfield = new TextField ();
            HBox hb4 = new HBox();
            hb4.getChildren().addAll(label4, customerlfield);
            hb4.setSpacing(10);

            Label label5 = new Label("car:");
            TextField carlfield = new TextField ();
            HBox hb5 = new HBox();
            hb5.getChildren().addAll(label5, carlfield);
            hb5.setSpacing(10);

            HBox style2 = new HBox();
            style2.getChildren().addAll(hb4,hb5);
            style2.setSpacing(25);
            style2.setAlignment(Pos.CENTER);


            
            Button search = new Button("Search");
            Button insert = new Button("Insert");
            Button update = new Button("Update");
            Button delete = new Button("Delete");
            HBox btn = new HBox();
            btn.setSpacing(12);
            btn.setAlignment(Pos.CENTER);
            btn.getChildren().addAll(search,insert,update,delete);

            search.setOnAction(e -> {
                try {
                    Builddata(idfield.getText(),datefield.getText(),customerlfield.getText(),carlfield.getText());
                    idfield.clear();datefield.clear();customerlfield.clear();carlfield.clear();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            insert.setOnAction(e ->{
                if (idfield.getText().isEmpty() || datefield.getText().isEmpty() || customerlfield.getText().isEmpty() || carlfield.getText().isEmpty()) {
                  showAlert("Empty Fields", "One of the Fields are emty");                
                }else{
                   if(!list_of_primary.contains(idfield.getText())){
                    
                    if(list_of_car.contains(carlfield.getText()) && list_of_customer.contains(customerlfield.getText())){
                        try {
                            insertData(idfield.getText(), datefield.getText(), customerlfield.getText(), carlfield.getText());
                            list_of_primary.add(idfield.getText());
                            idfield.clear();datefield.clear();customerlfield.clear();carlfield.clear();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        showAlert("Wrong Foreign Key", "Make sure to fill the Car and Customer field the available Foreigne Keys. ");
                    }

                   }else{
                    showAlert("Wrong Primary key", "Make sure to fill the text field a Correct Primary Key");
                   }

                }
            });

            update.setOnAction(e ->{
                if (list_of_primary.contains(idfield.getText())) {
                    if (list_of_customer.contains(customerlfield.getText()) && list_of_car.contains(carlfield.getText())) {
                        try {
                            updateData(idfield.getText(), datefield.getText(), customerlfield.getText(), carlfield.getText());
                            idfield.clear();datefield.clear();customerlfield.clear();carlfield.clear();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        showAlert("Wrong Foreign Key", "Please make sure to enter a available primary key");
                    }
                    
                }else{
                    showAlert("Wrong primary Key", "Please make sure to Enter a available Primary Key ");
                }

            });

            delete.setOnAction(e ->{
                if (list_of_primary.contains(idfield.getText())){
                try {
                    deleteData(idfield.getText());
                    idfield.clear();datefield.clear();customerlfield.clear();carlfield.clear();
                    Car_par.Builddata("", "");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                }else{
                    showAlert("Wrong id field", "Not available Primary Key");
                }
            });
            // Primary Key
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT id FROM orders");
            while (res1.next()) {
                list_of_primary.add(res1.getString(1));
            }

            Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt2 = con2.createStatement();
            ResultSet res2 = stmt2.executeQuery("SELECT id FROM customer");
            while (res2.next()) {
                list_of_customer.add(res2.getString(1));
            }

            Connection con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt3 = con3.createStatement();
            ResultSet res3 = stmt3.executeQuery("SELECT name FROM car");
            while (res3.next()) {
                list_of_car.add(res3.getString(1));
            }




            VBox v1 = new VBox();
            v1.setSpacing(14);
            VBox.setMargin(style1, new Insets(12));
            v1.getChildren().addAll(style1,style2,btn,tableView);
            return v1;
        }

  
           
    }

   
    


