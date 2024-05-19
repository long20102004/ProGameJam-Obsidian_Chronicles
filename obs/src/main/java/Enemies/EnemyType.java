package Enemies;

public enum EnemyType {
    Ghoul, Summoner, Spitter, Shielder, Dagger, Hoarder, Hive;
    public static String getEnemyType(int value){
        if (value < 1) return "NONE";
        return EnemyType.values()[value - 1].name();
    }
}
