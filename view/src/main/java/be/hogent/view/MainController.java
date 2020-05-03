package be.hogent.view;

import be.hogent.model.*;
import be.hogent.model.dao.DAOException;
import be.hogent.model.reports.PieChartReport;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.sql.Date;

public class MainController {

	@FXML
	private TableView<Beverage> beverageTable;
	@FXML
	private TableView<OrderItem> orderItemTable;
	@FXML
	private TableView<OrderItem> salesView;
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
	private TableColumn<OrderItem, String> beverageNameColumn;
	@FXML
	private TableColumn<OrderItem, Integer> beverageQtyColumn;
	@FXML
	private ComboBox<LocalDate> dates;
	@FXML
	private Label lb_currentWaiter;
	@FXML
	private Label lb_totalPrice;
	@FXML
	private Label lb_totalSalesPrice;
	@FXML
	private PieChart top3Waiters;

	private MainApp cafeView;
	private Stage dialogStage;
	private Cafe model;
	private Beverage b;
	private Order order;
	private OrderItem o;
	private ObservableList<OrderItem> orderItems;
	private ObservableList<OrderItem> sales;
	private Map<Table, Button> tableButtonMap;
	private LocalDate d;

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

		beverageNameColumn.setCellValueFactory(itemNameProperty);
		beverageQtyColumn.setCellValueFactory(itemQtyProperty);

		beverageTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setB(newValue));
		orderItemTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setO(newValue));
		dates.valueProperty().addListener((observableValue, date, t1) -> {
			try {
				setD(t1);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
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

	private void setB(Beverage b) {
		this.b = b;
	}

	private void setO(OrderItem o) {
		this.o = o;
	}

	private void setD(LocalDate d) throws DAOException {
		this.d = d;
		setSalesView();
	}


	private void login() throws DAOException {
		if (cafeView.showLogin()) {
			setCurrentWaiter();
			setTables();
			orderItems = FXCollections.observableList(new ArrayList<>());
			orderItemTable.setItems(orderItems);
			setSalesView();
		}
	}

	public void logOff() throws DAOException {
		lb_currentWaiter.setText("");
		lb_totalPrice.setText("€ 0.00");
		model.logoff();
		resetTables();
		model.setActiveTable(null);
		login();
		cafeView.closePrimary();
	}

	public void setCurrentWaiter() {
		lb_currentWaiter.setText(model.getLoggedOnWaiter().toString());
	}

	public void setTables() {
		tableButtonMap = new HashMap<>();
		scrollPane.setContent(tablePane);
		scrollPane.setFitToWidth(true);

		tablePane.setPadding(new Insets(5, 5, 5, 5));
		tablePane.setHgap(15);
		tablePane.setVgap(15);
		tablePane.setPrefColumns(6);
		tablePane.setMaxHeight(Region.USE_PREF_SIZE);
		for (Table table : model.getTables()) {
			Button button = new Button(table.toString());
			button.setPrefSize(110, 110);
			tablePane.getChildren().addAll(button);
			tableButtonMap.put(table, button);

			button.setOnMouseClicked(mouseEvent -> {
				updateTables();
				button.setStyle("-fx-background-color: yellow");
				for (Map.Entry<Table, Button> entry : tableButtonMap.entrySet()) {
					if (entry.getValue().equals(button)) {
						model.setActiveTable(entry.getKey());
						setOrderItems(entry.getKey().getOrder());
					}
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

	private void updateTables() {
		for (Map.Entry<Table, Button> entry : tableButtonMap.entrySet()) {
			if (entry.getKey().getAssignedWaiter() == null) {
				entry.getValue().setStyle("-fx-background-color: grey");
			} else {

				if (entry.getKey().getAssignedWaiter().equals(model.getLoggedOnWaiter())) {
					if (entry.getKey().getOrder().getOrderItems().size() > 0) {
						entry.getValue().setStyle("-fx-background-color: green");
					} else {
						entry.getValue().setStyle("-fx-background-color: grey");
						entry.getKey().setAssignedWaiter(null);
					}

				} else {
					entry.getValue().setDisable(true);
					entry.getValue().setStyle("-fx-background-color: red");
				}
			}
		}
	}


	public void setOrderItems(Order order) throws NullPointerException {

		try {
			orderItems = FXCollections.observableList(new ArrayList<>(order.getOrderItems()));
			orderItemTable.setItems(orderItems);
			updateTotalPrice();
		} catch (NullPointerException n) {
			orderItems = FXCollections.observableList(new ArrayList<>());
			orderItemTable.setItems(orderItems);
			updateTotalPrice();
		}
	}

	public void order() throws Cafe.alreadyOtherWaiterAssignedException, NullPointerException {
		try {
			int selectedIndex = beverageTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {

				Beverage beverage = beverageTable.getSelectionModel().getSelectedItem();
				model.order(beverage, 1);
				setOrderItems(model.getActiveTable().getOrder());
				updateTotalPrice();
				//System.out.println(model.getActiveTable().getOrder().getOrderItems().toString());
			} else {
				// Nothing selected.
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(cafeView.getPrimaryStage());
				alert.setTitle("No beverage selected");
				alert.setContentText("Please select a beverage in the table.");
				alert.showAndWait();
			}
			setOrderItems(model.getActiveTable().getOrder());
		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(cafeView.getPrimaryStage());
			alert.setTitle("No Table Selected");
			alert.setContentText("Please select a table!");
			alert.showAndWait();
		}
	}

	public void deleteOrderItem() {
		try {
			int selectedIndex = orderItemTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				model.getActiveTable().getOrder().getOrderItems().remove(orderItemTable.getSelectionModel().getSelectedItem());
				setOrderItems(model.getActiveTable().getOrder());
				updateTotalPrice();
				if (model.getActiveTable().getOrder().getOrderItems().size() == 0) {
					model.getActiveTable().setAssignedWaiter(null);
					updateTotalPrice();
				}
			} else {
				// Nothing selected.
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(cafeView.getPrimaryStage());
				alert.setTitle("No orderItem selected");
				alert.setContentText("Please select orderItem in the table.");
				alert.showAndWait();
			}
			setOrderItems(model.getActiveTable().getOrder());
		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(cafeView.getPrimaryStage());
			alert.setTitle("No Table Selected");
			alert.setContentText("Please select a table!");
			alert.showAndWait();
		}
	}

	private void updateTotalPrice() {
		if (model.getActiveTable() == null || model.getActiveTable().getOrder() == null || model.getActiveTable().getOrder().getOrderItems() == null || model.getActiveTable().getOrder().getOrderItems().size() == 0) {
			lb_totalPrice.setText("€ 0.00");
		} else {
			Double total = model.getActiveTable().getOrder().getTotalPrice();
			String price = String.format("%.2f", total);
			lb_totalPrice.setText("€ " + price);
		}
	}

	public void pay() throws DAOException, Cafe.alreadyOtherWaiterAssignedException {
		try{
			model.pay(model.getActiveTable());
			setOrderItems(model.getActiveTable().getOrder());
			updateTotalPrice();
			setSalesView();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initOwner(cafeView.getPrimaryStage());
			alert.setTitle("SUCCES");
			alert.setContentText(model.getActiveTable().toString() + " successfully payed the bill!");
			alert.showAndWait();
		} catch(DAOException e){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(cafeView.getPrimaryStage());
			alert.setTitle("FAILED");
			alert.setContentText("There was an error paying the bill!");
			alert.showAndWait();
		}
	}

	private void setSalesView() throws DAOException {
		if(dates.getSelectionModel().isEmpty()) {
			sales = FXCollections.observableList(new ArrayList<>(model.getAllOrderItemsForWaiter(model.getLoggedOnWaiter())));
			salesView.setItems(sales);
			setTotalSalesPrice();
			getDates();
		}
		else{
			sales = FXCollections.observableList(new ArrayList<>(model.getAllOrderItemsByDate(model.getLoggedOnWaiter(),dates.getSelectionModel().getSelectedItem())));
			salesView.setItems(sales);
			setTotalSalesPrice();
			getDates();
		}
	}

	private void setTotalSalesPrice() {
		Double total = sales.stream().mapToDouble(OrderItem::getPrice).sum();
		String price = String.format("%.2f", total);
		lb_totalSalesPrice.setText("€ " + price);
	}

	public void showTop3View() {
		ObservableList<PieChart.Data> topWaiters = FXCollections.observableArrayList();
		for (Map.Entry<Waiter, Double> entry : model.getTop3WaiterSales().entrySet()) {
			topWaiters.add(new PieChart.Data(entry.getKey().toString(), entry.getValue()));
		}
		top3Waiters.setData(topWaiters);
	}

	public void getDates() throws DAOException {
		ObservableList<LocalDate> listDates = FXCollections.observableArrayList(model.getAllDatesForWaiter(model.getLoggedOnWaiter()));
		dates.setItems(listDates);
	}

	public void exportToPDF(){
		if(model.createPDF(model.getLoggedOnWaiter(),sales)){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initOwner(cafeView.getPrimaryStage());
			alert.setTitle("SUCCESS");
			alert.setHeaderText("PDF file successfully created!");
			alert.setContentText("You can find CafeReport.pdf in your home directory.");
			alert.show();
		}
		else{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(cafeView.getPrimaryStage());
			alert.setTitle("FAIL");
			alert.setHeaderText("PDF file not created!");
			alert.setContentText("There was an error creating your pdf file!");
			alert.showAndWait();
		}

	}
}
