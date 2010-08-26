package com.lulu.coverGeneration.examples;

import com.lulu.pdfGenerator.beans.*;
import com.lulu.pdfGenerator.beans.coverSize.CoverSizeData;
import com.lulu.pdfGenerator.exceptions.JsonMapperException;
import com.lulu.pdfGenerator.json.JsonMapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author mbharadwaj
 *         Created: Jun 30, 2010
 */
public class PdfGeneration {
    public static final String longText = "Lulu (Lulu Enterprises, Inc. and Lulu Press, Inc. are collectively called \"Lulu\") is a company offering diverse publishing and printing services with headquarters at Raleigh, North Carolina, United States. The company is international with staff in 12 countries, and offices in Raleigh, London, Toronto and Bangalore.[1] In addition to printing and publishing services it also offers online order fulfillment. The brand name is derived from the concept of a lulu as an old-fashioned term for a remarkable person, object, or idea. [2] The company's CEO is Red Hat co-founder Bob Young. " +
            "Authors who publish/print materials and similar works through Lulu retain the copyrights to such materials and similar works. " +
            "Optional services offered by the company include ISBN assignment, and distribution of books to retailers requesting specific titles (returns are not accepted, which limits distribution to physical bookstores). Electronic distribution is also available. " +
            "Lulu Enterprises was founded in early 2002. OpenMind Publishing, founded by Bradley Schultz and Paul Elliot, merged its publishing company and staff with Lulu in the latter part of 2002. OpenMind Publishing was a publisher of customized texts for college professors. ";

    //private static final String commonImage = "http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg";
    private static final String KEY = "k";
    public static void main(String args[]) throws Exception {
        CoverGeneratorConfig coverGeneratorConfig = buildCoverGeneratorConfigExample();
        generateCover(coverGeneratorConfig);
    }

