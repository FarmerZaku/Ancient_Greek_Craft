package mod.greece;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GreekBlockScrollshelf extends GreekBlock {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	protected GreekBlockScrollshelf(Material material) {
		super(material);
	}
	
	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }
	
	
	// sets metadata to be used for proper texture rotation.
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
        	par1World.setBlockState(new BlockPos(par2,par3,par4), getStateFromMeta(2), 2);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
        }

        if (l == 1) {
        	par1World.setBlockState(new BlockPos(par2,par3,par4), getStateFromMeta(5), 2);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
        }

        if (l == 2) {
        	par1World.setBlockState(new BlockPos(par2,par3,par4), getStateFromMeta(3), 2);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
        }

        if (l == 3) {
        	par1World.setBlockState(new BlockPos(par2,par3,par4), getStateFromMeta(4), 2);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
        }
    }
	
	@Override
	  public IBlockState getStateFromMeta(int meta)
	  {
	    EnumFacing facing = EnumFacing.getHorizontal(meta);
	    return this.getDefaultState().withProperty(FACING, facing);
	  }
	
	@Override
	  public int getMetaFromState(IBlockState state)
	  {
	    EnumFacing facing = (EnumFacing)state.getValue(FACING);

	    int facingbits = facing.getHorizontalIndex();
	    return facingbits;
	  }
	
	// necessary to define which properties your blocks use
	  // will also affect the variants listed in the blockstates model file
	  @Override
	  protected BlockState createBlockState()
	  {
	    return new BlockState(this, new IProperty[] {FACING});
	  }
	  
/*	public static IIcon planksIcon;
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
		
    }*/
}