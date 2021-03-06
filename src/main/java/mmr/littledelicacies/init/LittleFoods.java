package mmr.littledelicacies.init;

import net.minecraft.item.Food;

public class LittleFoods {
    public static final Food APPLE_JUICE = (new Food.Builder()).hunger(1).saturation(0.32F).setAlwaysEdible().build();
    public static final Food TEA = (new Food.Builder()).hunger(0).saturation(0.02F).setAlwaysEdible().build();

    public static final Food CARAMEL_APPLE = (new Food.Builder()).hunger(4).saturation(0.32F).build();
    public static final Food PIE = (new Food.Builder()).hunger(6).saturation(0.38F).build();
    public static final Food CAKE = (new Food.Builder()).hunger(5).saturation(0.35F).build();
    public static final Food DONUT = (new Food.Builder()).hunger(4).saturation(0.36F).build();
    public static final Food POCKY = (new Food.Builder()).hunger(3).saturation(0.275F).fastToEat().build();
    public static final Food COOKIE = (new Food.Builder()).hunger(2).saturation(0.26F).fastToEat().build();
    public static final Food DOLL_COOKIE = (new Food.Builder()).hunger(3).saturation(0.28F).build();
    public static final Food MACAROON = (new Food.Builder()).hunger(3).saturation(0.24F).fastToEat().build();
    public static final Food DAIFUKU = (new Food.Builder()).hunger(3).saturation(0.28F).build();
    public static final Food TAIYAKI = (new Food.Builder()).hunger(4).saturation(0.29F).build();
    public static final Food ICECREAM_TAIYAKI = (new Food.Builder()).hunger(4).saturation(0.29F).build();
}
