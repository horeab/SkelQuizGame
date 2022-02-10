package libgdx.xxutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import libgdx.constants.Language;

class TranslateTool {

    public static String translate(String langFrom, String langTo, String text) throws IOException {
        langTo = langTo.equals(Language.nb.toString()) ? "no" : langTo;
        langFrom = langFrom.equals(Language.nb.toString()) ? "no" : langFrom;
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbwoMNU7oFbfz4EP0T0hIJ74WjXsio7Vkc97MyPhoI_YjkzuE2PpPqPBs2cUWcqUN8-95g/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Chrome/51.0.2704." + new Random().nextInt(1000));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            return translate(langFrom, langTo, text);
        }
    }
}
