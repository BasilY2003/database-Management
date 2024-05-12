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


public class Car_par {


    private static ObservableList<ObservableList<String>> data;
     static TableView tableView = new TableView<>();
     private static boolean firstimecreating = true;
     static List<String> list_of_cars = new ArrayList<String>();
     static List<String> list_of_parts = new ArrayList<String>();


    public static void Builddata(String car,String part) throws SQLException{

        try{

         data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM car_part ";

         boolean firstime = true;

         if (!car.isEmpty()) {
            if(firstime){ query += " WHERE car = '" + car + "'"; firstime=false; }
            else{ query += " AND    car  = '" + car + "'"; firstime=false;}
        }

         if (!part.isEmpty()) {
            if(firstime){ query += " WHERE part = '" + part + "'"; firstime=false; }
            else{ query += " AND    part  = '" + part + "'"; firstime=false;}
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
            System.out.println("Error on Building Data");
        }
    
    }

    private static void insertData(String car, String part) throws SQLException {
        
        data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "INSERT INTO car_part (car, part) VALUES ('" + car + "', '" + part +"')";


    Statement stmt = c.createStatement();
    stmt.execute(query); 
    Builddata("", "");
  
}

    private static void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}   

    private static void deleteData(String car, String part) throws SQLException {
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
    String query = "DELETE FROM car_part WHERE car = ? AND part = ?";

    try (PreparedStatement pstmt = c.prepareStatement(query)) {
        pstmt.setString(1, car);
        pstmt.setString(2, part);

        int rowsDeleted = pstmt.executeUpdate();

        if (rowsDeleted > 0) {
            showAlert("Delete Successful", "Record deleted successfully!");
            Builddata("", "");
        } else {
            showAlert("Delete Failed", "Failed to delete record. Make sure the record exists.");
        }
    }
}

    private static void updateData(String car, String part,String car2,String part2) throws SQLException {
       Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
       String query = "UPDATE car_part SET car = ?, part = ? WHERE car = ? AND part = ?";
    
    try (PreparedStatement pstmt = c.prepareStatement(query)) {
        pstmt.setString(1, car2);
        pstmt.setString(2, part2);
        pstmt.setString(3, car);
        pstmt.setString(4, part);


      

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) { 
            showAlert("Update Successful", "Record updated successfully!");
            Builddata("", ""); 
        } else {
            showAlert("Update Failed", "Failed to update record. Make sure the record exists.");
        }
    }
}



    public static VBox start2() throws Exception {

  
            Label label1 = new Label("Car:");
            TextField carfield = new TextField ();
            Label label0 = new Label("Update Car to:");
            TextField carfieldupdate = new TextField ();
            HBox hb = new HBox();
            hb.getChildren().addAll(label1, carfield,label0,carfieldupdate);
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);


            Label label2 = new Label("Part:");
            TextField partfield = new TextField ();
            Label label3 = new Label("Update Part to:");
            TextField partfieldupdate = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, partfield,label3,partfieldupdate);
            hb2.setSpacing(10);
            hb2.setAlignment(Pos.CENTER);


            // HBox style1 = new HBox();
            // style1.getChildren().addAll(hb,hb2);
            // style1.setSpacing(25);
            // style1.setAlignment(Pos.CENTER);

            
            HBox buttons = new HBox();
            buttons.setSpacing(15);
            buttons.setAlignment(Pos.CENTER);
            Button search = new Button("Search");
            Button insert = new Button("Insert");
            Button update = new Button("Update");
            Button delete = new Button("Delete");


            buttons.getChildren().addAll(search,insert,update,delete);



      
            search.setOnAction(e -> {
               try {
                Builddata(carfield.getText(),partfield.getText());
            } catch (SQLException e1) {
            }
            });


            insert.setOnAction(e->{
                 try {

                    if (carfield.getText().isEmpty() || partfield.getText().isEmpty()){showAlert("Fields are empty", "One or two of the fields are empty");}
                    else{
                    if (list_of_cars.contains(carfield.getText()) && list_of_parts.contains(partfield.getText())){
                    insertData(carfield.getText(),partfield.getText());
                    carfield.clear();partfield.clear();carfieldupdate.clear();partfieldupdate.clear();
                    }else{
                     showAlert("Wrong foreign Key","Please Enter correct Foreign key according to Car and Device Classes ");}}
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });


            update.setOnAction(e->{

                if(carfield.getText().isEmpty() || partfield.getText().isEmpty()){
                    showAlert("Empty fields", "One or two fields are empty");
                }else{
                    if (list_of_cars.contains(carfield.getText()) && list_of_parts.contains(partfield.getText()) && list_of_cars.contains(carfieldupdate.getText()) && list_of_parts.contains(partfieldupdate.getText())){
                        try {
                            updateData(carfield.getText(),partfield.getText(),carfieldupdate.getText(),partfieldupdate.getText());
                            carfield.clear(); partfield.clear();carfieldupdate.clear();partfieldupdate.clear();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        showAlert("Wrong foreign Key", "Please make sure to Enter a correct keys or Wrong in updating fields");
                    }
                }

            });


            delete.setOnAction(e ->{
                 if(carfield.getText().isEmpty() || partfield.getText().isEmpty()){
                    showAlert("Empty fields", "Car or Part field is empty or Both");
                }else{
                    if (list_of_cars.contains(carfield.getText()) && list_of_parts.contains(partfield.getText())){
                        try {
                            deleteData(carfield.getText(), partfield.getText());
                            carfield.clear();partfield.clear();carfieldupdate.clear();partfieldupdate.clear();
                            Builddata("","");
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }}
            
            });


            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT name FROM car");
            while (res1.next()) {
                list_of_cars.add(res1.getString(1));
            }

            Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt2 = con2.createStatement();
            ResultSet res2 = stmt2.executeQuery("SELECT no FROM device");
            while (res2.next()) {
                list_of_parts.add(res2.getString(1));
            }

            

            VBox v = new VBox();
            v.setSpacing(14);
            VBox.setMargin(hb, new Insets(14));
            v.getChildren().addAll(hb,hb2,buttons,tableView);
            return v;
        }

  
           
    }

   
    

