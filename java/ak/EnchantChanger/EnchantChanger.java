package ak.EnchantChanger;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.api.MakoUtils;
import ak.EnchantChanger.block.EcBlockHugeMateria;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.block.EcBlockMakoReactor;
import ak.EnchantChanger.block.EcBlockMaterializer;
import ak.EnchantChanger.eventhandler.*;
import ak.EnchantChanger.item.*;
import ak.EnchantChanger.modcoop.CoopMCE;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.utils.EnchantmentUtils;
import com.google.common.base.Optional;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.logging.Logger;

import static ak.EnchantChanger.Recipes.registerRecipes;
import static ak.EnchantChanger.utils.ConfigurationUtils.enableDungeonLoot;
import static ak.EnchantChanger.utils.ConfigurationUtils.initConfig;
import static ak.EnchantChanger.utils.RegistrationUtils.*;

@Mod(modid = "EnchantChanger", name = "EnchantChanger", version = "@VERSION@", dependencies = "required-after:Forge@[10.12.1.1090,)", useMetadata = true)
public class EnchantChanger {

    public static final LivingEventHooks livingeventhooks = new LivingEventHooks();
    //Logger
    public static final Logger logger = Logger.getLogger("EnchantChanger");
    public static Item itemExExpBottle;
    public static Item itemMateria;
    public static Item itemZackSword;
    public static Item itemCloudSword;
    public static Item ItemCloudSwordCore;
    public static Item itemSephirothSword;
    public static Item itemUltimateWeapon;
    public static Item itemPortableEnchantChanger;
    public static Item itemPortableEnchantmentTable;
    public static Item itemMasterMateria;
    public static Item itemImitateSephirothSword;
    public static Block blockEnchantChanger;
    public static Block blockHugeMateria;
    public static Item itemHugeMateria;
    public static Block blockLifeStream;
    public static Fluid fluidLifeStream;
    public static Item itemBucketLifeStream;
    public static Block blockMakoReactor;
    public static Potion potionMako;
    public static DamageSource damageSourceMako;
    public static Material materialMako = new MaterialLiquid(MapColor.grassColor);
    public static boolean loadMTH = false;
    public static boolean loadBC = false;
    public static boolean loadIC = false;
    public static boolean loadRFAPI = false;
    public static boolean loadUE = false;
    public static boolean loadMCE = false;
    public static boolean loadSS = false;
    @Mod.Instance("EnchantChanger")
    public static EnchantChanger instance;
    @SidedProxy(clientSide = "ak.EnchantChanger.Client.ClientProxy", serverSide = "ak.EnchantChanger.CommonProxy")
    public static CommonProxy proxy;

    public static String getUniqueStrings(Object obj) {
        UniqueIdentifier uId = null;
        if (obj instanceof ItemStack) {
            obj = ((ItemStack) obj).getItem();
        }
        if (obj instanceof Block) {
            uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
        }
        if (obj instanceof Item) {
            uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
        }
        return Optional.fromNullable(uId).or(new UniqueIdentifier("none:dummy")).toString();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        initConfig(config);

        itemMateria = (new EcItemMateria("Materia")).setHasSubtypes(true).setMaxDamage(0).setTextureName("ender_pearl");
        itemExExpBottle = new EcItemExExpBottle("ExExpBottle");
        itemZackSword = (new EcItemZackSword("ZackSword")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        ItemCloudSwordCore = (new EcItemCloudSwordCore("CloudSwordCore")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemCloudSword = (new EcItemCloudSword("CloudSword")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14).setCreativeTab(null);
        itemSephirothSword = (new EcItemSephirothSword("MasamuneBlade")).setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 2);
        itemUltimateWeapon = (new EcItemUltimateWeapon("UltimateWeapon")).setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 14);
        itemPortableEnchantChanger = (new EcItemMaterializer("PortableEnchantChanger"));
        itemPortableEnchantmentTable = (new EcItemEnchantmentTable("PortableEnchantmentTable"));
        itemMasterMateria = new EcItemMasterMateria("itemMasterMateria").setTextureName("ender_pearl").setHasSubtypes(true).setMaxDamage(0).setMaxStackSize(1);
        itemImitateSephirothSword = (new EcItemSephirothSwordImit("ImitateMasamuneBlade")).setTextureName(Constants.EcTextureDomain + "MasamuneBlade");
        blockEnchantChanger = (new EcBlockMaterializer()).setBlockName("EnchantChanger").setCreativeTab(Constants.TAB_ENCHANT_CHANGER).setBlockTextureName(Constants.EcTextureDomain + "EnchantChanger-top").setHardness(5.0f).setResistance(2000.0f).setLightOpacity(0);
        blockHugeMateria = new EcBlockHugeMateria().setHardness(5.0f).setResistance(2000.0f).setLightLevel(1.0f).setLightOpacity(0).setBlockName("blockHugeMateria").setBlockTextureName("glass");
        itemHugeMateria = new EcItemHugeMateria("HugeMateria");
        fluidLifeStream = new Fluid("lifestream").setLuminosity(15);
        FluidRegistry.registerFluid(fluidLifeStream);
        blockLifeStream = new EcBlockLifeStreamFluid(fluidLifeStream, materialMako).setBlockName("lifestream");
        itemBucketLifeStream = new EcItemBucketLifeStream(blockLifeStream, "bucket_lifestream").setContainerItem(Items.bucket).setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
        blockMakoReactor = new EcBlockMakoReactor().setBlockName("makoreactor").setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(Constants.TAB_ENCHANT_CHANGER).setBlockTextureName(Constants.EcTextureDomain + "makoreactor-side");

        registerBlockAndItem();
        registerEnchantments();
        PacketHandler.init();
        addStatusEffect();
        damageSourceMako = new DamageSource("mako").setDamageBypassesArmor();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(livingeventhooks);
        FillBucketHook.buckets.put(blockLifeStream, itemBucketLifeStream);
        MinecraftForge.EVENT_BUS.register(FillBucketHook.INSTANCE);
        MinecraftForge.EVENT_BUS.register(itemPortableEnchantmentTable);

        FMLCommonHandler.instance().bus().register(proxy);
        FMLCommonHandler.instance().bus().register(new PlayerCustomDataHandler());
        FMLCommonHandler.instance().bus().register(new CommonTickHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new GenerateHandler());

        registerTileEntities();

        registerEntities(this);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.registerRenderInformation();
        proxy.registerTileEntitySpecialRenderer();

        registerRecipes();

        if (enableDungeonLoot) {
            GenerateHandler.DungeonLootItemResist();
        }
        loadMTH = Loader.isModLoaded("MultiToolHolders");
        loadMCE = ModAPIManager.INSTANCE.hasAPI("mceconomy2");
        loadSS = Loader.isModLoaded("SextiarySector");
        loadRFAPI = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|energy");
        if (loadMCE) {
            MinecraftForge.EVENT_BUS.register(new CoopMCE());
        }
    }

    @Mod.EventHandler
    public void imcEvent(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equals("registerExtraMateria") && message.isNBTMessage()) {
                proxy.registerExtraMateriaRendering(message.getNBTValue());
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        EnchantmentUtils.initMaps();
        MakoUtils.init();
    }

}