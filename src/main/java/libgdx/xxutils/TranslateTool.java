package libgdx.xxutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import libgdx.constants.Language;

class TranslateTool {

    public static String translate(String langFrom, String langTo, String text) throws IOException {
        langTo = langTo.equals(Language.nb.toString()) ? "no" : langTo;
        langFrom = langFrom.equals(Language.nb.toString()) ? "no" : langFrom;
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbwrk82-rQsHXm7QRZNLyvxI7U0Ldg8ONXwqvzbwzW27Kn373wqjNjpbcbJOBEPATK1d_w/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
