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


   public class address {


    private static ObservableList<ObservableList<String>> data;
     static TableView tableView = new TableView<>();
     private static boolean firstimecreating = true;
     static List<Integer> list_of_primary = new ArrayList<Integer>();



    public static void Builddata(String id,String building,String street,String city,String country) throws SQLException{

        try{

         data = FXCollections.observableArrayList();
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");

        String query = "SELECT * FROM address ";

         boolean firstime = true;

         if (!id.isEmpty()) {
            if(firstime){ query += " WHERE id = '" + id + "'"; firstime=false; }
            else{ query += " AND    id  = '" + id + "'"; firstime=false;}
        }

         if (!building.isEmpty()) {
            if(firstime){ query += " WHERE building = '" + building + "'"; firstime=false; }
            else{ query += " AND    building  = '" + building + "'"; firstime=false;}
        }

        if (!street.isEmpty()) {
            if(firstime){ query += " WHERE street = '" + street + "'"; firstime=false; }
            else{ query += " AND    street  = '" + street + "'"; firstime=false;}
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
            System.out.println("Error on Building Data");
        }
    
    }

    private static void insertData(String id, String building, String street, String city, String country) {
    try {
        int number_of_id = Integer.parseInt(id);
        int number_of_building = Integer.parseInt(building);
        
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
        String query = "INSERT INTO address (id, building, street, city, country) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = c.prepareStatement(query)) {
            pstmt.setInt(1, number_of_id);
            pstmt.setInt(2, number_of_building);
            pstmt.setString(3, street);
            pstmt.setString(4, city);
            pstmt.setString(5, country);
            pstmt.executeUpdate();
            Builddata("","","","","");

        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Error", "Failed to insert data. Check your input and try again.");
    }
}

    private static void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}   

    private static void updateData(String idToUpdate, String newBuilding, String newStreet, String newCity, String newCountry) throws SQLException {
    try {

        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
   
        String updateQuery = "UPDATE address SET building=?, street=?, city=?, country=? WHERE id=?";
        try (PreparedStatement preparedStatement = c.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newBuilding);
            preparedStatement.setString(2, newStreet);
            preparedStatement.setString(3, newCity);
            preparedStatement.setString(4, newCountry);
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
       String query = "DELETE FROM address WHERE id = ?";

        try (PreparedStatement pstmt = c.prepareStatement(query)) { 
        pstmt.setString(1, idfield);

        int rowsDeleted = pstmt.executeUpdate();

        if (rowsDeleted > 0) {
            showAlert("Delete Successful", "Record deleted successfully!");
            Builddata("", "","","","");
            Customer.Builddata("", "", "", "", "");
        } else {
            showAlert("Delete Failed", "Failed to delete record. Make sure the record exists.");
        }
    }
}

   


    public static VBox start3() throws Exception {

  
            Label label1 = new Label("Id:");
            TextField idfield = new TextField ();
            HBox hb = new HBox();
            hb.getChildren().addAll(label1, idfield);
            hb.setSpacing(10);


            Label label2 = new Label("Building:");
            TextField buildingfield = new TextField ();
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(label2, buildingfield);
            hb2.setSpacing(10);

            HBox style1 = new HBox();
            style1.getChildren().addAll(hb,hb2);
            style1.setSpacing(25);
            style1.setAlignment(Pos.CENTER);

            Label label3 = new Label("Street:");
            TextField streetfield = new TextField ();
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(label3, streetfield);
            hb3.setSpacing(10);

            Label label4 = new Label("City:");
            TextField citylfield = new TextField ();
            HBox hb4 = new HBox();
            hb4.getChildren().addAll(label4, citylfield);
            hb4.setSpacing(10);

            HBox style2 = new HBox();
            style2.getChildren().addAll(hb3,hb4);
            style2.setSpacing(25);
            style2.setAlignment(Pos.CENTER);

            Label label5 = new Label("Country:");
            TextField countrylfield = new TextField ();
            HBox hb5 = new HBox();
            hb5.getChildren().addAll(label5, countrylfield);
            hb5.setSpacing(10);
            hb5.setAlignment(Pos.CENTER);


            HBox btns = new HBox();
            Button search = new Button("Search");
            Button insert = new Button("Insert");
            Button update = new Button("Update");
            Button delete = new Button("Delete");

            
            btns.setSpacing(10);
            btns.getChildren().addAll(search,insert,update,delete);
            btns.setAlignment(Pos.CENTER);


        search.setOnAction(e ->{
                try {
                    Builddata(idfield.getText(),buildingfield.getText(),streetfield.getText(),citylfield.getText(),countrylfield.getText());
                    idfield.clear();buildingfield.clear();streetfield.clear();citylfield.clear();countrylfield.clear();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

        insert.setOnAction(e ->{

                if (idfield.getText().isEmpty() || buildingfield.getText().isEmpty() || streetfield.getText().isEmpty()|| citylfield.getText().isEmpty()|| countrylfield.getText().isEmpty()){
                     showAlert("Empty Field", "Make sure every field is inserted.");
                }else{
                    if (!list_of_primary.contains(Integer.valueOf(idfield.getText()))){
                        try {
                            insertData(idfield.getText(),buildingfield.getText(),streetfield.getText(),citylfield.getText(),countrylfield.getText());
                            list_of_primary.add(Integer.parseInt(idfield.getText()));
                            Customer.list_of_foreign1.add(idfield.getText());
                            idfield.clear();buildingfield.clear();streetfield.clear();citylfield.clear();countrylfield.clear();
                        } catch (Exception e1) {
                        }
                    }else{
                    showAlert("Wrong Primary key", "This primary Key i already used");
                }
     } }); 

        update.setOnAction(e ->{
        if (idfield.getText().isEmpty() || buildingfield.getText().isEmpty() || streetfield.getText().isEmpty() || citylfield.getText().isEmpty() || countrylfield.getText().isEmpty()){
            showAlert("Empty field", "There is one or more fields are empty make sure to fill them");
        }else{

        if (!list_of_primary.contains(Integer.parseInt(idfield.getText()))){
            showAlert("Wrong id field", "This id field is not used");
        }else{
            try {
                updateData(idfield.getText(), buildingfield.getText(), streetfield.getText(), citylfield.getText(), countrylfield.getText());
                idfield.clear();buildingfield.clear();streetfield.clear();citylfield.clear();countrylfield.clear();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }}
     });

        delete.setOnAction(e ->{
              if (list_of_primary.contains(Integer.parseInt(idfield.getText()))) {
                try {
                    Customer.list_of_foreign1.remove(idfield.getText());
                    deleteData(idfield.getText());
                    list_of_primary.remove(Integer.valueOf(idfield.getText()));
                    Car_par.Builddata("", "");
                    idfield.clear();buildingfield.clear();streetfield.clear();citylfield.clear();countrylfield.clear();
                } catch (SQLException e1) {
                }
              }else{
                showAlert("Wrong id field","Please make sure to fill a correct id");
              }
        });
        
             


            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cars_db", "root", "");
            Statement stmt1 = con1.createStatement();
            ResultSet res1 = stmt1.executeQuery("SELECT id FROM address");
            while (res1.next()) {
                list_of_primary.add(Integer.valueOf(res1.getString(1)));
            }

            




            VBox v1 = new VBox();
            VBox.setMargin(style1, new Insets(14));
            v1.setSpacing(14);
            v1.getChildren().addAll(style1,style2,hb5,btns,tableView);
            return v1;
        }

  
           
    }