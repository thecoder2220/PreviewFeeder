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


        URL url = new URL("http://infolab.stanford.edu/pub/papers/google.pdf");
        InputStream in = url.openStream();


         /* try (FileInputStream stream = new FileInputStream(resource.getFile())) {
            extractURLService
        } */

                    /* Découpage en image  */
        PDDocument document = PDDocument.load(in);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int numberOfPages = document.getNumberOfPages() < 5 ? document.getNumberOfPages() : 5;
        String  pdfFilename ="test.pdf";

        for (int page = 0; page < numberOfPages; ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // suffix in pdfFilename will be used as the file format
            ImageIOUtil.writeImage(bim, "output/png/" + pdfFilename + "-" + (page + 1) + ".png", 300);
            System.out.println("output/png/" + pdfFilename + "-" + (page + 1) + ".png created");
        }
        in.close();

    }
}
