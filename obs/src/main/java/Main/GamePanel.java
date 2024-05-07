package Main;

import Inputs.KeyInputs;
import Inputs.MouseInputs;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
@Component
public class GamePanel extends JComponent {
    private final Game game;
    public GamePanel(Game game){
        this.game = game;
        MouseInputs mouseInputs = new MouseInputs(this);
        addMouseMotionListener(mouseInputs);
        addMouseListener(mouseInputs);
        addKeyListener(new KeyInputs(this));
        setFocusable(true);
    }
    @Override
    public void paintComponent(Graphics g) {
        game.draw(g);
    }
    public Game getGame(){
        return this.game;
    }
}
