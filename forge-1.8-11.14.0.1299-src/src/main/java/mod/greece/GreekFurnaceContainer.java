package mod.greece;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GreekFurnaceContainer extends Container
{
    private final IInventory tileFurnace;
    private int field_178152_f;
    private int field_178153_g;
    private int field_178154_h;
    private int field_178155_i;
    private static final String __OBFID = "CL_00001748";

    public GreekFurnaceContainer(InventoryPlayer p_i45794_1_, IInventory furnaceInventory)
    {
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(p_i45794_1_.player, furnaceInventory, 2, 116, 35));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(p_i45794_1_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(p_i45794_1_, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting listener)
    {
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, this.tileFurnace);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.field_178152_f != this.tileFurnace.getField(2))
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
            }

            if (this.field_178154_h != this.tileFurnace.getField(0))
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
            }

            if (this.field_178155_i != this.tileFurnace.getField(1))
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
            }

            if (this.field_178153_g != this.tileFurnace.getField(3))
            {
                icrafting.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
            }
        }

        this.field_178152_f = this.tileFurnace.getField(2);
        this.field_178154_h = this.tileFurnace.getField(0);
        this.field_178155_i = this.tileFurnace.getField(1);
        this.field_178153_g = this.tileFurnace.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileFurnace.setField(id, data);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileFurnace.isUseableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }
}


