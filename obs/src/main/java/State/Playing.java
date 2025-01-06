package State;

import AnimatedObjects.Merchant;
import Audio.AudioPlayer;
import Main.Game;
import OnlineData.ImageSender;
import Player.Gunslinger;
import Player.HoarderTransform;
import Player.Player;
import Player.SwordHero;
import Upgrade.Shop;
import utilz.Constant;
import utilz.ExtraMethods;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import Player.*;
import utilz.LoadSave;

import java.util.List;

import static Main.Game.*;
import static Player.Player.*;
import static java.lang.Math.random;

@Component
public class Playing implements StateMethods {
    public static boolean isAiMode = false;
    public static int index = 0;
    public static int countReceivedAction = 0;
    public static int maxActionCount = 300;
    public boolean readyToSend = true;
    public boolean readyToUpdate = false;

    private Game game;
    public static boolean receivedAction;
    public static String action;
    private int xDrawOffset;
    private int yDrawOffset;
    private BufferedImage currentIcon;
    private BufferedImage swordHeroIcon;
    private BufferedImage swordWomanIcon;
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
    private String[] aiAction1, aiAction2;
    Random random = new Random();
    private int currentLine = 1;
    private boolean jumpedLine = false;
    private boolean readyForAI = true;

    public Playing(Game game) {

        this.game = game;
        initClass();
        initAiAction();
    }

