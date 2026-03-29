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

        // TODO: Build the defense chain using fluent setNext():
        //   DodgeHandler -> BlockHandler -> ArmorHandler -> HpHandler
        // Hint: use hero stats for each handler's parameters.
        //   new DodgeHandler(hero.getDodgeChance(), <seed>)
        //   new BlockHandler(hero.getBlockRating() / 100.0)   <-- note the int-to-double conversion
        //   new ArmorHandler(hero.getArmorValue())
        //   new HpHandler()
        // Chain them: dodge.setNext(block).setNext(armor).setNext(hp)

        // TODO: Create an ActionQueue (the invoker).

        // TODO: Simulate rounds until hero or opponent is defeated (or maxRounds is reached).
        // Each round should:
        //   1) Increment round counter.
        //   2) Enqueue hero actions: AttackCommand, HealCommand, DefendCommand.
        //      Use hero.getAttackPower() for AttackCommand, a fixed heal amount for HealCommand,
        //      and a small dodge boost for DefendCommand.
        //   3) Print the queued commands using actionQueue.getCommandDescriptions().
        //   4) Call actionQueue.executeAll() to run all hero actions.
        //   5) If the opponent is still alive: have the opponent attack the hero.
        //      Route the attack through the defense chain: defenseChain.handle(opponent.getAttackPower(), hero)
        //      Do NOT call hero.takeDamage() directly here.
        //   6) Log round results (e.g. "[Round N] Opponent HP: X | Hero HP: Y").
        //   7) Add the log line to result.addLine(...).

        // TODO: After the loop, determine the winner.
        //   result.setWinner(hero.isAlive() ? hero.getName() : opponent.getName());
        result.setWinner("TODO");
        result.setRounds(round);
        return result;
    }
}
