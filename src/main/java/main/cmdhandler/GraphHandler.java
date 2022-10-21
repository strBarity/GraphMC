package main.cmdhandler;

import main.Main;
import main.calculator.FunctionCalculator;
import main.calculator.StringCalculator;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

public class GraphHandler {
    private static String graphExpression;
    private static Location graphOrigin = new Location(Bukkit.getWorld("world"), 0.5, 0.5, 0.7);
    private static Double graphRadius = 100.0;
    private static Double graphAccuracy = 25.0;
    private static Float graphSize = 1F;
    private static BossBar graphBar = BossBar.bossBar(Component.text("§c식 없음"), 1F, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
    public static boolean isMinusSqrted = false;
    private static boolean graphVisible = false;
    private static final HashMap<Entity, Integer> taskId = new HashMap<>();
    private static boolean isNotNumber(String s) {
        try {
            Double.parseDouble(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    public static void onCommand(CommandSender commandsender, String[] args) {
        Player p = (Player) commandsender;
        final Component currentShowing = Component.text(Main.INDEX + "§c그래프가 표시중인 도중엔 사용할 수 없습니다. 그래프를 끄려면 §e/graph toggle§c를 사용하세요.").clickEvent(ClickEvent.runCommand("/graph toggle"));
        final String noArg = Main.INDEX + "/graph origin <x> <y> <z> - 그래프의 원점을 지정합니다.\n" + Main.INDEX + "/graph size <굵기> - 선의 굵기를 지정합니다.\n" + Main.INDEX + "/graph radius <범위> - 그래프의 범위를 지정합니다.\n" + Main.INDEX + "/graph accuracy <정확도> - 그래프의 정확도를 지정합니다.\n" + Main.INDEX + "/graph expression <식> - 그래프의 식을 지정합니다. (y의 대한 식)\n" + Main.INDEX + "/graph toggle - 그래프의 보이는 여부를 키거나 끕니다.";
        if (args.length < 1) {
            p.sendMessage(noArg);
            return;
        } switch (args[0]) {
            case "test" -> p.sendMessage(Main.INDEX + FunctionCalculator.calculateFunction(args[1]));
            case "size" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || isNotNumber(args[1])) {
                    if (graphSize == null) p.sendMessage(noArg);
                    else p.sendMessage(Main.INDEX + "현재 선의 굵기는 §e" + graphSize + "§f입니다.");
                    break;
                } else if (Double.parseDouble(args[1]) > 10) {
                    p.sendMessage(Main.INDEX + "§c숫자가 너무 큽니다.");
                    break;
                }
                graphSize = Float.parseFloat(args[1]);
                p.sendMessage(Main.INDEX + "선의 굵기를 §e" + graphSize + "§f(으)로 설정했습니다.");
            } case "origin" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || args[2].isEmpty() || args[3].isEmpty() || isNotNumber(args[1]) || isNotNumber(args[2]) || isNotNumber(args[3])) {
                    p.sendMessage(noArg);
                    break;
                }
                graphOrigin = new Location(p.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                p.sendMessage(Main.INDEX + "그래프의 원점을 §e(§a" + graphOrigin.getX() + "§7, §a" + graphOrigin.getY() + "§7, §a" + graphOrigin.getZ() + "§e) §f(으)로 설정했습니다.");
            } case "radius" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || isNotNumber(args[1])) {
                    if (graphRadius == null) p.sendMessage(noArg);
                    else p.sendMessage(Main.INDEX + "현재 그래프의 반경은 §e" + graphRadius + "§f입니다.");
                    break;
                } else if (Double.parseDouble(args[1]) > 100) {
                    p.sendMessage(Main.INDEX + "§c숫자가 너무 큽니다.");
                    break;
                }
                graphRadius = Double.parseDouble(args[1]);
                p.sendMessage(Main.INDEX + "그래프의 반경을 §e" + graphRadius + "§f(으)로 설정했습니다.");
            } case "accuracy" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || isNotNumber(args[1])) {
                    if (graphAccuracy == null) p.sendMessage(noArg);
                    else p.sendMessage(Main.INDEX + "현재 그래프의 정확도는 §e" + graphExpression + "§f입니다.");
                    break;
                }
                graphAccuracy = Double.parseDouble(args[1]);
                p.sendMessage(Main.INDEX + "그래프의 정확도를 §e" + graphAccuracy + "§f(으)로 설정했습니다.");
            } case "expression" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1) {
                    if (graphExpression.isEmpty()) p.sendMessage(noArg);
                    else p.sendMessage(Main.INDEX + "현재 그래프의 식은 §by=" + graphExpression + "§f입니다.");
                    break;
                }
                String s = args[1].replace("xx", "x^2");
                s = s.replace("e", "§d§oe§b");
                s = s.replace("pi", "§d§oπ§b");
                s = s.replace("π", "§d§oπ§b");
                s = s.replace("√", "§c§o√§b");
                s = s.replace("root", "§c§o√§b");
                s = s.replace("sqrt", "§c§o√§b");
                graphExpression = s;
                p.sendMessage(Main.INDEX + "그래프의 식을 §by=" + s + "§f(으)로 설정했습니다.");
                graphBar = BossBar.bossBar(Component.text("§by=" + graphExpression + " §a표시 중"), 1F, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            } case "toggle" -> {
                if (graphExpression.isEmpty()) {
                    p.sendMessage(Main.INDEX + "§c그래프의 식이 비어 있습니다.");
                } else if (graphVisible) {
                    graphVisible = false;
                    p.hideBossBar(graphBar);
                    p.sendMessage(Main.INDEX + "더 이상 그래프를 표시하지 않습니다.");
                } else {
                    p.showBossBar(graphBar);
                    p.sendMessage(Main.INDEX + "§by=" + graphExpression + "§f에 대한 그래프를 표시합니다.");
                    p.showTitle(Title.title(Component.text("§by=" + graphExpression + " §a표시 중"), Component.text(""), Title.Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(3L), Duration.ofSeconds(1L))));
                    for (double i = 0.0; i < graphRadius*graphAccuracy; i++) {
                        graphVisible = true;
                        String e = ChatColor.stripColor(graphExpression);
                        final double x = graphOrigin.getX()-(graphRadius/2)+(i/graphAccuracy);
                        e = e.replace("x²", Double.toString(x*x)).replace("x^2", Double.toString(x*x)).replace("x", Double.toString(x)).replace("e", Double.toString(Math.E)).replace("π", Double.toString(Math.PI));
                        e = FunctionCalculator.calculateFunction(e);
                        if (e.contains(Integer.toString(-2147483648))) isMinusSqrted = true;
                        else {
                            e = e.replace("(", " ( ");
                            e = e.replace(")", " ) ");
                            e = e.replace("+", " + ");
                            e = e.replace("/", " / ");
                            e = e.replace("*", " * ");
                            e = e.replace("^", " ^ ");
                            e = e.replace("  ", " ");
                            Particle.DustTransition d;
                            if (e.isEmpty()) {
                                e = Double.toString(x);
                                d = new Particle.DustTransition(Color.BLUE, Color.BLUE, graphSize);
                            } else {
                                d = new Particle.DustTransition(Color.RED, Color.RED, graphSize);
                            }
                            StringCalculator calculator;
                            calculator = new StringCalculator();
                            double y = calculator.makeResult(e) + 0.5;
                            Marker mk = Objects.requireNonNull(p.getWorld().spawn(new Location(p.getWorld(), graphOrigin.getX() - (x), y, graphOrigin.getZ()), Marker.class));
                            mk.setGravity(false);
                            mk.setCustomNameVisible(false);
                            int tmpTaskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                                if (!graphVisible) {
                                    Bukkit.getScheduler().cancelTask(taskId.get(mk));
                                    mk.remove();
                                } else {
                                    mk.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, mk.getLocation(), 1, 0, 0, 0, 0, d, true);
                                    mk.teleport(new Location(p.getWorld(), graphOrigin.getX() - (x), y, graphOrigin.getZ()));
                                }
                            }, 0, 10L);
                            taskId.put(mk, tmpTaskId);
                        }
                    } if (isMinusSqrted) p.sendMessage(Main.INDEX + "음수의 제곱근은 무시되었습니다.");
                }
            } default -> p.sendMessage(noArg);
        }
    }
}