    private static void generateCover(CoverGeneratorConfig coverGeneratorConfig) throws JsonMapperException, IOException {
        String data = JsonMapper.toJson(coverGeneratorConfig, true);
        System.out.println(data);
        final String host = "http://apps.lulu.com/api/pdfgen";
        final String urlString = "/covers/v1/generate/templateCreatorId/lulu/templateId/6x9dustjacket";
        URL url = new URL(host + urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("CONTENT-TYPE", "application/json");

        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        //urlConnection.setDoInput(true);
        OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
        String urlParameters = "&api_key=" + URLEncoder.encode(KEY, "UTF-8") +
                "&data=" + URLEncoder.encode(data, "UTF-8");

        wr.write(urlParameters);
        wr.flush();
        //wr.close();
        String output = Util.inputStreamToString(urlConnection.getInputStream());
        urlConnection.disconnect();
        System.out.println(output);
        CoverGeneratorResponse response = JsonMapper.fromCoverGeneratorResponse(output);
        System.out.println(JsonMapper.toJson(response, true));
    }

    private static CoverGeneratorConfig buildCoverGeneratorConfigExample() {
        CoverGeneratorConfig coverGeneratorConfig = new CoverGeneratorConfig();
        LayoutDefinition layoutDefinition = new LayoutDefinition();
        CoverSizeData coverSizeData = new CoverSizeData();
        coverSizeData.setSpineWidth(new Width(5.19));
        layoutDefinition.setCoverSizeData(coverSizeData);
        coverGeneratorConfig.setLayoutDefinition(layoutDefinition);
        LayoutBlocks layoutBlocks = new LayoutBlocks();

        TextBlock text = TextBlock.frontCoverHeadline();
        //text.setBackgroundColor(new COLOR_CODE("#A80033"));
        text.setBackgroundColor(null);
        text.setText("A story of intrigue, mystery and misbehavior");
        layoutBlocks.setFrontCoverHeadline(text);

        text = TextBlock.title();
        text.setBackgroundColor(new ColorCode("#229999"));
        text.setTextColor(ColorCode.BLACK);
        text.setText("Revenge of the Albatross<br/> and more stories");
        layoutBlocks.setTitle(text);

        text = TextBlock.author();
        text.setBackgroundColor(new ColorCode("#229999"));
        text.setTextColor(ColorCode.BLACK);
        text.setText("Carl Mabry");
        layoutBlocks.setAuthor(text);

        text = TextBlock.spineTitle();
        text.setBackgroundColor(ColorCode.LIGHT_GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText("Revenge of the Albatross and more stories");
        layoutBlocks.setSpineTitle(text);

        text = TextBlock.spineAuthor();
        text.setBackgroundColor(ColorCode.LIGHT_GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText("Carl Mabry");
        layoutBlocks.setSpineAuthor(text);

        BarcodeBlock barcode = BarcodeBlock.ean();
        String ean = "9783468111242";
        barcode.setCode(ean);
        barcode.setSupplemental("54499");
        layoutBlocks.setBackCoverBarCode(barcode);

        text = TextBlock.frontFlapText();
        text.setBackgroundColor(ColorCode.GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText(longText);
        layoutBlocks.setFrontFlapText(text);

        text = TextBlock.backFlapText();
        text.setBackgroundColor(ColorCode.LIGHT_GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText(longText);
        layoutBlocks.setBackFlapText(text);

        text = TextBlock.authorBio();
        text.setBackgroundColor(ColorCode.GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText("Jackson Rogers is is an internationally renowned author, reporter, and columnist - the recipient of two Pulitzer Prizes and the author of five bestselling books, among them Web Diagnostics and Social Media - From Applications to Zen Programming.<p/>He teaches at Harvard University.");
        layoutBlocks.setAuthorBio(text);

        text = TextBlock.backCoverSynopsis();
        text.setBackgroundColor(ColorCode.LIGHT_GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText("Making Money with iPad Applications belongs in the toolbox of every person interested in the design and development of  Web-based software, from the CEO to the programming team. Making Money with iPad Applications explores the building blocks of great Web applications and uses them as basic principles of design so that every project produces inspiring workflows with successful results.  Principles include creating  only whats necessary, getting users up to speed quickly, and preventing and handling errors. It offers practical advice about how to achieve the qualities of great Web-based applications and consistently and successfully reproduce them.");
        layoutBlocks.setBackCoverSynopsis(text);

        text = TextBlock.reviewBlurbs();
        text.setBackgroundColor(ColorCode.GRAY);
        text.setTextColor(ColorCode.BLACK);
        text.setText("\"The perfect way to create extra income in these uncertain economic times\"<br/>" +
                " - New York Times<p/>" +
                "\"Clear and concise, this guide is ideal for both beginners and those experts who know the code but need guidance with on and offline marketing .\"<br/>" +
                " - Wired Magazine<p/>" +
                "\"There is a very obvious reason why Jackson Rogers is one of America's most celebrated Web authors: he is very good.\"<br/>" +
                "- CNET");
        layoutBlocks.setReviewBlurbs(text);

        ImageBlock image = ImageBlock.publisherLogoFront();
        image.setImageFile(new ImageFile("http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"));
        layoutBlocks.setPublisherLogoFront(image);

        image = ImageBlock.publisherLogoBack();
        image.setImageFile(new ImageFile("http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"));
        layoutBlocks.setPublisherLogoBack(image);

        image = ImageBlock.spineLogo();
        image.setImageFile(new ImageFile("http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"));
        layoutBlocks.setSpineLogo(image);

        image = ImageBlock.backFlapImage();
        image.setImageFile(new ImageFile("http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"));
        layoutBlocks.setBackFlapImage(image);

        image = ImageBlock.frontFlapImage();
        image.setImageFile(new ImageFile("http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"));
        layoutBlocks.setFrontFlapImage(image);

        image = ImageBlock.authorPhoto();
        image.setImageFile(new ImageFile("http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"));
        layoutBlocks.setAuthorPhoto(image);

        coverGeneratorConfig.setLayoutBlocks(layoutBlocks);

        Backgrounds backgrounds = new Backgrounds();
        final ColorCode CC_CCB399 = new ColorCode("#CCB399");
        final ColorCode CC_996600 = new ColorCode("#996600");
        Background frontCover = Background.frontCoverBackground();
        frontCover.setColorCode(CC_996600);
        backgrounds.setFrontCover(frontCover);
        Background backCover = Background.backCoverBackground();
        backCover.setColorCode(CC_996600);
        backgrounds.setBackCover(backCover);

        //Background spine = Background.spineBackground();
        //spine.setColorCode(COLOR_CODE.WHITE);
        //backgrounds.setSpine(spine);
        Background backFlap = Background.backFlapBackground();
        backFlap.setColorCode(CC_CCB399);
        backgrounds.setBackFlap(backFlap);
        Background frontFlap = Background.frontFlapBackground();
        frontFlap.setColorCode(CC_CCB399);
        backgrounds.setFrontFlap(frontFlap);

        coverGeneratorConfig.setBackgrounds(backgrounds);
        return coverGeneratorConfig;
    }

}
