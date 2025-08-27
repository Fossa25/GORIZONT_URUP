package com.example.proburok;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GeomexCOD extends GeologCOD {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private Button butnA;
    @FXML private Button butnB;
    @FXML private Button butnG;
    @FXML private Button butnN;
    @FXML private Button butnV;
    @FXML private TextField date;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextArea opisanie;
    @FXML private Button singUpButtun;
    @FXML private TextField texfak;
public String seri= "-fx-background-radius: 50px; -fx-background-color: #5e5d5d;";
public String zoloto = "-fx-background-color: aa9455";
    public String tippas;
    @FXML
    void initialize() {

        butnA.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if (butnA.getStyle().equals(seri)){
            butnA.setStyle(zoloto);

            texfak.setText("А"+stari);
            } else if (butnA.getStyle().equals(zoloto)) {
                butnA.setStyle(seri);
                String nowi = stari.replace("А","");
                texfak.setText(nowi);
            }
        });
        butnB.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if (butnB.getStyle().equals(seri)){
                butnB.setStyle(zoloto);
                switch (stari) {
                    case "" -> texfak.setText("Б");
                    case "А" -> texfak.setText(stari + "Б");
                    case "Г", "В","ВГ" -> texfak.setText("Б"+stari);
                    case "АВ" -> texfak.setText("АБВ");
                    case "АГ" -> texfak.setText("АБГ");
                    case "АВГ" -> texfak.setText("АБВГ");
                }
            } else if (butnB.getStyle().equals(zoloto)) {
                butnB.setStyle(seri);
                String nowi = stari.replace("Б","");
                texfak.setText(nowi);
            }
        });
        butnV.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if (butnV.getStyle().equals(seri)){
                butnV.setStyle(zoloto);
                switch (stari) {
                    case "" -> texfak.setText("В");
                    case "А", "Б","АБ" -> texfak.setText(stari + "В");
                    case "Г" -> texfak.setText("В"+stari );
                    case "АГ" -> texfak.setText("АВГ");
                    case "БГ" -> texfak.setText("БВГ");
                    case "АБГ" -> texfak.setText("АБВГ");
                }
            } else if (butnV.getStyle().equals(zoloto)) {
                butnV.setStyle(seri);
                String nowi = stari.replace("В","");
                texfak.setText(nowi);
            }
        });

        butnG.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if (butnG.getStyle().equals(seri)){
                butnG.setStyle(zoloto);
                switch (stari) {
                    case "" -> texfak.setText("Г");
                    case "А", "Б","В","АБ","АВ","БВ","АБВ" -> texfak.setText(stari + "Г");
                }
            } else if (butnG.getStyle().equals(zoloto)) {
                butnG.setStyle(seri);
                String nowi = stari.replace("Г","");
                texfak.setText(nowi);
            }
        });

        butnN.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if (butnN.getStyle().equals(seri)){
                butnN.setStyle(zoloto);
                butnA.setStyle(seri);butnB.setStyle(seri);butnV.setStyle(seri);butnG.setStyle(seri);
                butnA.setVisible(false);butnB.setVisible(false);butnV.setVisible(false);butnG.setVisible(false);
                texfak.setText("-");

            } else if (butnN.getStyle().equals(zoloto)) {
                butnN.setStyle(seri);
                butnA.setVisible(true);butnB.setVisible(true);butnV.setVisible(true);butnG.setVisible(true);
                texfak.setText("");
            }
        });



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
            }else {
                System.out.println("Error.");
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            // Получаем выбранные значения
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();

            // Получаем данные из полей
            String faktorValue = texfak.getText();

            if (selectedGor != null && selectedName != null) {
                getPas(katigoria.getText());
                new DatabaseHandler().DobavlenieGEOMEX(faktorValue, tippas,selectedGor, selectedName);

                System.out.println(tippas);
                ohistka();
                gorbox.setValue(null);
                texfak.setText("");
                butnA.setStyle(seri);butnB.setStyle(seri);butnV.setStyle(seri);butnG.setStyle(seri);butnN.setStyle(seri);
                butnA.setVisible(true);butnB.setVisible(true);butnV.setVisible(true);butnG.setVisible(true);butnN.setVisible(true);
            } else {
                System.out.println("Выберите горизонт и название!");
            }

        });
        PlanVKL.setOnMouseClicked(mouseEvent -> {
            String imagePath = Put+"/"+ nomer.getText() +"/План"; // Замените на ваш путь
            openImage(imagePath);
        });
        PoperVKL.setOnMouseClicked(mouseEvent -> {
            String imagePath = Put+"/"+ nomer.getText() +"/Поперечный"; // Замените на ваш путь
            openImage(imagePath);
        });
        ProdolVKL.setOnMouseClicked(mouseEvent -> {
            String imagePath = Put+"/"+ nomer.getText() +"/Продольный"; // Замените на ваш путь
            openImage(imagePath);
        });
    }
    private void poisk (String viborGOR, String viborName) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);

        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            date.setText(format.format(poluh.getDATA()));
            katigoria.setText(poluh.getKATEGORII());
            opisanie.setText(poluh.getOPISANIE());
            PlanVKL.setVisible(true);PlanVKLNe.setVisible(false);
            PoperVKL.setVisible(true);PoperVKLNe.setVisible(false);
            ProdolVKL.setVisible(true);ProdolVKLNe.setVisible(false);
        } else {

            System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
            nomer.clear();
            date.clear(); // Пользователь не найден
        }
    }
    private void ohistka(){

        namebox.setValue(null);
        nomer.setText(null);
        date.setText(null);
        katigoria.setText(null);
        opisanie.setText(null);

        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);
    }
    private void getPas (String kategor) {
        String kat = katigoria.getText();
        String faktor=texfak.getText();
        if (!kat.isEmpty()){

            switch (kategor) {
                case "I Категория" -> getFAKT1(faktor);
                case "II Категория" -> getFAKT2(faktor);
                case "III Категория" -> getFAKT3(faktor);
                case "IV Категория" -> getFAKT4(faktor);
                case "V Категория" -> getFAKT5(faktor);
                case "VI Категория" -> getFAKT6(faktor);
                case "VII Категория" -> getFAKT7(faktor);
                case "VIII Категория" -> getFAKT8(faktor);
                case "IX Категория" -> getFAKT9(faktor);
            }
    }}
    private void getFAKT1 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="1";
            case "Б" -> tippas="2";
            case "В" ->tippas="3";
            case "Г" -> tippas="4";
            case "АБ" ->tippas="5";
            case "АВ" -> tippas="6";
            case "АГ" -> tippas="7";
            case "БВ" -> tippas="8";
            case "БГ" -> tippas="9";
            case "ВГ" -> tippas="10";
            case "АБВ" -> tippas="11";
            case "АВГ" ->tippas="12";
            case "БВГ" -> tippas="13";
            case "АБВГ" ->tippas="14";
            case "-" ->tippas="15";
        }
    }
    private void getFAKT2 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="16";
            case "Б" -> tippas="17";
            case "В" ->tippas="18";
            case "Г" -> tippas="19";
            case "АБ" ->tippas="20";
            case "АВ" -> tippas="21";
            case "АГ" -> tippas="22";
            case "БВ" -> tippas="23";
            case "БГ" -> tippas="24";
            case "ВГ" -> tippas="25";
            case "АБВ" -> tippas="26";
            case "АВГ" ->tippas="27";
            case "БВГ" -> tippas="28";
            case "АБВГ" ->tippas="29";
            case "-" ->tippas="30";
        }

    }
    private void getFAKT3 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="31";
            case "Б" -> tippas="32";
            case "В" ->tippas="33";
            case "Г" -> tippas="34";
            case "АБ" ->tippas="35";
            case "АВ" -> tippas="36";
            case "АГ" -> tippas="37";
            case "БВ" -> tippas="38";
            case "БГ" -> tippas="39";
            case "ВГ" -> tippas="40";
            case "АБВ" -> tippas="41";
            case "АВГ" ->tippas="42";
            case "БВГ" -> tippas="43";
            case "АБВГ" ->tippas="44";
            case "-" ->tippas="45";
        }

    }
    private void getFAKT4 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="46";
            case "Б" -> tippas="47";
            case "В" ->tippas="48";
            case "Г" -> tippas="49";
            case "АБ" ->tippas="50";
            case "АВ" -> tippas="51";
            case "АГ" -> tippas="52";
            case "БВ" -> tippas="53";
            case "БГ" -> tippas="54";
            case "ВГ" -> tippas="55";
            case "АБВ" -> tippas="56";
            case "АВГ" ->tippas="57";
            case "БВГ" -> tippas="58";
            case "АБВГ" ->tippas="59";
            case "-" ->tippas="60";
        }
    }
    private void getFAKT5 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="61";
            case "Б" -> tippas="62";
            case "В" ->tippas="63";
            case "Г" -> tippas="64";
            case "АБ" ->tippas="65";
            case "АВ" -> tippas="66";
            case "АГ" -> tippas="67";
            case "БВ" -> tippas="68";
            case "БГ" -> tippas="69";
            case "ВГ" -> tippas="70";
            case "АБВ" -> tippas="71";
            case "АВГ" ->tippas="72";
            case "БВГ" -> tippas="73";
            case "АБВГ" ->tippas="74";
            case "-" ->tippas="75";
        }
    }
    private void getFAKT6 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="76";
            case "Б" -> tippas="77";
            case "В" ->tippas="78";
            case "Г" -> tippas="79";
            case "АБ" ->tippas="80";
            case "АВ" -> tippas="81";
            case "АГ" -> tippas="82";
            case "БВ" -> tippas="83";
            case "БГ" -> tippas="84";
            case "ВГ" -> tippas="85";
            case "АБВ" -> tippas="86";
            case "АВГ" ->tippas="87";
            case "БВГ" -> tippas="88";
            case "АБВГ" ->tippas="89";
            case "-" ->tippas="90";
        }
    }
    private void getFAKT7 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="91";
            case "Б" -> tippas="92";
            case "В" ->tippas="93";
            case "Г" -> tippas="94";
            case "АБ" ->tippas="95";
            case "АВ" -> tippas="96";
            case "АГ" -> tippas="97";
            case "БВ" -> tippas="98";
            case "БГ" -> tippas="99";
            case "ВГ" -> tippas="100";
            case "АБВ" -> tippas="101";
            case "АВГ" ->tippas="102";
            case "БВГ" -> tippas="103";
            case "АБВГ" ->tippas="104";
            case "-" ->tippas="105";
        }
    }
    private void getFAKT8 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="106";
            case "Б" -> tippas="107";
            case "В" ->tippas="108";
            case "Г" -> tippas="109";
            case "АБ" ->tippas="110";
            case "АВ" -> tippas="111";
            case "АГ" -> tippas="112";
            case "БВ" -> tippas="113";
            case "БГ" -> tippas="114";
            case "ВГ" -> tippas="115";
            case "АБВ" -> tippas="116";
            case "АВГ" ->tippas="117";
            case "БВГ" -> tippas="118";
            case "АБВГ" ->tippas="119";
            case "-" ->tippas="120";
        }
    }
    private void getFAKT9 (String fakt) {
        switch (fakt) {
            case "А" -> tippas="121";
            case "Б" -> tippas="122";
            case "В" ->tippas="123";
            case "Г" -> tippas="124";
            case "АБ" ->tippas="125";
            case "АВ" -> tippas="126";
            case "АГ" -> tippas="127";
            case "БВ" -> tippas="128";
            case "БГ" -> tippas="129";
            case "ВГ" -> tippas="130";
            case "АБВ" -> tippas="131";
            case "АВГ" ->tippas="132";
            case "БВГ" -> tippas="133";
            case "АБВГ" ->tippas="134";
            case "-" ->tippas="135";
        }
    }
}
