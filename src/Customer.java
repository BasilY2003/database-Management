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


   public class Customer {


    private static ObservableList<ObservableList<String>> data;
     static TableView tableView = new TableView<>();
     private static boolean firstimecreating = true;
     static List<String> list_of_primary1 = new ArrayList<String>();
     static List<String> list_of_foreign1 = new ArrayList<String>();


    public static void Builddata(String id,String F_name,String L_name,String address,String job) throws SQLException{

        try{

         data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM customer ";

         boolean firstime = true;

         if (!id.isEmpty()) {
            if(firstime){ query += " WHERE id = '" + id + "'"; firstime=false; }
            else{ query += " AND    id  = '" + id + "'"; firstime=false;}
        }

         if (!F_name.isEmpty()) {
            if(firstime){ query += " WHERE F_name = '" + F_name + "'"; firstime=false; }
            else{ query += " AND    F_name  = '" + F_name + "'"; firstime=false;}
        }

        if (!L_name.isEmpty()) {
            if(firstime){ query += " WHERE L_name = '" + L_name + "'"; firstime=false; }
            else{ query += " AND    L_name  = '" + L_name + "'"; firstime=false;}
        }

         if (!address.isEmpty()) {
            if(firstime){ query += " WHERE address = '" + address + "'"; firstime=false; }
            else{ query += " AND    address  = '" + address + "'"; firstime=false;}
        }

         if (!job.isEmpty()) {
            if(firstime){ query += " WHERE job = '" + job + "'"; firstime=false; }
            else{ query += " AND    job  = '" + job + "'"; firstime=false;}
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
            System.out.println("Error on F_name Data");
        }
    
    }

    private static void insertData(String id, String f_name, String l_name, String address,String job) throws SQLException {
        
        data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "INSERT INTO customer (id, f_name, l_name, address,job) VALUES ('" + id + "', '" + f_name + "', '" + l_name + "', '" + address + "', '" + job + "')";


    Statement stmt = c.createStatement();
    stmt.execute(query); 
    Builddata("", "", "", "","");
  
}

    private static void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}   

    private static void updateData(String idToUpdate, String fname, String lname, String address, String job) throws SQLException {
    try {

        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
   
        String updateQuery = "UPDATE customer SET f_name=?, l_name=?, address=?, job=? WHERE id=?";
        try (PreparedStatement preparedStatement = c.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, lname);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, job);
            preparedStatement.setString(5, idToUpdate);
            preparedStatement.executeUpdate();

            Builddata("", "", "", "", "");

            System.out.println("Record with id " + idToUpdate + " updated successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private static void deleteData(String idfield) throws SQLException {
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
    String query = "DELETE FROM customer WHERE id = ?";

     try (PreparedStatement pstmt = c.prepareStatement(query)) { 
     pstmt.setString(1, idfield);

     int rowsDeleted = pstmt.executeUpdate();

     if (rowsDeleted > 0) {
         showAlert("Delete Successful", "Record deleted successfully!");
         Builddata("", "","","","");
     } else {
         showAlert("Delete Failed", "Failed to delete record. Make sure the record exists.");
     }
 }
}





    public static VBox start3() throws Exception {

  
            Label label1 = new Label("id:");
            TextField idfield = new TextField ();
            HBox hb = new HBox();
            hb.getChildren().addAll(label1, idfield);
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);


            Label label2 = new Label("F_name:");
            TextField F_namefield = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, F_namefield);
            hb2.setSpacing(10);


            Label label3 = new Label("L_name:");
            TextField L_namefield = new TextField ();
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(label3, L_namefield);
            hb3.setSpacing(10);

            HBox style1 = new HBox();
            style1.getChildren().addAll(hb2,hb3);
            style1.setSpacing(25);
            style1.setAlignment(Pos.CENTER);

            Label label4 = new Label("address:");
            TextField addresslfield = new TextField ();
            HBox hb4 = new HBox();
            hb4.getChildren().addAll(label4, addresslfield);
            hb4.setSpacing(10);

            Label label5 = new Label("job:");
            TextField joblfield = new TextField ();
            HBox hb5 = new HBox();
            hb5.getChildren().addAll(label5, joblfield);
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
            btns.setSpacing(10);
            btns.setAlignment(Pos.CENTER);
            btns.getChildren().addAll(search,insert,update,delete);



            search.setOnAction(e -> {
                try {
                    Builddata(idfield.getText(),F_namefield.getText(),L_namefield.getText(),addresslfield.getText(),joblfield.getText());
                    idfield.clear(); F_namefield.clear(); L_namefield.clear(); addresslfield.clear();joblfield.clear();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            insert.setOnAction(e ->{

                if (list_of_primary1.contains(idfield.getText())){
                        showAlert("Used Primary key","This id is already Used make sure to use another one.");
                }else{
                    if (list_of_foreign1.contains(addresslfield.getText())){
                           try {
                            insertData(idfield.getText(),F_namefield.getText(),L_namefield.getText(),addresslfield.getText(),joblfield.getText());
                            list_of_primary1.add(idfield.getText());
                            order.list_of_customer.add(idfield.getText());
                            idfield.clear(); F_namefield.clear(); L_namefield.clear(); addresslfield.clear();joblfield.clear();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        showAlert("This foreign key is wrong", "Please enter a foreign key value.");
                    }
                }

            });

            update.setOnAction(e ->{
                   if (!list_of_primary1.contains(idfield.getText())){
                    showAlert("Not available Primary Key", "Please make sure to enter a available id");
                   }else{
                    if (list_of_foreign1.contains(addresslfield.getText())) {
                        try {
                            updateData(idfield.getText(), F_namefield.getText(), L_namefield.getText(), addresslfield.getText(), joblfield.getText());
                            idfield.clear(); F_namefield.clear(); L_namefield.clear(); addresslfield.clear();joblfield.clear();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        showAlert("Not available ", "Please make sure to Update to available Address");
                    }
                   }
            });
            
            delete.setOnAction(e ->{
                if (list_of_primary1.contains(idfield.getText())) {
                    try {
                        deleteData(idfield.getText());
                        list_of_primary1.remove(idfield.getText());
                        Builddata("","","","","");
                        order.Builddata("","","","");
                        idfield.clear(); F_namefield.clear(); L_namefield.clear(); addresslfield.clear();joblfield.clear();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    showAlert("Inaccurate id", "Please make sure to put a available ID");
                }
            });



            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT id FROM customer");
            while (res1.next()) {
                list_of_primary1.add(res1.getString(1));
            }

             Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt2 = con2.createStatement();
            ResultSet res2 = stmt2.executeQuery("SELECT id FROM address");
            while (res2.next()) {
                list_of_foreign1.add(res2.getString(1));
            }









            VBox v1 = new VBox();
            v1.setSpacing(14);
            VBox.setMargin(style1, new Insets(10));
            v1.getChildren().addAll(style1,style2,hb,btns,tableView);
            return v1;
        }

  
           
    }

   
    


