package io.hysekai.game.squidgame.utils;

import org.bukkit.ChatColor;

public enum Colors {

    GREEN("Vert", ChatColor.GREEN, org.bukkit.Color.GREEN),
    DARK_RED("Rouge ",ChatColor.DARK_RED, org.bukkit.Color.RED),
    RED("Rouge",ChatColor.RED, org.bukkit.Color.GREEN),
    YELLOW("Yellow",ChatColor.YELLOW, org.bukkit.Color.YELLOW),
    AQUA("Aqua",ChatColor.AQUA, org.bukkit.Color.AQUA),
    WHITE("Blanche",ChatColor.WHITE, org.bukkit.Color.WHITE),
    BLACK("Noire",ChatColor.BLACK, org.bukkit.Color.BLACK),
    ORANGE("Orange",ChatColor.GOLD, org.bukkit.Color.ORANGE),
    DARK_GREEN("Vert",ChatColor.DARK_GREEN, org.bukkit.Color.GREEN),
    PURPLE("Violet",ChatColor.LIGHT_PURPLE, org.bukkit.Color.PURPLE),
    GREY("Gris",ChatColor.GRAY, org.bukkit.Color.GRAY),
    BLUE("Blue",ChatColor.BLUE, org.bukkit.Color.BLUE);

    private final String name;
    private final ChatColor chatColor;
    private final org.bukkit.Color color;

    Colors(String name, ChatColor chatColor, org.bukkit.Color color){
        this.name = name;
        this.chatColor = chatColor;
        this.color = color;
    }


    public String getName() {return name;}
    public org.bukkit.Color getColor() {return color;}
    public ChatColor getChatColor() {return chatColor;}
}
