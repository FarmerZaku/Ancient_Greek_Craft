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
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.EAST));
	}
	
	// sets metadata to be used for proper texture rotation.
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        //int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        
		//System.out.println(facing.getAxisDirection().toString());
			
		//if (hitY == 1) {
			//System.out.println("DOWN OR UP");
			// ---this works pretty well---
			float east_west_diff = (float) (placer.posX-pos.getX());
			float north_south_diff = (float) (placer.posZ-pos.getZ());
			if (Math.abs(east_west_diff) > Math.abs(north_south_diff)) {
				if (placer.posX > pos.getX())
					meta=3;
				else meta=1;
			}
			else {
				if (placer.posZ > pos.getZ())
					meta=0;
				else meta=2;
			}
			// ---the above works pretty well---
		//}
		
		//else {
		//if (placer.posX > pos.getX())
			
		//placer.posX
		//System.out.println("PLACER X"+placer.posX+" BLOCK X"+pos.getX());
		
		
        //if (facing.getAxisDirection().toString() == "north")
		//facing = EnumFacing.getHorizontal(1);
		//System.out.println(facing.getFront(facing.getHorizontalIndex()));
		//System.out.println(facing.getHorizontal(facing.getHorizontalIndex()));
		//System.out.println(facing.fromAngle(0));
		//System.out.println("OFFSET X >> "+facing.getFrontOffsetX()+"OFFSET Z >> "+facing.getFrontOffsetZ());
		//System.out.println("X >> "+hitX+" Y >> "+hitY+" Z >> "+hitZ);
		
		// ---this works!---
		/*if (facing.getHorizontal(facing.getHorizontalIndex()).getName() == "south")
			meta=0;
		else if (facing.getHorizontal(facing.getHorizontalIndex()).getName() == "west")
			meta=1;
		else if (facing.getHorizontal(facing.getHorizontalIndex()).getName() == "north")
			meta=2;
		else if (facing.getHorizontal(facing.getHorizontalIndex()).getName() == "east")
			meta=3;*/
		// ---the above works---
		//}
		
        //System.out.println(facing.getName());
		//return this.getStateFromMeta(meta);
		
        /*if (facing.getName() == "north") { //0=south, 1=west, 2=north, 3=east
        	System.out.println("NORTH");
        	worldIn.setBlockState(new BlockPos(hitX,hitY,hitZ), getStateFromMeta(2), 2);
        	return this.getStateFromMeta(meta);
        }

        else if (facing.getName() == "south") { //1
        	System.out.println("SOUTH");
        	worldIn.setBlockState(new BlockPos(hitX,hitY,hitZ), getStateFromMeta(5), 2);
        	return this.getStateFromMeta(meta);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
        }

        else if (facing.getName() == "east") { //2
        	System.out.println("EAST");
        	worldIn.setBlockState(new BlockPos(hitX,hitY,hitZ), getStateFromMeta(3), 2);
        	return this.getStateFromMeta(meta);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
        }

        else if (facing.getName() == "west") { //3
        	System.out.println("WEST");
        	worldIn.setBlockState(new BlockPos(hitX,hitY,hitZ), getStateFromMeta(4), 2);
        	return this.getStateFromMeta(meta);
            //par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
        }*/
        
        //else 
        	return this.getStateFromMeta(meta);
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