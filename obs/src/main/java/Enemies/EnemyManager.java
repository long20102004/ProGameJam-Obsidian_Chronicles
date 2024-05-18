package Enemies;

import Level.Level;
import Main.Game;
import OnlineData.ImageSender;
import Player.Player;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
@Component
public class EnemyManager {
    private final Game game;
    private Level currentLevel;
    public EnemyManager(Game game){
        this.game = game;
        currentLevel = game.getLevelManager().getLevel();
    }
    public void loadEnemies(Level level){
        currentLevel = level;
    }
    public void update(){
        while (!extraSpitter.isEmpty()){
            currentLevel.enemies.add(extraSpitter.poll());
        }
        for (Enemy enemy : currentLevel.enemies){
            if (enemy.isActive){
                enemy.update(game);
                playerAttack(enemy, game.getPlayer());
            }
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        while (!extraSpitter.isEmpty()){
            currentLevel.enemies.add(extraSpitter.poll());
        }
        for (Enemy enemy : currentLevel.enemies){
            if (enemy.isActive) enemy.draw(g, xLevelOffset, yLevelOffset);
        }
    }
    public void playerAttack(Enemy enemy, Player player){
        if (!player.isAttacked()) return;
        if (enemy.isActive){
            if (player.getAttackBox().intersects(enemy.hitbox)){
                enemy.hurt(Player.damage);
            }
        }
    }
    public void resetAll(){
        for (Enemy enemy : currentLevel.enemies){
            enemy.resetAll();
        }
    }
    public boolean checkWon(int indexLevel){
//        if (indexLevel >= 1) return false;
////        if (!currentLevel.getBoss().getFirst().isActive) return true;
////        boolean readyForBoss = true;
////        for (Enemy enemy : currentLevel.getMustKillEnemies()){
////            if (enemy.isActive) {
////                readyForBoss = false;
////                break;
////            }
////        }
////        if (readyForBoss){
////            loadBossPosition();
////        }
//        Game.state = 1;
//        ImageSender.sendGameState();
//        game.resetAll();
        return true;
    }

    private void loadBossPosition() {
        game.getPlayer().getHitbox().x = currentLevel.getPlayerTeleport().x;
        game.getPlayer().getHitbox().y = currentLevel.getPlayerTeleport().y;
    }

    public Queue<Spitter> extraSpitter = new LinkedList<>();
}
