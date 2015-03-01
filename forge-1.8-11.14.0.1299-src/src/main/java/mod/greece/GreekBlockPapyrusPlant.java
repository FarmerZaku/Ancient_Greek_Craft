package mod.greece;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class GreekBlockPapyrusPlant extends BlockReed {
	
	protected GreekBlockPapyrusPlant() {
        setStepSound(Block.soundTypeGrass);
        setUnlocalizedName("papyrusPlantBlock");
    }
	
	//@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
        return Greece.papyrusPlantItem;
    }
}