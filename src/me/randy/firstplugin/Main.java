package me.randy.firstplugin;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import me.randy.firstplugin.commands.Start;
import me.randy.firstplugin.listeners.DeathListener;
import me.randy.firstplugin.listeners.JoinListener;

public final class Main extends JavaPlugin {
	
	public Game CurrentGame = null;
	private int NextSwap = 0;
	private int Count = 0;
	
	@Override
	public void onEnable () {
		/* COMMANDS */
		new Start(this);
		/* EVENTS */
		new JoinListener(this);
		new DeathListener(this);
		
		System.out.println("Plugin is now enabled");
	}
	
	@Override
	public void onDisable () {
		// when plugin ends
		System.out.println("Plugin is now disabled");
	}
	
	private void setTimeout(Runnable runnable, int delay) {
		try {
	        Thread.sleep(delay);
	        runnable.run();
	    }
	    catch (Exception e){
	        System.err.println(e);
	    }
	}
	
	private void GameLoop () {
		
		Count += 50;
		if (this.Count >= this.NextSwap) {
			int int_random = ThreadLocalRandom.current().nextInt() % 8;
			if (int_random == 0) int_random = 5;
			this.CurrentGame.Swap();
			Bukkit.broadcastMessage(int_random + "minutes till the next swap!");
			this.NextSwap = int_random * 1000 * 60;
			this.Count = 0;
		}
		this.setTimeout(() -> this.GameLoop(), 50);
	}
	
	public void Start () {
		if (this.CurrentGame != null) {
			return;
		}
		Bukkit.broadcastMessage(ChatColor.GREEN + "Game has begun!");
		Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
		CurrentGame = new Game(players);
		Bukkit.broadcastMessage(ChatColor.AQUA + "First Swap starting in 5 Minutes!");
		this.NextSwap = 1000 * 60 * 5;
		this.GameLoop();
	}
}
