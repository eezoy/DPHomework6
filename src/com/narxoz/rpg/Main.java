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

        // TODO: Replace placeholder stats with your own meaningful hero values.
        ArenaFighter hero = new ArenaFighter("Hero", 100, 0.20, 25, 5, 18, 3);

        // TODO: Replace placeholder stats with your own meaningful opponent values.
        ArenaOpponent opponent = new ArenaOpponent("Champion", 90, 14);

        // -----------------------------------------------------------------------
        // Part 1 — Command Queue Demo
        // -----------------------------------------------------------------------
        System.out.println("--- Command Queue Demo ---");

        ActionQueue queue = new ActionQueue();

        // Enqueue three hero actions.
        queue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
        queue.enqueue(new HealCommand(hero, 20));
        queue.enqueue(new DefendCommand(hero, 0.15));

        System.out.println("Queued actions:");
        // TODO: Print all queued commands using queue.getCommandDescriptions().
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        // Demonstrate undoLast() — removes the last queued command.
        System.out.println("\nUndoing last queued action...");
        queue.undoLast();

        System.out.println("Queue after undo:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        // Re-queue the defend action and execute everything.
        queue.enqueue(new DefendCommand(hero, 0.15));
        System.out.println("\nExecuting all queued commands...");
        queue.executeAll();

        // -----------------------------------------------------------------------
        // Part 2 — Defense Chain Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Defense Chain Demo ---");

        ArenaFighter chainDemoHero = new ArenaFighter("Chain Tester", 100, 0.20, 25, 5, 18, 3);

        // First run: force the attack through the full chain so reduced damage is visible.
        DefenseHandler dodge = new DodgeHandler(0.0, 99L);
        DefenseHandler block = new BlockHandler(0.30);
        DefenseHandler armor = new ArmorHandler(5);
        DefenseHandler hp    = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        System.out.println("Sending 20 incoming damage through the defense chain...");
        System.out.println("Hero HP before: " + chainDemoHero.getHealth());
        dodge.handle(20, chainDemoHero);
        System.out.println("Hero HP after:  " + chainDemoHero.getHealth());

        // Second run: use a guaranteed dodge to show that the chain can stop early.
        DefenseHandler dodgeOnly = new DodgeHandler(1.0, 99L);
        dodgeOnly.setNext(new BlockHandler(0.30)).setNext(new ArmorHandler(5)).setNext(new HpHandler());

        System.out.println("\nSending another 20 damage into a guaranteed dodge...");
        System.out.println("Hero HP before: " + chainDemoHero.getHealth());
        dodgeOnly.handle(20, chainDemoHero);
        System.out.println("Hero HP after:  " + chainDemoHero.getHealth());

        // -----------------------------------------------------------------------
        // Part 3 — Full Tournament Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Full Arena Tournament ---");

        // TODO: Replace these placeholder names and stats with your own characters.
        ArenaFighter tournamentHero     = new ArenaFighter("Erlan",    120, 0.25, 25, 8, 22, 3);
        ArenaOpponent tournamentOpponent = new ArenaOpponent("Iron Vane", 100, 16);

        TournamentResult result = new TournamentEngine(tournamentHero, tournamentOpponent)
                .setRandomSeed(42L)
                .runTournament();

        System.out.println("Winner : " + result.getWinner());
        System.out.println("Rounds : " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println("  " + line);
        }

        // TODO: Expand this demo so it clearly proves:
        //   1) Command queue with visible undo (already partially shown above).
        //   2) Defense chain reducing or absorbing incoming damage (shown above).
        //   3) A complete tournament run with a readable round-by-round log.

        System.out.println("\n=== Demo Complete ===");
    }
}
