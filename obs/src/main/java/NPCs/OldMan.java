package NPCs;

import Dialogue.Dialogue;
import utilz.LoadSave;
import org.springframework.stereotype.Component;
import utilz.Constant;

import java.awt.image.BufferedImage;

@Component
public class OldMan extends NPC{
    public OldMan(int xPos, int yPos){
        super(xPos,yPos);
        initClass();
    }

    private void initClass() {
        dialogue = new Dialogue();
        BufferedImage tmp = LoadSave.getImg(LoadSave.OLD_MAN);
        animation = new BufferedImage[18];
        for (int i=0; i<animation.length; i++){
            animation[i] = tmp.getSubimage(i * Constant.NPC.DEFAULT_WIDTH, 0, Constant.NPC.DEFAULT_WIDTH, Constant.NPC.DEFAULT_HEIGHT);
        }
        conversations[0] = "Finally here you are, Ezran!";
        conversations[1] = "It's been long time since...";
        conversations[2] = "It's your era!";
        conversations[3] = "Go and find out the truth, my son";
        conversations[4] = "God bless you";
        dialogue.setText(conversations);
    }
}
