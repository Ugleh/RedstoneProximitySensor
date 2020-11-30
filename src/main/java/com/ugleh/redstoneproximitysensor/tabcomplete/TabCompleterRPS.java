package com.ugleh.redstoneproximitysensor.tabcomplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterRPS implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> list = new ArrayList<>();
        List<String> l  = new ArrayList<>();
        if(args.length <= 1) {
            list.add("give");
            list.add("list");
            list.add("reload");
            list.add("spawn");
            list.add("ignore");
            return list;
        }
        //rps <> []
        if(args.length <= 3) {
            //rps give <> [] []
            if(args[0].equalsIgnoreCase("give")) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }

                if(args.length == 2) {
                    for(String key : list) {
                        if(key.startsWith(args[1])) {
                            l.add(key);
                        }
                    }
                    list = l;
                }else {
                    l.add("1");
                    l.add("2");
                    l.add("3");
                    list = l;
                }
            }
            //rps give <> []
            if(args.length <= 2 && args[0].equalsIgnoreCase("list")) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }

                for(String key : list) {
                    if(key.startsWith(args[1])) {
                        l.add(key);
                    }
                }
                list = l;
            }
        }
        return list;
    }
}
