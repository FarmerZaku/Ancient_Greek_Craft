package mod.greece;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;


@Mod(modid = Greece.MODID, name = Greece.NAME, version = Greece.VERSION)
public class Greece
{
	public static final String MODID = "greece";
	public static final String NAME = "AncientGreekCraft";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide="mod.greece.client.ClientProxy", serverSide="mod.greece.CommonProxy")
	public static CommonProxy proxy;
	
	//---------MATERIALS---------
	public static ToolMaterial bronze = EnumHelper.addToolMaterial("Bronze", 2, 200, 5.0F, 2.0F, 12);
	public static ToolMaterial clay = EnumHelper.addToolMaterial("Clay", 1, 100, 3.0F, 0.5F, 12);
	public static ToolMaterial copper = EnumHelper.addToolMaterial("Copper", 1, 100, 3.0F, 1.0F, 18);

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
	
	public static Block multiFurnace = new GreekBlockFurnace(false)
	.setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("blockMultiFurnace");

	// ITEMS
	public static Item papyrusPlantItem = new GreekItemPapyrusPlant(papyrusPlantBlock);
	public static Item papyrus = new GreekItem()
	.setUnlocalizedName("itemPapyrus").setCreativeTab(CreativeTabs.tabMisc);
	
	public static Item bakingCover = new GreekItemTool(clay, 15).setUnlocalizedName("itemBakingCover");
	public static Item chisel = new GreekItemTool(bronze, 12).setUnlocalizedName("itemChisel");
	//public static Item fryingPanUnfired = new GreekItem(clay).setUnlocalizedName("itemFryingPanCeramic");
	public static Item fryingPanCeramic = new GreekItemTool(clay, 20).setUnlocalizedName("itemFryingPanCeramic");
	public static Item fryingPanBronze = new GreekItemTool(bronze, 20).setUnlocalizedName("itemFryingPanBronze");
	
	public static Item plasterBucket = new GreekItem()
	.setUnlocalizedName("itemPlasterBucket").setCreativeTab(CreativeTabs.tabMisc);
	public static Item basketEmpty = new GreekItem()
	.setUnlocalizedName("itemBasketEmpty").setCreativeTab(CreativeTabs.tabMisc);
	public static Item basketGrain = new GreekItem()
	.setUnlocalizedName("itemBasketGrain").setCreativeTab(CreativeTabs.tabMisc);
	public static Item basketFlour = new GreekItem()
	.setUnlocalizedName("itemBasketFlour").setCreativeTab(CreativeTabs.tabMisc);
	
	
	// amphoras
	public static Item amphoraUnfired = new GreekItem()
	.setUnlocalizedName("itemAmphoraUnfired").setCreativeTab(CreativeTabs.tabMisc);
	public static Item amphora = new GreekItem()
	.setUnlocalizedName("itemAmphora").setCreativeTab(CreativeTabs.tabMisc);
	public static Item amphoraGrain = new GreekItem()
	.setUnlocalizedName("itemAmphoraGrain").setCreativeTab(CreativeTabs.tabMisc);
	public static Item amphoraFlour = new GreekItem()
	.setUnlocalizedName("itemAmphoraFlour").setCreativeTab(CreativeTabs.tabMisc);
	public static Item amphoraWine = new GreekItem()
	.setUnlocalizedName("itemAmphoraWine").setCreativeTab(CreativeTabs.tabMisc);
	public static Item amphoraOil = new GreekItem()
	.setUnlocalizedName("itemAmphoraOil").setCreativeTab(CreativeTabs.tabMisc);
	
	public static Item straw = new GreekItem()
	.setUnlocalizedName("itemStraw").setCreativeTab(CreativeTabs.tabMisc);
	public static Item dough = new GreekItem()
	.setUnlocalizedName("itemDough").setCreativeTab(CreativeTabs.tabMisc);


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// REGISTER BLOCKS
		// Don't use getUnlocalizedName() here for the block name, apparently it will prepend like "tile." in front?
		// or at least I think that's what was breaking it. Anyhow, passing a string works.
		GameRegistry.registerBlock(marble, "blockMarble");
		GameRegistry.registerBlock(marbleBrick, "blockMarbleBrick");
		GameRegistry.registerBlock(papyrusPlantBlock, "blockPapyrusPlant");
		GameRegistry.registerBlock(scroll_shelf, "blockScrollShelf");
		GameRegistry.registerBlock(multiFurnace, "blockMultiFurnace");

		// REGISTER ITEMS
		GameRegistry.registerItem(papyrus, "itemPapyrus");
		GameRegistry.registerItem(papyrusPlantItem, "itemPapyrusPlant");
		GameRegistry.registerItem(chisel, "itemChisel");
		GameRegistry.registerItem(bakingCover, "itemBakingCover");
		
		// amphoras
		GameRegistry.registerItem(amphoraUnfired, "itemAmphoraUnfired");
		GameRegistry.registerItem(amphora, "itemAmphora");
		GameRegistry.registerItem(amphoraGrain, "itemAmphoraGrain");
		GameRegistry.registerItem(amphoraFlour, "itemAmphoraFlour");
		GameRegistry.registerItem(amphoraWine, "itemAmphoraWine");
		GameRegistry.registerItem(amphoraOil, "itemAmphoraOil");
		GameRegistry.addRecipe(new ItemStack(amphoraUnfired), "a a", "aaa", " a ",
        		'a', Item.getByNameOrId("clay"));
		GameRegistry.addSmelting(amphoraUnfired, new ItemStack(amphora), 1);

		// RECIPES
		GameRegistry.addRecipe(new ItemStack(Greece.marbleBrick, 4), new Object[]{
			"xx ",
			"xx ",
			"y  ",
			'x', marble, 'y', new ItemStack(chisel, 1, OreDictionary.WILDCARD_VALUE)
		});

		GameRegistry.addRecipe(new ItemStack(papyrus, 3), new Object[]{
			"xxx",
			'x', papyrusPlantItem
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
		
		//register event handlers
		//MinecraftForge.EVENT_BUS.register(new GreekCraftingHandler()); // wrong bus for this event
		FMLCommonHandler.instance().bus().register(new GreekCraftingHandler());
		
		//register tile entities
		GameRegistry.registerTileEntity(GreekFurnaceTileEntity.class, "MultiFurnace");
		
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
	    	renderItem.getItemModelMesher().register(chisel, 0, new ModelResourceLocation("greece:itemChisel", "inventory"));
	    	renderItem.getItemModelMesher().register(bakingCover, 0, new ModelResourceLocation("greece:itemBakingCover", "inventory"));
	    	renderItem.getItemModelMesher().register(amphoraUnfired, 0, new ModelResourceLocation("greece:itemAmphoraUnfired", "inventory"));
	    	renderItem.getItemModelMesher().register(amphora, 0, new ModelResourceLocation("greece:itemAmphora", "inventory"));
	    	renderItem.getItemModelMesher().register(amphoraGrain, 0, new ModelResourceLocation("greece:itemAmphoraGrain", "inventory"));
	    	renderItem.getItemModelMesher().register(amphoraFlour, 0, new ModelResourceLocation("greece:itemAmphoraFlour", "inventory"));
	    	renderItem.getItemModelMesher().register(amphoraWine, 0, new ModelResourceLocation("greece:itemAmphoraWine", "inventory"));
	    	renderItem.getItemModelMesher().register(amphoraOil, 0, new ModelResourceLocation("greece:itemAmphoraOil", "inventory"));
    	}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
