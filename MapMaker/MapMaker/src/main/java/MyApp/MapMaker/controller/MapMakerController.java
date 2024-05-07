package MyApp.MapMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class MapMakerController {
    @PostMapping("/upload-image")
    public ModelAndView getData(@RequestParam("image")MultipartFile image,
                                @RequestParam("width") int width,
                                @RequestParam("height")int height,
                                @RequestParam("map-width") int mapWidth,
                                @RequestParam("map-height")int mapHeight) throws IOException {
        BufferedImage img = null;
        List<String> imagesBase64 = new ArrayList<>();
        try{
            InputStream inputStream = image.getInputStream();
            img = ImageIO.read(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        int numRow = img.getHeight() / height;
        int numCol = img.getWidth() / width;
        BufferedImage[][] images = new BufferedImage[numRow][numCol];
        for (int i=0; i<numRow; i++){
            for (int j=0; j<numCol; j++){
                images[i][j] = img.getSubimage(j * width, i * height, width, height);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(images[i][j], "png", baos);
                byte[] bytes = baos.toByteArray();
                String imageBase64 = Base64.getEncoder().encodeToString(bytes);
                imagesBase64.add(imageBase64);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("images", imagesBase64);
        modelAndView.addObject("mapWidth", mapWidth);
        modelAndView.addObject("mapHeight", mapHeight);
        modelAndView.setViewName("edit");
        return modelAndView;
    }

}
