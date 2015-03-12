package mod.greece;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GreekItemTool extends Item {
	private float weaponDamage;
	protected ToolMaterial toolMaterial;
	private int damagePerUse;

	public GreekItemTool() {
		setMaxStackSize(64);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public GreekItemTool(ToolMaterial material, int dpu) {
		this.toolMaterial = material;
		this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabTools);
        this.weaponDamage = 0.0F + material.getDamageVsEntity();
        damagePerUse = dpu;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
        return true;
    }
	
	public int getDamagePerUse() {
		return damagePerUse;
	}
	
	/*@Override
	public boolean func_111207_a(ItemStack itemStack, EntityPlayer player, EntityLivingBase target){
		if (!target.worldObj.isRemote) {
			
		}
		
		return false;
	}*/
	
	public String getToolMaterialName()
    {
        return this.toolMaterial.toString();
    }

}
