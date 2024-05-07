package Enemies;

import Level.Level;
import Main.Game;
import Player.Player;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

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
            currentLevel.spittersList.add(extraSpitter.poll());
        }
        for (Ghoul ghoul : currentLevel.ghoulList){
            if (ghoul.isActive) {
                ghoul.update(game);
                playerAttack(ghoul, game.getPlayer());
            }
        }
        for (Dagger dagger : currentLevel.daggerList){
            if (dagger.isActive) {
                dagger.update(game);
                playerAttack(dagger, game.getPlayer());
            }
        }
        for (Summoner summoner : currentLevel.summonersList){
            if (summoner.isActive) {
                summoner.update(game);
                playerAttack(summoner, game.getPlayer());
            }
        }
        for (Spitter spitter : currentLevel.spittersList){
            if (spitter.isActive) {
                spitter.update(game);
                playerAttack(spitter, game.getPlayer());
            }
        }
        for (Shielder shielder : currentLevel.shielderList){
            if (shielder.isActive){
                shielder.update(game);
                playerAttack(shielder, game.getPlayer());
            }
        }
        for (Hoarder hoarder : currentLevel.hoardersList){
            if (hoarder.isActive){
                hoarder.update(game);
                playerAttack(hoarder, game.getPlayer());
            }
        }
        for (Guardian guardian : currentLevel.guardiansList){
            if (guardian.isActive){
                guardian.update(game);
                playerAttack(guardian, game.getPlayer());
            }
        }
        for (Hive hive : currentLevel.hivesList){
            if (hive.isActive){
                hive.update(game);
                playerAttack(hive, game.getPlayer());
            }
        }
    }
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        while (!extraSpitter.isEmpty()){
            currentLevel.spittersList.add(extraSpitter.poll());
        }
        for (Ghoul ghoul : currentLevel.ghoulList){
            if (ghoul.isActive) ghoul.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Dagger dagger : currentLevel.daggerList){
            if (dagger.isActive) dagger.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Summoner summoner : currentLevel.summonersList){
            if (summoner.isActive) summoner.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Spitter spitter :currentLevel.spittersList){
            if (spitter.isActive) spitter.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Shielder shielder : currentLevel.shielderList){
            if (shielder.isActive) shielder.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Hoarder hoarder : currentLevel.hoardersList){
            if (hoarder.isActive) hoarder.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Guardian guardian : currentLevel.guardiansList){
            if (guardian.isActive) guardian.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Hive hive : currentLevel.hivesList){
            if (hive.isActive) hive.draw(g, xLevelOffset, yLevelOffset);
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
        for (Ghoul ghoul : currentLevel.ghoulList){
            ghoul.resetAll();
        }
        for (Dagger dagger : currentLevel.daggerList){
            dagger.resetAll();
        }
        for (Summoner summoner : currentLevel.summonersList){
            summoner.resetAll();
        }
        for (Spitter spitter : currentLevel.spittersList){
            spitter.resetAll();
        }
        for (Hoarder hoarder : currentLevel.hoardersList){
            hoarder.resetAll();
        }
    }
    public boolean checkWon(int indexLevel){
        if (indexLevel >= 1) return false;
        if (!currentLevel.getBoss().getFirst().isActive) return true;
        boolean readyForBoss = true;
        for (Enemy enemy : currentLevel.getMustKillEnemies()){
            if (enemy.isActive) {
                readyForBoss = false;
                break;
            }
        }
        if (readyForBoss){
            loadBossPosition();
        }
//        for (Ghoul ghoul : currentLevel.ghoulList){
//            if (ghoul.isActive) {
//                return false;
//            }
//        }
//        for (Dagger dagger : daggerList){
//            if (dagger.isActive) {
//                return false;
//            }
//        }
//        for (Summoner summoner : currentLevel.summonersList){
//            if (summoner.isActive) {
//                return false;
//            }
//        }
//        for (Spitter spitter : currentLevel.spittersList){
//            if (spitter.isActive) {
//                return false;
//            }
//        }
        return false;
    }

    private void loadBossPosition() {
        game.getPlayer().getHitbox().x = currentLevel.getPlayerTeleport().x;
        game.getPlayer().getHitbox().y = currentLevel.getPlayerTeleport().y;
    }

    public Queue<Spitter> extraSpitter = new LinkedList<>();
}
