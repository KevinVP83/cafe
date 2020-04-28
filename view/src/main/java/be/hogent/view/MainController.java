package be.hogent.view;

import be.hogent.model.Beverage;
import be.hogent.model.Cafe;
import be.hogent.model.dao.DAOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Date;

public class MainController {

	@FXML
	private TableView<Beverage> beverageTable;
	@FXML
	private GridPane tableGrid;
	@FXML
	private TableColumn<Beverage, String> nameColumn;
	@FXML
	private TableColumn<Beverage, Double> priceColumn;
	@FXML
	private ComboBox<Date> dates;

	@FXML
	private Label lb_currentWaiter;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birthdayLabel;

	// Reference to the main application.
	private MainApp cafeView;

	private Cafe model;
	private Beverage b;

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
	private void initialize () {
		
		PropertyValueFactory<Beverage, String> nameProperty =
				new PropertyValueFactory<> ("name");
		
		PropertyValueFactory<Beverage, Double> priceProperty =
				new PropertyValueFactory<> ("price");
		
		nameColumn.setCellValueFactory (nameProperty);
		priceColumn.setCellValueFactory (priceProperty);
	}
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param cafeView
	 */
	public void setCafeView (MainApp cafeView) throws DAOException {
		this.cafeView = cafeView;
		login();
		cafeView.closePrimary();
		beverageTable.setItems (cafeView.getBeverages ());
	}
	
	public void setModel (Cafe model) {
		this.model = model;
	}


	private void login() {
		if (cafeView.showLogin()) {
			setCurrentWaiter();
//			getDates();
		}
	}

	public void logOff(){
		lb_currentWaiter.setText("XXXXX");
		model.logoff();
		login();
		cafeView.closePrimary();
	}

	public void setCurrentWaiter(){
		lb_currentWaiter.setText(model.getLoggedOnWaiter().toString());

	}

//	public void getDates() throws DAOException {
//		ObservableList<Date> listDates = FXCollections.observableArrayList(model.getAllDatesForWaiter(model.getLoggedOnWaiter()));
//		dates.setItems(listDates);
//	}
}