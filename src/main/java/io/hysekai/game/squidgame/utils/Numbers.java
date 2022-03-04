package io.hysekai.game.squidgame.utils;

import fr.hysekai.schematic.schematic.Schematic;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Numbers {

    NUMBER_0(0,"", new Schematic("zero"), Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_1(1,"A", new Schematic("un"), Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT),
            new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_2(2,"B", new Schematic("deux"),Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_3(3,"C",new Schematic("trois"), Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE),
            new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER),
            new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL_MIRROR),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_4(4,"D", new Schematic("quatre"),Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT),
            new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_5(5,"E", new Schematic("cinq"),Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_6(6,"F", new Schematic("six"),Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT),
            new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_7(7,"G", new Schematic("sept"),Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_8(8,"H", new Schematic("huit"),Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_9(9, "I", new Schematic("neuf"), Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT),
            new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP),
            new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT),
            new Pattern(DyeColor.BLACK, PatternType.BORDER)
    )),
    NUMBER_10(10, "J",null, null),
    NUMBER_11(11,"K", null, null),
    NUMBER_12(12,"L", null, null),
    NUMBER_13(13,"M", null, null),
    NUMBER_14(14,"N", null, null),
    NUMBER_15(15,"O", null, null),
    NUMBER_16(16,"P", null, null),
    NUMBER_17(17,"Q", null, null),
    NUMBER_18(18,"R", null, null),
    NUMBER_19(19,"S", null, null),
    NUMBER_20(20,"T", null, null),
    NUMBER_21(21,"U", null, null),
    NUMBER_22(22,"V", null, null),
    NUMBER_23(23,"W", null, null),
    NUMBER_24(24,"X", null, null),
    NUMBER_25(25,"Y", null, null);


    private final int number;
    private final String data;
    private final Schematic schematic;
    private final List<Pattern> patterns;

    Numbers(int number, String data, Schematic schematic, List<Pattern> patterns){
        this.number = number;
        this.data = data;
        this.schematic = schematic;
        this.patterns = patterns;
    }


    public int getNumber() {return number;}
    public String getData() { return data; }
    public Schematic getSchematic() { return schematic; }
    public List<Pattern> getPatterns() {return patterns;}

    public static Optional<Numbers> getNumber(int i){
        return Arrays.stream(Numbers.values()).filter(numbers -> numbers.getNumber() == i).findFirst();
    }
    public static Optional<Numbers> getNumber(String data){
        return Arrays.stream(Numbers.values()).filter(numbers -> numbers.getData().equalsIgnoreCase(data)).findFirst();
    }


    public static Optional<Numbers> getNumber(char letter){
        try{
            int i = Integer.parseInt(String.valueOf(letter));
            return Arrays.stream(Numbers.values()).filter(numbers -> numbers.getNumber() == i).findFirst();
        }catch (NumberFormatException e){ }
        return Arrays.stream(Numbers.values()).filter(numbers -> numbers.getNumber() == 0).findFirst();
    }

}
