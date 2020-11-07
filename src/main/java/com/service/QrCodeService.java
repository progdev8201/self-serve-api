package com.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

@Validated
@Service
public class QrCodeService {
    private final String DIR = "src/main/resources/qrfolder/";
    private final String ext = ".png";
    private final String LOGO = "src/main/resources/img/waiter3.png";
    private final String CONTENT = "http://i-serve.ca/start?restaurantTableId=";
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final float DELTA_PERCENTAGE_LIMIT = 0.7f;

    //todo delete this
    public static void main(String[] args) throws IOException, WriterException {
        QrCodeService code = new QrCodeService();
        code.downloadQrCode(4);
    }

    //todo implement this method
    public ResponseEntity<List<Resource>> downloadAllQrCode(List<Integer> tableIds){
        return null;
    }

    public ResponseEntity<Resource> downloadQrCode(@NotNull int tableId) {
        // Create new configuration that specifies the error correction
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {

            // Create a qr code with the url as content and a size of WxH px
            bitMatrix = writer.encode(CONTENT + tableId, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);

            // Load QR image
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Load logo image
            BufferedImage overly = ImageIO.read(new File(LOGO));

            System.out.println("my qr width: " + qrImage.getWidth() + "\nand height: " + qrImage.getHeight());

            System.out.println("my overly width: " + overly.getWidth() + "\nand height: " + overly.getHeight());

            // Calculate the delta height and width between QR code and logo
            int deltaHeight = qrImage.getHeight() - overly.getHeight();
            int deltaWidth = qrImage.getWidth() - overly.getWidth();

            System.out.println("my delta height: " + deltaHeight + "\nmy delta width: " + deltaWidth);

            System.out.println("my height %: " + qrImage.getHeight() * DELTA_PERCENTAGE_LIMIT + "\nmy width %: " + qrImage.getWidth() * DELTA_PERCENTAGE_LIMIT);

            if (deltaHeight < qrImage.getHeight() * DELTA_PERCENTAGE_LIMIT || deltaWidth < qrImage.getWidth() * DELTA_PERCENTAGE_LIMIT)
                throw new Exception("Logo too big Exception");

            // Initialize combined image
            BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();

            // Write QR code to new image at position 0/0
            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // Write logo into combine image at position (deltaWidth / 2) and
            // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
            // the same space for the logo to be centered
            g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

            // Write combined image as PNG to OutputStream
            ImageIO.write(combined, "png", os);
            // Store Image
//             Files.copy(new ByteArrayInputStream(os.toByteArray()), Paths.get(DIR + "fileqrcode" + ext), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("multipart/form-data"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "tableId" + tableId + ".png" + "\"")
                .body(new ByteArrayResource(os.toByteArray()));
    }
}
