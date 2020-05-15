package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.model.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ConnectedGlass.MODID)
public class ClientProxy {

    public static final Map<CGGlassBlock,TextureAtlasSprite> TEXTURES = new HashMap<>();

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e){
        for(CGGlassBlock block : ConnectedGlass.BLOCKS){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,new ModelResourceLocation(block.getRegistryName(), "inventory"));
            ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state){
                    return new ModelResourceLocation(block.getRegistryName(), "normal");
                }
            });
        }
        for(CGPaneBlock pane : ConnectedGlass.PANES){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(pane), 0, new ModelResourceLocation(pane.getRegistryName(), "inventory"));
            ModelLoader.setCustomStateMapper(pane, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state){
                    return new ModelResourceLocation(pane.getRegistryName(), "normal");
                }
            });
        }
    }

    @SubscribeEvent
    public static void onBake(ModelBakeEvent e){
        for(CGGlassBlock block : ConnectedGlass.BLOCKS){
            IBakedModel model = block.connected ? new CGConnectedBakedModel(block) : new CGBakedModel(block);
            e.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "normal"), model);
            e.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "inventory"), model);
        }
        for(CGPaneBlock pane : ConnectedGlass.PANES){
            CGPaneBakedModel model = pane.block.connected ? new CGConnectedPaneBakedModel(pane) : new CGPaneBakedModel(pane);
            e.getModelRegistry().putObject(new ModelResourceLocation(pane.getRegistryName(), "normal"), model);
            e.getModelRegistry().putObject(new ModelResourceLocation(pane.getRegistryName(), "inventory"), model);
        }
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre e){
        if(e.getMap().getBasePath().equals("textures")){
            for(CGGlassBlock block : ConnectedGlass.BLOCKS){
                TEXTURES.put(block, e.getMap().registerSprite(block.getRegistryName()));
            }
        }
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Post e){
        if(e.getMap().getBasePath().equals("textures")){
            for(CGGlassBlock block : ConnectedGlass.BLOCKS){
                TEXTURES.put(block, e.getMap().getAtlasSprite(block.getRegistryName().toString()));
            }
        }
    }

}