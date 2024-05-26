package State;

import Audio.AudioPlayer;
import Main.Game;
import OnlineData.ImageSender;
import Player.Gunslinger;
import Player.HoarderTransform;
import Player.Player;
import Player.SwordHero;
import Upgrade.Shop;
import utilz.ExtraMethods;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import Player.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static Main.Game.reward;

@Component
public class Playing implements StateMethods {
    public static boolean isAiMode = false;
    public static int index;
    public static int countReceivedAction = 0;
    public static int maxActionCount = 300;
    public boolean readyToSend = true;
    public boolean readyToUpdate = false;
    private Game game;
    public static boolean receivedAction;
    public static String action;
    private int xDrawOffset;
    private int yDrawOffset;
    private final float rightBorder = 0.5f * Game.GAME_WIDTH;
    private final float leftBorder = 0.5f * Game.GAME_WIDTH;
    private final float underBorder = 0.6f * Game.GAME_HEIGHT;
    private final float aboveBorder = 0.4f * Game.GAME_HEIGHT;
    private int levelTilesWide;
    private int levelTilesHeight;
    private int maxTilesOffset;
    private int maxTileOffsetY;
    private int maxLevelOffsetX;
    private int maxLevelOffsetY;
    private int state;
    private Timer timer = new Timer();
    private Shop shop;
    private Player currentPlayer;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private TimerTask timerTask;

    public Playing(Game game) {
        this.game = game;
        initClass();
    }

