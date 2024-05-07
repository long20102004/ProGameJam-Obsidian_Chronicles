package obs.chro.controller;

import Main.Game;
import OnlineData.ImageSender;
import Player.Player;
import State.Playing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class MyController {
    @GetMapping
    public String hello() throws IOException {
        return "hello";
    }

    @PostMapping("/data")
    public String receiveData(@RequestBody String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data", false));
            writer.write("true");
            writer.newLine();
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "hello";
    }
}