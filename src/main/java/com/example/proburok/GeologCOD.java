package com.example.proburok;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GeologCOD extends Configs {

    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private Button B4;
    @FXML private Button B5;
    @FXML private Button B6;
    @FXML private Button B7;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private TextArea opisanie;
    @FXML private TextField date;
    @FXML private TextField katigoria;
    @FXML private TextField nomer;
    @FXML private Button singUpButtun;
    @FXML private ComboBox<String> gorbox;
    @FXML private ComboBox<String> namebox;
    @FXML private ComboBox<String> ugli;
    @FXML private TextField sehen;

    @FXML private ImageView planVNS;

    @FXML private ImageView planVNSNE;

    @FXML private ImageView planobnov;

    @FXML private ImageView poperVNS;

    @FXML private ImageView poperVNSNE;

    @FXML private ImageView poperobnov;

    @FXML private ImageView prodolVNS;

    @FXML private ImageView prodolVNSNE;
    @FXML private ImageView instr;
    @FXML private ImageView prodolobnov;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private TextField dlina;
    @FXML private ImageView tabl;
    @FXML private ImageView pethat;
    @FXML private CheckBox CB;
    //CB.setDisable(false);
    public String blak= " -fx-border-color: #00000000;-fx-background-color:#00000000;-fx-border-width: 0px";
    public String red = "-fx-border-color: #14b814;-fx-background-color:#00000000;-fx-border-width: 3px";
    private String tippas;
    List<String> soprigenii = Arrays.asList("12.8/12.8", "3.7/3.7", "3.7/2.5", "14.7/14.7", "8.5/8.5", "5.8/5.8", "12.8|12.8");
    //private String NOMER;
    public String tex1 = "Слоистый массив кварцевых альбитофиров не затронутых гидротермальными процессами. Шероховатые матовые поверхности структурных нарушений, наличие поперечных трещин.";
    public String tex2 = "Слоистый массив гидротермально измененных кварцевых альбитофиров из тонких продолговатых выклинивающихся плиток. Структурные нарушения скользкие, с глянцевым блеском, " +
            "заполнены серицитом и тальком. Склонность к размягчению и набуханию от влаги";
    public String tex2_V ="Слоистый массив гидротермально измененных кварцевых альбитофиров из тонких продолговатых выклинивающихся плиток с сульфидной минерализацией. " +
            "Структурные нарушения скользкие, с глянцевым блеском, заполнены серицитом и тальком. " +
            "Склонность к размягчению и набуханию от влаги; при обширной минерализации может наблюдаться некоторая сыпучесть.";
    public String tex3 = "Интенсивно перемятый массив туфов из острых фрагментов пород, пересеченных кварц-карбонатными прожилками. Стеклянный блеск. ";
    public String tex3_V= "Интенсивно перемятый массив туфов из острых фрагментов пород, пересеченных кварц-карбонатными прожилками, наличие вкрапленной и прожилково-вкрапленной минерализации. Стеклянный блеск.";
    public String tex4 = "Массив прочных слоистых туфов (кремнистых сланцев). Шероховатые поверхности трещин";
    public String tex5 = "Сплошной массив медной руды. Микротрещиноватый, колкий, хрупкий.";
    public String tex6 = "Сплошной массив серной руды. Высокопрочный, вязкий.";


    @FXML
    void initialize() {
        dlina.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("[0-9]*([.,][0-9]*)?")) {
                return change;
            }
            return null;
        }));
        CB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Изменение состояния чекбокса: " + newValue);
            String currentCategory = katigoria.getText();

            if (newValue) {
                if ("2".equals(currentCategory)) {
                    System.out.println("Установка tex2_V");
                    opisanie.setText(tex2_V);
                } else if ("3".equals(currentCategory)) {
                    System.out.println("Установка tex3_V");
                    opisanie.setText(tex3_V);
                }
            } else {
                if ("2".equals(currentCategory)) {
                    System.out.println("Установка tex2");
                    opisanie.setText(tex2);
                } else if ("3".equals(currentCategory)) {
                    System.out.println("Установка tex3");
                    opisanie.setText(tex3);
                }
            }
        });
        String initCategory = katigoria.getText();
        opisanie.setText(CB.isSelected() ?
                ("2".equals(initCategory) ? tex2_V : ("3".equals(initCategory) ? tex3_V : "")) :
                ("2".equals(initCategory) ? tex2 : ("3".equals(initCategory) ? tex3 : "")));


        setupTooltips(); // Добавляем подсказки
        setupCursor();   // Настраиваем курсор
        ugli.getItems().addAll("35-45", "45-65", "65-80","Вкрест");
        setupImageHandlers();
        viborKatigorii();

        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));
        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml"));

        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Baza> bazas = dbHandler.getAllBaza();
        List<String> nom = bazas.stream() //заполнение базы
                .map(Baza::getGORIZONT)
                .distinct()
                .collect(Collectors.toList());

        gorbox.setItems(FXCollections.observableArrayList(nom));

        // Слушатель изменений выбора в gorbox
        gorbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<Baza> namespisok = dbHandler.poiskName(newValue);
                List<String> imi = namespisok.stream() //заполнение базы
                        .map(Baza::getNAME)
                        .distinct()
                        .toList();
                ohistka();

                namebox.setItems(FXCollections.observableArrayList(imi));
            }
        });
        namebox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null ) {
                poisk(gorbox.getValue(),newValue);
                viborKatigorii();
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            String kategoriyaValue = katigoria.getText();
            String opisanieValue = opisanie.getText();
            String selectPrivizka = date.getText();
            String selecteUgol = ugli.getValue();
            String dlinaValue = dlina.getText();
            proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
                if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}

                if (kategoriyaValue == null || kategoriyaValue.isEmpty()) { errors.append("- Не заполнено поле категория \n"); }
                if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле описание\n");}
                if (selecteUgol == null || selecteUgol.isEmpty()) {errors.append("- Не выбран угол\n");}
                if (planVNS.isVisible() || planVNSNE.isVisible() ) {errors.append("- Не внесён план\n");}
                if (poperVNS.isVisible() || poperVNSNE.isVisible() ) {errors.append("- Не внесён поперечный разрез\n");}
                if (prodolVNS.isVisible() || prodolVNSNE.isVisible() ) {errors.append("- Не внесён продольный разрез\n");}
                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                // Все данные валидны - выполняем сохранение
                getPas(kategoriyaValue);
                String prim =  "Все данные внесены" ;
                new DatabaseHandler().DobavlenieGEOLOG(kategoriyaValue, opisanieValue,selecteUgol,tippas,dlinaValue, selectedGor, selectedName,prim);
                System.out.println("ТИПОВОЙ ПАСПОРТ= "+tippas);
                ohistka();
              // gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Юбилейного месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });
    }
    private void viborKatigorii(){
        if (soprigenii.contains(sehen.getText())) {
            klikKatigorii(B1,"1","no",tex1,"no");
            klikKatigorii(B2,"2","no",tex2,"yes");
            klikKatigorii(B3,"3","no",tex3,"yes");
            klikKatigorii(B4,"4","no",tex4,"no");
            klikKatigorii(B5,"5","no",tex5,"no");
            klikKatigorii(B6,"6","no",tex6,"no");
        }else {
            klikKatigorii(B1,"1","no",tex1,"no");
            klikKatigorii(B2,"2","yes",tex2,"yes");
            klikKatigorii(B3,"3","no",tex3,"yes");
            klikKatigorii(B4,"4","no",tex4,"no");
            klikKatigorii(B5,"5","no",tex5,"no");
            klikKatigorii(B6,"6","no",tex6,"no");
        }
    }

    private void klikKatigorii(Button imageView, String kat, String x, String tx,String krigik  ) {
        imageView.setOnMouseClicked(e -> {
            obnul(kat,tx);
            imageView.setStyle(red);
            if (x.equals("yes")){

                tikalka("y");
                ugli.setValue(null);
            } else if (x.equals("no")) {
                tikalka("n");
                ugli.setEditable(true);
                ugli.setValue("-");
            }
            if (krigik.equals("yes")){
                CB.setDisable(false);
                CB.setSelected(false);
            } else if (krigik.equals("no")) {
                CB.setDisable(true);
                CB.setSelected(false);
            }

        });
    }
    void tikalka(String yn){
        if (yn.equals("y")){
            ugli.setMouseTransparent(false); // Игнорировать клики
            ugli.setFocusTraversable(true);
            ugli.setEditable(false);
        } else if (yn.equals("n")) {

            ugli.setMouseTransparent(true); // Игнорировать клики
            ugli.setFocusTraversable(false); // Не получать фокус

        }
    }
    private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План");
        openImageHandler(PoperVKL, "Поперечный");
        openImageHandler(ProdolVKL, "Продольный");

        obnovaImageHandler(planobnov, "План");
        obnovaImageHandler(poperobnov, "Поперечный");
        obnovaImageHandler(prodolobnov, "Продольный");

        sozdaniiImageHandler(planVNS, "План",PlanVKL,PlanVKLNe,planVNS,planobnov);
        sozdaniiImageHandler(poperVNS, "Поперечный",PoperVKL,PoperVKLNe,poperVNS,poperobnov);
        sozdaniiImageHandler(prodolVNS, "Продольный",ProdolVKL,ProdolVKLNe,prodolVNS,prodolobnov);
    }

    private void openImageHandler(ImageView imageView, String folder) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder);
        });
    }
    private void obnovaImageHandler(ImageView imageView, String folder) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            Sosdatpapky(nomer.getText(),folder);
        });
    }
    private void sozdaniiImageHandler(ImageView imageView, String folder,ImageView image1,ImageView image2,ImageView image3,ImageView image4) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            Sosdatpapky(nomer.getText(),folder);
            image1.setVisible(true); image2.setVisible(false);
            image3.setVisible(false); image4.setVisible(true);
        });}
    public void obnul(String x,String tx){
        if (x.equals("x")) {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            B6.setStyle(blak);
            B7.setStyle(blak);
            opisanie.setText(tx);

        }else {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            B6.setStyle(blak);
            B7.setStyle(blak);
            katigoria.setText(x);
            opisanie.setText(tx);
        }
    }

    private void poisk (String viborGOR, String viborName) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);

        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            //SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            date.setText(poluh.getPRIVIZKA());
            katigoria.setText(poluh.getKATEGORII());
            if ((katigoria.getText() ==null) || katigoria.getText().isEmpty() ){
                obnul("x","");
                System.out.println("Категории нет ");
            }else {
                switch (katigoria.getText()){
                    case"1" -> {obnul("x",opisanie.getText());B1.setStyle(red);tikalka("n");}
                    case"2" -> {obnul("x",opisanie.getText());B2.setStyle(red);tikalka("y");}
                    case"3" -> {obnul("x",opisanie.getText());B3.setStyle(red);tikalka("n");}
                    case"4" -> {obnul("x",opisanie.getText());B4.setStyle(red);tikalka("n");}
                    case"5" ->{obnul("x",opisanie.getText());B5.setStyle(red);tikalka("n");}
                    case"6" -> {obnul("x",opisanie.getText());B6.setStyle(red);tikalka("n");}
                    case"7" -> {obnul("x",opisanie.getText());B7.setStyle(red);tikalka("y");}

                }}
            opisanie.setText(poluh.getOPISANIE());
            ugli.setValue(poluh.getUGOL());
            sehen.setText(poluh.getSEHEN());
            dlina.setText(poluh.getDLINA());

             planVNS.setVisible(true); planVNSNE.setVisible(false);
            poperVNS.setVisible(true); poperVNSNE.setVisible(false);
            prodolVNS.setVisible(true); prodolVNSNE.setVisible(false);


            if ("2".equals(katigoria.getText()) || "3".equals(katigoria.getText())) {
                CB.setDisable(false);
                if (tex2_V.equals(opisanie.getText())) {
                    CB.setSelected(true);

                } else  if (tex3_V.equals(opisanie.getText())) {
                    CB.setSelected(true);
                }else {CB.setSelected(false);}



            } else {
                CB.setDisable(true);
            }

            proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
        } else {

            System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
            nomer.clear();
            date.clear(); // Пользователь не найден
        }
    }
    private void ohistka(){
        obnul("x","");
        //namebox.setValue(null);
        nomer.setText(null);
        date.setText(null);
        katigoria.setText(null);
        opisanie.setText(null);
        dlina.setText(null);
        ugli.setValue(null);
        tippas="";
        CB.setDisable(true);
        CB.setSelected(false);

        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);planobnov.setVisible(false);
        planVNS.setVisible(false); planVNSNE.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);poperobnov.setVisible(false);
        poperVNS.setVisible(false); poperVNSNE.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);prodolobnov.setVisible(false);
        prodolVNS.setVisible(false); prodolVNSNE.setVisible(true);
    }
    public void Sosdatpapky(String gor, String papka){

        String path = Put + "/" + gor + "/" + papka;
        File newFolder = new File(path);

        // Создаем папку, если её нет
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }

        // Удаляем все существующие файлы в папке
        File[] existingFiles = newFolder.listFiles();
        if (existingFiles != null) {
            for (File file : existingFiles) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }

        // Диалог выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Window mainWindow = planobnov.getScene().getWindow(); // Получаем текущее окно
        File selectedFile = fileChooser.showOpenDialog(mainWindow);

        if (selectedFile != null) {
            try {
                File destinationFile = new File(newFolder.getAbsolutePath() + "/" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Файл скопирован: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка при копировании файла: " + e.getMessage());
            }
        }
    }

    void openImage(String imagePath) {
        File folder = new File(imagePath);

        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            return;
        }

        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });

        // Проверяем, есть ли изображения в папке
        if (files != null && files.length > 0) {
            // Берем первый файл (первое изображение)
            File imageFile = files[0];

            try {
                // Открываем изображение
                Desktop.getDesktop().open(imageFile);
                System.out.println("Открыто изображение: " + imageFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Не удалось открыть изображение: " + e.getMessage());
            }
        } else {
            System.err.println("В папке нет изображений.");
        }
    }


    void proverkaImage(String imagePath,ImageView VNS,ImageView VNSNE,ImageView VKL,ImageView VKLNE,ImageView OBNV) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            VNS.setVisible(true); VNSNE.setVisible(false);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
            return;
        }
        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });
        // Проверяем, есть ли изображения в папке
        if (files != null && files.length > 0) {

            VNS.setVisible(false); VNSNE.setVisible(false);
            VKL.setVisible(true);VKLNE.setVisible(false);OBNV.setVisible(true);

        } else {
            System.err.println("В папке нет изображений.");
            VNS.setVisible(true); VNSNE.setVisible(false);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
        }
    }
    private void setupTooltips() {
        Tooltip.install(planVNS, createTooltip("Внесите изображение плана"));
        Tooltip.install(PlanVKL, createTooltip("Показать план"));
        Tooltip.install(PlanVKLNe, createTooltip("План не внесён"));
        Tooltip.install(planobnov, createTooltip("Обновить изображение плана"));

        Tooltip.install(poperVNS, createTooltip("Внесите изображение поперечного разреза"));
        Tooltip.install(PoperVKL, createTooltip("Показать поперечный разрез"));
        Tooltip.install(PoperVKLNe, createTooltip("Поперечный разрез не внесён"));
        Tooltip.install(poperobnov, createTooltip("Обновить изображение поперечного разреза"));

        Tooltip.install(prodolVNS, createTooltip("Внесите изображение продольного разреза"));
        Tooltip.install(ProdolVKL, createTooltip("Показать продольный разрез"));
        Tooltip.install(ProdolVKLNe, createTooltip("Продольный разрез не внесён"));
        Tooltip.install(prodolobnov, createTooltip("Обновить изображение продольного разреза"));

    }
    private Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setStyle("-fx-font-size: 14; -fx-background-color: #aa9455;");
        return tooltip;
    }
    private void setupCursor() {
        String hoverStyle = "-fx-cursor: hand;";//рука
        //String zapret = "-fx-cursor: wait;";//загрузка круг crosshair;";//крест
        String zapret = "-fx-cursor: crosshair;";//крест+
        String obnov = "-fx-cursor: wait;";

        planVNS.setStyle(hoverStyle);
        PlanVKL.setStyle(hoverStyle);
        planVNSNE.setStyle(zapret);
        PlanVKLNe.setStyle(zapret);
        planobnov.setStyle(obnov);

        poperVNS.setStyle(hoverStyle);
        PoperVKL.setStyle(hoverStyle);
        poperVNSNE.setStyle(zapret);
        PoperVKLNe.setStyle(zapret);
        poperobnov.setStyle(obnov);

        prodolVNS.setStyle(hoverStyle);
        ProdolVKL.setStyle(hoverStyle);
        prodolVNSNE.setStyle(zapret);
        ProdolVKLNe.setStyle(zapret);
        prodolobnov.setStyle(obnov);


    }
    void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void getPas (String kategor) {
        String kat = katigoria.getText();
        String shen=sehen.getText();
        String Nopas = "Типовой паспорт не разработан";
        if (!kat.isEmpty()){
            if (soprigenii.contains(shen)) {
                switch (kategor) {
                    case "1" -> TP_SOPR(shen,"1",Nopas,          Nopas,   "18","24",    Nopas,"36");
                    case "2" -> TP_SOPR(shen,"2","7","13","19","25","30","37");
                    case "3" ->TP_SOPR(shen,"3","8","14","20","26","31","38");
                    case "4" -> TP_SOPR(shen,"4","10","15","21","27","33","39");
                    case "5" -> TP_SOPR(shen,"5","11","16","22","28","34","40");
                    case "6" ->TP_SOPR(shen,"6","12","17","23","29","35","41");


                    default -> throw new IllegalStateException("Unexpected value: " + kategor);
                }
            }else{
            switch (kategor) {
                case "1" -> TP_1_6(shen,"1","2","3","4","5","6","7","8","9",Nopas,"10","11","12","13");
                case "2" -> getFAKT2(shen);
                case "3" -> TP_1_6(shen,"70","71","72","73","74","75","76","77","78","79","80","81",Nopas, Nopas);
                case "4" -> TP_1_6(shen,"92","93","94","95","96","97","98","99","100","101","102","103","104", "105");
                case "5" -> TP_1_6(shen,"106","107","108","109","110","111","112","113","114","115","116","117","118", "119");
                case "6" ->TP_1_6(shen,"129","130","131","132","133","134","135","136","137","138", "139", "140","141", "142");


                default -> throw new IllegalStateException("Unexpected value: " + kategor);
            }}
        }}

    private void TP_1_6 (String SH,String x14_7,String x17_9,String x8_5,String x7_7,String x13_6,String x12_5,String x12_8,String x5_8
            ,String x3_7,String x2_5,String x4_1,String x6_9,String x5_1,String x2_25) {

        switch (SH) {
            case "14.7" -> tippas=x14_7;
            case "17.9" ->tippas=x17_9;
            case "8.5" ->tippas=x8_5;
            case "7.7" -> tippas=x7_7;
            case "13.6" ->tippas=x13_6;
            case "12.5" -> tippas=x12_5;
            case "12.8" -> tippas=x12_8;
            case "5.8" -> tippas=x5_8;
            case "3.7" -> tippas=x3_7;
            case "4.1" -> tippas=x4_1;
            case "2.5" -> tippas=x2_5;
            case "6.9" -> tippas=x6_9;
            case "5.1" -> tippas=x5_1;
            case "2.25" ->tippas=x2_25;
            default -> tippas = "Типовой паспорт не разработан";

        }
    }
    private void TP_SOPR (String SH,String x12_8_12_8,String x3_7_3_7,String x3_7_2_5,String x14_7_14_7,String x8_5_8_5,String x5_8_5_8,String x12_8012_8) {

        switch (SH) {

            case "12.8/12.8" -> tippas=x12_8_12_8;
            case "3.7/3.7" -> tippas=x3_7_3_7;
            case "3.7/2.5" -> tippas=x3_7_2_5;
            case "14.7/14.7" -> tippas=x14_7_14_7;
            case "8.5/8.5" -> tippas=x8_5_8_5;
            case "5.8/5.8" -> tippas=x5_8_5_8;
            case "12.8|12.8" -> tippas=x12_8012_8;

            default -> tippas = "Типовой паспорт не разработан";

        }
    }

    private void UU (String UGL,String x1,String x2,String x3,String x4) {

        switch (UGL) {
                    case "35-45" -> tippas = x1;
                    case "45-65" -> tippas = x2;
                    case "65-80" -> tippas = x3;
                    case "Вкрест" -> tippas = x4;
                    default -> tippas = "Типовой паспорт не разработан";

            }
    }


    private void getFAKT2 (String fakt) {
        String UGL= ugli.getValue();
        switch (fakt) {
            case "14.7" -> UU(UGL,"14","28","42","56");
            case "17.9" ->UU(UGL,"15","29","43","57");
            case "8.5" ->UU(UGL,"16","30","44","58");
            case "7.7" -> UU(UGL,"17","31","45","59");
            case "13.6" ->UU(UGL,"18","32","46","60");
            case "12.5" -> UU(UGL,"19","33","47","61");
            case "12.8" -> UU(UGL,"20","34","48","62");
            case "5.8" -> UU(UGL,"21","35","49","63");
            case "3.7" -> UU(UGL,"22","36","50","64");
            case "2.5" -> UU(UGL,"23","37","51","65");
            case "4.1" ->UU(UGL,"24","38","52","66");
            case "6.9" -> UU(UGL,"25","39","53","67");
            case "5.1" ->UU(UGL,"26","40","54","68");
            case "2.25" ->UU(UGL,"27","41","55","69");

            default -> tippas = "Типовой паспорт не разработан";
        }
    }



}
