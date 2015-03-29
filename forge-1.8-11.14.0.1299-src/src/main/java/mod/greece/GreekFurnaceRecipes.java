//package mod.greece;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.minecraft.item.ItemStack;
//
//public class GreekFurnaceRecipes {
//	private static final GreekFurnaceRecipes spells = new GreekFurnaceRecipes();
//	// This creates a HashMap whose Key is a specific, ordered List of Integers
//	// If you want multiple outputs from one recipe, change the ItemStack to an ItemStack[]
//	// (and of course adjust your TileEntity and Container code)
//	private HashMap<List<Integer>, ItemStack> metaInscribingList = new HashMap<List<Integer>, ItemStack>();
//	// Same as above except it gives us the experience for each crafting result
//	private HashMap<List<Integer>, Float> metaExperience = new HashMap<List<Integer>, Float>();
//
//	/**
//	 * Used to call methods addInscribing and getInscribingResult.
//	 */
//	public static final GreekFurnaceRecipes spells() {
//		return spells;
//	}
//
//	/**
//	 * Adds all recipes to the HashMap
//	 */
//	private GreekFurnaceRecipes()
//	{
//		// Note that I have defined constant integer values in my ItemRune class
//		// such that ItemRune.RUNE_NAME returns the appropriate int value for the
//		// corresponding metadata
//
//		/*// This one only takes 2 Items to craft:
//		this.addInscribing(Arrays.asList(ItemRune.RUNE_CREATE,ItemRune.RUNE_FIRE),new ItemStack(ArcaneLegacy.scrollCombust), 0.3F);
//
//		// This one takes 7 Items to craft (the max number of slots currently in my Arcane Inscriber, but I could easily add more):
//		this.addInscribing(Arrays.asList(ItemRune.RUNE_AUGMENT,ItemRune.RUNE_AUGMENT,ItemRune.RUNE_CREATE,ItemRune.RUNE_AUGMENT,ItemRune.RUNE_LIFE,ItemRune.RUNE_SPACE,ItemRune.RUNE_TIME),new ItemStack(ArcaneLegacy.scrollHealAuraI), 1.0F);
//
//		// Here's a generic format for adding both item ID and metadata:
//		this.addInscribing(Arrays.asList(Item1.itemID, metadata1, Item2.itemID, metadata2, ... etc.), new ItemStack(craftResult.itemID, stacksize, metadata), XP);
//
//		// You could also skip the addInscribing method and add directly to the HashMap:
//		metaInscribingList.put(Arrays.asList(Item1.itemID, meta1, Item2.itemID, meta2,... ItemN.itemID, metaN), ItemStack(craftResult.itemID,stacksize,metadata));
//		// Note that XP must be added each time as well, effectively doubling the lines of code in this method
//		metaExperience.put(Arrays.asList(ItemResult.itemID, ItemResult.getItemDamage()), experience);*/
//	}
//
//	/**
//	 * Adds an array of runes, the resulting scroll, and experience given
//	 */
//	public void addInscribing(List<Integer> runes, ItemStack scroll, float experience)
//	{
//		// Check if recipe already exists and print conflict information:
//		if (metaInscribingList.containsKey(runes))
//		{
//			System.out.println("[WARNING] Conflicting recipe: " + runes.toString() + " for " + metaInscribingList.get(runes).toString());
//		}
//		else
//		{
//			// Add new recipe to the HashMap... wow, it looks so simple like this :)
//			metaInscribingList.put(runes, scroll);
//			//metaExperience.put(Arrays.asList(scroll.itemID, scroll.getItemDamage()), experience);
//			metaExperience.put(Arrays.asList(scroll.hashCode(), scroll.getItemDamage()), experience);
//		}
//	}
//
//	/**
//	 * Used to get the resulting ItemStack form a source inventory (fed to it by the contents of the slots in your container)
//	 * @param item The Source inventory from your custom furnace input slots
//	 * @return The result ItemStack (NOTE: if you want multiple outputs, change to ItemStack[] and adjust accordingly)
//	 */
//	public ItemStack getInscribingResult(ItemStack[] runes)
//	{
//		// count the recipe length so we can make the appropriate sized array
//		int recipeLength = 0;
//		for (int i = 0; i < runes.length && runes[i] != null && i < GreekFurnaceContainer.RUNE_SLOTS; ++i)
//		{
//			// +1 for metadata value of itemstack, add another +1 if you also need the itemID
//			++recipeLength;
//		}
//		// make the array and fill it with the integer values from the passed in ItemStacks
//		// Note that I'm only using the metadata value as all my runes have the same itemID
//		Integer[] idIndex = new Integer[recipeLength];
//		for (int i = 0; i < recipeLength; ++i) {
//			// if you need itemID as well put this:
//			// idIndex[i] = (Integer.valueOf(runes[i].itemID));
//			// be sure to increment i before you do the metadata if you added an itemID
//			idIndex[i] = (Integer.valueOf(runes[i].getItemDamage()));
//		}
//		// And use it as the key to get the correct result from the HashMap:
//		return (ItemStack) metaInscribingList.get(Arrays.asList(idIndex));
//	}
//
//	/**
//	 * Grabs the amount of base experience for this item to give when pulled from the furnace slot.
//	 */
//	public float getExperience(ItemStack item)
//	{
//		if (item == null || item.getItem() == null)
//		{
//			return 0;
//		}
//		float ret = -1; // value returned by "item.getItem().getSmeltingExperience(item);" when item doesn't specify experience to give
//		/*if (ret < 0 && metaExperience.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
//		{
//			ret = metaExperience.get(Arrays.asList(item.itemID, item.getItemDamage()));
//		}*/
//		if (ret < 0 && metaExperience.containsKey(Arrays.asList(item, item.getItemDamage())))
//		{
//			ret = metaExperience.get(Arrays.asList(item, item.getItemDamage()));
//		}
//
//		return (ret < 0 ? 0 : ret);
//	}
//
//	public Map<List<Integer>, ItemStack> getMetaInscribingList()
//	{
//		return metaInscribingList;
//	}
//}