package net.xiaoyu233.mitemod.miteite.trans.entity;

import net.minecraft.*;
import net.xiaoyu233.fml.util.ReflectHelper;
import net.xiaoyu233.mitemod.miteite.gui.GuiForgingTable;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.Materials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(bex.class)
public abstract class ClientPlayerTrans extends beu {
   @Shadow
   protected Minecraft d;

   public ClientPlayerTrans(World par1World, String par2Str) {
      super(par1World, par2Str);
   }

   @Shadow
   public void a(ChatMessage chatMessage) {
   }

   @Shadow
   public boolean a(int i, String s) {
      return false;
   }

   @Shadow
   public ChunkCoordinates b() {
      return null;
   }

   public void displayGUIChestForMinecartEntity(EntityMinecartChest par1IInventory) {
      this.d.a(new axj(ReflectHelper.dyCast(this), par1IInventory));
   }

   public void displayGUIForgingTable(int x, int y, int z, ForgingTableSlots slots) {
      this.d.a(new GuiForgingTable(ReflectHelper.dyCast(this), x, y, z, slots));
   }

   @Overwrite
   private float getBenchAndToolsModifier(Container container) {
      if (!(container instanceof ContainerWorkbench)) {
         return 0.0F;
      } else {
         ContainerWorkbench container_workbench = (ContainerWorkbench) container;
         SlotResult slot_crafting = (SlotResult) container_workbench.getSlot(0);
         ItemStack item_stack = slot_crafting.getStack();
         Item item = item_stack == null ? null : item_stack.getItem();
         aah recipe = container_workbench.getRecipe();
         Material material_to_check_tool_bench_hardness_against;
         if (recipe == null) {
            material_to_check_tool_bench_hardness_against = item.getHardestMetalMaterial();
         } else {
            material_to_check_tool_bench_hardness_against = recipe.getMaterialToCheckToolBenchHardnessAgainst();
         }

         Material benchMaterial = BlockWorkbench.getToolMaterial(container_workbench.getBlockMetadata());

         if (material_to_check_tool_bench_hardness_against == null){
            return benchMaterial == Materials.vibranium
                    ||benchMaterial == Material.flint
                    || benchMaterial == Material.obsidian
                    || benchMaterial == Material.adamantium
                    || benchMaterial == Material.copper
                    || benchMaterial == Material.silver
                    || benchMaterial == Material.gold
                    || benchMaterial == Material.mithril
                    || benchMaterial == Material.ancient_metal
                    || benchMaterial == Material.iron ? Float.MAX_VALUE : 0.2F;
         }

         if (benchMaterial.getMinHarvestLevel() < material_to_check_tool_bench_hardness_against.getMinHarvestLevel()) {
            return 0F;
         }
         if (benchMaterial == Material.flint
                 || benchMaterial == Material.obsidian
                 || benchMaterial == Materials.vibranium
                 || benchMaterial == Material.adamantium
                 || benchMaterial == Material.copper
                 || benchMaterial == Material.silver
                 || benchMaterial == Material.gold
                 || benchMaterial == Material.mithril
                 || benchMaterial == Material.ancient_metal
                 || benchMaterial == Material.iron) {
            return Float.MAX_VALUE;
         } else {
            Minecraft.setErrorMessage("getBenchAndToolsModifier: unrecognized tool material " + benchMaterial);
            return 0.0F;
         }
      }
   }
}
