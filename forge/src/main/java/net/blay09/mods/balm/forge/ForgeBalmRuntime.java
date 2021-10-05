package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.ForgeBalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.forge.block.ForgeBalmBlocks;
import net.blay09.mods.balm.forge.block.entity.ForgeBalmBlockEntities;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.entity.ForgeBalmEntities;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.item.ForgeBalmItems;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenus;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.sound.ForgeBalmSounds;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ForgeBalmRuntime implements BalmRuntime {
    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final BalmBlocks blocks = new ForgeBalmBlocks();
    private final BalmBlockEntities blockEntities = new ForgeBalmBlockEntities();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmItems items = new ForgeBalmItems();
    private final BalmMenus menus = new ForgeBalmMenus();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmRegistries registries = new ForgeBalmRegistries();
    private final BalmSounds sounds = new ForgeBalmSounds();
    private final BalmEntities entities = new ForgeBalmEntities();
    private final BalmProviders providers = new ForgeBalmProviders();

    private final List<String> addonClasses = new ArrayList<>();

    public ForgeBalmRuntime() {
        ForgeBalmCommonEvents.registerEvents(events);
    }

    @Override
    public BalmConfig getConfig() {
        return config;
    }

    @Override
    public BalmEvents getEvents() {
        return events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return worldGen;
    }

    @Override
    public BalmBlocks getBlocks() {
        return blocks;
    }

    @Override
    public BalmBlockEntities getBlockEntities() {
        return blockEntities;
    }

    @Override
    public BalmItems getItems() {
        return items;
    }

    @Override
    public BalmMenus getMenus() {
        return menus;
    }

    @Override
    public BalmNetworking getNetworking() {
        return networking;
    }

    @Override
    public BalmHooks getHooks() {
        return hooks;
    }

    @Override
    public BalmRegistries getRegistries() {
        return registries;
    }

    @Override
    public BalmSounds getSounds() {
        return sounds;
    }

    @Override
    public BalmEntities getEntities() {
        return entities;
    }

    @Override
    public BalmProviders getProviders() {
        return providers;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public void initialize(String modId) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        for (DeferredRegister<?> deferredRegister : DeferredRegisters.getByModId(modId)) {
            deferredRegister.register(modEventBus);
        }

        ((ForgeBalmEntities) entities).register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLLoadCompleteEvent event) -> initializeAddons());
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            addonClasses.add(className);
        }
    }

    private void initializeAddons() {
        for (String addonClass : addonClasses) {
            try {
                Class.forName(addonClass).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener((ResourceManagerReloadListener) reloadListener::accept);
        });
    }

}