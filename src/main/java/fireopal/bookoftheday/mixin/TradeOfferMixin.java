package fireopal.bookoftheday.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static fireopal.bookoftheday.BookOfTheDay.CONFIG;

@Mixin(TradeOffer.class)
public class TradeOfferMixin {
	@Shadow @Mutable
	private ItemStack firstBuyItem;

	@Shadow @Mutable
	private ItemStack sellItem;

	@Shadow
	private int uses;

	@Inject(at = @At("HEAD"), method = "resetUses()V")
	private void resetUses(CallbackInfo info) {
		Random random = Random.create();

		if (sellItem.isOf(Items.ENCHANTED_BOOK) 
			&& ((uses > 0 && random.nextFloat() <= CONFIG.chance_to_reset_when_traded_with) 
				|| random.nextFloat() <= CONFIG.chance_to_reset_when_not_traded_with)
			) {

			List<Enchantment> list = Registry.ENCHANTMENT.stream()
				.filter(Enchantment::isAvailableForEnchantedBookOffer)
				.collect(Collectors.toList());

			Enchantment enchantment = list.get(random.nextInt(list.size()));
			int level = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());

			sellItem = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level));
		}

		//Triggered if CONFIG.chance_to_randomize_price is set to a value greater than 0;
		if (CONFIG.chance_to_randomize_price > 0 && random.nextFloat() <= CONFIG.chance_to_randomize_price) {
			int priceDifference = random.nextInt(CONFIG.max_price_difference_if_prices_randomized + 1 
				- CONFIG.min_price_difference_if_prices_randomized) + CONFIG.min_price_difference_if_prices_randomized;

			int newCost = firstBuyItem.getCount() + priceDifference;

			if (newCost > 0 && newCost < 64) {
				firstBuyItem.setCount(newCost);
			}
		}
	}
}