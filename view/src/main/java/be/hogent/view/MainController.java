package be.hogent.view;

import be.hogent.model.*;
import be.hogent.model.dao.DAOException;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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

	public MainController() {
	}

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
		tableButtonMap = new TreeMap<>();
		scrollPane.setContent(tablePane);
		scrollPane.setFitToWidth(true);

		tablePane.setPadding(new Insets(5, 5, 5, 5));
		tablePane.setHgap(15);
		tablePane.setVgap(15);
		tablePane.setPrefColumns(6);
		tablePane.setMaxHeight(Region.USE_PREF_SIZE);
		for (Table table : model.getTables()) {
			Button button = new Button(table.toString());
			button.setPrefSize(150, 150);
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

	private void resetTables() {
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

	private void setOrderItems(Order order) throws NullPointerException {

		try {
			orderItems = FXCollections.observableList(new ArrayList<>(order.getOrderItems()));
			orderItemTable.setItems(orderItems);
			orderItemTable.refresh();
			updateTotalPrice();
		} catch (NullPointerException n) {
			orderItems = FXCollections.observableList(new ArrayList<>());
			orderItemTable.setItems(orderItems);
			orderItemTable.refresh();
			updateTotalPrice();
		}
	}

	public void order() {
		try {
			if (model.getActiveTable() == null) {
				showError("No table selected. Please select a table first!");
			} else {
				int selectedIndex = beverageTable.getSelectionModel().getSelectedIndex();
				if (selectedIndex >= 0) {

					Beverage beverage = beverageTable.getSelectionModel().getSelectedItem();
					model.order(beverage, 1);
					setOrderItems(model.getActiveTable().getOrder());
					updateTotalPrice();
				} else {
					// Nothing selected.
					showError("Please select a beverage in the table.");
				}
				setOrderItems(model.getActiveTable().getOrder());
			}
		}catch (Cafe.alreadyOtherWaiterAssignedException e) {
			showError("There is already another waiter assigned!");
		}
	}

	public void decreaseOrderItem(){
		if(model.getActiveTable()==null){
			showError("No table selected. Please select a table first!");
		}
		else{
			int selectedIndex = orderItemTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				if(orderItemTable.getSelectionModel().getSelectedItem().getQuantity()>0) {
					orderItemTable.getSelectionModel().getSelectedItem().decreaseQuantity(1);
					setOrderItems(model.getActiveTable().getOrder());
					updateTotalPrice();
				}
				if (orderItemTable.getSelectionModel().getSelectedItem().getQuantity()==0) {
					model.getActiveTable().getOrder().getOrderItems().remove(orderItemTable.getSelectionModel().getSelectedItem());
					setOrderItems(model.getActiveTable().getOrder());
					updateTotalPrice();
				}
			} else {
				// Nothing selected.
				showError("No orderItem selected. Please select orderItem first!");
			}
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
				showError("No orderItem selected. Please select orderItem first!");
			}
			setOrderItems(model.getActiveTable().getOrder());
		} catch (NullPointerException e) {
			showError("No table selected. Please select a table first!");
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

	public void pay() {
		if(model.getActiveTable()==null || model.getActiveTable().getOrder()==null || model.getActiveTable().getOrder().getOrderItems().size()==0){
			showError("No order to pay!");
		}
		else {
			try {
				model.pay(model.getActiveTable());
				setOrderItems(model.getActiveTable().getOrder());
				updateTotalPrice();
				setSalesView();
				showInfo(model.getActiveTable().toString() + " successfully payed the bill!");
			} catch (Cafe.alreadyOtherWaiterAssignedException e) {
				showError("There is already another waiter assigned!");
			}
		}
	}

	private void setSalesView()  {
		if(dates.getSelectionModel().isEmpty()) {
			sales = FXCollections.observableList(new ArrayList<>(model.getAllOrderItemsForWaiter(model.getLoggedOnWaiter())));
		}
		else{
			sales = FXCollections.observableList(new ArrayList<>(model.getAllOrderItemsByDate(model.getLoggedOnWaiter(),dates.getSelectionModel().getSelectedItem())));
		}
		salesView.setItems(sales);
		setTotalSalesPrice();
		getDates();
	}

	private void setTotalSalesPrice() {
		Double total = sales.stream().mapToDouble(OrderItem::getPrice).sum();
		String price = String.format("%.2f", total);
		lb_totalSalesPrice.setText("€ " + price);
	}

	public void showTop3View() throws IOException {
		ObservableList<PieChart.Data> topWaiters = FXCollections.observableArrayList();
		for (Map.Entry<Waiter, Double> entry : model.getTop3WaiterSales().entrySet()) {
			topWaiters.add(new PieChart.Data(entry.getKey().toString(), entry.getValue()));
		}
		top3Waiters.setData(topWaiters);
		try {
			model.showTopWaitersReport(model.getTop3WaiterSales());
		}catch (IOException e){
			showError("Error creating TopWaiters.jpg in your root directory!");
		}
	}

	private void getDates() {
		try {
			ObservableList<LocalDate> listDates = FXCollections.observableArrayList(model.getAllDatesForWaiter(model.getLoggedOnWaiter()));
			dates.setItems(listDates);
		}catch(DAOException e){
			showError("There was a problem getting the requested dates from database !");
		}
	}

	public void exportToPDF(){
		if(model.createPDF(model.getLoggedOnWaiter(),sales)){
			showInfo("PDF successfully created! You can find CafeReport.pdf in your home directory.");
		}
		else{
			showError("There was an error creating your pdf file!");
		}
	}

	private void showError(String message){
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.initOwner(cafeView.getPrimaryStage());
		alert.setTitle("ERROR");
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showInfo(String message){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initOwner(cafeView.getPrimaryStage());
		alert.setTitle("INFO");
		alert.setContentText(message);
		alert.show();
	}
}
