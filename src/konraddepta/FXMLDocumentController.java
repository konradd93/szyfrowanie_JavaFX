/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konraddepta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 *
 * @author Dell
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextArea zawartoscPliku;

    @FXML
    public TextArea zaszyfrowane;

    @FXML
    public TextArea odszyfrowane;

    @FXML
    public TextField filePath;

    @FXML
    private TextField przesuniecie;

    @FXML
    private Button koduj;

    @FXML
    private Button dekoduj;

    @FXML
    private Button zapiszZaszyfrowany;

    @FXML
    private Button buttonRsa;

    @FXML
    private TextField keyBits;

    @FXML
    private Label footer;

    // File dir;
    private String oldKeyBits;

    public static FXMLDocumentController fxmlController;

    private String enc;
    private String dec;
    private int przesuniecieLiczba;
    private Cezar cezar = new Cezar();

    @FXML
    private void otworzPlik(ActionEvent event) throws IOException {
        // Tworzymy kontrolkę (okienko) służącą do wybierania pliku
        FileChooser fileChooser = new FileChooser();
        // Tytuł okienka
        fileChooser.setTitle("Otwórz Plik");
        // Dodajemy filtr rodzaju pliku - tu plików txt
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Pliki TXT", "*.txt")
        );

        Window stage = null;
        // Pokaż okno
        File plik = fileChooser.showOpenDialog(stage);

        // Jeśli zamkniemy fileChooser nie wybierając pliku zostanie zwrócony null
        // Jeśli wybierzemy plik, podejmujemy  odpowiednie działania
        if (plik != null) {
            // Wyswietlenie w terminalu ścieżki do pliku. 
            //System.out.println("Plik: "+ plik.getAbsolutePath());
            ladujPlik(plik);
            zapiszZaszyfrowany.setDisable(true);
            footer.setTextFill(Color.BLACK);
            footer.setText("Otwarto plik: " + plik.getAbsolutePath());
        } else {
            footer.setTextFill(Color.RED);
            footer.setText("Niewybrałeś pliku...");
        }

    }

    //wyswietla zawartość pliku txt w textArea.
    private void ladujPlik(File plik) throws FileNotFoundException, IOException {

//BufferedReader br = new BufferedReader(new FileReader( plik.getAbsolutePath() )); 
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(plik), "UTF-8");
        BufferedReader br = new BufferedReader(inputStreamReader);
        String linia;
        String tekst = "";
        while ((linia = br.readLine()) != null) {
            tekst = tekst + linia + "\n";
        }
        br.close();
        zawartoscPliku.setText(tekst);

    }

    @FXML
    private void modyfikacjaTekstu() {

        if (przesuniecie.getText().compareTo("") == 0) {

        } else {

            zapiszZaszyfrowany.setDisable(true);
            koduj.setDisable(false);
            dekoduj.setDisable(true);
        }
    }

    @FXML
    private void zapisz(ActionEvent event) throws IOException {
        // Tworzymy kontrolkę (okienko) służącą do wybierania pliku
        FileChooser saveFileChooser = new FileChooser();
        // Tytuł okienka
        saveFileChooser.setTitle("Zapisz Plik");
        // Dodajemy filtr rodzaju pliku - tu plików txt
        saveFileChooser.getExtensionFilters().add(
                new ExtensionFilter("Pliki TXT", "*.txt")
        );

        Window stage = null;
        // Pokaż okno
        File plik = saveFileChooser.showSaveDialog(stage);

        // Jeśli zamkniemy fileChooser nie wybierając pliku zostanie zwrócony null
        // Jeśli wybierzemy plik, podejmujemy  odpowiednie działania
        if (plik != null) {
            // Wyswietlenie w terminalu ścieżki do pliku. 
            System.out.println("Plik: " + plik.getAbsolutePath());
            FileWriter zapis = new FileWriter(plik);
            zapis.write(zawartoscPliku.getText());
            zapis.close();
            footer.setText("Zapisano: " + plik.getAbsolutePath());
        }

        //ladujPlik(plik);
    }

    @FXML
    private void wpiszPrzesuniecie(KeyEvent e) {
        wpiszTylkoLiczby(e);
    }

    @FXML
    private void wpiszIleBitow(KeyEvent e) {
        wpiszTylkoLiczby(e);

    }

    @FXML
    private void spr() {
        int bits = Integer.parseInt(keyBits.getText());
        if (bits > 4096) {
            buttonRsa.setDisable(false);
            footer.setTextFill(Color.RED);
            footer.setText("To trochę potrwa....");
            if (bits > 6000) {
                footer.setTextFill(Color.RED);
                footer.setText("Za długi klucz.");
                buttonRsa.setDisable(true);
            }
        } else if (bits < 512) {
            footer.setTextFill(Color.RED);
            footer.setText("Za krótkis klucz.");
            buttonRsa.setDisable(true);
        } else {
            buttonRsa.setDisable(false);
            footer.setTextFill(Color.BLACK);
            footer.setText("");
        }
    }

    private void wpiszTylkoLiczby(KeyEvent e) {
        char caracter = e.getCharacter().charAt(0);
        if (((caracter < '0') || (caracter > '9'))
                && (caracter != '\b')) {

            e.consume();
        } else {
            koduj.setDisable(false);
        }
    }

    @FXML
    private void czyscIfClickedPrzesuniecie() {
        przesuniecie.clear();
    }

    @FXML
    private void czyscIfClickedBits() throws IOException {
        if (!keyBits.getText().equals("")) {
            oldKeyBits = keyBits.getText();
        }
        keyBits.clear();

        // Alert alert = new Alert(AlertType.CONFIRMATION);
        //  alert.setTitle("Usuwanie..");
        //  alert.setHeaderText("Czy chcesz usunąć folder? ");
        //  alert.setContentText(filePath.getText());
        // Optional<ButtonType> result = alert.showAndWait();
        //  if (result.get() == ButtonType.OK) {
        File dir = new File(filePath.getText());
        deleteDir(dir);

        footer.setTextFill(Color.BLACK);
        footer.setText("Usunięto klucze z " + filePath.getText() + " (jeśli istniały)");
        // } else {
        // footer.setTextFill(Color.BLACK);
        // footer.setText("Długość klucza nie została zmieniona.");
        // keyBits.setText(oldKeyBits);
        // }

    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @FXML
    private void encryptCezar() {
        try {
            //gdy nie ma nic w treści szyfrowania
            if (zawartoscPliku.getText().equals("")) {
                footer.setTextFill(Color.RED);
                footer.setText("Co mam szyfrować??");
            } else if (przesuniecie.getText().equals("")) {
                footer.setTextFill(Color.RED);
                footer.setText("Podaj przesunięcie !");
            } //gdy przesunięcie jest zbyt duże
            else {
                String przesuniecieText = przesuniecie.getText();
                this.przesuniecieLiczba = Integer.parseInt(przesuniecieText);
                if (przesuniecieLiczba > 90) {
                    footer.setTextFill(Color.RED);
                    footer.setText("Przesunięcie musi być mniejsze niż 90!!");
                } else {

                    footer.setText("");

                    cezar.setTekst(zawartoscPliku.getText());
                    cezar.setIle(przesuniecieLiczba);
                    cezar.encrypt();
                    zawartoscPliku.setText(cezar.getEnc());
                    enc = cezar.getEnc();

                    koduj.setDisable(true);
                    dekoduj.setDisable(false);
                    zapiszZaszyfrowany.setDisable(false);
                    footer.setTextFill(Color.BLACK);
                    footer.setText("Zaszyfrowano z przesunięciem: " + cezar.getIle());
                }
            }
        } catch (RuntimeException e) {
            footer.setTextFill(Color.RED);
           footer.setText("Zły foramt pliku zmień na UTF-8 bez BOM-u lub wpisz ręcznie...");
        }
    }

    @FXML
    private void decryptCezar() {

        footer.setText("");
        cezar.setTekst(enc);
        cezar.setIle(przesuniecieLiczba);
        cezar.decrypt();
        zawartoscPliku.setText(cezar.getDec());
        dec = cezar.getDec();

        zapiszZaszyfrowany.setDisable(true);
        koduj.setDisable(false);
        dekoduj.setDisable(true);
        footer.setText("");

    }

    @FXML
    private void czekajj() {
        footer.setTextFill(Color.BLACK);
        footer.setText("Szyfrowanie...");
    }

    @FXML
    private void rsa() {
        if (keyBits.getText().equals("")) {
            keyBits.setText(oldKeyBits);
        }
        zaszyfrowane.setText("");
        odszyfrowane.setText("");

        EncryptionUtil rsa = new EncryptionUtil();
        rsa.setBits(Integer.parseInt(keyBits.getText()));
        rsa.setkeyFilePath(filePath.getText());
        rsa.rsaGenerate(zawartoscPliku.getText());
        footer.setTextFill(Color.BLACK);
        footer.setText("Klucze zapisano w " + rsa.getkeyFilePath());
        rsa = null;
        System.gc();
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //if(cezar.isEnc==false) dekoduj.setDisable(true);
        //  else dekoduj.setDisable(false);
        fxmlController = this;
        keyBits.setText("2048");
        // initialize your logic here: all @FXML variables will have been injected
        footer.setText("");
        String property = System.getProperty("os.name");
        if (property.charAt(0) == 'W') {
            filePath.setText("C:/keys");
        }
        if (property.charAt(0) == 'L' || property.charAt(0) == 'M') {
            filePath.setText("/home/keys");
        }
    }

}
