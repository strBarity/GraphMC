package main;

import main.cmdhandler.CMDHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public static final String INDEX = "§4[§cGraphMC§4]§f ";

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(Main.INDEX + "§a플러그인이 활성화되었습니다.");
        this.getDescription().getCommands().keySet().forEach(s -> {
            Objects.requireNonNull(getCommand(s)).setExecutor(new CMDHandler());
            Objects.requireNonNull(getCommand(s)).setTabCompleter(new CMDHandler());
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Main.INDEX + "§c플러그인이 비활성화되었습니다.");
    }
}
