package utilz;

import Level.Level;
import Main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static Main.Game.*;
import static java.lang.Math.*;

public class ExtraMethods {
    private static Game game;
    private static int[][] lvlData;
    public static Level level;
    private static int maxRow, maxCol;
    public static int indexPicture = 0;
    public static Random random = new Random();

    public ExtraMethods(Game newGame, Level newLevel){
        game = newGame;
        level = newLevel;
        maxRow = level.getLvlData().length;
        maxCol = level.getLvlData()[0].length;
        lvlData = new int[maxRow][maxCol];
        lvlData = game.getLevelManager().getLevel().getLvlData();
    }
    public static boolean isIndexValid(int row, int col){
        return row >= 0 && row <= maxRow && col >= 0 && col <= maxCol;
    }
    public static boolean isTileSolid(float xPos, float yPos){
//        if (xPos <= 0 || xPos + game.getPlayer().getHitbox().width >= maxX || yPos <= 0 || yPos + game.getPlayer().getHitbox().height>= maxY) return true;
        int x = (int) (yPos / TILE_SIZE);
        int y = (int) (xPos / TILE_SIZE);
        if (!isIndexValid(x,y)) return true;
        return (lvlData[x][y] > 0 && lvlData[x][y] != 1 && lvlData[x][y] < 250);
    }
    public static boolean isMovingPossible(Rectangle2D.Float border, float xPos, float yPos){
        for (int i = (int) xPos; i<= xPos + border.width; i++) {
            for (int j = (int) yPos; j <= yPos + border.height; j++) {
                if (isTileSolid(i, j)) return false;
            }
        }
        return true;
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float border){
        if (!isTileSolid(border.x, border.y + border.height + 1))
            return isTileSolid(border.x + border.width, border.y + border.height + 1);
        return true;
    }
    public static float updateSpaceBetweenXAndWall(Rectangle2D.Float border, float xSpeed){
        int currentRow = (int) (border.x + border.width) / TILE_SIZE;
        if (xSpeed > 0){
            return (currentRow + 1) * TILE_SIZE - border.width - 1;
        }
        else return border.x;
    }
    public static float updateSpaceBetweenYAndWall(Rectangle2D.Float border, float airSpeed){
        int currentTile = (int) ((border.y + border.height) / TILE_SIZE);
        if (airSpeed > 0){
            return (currentTile + 1) * TILE_SIZE - border.height - 1;
        }
        else return border.y;
    }

    public static boolean isPlayerInRange(Rectangle2D.Float enemyHitBox, int sight, boolean isFly){
        int enemyEndPos = (int) (enemyHitBox.y + enemyHitBox.height);
        int playerEndPos = (int) (game.getPlayer().getHitbox().y + game.getPlayer().getHitbox().getHeight());
        if (abs(game.getPlayer().getHitbox().x - enemyHitBox.x) <= sight){
            return isFly || abs(enemyEndPos - playerEndPos) == 0;
        }
        return false;
    }
    public static boolean isSightClear(Rectangle2D.Float enemyHitBox, boolean isFly){
        int startX = (int) ((int) min(enemyHitBox.x, game.getPlayer().getHitbox().x));
        int endX = (int) ((int) max(enemyHitBox.x, game.getPlayer().getHitbox().x));
        for (int i=startX; i<=endX; i++){
            if (isTileSolid(i, enemyHitBox.y - 1)) return false;
            if (!isFly && !isTileSolid(i, enemyHitBox.y + enemyHitBox.height + 1)) return false;
        }
        return true;
    }
    public static boolean ableToDo(Rectangle2D.Float enemyHitBox, int sight, boolean isFly){
        return (isPlayerInRange(enemyHitBox, sight, isFly) && isSightClear(enemyHitBox, isFly));
    }
    public static BufferedImage reverseImg(BufferedImage img){
        AffineTransform tx = AffineTransform.getScaleInstance(-1,1);
        tx.translate(-img.getWidth(null),0);
        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }
    public static boolean isIn(MouseEvent e, Rectangle bound){
        int xPos = e.getX();
        int yPos = e.getY();
        return bound.contains(xPos, yPos);
    }

    public static boolean isEntityOnWall(Rectangle2D.Float hitbox, boolean isRight) {
        if (isRight){
            return isTileSolid(hitbox.x + hitbox.width + 1, hitbox.y);
        }
        else return isTileSolid(hitbox.x - 1, hitbox.y);
    }
    public static void updateLongMove(Rectangle2D.Float hitbox, float speed, boolean isRight) {
        int startTile;
        int endTile;
        if (isRight) {
            startTile = (int) ((1.0 * hitbox.x + hitbox.width) / TILE_SIZE);
            endTile = (int) Math.floor((hitbox.x + speed + hitbox.width) / TILE_SIZE);
            for (int i = startTile; i <= endTile; i++) {
                if (isTileSolid(i * TILE_SIZE, hitbox.y) || isTileSolid(i * TILE_SIZE, hitbox.y + hitbox.height)) {
                    hitbox.x = i * TILE_SIZE - hitbox.width - 1;
                    return;
                }
            }
            hitbox.x = hitbox.x + speed;
        } else {
            endTile = (int) (hitbox.x / TILE_SIZE);
            startTile = (int) ((1.0 * hitbox.x - speed) / TILE_SIZE);
            for (int i = endTile; i >= startTile; i--) {
                if (isTileSolid(i * TILE_SIZE, hitbox.y) || isTileSolid(i * TILE_SIZE, hitbox.y + hitbox.height)) {
                    hitbox.x =(i + 1) * TILE_SIZE;
                    return;
                }
            }
            hitbox.x = hitbox.x - speed;
        }
        if (isTileSolid(hitbox.x + hitbox.width + 1, hitbox.y + hitbox.height)){
            hitbox.x--;
        }
        if (isTileSolid(hitbox.x, hitbox.y)) hitbox.x ++;
    }
    public static BufferedImage getScreenShot(){
        Robot robot;
        BufferedImage screenCapture = null;
          try {
              robot = new Robot();
              Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
              Rectangle screenRect = new Rectangle(screenSize);
              screenCapture = robot.createScreenCapture(screenRect);
              System.out.println(screenRect);
              ImageIO.write(screenCapture, "png", new File("screenshot" + indexPicture + ".png"));
              indexPicture++;
          } catch (AWTException | IOException e) {
              throw new RuntimeException(e);
          }
          return screenCapture;
    }
    public static void triggerKeyPress(int e){
        Robot robot = null;
        try {
            robot = new Robot();
            robot.keyPress(e);
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }
}
