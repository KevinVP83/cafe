

package be.hogent.view;

import be.hogent.model.Beverage;
import be.hogent.model.Cafe;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;

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
	private Label firstNameLabel;
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
	//private Map<> tableButtonMap;

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
		
		//showPersonDetails (p);
		
		// Listen for selection changes and show the person details when changed.
		//beverageTable.getSelectionModel ().selectedItemProperty ().addListener (
				//(observable, oldValue, newValue) -> showPersonDetails (newValue));
	}
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param cafeView
	 */
	public void setCafeView (MainApp cafeView) {
		this.cafeView = cafeView;
		//tableButtonMap = new HashMap<>();
		//b = cafeView.getBeverages ().get (0);
		
		// Add observable list data to the table
		beverageTable.setItems (cafeView.getBeverages ());
		//showPersonDetails (b);
	}
	
	public void setModel (Cafe model) {
		this.model = model;
	}

	public void initializeTableGrid() {
		double tableCount = model.getTables().size();
		double numCol = Math.round(Math.sqrt(tableCount));
		double numRow = Math.ceil(Math.sqrt(tableCount));
		tableGrid.setGridLinesVisible(false);
		tableGrid.setAlignment(Pos.CENTER);
		tableGrid.setPadding(new Insets(10, 10, 10, 10));
		for (int i = 0; i < numCol; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth(100 / numCol);
			tableGrid.getColumnConstraints().add(columnConstraints);
		}
		for (int i = 0; i < numRow; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100 / numRow);
			tableGrid.getRowConstraints().add(rowConstraints);
		}
		int tableCounter = 0;
		for (int row = 0; row < numRow; row++) {
			for (int col = 0; col < numCol; col++) {
				if (tableCounter < tableCount) {
					Button table = new Button();
					table.setPrefHeight(tableGrid.getPrefHeight() / numRow);
					table.setPrefWidth(tableGrid.getPrefWidth() / numCol);
					int tableID = tableCounter + 1;
					table.setId(String.valueOf(tableID));
					table.setText("Table " + tableID);
					table.setOnAction(event -> {
//						model.setActiveTable(model.getTables().get(tableID - 1));
//						handleTableSelect();
					});
					tableGrid.add(table, col, row);
					//tableButtonMap.put(model.getTables().get(tableCounter),table);
				}
				tableCounter++;
			}
		}
	}
	
//	/**
//	 * Fills all text fields to show details about the person.
//	 * If the specified person is null, all text fields are cleared.
//	 *
//	 * @param person the person or null
//	 */
//	private void showPersonDetails (Person person) {
//
//		if (person != null) {
//			// Fill the labels with info from the person object.
//			firstNameLabel.setText (person.getFirstName ());
//			lastNameLabel.setText (person.getLastName ());
//			streetLabel.setText (person.getStreet ());
//			postalCodeLabel.setText (Integer.toString (person.getPostalCode ()));
//			cityLabel.setText (person.getCity ());
//			birthdayLabel.setText (DateUtil.format (person.getBirthday ()));
//		} else {
//			// Person is null, remove all the text.
//			firstNameLabel.setText ("");
//			lastNameLabel.setText ("");
//			streetLabel.setText ("");
//			postalCodeLabel.setText ("");
//			cityLabel.setText ("");
//			birthdayLabel.setText ("");
//		}
//	}
//
//	/**
//	 * Called when the user clicks on the delete button.
//	 */
//	@FXML
//	private void handleDeletePerson () {
//		int selectedIndex = personTable.getSelectionModel ().getSelectedIndex ();
//		if (selectedIndex >= 0) {
//
//			Person persontoDelete = personTable.getSelectionModel ().getSelectedItem ();
//			model.deletePerson (persontoDelete);
//			personTable.getItems ().remove (selectedIndex);
//
//		} else {
//			// Nothing selected.
//			Alert alert = new Alert (AlertType.WARNING);
//			alert.initOwner (addressBookView.getPrimaryStage ());
//			alert.setTitle ("No Selection");
//			alert.setHeaderText ("No Person Selected");
//			alert.setContentText ("Please select a person in the table.");
//
//			alert.showAndWait ();
//		}
//	}
//
//	/**
//	 * Called when the user clicks the new button. Opens a dialog to edit
//	 * details for a new person.
//	 */
//	@FXML
//	private void handleNewPerson () {
//		Person tempPerson = new Person ();
//		boolean okClicked = addressBookView.showPersonEditDialog (tempPerson);
//		if (okClicked) {
//			addressBookView.getPersonData ().add (tempPerson);
//			model.addPerson (tempPerson);
//
//		}
//	}
//
//	/**
//	 * Called when the user clicks the edit button. Opens a dialog to edit
//	 * details for the selected person.
//	 */
//	@FXML
//	private void handleEditPerson () {
//		Person selectedPerson = personTable.getSelectionModel ().getSelectedItem ();
//		if (selectedPerson != null) {
//			boolean okClicked = addressBookView.showPersonEditDialog (selectedPerson);
//			if (okClicked) {
//				showPersonDetails (selectedPerson);
//
//			}
//
//		} else {
//			// Nothing selected.
//			Alert alert = new Alert (AlertType.WARNING);
//			alert.initOwner (addressBookView.getPrimaryStage ());
//			alert.setTitle ("No Selection");
//			alert.setHeaderText ("No Person Selected");
//			alert.setContentText ("Please select a person in the table.");
//
//			alert.showAndWait ();
//		}
//	}
}