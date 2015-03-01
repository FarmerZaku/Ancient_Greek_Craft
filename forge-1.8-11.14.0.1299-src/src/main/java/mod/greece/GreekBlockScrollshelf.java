package mod.greece;

/*import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GreekBlockScrollshelf extends GreekBlock {	
	protected GreekBlockScrollshelf(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}
	
	// sets metadata to be used for proper texture rotation.
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
        }

        if (l == 1) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
        }

        if (l == 2) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
        }

        if (l == 3) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
        }
    }
	
	public static IIcon planksIcon;
	public static IIcon shelfIcon;
		
	public void registerBlockIcons(IIconRegister icon) {
		planksIcon = icon.registerIcon("greece:planks_oak");
		shelfIcon = icon.registerIcon("greece:scrollshelf");
	}
	
	public IIcon getIcon(int side, int metadata) {
		// Bottom (0), Top (1), North (2), South (3), West (4), East (5)
		if (metadata == 0 && side == 3) // for inventory icon. metadata=0 when in inventory
			return shelfIcon;
		else if (side==0)
			return planksIcon;
		else if (side==1)
			return planksIcon;
		else if (side != metadata)
			return planksIcon;
		else return shelfIcon;
		
    }
}*/