package State;

import Audio.AudioPlayer;
import Main.Game;
import Player.Gunslinger;
import Player.HoarderTransform;
import Player.Player;
import Player.SwordHero;
import Upgrade.Shop;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static Main.Game.*;

public class Playing implements StateMethods {
    private final Game game;
    private int xDrawOffset;
    private int yDrawOffset;
    private final float rightBorder = 0.5f * GAME_WIDTH;
    private final float leftBorder = 0.5f * GAME_WIDTH;
    private final float underBorder = 0.6f * GAME_HEIGHT;
    private final float aboveBorder = 0.4f * GAME_HEIGHT;
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
        System.out.println(levelTilesWide + " " + levelTilesHeight);
        maxTilesOffset = levelTilesWide - NUMBER_TILES_IN_WIDTH;
        maxTileOffsetY = levelTilesHeight - NUMBER_TILES_IN_HEIGHT;
        maxLevelOffsetX = maxTilesOffset * TILE_SIZE;
        maxLevelOffsetY = maxTileOffsetY * TILE_SIZE;
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
                Player.currentHero = Player.GUN_SLINGER;
                playerTransform();
                break;
            case KeyEvent.VK_F:
                game.getPlayer().setDash(false);
        }
    }

    private void playerTransform() {
        if (timerTask != null){
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
                game.setPlayer(new HoarderTransform(currentPoint.x, currentPoint.y - 3 * TILE_SIZE, game));
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
        game.getLevelManager().draw(g, xDrawOffset, yDrawOffset);
        Color color = new Color(0, 0, 0, 80);
        g.setColor(color);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        game.getItemsManager().draw(g, xDrawOffset, yDrawOffset);
        game.getObjectManager().draw(g, xDrawOffset, yDrawOffset);
        if (game.getPlayer().getActive()) game.getPlayer().draw(g, xDrawOffset, yDrawOffset);
        game.getEnemyManager().draw(g, xDrawOffset, yDrawOffset);
        game.getNpcManager().draw(g, xDrawOffset, yDrawOffset);
        if (shop.isShopping()) shop.draw(g);
    }

    @Override
    public void update() {
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

    public void setMaxOffset() {
        levelTilesWide = game.getLevelManager().getLevel().getLvlData()[0].length + 5;
        levelTilesHeight = game.getLevelManager().getLevel().getLvlData().length + 5;
        maxTilesOffset = levelTilesWide - NUMBER_TILES_IN_WIDTH;
        maxTileOffsetY = levelTilesHeight - NUMBER_TILES_IN_HEIGHT;
        maxLevelOffsetX = maxTilesOffset * TILE_SIZE;
        maxLevelOffsetY = maxTileOffsetY * TILE_SIZE;
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
}
