import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class Car extends Application {

    private ObservableList<ObservableList<String>> data;
    TableView tableView = new TableView<>();
    private boolean firstimecreating = true;
    static List<String> list = new ArrayList<String>();
    List<String> list_of_primary = new ArrayList<String>();


    public static void main(String[] args) {
             launch(args);
             
            }

    private void Builddata(String name,String model,String year,String made) throws SQLException{

        try{

        data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM car ";

         boolean firstime = true;

         if (!name.isEmpty()) {
            if(firstime){ query += " WHERE name = '" + name + "'"; firstime=false; }
            else{ query += " AND    name  = '" + name + "'"; firstime=false;}
        }

        if (!model.isEmpty()) {
            if(firstime){ query += " WHERE  model= '" + model + "'"; firstime=false; }
            else{ query += " AND    model = '" + model + "'"; firstime=false;}
        }

        if (!year.isEmpty()) {
            if(firstime){ query += " WHERE  year = '" + year + "'"; firstime=false; }
            else{ query += " AND    year  = '" + year + "'"; firstime=false;}
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
            System.out.println("Error on Building Data");
        }
    
    }

        
    private void insertData(String name, String model, String year, String made) throws SQLException {
        
        data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "INSERT INTO car (name, model, year, made) VALUES ('" + name + "', '" + model + "', '" + year + "', '" + made + "')";


    Statement stmt = c.createStatement();
    stmt.execute(query); 
    Builddata("", "", "", "");
  
}


    private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}

  
    private void updateData(String name, String model, String year, String made) throws SQLException {
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
    String query = "UPDATE car SET model = ?, year = ?, made = ? WHERE name = ?";
    
    try (PreparedStatement pstmt = c.prepareStatement(query)) {
        pstmt.setString(1, model);
        pstmt.setString(2, year);
        pstmt.setString(3, made);
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
    

    private void deleteData(String name) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "DELETE FROM car WHERE name = ?";

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
    

	
    
    @Override
    public void start(Stage stage) throws Exception {
        
        stage.setTitle("Database"); 
  
        TabPane tabpane = new TabPane(); 
  
  
            Tab tab = new Tab("Car"); 
            Tab car_part = new Tab("Car_part",Car_par.start2());
            Tab address1 = new Tab("Address",address.start3());
            Tab customerinstance = new Tab("Customer",Customer.start3());
            Tab manufactureins = new Tab("manufacture",manufacture.start3());
            Tab deviceins = new Tab("device",device.start3());
            Tab orderins = new Tab("Order",order.start3());

            Label label1 = new Label("Name:");
            TextField namefield = new TextField ();
            HBox hb = new HBox();
            hb.getChildren().addAll(label1, namefield);
            hb.setSpacing(10);


            Label label2 = new Label("Year:");
            TextField yearfield = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, yearfield);
            hb2.setSpacing(10);

            HBox style1 = new HBox();
            style1.getChildren().addAll(hb,hb2);
            style1.setSpacing(25);
            style1.setAlignment(Pos.CENTER);

            Label label3 = new Label("Made:");
            TextField madefield = new TextField ();
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(label3, madefield);
            hb3.setSpacing(10);

            Label label4 = new Label("Model:");
            TextField modelfield = new TextField ();
            HBox hb4 = new HBox();
            hb4.getChildren().addAll(label4, modelfield);
            hb4.setSpacing(10);

            HBox style2 = new HBox();
            style2.getChildren().addAll(hb3,hb4);
            style2.setSpacing(25);
            style2.setAlignment(Pos.CENTER);
            


            HBox btns = new HBox();
            btns.setSpacing(10);
            btns.setAlignment(Pos.CENTER);
            Button insert = new Button("Insert");
            Button search = new Button("Search");
            Button update = new Button("Update");
            Button delete = new Button("Delete");


            btns.getChildren().addAll(insert,search,update,delete);
            btns.setSpacing(15);

            search.setOnAction(e -> { 

                try {
                    Builddata(namefield.getText(),modelfield.getText(),yearfield.getText(),madefield.getText());
                } catch (SQLException e1) {
                }
            });

            insert.setOnAction(e -> {
                try {

                    if (list.contains(madefield.getText())) {
                        
                    if (namefield.getText().isEmpty() || modelfield.getText().isEmpty() || yearfield.getText().isEmpty()) {
                     showAlert("Empyt Text Fiedl", "Please make sure you entered the text fields");}
                     else{
                        if (list_of_primary.contains(namefield.getText())) {
                            showAlert("The primary Key already exists", "Please Change the name of the Car ");
                        }else{
                    insertData(namefield.getText(),modelfield.getText(),yearfield.getText(),madefield.getText());
                    list_of_primary.add(namefield.getText());
                    Car_par.list_of_cars.add(namefield.getText());
                    order.list_of_car.add(namefield.getText());
                    namefield.clear(); modelfield.clear(); yearfield.clear(); madefield.clear();}}
                     }else{
                        showAlert("Foreign Key not valid", "Please insert the foreign Key according to the table");
                     }

                } catch (SQLException e1) {
                    
                }
            });

            update.setOnAction(e ->{
              try { if (!list_of_primary.contains(namefield.getText())) {
                showAlert("Wrong Primary Key", "Please enter a CORRECT Primary Key Value");
              }else{
                if (namefield.getText().isEmpty() || modelfield.getText().isEmpty() || yearfield.getText().isEmpty() || madefield.getText().isEmpty()){
                    showAlert("Empty Fields", "You cant Update if any of the fields is empty");
                }else{
                    if (!list.contains(madefield.getText())) {
                        showAlert("Wrong Forign Key", "You need to put a correct Forign Key value");
                    }else{
                updateData(namefield.getText(),modelfield.getText(),yearfield.getText(),madefield.getText());
                    namefield.clear();
                    modelfield.clear();
                    yearfield.clear();
                    madefield.clear();}}}
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        
            });

            delete.setOnAction(e ->{

                if (namefield.getText().isEmpty()) {
                    showAlert("Empty Name Field", "Please make sure to Fill the name Field");
                }else{

                if (list_of_primary.contains(namefield.getText())) {
                    try {
                        deleteData(namefield.getText());
                        Builddata("","","","");
                        order.Builddata("","","","");
                    } catch (SQLException e1) {}
                }else{ showAlert("Wrong Name Field", "Please Enter a correct Name Field");}}
            });

         
         
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT name FROM manufacture");
            while (res1.next()) {
                list.add(res1.getString(1));
            }

             Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt2 = con2.createStatement();
            ResultSet res2 = stmt2.executeQuery("SELECT name FROM car");
            while (res2.next()) {
                list_of_primary.add(res2.getString(1));
            }
           
          

           
        

            VBox v = new VBox();
            v.setSpacing(12);
            VBox.setMargin(style1, new Insets(14));
            v.getChildren().addAll(style1,style2,btns,tableView);

            tab.setContent(v); 
            tabpane.getTabs().addAll(tab,car_part,address1,customerinstance,manufactureins,deviceins,orderins); 
        
            Scene scene = new Scene(tabpane, 800, 650); 
            stage.setScene(scene); 
            stage.show();
        
    }




    
}

