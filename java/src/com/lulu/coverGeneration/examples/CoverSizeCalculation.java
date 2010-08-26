package com.lulu.coverGeneration.examples;

import com.lulu.pdfGenerator.beans.coverSize.*;
import com.lulu.pdfGenerator.json.JsonMapper;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author mbharadwaj
 */
public class CoverSizeCalculation {
    public static void main(String[] args) throws Exception {
        CoverSizeConfig coverSizeConfig = new CoverSizeConfig(32);
        coverSizeConfig.setBindingType(BindingType.JACKET_HARDCOVER.toString());
        coverSizeConfig.setPaperType(PaperType.REGULAR.toString());
        coverSizeConfig.setTrimSize(TrimSize.A5.toString());
        String data = JsonMapper.toJson(coverSizeConfig, true);
        System.out.println(data);
        URL url = new URL("http://apps.lulu.com/api/pdfgen/covers/v1/calculateSize");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("CONTENT-TYPE", "application/json");

        urlConnection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
        String urlParameters = "&api_key=" + URLEncoder.encode("k", "UTF-8") +
                "&data=" + URLEncoder.encode(data, "UTF-8");

        wr.write(urlParameters);

        //wr.write(data);
        wr.flush();
        String coverSizeResults = Util.inputStreamToString(urlConnection.getInputStream());
        urlConnection.disconnect();

        //output is JSON
        System.out.println(coverSizeResults);
        //can be mapped to CoverSizeData object
        CoverSizeResponse coverSizeResponse = JsonMapper.fromCoverSizeResponseJson(coverSizeResults);
        System.out.println(JsonMapper.toJson(coverSizeResponse, true));
    }
}