    private void initClass() {
        shop = new Shop(this);
        levelTilesWide = game.getLevelManager().getLevel().getLvlData()[0].length + 5;
        levelTilesHeight = game.getLevelManager().getLevel().getLvlData().length + 5;
        maxTilesOffset = levelTilesWide - Game.NUMBER_TILES_IN_WIDTH;
        maxTileOffsetY = levelTilesHeight - Game.NUMBER_TILES_IN_HEIGHT;
        maxLevelOffsetX = maxTilesOffset * Game.TILE_SIZE;
        maxLevelOffsetY = maxTileOffsetY * Game.TILE_SIZE;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_W)) {
            game.getPlayer().setLedgeGrab(true);
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                game.getPlayer().setRight(false);
                game.getPlayer().setLeft(true);
                game.getPlayer().setMoving(true);

                break;
            case KeyEvent.VK_D:
                game.getPlayer().setRight(true);
                game.getPlayer().setLeft(false);
                game.getPlayer().setMoving(true);

                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                game.getPlayer().setJump(true);
                break;
            case KeyEvent.VK_J:
                setAttackDueToTimeAndPressed();
                break;
            case KeyEvent.VK_ESCAPE:
                GameState.gameState = GameState.PAUSE;
                break;
            case KeyEvent.VK_S:
                game.getPlayer().setBlock(true);
                break;
            case KeyEvent.VK_F:
                game.getPlayer().setDash(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        Game.reward -= 1;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                game.getPlayer().setMoving(false);
//                game.getPlayer().setChangeDir(false);
                break;
            case KeyEvent.VK_SPACE:
                game.getPlayer().setJump(false);
                break;
            case KeyEvent.VK_W:
                game.getPlayer().setLedgeGrab(false);
                game.getPlayer().setJump(false);
                break;
            case KeyEvent.VK_J:
                resetAttack();
                break;
            case KeyEvent.VK_S:
                game.getPlayer().setBlock(false);
                break;
            case KeyEvent.VK_SHIFT:
                game.getPlayer().setLedgeGrab(false);
                break;
            case KeyEvent.VK_E:
                game.getPlayer().increaseTalking();
                break;
            case KeyEvent.VK_U:
                Player.currentHero = Player.HOARDER;
                playerTransform();
                break;
            case KeyEvent.VK_G:
                Player.currentHero = Player.SWORD_WOMAN;
                playerTransform();
                break;
            case KeyEvent.VK_F:
                game.getPlayer().setDash(false);
                break;
            case KeyEvent.VK_ENTER:
                isAiMode = true;
        }
    }

    private void playerTransform() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        Point currentPoint = new Point((int) game.getPlayer().getHitbox().x, (int) game.getPlayer().getHitbox().y);
        currentPlayer = game.getPlayer();
        if (Player.currentHero != Player.NOT_CHANGE) game.getAudioPlayer().playEffectSound(AudioPlayer.TRANSFORM);
        switch (Player.currentHero) {
            case Player.SWORD_HERO -> game.setPlayer(new SwordHero(currentPoint.x, currentPoint.y, game));
            case Player.GUN_SLINGER -> game.setPlayer(new Gunslinger(currentPoint.x, currentPoint.y, game));
            case Player.HOARDER -> {
                game.setPlayer(new HoarderTransform(currentPoint.x, currentPoint.y - 3 * Game.TILE_SIZE, game));
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        game.getAudioPlayer().playEffectSound(AudioPlayer.TRANSFORM);
                        Point point = new Point((int) game.getPlayer().getHitbox().x, (int) game.getPlayer().getHitbox().y);
                        game.setPreviousPlayer(currentPlayer, point);
                    }
                };
                timer.schedule(timerTask, 5000);
            }
            case Player.SWORD_WOMAN -> game.setPlayer(new SwordWoman(currentPoint.x, currentPoint.y, game));

        }
        Player.currentHero = Player.NOT_CHANGE;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (game.getPlayer() instanceof Gunslinger) game.getPlayer().getGun().updateAngle(e);
        shop.mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        shop.mousePressed(e);
        switch (e.getButton()) {
            case MouseEvent.BUTTON3:
                game.getPlayer().setDash(true);
                break;
            case MouseEvent.BUTTON1:
                game.getPlayer().setAttacking(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        shop.mouseReleased(e);
        switch (e.getButton()) {
            case MouseEvent.BUTTON3:
                game.getPlayer().setDash(false);
                break;
            case MouseEvent.BUTTON1:
                game.getPlayer().setAttacking(false);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    private void setAttackDueToTimeAndPressed() {
        state = (state + 1) % 3;
        switch (state) {
            case 0:
                game.getPlayer().setAttacking(true);
                break;
            case 1:
                game.getPlayer().setDoubleAttack(true);
                break;
            case 2:
                game.getPlayer().setTripleAttack(true);
                break;
        }
    }

    private void resetAttack() {
        game.getPlayer().setAttacking(false);
        game.getPlayer().setDoubleAttack(false);
        game.getPlayer().setTripleAttack(false);
    }

    @Override
    public void draw(Graphics g) {
//        action();
        game.getLevelManager().draw(g, xDrawOffset, yDrawOffset);
        Color color = new Color(0, 0, 0, 80);
        g.setColor(color);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        game.getItemsManager().draw(g, xDrawOffset, yDrawOffset);
        game.getObjectManager().draw(g, xDrawOffset, yDrawOffset);
        if (game.getPlayer().getActive()) game.getPlayer().draw(g, xDrawOffset, yDrawOffset);
        game.getEnemyManager().draw(g, xDrawOffset, yDrawOffset);
        game.getNpcManager().draw(g, xDrawOffset, yDrawOffset);
        if (shop.isShopping()) shop.draw(g);
    }

    @Override
    public void update() {
        if (isAiMode){
            autoAction();
            if (checkTrap()) {
//            System.out.println(game.getPlayer().getHitbox().x + "here: ");
//            System.out.println(game.getLevelManager().getLevel().getTrapStartPoint().x + " " + game.getLevelManager().getLevel().getTrapEndPoint().x );
                game.getPlayer().setHitboxX((float) game.getLevelManager().getLevel().getTeleportTrapPoint().x);
                game.getPlayer().setHitboxY((float) game.getLevelManager().getLevel().getTeleportTrapPoint().y);
            }
        }
//        if (!readyToUpdate) return;
        if (game.getPlayer().getActive()) game.getPlayer().update(game);
        updateDrawOffset();
        game.getEnemyManager().update();
        game.getObjectManager().update();
        game.getItemsManager().update();
        game.getLevelManager().update();
        game.getNpcManager().update(game);
        game.getAudioPlayer().update();
        if (shop.isShopping()) shop.update();
    }

    private void autoAction() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("Action2.txt"));
            String actions = lines.get(10);
            String[] action = actions.split(" ");
//            System.out.println(action.length);
            if (index < action.length) performAction(action[index]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performAction(String s) {
        resetStatus();
        switch (s) {
            case "0" -> {
                game.getPlayer().setLeft(true);
                game.getPlayer().setMoving(true);
                game.getPlayer().setRight(false);
            }
            case "1" -> {
                game.getPlayer().setJump(true);
            }
            case "2" -> {
                game.getPlayer().setRight(true);
                game.getPlayer().setMoving(true);
                game.getPlayer().setLeft(false);
            }
            case "3" -> {
                game.getPlayer().setAttacking(true);
            }
        }
    }

    private void resetStatus() {
        game.getPlayer().setRight(false);
        game.getPlayer().setMoving(false);
        game.getPlayer().setLeft(false);
        game.getPlayer().setDash(false);
        game.getPlayer().setAttacking(false);
        game.getPlayer().setJump(false);
    }


    private void updateDrawOffset() {
        int playerX = (int) game.getPlayer().getHitbox().x;
        int diff = playerX - xDrawOffset;
        if (diff > rightBorder) {
            xDrawOffset += (int) (diff - rightBorder);
        } else if (diff < leftBorder) {
            xDrawOffset += (int) (diff - leftBorder);
        }
        if (xDrawOffset > maxLevelOffsetX) xDrawOffset = maxLevelOffsetX;
        else if (xDrawOffset < 0) xDrawOffset = 0;
        int playerY = (int) game.getPlayer().getHitbox().y;
        int diffY = playerY - yDrawOffset;
        if (diffY > underBorder) {
            yDrawOffset += (int) (diffY - underBorder);
        } else if (diffY < aboveBorder) {
            yDrawOffset += (int) (diffY - aboveBorder);
        }
        if (yDrawOffset < 0) yDrawOffset = 0;
        else if (yDrawOffset > maxLevelOffsetY) yDrawOffset = maxLevelOffsetY;
    }

    public Game getGame() {
        return game;
    }

    public void action() {
        readDataFromFile();
        if (!readyToUpdate && readyToSend) {
            sendData();
            readyToSend = false;
        }
        if (receivedAction) {
            if (checkTrap()) {
//            System.out.println(game.getPlayer().getHitbox().x + "here: ");
//            System.out.println(game.getLevelManager().getLevel().getTrapStartPoint().x + " " + game.getLevelManager().getLevel().getTrapEndPoint().x );
                game.getPlayer().setHitboxX((float) game.getLevelManager().getLevel().getTeleportTrapPoint().x);
                game.getPlayer().setHitboxY((float) game.getLevelManager().getLevel().getTeleportTrapPoint().y);
            }
            countReceivedAction++;
            reward--;
            if (countReceivedAction >= maxActionCount || checkLevelEndPoint()) {
                Game.state = 1;
                if (checkLevelEndPoint()) reward += 1000;
                sendData();
                readyToSend = true;
                readyToUpdate = false;
                game.resetAll();
            }
            readyToUpdate = true;
            readyToSend = true;
            switch (action) {
                case "0" -> {
                    game.getPlayer().setLeft(true);
                    game.getPlayer().setMoving(true);
                }
                case "1" -> {
                    game.getPlayer().setJump(true);
                }
                case "2" -> {
                    game.getPlayer().setRight(true);
                    game.getPlayer().setMoving(true);
                }
                case "3" -> {
                    game.getPlayer().setAttacking(true);
                }
            }
            resetDataFile();
        } else {
            readyToUpdate = false;
            game.getPlayer().setRight(false);
            game.getPlayer().setMoving(false);
            game.getPlayer().setLeft(false);
            game.getPlayer().setDash(false);
            game.getPlayer().setAttacking(false);
            game.getPlayer().setJump(false);
        }

    }

    private boolean checkLevelEndPoint() {
        if (game.getPlayer() == null) return false;
        if (game.getPlayer().getHitbox().getY() > game.getLevelManager().getLevel().getPlayerEndPoint().getY()
                || game.getPlayer().getHitbox().getY() > game.getLevelManager().getLevel().getPlayerEndPoint2().getY()) {
            return true;
        }
        return false;
    }

    private void resetDataFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data", false));
            writer.write("false");
            writer.newLine();
            writer.write("NONE");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMaxOffset() {
        levelTilesWide = game.getLevelManager().getLevel().getLvlData()[0].length + 5;
        levelTilesHeight = game.getLevelManager().getLevel().getLvlData().length + 5;
        maxTilesOffset = levelTilesWide - Game.NUMBER_TILES_IN_WIDTH;
        maxTileOffsetY = levelTilesHeight - Game.NUMBER_TILES_IN_HEIGHT;
        maxLevelOffsetX = maxTilesOffset * Game.TILE_SIZE;
        maxLevelOffsetY = maxTileOffsetY * Game.TILE_SIZE;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public Shop getShop() {
        return shop;
    }


    public void readDataFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data"));
            String line;
            if ((line = reader.readLine()) != null) {
                receivedAction = Boolean.parseBoolean(line.trim());
                if ((line = reader.readLine()) != null) {
                    action = line.trim();
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendData() {
        ImageSender.sendImage(ExtraMethods.getScreenShot());
        ImageSender.sendReward();
        ImageSender.sendGameState();
        deleteAllCurrentImage();
        Game.reward = 0;
    }

    private static void deleteAllCurrentImage() {
        String directoryPath = System.getProperty("user.dir"); // Get the current working directory
        try {
            Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().startsWith("screenshot")) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkTrap() {
        reward -= 5;
        if (game.getPlayer().getHitbox().x <= game.getLevelManager().getLevel().getTrapStartPoint().x &&
                game.getPlayer().getHitbox().x >= game.getLevelManager().getLevel().getTrapEndPoint().x) return true;
        return false;
    }
}
