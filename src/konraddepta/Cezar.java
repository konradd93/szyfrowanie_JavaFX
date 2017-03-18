package konraddepta;

import java.util.Scanner;

public class Cezar {

    private String tekst;
    private int ile;
    private String enc;
    private String dec;
    public boolean isEnc;
    private Boolean[] left;
    private Boolean[] right;
    private Boolean[] isPolish;

    Cezar() {
        tekst = "";
        ile = 0;
        isEnc = false;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public void setIle(int ile) {
        this.ile = ile;
    }

    public String view() {
        return tekst;
    }

    public String getEnc() {
        return enc;
    }
    
    public int getIle() {
        return ile;
    }

    public String getDec() {
        return dec;
    }

    void encrypt() {
        int polskie[] = {347, 263, 378, 380, 281, 261, 243, 324, 346, 262, 377, 379, 280, 260, 211, 323};

        char x[] = tekst.toCharArray();
        left = new Boolean[x.length];
        right = new Boolean[x.length];
        isPolish = new Boolean[x.length];
        for (int i = 0; i != x.length; i++) {
            int n = x[i];

            if (n < 126) {
                isPolish[i] = false;
                n += ile;
                if (n >= 126) {
                    n -= 126;
                    right[i] = true;
                    left[i] = false;
                    if (n <= 32) {
                        n += 32;
                    }
                    left[i] = true;
                } else {

                    left[i] = false;
                    right[i] = false;
                }
            } else {
                for (int pol : polskie) {
                    if (n == pol) {
                        isPolish[i] = true;
                    }
                }
                if (isPolish[i] == true) {
                    n = n - 200;
                    n += ile;
                    right[i] = false;
                    left[i] = false;
                    if (n > 126) {
                        n -= 126;
                        right[i] = true;
                        left[i] = false;
                    }
                    if (n < 32) {
                        n += 32;
                        left[i] = true;
                    }

                }
            }

            x[i] = (char) n;

        }
        enc = new String(x);
        isEnc = true;
    }

    void decrypt() {
        if (isEnc == true) {
            char x[] = enc.toCharArray();
            for (int i = 0; i != x.length; i++) {
                int n = x[i];
                if (isPolish[i] == false) {
                    if (right[i] == true) {
                        n += 126;
                    }
                    if (left[i] == true) {
                        n -= 32;
                    }
                    n -= ile;
                } else {
                    if (right[i] == true) {
                        n += 126;
                    }
                    if (left[i] == true) {
                        n -= 32;
                    }
                    n = n + 200;
                    n -= ile;

                }

                x[i] = (char) n;
            }
            dec = new String(x);

        }

    }
}
