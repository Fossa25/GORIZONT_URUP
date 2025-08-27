package com.example.proburok;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ProhodCOD extends Configs{
    @FXML private DatePicker data;
    @FXML private ComboBox<String> sehenbox;
    @FXML private TextField gorizont;
    @FXML private TextField sehen;
    @FXML private TextField nameProhod;

    @FXML private TextField nomer;
    @FXML private TextField privizka;
    @FXML private Button singUpButtun;
    private String NOMER;
    private Date DATA;
    private String SEHEN;
    private String GORIZONT;
    private String NAME;
    private String PRIVIZKA;
    @FXML private TableView<Probnik> Tablshen;

    @FXML private TableView<Probnik1> Tablshen1;
    @FXML private TableColumn<Probnik, String> stolb1;
    @FXML private TableColumn<Probnik, Double> stolb2;
    @FXML private TableColumn<Probnik1, String> stolb11;
    @FXML private TableColumn<Probnik1, String> stolb21;

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView pethat;
    @FXML private ImageView tabl;
    @FXML private ComboBox<String> ushatok;
    @FXML
    void initialize() {
       tabltrabl();
        data.setValue(LocalDate.now());
        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml"));
        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));

        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Юбилейного месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });
        // Ограничиваем ввод в поле номера только цифрами
        nomer.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[^/\\\\|?]*")) {
                return change;
            }
            return null;
        }));

        ushatok.getItems().addAll("1 участок", "2 участок", "3 участок");

        // В методе initialize() класса ProhodCOD
        ushatok.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && data.getValue() != null) {
                String year = String.valueOf(data.getValue().getYear());
                String prefix = "";
                if (newValue.equals("1 участок")) prefix = "1";
                else if (newValue.equals("2 участок")) prefix = "2";
                else if (newValue.equals("3 участок")) prefix = "3";

                // Получаем следующий порядковый номер из БД
                DatabaseHandler dbHandler = new DatabaseHandler();
                int nextNumber = dbHandler.getNextSequenceNumber(prefix, year);

                // Устанавливаем номер в формате "1-5-2025"
                nomer.setText(prefix + "-" + nextNumber + "-" + year);
            }
        });
        singUpButtun.setOnMouseClicked(mouseEvent -> {
            NOMER = nomer.getText() != null ? nomer.getText().trim() : "";
            SEHEN = sehen.getText() != null ? sehen.getText().trim() : "";
            GORIZONT = gorizont.getText() != null ? gorizont.getText().trim() : "";
            PRIVIZKA = privizka.getText() != null ? privizka.getText().trim() : "";
            NAME = nameProhod.getText() != null ? nameProhod.getText().trim() : "";
            LocalDate selectedDate = data.getValue();
            try {
                StringBuilder errors = new StringBuilder();
                if (NOMER.isEmpty()) errors.append("- Не заполнен номер\n");
                if (selectedDate == null) {
                    errors.append("- Не выбрана дата\n");
                } else {
                    DATA = Date.valueOf(selectedDate); // Преобразуем только если дата выбрана
                }
                if (SEHEN.isEmpty()) errors.append("- Не выбрано сечение\n");
                if (GORIZONT.isEmpty()) errors.append("- Не заполнен горизонт\n");
                if (PRIVIZKA.isEmpty()) errors.append("- Не заполнена привязка\n");
                if (NAME.isEmpty()) errors.append("- Не заполнено название выработки\n");

                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors);
                    return;
                }
                String nameBD = NAME + " № " + NOMER;
                String prim =  "Требуется геологическое описание" ;

                // Все данные валидны - сохраняем
                DatabaseHandler Tabl = new DatabaseHandler();
                Tabl.Dobavlenie(NOMER, DATA, SEHEN, GORIZONT, PRIVIZKA, nameBD,NAME,ushatok.getValue(),prim);

                ohistka();

            } catch (DateTimeException e) {
                showAlert("Ошибка в формате даты!");
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });


    }
    private void ohistka() {
        ushatok.setValue("");
        nomer.setText(""); // Используйте пустую строку вместо null
        sehen.setText("");
        gorizont.setText("");
        privizka.setText("");
        nameProhod.setText("");
    }
    // Метод для показа всплывающего окна с ошибкой
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

private void tabltrabl(){
    stolb1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka()));
    stolb2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlohad()).asObject());

    // Для второй таблицы
    stolb11.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka1()));
    stolb21.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNety()));

    ObservableList<Probnik> tabl = FXCollections.observableArrayList(
            new Probnik("Полевой штрек, автотранспортный уклон (ДСО) (на закруглении)", 17.9),
            new Probnik("Полевой штрек, автотранспортный уклон (ДСО)", 14.7),
            new Probnik("Подэтажный штрек, заезд на подэтаж", 13.6),
            new Probnik("Заезд к рудному телу, автотранспортный съезд на подэтаже (в пределах блока)", 12.8),
            new Probnik("Заезд к рудному телу", 12.5),
            new Probnik("Полевой штрек, блоковый квершлаг (ж/д)", 8.5),
            new Probnik("Блоковый квершлаг (ж/д)", 7.7),
            new Probnik("Ниша лебедки ЛС55", 6.9),
            new Probnik("Штрек подсечки, буровой восстающий", 5.8),
            new Probnik("МХВ", 5.1),
            new Probnik("Блоковый МХВ", 4.1),
            new Probnik("Выработка скреперования", 3.7),
            new Probnik("Ниша выпускной дучки", 2.5),
            new Probnik("Рудоспуск, дучка", 2.25)
    );
    ObservableList<Probnik1> tabl1 = FXCollections.observableArrayList(
            new Probnik1 ("Автотранспортный уклон – съезд на подэтаж","14.7/14.7"),
            new Probnik1 ("Панельный штрек – орт-заезд","12.8/12.8"),
            new Probnik1 ("Полевой штрек (ж/д) – квершлаг (ж/д)","8.5/8.5"),
            new Probnik1 ("Орт подсечки (подсечная выработка) – отрезной штрек","5.8/5.8"),
            new Probnik1 ("Скреперный штрек (орт) – вентиляционно-ходовая сбойка","3.7/3.7"),
            new Probnik1 ("Скреперный штрек (орт) – ниша дучки","3.7/2.5"),
            new Probnik1 ("Орт-заезд – вентиляционно-ходовая сбойка","12.8|12.8")
    );

    stolb1.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : item);
            setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
        }
    });
    stolb2.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : String.valueOf(item));
            setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
        }
    });
    stolb11.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : item);
            setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
        }
    });
    stolb21.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : item);
            setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
        }
    });
    Tablshen.setItems(tabl);
    Tablshen1.setItems(tabl1);

    Tablshen.setOnMouseClicked(mouseEvent -> {
        Probnik selectedPerson = Tablshen.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            // Отображаем значение из первого столбца ("Имя") в TextField
            sehen.setText(String.valueOf(selectedPerson.getPlohad()));
        }
    });
    Tablshen1.setOnMouseClicked(mouseEvent -> {
        Probnik1 selectedPerson = Tablshen1.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            // Отображаем значение из первого столбца ("Имя") в TextField
            sehen.setText(String.valueOf(selectedPerson.getNety()));
        }
    });}

    }






