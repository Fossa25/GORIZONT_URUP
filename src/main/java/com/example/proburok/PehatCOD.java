package com.example.proburok;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PehatCOD extends GeologCOD {

    private String Photosxemtefat;
    private String Photosxemtefat2;
    private String Photovidkrepi;
    private String Photovidkrepi2;
    private String Photoilement;


    private String ankerPM;
    private String ankerR;
    private String setkaPM;
    private String setkaR;
    private String torkretPM;
    private String torkretR;

    private String ankerPM1;
    private String ankerR1;
    private String ankerPM2;
    private String ankerR2;
    private String ankerPM3;
    private String ankerR3;
    private String ankerPM4;
    private String ankerR4;

    List<String> soprigenii = Arrays.asList("12.8/12.8", "3.7/3.7", "3.7/2.5", "14.7/14.7", "8.5/8.5", "5.8/5.8", "12.8|12.8");
    List<String> soprigenii_KrepVibor = Arrays.asList("8", "31", "9", "32");
    List<String> obhvid2 = Arrays.asList("106", "107", "108", "109", "110",
            "111", "112", "113", "114", "129", "130", "131", "132", "133", "134", "135", "136", "137");
    List<String> sxemust2 = Arrays.asList("14", "15", "18", "19", "20",
            "28", "29", "32", "33", "34", "42", "43", "46", "47", "48", "56", "57", "60",
            "61", "62", "70", "71", "74", "75", "76", "92", "93", "96", "97", "98", "106","107",
            "110", "111", "112");
    List<String> sxemust2_dop = Arrays.asList( "129","130", "133", "134", "135");
    List<String> sxemust1_new = Arrays.asList( "129","130", "131","132","133", "134", "135","136", "137","138","139", "140", "141", "142");

    List<String> rasht_3temp = Arrays.asList( "82","83", "84", "85", "86","87", "88", "89", "90", "91");
    private static final Map<String, String> PLACEHOLDER_MAP;

    static {
        PLACEHOLDER_MAP = new HashMap<>();
        PLACEHOLDER_MAP.put("${nomer}", "nomer");
        PLACEHOLDER_MAP.put("${name}", "name");
        PLACEHOLDER_MAP.put("${gorizont}", "gorizont");
        PLACEHOLDER_MAP.put("${opisanie}", "opisanie");
        PLACEHOLDER_MAP.put("${kategorii}", "kategorii");
        PLACEHOLDER_MAP.put("${faktor}", "faktor");
        PLACEHOLDER_MAP.put("${plan}", "plan");
        PLACEHOLDER_MAP.put("${poper}", "poper");
        PLACEHOLDER_MAP.put("${prodol}", "prodol");
        PLACEHOLDER_MAP.put("${dlina}", "dlina");
        PLACEHOLDER_MAP.put("${tipovoi}","tipovoi");
        PLACEHOLDER_MAP.put("${sxematexfakt}", "sxematexfakt");
        PLACEHOLDER_MAP.put("${obvid}", "obvid");
        PLACEHOLDER_MAP.put("${konstrk}", "konstrk");
        PLACEHOLDER_MAP.put("${obvid2}", "obvid2");
        PLACEHOLDER_MAP.put("${sxematexfakt2}", "sxematexfakt2");
    }
    @FXML private ImageView instr;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private TextField cehen;
    @FXML private TextField bdname;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextField nomer1;
    @FXML private TextArea opisanie;
    @FXML private Button singUpButtun;
    @FXML private TextField texfak;
    @FXML private ComboBox<String> krepbox;
    @FXML  private Label krep;
    @FXML private TextField dlina;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML
    private TextArea primhanie;
    @FXML
    private CheckBox cb;
    @FXML private Button singUpButtun1;
    @FXML
    void initialize() {
        primhanie.visibleProperty().bind(cb.selectedProperty());
        singUpButtun1.visibleProperty().bind(cb.selectedProperty());

        singUpButtun1.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();

            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}

                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }

                new DatabaseHandler().DobavleniePRIM(primhanie.getText(), selectedGor, selectedName);
                clearFields();
                //gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }

        });
        setupComboBoxes();
        setupButtonAction();
        setupImageHandlers();
        singUpButtun.setVisible(false);
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });

    }

    private void setupComboBoxes() {
        ObservableList<String> horizons = FXCollections.observableArrayList(
                dbHandler.getAllBaza().stream()
                        .map(Baza::getGORIZONT)
                        .distinct()
                        .collect(Collectors.toList())
        );
        gorbox.setItems(horizons);

        gorbox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateNamesComboBox(newVal);
            }
        });

        namebox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && gorbox.getValue() != null) {
                populateFields(gorbox.getValue(), newVal);

            }
        });

        krepbox.getItems().addAll("усиленная комбинированная (УКК)","металлическая податливая (КМП)");
        krepbox.setValue("усиленная комбинированная (УКК)");
        krepbox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (soprigenii_KrepVibor.contains(nomer1.getText())) {
                if (newVal != null && !nomer1.getText().isEmpty()) {
                    Map<String, String> mapping = newVal.equals("металлическая податливая (КМП)")
                            ? Map.of("8","9", "31","32")
                            : Map.of("9","8", "32","31");

                    String newValue = mapping.getOrDefault(nomer1.getText(), nomer1.getText());
                    nomer1.setText(newValue);
                }
            }else if (newVal != null && !nomer1.getText().isEmpty()) {
                Map<String, String> mapping = newVal.equals("металлическая податливая (КМП)")
                        ? Map.of("70","82", "71","83", "72","84", "73","85", "74","86", "75","87","76","88","77","89","78","90","80","91")
                        : Map.of("82","70", "83","71", "84","72", "85","73", "86","74", "87","75","88","76","89","77","90","78","91","80");

                String newValue = mapping.getOrDefault(nomer1.getText(), nomer1.getText());
                nomer1.setText(newValue);
            }
        });

        addTextChangeListener(nomer);
        addTextChangeListener(bdname);
        addTextChangeListener(katigoria);
        addTextChangeListener(cehen);
        addTextChangeListener(nomer1);
        addTextChangeListener(texfak);
        addTextChangeListener(dlina);
    }
    private void addTextChangeListener(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            checkFieldsAndUpdateButton();
        });
    }

    private void checkFieldsAndUpdateButton() {
        boolean allValid = validateRequiredFields() &&
                gorbox.getValue() != null &&
                namebox.getValue() != null;

        singUpButtun.setDisable(!allValid);
    }
    private void updateNamesComboBox(String horizon) {
        ObservableList<String> names = FXCollections.observableArrayList(
                dbHandler.poiskName(horizon).stream()
                        .map(Baza::getNAME)
                        .distinct()
                        .collect(Collectors.toList())
        );
        clearFields();
        namebox.setItems(names);

    }

    private void updateUI(Baza data) {
        nomer.setText(data.getNOMER());
        //date.setText(new SimpleDateFormat("dd.MM.yyyy").format(data.getDATA()));
        bdname.setText(data.getNAME_BD());
        katigoria.setText(data.getKATEGORII());
        opisanie.setText(data.getOPISANIE());
        cehen.setText(data.getSEHEN());
        texfak.setText(data.getPRIVIZKA());
        nomer1.setText(data.getTIPPAS());
        dlina.setText(data.getDLINA());
        primhanie.setText(data.getPRIM());
        // Проверяем заполненность полей после обновления
        if (!validateRequiredFields()) {
            showAlert("Неполные данные", "Не все данные заполнены для выбранной записи!");
            singUpButtun.setVisible(false);
            checkFieldsAndUpdateButton();
        } else {
            singUpButtun.setVisible(true);
        }
    }
    private boolean validateRequiredFields() {
        return isFieldValid(nomer) &&
                isFieldValid(bdname) &&
                isFieldValid(katigoria) &&
                isFieldValid(cehen) &&
                isFieldValid(nomer1) &&
                isFieldValid(dlina) &&
                isFieldValid(texfak);
    }

    private boolean isFieldValid(TextField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }

    private void populateFields(String horizon, String name) {
        Baza data = dbHandler.danii(horizon, name);
        if (data != null) {
            krepbox.setValue("усиленная комбинированная (УКК)");
            updateUI(data);
            toggleImageViews(true);

            // Безопасная проверка значений с обработкой null
            String cehenValue = cehen.getText() != null ? cehen.getText() : "";
            String nomer1Value = nomer1.getText() != null ? nomer1.getText() : "";

            if (soprigenii.contains(cehenValue)) {
                if ("8".equals(nomer1Value) || "31".equals(nomer1Value)) {
                    krepbox.setVisible(true);
                    krep.setVisible(true);
                } else {
                    krepbox.setVisible(false);
                    krep.setVisible(false);
                }
            } else {
                if ("70".equals(nomer1Value) || "71".equals(nomer1Value) || "72".equals(nomer1Value)
                        || "73".equals(nomer1Value) || "74".equals(nomer1Value) || "75".equals(nomer1Value)
                        || "76".equals(nomer1Value) || "77".equals(nomer1Value) || "78".equals(nomer1Value)|| "80".equals(nomer1Value)) {
                    krepbox.setVisible(true);
                    krep.setVisible(true);
                } else {
                    krepbox.setVisible(false);
                    krep.setVisible(false);
                }
            }
        } else {
            clearFields();
            toggleImageViews(false);
            singUpButtun.setVisible(false);
        }
    }
    private void clearFields() {
        namebox.setValue(null);
        nomer.clear();
        bdname.clear();
        katigoria.clear();
        opisanie.setText("");
        cehen.clear();
        nomer1.clear();
        texfak.clear();
        dlina.clear();
        krepbox.setVisible(false);krep.setVisible(false);
        krepbox.setValue("усиленная комбинированная (УКК)");
        primhanie.clear();
        cb.setSelected(false);

      this.Photosxemtefat = "";
        this.Photosxemtefat2= "";
        this.Photovidkrepi= "";
        this.Photovidkrepi2= "";
        this.Photoilement= "";

        this.ankerPM= "";
        this.ankerR= "";
        this.setkaPM= "";
        this.setkaR= "";
        this.torkretPM= "";
        this.torkretR= "";

        this.ankerPM1= "";
        this.ankerR1= "";
        this.ankerPM2= "";
        this.ankerR2= "";
        this.ankerPM3= "";
        this.ankerR3= "";
        this.ankerPM4= "";
        this.ankerR4= "";
    }

    private void toggleImageViews(boolean visible) {
        PlanVKL.setVisible(visible);
        PlanVKLNe.setVisible(!visible);
        PoperVKL.setVisible(visible);
        PoperVKLNe.setVisible(!visible);
        ProdolVKL.setVisible(visible);
        ProdolVKLNe.setVisible(!visible);
    }

    private void setupButtonAction() {
        singUpButtun.setOnAction(event -> handleDocumentGeneration());

    }

    private void handleDocumentGeneration() {
        if (!validateInput() || !validateRequiredFields()) {
            showAlert("Ошибка", "Проверьте заполнение всех обязательных полей!");
            return;
        }
        String plan = Put + "/" + nomer.getText() + "/План";
        String poper = Put + "/" + nomer.getText() + "/Поперечный";
        String prodol = Put + "/" + nomer.getText() + "/Продольный";
        String sxema = Put + "/" + nomer.getText() + "/Схема";
        if (plan.contains("null") || poper.contains("null") ||prodol.contains("null")  ) {
            showAlert("Ошибка", "Некорректный номер документа!");
            return;
        }
        String vidkripi= nomer1.getText()+".jpg";


        if (soprigenii.contains(cehen.getText())) {
            this.Photovidkrepi=getRESURS(HABLON_PATH_SOPR, vidkripi);
            this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA_SOPR, vidkripi);
            this.Photoilement=getIlement_SOPR(nomer1.getText());
            if (nomer1.getText().equals("32")){
              this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA_SOPR, "32_1.jpg");
            }
        } else {
            this.Photovidkrepi=getRESURS(HABLON_PATH_VID, vidkripi);
            this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, vidkripi);
            this.Photoilement=getIlement(nomer1.getText());

            if (obhvid2.contains(nomer1.getText()))
            { switch (nomer1.getText()) {
                case "106" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "120.jpg"); break;
                case "107" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "121.jpg"); break;
                case "108" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "122.jpg"); break;
                case "109" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "123.jpg"); break;
                case "110" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "124.jpg"); break;
                case "111" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "125.jpg"); break;
                case "112" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "126.jpg"); break;
                case "113" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "127.jpg"); break;
                case "114" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "128.jpg"); break;
                case "129" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "143.jpg"); break;
                case "130" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "144.jpg"); break;
                case "131" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "145.jpg"); break;
                case "132" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "146.jpg"); break;
                case "133" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "147.jpg"); break;
                case "134" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "148.jpg"); break;
                case "135" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "149.jpg"); break;
                case "136" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "150.jpg"); break;
                case "137" : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, "151.jpg"); break;
                default : this.Photovidkrepi2=getRESURS(HABLON_PATH_VID, null); break;
            }}

            if (sxemust2.contains(nomer1.getText()))
            { String sxema2= nomer1.getText()+"_1.jpg";
                this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, sxema2);
            }
            if (sxemust2_dop.contains(nomer1.getText()))
            {   switch (nomer1.getText()) {
                case "129" : this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, "120_1.jpg"); break;
                case "130" : this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, "121_1.jpg"); break;
                case "133" : this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, "124_1.jpg"); break;
                case "134" : this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, "125_1.jpg"); break;
                case "135" : this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, "126_1.jpg"); break;
                default : this.Photosxemtefat2=getRESURS(HABLON_PATH_USTANOVKA, null); break;
            }}
            if (sxemust1_new.contains(nomer1.getText()))
            {   switch (nomer1.getText()) {
                case "129" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "120.jpg"); break;
                case "130" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "121.jpg"); break;
                case "131" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "122.jpg"); break;
                case "132" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "123.jpg"); break;
                case "133" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "124.jpg"); break;
                case "134" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "125.jpg"); break;
                case "135" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "126.jpg"); break;
                case "136" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "127.jpg"); break;
                case "137" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "128.jpg"); break;
                case "138" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "129.jpg"); break;
                case "139" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "130.jpg"); break;
                case "140" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "131.jpg"); break;
                case "141" : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, "132.jpg"); break;


                default : this.Photosxemtefat=getRESURS(HABLON_PATH_USTANOVKA, null); break;
            }}
        }
        try {
            if (validateInput()) {
                generateWordDocument(
                        nomer.getText(),
                       bdname.getText(),
                        gorbox.getValue(),
                        katigoria.getText(),
                        opisanie.getText(),
                        texfak.getText(),
                        getPoto(plan,0),
                        getPoto(poper,0),
                        getPoto(prodol,0),
                        dlina.getText(),
                        nomer1.getText(),
                        this.Photosxemtefat,
                        this.Photovidkrepi,
                        this.Photoilement,
                        this.Photovidkrepi2,
                        this.Photosxemtefat2
                );
            }
        } catch (IOException | InvalidFormatException e) {
            handleError(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean validateInput() {
        if (gorbox.getValue() == null || namebox.getValue() == null) {
            showAlert("Ошибка", "Выберите горизонт и название!");
            return false;
        }
        return true;
    }
    private void rashet(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }

        if (dlina.getText() == null || dlina.getText().trim().isEmpty()) {
            throw new ParseException("Значение длины не может быть пустым", 0);
        }

        switch (list) {
            case "70" -> {this.ankerPM = "13.75";this.setkaPM = "8.5";this.torkretPM = "0.6";}
            case "71" -> {this.ankerPM = "15.0";this.setkaPM = "8.2";this.torkretPM = "0.6";}
            case "72" -> {this.ankerPM = "11.25";this.setkaPM = "5.3";this.torkretPM = "0.3";}
            case "73"-> {this.ankerPM = "8.75";this.setkaPM = "5.3";this.torkretPM = "0.3";}
            case "74" -> {this.ankerPM = "13.75";this.setkaPM = "9.2";this.torkretPM = "0.5";}
            case "75", "76" -> {this.ankerPM = "12.50";this.setkaPM = "8.3";this.torkretPM = "0.5";}
            case "77"-> {this.ankerPM = "8.75";this.setkaPM = "5.5";this.torkretPM = "0.3";}
            case "78"-> {this.ankerPM = "7.50";this.setkaPM = "4.6";this.torkretPM = "0.2";}
            case "80"-> {this.ankerPM = "6.25";this.setkaPM = "3.7";this.torkretPM = "0.3";}
            case "81" -> {this.ankerPM = "10.00";this.setkaPM = "4.8";this.torkretPM = "0.3";}

            case "106","110", "111", "112", "129",  "133",
                 "134", "135" -> {this.ankerPM = "8.75";this.setkaPM = "5.5";this.torkretPM = "5.9";}

            case  "107","130" -> {this.ankerPM = "12.85";this.setkaPM = "6.4";this.torkretPM = "7.4";}

            case "82", "83", "84", "85", "86" -> {this.ankerPM = "10.10";this.setkaPM = "2.53";this.torkretPM = "0";
                this.ankerPM1 = "1.00";this.ankerPM2 = "2.00";this.ankerPM3 = "3.00";this.ankerPM4 = "4.00";}

            case "87", "88" -> {this.ankerPM = "9.60";this.setkaPM = "2.40";this.torkretPM = "0";
                this.ankerPM1 = "1.00";this.ankerPM2 = "2.00";this.ankerPM3 = "3.00";this.ankerPM4 = "4.00";}

            case "89" -> {this.ankerPM = "6.60";this.setkaPM = "1.65";this.torkretPM = "0";
                this.ankerPM1 = "1.00";this.ankerPM2 = "2.00";this.ankerPM3 = "3.00";this.ankerPM4 = "4.00";}

            case "90" -> {this.ankerPM = "5.3";this.setkaPM = "1.33";this.torkretPM = "0";
                this.ankerPM1 = "1.00";this.ankerPM2 = "2.00";this.ankerPM3 = "3.00";this.ankerPM4 = "4.00";}

            case "91" -> {this.ankerPM = "6.9";this.setkaPM = "1.73";this.torkretPM = "0";
                this.ankerPM1 = "1.00";this.ankerPM2 = "2.00";this.ankerPM3 = "3.00";this.ankerPM4 = "4.00";}

            case "13","27","41","55","69", "105",
                 "119", "142" ->{this.ankerPM = "0";this.setkaPM = "0";this.torkretPM = "0";}

            case  "1","2","5","6","7" -> {this.ankerPM = "6.66";this.setkaPM = "5.2";this.torkretPM = "0";}

            case  "14","18", "19","20","28","32","33","34",
                  "42","46","47","48", "56","60","61","62",
                  "92","96","97","98" -> {this.ankerPM = "8.75";this.setkaPM = "5.5";this.torkretPM = "0";}

            case  "15", "29", "43","57", "93"-> {this.ankerPM = "10.00";this.setkaPM = "6.4";this.torkretPM = "0";}

            case  "23","37","51","65","79","101", "115","138" -> {this.ankerPM = "4.28";this.setkaPM = "0";this.torkretPM = "0";}

            case "8" ->{this.ankerPM = "4.44";this.setkaPM = "3.2";this.torkretPM = "0";}

            case "10" ->{this.ankerPM = "4.44";this.setkaPM = "3.7";this.torkretPM = "0";}

            case "99", "100"->{this.ankerPM = "5.00";this.setkaPM = "3.7";this.torkretPM = "0";}

            case "9","22", "36","50", "64" ->{this.ankerPM = "5.71";this.setkaPM = "2.4";this.torkretPM = "0";}

            case "3", "4","16","17","24", "30","31","38", "44","45",
                 "52", "58","59", "66", "94","95","102" ->{this.ankerPM = "6.25";this.setkaPM = "3.7";this.torkretPM = "0";}

            case "21","35","49","63" ->{this.ankerPM = "7.14";this.setkaPM = "3.2";this.torkretPM = "0";}

            case "11","103" ->{this.ankerPM = "7.50";this.setkaPM = "4.6";this.torkretPM = "0";}

            case "25","39","53","67" ->{this.ankerPM = "10.00";this.setkaPM = "4.8";this.torkretPM = "0";}

            case "12","26","40","54","68",
                 "104","141" ->{this.ankerPM = "15.00";this.setkaPM = "10.30";this.torkretPM = "0";}

            case "118" ->{this.ankerPM = "22.85";this.setkaPM = "10.30";this.torkretPM = "0";}

            case  "108","131" -> {this.ankerPM = "8.57";this.setkaPM = "3.7";this.torkretPM = "4.5";}
            case  "109", "132" -> {this.ankerPM = "8.57";this.setkaPM = "3.7";this.torkretPM = "4.4";}

            case  "113" -> {this.ankerPM = "7.14";this.setkaPM = "3.2";this.torkretPM = "3.9";}
            case   "136" -> {this.ankerPM = "7.14";this.setkaPM = "3.2";this.torkretPM = "4.4";}

            case   "114" -> {this.ankerPM = "5.71";this.setkaPM = "2.4";this.torkretPM = "3.1";}
            case   "137" -> {this.ankerPM = "5.71";this.setkaPM = "2.4";this.torkretPM = "3.4";}

            case   "116","139"-> {this.ankerPM = "6.25";this.setkaPM = "3.7";this.torkretPM = "0";}

            case   "117","140" -> {this.ankerPM = "10.00";this.setkaPM = "4.8";this.torkretPM = "0";}
            default -> throw new IllegalStateException("Unexpected value: " + list);
        }

        // Обработка ввода с заменой запятых на точки
        String input = dlina.getText().trim().replace(',', '.');
        if (rasht_3temp.contains(nomer1.getText())) {
            try {
                double dlina_Dobl = Double.parseDouble(input);
                double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
                double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
                double ankerPM_Dobl1 = Double.parseDouble(this.ankerPM1);
                double ankerPM_Dobl2= Double.parseDouble(this.ankerPM2);
                double ankerPM_Dobl3 = Double.parseDouble(this.ankerPM3);
                double ankerPM_Dobl4 = Double.parseDouble(this.ankerPM4);

                double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
                double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
                double ankerR_Doble1 = dlina_Dobl * ankerPM_Dobl1;
                double ankerR_Doble2 = dlina_Dobl * ankerPM_Dobl2;
                double ankerR_Doble3 = dlina_Dobl * ankerPM_Dobl3;
                double ankerR_Doble4 = dlina_Dobl * ankerPM_Dobl4;

                this.ankerR = String.format(Locale.US,"%.2f", ankerR_Doble);
                this.setkaR = String.format(Locale.US,"%.2f", setkaR_Doble);
                this.ankerR1 = String.format(Locale.US,"%.2f", ankerR_Doble1);
                this.ankerR2 = String.format(Locale.US,"%.2f", ankerR_Doble2);
                this.ankerR3 = String.format(Locale.US,"%.2f", ankerR_Doble3);
                this.ankerR4 = String.format(Locale.US,"%.2f", ankerR_Doble4);

            } catch (NumberFormatException e) {
                throw new ParseException("Некорректный числовой формат", 0);
            }
        }else{
            try {
            double dlina_Dobl = Double.parseDouble(input);
            double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
            double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
            double torkretPM_Dobl = Double.parseDouble(this.torkretPM);

            double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
            double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
            double torkretR_Doble = dlina_Dobl * torkretPM_Dobl;

            this.ankerR = String.format(Locale.US,"%.2f", ankerR_Doble);
            this.setkaR = String.format(Locale.US,"%.2f", setkaR_Doble);
            this.torkretR = String.format(Locale.US,"%.2f", torkretR_Doble);

        } catch (NumberFormatException e) {
            throw new ParseException("Некорректный числовой формат", 0);
        }}
    }
    private void rashet_SOPR(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }
        switch (list) {
            case "3" -> {this.ankerR = "218";this.setkaR = "88";this.torkretR = "3.2";}
            case "8" -> {this.ankerR = "40";this.setkaR = "11";this.torkretR = "1.1";}
            case "20" -> {this.ankerR = "375";this.setkaR = "188";this.torkretR = "15.5";}
            case "26" -> {this.ankerR = "293";this.setkaR = "125";this.torkretR = "11.2";}
            case "31" -> {this.ankerR = "37";this.setkaR = "8.5";this.torkretR = "0.8";}
            case "38" -> {this.ankerR = "336";this.setkaR = "126";this.torkretR = "8.7";}
            case "4" -> {this.ankerR = "137";this.setkaR = "95.2";this.torkretR = "0";}
            case "2","5","6" -> {this.ankerR = "162";this.setkaR = "88";this.torkretR = "0";}
            case "1" -> {this.ankerR = "170";this.setkaR = "95.2";this.torkretR = "0";}
            case "36","39" -> {this.ankerR = "208";this.setkaR = "126";this.torkretR = "0";}
            case "37","40","41" -> {this.ankerR = "296";this.setkaR = "126";this.torkretR = "0";}
            case "18","21" -> {this.ankerR = "311";this.setkaR = "168";this.torkretR = "0";}
            case "19","22","23" -> {this.ankerR = "375";this.setkaR = "188";this.torkretR = "0";}
            case "7" -> {this.ankerR = "28";this.setkaR = "11";this.torkretR = "0";}
            case "10" -> {this.ankerR = "28";this.setkaR = "12";this.torkretR = "0";}
            case "11","12" -> {this.ankerR = "28";this.setkaR = "9";this.torkretR = "0";}
            case "33" -> {this.ankerR = "30";this.setkaR = "8.5";this.torkretR = "0";}
            case "13","15","16","17" -> {this.ankerR = "32";this.setkaR = "13";this.torkretR = "0";}
            case "30","34","35" -> {this.ankerR = "37";this.setkaR = "8.5";this.torkretR = "0";}
            case "24","27" -> {this.ankerR = "194";this.setkaR = "118";this.torkretR = "0";}
            case "25","28","29" -> {this.ankerR = "293";this.setkaR = "125";this.torkretR = "0";}

            case "9" -> {this.ankerR = "5";this.setkaR = "9";this.ankerR4 = "9";
                this.ankerR1 = "10";this.ankerR2 = "1.5";this.ankerR3 = "5";}
            case "14" -> {this.ankerR = "6";this.setkaR = "12";this.ankerR4 = "8";
                this.ankerR1 = "48";this.ankerR2 = "0.5";this.ankerR3 = "1.6";}
            case "32" -> {this.ankerR = "10";this.setkaR = "16";this.ankerR4= "15";
                this.ankerR1 = "16";this.ankerR2 = "2.5";this.ankerR3 = "5";}
        }
    }
    private void generateWordDocument(String... params) throws IOException, InvalidFormatException, ParseException {

        if (soprigenii.contains(cehen.getText())) {
            rashet_SOPR(nomer1.getText());
        }else{ rashet(nomer1.getText());}

            Map<String, String> tableData = new HashMap<>();
            tableData.put("${table.ankPM}", this.ankerPM);
            tableData.put( "${table.ankR}",this.ankerR);
            tableData.put( "${table.stkPM}" ,this.setkaPM);
            tableData.put( "${table.stkR}",this.setkaR);
            tableData.put("${table.torPM}",this.torkretPM);
            tableData.put("${table.torR}",this.torkretR);
            tableData.put( "${table.ankR1}",this.ankerR1);
            tableData.put("${table.ankPM2}", this.ankerPM2);
            tableData.put( "${table.ankR2}",this.ankerR2);
            tableData.put("${table.ankPM3}", this.ankerPM3);
            tableData.put( "${table.ankR3}",this.ankerR3);
            tableData.put("${table.ankPM4}", this.ankerPM4);
            tableData.put( "${table.ankR4}",this.ankerR4);



        String outputFileName = OUTPUT_PATH + nomer.getText() + "_" + gorbox.getValue() + ".docx";
        File outputFile = new File(outputFileName);
        try {
            TemplateResource templateResource;
        // Получаем поток входных данных для ресурса
        //InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_PATH);
            if (soprigenii.contains(cehen.getText())) {
                 templateResource = getDokHablon_SOPR(nomer1.getText());
            }else{  templateResource = getDokHablon(nomer1.getText());}

            InputStream inputStream = templateResource.getInputStream();
            String templatePath = templateResource.getTemplatePath();
            System.err.println("Берем шаблон: " + templatePath);
            if (inputStream == null) {
            throw new FileNotFoundException("Ресурс не найден: " + templatePath);
        }

        // Создаем временный файл
        Path tempFile = Files.createTempFile("hablon_", ".docx");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        try (FileInputStream template = new FileInputStream(tempFile.toFile());
             XWPFDocument document = new XWPFDocument(template)) {

            replacePlaceholders(document, params);

            replaceTablePlaceholders(document, tableData);

            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                document.write(out);
            }
            // Автоматическое открытие файла после сохранения
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (outputFile.exists()) {
                    desktop.open(outputFile);
                } else {
                    System.err.println("Файл не найден: " + outputFile.getAbsolutePath());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка генерации документа: " + e.getMessage());
        }
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
        String prim ="Паспорт создан";
        new DatabaseHandler().DobavleniePRIM(prim, gorbox.getValue(), namebox.getValue());

        clearFields();
    }
    private void replaceTablePlaceholders(XWPFDocument doc, Map<String, String> tableData) {
        // Перебираем все таблицы в документе
        for (XWPFTable table : doc.getTables()) {
            // Перебираем все строки в таблице
            for (XWPFTableRow row : table.getRows()) {
                // Перебираем все ячейки в строке
                for (XWPFTableCell cell : row.getTableCells()) {
                    // Перебираем все абзацы в ячейке
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        // Перебираем все плейсхолдеры
                        for (Map.Entry<String, String> entry : tableData.entrySet()) {
                            String placeholder = entry.getKey();
                            String value = entry.getValue();

                            // Если абзац содержит плейсхолдер - заменяем его
                            if (paragraph.getText().contains(placeholder)) {
                                replaceTextInParagraph(paragraph, placeholder, value);
                            }
                        }
                    }
                }
            }
        }
    }

    private void replacePlaceholders(XWPFDocument doc, String[] values) {
        // Списки плейсхолдеров для разных типов замен
        Set<String> textPlaceholders = Set.of(
                "${nomer}", "${name}", "${gorizont}",
                "${opisanie}", "${kategorii}", "${faktor}","${tipovoi}", "${dlina}"
        );

        Set<String> imagePlaceholders = Set.of(
                "${plan}", "${poper}", "${prodol}"
        );
        Set<String> hablonPlaceholders = Set.of(

                "${obvid2}","${obvid}","${konstrk}"
        );
        Set<String> delitPlaceholders = Set.of(

                "${sxematexfakt}",  "${sxematexfakt2}"
        );

        // Обработка текстовых плейсхолдеров
        textPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String value = getValueByFieldName(fieldName, values);
                    System.out.println("[ТЕКСТ] Замена " + placeholder + " → " + value);
                    replaceTextInParagraph(p, placeholder, value);
                }
            }
        });

        // Обработка плейсхолдеров изображений
        imagePlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    //System.out.println("[ИЗОБРАЖЕНИЕ] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p, doc, placeholder, imagePath);
                }
            }
        });
        // Обработка плейсхолдеров hablon
        hablonPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[ШАБЛОН] Замена " + placeholder + " → " + imagePath);
                    replaceHABLONInParagraph(p, doc, placeholder, imagePath);
                }
            }
        });
        delitPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[DELIT] Замена " + placeholder + " → " + imagePath);
                    //replaceTextInParagraph(p, placeholder, "");
                    replaceSXEMAInParagraph(p, doc, placeholder, imagePath);
                }
            }
        });
    }

    private String getValueByFieldName(String name, String[] values) {
        return switch (name) { //зависимости от ключа возвращает значение из массива который вводим
            case "nomer" -> values[0];
            case "name" -> values[1];
            case "gorizont" -> values[2];
            case "kategorii" -> values[3];
            case "opisanie" -> values[4];
            case "faktor" -> values[5];
            case "plan" -> values[6];
            case "poper" -> values[7];
            case "prodol" -> values[8];
            case "dlina" -> values[9];
            case "tipovoi" -> values[10];
            case "sxematexfakt" -> values[11];
            case "obvid" -> values[12];
            case "konstrk" -> values[13];
            case "obvid2" -> values[14];
            case "sxematexfakt2" -> values[15];
            default -> "";
        };
    }

    private void replaceTextInParagraph(XWPFParagraph p, String placeholder, String replacement) {
        String text = p.getText(); // получаем весь текст параметра
        p.getRuns().forEach(r -> r.setText("", 0)); //удаляем существующий из всех ронов и заменяем полностью
        XWPFRun newRun = p.createRun(); // создаем новый для нового текста
        newRun.setText(text.replace(placeholder, replacement)); // заменяем "${nomer}", "123",
    }
    // Пример корректной вставки изображения
    private void replaceImageInParagraph(XWPFParagraph p, XWPFDocument doc, String placeholder, String imagePath) {
        replaceTextInParagraph(p, placeholder, ""); // Удаляем плейсхолдер
        // Проверка существования файла
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            System.err.println("Файл не найден: " + imagePath);
            return;
        }
        try (FileInputStream is = new FileInputStream(imagePath)) {
            // Определяем тип изображения через PictureType
            PictureData.PictureType type = getImageType(imagePath);

            // Создаем Run и добавляем изображение
            XWPFRun run = p.createRun();
            int width = 470; // Ширина в пикселях
            int height = 340; // Высота в пикселях

            // Добавляем картинку с корректными параметрами
            run.addPicture(
                    is,
                    type.ordinal(),
                    imagePath,
                    Units.toEMU(width),
                    Units.toEMU(height)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void replaceHABLONInParagraph(XWPFParagraph p, XWPFDocument doc, String placeholder, String imagePath) {
        replaceTextInParagraph(p, placeholder, "");

        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Путь к изображению шаблона пуст");
            return;
        }
        // Нормализация пути для ресурсов
        String normalizedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
        try (InputStream is = getClass().getResourceAsStream(normalizedPath)) {
            if (is == null) {
                System.err.println("Ресурс не найден: " + normalizedPath);
                return;
            }

            // Читаем все байты изображения
            byte[] imageBytes = IOUtils.toByteArray(is);

            // Определяем тип изображения по содержимому
            PictureData.PictureType type = determineImageType(imageBytes);

            // Создаем Run и добавляем изображение
            XWPFRun run = p.createRun();
            run.addPicture(
                    new ByteArrayInputStream(imageBytes),
                    type.ordinal(),
                    "image", // Имя не важно
                    Units.toEMU(470),
                    Units.toEMU(320)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void replaceSXEMAInParagraph(XWPFParagraph p, XWPFDocument doc, String placeholder, String imagePath) {
        replaceTextInParagraph(p, placeholder, "");

        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Путь к изображению шаблона пуст");
            return;
        }
        // Нормализация пути для ресурсов
        String normalizedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
        try (InputStream is = getClass().getResourceAsStream(normalizedPath)) {
            if (is == null) {
                System.err.println("Ресурс не найден: " + normalizedPath);
                return;
            }

            // Читаем все байты изображения
            byte[] imageBytes = IOUtils.toByteArray(is);

            // Определяем тип изображения по содержимому
            PictureData.PictureType type = determineImageType(imageBytes);

            // Создаем Run и добавляем изображение
            XWPFRun run = p.createRun();
            run.addPicture(
                    new ByteArrayInputStream(imageBytes),
                    type.ordinal(),
                    "image", // Имя не важно
                    Units.toEMU(470),
                    Units.toEMU(560)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PictureData.PictureType determineImageType(byte[] imageData) {
        if (imageData.length >= 4 &&
                imageData[0] == (byte)0xFF && imageData[1] == (byte)0xD8) {
            return PictureData.PictureType.JPEG;
        } else if (imageData.length >= 8 &&
                new String(imageData, 0, 8).equals("\211PNG\r\n\032\n")) {
            return PictureData.PictureType.PNG;
        }
        return PictureData.PictureType.JPEG; // fallback
    }
    // Определение типа изображения
    private PictureData.PictureType getImageType(String imagePath) {
        String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> PictureData.PictureType.JPEG;
            case "png" -> PictureData.PictureType.PNG;
            case "gif" -> PictureData.PictureType.GIF;
            case "emf" -> PictureData.PictureType.EMF;
            case "wmf" -> PictureData.PictureType.WMF;
            default -> throw new IllegalArgumentException("Unsupported image type: " + ext);
        };
    }

        // Сформируйте полный путь к ресурсу
        public String getRESURS(String resourcePath, String fileName) {
            // Возвращаем путь в формате "/папка/файл"
            if (fileName == null) return "";
            return (resourcePath.startsWith("/") ? "" : "/") +
                    resourcePath.replace("\\", "/") +
                    "/" + fileName;
        }
public String getIlement (String list){

    return switch (list) {
        case "1","2","5","6","7", "14","15","18","19", "20","28","29",
             "32","33", "34", "42","43", "46","47","48", "56","57",
             "60", "61", "62", "70","71","74","75","76",
             "92","93","96","97","98" -> getRESURS(HABLON_PATH_ILIMENT, "5.jpg");


        case "3", "4","8", "9","10", "11", "16","17", "21","22", "24","25",
             "30", "31","35","36","38", "39", "44","45","49",
             "50","52","53","58", "59", "63","64","66","67",
             "72","73","77","78", "80","81", "94","95", "99",
             "100", "102", "103" -> getRESURS(HABLON_PATH_ILIMENT, "2.jpg");

        case "23","37","51","65","79", "101","115","138" -> getRESURS(HABLON_PATH_ILIMENT, "1.jpg");

        case  "82","83", "84","85","86","87","88","89","90","91"-> getRESURS(HABLON_PATH_ILIMENT, "3.jpg");

        case "12", "26", "40","54","68","104","118","141" -> getRESURS(HABLON_PATH_ILIMENT, "4.jpg");

        case "108","109","113", "114","116","117",
             "131", "132","136","137", "139", "140" ->getRESURS(HABLON_PATH_ILIMENT, "6.jpg");

        case  "106","107","110","111","112","129","130","133","134","135" ->getRESURS(HABLON_PATH_ILIMENT, "7.jpg");

        case  "13","27", "41","55","69","105", "119","142" ->"-";

        default -> "Unsupported image type: " ;
//               "120","121","122","123", "124","125","126", "127","128",
        };
    }
    public String getIlement_SOPR (String list){

        return switch (list) {
            case "7","8","10","11","12","13","15","16","17",
                 "24","25","26","27","28","29",
                 "30","31","33","34","35" -> getRESURS(HABLON_PATH_ILIMENT, "2.jpg");

            case "9","14","32" -> getRESURS(HABLON_PATH_ILIMENT, "3.jpg");

            case "1","2","3","4","5","6",
                 "18","19","20","21","22","23",
                 "36","37","38","39","40","41"  -> getRESURS(HABLON_PATH_ILIMENT, "5.jpg");

            default -> "Unsupported image type: " ;
        };
    }
    public TemplateResource getDokHablon (String list){
        return switch (list) {

            case "70","71","72","73","74","75","76","77","78",
                 "80","81" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1), TEMPLATE_PATH1);


            case "106","107","110","111","112","129",
                 "130","133","134","135" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH2), TEMPLATE_PATH2);

            case "82","83", "84","85","86","87","88","89",
                 "90", "91" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH3), TEMPLATE_PATH3);

            case "13","27","41","55","69", "105",
                 "119", "142" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH4), TEMPLATE_PATH4);

            case  "1","2","5","6","7","14","15","18", "19","20","28", "29",
                  "32","33","34","42","43","46","47","48", "56","57","60","61","62",
                  "92","93","96","97","98" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH5), TEMPLATE_PATH5);

            case  "23","37","51","65","79","101",
                  "115","138" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH6), TEMPLATE_PATH6);

            case  "3", "4","8", "9","10","11","12","16","17","21","22", "24", "25","26",
                  "30","31","35","36","38","39","40", "44","45", "49","50",
                  "52","53","54", "58","59", "63","64","66","67","68",
                  "94","95","99", "100","102", "103","104",
                  "118","141" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH7), TEMPLATE_PATH7);

            case  "108","109","113", "114","116","117","131","132","136","137",
                  "139","140" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH8), TEMPLATE_PATH8);


            default -> throw new IllegalStateException("Unexpected value: " + list);
        };
    }
    public TemplateResource getDokHablon_SOPR (String list){
        return switch (list) {

            case  "3","8","20","26", "31" ,"38" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1_SOPR), TEMPLATE_PATH1_SOPR);

            case "9","14","32" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH3_SOPR), TEMPLATE_PATH3_SOPR);

            case "1","2", "4","5","6",
                 "18","19", "21","22","23",
                 "36","37", "39","40","41"-> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH5_SOPR), TEMPLATE_PATH5_SOPR);

            case "7","10","11","12","13", "15","16","17",
                 "24", "25", "27","28", "29","30",
                 "33","34", "35"-> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH7_SOPR), TEMPLATE_PATH7_SOPR);

            default -> throw new IllegalStateException("Unexpected value: " + list);
        };
    }
    String getPoto(String imagePath,int i) {
        if (imagePath == null || imagePath.isEmpty()) return "";
        try {
            File folder = new File(imagePath);

        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            return "";
        }

        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });

        // Проверяем, есть ли изображения в папке
        if (files == null || files.length == 0) {
            System.err.println("Нет изображений в: " + imagePath);
            return "";
        }

        return files[i].getAbsolutePath();
        } catch (SecurityException e) {
            showAlert("Ошибка", "Нет доступа к папке: " + imagePath);
            return "";
        }
    }
    private void setupImageHandlers() {
        setupImageHandler(PlanVKL, "План");
        setupImageHandler(PoperVKL, "Поперечный");
        setupImageHandler(ProdolVKL, "Продольный");
    }

    private void setupImageHandler(ImageView imageView, String folder) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder);
        });
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void handleError(Exception e) {
        e.printStackTrace();
        showAlert("Ошибка", "Произошла ошибка: " + e.getMessage());
    }
}