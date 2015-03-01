package mod.greece;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Greece.MODID, version = Greece.VERSION)
public class Greece
{
	public static final String MODID = "greece";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide="mod.greece.client.ClientProxy", serverSide="mod.greece.CommonProxy")
	public static CommonProxy proxy;

	// BLOCKS
	public static Block marble = new GreekBlock(Material.rock)
	.setHardness(0.5F).setStepSound(Block.soundTypeStone)
	.setUnlocalizedName("marbleBlock").setCreativeTab(CreativeTabs.tabBlock);
	
	/*public static Block scroll_shelf = new GreekBlockScrollshelf(Material.wood)
	.setHardness(0.5F).setStepSound(Block.soundTypeWood)
	.setUnlocalizedName("scrollShelf").setCreativeTab(CreativeTabs.tabBlock);*/

	public static Block papyrusPlantBlock = new GreekBlockPapyrusPlant();

	// ITEMS
	public static Item papyrusPlantItem = new GreekItemPapyrusPlant(papyrusPlantBlock)
	.setUnlocalizedName("papyrusPlant").setCreativeTab(CreativeTabs.tabMisc);

	public static Item papyrus = new GreekItem()
	.setUnlocalizedName("papyrus").setCreativeTab(CreativeTabs.tabMisc);


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// REGISTER BLOCKS
		GameRegistry.registerBlock(marble, marble.getUnlocalizedName());
		//GameRegistry.registerBlock(scroll_shelf, scroll_shelf.getUnlocalizedName());
		GameRegistry.registerBlock(papyrusPlantBlock, papyrusPlantBlock.getUnlocalizedName());

		// REGISTER ITEMS
		GameRegistry.registerItem(papyrusPlantItem, papyrusPlantItem.getUnlocalizedName());
		GameRegistry.registerItem(papyrus, papyrus.getUnlocalizedName());

		// RECIPES
		GameRegistry.addRecipe(new ItemStack(Blocks.diamond_block), new Object[]{
			"xx ",
			"xx ",
			"xx ",
			'x', Blocks.dirt
		});

		GameRegistry.addRecipe(new ItemStack(papyrus, 3), new Object[]{
			"xxx",
			'x', papyrusPlantItem
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