    private void initAiAction() {
        try {
            List<String> lines1 = Files.readAllLines(Paths.get("Action.txt"));
            List<String> lines2 = Files.readAllLines(Paths.get("Action2.txt"));
//            int rand = random.nextInt(0, 22);
//            System.out.println(rand);
            String actions1 = lines1.get(12);
            String actions2 = lines2.get(4);
            aiAction1 = actions1.split(" ");
            aiAction2 = actions2.split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClass() {
        shop = new Shop(this);
        swordHeroIcon = LoadSave.getImg(LoadSave.SWORD_HERO_ICON);
        swordWomanIcon = LoadSave.getImg(LoadSave.SWORD_WOMAN_ICON);
        currentIcon = swordHeroIcon;
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
        if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_I)) {
            game.getPlayer().setBuffs(true);
            game.getPlayer().setLightCutBuff(true);
            if (game.getPlayer().getCountAniBuffs() == 0) game.getPlayer().setCountAniBuffs(1);
        } else if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_O)) {
            game.getPlayer().setBuffs(true);
            game.getPlayer().setHolySlashBuff(true);
            if (game.getPlayer().getCountAniBuffs() == 0) game.getPlayer().setCountAniBuffs(1);
        } else if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_S)) {
            game.getPlayer().setBuffs(true);
            game.getPlayer().setCastShieldBuff(true);
            game.getPlayer().updateHealthAndPower(20,-5,0);
            if (game.getPlayer().getCountAniBuffs() == 0) game.getPlayer().setCountAniBuffs(1);
        } else if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_K)) {
            game.getPlayer().setBuffs(true);
            game.getPlayer().setCastBuff(true);
            if (game.getPlayer().getCountAniBuffs() == 0) game.getPlayer().setCountAniBuffs(1);
        } else if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_X)) {
            game.getPlayer().setBuffs(true);
            game.getPlayer().setGreatHealBuff(true);
            if (game.getPlayer().getCountAniBuffs() == 0) game.getPlayer().setCountAniBuffs(1);
        } else if (pressedKeys.contains(KeyEvent.VK_SHIFT) && pressedKeys.contains(KeyEvent.VK_W)) {
            game.getPlayer().setLedgeGrab(true);
            return;
        } else {
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
                if (!Merchant.isLocked)
                    game.getPlaying().getShop().setShopping(!game.getPlaying().getShop().isShopping());
                break;

            case KeyEvent.VK_U:
                if (!HoarderTransform.isLocked) {
                    Player.currentHero = Player.HOARDER;
                    playerTransform();
                }
                break;
            case KeyEvent.VK_G:
                currentHero = SWORD_WOMAN;
                playerTransform();
                break;
            case KeyEvent.VK_K:
                currentHero = GUN_SLINGER;
                playerTransform();
                break;
            case KeyEvent.VK_H:
                currentHero = SWORD_HERO;
                playerTransform();
                break;
            case KeyEvent.VK_F:
                game.getPlayer().setDash(false);
                break;
//            case KeyEvent.VK_ENTER:
//                readyForAI = true;
//                isAiMode = true;
        }
    }

    private void playerTransform() {
        if (game.getPlayer().getCurrentPower() < 30) return;
        int powerLeft = game.getPlayer().getCurrentPower();
        int hpLeft = game.getPlayer().getCurrentHealth();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        Point currentPoint = new Point((int) game.getPlayer().getHitbox().x, (int) game.getPlayer().getHitbox().y);
        currentPlayer = game.getPlayer();
        if (Player.currentHero != Player.NOT_CHANGE) game.getAudioPlayer().playEffectSound(AudioPlayer.TRANSFORM);
        switch (Player.currentHero) {
            case SWORD_HERO -> {
                game.setPlayer(new SwordHero(currentPoint.x, currentPoint.y, game));
                currentIcon = swordHeroIcon;
            }
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
            case Player.SWORD_WOMAN -> {
                game.setPlayer(new SwordWoman(currentPoint.x, currentPoint.y, game));
                currentIcon = swordWomanIcon;
            }

        }game.getPlayer().setCurrentPower(powerLeft - 30);
        game.getPlayer().setCurrentHealth(hpLeft);
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
                //game.getPlayer().setAttacking(false);
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
        game.getPlayer().setAttacking(true);
        game.getPlayer().setDoubleAttack(false);
        game.getPlayer().setTripleAttack(false);
    }

    @Override
    public void draw(Graphics g) {
        game.getLevelManager().draw(g, xDrawOffset, yDrawOffset);
        Color color = new Color(0, 0, 0, 80);
        g.setColor(color);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        game.getItemsManager().draw(g, xDrawOffset, yDrawOffset);
        game.getObjectManager().draw(g, xDrawOffset, yDrawOffset);
        if (game.getPlayer().isActive()) game.getPlayer().draw(g, xDrawOffset, yDrawOffset);
        game.getEnemyManager().draw(g, xDrawOffset, yDrawOffset);
        game.getNpcManager().draw(g, xDrawOffset, yDrawOffset);
        if (shop.isShopping()) shop.draw(g);

        zoom = 0.18f;
        MODE = MODE * zoom / 2.3f;
        TILE_SIZE = (int) (TILE_DEFAULT_SIZE * MODE);
        GAME_HEIGHT = 100 * TILE_SIZE;
        GAME_WIDTH = 120 * TILE_SIZE;
        int deltaX = 40 * CONST_TILE_SIZE - GAME_WIDTH;
        game.getLevelManager().draw(g, 0, 0);
        drawPlayerIcon(g);
        resetSize();
    }

    private void drawPlayerIcon(Graphics g) {
        float currentPosX = game.getPlayer().getHitbox().x;
        float currentPosY = game.getPlayer().getHitbox().y;
        float currentRateInHeight = currentPosY / CONST_TILE_SIZE;
        float currentRateInWidth = currentPosX / CONST_TILE_SIZE;
        float currentXPosInMiniMap = currentRateInWidth * TILE_SIZE;
        float currentYPosInMiniMap = currentRateInHeight * TILE_SIZE;
        g.setColor(Color.RED);
        switch (Player.currentHero) {
            case SWORD_HERO -> currentIcon = swordHeroIcon;
            case SWORD_WOMAN -> currentIcon = swordWomanIcon;
        }
        g.setColor(new Color(171, 194, 232, 80));
        g.fillOval((int) (currentXPosInMiniMap - 5), (int) (currentYPosInMiniMap - 5) - 12, 30, 30);
        g.drawImage(currentIcon, (int) currentXPosInMiniMap, (int) currentYPosInMiniMap - 16, 20, 20, null);
    }

    private void resetSize() {
        MODE = MODE / zoom * 2.3f;
        zoom = 2.3f;
        TILE_SIZE = (int) (TILE_DEFAULT_SIZE * MODE);
        GAME_HEIGHT = 30 * TILE_SIZE;
        GAME_WIDTH = 50 * TILE_SIZE;
    }

    @Override
    public void update() {
        if (isAiMode) {
            autoAction();
            if (checkTrap()) {
//            System.out.println(game.getPlayer().getHitbox().x + "here: ");
//            System.out.println(game.getLevelManager().getLevel().getTrapStartPoint().x + " " + game.getLevelManager().getLevel().getTrapEndPoint().x );
                game.getPlayer().setHitboxX((float) game.getLevelManager().getLevel().getTeleportTrapPoint().x);
                game.getPlayer().setHitboxY((float) game.getLevelManager().getLevel().getTeleportTrapPoint().y);
            }
        }
//        else resetStatus();
//        if (!readyToUpdate) return;
        if (game.getPlayer().isActive()) game.getPlayer().update(game);
        updateDrawOffset();
        game.getEnemyManager().update();
        game.getObjectManager().update();
        game.getItemsManager().update();
        game.getLevelManager().update();
        game.getNpcManager().update(game);
        game.getAudioPlayer().update();
        if (shop.isShopping()) shop.update(game);
    }

    private void autoAction() {
        if (!readyForAI) return;
        if (!ExtraMethods.isEntityOnWall(game.getPlayer().getHitbox(), game.getPlayer().isRight())) {
            if (currentLine == 1) performAction(aiAction1[index]);
            else performAction(aiAction2[index]);
        }
        if (checkLevelEndPoint() && !jumpedLine) {
            readyForAI = true;
            jumpedLine = true;
            index = 0;
            currentLine++;
            game.getPlayer().setHitboxX((float) game.getLevelManager().getLevel().getLine2Spawn().getX());
            game.getPlayer().setHitboxY((float) game.getLevelManager().getLevel().getLine2Spawn().getY());
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
//    public void action() {
////        readDataFromFile();
//        if (!readyToUpdate && readyToSend){
////            sendData();
//            readyToSend = false;
//        }
//        if (receivedAction) {
//            countReceivedAction++;
//            reward--;
//            if (countReceivedAction >= maxActionCount){
//                Game.state = 1;
//                ImageSender.sendGameState();
//                game.resetAll();
//            }
//            readyToUpdate = true;
//            readyToSend = true;
//            switch (action) {
//                case "0" -> {
//                    game.getPlayer().setLeft(true);
//                    game.getPlayer().setMoving(true);
//                }
//                case "1" -> {
//                    game.getPlayer().setJump(true);
//                }
//                case "2" -> {
//                    game.getPlayer().setRight(true);
//                    game.getPlayer().setMoving(true);
//                }
//                case "3" -> {
//                    game.getPlayer().setAttacking(true);
//                }
//            }
//
//            resetDataFile();
//        } else {
//            readyToUpdate = false;
//            game.getPlayer().setRight(false);
//            game.getPlayer().setMoving(false);
//            game.getPlayer().setLeft(false);
//            game.getPlayer().setDash(false);
//            game.getPlayer().setAttacking(false);
//            game.getPlayer().setJump(false);
//        }
//
//    }

    private boolean checkLevelEndPoint() {
        if (game.getPlayer() == null) return false;
        if (game.getPlayer().getHitbox().getY() > game.getLevelManager().getLevel().getPlayerEndPoint().getY()) {
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
        String directoryPath = System.getProperty("user.dir");
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
        if (currentLine == 1) return false;
        reward -= 5;
        if (game.getPlayer().getHitbox().x <= game.getLevelManager().getLevel().getTrapStartPoint().x &&
                game.getPlayer().getHitbox().x >= game.getLevelManager().getLevel().getTrapEndPoint().x) return true;
        return false;
    }
//    public void readDataFromFile() {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("data"));
//            String line;
//            if ((line = reader.readLine()) != null) {
//                receivedAction = Boolean.parseBoolean(line.trim());
//                if ((line = reader.readLine()) != null) {
//                    action = line.trim();
//                }
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void sendData(){
//        ImageSender.sendImage(ExtraMethods.getScreenShot());
//        ImageSender.sendReward();
//        ImageSender.sendGameState();
//    }
}
