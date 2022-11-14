package main.cmdhandler;

import main.Main;
import main.calculator.ExpressionParser;
import main.calculator.FunctionCalculator;
import main.calculator.NumberParser;
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
    public static boolean isMinusSqrted = false;
    public static boolean isMinusLogged = false;
    public static boolean isConstantFunction = false;
    private static boolean graphVisible = false;
    private static final BossBar graphBar = BossBar.bossBar(Component.text("§7로딩 중..."), 0F, BossBar.Color.PINK, BossBar.Overlay.PROGRESS);
    private static final HashMap<Entity, Integer> taskId = new HashMap<>();
    public static void onCommand(CommandSender commandsender, String[] args) {
        Player p = (Player) commandsender;
        final Component currentShowing = Component.text(Main.INDEX + "§c그래프가 표시중인 도중엔 사용할 수 없습니다. 그래프를 끄려면 §e/graph toggle§c를 사용하세요.").clickEvent(ClickEvent.runCommand("/graph toggle"));
        final String notNumber = Main.INDEX + "§c올바른 숫자를 입력해주세요."; final String tooLarge = Main.INDEX + "§c숫자가 너무 큽니다."; final String notExpression = Main.INDEX + "§c그래프의 식이 올바르지 않습니다.";
        if (args.length < 1) {
            p.sendMessage(Main.INDEX + "§6-------------------------------------------\n" + Main.INDEX + "§cGraphMC §6- §e마인크래프트 그래핑 계산기§a by §dBarity_\n" + Main.INDEX + "§7명령어 목록을 보려면 §e/graph help§7를 입력하세요.\n" + Main.INDEX + "§6-------------------------------------------");
            return;
        } switch (args[0]) {
            case "help" -> p.sendMessage(Main.INDEX + "§6---------------[ §e명령어 목록 §6]------------------\n" + Main.INDEX + "/graph help - 이 창을 띄웁니다.\n" + Main.INDEX + "/graph functions - 그래프의 식에 사용 가능한 함수 리스트를 봅니다.\n" + Main.INDEX + "/graph origin <x> <y> <z> - 그래프의 원점을 지정합니다.\n" + Main.INDEX + "/graph size <굵기> - 선의 굵기를 지정합니다.\n" + Main.INDEX + "/graph radius <범위> - 그래프의 범위를 지정합니다.\n" + Main.INDEX + "/graph accuracy <정확도> - 그래프의 정확도를 지정합니다.\n" + Main.INDEX + "/graph expression <식> - 그래프의 식을 지정합니다. (y의 대한 식)\n" + Main.INDEX + "/graph toggle - 그래프의 보이는 여부를 키거나 끕니다.\n" + Main.INDEX + "§6-------------------------------------------");
            case "functions" -> p.sendMessage(Main.INDEX + "§6----------------[ §e함수 목록 §6]------------------\n" + Main.INDEX + "§5e§f - 자연로그의 밑 §7(2.7182818284590452354...)\n" + Main.INDEX + "§5pi§f, §5π§f - 원주율 §7(3.14159265358979323846...)\n" + Main.INDEX + "§4abs§b(§dx§b)§f, §4|§dx§4|§f - 절댓값\n" + Main.INDEX + "§aasin§b(§dx§b)§f, §aacos§b(§dx§b)§f, §aatan§b(§dx§b) §f- 아크 삼각함수 (역삼각함수)\n" + Main.INDEX + "§2sinh§b(§dx§b), §2cosh§b(§dx§b), §2tanh§b(§dx§b) §f- 쌍곡선 함수\n" + Main.INDEX + "§esin§b(§dx§b), §ecos§b(§dx§b), §etan§b(§dx§b) §f- 삼각함수\n" + Main.INDEX + "§3exp§b(§dx§b) §f- EXP 함수 §b(§5e§b^§dx§b)\n" + Main.INDEX + "§6log§b(§dx§b)§f - 로그 함수 (자연로그)\n" + Main.INDEX + "§csqrt§b(§dx§b), §croot§b(§dx§b), §c§o√§b(§dx§b)§f - 제곱근 (루트)\n" + Main.INDEX + "§7(음수의 제곱근과 로그는 무시되어 값이 반환되지 않음)\n" + Main.INDEX + "§6-------------------------------------------");
            case "size" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || NumberParser.isNotFloat(args[1])) {
                    if (graphSize == null) p.sendMessage(notNumber);
                    else if (args.length == 1) p.sendMessage(Main.INDEX + "현재 선의 굵기는 §e" + graphSize + "§f입니다.");
                    break;
                } else if (Double.parseDouble(args[1]) > 10) {
                    p.sendMessage(tooLarge);
                    break;
                } graphSize = Float.parseFloat(args[1]);
                p.sendMessage(Main.INDEX + "선의 굵기를 §e" + graphSize + "§f(으)로 설정했습니다.");
            } case "origin" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length < 4 || NumberParser.isNotDouble(args[1]) || NumberParser.isNotDouble(args[2]) || NumberParser.isNotDouble(args[3])) {
                    p.sendMessage(notNumber);
                    break;
                } graphOrigin = new Location(p.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                p.sendMessage(Main.INDEX + "그래프의 원점을 §e(§a" + graphOrigin.getX() + "§7, §a" + graphOrigin.getY() + "§7, §a" + graphOrigin.getZ() + "§e) §f(으)로 설정했습니다.");
            } case "radius" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || NumberParser.isNotDouble(args[1])) {
                    if (graphRadius == null) p.sendMessage(notNumber);
                    else if (args.length == 1) p.sendMessage(Main.INDEX + "현재 그래프의 반경은 §e" + graphRadius + "§f입니다.");
                    break;
                } else if (Double.parseDouble(args[1]) > 100) {
                    p.sendMessage(tooLarge);
                    break;
                } graphRadius = Double.parseDouble(args[1]);
                p.sendMessage(Main.INDEX + "그래프의 반경을 §e" + graphRadius + "§f(으)로 설정했습니다.");
            } case "accuracy" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1 || NumberParser.isNotDouble(args[1])) {
                    if (graphAccuracy == null) p.sendMessage(notNumber);
                    else if (args.length == 1 ) p.sendMessage(Main.INDEX + "현재 그래프의 정확도는 §e" + graphAccuracy + "§f입니다.");
                    break;
                } graphAccuracy = Double.parseDouble(args[1]);
                p.sendMessage(Main.INDEX + "그래프의 정확도를 §e" + graphAccuracy + "§f(으)로 설정했습니다.");
            } case "expression" -> {
                if (graphVisible) {
                    p.sendMessage(currentShowing);
                    break;
                } else if (args.length == 1) {
                    if (graphExpression.isEmpty()) p.sendMessage(Main.INDEX + "올바른 식을 입력해주세요.");
                    else p.sendMessage(Main.INDEX + "현재 그래프의 식은 §by=" + graphExpression + "§f입니다.");
                    break;
                } String s = ExpressionParser.highlightFunction(args[1].replace("xx", "x^2"));
                try {
                    for (double i = 0.0; i < graphRadius * graphAccuracy; i++) {
                        String e = ChatColor.stripColor(s);
                        final double t = graphOrigin.getX() + (i / graphAccuracy) - (graphRadius / 2);
                        e = e.replace("x²", Double.toString(t * t)).replace("exp", "rais").replace("x^2", Double.toString(t * t)).replace("x", Double.toString(t)).replace("e", Double.toString(Math.E)).replace("π", Double.toString(Math.PI));
                        e = FunctionCalculator.calculateFunction(e);
                        if (!e.contains("i") && !e.contains("NaN")) {
                            e = ExpressionParser.splitOperator(e);
                            StringCalculator calculator;
                            calculator = new StringCalculator();
                            double y = calculator.makeResult(e) + 0.5;
                            if (NumberParser.isNotDouble(Double.toString(y))) {
                                p.sendMessage(notExpression);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    p.sendMessage(notExpression);
                    break;
                } graphExpression = s;
                p.sendMessage(Main.INDEX + "그래프의 식을 §by=" + graphExpression + "§f(으)로 설정했습니다.");
            } case "toggle" -> {
                if (graphExpression == null || graphExpression.isEmpty()) {
                    p.sendMessage(Main.INDEX + "§c그래프의 식이 비어 있습니다.");
                } else if (graphVisible) {
                    graphVisible = false; isMinusLogged = false; isMinusSqrted = false; isConstantFunction = false;
                    p.hideBossBar(graphBar);
                    graphBar.progress(0F);
                    graphBar.name(Component.text("§7로딩 중..."));
                    p.sendMessage(Main.INDEX + "더 이상 그래프를 표시하지 않습니다.");
                } else {
                    graphBar.progress(0F);
                    p.showBossBar(graphBar);
                    p.sendMessage(Main.INDEX + "§by=" + graphExpression + "§f에 대한 그래프를 표시합니다.");
                    p.showTitle(Title.title(Component.text("§by=" + graphExpression + " §a표시 중"), Component.text(""), Title.Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(3L), Duration.ofSeconds(1L))));
                    for (double i = 0.0; i < graphRadius*graphAccuracy; i++) {
                        graphBar.progress(Float.parseFloat(Double.toString(i / (graphRadius * graphAccuracy))));
                        graphVisible = true; Particle.DustTransition d;
                        String e = ChatColor.stripColor(graphExpression);
                        if (e.contains("x")) d = new Particle.DustTransition(Color.RED, Color.RED, graphSize);
                        else {
                            isConstantFunction = true;
                            d = new Particle.DustTransition(Color.BLUE, Color.BLUE, graphSize);
                        } final double x = graphOrigin.getX()-(graphRadius/2)+(i/graphAccuracy);
                        e = e.replace("exp", "rais").replace("x²", Double.toString(x*x)).replace("x^2", Double.toString(x*x)).replace("x", Double.toString(x)).replace("e", Double.toString(Math.E)).replace("π", Double.toString(Math.PI));
                        e = FunctionCalculator.calculateFunction(e);
                        if (e.contains("iπ")) isMinusLogged = true;
                        if (e.replace("iπ", "").contains("i")) isMinusSqrted = true;
                        if (!e.contains("i") && !e.contains("NaN")) {
                            e = ExpressionParser.splitOperator(e);
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
                    } graphBar.name(Component.text("§by=" + graphExpression + " §a표시 중"));
                    if (isConstantFunction) p.sendMessage(Main.INDEX + "§7상수함수의 그래프를 표시하고 있습니다.");
                    else if (isMinusSqrted && isMinusLogged) p.sendMessage(Main.INDEX + "음수의 제곱근과 로그는 무시되었습니다.");
                    else if (isMinusSqrted) p.sendMessage(Main.INDEX + "음수의 제곱근은 무시되었습니다.");
                    else if (isMinusLogged) p.sendMessage(Main.INDEX + "음수의 로그는 무시되었습니다.");
                }
            } default -> p.sendMessage(Component.text(Main.INDEX + "§c알 수 없는 명령어입니다: §4" + args[0] + "\n" + Main.INDEX + "§6명령어 목록을 보려면 §e/graph help§6를 입력하세요.").clickEvent(ClickEvent.runCommand("/graph help")));
        }
    }
}
