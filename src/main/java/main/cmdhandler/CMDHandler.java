package main.cmdhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CMDHandler implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if ("graph".equals(s)) {
            GraphHandler.onCommand(commandSender, strings);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (s.equals("graph") && strings.length == 1) return Arrays.asList("origin", "radius", "accuracy", "size", "expression", "toggle");
        else if (s.equals("graph") && strings.length > 1 ) return List.of();
        return null;
    }
}

