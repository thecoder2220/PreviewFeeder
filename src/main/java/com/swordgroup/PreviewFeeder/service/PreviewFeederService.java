package com.swordgroup.PreviewFeeder.service;

import com.swordgroup.PreviewFeeder.common.Constant;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public final class PreviewFeederService {

    @Autowired
    ExtractURLService extractURLService;


    public static void main(final String[] args) throws IOException {
        ClassPathResource resource = new ClassPathResource(Constant.FILE_TO_FEED);
        /* String objectID = "https://www.vinci.com/commun/communiques.nsf/DCC7F89D1A4DE415C125692F002D5AE4/$file/opegtmus.pdf";  */
        String objectID ="https://www.vinci.com/vinci/actualites.nsf/4D7F804C5BDAB3BBC1257E12003DB5B9/$File/dp_Dufour_2-03-2015.pdf";
        URL url = new URL(objectID);
        String specificDirectoryName = objectID.replaceFirst("https://","").replaceAll("[^A-Za-z0-9]","-");

        new File("output/img/" +specificDirectoryName).mkdirs();

        /* URL url = new URL("http://infolab.stanford.edu/pub/papers/google.pdf");
        URL url = new URL("https://www.vinci.com/vinci/transactions.nsf/(unid)/1653B12B00829395C125833500401652/$file/vinci_2018_43.pdf");*/

        String[] tokens = objectID.split("/");
        String pdfFilename = tokens[tokens.length-1];
        InputStream in = url.openStream();

         /* try (FileInputStream stream = new FileInputStream(resource.getFile())) {
            extractURLService
        } */

                    /* Découpage en image  */
        PDDocument document = PDDocument.load(in);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int numberOfPages = document.getNumberOfPages() < 5 ? document.getNumberOfPages() : 5;
        for (int page = 0; page < numberOfPages; ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // suffix in pdfFilename will be used as the file format
            ImageIOUtil.writeImage(bim, "output/img/" + specificDirectoryName + "/"+ pdfFilename + "-" + (page + 1) + ".png", 300);
            System.out.println("output/img/" + specificDirectoryName + "/"+ pdfFilename + "-" + (page + 1) + ".png created");
        }
        in.close();

    }
}