//package mod.greece;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraft.inventory.Container;
//import net.minecraft.inventory.ICrafting;
//import net.minecraft.inventory.Slot;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class GreekFurnaceContainer extends Container
//{
//	private GreekFurnaceTileEntity inscriber;
//	private int lastProgressTime;
//	private int lastBurnTime;
//	private int lastItemBurnTime;
//
//	// NOTE that here you could add as many slots as you want.
//	// I have a complementary discharge slot array because the items I use as fuel
//	// are rechargeable and I want to get them back
//
//	// The way I use INPUT slots is both for fuel AND to determine the output result
//	// You'll probably just want to use it for the latter, in which case you'll need to add a FUEL slot
//	public static final int INPUT[] = {0,1,2,3,4,5,6};
//
//	// delete the DISCHARGE slots if you don't need to keep your used up fuel
//	public static final int DISCHARGE[] = {7,8,9,10,11,12,13};
//
//	// BLANK_SCROLL is NOT fuel, but a final requirement to actually inscribe a scroll. This allows me
//	// to set up a recipe in the rune slots and see what it is first before actually consuming the runes. You
//	// probably would change this slot to store FUEL instead, like most furnaces do.
//
//	// RECIPE just stores the current recipe so I can keep inscribing scrolls even after the runes are used
//	// up and to display the recipe on screen. You probably don't need this slot.
//
//	public static final int RUNE_SLOTS = INPUT.length, BLANK_SCROLL = RUNE_SLOTS*2, RECIPE = BLANK_SCROLL+1, OUTPUT = RECIPE+1, INV_START = OUTPUT+1, INV_END = INV_START+26, HOTBAR_START = INV_END+1, HOTBAR_END= HOTBAR_START+8;
//
//	public GreekFurnaceContainer(InventoryPlayer inventoryPlayer, GreekFurnaceTileEntity par2GreekFurnaceTileEntity)
//	{
//		int i;
//
//		this.inscriber = par2GreekFurnaceTileEntity;
//
//		// ADD CUSTOM SLOTS
//		for (i = 0; i < RUNE_SLOTS; ++i) {
//			this.addSlotToContainer(new Slot(par2GreekFurnaceTileEntity, INPUT[i], 43 + (18*i), 15));
//		}
//		for (i = 0; i < RUNE_SLOTS; ++i) {
//			this.addSlotToContainer(new SlotArcaneInscriberDischarge(par2GreekFurnaceTileEntity, DISCHARGE[i], 43 + (18*i), 64));
//		}
//
//		this.addSlotToContainer(new Slot(par2GreekFurnaceTileEntity, BLANK_SCROLL, 63, 39));
//		this.addSlotToContainer(new SlotArcaneInscriberRecipe(par2GreekFurnaceTileEntity, RECIPE, 17, 35));
//		this.addSlotToContainer(new GreekFurnaceSlot(inventoryPlayer.player, par2GreekFurnaceTileEntity, OUTPUT, 119, 39));
//
//		// ADD PLAYER INVENTORY
//		for (i = 0; i < 3; ++i)
//		{
//			for (int j = 0; j < 9; ++j)
//			{
//				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//			}
//		}
//
//		// ADD PLAYER ACTION BAR
//		for (i = 0; i < 9; ++i)
//		{
//			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
//		}
//	}
//
//	public void addCraftingToCrafters(ICrafting iCrafting)
//	{
//		super.addCraftingToCrafters(iCrafting);
//		iCrafting.sendProgressBarUpdate(this, 0, this.inscriber.inscribeProgressTime);
//	}
//
//	/**
//	 * Looks for changes made in the container, sends them to every listener.
//	 */
//	public void detectAndSendChanges()
//	{
//		super.detectAndSendChanges();
//
//		for (int i = 0; i < this.crafters.size(); ++i)
//		{
//			ICrafting icrafting = (ICrafting)this.crafters.get(i);
//
//			if (this.lastProgressTime != this.inscriber.inscribeProgressTime)
//			{
//				icrafting.sendProgressBarUpdate(this, 0, this.inscriber.inscribeProgressTime);
//			}
//		}
//
//		this.lastProgressTime = this.inscriber.inscribeProgressTime;
//	}
//
//	@SideOnly(Side.CLIENT)
//	public void updateProgressBar(int par1, int par2)
//	{
//		if (par1 == 0)
//		{
//			this.inscriber.inscribeProgressTime = par2;
//		}
//	}
//
//	@Override
//	public boolean canInteractWith(EntityPlayer entityplayer)
//	{
//		return this.inscriber.isUseableByPlayer(entityplayer);
//	}
//
//	/**
//	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
//	 */
//	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
//	{
//		ItemStack itemstack = null;
//		Slot slot = (Slot)this.inventorySlots.get(par2);
//
//		if (slot != null && slot.getHasStack())
//		{
//			ItemStack itemstack1 = slot.getStack();
//			itemstack = itemstack1.copy();
//
//			// If item is in TileEntity inventory
//			if (par2 < INV_START)
//			{
//				// try to place in player inventory / action bar
//				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, true))
//				{
//					return null;
//				}
//
//				slot.onSlotChange(itemstack1, itemstack);
//			}
//			// Item is in player inventory, try to place in inscriber
//			else if (par2 > OUTPUT)
//			{
//				// if it is a charged rune, place in the first open input slot
//				if (GreekFurnaceTileEntity.isSource(itemstack1))
//				{
//					if (!this.mergeItemStack(itemstack1, INPUT[0], INPUT[RUNE_SLOTS-1]+1, false))
//					{
//						return null;
//					}
//				}
//				// if it's a blank scroll, place in the scroll slot
//				//else if (itemstack1.itemID == ArcaneLegacy.scrollBlank.itemID)
//				else if (itemstack1 == new ItemStack(Greece.bakingCover))
//				{
//					if (!this.mergeItemStack(itemstack1, BLANK_SCROLL, BLANK_SCROLL+1, false))
//					{
//						return null;
//					}
//				}
//				// item in player's inventory, but not in action bar
//				else if (par2 >= INV_START && par2 < HOTBAR_START)
//				{
//					// place in action bar
//					if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END+1, false))
//					{
//						return null;
//					}
//				}
//				// item in action bar - place in player inventory
//				else if (par2 >= HOTBAR_START && par2 < HOTBAR_END+1 && !this.mergeItemStack(itemstack1, INV_START, HOTBAR_START, false))
//				{
//					return null;
//				}
//			}
//			// In one of the inscriber slots; try to place in player inventory / action bar
//			else if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, false))
//			{
//				return null;
//			}
//
//			if (itemstack1.stackSize == 0)
//			{
//				slot.putStack((ItemStack)null);
//			}
//			else
//			{
//				slot.onSlotChanged();
//			}
//
//			if (itemstack1.stackSize == itemstack.stackSize)
//			{
//				return null;
//			}
//
//			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
//		}
//		return itemstack;
//	}
//
//	/*@Override
//	public boolean canInteractWith(EntityPlayer playerIn) {
//		// TODO Auto-generated method stub
//		return false;
//	}*/
//}