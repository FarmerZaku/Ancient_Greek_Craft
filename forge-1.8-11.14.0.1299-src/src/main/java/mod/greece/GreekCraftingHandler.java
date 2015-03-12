package mod.greece;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class GreekCraftingHandler {
	@SubscribeEvent
	public void onItemCraftedEvent(ItemCraftedEvent event) {
		//System.out.println("CRAFTED");
		//event.player.inventory.addItemStackToInventory((new ItemStack(Greece.papyrus, 1)));
		
		for(int i=0; i < event.craftMatrix.getSizeInventory(); i++)
		{        	
	    	if(event.craftMatrix.getStackInSlot(i) != null)
	    	{
	    		ItemStack j = event.craftMatrix.getStackInSlot(i);
	    		if (j.getItem() != null && j.getItem().getClass() == Greece.chisel.getClass() /*== Greece.chisel || j.getItem() == Greece.bakingCover)*/ && j.getItemDamage()+10 < j.getMaxDamage())
	    		{
	    			int oldDamage = j.getItemDamage(); 
	    			ItemStack k;
	    			
	    			// make a condition for all GreekTools
	    			GreekItemTool temp; // use this to getDamagePerUse. For some reason, Greece.chisel doesn't find this method
	    			int damagePerUse;
	    			if (j.getItem() == Greece.chisel) {		 
	    				temp = (GreekItemTool)Greece.chisel;
	    				damagePerUse = temp.getDamagePerUse();
	    				k = new ItemStack(Greece.chisel, 2, (j.getItemDamage() + damagePerUse));
	    			}
	    			else if (j.getItem() == Greece.bakingCover) {
	    				temp = (GreekItemTool)Greece.bakingCover;
	    				damagePerUse = temp.getDamagePerUse();
	    				k = new ItemStack(Greece.bakingCover, 2, (j.getItemDamage() + damagePerUse));
	    			}
	    			else if (j.getItem() == Greece.fryingPanCeramic) {
	    				temp = (GreekItemTool)Greece.fryingPanCeramic;
	    				damagePerUse = temp.getDamagePerUse();
	    				k = new ItemStack(Greece.fryingPanCeramic, 2, (j.getItemDamage() + damagePerUse));
	    			}
	    			else { // if (j.getItem() == Greece.fryingPanBronze) 
	    				temp = (GreekItemTool)Greece.fryingPanBronze;
	    				damagePerUse = temp.getDamagePerUse();
	    				k = new ItemStack(Greece.fryingPanBronze, 2, (j.getItemDamage() + damagePerUse));
	    			}
	    			//k.setItemDamage(oldDamage + 10);
	    			event.craftMatrix.setInventorySlotContents(i, k);
	    		}
	    		else if (j.getItem() != null && j.getItem().getClass() == Greece.chisel.getClass() && j.getItemDamage()+15 >= j.getMaxDamage())
	    			event.player.playSound("random.break", 1, 1);
	    		else if (event.crafting.getItem() == Greece.plasterBucket && j.getItem() != null & j.getItem() == Item.getByNameOrId("bucketWater")) {
	    			event.craftMatrix.setInventorySlotContents(i, null);
	    		} else if ((event.crafting.getItem() == Greece.basketGrain || event.crafting.getItem() == Greece.amphoraGrain) 
	    				&& j.getItem() != null & j.getItem() == Item.getByNameOrId("wheat")) {
	    			//Give 'em a straw item. If it doesn't fit in their inventory, drop it
	    			//if (!player.inventory.addItemStackToInventory(new ItemStack(Greece.straw))) {
	    				//event.player.dropPlayerItem(new ItemStack(Greece.straw));
	    				event.player.dropItem(Greece.straw, 1); // 1.8 -- is this equivalent to the above line?
	    			//}
	    		} else if (event.crafting.getItem() == Greece.dough && j.getItem() != null & j.getItem() == Greece.basketFlour) {
	    			event.craftMatrix.setInventorySlotContents(i, new ItemStack(Greece.basketEmpty, 2));
	    		} else if (event.crafting.getItem() == Greece.dough && j.getItem() != null & j.getItem() == Item.getByNameOrId("bucketWater")) {
	    			event.craftMatrix.setInventorySlotContents(i, new ItemStack(Item.getByNameOrId("bucketEmpty"), 2));
	    		} else if (event.crafting.getItem() == Greece.dough && j.getItem() != null & j.getItem() == Greece.amphoraFlour) {
	    			event.craftMatrix.setInventorySlotContents(i, new ItemStack(Greece.amphora, 2));
	    		}
	    	}  
		}
	}
}
	
	
	
	
	/* //TEMPLATE FOR HOW EVENTS WORK//
	// In your TutEventHandler class - the name of the method doesn't matter
	// Only the Event type parameter is what's important (see below for explanations of some types)
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		// This event has an Entity variable, access it like this:
		//event.entity;

		// do something to player every update tick:
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			ItemStack heldItem = player.getHeldItem();
			if (heldItem != null && heldItem == new ItemStack(Item.getByNameOrId("arrow"))) {
				player.capabilities.allowFlying = true;
			}
			else {
				player.capabilities.allowFlying = player.capabilities.isCreativeMode ? true : false;
			}
		}
	}*/

