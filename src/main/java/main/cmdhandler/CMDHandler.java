package main.cmdhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        List<String> graphTab = new ArrayList<>(Arrays.asList("origin", "radius", "accuracy", "size", "expression", "toggle"));
        if (strings.length == 1 && "graph".equals(s) && commandSender.isOp()) {
            graphTab.add("test");
            return graphTab;
        } else if (strings.length == 1 && "graph".equals(s) && !commandSender.isOp()) return graphTab;
        return null;
    }
}

