package com.lightning.northstar.block.tech.rocket_station;

import java.util.List;

import com.google.common.collect.Lists;
import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarPackets;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RocketStationScreen extends AbstractContainerScreen<RocketStationMenu> implements ContainerListener {
	   private final ResourceLocation TABLE_LOCATION =  Northstar.asResource("textures/gui/rocket_station.png");

	public RocketStationScreen(RocketStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);

	}

	   protected void subInit() {
	   }

	   protected void init() {
	      super.init();
	      this.subInit();
	      this.menu.addSlotListener(this);
	   }

	   public void removed() {
	      super.removed();
	      this.menu.removeSlotListener(this);
	   }

	   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
	      this.renderBackground(pPoseStack);
	      super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	      RenderSystem.disableBlend();
	      this.renderFg(pPoseStack, pMouseX, pMouseY, pPartialTick);;
	      this.renderButtons(pPoseStack, pMouseX, pMouseY, pPartialTick);
	      this.renderCost(pPoseStack, pPartialTick);
	      this.renderTooltip(pPoseStack, pMouseX, pMouseY);
	   }
	   
	   protected void renderCost(PoseStack pPoseStack, float delta) {
		   this.menu.slotsChanged(this.menu.container);
		   int x = ((width - (imageWidth + (imageWidth / 2))) / 2);
		   int y = (height - (imageHeight + (imageHeight / 2))) / 2;
		   if (this.menu.blockEntity == null) {
			   System.out.println("Ruh roh");
		   }
		   if(this.menu.target != null)
		   this.font.draw(pPoseStack, Component.literal("Estimated Fuel Cost: " + this.menu.fuelCost + " gJ"), x + imageWidth - 110, y + imageWidth - 100, 0x313a54);
		   if(this.menu.target == null)
		this.font.draw(pPoseStack, Component.literal("Invalid Target"), x + imageWidth - 40, y + imageWidth - 100, 0x313a54);
	   }
	   
	   protected void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
	        int x = ((width - (imageWidth + (imageWidth / 2))) / 2);
	        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
		   
	       IconButton assemble = new IconButton(x + imageWidth - 10, y + imageHeight - 79, AllIcons.I_ADD);
		   assemble.setToolTip(Lang.translateDirect("station.assemble_train"));
		   assemble.withCallback(() -> {
				NorthstarPackets.getChannel()
					.sendToServer(RocketStationEditPacket.tryAssemble(menu.blockEntity.getBlockPos()));
				removed(); onClose();
			});
		   assemble.render(pPoseStack, mouseX, mouseY, delta);
		   assemble.renderButton(pPoseStack, mouseX, mouseY, delta);
		   addRenderableWidget(assemble);
		   
		   if ((Math.abs(x + imageWidth - mouseX) < 9 && Math.abs(y + imageHeight - 79 + 9 - mouseY) < 9)) {
	            List<Component> list = Lists.newArrayList();
	            RenderSystem.colorMask(true, true, true, true);
	            list.add((Lang.translateDirect("northstar.gui.rocket_station.assemble").withStyle(ChatFormatting.WHITE)));
	               
	            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
	        }
		   
	   }
	   protected void assemble() {
		   if (this.menu != null)
		   {this.menu.assemble();}
		   else {System.out.println("no block entity :(");}
		   if (this.menu == null) {System.out.println("no menu?? :(");}
	   }

	   protected void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

	   }
	   
	   protected void renderLabels(PoseStack pPoseStack, int pX, int pY) {
		      RenderSystem.disableBlend();
		      super.renderLabels(pPoseStack, pX, pY);
	   }

	   protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
	      RenderSystem.setShader(GameRenderer::getPositionTexShader);
	      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	      RenderSystem.setShaderTexture(0, this.TABLE_LOCATION);
	      int i = (this.width - this.imageWidth) / 2;
	      int j = (this.height - this.imageHeight) / 2;
	      this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	      this.blit(pPoseStack, i + 59, j + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
	      if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
	         this.blit(pPoseStack, i + 99, j + 45, this.imageWidth, 0, 28, 21);
	      }

	   }

	   public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
		   
	   }

	   /**
	    * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
	    * contents of that slot.
	    */
	   public void slotChanged(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack) {
	   }
}
