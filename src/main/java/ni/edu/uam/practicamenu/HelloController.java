package ni.edu.uam.practicamenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class HelloController {
    private final ObservableList<Producto> productos = FXCollections.observableArrayList();

    @FXML
    private TextField codigoField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField categoriaField;

    @FXML
    private TextField precioField;

    @FXML
    private TextField existenciaField;

    @FXML
    private TableView<Producto> productosTable;

    @FXML
    private TableColumn<Producto, String> codigoColumn;

    @FXML
    private TableColumn<Producto, String> nombreColumn;

    @FXML
    private TableColumn<Producto, String> categoriaColumn;

    @FXML
    private TableColumn<Producto, Double> precioColumn;

    @FXML
    private TableColumn<Producto, Integer> existenciaColumn;

    @FXML
    private Label resultadoLabel;

    @FXML
    private void initialize() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        existenciaColumn.setCellValueFactory(new PropertyValueFactory<>("existencia"));

        productosTable.setItems(productos);
        productosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> cargarProductoSeleccionado(seleccionado)
        );

        resultadoLabel.setText("Listo para registrar productos.");
    }

    @FXML
    private void nuevoProducto() {
        limpiarCampos();
        productosTable.getSelectionModel().clearSelection();
        codigoField.requestFocus();
        resultadoLabel.setText("Ingrese los datos del nuevo producto.");
    }

    @FXML
    private void guardarProducto() {
        if (!camposValidos()) {
            return;
        }

        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();
        String codigo = codigoField.getText().trim();
        String nombre = nombreField.getText().trim();
        String categoria = categoriaField.getText().trim();
        double precio = Double.parseDouble(precioField.getText().trim());
        int existencia = Integer.parseInt(existenciaField.getText().trim());

        if (seleccionado == null) {
            productos.add(new Producto(codigo, nombre, categoria, precio, existencia));
            resultadoLabel.setText("Producto guardado correctamente.");
        } else {
            seleccionado.setCodigo(codigo);
            seleccionado.setNombre(nombre);
            seleccionado.setCategoria(categoria);
            seleccionado.setPrecio(precio);
            seleccionado.setExistencia(existencia);
            productosTable.refresh();
            resultadoLabel.setText("Producto actualizado correctamente.");
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto seleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un producto para eliminar.");
            resultadoLabel.setText("No se elimino ningun producto: no hay seleccion.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Eliminar producto");
        confirmacion.setContentText("Desea eliminar el producto " + seleccionado.getNombre() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            productos.remove(seleccionado);
            limpiarCampos();
            resultadoLabel.setText("Producto eliminado correctamente.");
        } else {
            resultadoLabel.setText("Eliminacion cancelada.");
        }
    }

    @FXML
    private void salirAplicacion() {
        productosTable.getScene().getWindow().hide();
    }

    private void cargarProductoSeleccionado(Producto producto) {
        if (producto == null) {
            return;
        }

        codigoField.setText(producto.getCodigo());
        nombreField.setText(producto.getNombre());
        categoriaField.setText(producto.getCategoria());
        precioField.setText(String.valueOf(producto.getPrecio()));
        existenciaField.setText(String.valueOf(producto.getExistencia()));
        resultadoLabel.setText("Producto seleccionado: " + producto.getNombre());
    }

    private boolean camposValidos() {
        if (codigoField.getText().trim().isEmpty()
                || nombreField.getText().trim().isEmpty()
                || categoriaField.getText().trim().isEmpty()
                || precioField.getText().trim().isEmpty()
                || existenciaField.getText().trim().isEmpty()) {
            mostrarAdvertencia("Complete todos los campos del producto.");
            resultadoLabel.setText("No se pudo guardar: faltan datos.");
            return false;
        }

        try {
            double precio = Double.parseDouble(precioField.getText().trim());
            int existencia = Integer.parseInt(existenciaField.getText().trim());

            if (precio < 0 || existencia < 0) {
                mostrarAdvertencia("El precio y la existencia no pueden ser negativos.");
                resultadoLabel.setText("No se pudo guardar: valores negativos.");
                return false;
            }
        } catch (NumberFormatException exception) {
            mostrarAdvertencia("El precio debe ser decimal y la existencia debe ser un numero entero.");
            resultadoLabel.setText("No se pudo guardar: formato numerico invalido.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        codigoField.clear();
        nombreField.clear();
        categoriaField.clear();
        precioField.clear();
        existenciaField.clear();
    }

    private void mostrarAdvertencia(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Validacion");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
