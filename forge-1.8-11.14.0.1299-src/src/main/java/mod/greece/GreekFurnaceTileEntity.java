package mod.greece;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GreekFurnaceTileEntity extends TileEntityLockable implements IUpdatePlayerListBox, ISidedInventory
{
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;
    private static final String __OBFID = "CL_00000357";

    public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

    public ItemStack getStackInSlot(int index)
    {
        return this.furnaceItemStacks[index];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.furnaceItemStacks[index] != null)
        {
            ItemStack itemstack;

            if (this.furnaceItemStacks[index].stackSize <= count)
            {
                itemstack = this.furnaceItemStacks[index];
                this.furnaceItemStacks[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.furnaceItemStacks[index].splitStack(count);

                if (this.furnaceItemStacks[index].stackSize == 0)
                {
                    this.furnaceItemStacks[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.furnaceItemStacks[index] != null)
        {
            ItemStack itemstack = this.furnaceItemStacks[index];
            this.furnaceItemStacks[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        boolean flag = stack != null && stack.isItemEqual(this.furnaceItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]);
        this.furnaceItemStacks[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.func_174904_a(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    public String getName()
    {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }

    public boolean hasCustomName()
    {
        return this.furnaceCustomName != null && this.furnaceCustomName.length() > 0;
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.furnaceCustomName = p_145951_1_;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = compound.getShort("BurnTime");
        this.cookTime = compound.getShort("CookTime");
        this.totalCookTime = compound.getShort("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

        if (compound.hasKey("CustomName", 8))
        {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.cookTime);
        compound.setShort("CookTimeTotal", (short)this.totalCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            if (this.furnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.furnaceCustomName);
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory p_174903_0_)
    {
        return p_174903_0_.getField(0) > 0;
    }

    public void update()
    {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (!this.isBurning() && (this.furnaceItemStacks[1] == null || this.furnaceItemStacks[0] == null))
            {
                if (!this.isBurning() && this.cookTime > 0)
                {
                    this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
                }
            }
            else
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (this.furnaceItemStacks[1] != null)
                        {
                            --this.furnaceItemStacks[1].stackSize;

                            if (this.furnaceItemStacks[1].stackSize == 0)
                            {
                                this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.func_174904_a(this.furnaceItemStacks[0]);
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }

            if (flag != this.isBurning())
            {
                flag1 = true;
                GreekBlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int func_174904_a(ItemStack p_174904_1_)
    {
        return 200;
    }

    private boolean canSmelt()
    {
        if (this.furnaceItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true;
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null)
            {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem() == Items.bucket)
            {
                this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0)
            {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    public static int getItemBurnTime(ItemStack p_145952_0_)
    {
        if (p_145952_0_ == null)
        {
            return 0;
        }
        else
        {
            Item item = p_145952_0_.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(p_145952_0_);
        }
    }

    public static boolean isItemFuel(ItemStack p_145954_0_)
    {
        return getItemBurnTime(p_145954_0_) > 0;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == 2 ? false : (index != 1 ? true : isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack));
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.water_bucket && item != Items.bucket)
            {
                return false;
            }
        }

        return true;
    }

    public String getGuiID()
    {
        return "minecraft:furnace";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        //return new ContainerFurnace(playerInventory, this);
    	return new GreekFurnaceContainer(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            this.furnaceItemStacks[i] = null;
        }
    }
}

//package mod.greece;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.ISidedInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.IChatComponent;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class GreekFurnaceTileEntity extends TileEntity implements ISidedInventory
//{
//	private static final int[] slots_top = new int[] {0};
//	private static final int[] slots_bottom = new int[] {2, 1};
//	private static final int[] slots_sides = new int[] {1};
//
//	/** Array bounds = number of slots in GreekFurnaceContainer */
//	private ItemStack[] inscriberInventory = new ItemStack[GreekFurnaceContainer.INV_START];
//
//	/** Time required to scribe a single scroll */
//	private static final int INSCRIBE_TIME = 100, RUNE_CHARGE_TIME = 400;
//
//	/** The number of ticks that the inscriber will keep inscribing */
//	public int currentInscribeTime;
//
//	/** The number of ticks that a charged rune will provide */
//	public int inscribeTime = 400;
//
//	/** The number of ticks that the current scroll has been inscribing for */
//	public int inscribeProgressTime;
//
//	private String displayName = "Arcane Inscriber";
//	private String furnaceCustomName;
//
//	public GreekFurnaceTileEntity() {
//	}
//
//	@Override
//	public int getSizeInventory() {
//		return inscriberInventory.length;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int slot) {
//		return this.inscriberInventory[slot];
//	}
//
//	@Override
//	public ItemStack decrStackSize(int slot, int amt)
//	{
//		ItemStack stack = getStackInSlot(slot);
//		if (stack != null) {
//			if (stack.stackSize <= amt) {
//				setInventorySlotContents(slot, null);
//			} else {
//				stack = stack.splitStack(amt);
//				if (stack.stackSize == 0) {
//					setInventorySlotContents(slot, null);
//				}
//			}
//		}
//		return stack;
//	}
//
//	@Override
//	public ItemStack getStackInSlotOnClosing(int slot)
//	{
//		ItemStack stack = getStackInSlot(slot);
//		if (stack != null) { setInventorySlotContents(slot, null); }
//		return stack;
//	}
//
//	@Override
//	public void setInventorySlotContents(int slot, ItemStack stack)
//	{
//		inscriberInventory[slot] = stack;
//		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
//			stack.stackSize = getInventoryStackLimit();
//		}
//	}
//
//	/*@Override // function unneeded?
//	public String getInvName() {
//		return this.isInvNameLocalized() ? this.displayName : "container.arcaneinscriber";
//	}*/
//
//	/**
//	 * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
//	 * language. Otherwise it will be used directly.
//	 */
//	public boolean isInvNameLocalized()
//	{
//		return this.displayName != null && this.displayName.length() > 0;
//	}
//
//	/**
//	 * Sets the custom display name to use when opening a GUI linked to this tile entity.
//	 */
//	public void setGuiDisplayName(String par1Str)
//	{
//		this.displayName = par1Str;
//	}
//
//	@Override
//	public int getInventoryStackLimit() {
//		return 64;
//	}
//
//	/**
//	 * Returns an integer between 0 and the passed value representing how
//	 * close the current item is to being completely cooked
//	 */
//	@SideOnly(Side.CLIENT)
//	public int getInscribeProgressScaled(int par1)
//	{
//		return this.inscribeProgressTime * par1 / INSCRIBE_TIME;
//	}
//
//	/**
//	 * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
//	 * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
//	 */
//	@SideOnly(Side.CLIENT)
//	public int getInscribeTimeRemainingScaled(int par1)
//	{
//		return this.currentInscribeTime * par1 / this.INSCRIBE_TIME;
//	}
//
//	/**
//	 * Returns true if the furnace is currently burning
//	 */
//	public boolean isInscribing()
//	{
//		return this.currentInscribeTime > 0;
//	}
//
//	/**
//	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
//	 * ticks and creates a new spawn inside its implementation.
//	 */
//	public void updateEntity()
//	{
//		boolean flag = this.currentInscribeTime > 0;
//		boolean flag1 = false;
//
//		if (this.currentInscribeTime > 0)
//		{
//			--this.currentInscribeTime;
//
//			// Container recipe doesn't match current non-null InscribingResult
//			flag1 = (this.inscriberInventory[GreekFurnaceContainer.RECIPE] != this.getCurrentRecipe() && this.getCurrentRecipe() != null);
//			// Recipe changed - reset timer and current recipe slot
//			if (flag1)
//			{
//				this.inscriberInventory[GreekFurnaceContainer.RECIPE] = this.getCurrentRecipe();
//				this.markDirty(); //this.onInventoryChanged()
//				this.currentInscribeTime = 0;
//			}
//		}
//
//		if (!this.worldObj.isRemote)
//		{
//			if (this.currentInscribeTime == 0)
//			{
//				flag1 = (this.inscriberInventory[GreekFurnaceContainer.RECIPE] != this.getCurrentRecipe());
//				this.inscriberInventory[GreekFurnaceContainer.RECIPE] = this.getCurrentRecipe();
//				// Recipe changed - update inventory
//				// (only because I'm showing the output for the current recipe on screen)
//				if (flag1) { this.markDirty(); //this.onInventoryChanged(); }
//
//				if (this.canInscribe()) {
//					// This is the equivalent of getItemBurnTime from furnace. Note again that I am setting
//					// my burn time based on an INPUT slot, even though this is generally done with FUEL
//					this.currentInscribeTime = this.getInscriberChargeTime(this.inscriberInventory[0]);
//				}
//
//				if (this.currentInscribeTime > 0)
//				{
//					flag1 = true;
//
//					// This is where you decrement your FUEL slot's inventory.
//					// However, since I use INPUT as FUEL and need to save the used up FUEL in DISCHARGE,
//					// I will use a for loop to decrement all of the inputs and increment all of the discharge slots
//					// Yours will probably look much simpler - look at the vanilla Furnace code to see an example
//					for (int i = 0; i < GreekFurnaceContainer.RUNE_SLOTS; ++i)
//					{
//						if (this.inscriberInventory[GreekFurnaceContainer.INPUT[i]] != null)
//						{
//							--this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].stackSize;
//							if (this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]] != null) {
//								++this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]].stackSize;
//							}
//							else {
//								//ItemStack discharge = new ItemStack(ArcaneLegacy.runeBasic,1,this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].getItemDamage());
//								ItemStack discharge = new ItemStack(Greece.bakingCover,1,this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].getItemDamage());
//								this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]] = discharge.copy();
//							}
//
//							if (this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].stackSize == 0)
//							{
//								//this.inscriberInventory[GreekFurnaceContainer.INPUT[i]] = this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].getItem().getContainerItemStack(inscriberInventory[GreekFurnaceContainer.INPUT[i]]);
//								this.inscriberInventory[GreekFurnaceContainer.INPUT[i]] = this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].getItem().getContainerItem(inscriberInventory[GreekFurnaceContainer.INPUT[i]]);
//							}
//						}
//					}
//				}
//			}
//
//			// Everything's good to go, so increment furnace progress until it reaches the required time to smelt your item(s)
//			if (this.isInscribing() && this.canInscribe())
//			{
//				++this.inscribeProgressTime;
//				if (this.inscribeProgressTime == INSCRIBE_TIME)
//				{
//					this.inscribeProgressTime = 0;
//					this.inscribeScroll();
//					flag1 = true;
//				}
//			}
//			else
//			{
//				this.inscribeProgressTime = 0;
//			}
//
//			if (flag != this.currentInscribeTime > 0)
//			{
//				flag1 = true;
//				// is this unnecessary?
//				//BlockArcaneInscriber.updateInscriberBlockState(this.currentInscribeTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
//			}
//		}
//
//		if (flag1) {
//			this.markDirty(); // this.onInventoryChanged();
//		}
//		}
//	}
//
//	/**
//	 * Returns true if the inscriber can inscribe a scroll;
//	 * i.e. has a blank scroll, has a charged rune, destination stack isn't full, etc.
//	 * This method will look very different for every furnace depending on whatever requirements
//	 * you decide are necessary for your furnace to smelt an item or items
//	 */
//	private boolean canInscribe()
//	{
//		boolean canInscribe = true;
//		// Still time remaining to inscribe current recipe
//		if (this.isInscribing() && this.inscriberInventory[GreekFurnaceContainer.RECIPE] != null)
//		{
//			canInscribe = (this.inscriberInventory[GreekFurnaceContainer.BLANK_SCROLL] == null ? false : true);
//		}
//		// No charged rune in first input slot
//		else if (this.inscriberInventory[GreekFurnaceContainer.INPUT[0]] == null)
//		{
//			canInscribe = false;
//		}
//		// No blank scrolls to inscribe
//		else if (this.inscriberInventory[GreekFurnaceContainer.BLANK_SCROLL] == null)
//		{
//			canInscribe = false;
//		}
//		// Check if any of the discharge slots are full
//		else
//		{
//			for (int i = 0; i < GreekFurnaceContainer.RUNE_SLOTS && canInscribe; ++i)
//			{
//				if (this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]] != null)
//				{
//					// Check if input[i] and discharge[i] are mismatched
//					if (this.inscriberInventory[GreekFurnaceContainer.INPUT[i]] != null)
//					{
//						canInscribe = ((this.inscriberInventory[GreekFurnaceContainer.INPUT[i]].getItemDamage() == this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]].getItemDamage())
//								&& this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]].stackSize < this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]].getMaxStackSize());
//					}
//					else
//					{
//						canInscribe = this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]].stackSize < this.inscriberInventory[GreekFurnaceContainer.DISCHARGE[i]].getMaxStackSize();
//					}
//				}
//			}
//		}
//
//		// If all of your above requirements are met, then on to the final check. Most of these should
//		// be the same as I have here regardless of furnace type, but notice I have an extra check for
//		// a null itemstack because the smelting result might be stored in my recipe slot even if all
//		// other slots in the furnace are empty.
//		if (canInscribe) {
//			ItemStack itemstack = getCurrentRecipe();
//			if (itemstack == null) { itemstack = this.inscriberInventory[GreekFurnaceContainer.RECIPE]; }
//			// Invalid recipe
//			if (itemstack == null) return false;
//			// Recipe is different from the current recipe
//			if (this.inscriberInventory[GreekFurnaceContainer.RECIPE] != null && !this.inscriberInventory[GreekFurnaceContainer.RECIPE].isItemEqual(itemstack)) return false;
//			// Output slot is empty, inscribe away!
//			if (this.inscriberInventory[GreekFurnaceContainer.OUTPUT] == null) return true;
//			// Current scroll in output slot is different than recipe output
//			if (!this.inscriberInventory[GreekFurnaceContainer.OUTPUT].isItemEqual(itemstack)) return false;
//			// Inscribing may surpass stack size limit
//			int result = inscriberInventory[GreekFurnaceContainer.OUTPUT].stackSize + itemstack.stackSize;
//			return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
//		}
//		else
//		{
//			return canInscribe;
//		}
//	}
//
//	// If you have more than one output per recipe, this will return an ItemStack[] array instead
//	public ItemStack getCurrentRecipe() {
//		return GreekFurnaceRecipes.spells().getInscribingResult(this.inscriberInventory);
//	}
//
//	/**
//	 * Inscribe a blank scroll with the last current recipe
//	 */
//	public void inscribeScroll()
//	{
//		if (this.canInscribe())
//		{
//			// If you had multiple outputs, this would be an ItemStack[] array that you would
//			// then need to iterate through, checking if each index was null and if not,
//			// finding the correct output slot to try and add to
//			ItemStack inscribeResult = this.inscriberInventory[GreekFurnaceContainer.RECIPE];
//
//			if (inscribeResult != null)
//			{
//				if (this.inscriberInventory[GreekFurnaceContainer.OUTPUT] == null)
//				{
//					this.inscriberInventory[GreekFurnaceContainer.OUTPUT] = inscribeResult.copy();
//				}
//				else if (this.inscriberInventory[GreekFurnaceContainer.OUTPUT].isItemEqual(inscribeResult))
//				{
//					inscriberInventory[GreekFurnaceContainer.OUTPUT].stackSize += inscribeResult.stackSize;
//				}
//
//				// This is where you'd decrement all your INPUT slots, but for me, I only need to do that for
//				// BLANK_SCROLL since I used my INPUT as fuel earlier
//				--this.inscriberInventory[GreekFurnaceContainer.BLANK_SCROLL].stackSize;
//
//				if (this.inscriberInventory[GreekFurnaceContainer.BLANK_SCROLL].stackSize <= 0)
//				{
//					this.inscriberInventory[GreekFurnaceContainer.BLANK_SCROLL] = null;
//				}
//			}
//		}
//	}
//
//	/**
//	 * Returns the number of ticks that the supplied rune will keep
//	 * the inscriber running, or 0 if the rune isn't charged
//	 */
//	public static int getInscriberChargeTime(ItemStack rune)
//	{
//		//if (rune != null && rune.itemID == ArcaneLegacy.runeCharged.itemID) {
//		if (rune != null && rune == new ItemStack(Greece.bakingCover)) { // ????
//			return RUNE_CHARGE_TIME;
//		} else { return 0; }
//	}
//
//	/**
//	 * Return true if item is an energy source (i.e. a charged rune)
//	 */
//	public static boolean isSource(ItemStack itemstack)
//	{
//		return getInscriberChargeTime(itemstack) > 0;
//	}
//
//	@Override // Major problems here?
//	public boolean isUseableByPlayer(EntityPlayer player)
//	{
//		//return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
//		return worldObj.getTileEntity(this.pos) == this;
//				//player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
//	}
//
//	/*@Override // replaced by openInventory and closeInventory?
//	public void openChest() {}
//
//	@Override
//	public void closeChest() {}*/
//
//	/**
//	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
//	 */
//	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
//	{
//		boolean isValid = false;
//
//		if (slot >= GreekFurnaceContainer.INPUT[0] && slot <= GreekFurnaceContainer.INPUT[GreekFurnaceContainer.RUNE_SLOTS-1])
//		{
//			//isValid = itemstack.getItem().itemID == ArcaneLegacy.runeCharged.itemID;
//			isValid = itemstack.getItem() == Greece.bakingCover;
//		}
//		else if (slot == GreekFurnaceContainer.BLANK_SCROLL)
//		{
//			//isValid = itemstack.getItem().itemID == ArcaneLegacy.scrollBlank.itemID;
//			isValid = itemstack.getItem() == Greece.bakingCover;
//		}
//		return isValid;
//	}
//
//	/**
//	 * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
//	 * block.
//	 */
//	public int[] getAccessibleSlotsFromSide(int par1)
//	{
//		return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
//	}
//
//	/**
//	 * Returns true if automation can insert the given item in the given slot from the given side.
//	 * Args: Slot, item, side
//	 */
//	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
//	{
//		return this.isItemValidForSlot(par1, par2ItemStack);
//	}
//
//	/**
//	 * Returns true if automation can extract the given item in the given slot from the given side.
//	 * Args: Slot, item, side
//	 */
//	public boolean canExtractItem(int slot, ItemStack itemstack, int side)
//	{
//		return (slot == GreekFurnaceContainer.OUTPUT || (slot >= GreekFurnaceContainer.DISCHARGE[0] && slot <= GreekFurnaceContainer.DISCHARGE[GreekFurnaceContainer.RUNE_SLOTS-1]));
//	}
//
//	@Override
//	public void readFromNBT(NBTTagCompound tagCompound)
//	{
//		super.readFromNBT(tagCompound);
//		//NBTTagList nbttaglist = tagCompound.getTagList("Items");
//		NBTTagList nbttaglist = tagCompound.getTagList("Items", 1); // int argument "type." What is this? I'm just setting it to 1.
//		this.inscriberInventory = new ItemStack[this.getSizeInventory()];
//
//		for (int i = 0; i < nbttaglist.tagCount(); ++i)
//		{
//			//NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
//			
//			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
//			int b0 = nbttagcompound1.getInteger("Slot");
//
//			if (b0 >= 0 && b0 < this.inscriberInventory.length)
//			{
//				this.inscriberInventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
//			}
//		}
//
//		this.currentInscribeTime = tagCompound.getShort("IncribeTime");
//		this.inscribeProgressTime = tagCompound.getShort("InscribeProgress");
//		// this.inscribeTime = INSCRIBE_TIME;
//
//		if (tagCompound.hasKey("CustomName"))
//		{
//			this.displayName = tagCompound.getString("CustomName");
//		}
//	}
//
//	@Override
//	public void writeToNBT(NBTTagCompound tagCompound)
//	{
//		super.writeToNBT(tagCompound);
//		tagCompound.setShort("InscribeTime", (short)this.currentInscribeTime);
//		tagCompound.setShort("InscribeProgress", (short)this.inscribeProgressTime);
//		NBTTagList nbttaglist = new NBTTagList();
//
//		for (int i = 0; i < this.inscriberInventory.length; ++i)
//		{
//			if (this.inscriberInventory[i] != null)
//			{
//				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
//				nbttagcompound1.setInteger("Slot", i);
//				this.inscriberInventory[i].writeToNBT(nbttagcompound1);
//				nbttaglist.appendTag(nbttagcompound1);
//			}
//		}
//
//		tagCompound.setTag("Items", nbttaglist);
//
//		if (this.isInvNameLocalized())
//		{
//			tagCompound.setString("CustomName", this.displayName);
//		}
//	}
//
//	/*@Override // see above
//	public void setInventorySlotContents(int index, ItemStack stack) {
//		// TODO Auto-generated method stub
//		
//	}*/ 
//
//	/*@Override // see above
//	public boolean isUseableByPlayer(EntityPlayer player) {
//		// TODO Auto-generated method stub
//		return false;
//	}*/
//
//	@Override
//	public void openInventory(EntityPlayer player) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void closeInventory(EntityPlayer player) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/*@Override // see above
//	public boolean isItemValidForSlot(int index, ItemStack stack) {
//		// TODO Auto-generated method stub
//		return false;
//	}*/
//
//	@Override
//	public int getField(int id) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void setField(int id, int value) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public int getFieldCount() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void clear() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public String getName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean hasCustomName() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public IChatComponent getDisplayName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int[] getSlotsForFace(EnumFacing side) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean canInsertItem(int index, ItemStack itemStackIn,
//			EnumFacing direction) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean canExtractItem(int index, ItemStack stack,
//			EnumFacing direction) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	
//	public void setCustomInventoryName(String p_145951_1_)
//    {
//        this.furnaceCustomName = p_145951_1_;
//    }
//}