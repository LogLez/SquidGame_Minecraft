package io.hysekai.game.squidgame.utils;

import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class UtilItem extends ItemStack {


    public UtilItem() {
    }

    public UtilItem(ItemStack s) {
        this.setType(s.getType());
        this.setDurability(s.getDurability());
        if (s.getItemMeta() != null) {
            this.setItemMeta(s.getItemMeta());
        }
        this.setAmount(s.getAmount());
    }



    public static UtilItem fromBlock(Block block) {
        return new UtilItem().createItem(block.getType(), 1, block.getData());
    }

    public UtilItem clone() {
        UtilItem itemStack = (UtilItem) super.clone();
        if (this.getItemMeta() != null) {
            itemStack.setItemMeta(this.getItemMeta().clone());
        }

        if (this.getData() != null) {
            itemStack.setData(this.getData().clone());
        }

        return itemStack;
    }

    public UtilItem setAmount2(int value) {
        this.setAmount(value);
        return this;
    }

    public UtilItem createItem(Material material, int amount, int data) {
        this.setType(material);
        this.setAmount(amount);
        this.setDurability((short) data);
        return this;
    }

    public UtilItem createItem(Material material, int amount, MaterialData data) {
        this.setType(material);
        this.setAmount(amount);
        this.setData(data);
        return this;
    }

    public UtilItem createItem(Material material) {
        this.setType(material);
        this.setAmount(1);
        return this;
    }



    public String getName() {
        return this.getItemMeta().getDisplayName();
    }

    public UtilItem setName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        this.setItemMeta(meta);
        return this;
    }

    public UtilItem setBannerColor(DyeColor dyeColor ) {
        if(this.getType() == Material.BANNER) return this;
        BannerMeta meta = (BannerMeta) getItemMeta();
        meta.setBaseColor(dyeColor);
        this.setItemMeta(meta);
        return this;
    }
    public UtilItem setArmorColor(org.bukkit.Color dyeColor ) {
        if(!this.getType().toString().contains("LEATHER")) return this;
        LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
        meta.setColor(dyeColor);
        this.setItemMeta(meta);
        return this;
    }

    public UtilItem addLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        if (getLore() != null) {
            List<String> defaultLore = getLore();
            defaultLore.addAll(lore);
            if (lore != null)
                meta.setLore(defaultLore);
            this.setItemMeta(meta);
        } else
            return setLore(lore);
        return this;
    }

    public UtilItem addLore(String... lore) {
        ItemMeta meta = getItemMeta();
        if (getLore() != null) {
            ArrayList<String> list = new ArrayList<String>();
            list.addAll(getLore());
            if (lore.length > 0) {
                for (String line : lore) {
                    list.add(ChatColor.translateAlternateColorCodes('&', line));
                }
            }
            if (lore.length > 0) {
                meta.setLore(list);
            }
            this.setItemMeta(meta);
        } else
            return setLore(lore);
        return this;
    }

    public List<String> getLore() {
        return this.getItemMeta().getLore();
    }

    public UtilItem setLore(String... lore) {
        ItemMeta meta = getItemMeta();
        ArrayList<String> list = new ArrayList<String>();
        if (lore.length > 0) {
            for (String line : lore) {
                if (line != null)
                    list.add(line.replaceAll("&", "§"));
            }
        }
        if (lore.length > 0) {
            meta.setLore(list);
        }
        this.setItemMeta(meta);
        return this;
    }

    public UtilItem setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        if (lore != null)
            meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }


    public UtilItem addEnchantement(int level, Enchantment... enchant) {
        for (Enchantment ench : enchant) {
            this.addUnsafeEnchantment(ench, level);
        }
        return this;
    }

    public UtilItem setOwner(OfflinePlayer off) {
        return setOwner(off.getName());
    }



    public UtilItem setOwner(String player) {
        try {
            if (!player.equals(null)) {
                if (this.getType().equals(Material.SKULL_ITEM) && this.getDurability() == 3) {
                    SkullMeta meta = (SkullMeta) getItemMeta();
                    meta.setOwner(player);
                    this.setItemMeta(meta);
                }
            }
        } catch (Exception e) {
        }
        return this;
    }

    public UtilItem translateColor(byte color) {
        switch (color) {
            case 0:
                setColor(25, 25, 25);
                break;
            case 1:
                setColor(153, 51, 51);
                break;
            case 2:
                setColor(102, 127, 51);
                break;
            case 3:
                setColor(102, 76, 51);
                break;
            case 4:
                setColor(51, 76, 178);
                break;
            case 5:
                setColor(127, 63, 178);
                break;
            case 6:
                setColor(76, 127, 153);
                break;
            case 7:
                setColor(153, 153, 153);
                break;
            case 8:
                setColor(76, 76, 76);
                break;
            case 9:
                setColor(242, 127, 165);
                break;
            case 10:
                setColor(127, 204, 25);
                break;
            case 11:
                setColor(229, 229, 51);
                break;
            case 12:
                setColor(102, 153, 216);
                break;
            case 13:
                setColor(178, 76, 216);
                break;
            case 14:
                setColor(216, 127, 51);
                break;
            case 15:
                setColor(255, 255, 255);
                break;
            default:
                break;
        }
        return this;
    }

    public UtilItem setColor(String string) {
        if (!string.equals("null")) {
            setColor(Integer.parseInt(string.split(" ")[0]), Integer.parseInt(string.split(" ")[1]),
                    Integer.parseInt(string.split(" ")[2]));
        }
        return this;
    }



    public UtilItem addPage(String... string) {
        if (this.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) this.getItemMeta();
            meta.addPage(string);
            this.setItemMeta(meta);
        }
        return this;
    }

    public UtilItem setAuthor(String name) {
        if (this.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) this.getItemMeta();
            meta.setAuthor(name);
            this.setItemMeta(meta);
        }
        return this;
    }

    public UtilItem setTitle(String name) {
        if (this.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) this.getItemMeta();
            meta.setTitle(name);
            this.setItemMeta(meta);
        }
        return this;
    }

    public UtilItem setColor(int red, int green, int blue) {
        String redd = "" + red;
        String greenn = "" + green;
        String bluee = "" + blue;
        try {
            if (!redd.equals(null) && !greenn.equals(null) && !bluee.equals(null)) {
                if (this.getType().equals(Material.LEATHER_BOOTS) || this.getType().equals(Material.LEATHER_LEGGINGS)
                        || this.getType().equals(Material.LEATHER_CHESTPLATE)
                        || this.getType().equals(Material.LEATHER_HELMET)) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
                    meta.setColor(Color.fromRGB(red, green, blue));
                    this.setItemMeta(meta);
                }
            }
        } catch (Exception e) {
        }

        return this;
    }


    public void setGlowing() {
        if (this.getEnchantments() != null)  setGlowing();

    }

    public UtilItem removePotionLore() {
        ItemStack stack = this;
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.setItemMeta(meta);
        return this;
    }

    public ItemStack setGlowingRecord() {
        ItemStack stack = this;
        /*
         * if (this.getEnchantments() != null) stack = glow(this);
         */
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        this.setItemMeta(meta);
        return stack;
    }

    public UtilItem hideEnchants() {
        UtilItem stack = this;
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.setItemMeta(meta);
        return stack;
    }

    public UtilItem removeAttributes() {
        UtilItem stack = this;
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        this.setItemMeta(meta);
        return stack;
    }

    public UtilItem setDurability(int dura) {
        this.setDurability((short) dura);
        return this;
    }

    public UtilItem setUnbreakable() {
        this.setUnbreakable();
        return this;

    }


}
