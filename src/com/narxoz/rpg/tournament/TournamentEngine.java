package com.narxoz.rpg.tournament;

import com.narxoz.rpg.arena.*;
import com.narxoz.rpg.chain.*;
import com.narxoz.rpg.command.*;
import java.util.Random;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private Random random = new Random(1L);

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        int round = 0;
        final int maxRounds = 20;
        ActionQueue actionQueue = new ActionQueue();

        while (hero.isAlive() && opponent.isAlive() && round < maxRounds) {
            round++;

            actionQueue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
            actionQueue.enqueue(new HealCommand(hero, 20));
            actionQueue.enqueue(new DefendCommand(hero, 0.15));

            System.out.println("[Round " + round + "] Queued actions:");
            for (String description : actionQueue.getCommandDescriptions()) {
                System.out.println("  " + description);
            }

            actionQueue.executeAll();

            if (opponent.isAlive()) {
                DefenseHandler dodge = new DodgeHandler(hero.getDodgeChance(), random.nextLong());
                DefenseHandler block = new BlockHandler(hero.getBlockRating() / 100.0);
                DefenseHandler armor = new ArmorHandler(hero.getArmorValue());
                DefenseHandler hp = new HpHandler();
                dodge.setNext(block).setNext(armor).setNext(hp);

                dodge.handle(opponent.getAttackPower(), hero);
            }

            String logLine = "[Round " + round + "] Opponent HP: " + opponent.getHealth() + " | Hero HP: " + hero.getHealth();
            System.out.println(logLine);
            result.addLine(logLine);
        }

        if (hero.isAlive() && !opponent.isAlive()) result.setWinner(hero.getName());
        
        else if (opponent.isAlive() && !hero.isAlive()) result.setWinner(opponent.getName());
        
        else if (hero.getHealth() >= opponent.getHealth()) result.setWinner(hero.getName());
        
        else result.setWinner(opponent.getName());
        
        result.setRounds(round);
        return result;
    }
}
