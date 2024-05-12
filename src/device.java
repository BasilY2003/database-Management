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


   public class device {


    private static ObservableList<ObservableList<String>> data;
     static TableView tableView = new TableView<>();
     private static boolean firstimecreating = true;
     static List<String> list_of_primary = new ArrayList<String>();
     static List<String> list_of_foreign = new ArrayList<String>();



    public static void Builddata(String no,String name,String price,String weight,String made) throws SQLException{

        try{

         data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM device ";

         boolean firstime = true;

         if (!no.isEmpty()) {
            if(firstime){ query += " WHERE no = '" + no + "'"; firstime=false; }
            else{ query += " AND    no  = '" + no + "'"; firstime=false;}
        }

         if (!name.isEmpty()) {
            if(firstime){ query += " WHERE name = '" + name + "'"; firstime=false; }
            else{ query += " AND    name  = '" + name + "'"; firstime=false;}
        }

        if (!price.isEmpty()) {
            if(firstime){ query += " WHERE price = '" + price + "'"; firstime=false; }
            else{ query += " AND    price  = '" + price + "'"; firstime=false;}
        }

         if (!weight.isEmpty()) {
            if(firstime){ query += " WHERE weight = '" + weight + "'"; firstime=false; }
            else{ query += " AND    weight  = '" + weight + "'"; firstime=false;}
        }

         if (!made.isEmpty()) {
            if(firstime){ query += " WHERE made = '" + made + "'"; firstime=false; }
            else{ query += " AND    made  = '" + made + "'"; firstime=false;}
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

    private static void insertData(String id, String name, String price, String weight,String made) throws SQLException {
        
        data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "INSERT INTO device (no, name, price, weight, made) VALUES ('" + id + "', '" + name + "', '" + price + "', '" + weight + "', '" + made + "')";


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
   
    private static void updateData(String no, String name, String price, String weight, String made) throws SQLException {
    try {

        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
   
        String updateQuery = "UPDATE device SET name=?, price=?, weight=?, made=? WHERE no=?";
        try (PreparedStatement preparedStatement = c.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, price);
            preparedStatement.setString(3, weight);
            preparedStatement.setString(4, made);
            preparedStatement.setString(5, no);
            preparedStatement.executeUpdate();

            Builddata("", "", "", "", "");

            System.out.println("Record with id " + no + " updated successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private static void deleteData(String nofield) throws SQLException {
       Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
       String query = "DELETE FROM device WHERE no = ?";

     try (PreparedStatement pstmt = c.prepareStatement(query)) { 
     pstmt.setString(1, nofield);

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

  
            Label label1 = new Label("no:");
            TextField nofield = new TextField ();
            HBox hb = new HBox();
            hb.getChildren().addAll(label1, nofield);
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);


            Label label2 = new Label("name:");
            TextField namefield = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, namefield);
            hb2.setSpacing(10);

            Label label3 = new Label("price:");
            TextField pricefield = new TextField ();
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(label3, pricefield);
            hb3.setSpacing(10);

            HBox style1 = new HBox();
            style1.getChildren().addAll(hb2,hb3);
            style1.setSpacing(25);
            style1.setAlignment(Pos.CENTER);

            Label label4 = new Label("weight:");
            TextField weightlfield = new TextField ();
            HBox hb4 = new HBox();
            hb4.getChildren().addAll(label4, weightlfield);
            hb4.setSpacing(10);

            Label label5 = new Label("made:");
            TextField madelfield = new TextField ();
            HBox hb5 = new HBox();
            hb5.getChildren().addAll(label5, madelfield);
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
                    Builddata(nofield.getText(),namefield.getText(),pricefield.getText(),weightlfield.getText(),madelfield.getText());
                    nofield.clear();namefield.clear();pricefield.clear();weightlfield.clear();madelfield.clear();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            insert.setOnAction(e ->{

                if (list_of_primary.contains(nofield.getText())){
                   showAlert("used no field", "This primary key is already used");
                }else{
                        if (list_of_foreign.contains(madelfield.getText())){ 
                            try {
                                insertData(nofield.getText(), namefield.getText(), pricefield.getText(), weightlfield.getText(), madelfield.getText());
                                list_of_primary.add(nofield.getText());
                                Car_par.list_of_parts.add(nofield.getText());
                                nofield.clear();namefield.clear();pricefield.clear();weightlfield.clear();madelfield.clear();

                            } catch (SQLException e1) {
                            }
                        }else{
                            showAlert("made field is not accurate", "Please put in made field a foreign key value");
                        }
                }


            });

            update.setOnAction(e ->{
                if (!list_of_primary.contains(nofield.getText())) {
                    showAlert("The No field is not available", "Make sure to put a available No field");
                }else{
                    if (list_of_foreign.contains(madelfield.getText())) {
                        try {
                            updateData(nofield.getText(), namefield.getText(), pricefield.getText(), weightlfield.getText(), madelfield.getText());
                            nofield.clear();namefield.clear();pricefield.clear();weightlfield.clear();madelfield.clear();

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        showAlert("Wrong foreign Key", "Please Enter a Correct Foreign key");
                    }
                }
            });

            delete.setOnAction(e ->{
                if (list_of_primary.contains(nofield.getText())) {
                    try {
                        deleteData(nofield.getText());
                        list_of_primary.remove(nofield.getText());
                        Car_par.Builddata("", "");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }       
                }else{
                    showAlert("Not accurate No field", "Please make sure to put a Available No field.");
                }
            });






            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT no FROM device");
            while (res1.next()) {
                list_of_primary.add(res1.getString(1));
            }

             Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt2 = con2.createStatement();
            ResultSet res2 = stmt2.executeQuery("SELECT name FROM manufacture");
            while (res2.next()) {
                list_of_foreign.add(res2.getString(1));
            }




            VBox v1 = new VBox();
            v1.setSpacing(14);
            VBox.setMargin(hb, new Insets(14));
            v1.getChildren().addAll(hb,style1,style2,btns,tableView);
            return v1;
        }

  
           
    }

   
    


