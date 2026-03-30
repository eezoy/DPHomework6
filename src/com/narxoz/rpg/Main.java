package com.narxoz.rpg;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.ArmorHandler;
import com.narxoz.rpg.chain.BlockHandler;
import com.narxoz.rpg.chain.DefenseHandler;
import com.narxoz.rpg.chain.DodgeHandler;
import com.narxoz.rpg.chain.HpHandler;
import com.narxoz.rpg.command.ActionQueue;
import com.narxoz.rpg.command.AttackCommand;
import com.narxoz.rpg.command.DefendCommand;
import com.narxoz.rpg.command.HealCommand;
import com.narxoz.rpg.tournament.TournamentEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 6 Demo: Chain of Responsibility + Command ===\n");

        ArenaFighter hero = new ArenaFighter("Knight", 100, 0.20, 25, 5, 18, 3);
        ArenaOpponent opponent = new ArenaOpponent("Orc", 90, 14);

        // -----------------------------------------------------------------------
        // Part 1 — Command Queue Demo
        // -----------------------------------------------------------------------
        System.out.println("--- Command Queue Demo ---");

        ActionQueue queue = new ActionQueue();

        queue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
        queue.enqueue(new HealCommand(hero, 20));
        queue.enqueue(new DefendCommand(hero, 0.15));

        System.out.println("Queued actions (3 total):");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        // Demonstrate undoLast() — removes the last queued command.
        System.out.println("\nUndoing last queued action...");
        queue.undoLast();

        System.out.println("Queue after undo (2 total):");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        System.out.println("\nBefore execute: opponent HP = " + opponent.getHealth()
            + ", hero HP = " + hero.getHealth()
            + ", hero dodge = " + hero.getDodgeChance());
        System.out.println("Executing remaining queued commands...");
        queue.executeAll();
        System.out.println("After execute:  opponent HP = " + opponent.getHealth()
            + ", hero HP = " + hero.getHealth()
            + ", hero dodge = " + hero.getDodgeChance());
        System.out.println("The removed defend command was not executed.\n");

        // -----------------------------------------------------------------------
        // Part 2 — Defense Chain Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Defense Chain Demo ---");

        ArenaFighter chainDemoHero = new ArenaFighter("Chain Tester", 100, 0.20, 25, 5, 18, 3);

        DefenseHandler dodge = new DodgeHandler(0.0, 42L);
        DefenseHandler block = new BlockHandler(0.30);
        DefenseHandler armor = new ArmorHandler(5);
        DefenseHandler hp = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        System.out.println("Sending 20 incoming damage through the defense chain...");
        System.out.println("Hero HP before: " + chainDemoHero.getHealth());
        dodge.handle(20, chainDemoHero);
        System.out.println("Hero HP after:  " + chainDemoHero.getHealth());

        DefenseHandler dodgeOnly = new DodgeHandler(1.0, 42L);
        dodgeOnly.setNext(new BlockHandler(0.30)).setNext(new ArmorHandler(5)).setNext(new HpHandler());

        System.out.println("\nSending another 20 damage into a guaranteed dodge...");
        System.out.println("Hero HP before: " + chainDemoHero.getHealth());
        dodgeOnly.handle(20, chainDemoHero);
        System.out.println("Hero HP after:  " + chainDemoHero.getHealth());

        // -----------------------------------------------------------------------
        // Part 3 — Full Tournament Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Full Arena Tournament ---");

        ArenaFighter tournamentHero = new ArenaFighter("Killer",    120, 0.25, 25, 8, 22, 3);
        ArenaOpponent tournamentOpponent = new ArenaOpponent("Zombie", 100, 16);

        TournamentResult result = new TournamentEngine(tournamentHero, tournamentOpponent)
                .setRandomSeed(42L)
                .runTournament();

        System.out.println("Winner : " + result.getWinner());
        System.out.println("Rounds : " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println("  " + line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
