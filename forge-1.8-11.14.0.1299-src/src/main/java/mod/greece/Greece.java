package mod.greece;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = Greece.MODID, name = Greece.NAME, version = Greece.VERSION)
public class Greece
{
	public static final String MODID = "greece";
	public static final String NAME = "AncientGreekCraft";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide="mod.greece.client.ClientProxy", serverSide="mod.greece.CommonProxy")
	public static CommonProxy proxy;

	// BLOCKS
	public static Block marble = new GreekBlock(Material.rock)
	.setHardness(0.5F).setStepSound(Block.soundTypeStone)
	.setUnlocalizedName("blockMarble").setCreativeTab(CreativeTabs.tabBlock);
	
	public final static Block marbleBrick = new GreekBlock(Material.rock)
	.setHardness(4.0f).setStepSound(Block.soundTypeStone)
	.setUnlocalizedName("blockMarbleBrick").setCreativeTab(CreativeTabs.tabBlock);
	
	public static Block scroll_shelf = new GreekBlockScrollshelf(Material.wood)
	.setHardness(0.5F).setStepSound(Block.soundTypeWood)
	.setUnlocalizedName("blockScrollShelf").setCreativeTab(CreativeTabs.tabBlock);

	public static Block papyrusPlantBlock = new GreekBlockPapyrusPlant();

	// ITEMS
	public static Item papyrusPlantItem = new GreekItemPapyrusPlant(papyrusPlantBlock);

	public static Item papyrus = new GreekItem()
	.setUnlocalizedName("itemPapyrus").setCreativeTab(CreativeTabs.tabMisc);


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// REGISTER BLOCKS
		// Don't use getUnlocalizedName() here for the block name, apparently it will prepend like "tile." in front?
		// or at least I think that's what was breaking it. Anyhow, passing a string works.
		GameRegistry.registerBlock(marble, "blockMarble");
		GameRegistry.registerBlock(marbleBrick, "blockMarbleBrick");
		GameRegistry.registerBlock(papyrusPlantBlock, "blockPapyrusPlant");
		GameRegistry.registerBlock(scroll_shelf, "blockScrollShelf");
		

		// REGISTER ITEMS
		GameRegistry.registerItem(papyrus, "itemPapyrus");
		GameRegistry.registerItem(papyrusPlantItem, "itemPapyrusPlant");

		// RECIPES
		GameRegistry.addRecipe(new ItemStack(Greece.marbleBrick, 4), new Object[]{
			"xx",
			"xx",
			//"xx ",
			'x', Greece.marble
		});

		GameRegistry.addRecipe(new ItemStack(papyrus, 3), new Object[]{
			"xxx",
			'x', papyrusPlantItem
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
		
		//register renders
    	if(event.getSide() == Side.CLIENT)
    	{
	    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	    
	    	//blocks
	    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(marble), 0, new ModelResourceLocation("greece:blockMarble", "inventory"));
	    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(marbleBrick), 0, new ModelResourceLocation("greece:blockMarbleBrick", "inventory"));
	    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(scroll_shelf), 0, new ModelResourceLocation("greece:blockScrollShelf", "inventory"));
	    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(papyrusPlantBlock), 0, new ModelResourceLocation("greece:blockPapyrusPlant", "inventory"));
	    
	    	//items
	    	renderItem.getItemModelMesher().register(papyrus, 0, new ModelResourceLocation("greece:itemPapyrus", "inventory"));
	    	renderItem.getItemModelMesher().register(papyrusPlantItem, 0, new ModelResourceLocation("greece:itemPapyrusPlant", "inventory"));
    	}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
