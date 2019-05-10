import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ElectronicStoreApp extends Application {
	private ListView<Product> mlist;
	private ListView<Product> sList;
	private ListView<Product> clist;
	Button addButton = new Button("Add to Cart");
	Button removeButton = new Button("Remove from Cart");
	// private boolean carta;
	private int total;
	private int saleNum = 0;
	private double ps;
	private double revenueNum = 0.0;
	private String windowName;
	ObservableList<Product> stocks;
	ObservableList<Product> cart = FXCollections.observableList(new ArrayList<>());
	ObservableList<Product> popularProducts = FXCollections.observableList(new ArrayList<>());
	
	ElectronicStore store = ElectronicStore.createStore();

	public void start(Stage primaryStage) {

		// Create the labels
		Label label1 = new Label("Store Summary:");
		label1.setPrefSize(190, 30);
		label1.setAlignment(Pos.CENTER);

		Label label2 = new Label("Store Stock:");
		label2.setPrefSize(300, 30);

		Label label3 = new Label("Current Cart" + "(" + total + ")");
		label3.setPrefSize(300, 30);

		Label label4 = new Label("# sales: ");
		label4.setPrefSize(90, 30);
		label4.setAlignment(Pos.CENTER_RIGHT);
		Label label5 = new Label("Revenue: ");
		label5.setPrefSize(90, 30);
		label5.setAlignment(Pos.CENTER_RIGHT);
		Label label6 = new Label("$/ Sale: ");
		label6.setPrefSize(90, 30);
		label6.setAlignment(Pos.CENTER_RIGHT);
		Label label7 = new Label("Most popular items: ");
		label7.setPrefSize(200, 30);
		label7.setAlignment(Pos.CENTER);
		// Create the TextFields
		TextField saleField = new TextField(Integer.toString(saleNum));
		saleField.setPrefSize(80, 30);
		TextField revenueField = new TextField(Double.toString(revenueNum));
		revenueField.setPrefSize(80, 30);
		TextField lField = new TextField("N\\A");
		lField.setPrefSize(80, 30);
		// Create the lists
		mlist = new ListView<Product>();
		mlist.setPrefSize(180, 190);

		sList = new ListView<Product>();
		sList.setPrefSize(280, 313);
		sList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {

			@Override
			public void changed(ObservableValue<? extends Product> arg0, Product arg1, Product arg2) {
				if (arg2 != null) {
					addButton.setDisable(false);
				} else {
					addButton.setDisable(true);
				}
			}
		});

		clist = new ListView<Product>();
		clist.setPrefSize(280, 311);
		clist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {

			@Override
			public void changed(ObservableValue<? extends Product> arg0, Product arg1, Product arg2) {
				if (arg2 != null) {
					removeButton.setDisable(false);
				} else {
					removeButton.setDisable(true);
				}
			}
		});
		// sList.setItems(FXCollections.observableArrayList(titles));
		// tList.relocate(10, 40);
		// tList.setPrefSize(540, 150);
		// Create the buttons

		Button aButton = new Button("Reset Store");
		aButton.setPrefSize(170, 50);
		aButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(255,255,255); " + "-fx-text-fill: rgb(0,0,0);");
		aButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				initStore();
				saleNum = 0;
				revenueNum = 0.0;
				saleField.setText(Integer.toString(saleNum));
				revenueField.setText(Double.toString(revenueNum));
				total = 0;
				label3.setText("Current Cart" + "(" + total + ")");
				lField.setText("N\\A");

			}

		});

		addButton.setPrefSize(170, 50);
		addButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(255,255,255); " + "-fx-text-fill: rgb(0,0,0);");

		addButton.setDisable(true);
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Product product = sList.getSelectionModel().getSelectedItem();

				if (product.getQuantity() > 0) {
					product.setQuantity(product.getQuantity() - 1);
					;
					cart.add(product);
					compute();
					label3.setText("Current Cart" + "(" + total + ")");
				}

				if (product.getQuantity() <= 0) {
					stocks.remove(product);
				}

			}

		});

		removeButton.setPrefSize(140, 50);
		removeButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(255,255,255); " + "-fx-text-fill: rgb(0,0,0);");
		removeButton.setDisable(true);
		removeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Product product = clist.getSelectionModel().getSelectedItem();
				cart.remove(product);
				if (stocks.contains(product)) {
					product.setQuantity(product.getQuantity() + 1);
				} else {
					stocks.add(product);
				}
			}

		});
		Button completeButton = new Button("Complete Sale");
		completeButton.setPrefSize(140, 50);
		completeButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(255,255,255); " + "-fx-text-fill: rgb(0,0,0);");
		completeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				saleNum++;
				saleField.setText(Integer.toString(saleNum));

				revenueNum += total;
				revenueField.setText(Double.toString(revenueNum));
				ps = revenueNum / saleNum;
				lField.setText(Double.toString(ps));
				total = 0;
				label3.setText("Current Cart" + "(" + total + ")");
				for (Product product : cart) {
					product.sellUnits(1);
				}
				cart.clear();
				List<Product> popularList = new ArrayList<Product>( (store.stock));
				List<Product> pL = new ArrayList<Product>();
				Collections.sort(popularList, Product.getPolularComparator());
				popularProducts.clear();
				for (int i= 0; i < 3; i++) {
					popularProducts.add(popularList.get(i));
					//System.out.println(i + ": " + popularList.get(i).toString() + ": sold = " + popularList.get(i).getSold());
				}
			}

		});
		// removeButton.setDisable(carta);
		Pane aPane = new Pane();
		HBox hBox = new HBox(0);
		VBox vBox1 = new VBox(0);
		VBox vBox2 = new VBox(0);
		VBox vBox3 = new VBox(0);

		HBox hBox1 = new HBox(0);
		HBox hBox2 = new HBox(0);
		HBox hBox3 = new HBox(0);
		HBox hBox4 = new HBox(0);
		HBox hBox5 = new HBox(0);
		HBox hBox6 = new HBox(0);
		HBox hBox7 = new HBox(0);
		HBox hBox8 = new HBox(0);
		HBox hBox9 = new HBox(0);
		HBox hBox10 = new HBox(0);
		hBox9.setAlignment(Pos.CENTER_LEFT);
		hBox8.setAlignment(Pos.CENTER_LEFT);
		hBox7.setAlignment(Pos.CENTER);
		hBox5.setAlignment(Pos.CENTER);
		hBox6.setAlignment(Pos.CENTER);
		hBox10.setAlignment(Pos.CENTER_LEFT);

		vBox2.getChildren().add(0, label2);
		vBox2.getChildren().add(1, hBox8);
		vBox2.getChildren().add(2, hBox7);
		vBox3.getChildren().add(0, label3);
		vBox3.getChildren().add(1, hBox9);
		vBox3.getChildren().add(2, hBox10);
		hBox1.getChildren().addAll(label4, saleField);
		hBox2.getChildren().addAll(label5, revenueField);
		hBox3.getChildren().addAll(label6, lField);
		hBox4.getChildren().add(label7);
		hBox5.getChildren().add(mlist);
		hBox6.getChildren().add(aButton);
		hBox7.getChildren().add(addButton);
		hBox8.getChildren().add(sList);
		hBox9.getChildren().add(clist);
		hBox10.getChildren().addAll(removeButton, completeButton);

		vBox1.getChildren().addAll(label1, hBox1, hBox2, hBox3, hBox4, hBox5, hBox6);
		hBox.getChildren().addAll(vBox1, vBox2, vBox3);

		aPane.getChildren().addAll(hBox);
		initStore();
		primaryStage.setTitle("Electronic Store Application -" + windowName);
		// primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(aPane, 800, 400));
		primaryStage.show();

	}

	public void initStore() {
		store = ElectronicStore.createStore();
		stocks = FXCollections.observableList(store.stock);
		sList.setItems(stocks);
		mlist.setItems(popularProducts);
		popularProducts.clear();
		cart.clear();
		clist.setItems(cart);
		// System.out.println(cart.isEmpty());
		windowName = store.name;
	}

	public void compute() {
		total = 0;
		for (Product product : cart) {
			total += product.getPrice();
		}

		System.out.println(total);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
