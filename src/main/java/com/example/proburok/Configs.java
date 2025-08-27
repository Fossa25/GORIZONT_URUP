package com.example.proburok;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Configs {
    //protected String dbHost = "10.38.1.214";
    protected String dbHost = ConfigLoader.getProperty("dbHost");
    protected String dbPort = "3306";
    protected String dbUser = "TOPA";
    protected String dbPass = "300122";
    protected String dbName = "gorizont";

    protected String OUTPUT_PATH = ConfigLoader.getProperty("OUTPUT_PATH");
    protected String Put =  ConfigLoader.getProperty("Put");

    protected String Put_albom ="/com/example/proburok/docs/AlbomUrup.pdf";
    protected String Put_geolog ="/com/example/proburok/docs/GeologiUrup.pdf";
    protected String Put_instr ="/com/example/proburok/docs/instruk.pdf";

    protected String TEMPLATE_PATH1 = "/com/example/proburok/docs/template_1.docx";
    protected String TEMPLATE_PATH2 = "/com/example/proburok/docs/template_2.docx";
    protected String TEMPLATE_PATH3 = "/com/example/proburok/docs/template_3.docx";
    protected String TEMPLATE_PATH4 = "/com/example/proburok/docs/template_4.docx";
    protected String TEMPLATE_PATH5 = "/com/example/proburok/docs/template_5.docx";
    protected String TEMPLATE_PATH6 = "/com/example/proburok/docs/template_6.docx";
    protected String TEMPLATE_PATH7 = "/com/example/proburok/docs/template_7.docx";
    protected String TEMPLATE_PATH8 = "/com/example/proburok/docs/template_8.docx";


    protected String TEMPLATE_PATH1_SOPR = "/com/example/proburok/docs/template_1S.docx";

    protected String TEMPLATE_PATH3_SOPR = "/com/example/proburok/docs/template_3S.docx";

    protected String TEMPLATE_PATH5_SOPR = "/com/example/proburok/docs/template_5S.docx";

    protected String TEMPLATE_PATH7_SOPR = "/com/example/proburok/docs/template_7S.docx";



protected String HABLON_PATH = "/com/example/proburok/hablon";

protected String HABLON_PATH_VID =HABLON_PATH+"/obvid";
    protected String HABLON_PATH_SOPR =HABLON_PATH+"/soprigenii";

protected String HABLON_PATH_ILIMENT =HABLON_PATH+"/ilement";

protected String HABLON_PATH_USTANOVKA =HABLON_PATH+"/ustanovka";
protected String HABLON_PATH_USTANOVKA_SOPR =HABLON_PATH+"/ustanovka_sopr";

public void openNewScene(String Window){

        // Загрузка нового окна
        FXMLLoader loader = new FXMLLoader();

        // Проверка пути к FXML-файлу
        URL fxmlUrl = getClass().getResource(Window);

        loader.setLocation(fxmlUrl);
        try {
            Parent root = loader.load(); // Загрузка FXML
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OpenDok(String Put,String NemDok){
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Получаем поток входных данных для ресурса
                InputStream inputStream = getClass().getResourceAsStream(Put);
                if (inputStream == null) {
                    throw new FileNotFoundException("Ресурс не найден: " + Put);
                }
                // Создаем временный файл
                Path tempFile = Files.createTempFile(NemDok, ".pdf");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Открываем временный файл
                desktop.open(tempFile.toFile());
                // Опционально: удаление временного файла после использования
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        Files.deleteIfExists(tempFile);
                    } catch (IOException e) {
                        System.err.println("Не удалось удалить временный файл: " + tempFile);
                    }
                }));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка при открытии документа", e);
            }
        }
    }
}


//protected String dbHost = "localhost";
//protected String dbPort = "3306";
//protected String dbUser = "root";
//protected String dbPass = "Kliptomen250598";
//protected String dbName = "icproger";
//protected String Put = "E://Изображения";

//protected String OUTPUT_PATH = "C://Горизонт_Уруп//Паспорта//";
//protected String Put =  "C://Горизонт_Уруп//Изображения";

//protected String Put =  "src//main//resources//com//example//proburok//Изображения";
//protected String TEMPLATE_PATH = "src//main//resources//com//example//proburok//template.docx";
////private static final String TEMPLATE_PATH = "E://ProbUrok//src//main//resources//com//example//proburok//template.docx";
//protected String OUTPUT_PATH = "C://Паспорта//";
//protected String HABLON_PATH = "src//main//resources//com//example//proburok//шаблоны";
//protected String HABLON_PATH_SXEMATEX =HABLON_PATH+"/горно-техфакт";
//protected String HABLON_PATH_VID =HABLON_PATH+"/общий вид крепи";
//protected String HABLON_PATH_ILIMENT =HABLON_PATH+"/элементы крепи";