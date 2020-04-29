package be.hogent.view;

import be.hogent.model.*;
import be.hogent.model.dao.DAOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController {

	@FXML
	private TableView<Beverage> beverageTable;
	@FXML
	private TableView<OrderItem> orderItemTable;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private TilePane tablePane;
	@FXML
	private TableColumn<Beverage, String> nameColumn;
	@FXML
	private TableColumn<Beverage, Double> priceColumn;
	@FXML
	private TableColumn<OrderItem, String> itemNameColumn;
	@FXML
	private TableColumn<OrderItem, Integer> itemQtyColumn;
	@FXML
	private ComboBox<Date> dates;
	@FXML
	private Label lb_currentWaiter;
	//@FXML
	//private Button bt_Order;

	// Reference to the main application.
	private MainApp cafeView;
	private Stage dialogStage;
	private Cafe model;
	private Beverage b;
	private Order order;
	private OrderItem orderItem;
	private ObservableList<OrderItem> orderItems;

	/**
	 * The constructor.
	 * The constructor is called before the initialize() method.
	 */
	public MainController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		PropertyValueFactory<Beverage, String> nameProperty =
				new PropertyValueFactory<>("name");

		PropertyValueFactory<Beverage, Double> priceProperty =
				new PropertyValueFactory<>("price");

		nameColumn.setCellValueFactory(nameProperty);
		priceColumn.setCellValueFactory(priceProperty);

		PropertyValueFactory<OrderItem, String> itemNameProperty =
				new PropertyValueFactory<>("name");

		PropertyValueFactory<OrderItem, Integer> itemQtyProperty =
				new PropertyValueFactory<>("quantity");

		itemNameColumn.setCellValueFactory(itemNameProperty);
		itemQtyColumn.setCellValueFactory(itemQtyProperty);

		beverageTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setB(newValue));
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param cafeView
	 */
	public void setCafeView(MainApp cafeView) throws DAOException {
		this.cafeView = cafeView;
		login();
		cafeView.closePrimary();
		beverageTable.setItems(cafeView.getBeverages());
	}

	public void setModel(Cafe model) {
		this.model = model;
	}

	private void setB(Beverage b){this.b = b;}


	private void login() {
		if (cafeView.showLogin()) {
			setCurrentWaiter();
			setTables();
		}
	}

	public void logOff() {
		lb_currentWaiter.setText("XXXXX");
		model.logoff();
		resetTables();
		login();
		cafeView.closePrimary();
	}

	public void setCurrentWaiter() {
		lb_currentWaiter.setText(model.getLoggedOnWaiter().toString());
	}

	public void setTables() {
		scrollPane.setContent(tablePane);
		scrollPane.setFitToWidth(true);

		tablePane.setPadding(new Insets(5, 5, 5, 5));
		tablePane.setHgap(15);
		tablePane.setVgap(15);
		tablePane.setPrefColumns(6);
		tablePane.setMaxHeight(Region.USE_PREF_SIZE);
		for (Table table : model.getTables()) {
			Button button = new Button(table.toString());
			button.setPrefSize(100, 100);
			tablePane.getChildren().addAll(button);

			button.setOnMouseClicked(mouseEvent -> {
				button.setStyle("-fx-background-color: orange");

				String tableName = button.getText();
				for (Table t : model.getTables()) {
					if (t.toString().equals(tableName))
						model.setActiveTable(t);
					    showTableOrder(t);
				}

			});

			if (table.getAssignedWaiter() == null) {
				button.setStyle("-fx-background-color: grey");
			} else {

				if (table.getAssignedWaiter().equals(model.getLoggedOnWaiter())) {
					button.setStyle("-fx-background-color: green");

				} else {
					button.setDisable(true);
					button.setStyle("-fx-background-color: red");
				}
			}
		}
	}

	public void resetTables() {
			tablePane.getChildren().clear();
	}

	public void setOrderItems(Order order)throws NullPointerException{

		try{
			orderItems = FXCollections.observableList(new ArrayList<>(order.getOrderItems()));
			orderItemTable.setItems(orderItems);
		}catch (NullPointerException n){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("No active order found");
			alert.setContentText("No active order for this table");
			alert.showAndWait();
		}

	}

	public void showTableOrder(Table table) {
		for (Table t : model.getTables()) {
			if (t.toString().equals(table.toString())) {
				if (!(table.getOrder() == null)) {
					order = t.getOrder();
					setOrderItems(order);
					//selectedTabel = table;
				}
			}
		}
	}

	public void order() throws Cafe.alreadyOtherWaiterAssignedException {
		int selectedIndex = beverageTable.getSelectionModel ().getSelectedIndex ();
		if (selectedIndex >= 0) {

			Beverage beverage = beverageTable.getSelectionModel ().getSelectedItem ();
			model.order (beverage,1);
			showTableOrder(model.getActiveTable());

		} else {
			// Nothing selected.
			Alert alert = new Alert (Alert.AlertType.WARNING);
			alert.initOwner (cafeView.getPrimaryStage ());
			alert.setTitle ("No Selection");
			alert.setHeaderText ("No beverage Selected");
			alert.setContentText ("Please select a beverage in the table.");

			alert.showAndWait ();
		}
	}
}



//	public void getDates() throws DAOException {
//		ObservableList<Date> listDates = FXCollections.observableArrayList(model.getAllDatesForWaiter(model.getLoggedOnWaiter()));
//		dates.setItems(listDates);
//	}
