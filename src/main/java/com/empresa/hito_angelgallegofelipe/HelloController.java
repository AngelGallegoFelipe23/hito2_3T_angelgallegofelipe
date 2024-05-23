package com.empresa.hito_angelgallegofelipe;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField precioTextField;

    @FXML
    private TextField pesoPorUnidadTextField;

    @FXML
    private TableView<Fruta> frutasTable;

    @FXML
    private TableColumn<Fruta, String> nombreColumn;

    @FXML
    private TableColumn<Fruta, Double> precioColumn;

    @FXML
    private TableColumn<Fruta, Double> pesoColumn;

    @FXML
    private TextField searchTextField;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private void handleTableDoubleClick() {
        frutasTable.setRowFactory(tv -> {
            TableRow<Fruta> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Fruta rowData = row.getItem();
                    fillFields(rowData);
                }
            });
            return row;
        });
    }

    private void fillFields(Fruta fruta) {
        nombreTextField.setText(fruta.getNombre());
        precioTextField.setText(String.valueOf(fruta.getPrecio()));
        pesoPorUnidadTextField.setText(String.valueOf(fruta.getPesoPorUnidad()));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar conexión con MongoDB Atlas
        String uri = "mongodb+srv://admin:123@cluster0.ysijcxy.mongodb.net/"; // Reemplaza con tu cadena de conexión
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("fruteria"); // Reemplaza con tu nombre de base de datos
        collection = database.getCollection("fruta");

        // Configurar columnas de la tabla
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        precioColumn.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        pesoColumn.setCellValueFactory(cellData -> cellData.getValue().pesoPorUnidadProperty().asObject());

        // Cargar automáticamente los datos al iniciar la aplicación
        handleRead();

        // Configurar evento de doble clic en la tabla
        handleTableDoubleClick();

        // Configurar el buscador
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue);
        });
    }

    @FXML
    private void handleSearch(String searchText) {
        // Obtener todas las frutas de la base de datos que coincidan con el texto de búsqueda
        FindIterable<Document> frutas = collection.find(new Document("nombre", new Document("$regex", searchText).append("$options", "i")));

        // Limpiar la tabla antes de agregar nuevas frutas
        frutasTable.getItems().clear();

        // Recorrer las frutas y agregarlas a la tabla
        for (Document fruta : frutas) {
            String id = fruta.getObjectId("_id").toString();
            String nombre = fruta.getString("nombre");
            Double precio = fruta.getDouble("precio");
            Double pesoPorUnidad = fruta.getDouble("pesoPorUnidad");

            Fruta nuevaFruta = new Fruta(id, nombre, precio, pesoPorUnidad);
            frutasTable.getItems().add(nuevaFruta);
        }
    }

    @FXML
    private void handleCreate() {
        String nombre = nombreTextField.getText();
        double precio = Double.parseDouble(precioTextField.getText());
        double pesoPorUnidad = Double.parseDouble(pesoPorUnidadTextField.getText());

        if (!nombre.isEmpty() && precio > 0 && pesoPorUnidad > 0) {
            Document fruta = new Document("nombre", nombre)
                    .append("precio", precio)
                    .append("pesoPorUnidad", pesoPorUnidad);
            collection.insertOne(fruta);
            welcomeText.setText("Fruta creada con ID: " + fruta.getObjectId("_id").toString());
            handleRead(); // Para actualizar la tabla después de la creación
        } else {
            welcomeText.setText("Todos los campos son obligatorios y deben tener valores válidos.");
        }
    }

    @FXML
    private void handleRead() {
        // Obtener todas las frutas de la base de datos
        FindIterable<Document> frutas = collection.find();

        // Limpiar la tabla antes de agregar nuevas frutas
        frutasTable.getItems().clear();

        // Recorrer las frutas y agregarlas a la tabla
        for (Document fruta : frutas) {
            String id = fruta.getObjectId("_id").toString();
            String nombre = fruta.getString("nombre");
            Double precio = fruta.getDouble("precio");
            Double pesoPorUnidad = fruta.getDouble("pesoPorUnidad");

            Fruta nuevaFruta = new Fruta(id, nombre, precio, pesoPorUnidad);
            frutasTable.getItems().add(nuevaFruta);
        }
    }

    @FXML
    private void handleUpdate() {
        Fruta selectedFruta = frutasTable.getSelectionModel().getSelectedItem();
        if (selectedFruta != null) {
            String id = selectedFruta.getId();
            String nombre = nombreTextField.getText();
            double precio = Double.parseDouble(precioTextField.getText());
            double pesoPorUnidad = Double.parseDouble(pesoPorUnidadTextField.getText());

            Document updatedFruta = new Document("nombre", nombre)
                    .append("precio", precio)
                    .append("pesoPorUnidad", pesoPorUnidad);
            collection.updateOne(new Document("_id", new ObjectId(id)),
                    new Document("$set", updatedFruta));
            welcomeText.setText("Fruta actualizada con ID: " + id);
            handleRead(); // Para actualizar la tabla después de la actualización
        } else {
            welcomeText.setText("Seleccione una fruta para actualizar.");
        }
    }

    @FXML
    private void handleDelete() {
        Fruta selectedFruta = frutasTable.getSelectionModel().getSelectedItem();
        if (selectedFruta != null) {
            String id = selectedFruta.getId();
            collection.deleteOne(new Document("_id", new ObjectId(id)));
            welcomeText.setText("Fruta eliminada con ID: " + id);
            handleRead(); // Para actualizar la tabla después de la eliminación
        } else {
            welcomeText.setText("Seleccione una fruta para eliminar.");
        }
    }

    // Método para cerrar la conexión a MongoDB cuando la aplicación se cierre
    public void close() {
        mongoClient.close();
    }
}
