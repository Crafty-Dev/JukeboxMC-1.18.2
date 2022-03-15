package de.crafty.jukeboxmc;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

@Mod("jukeboxmc")
public class JukeboxMC {

    public static final String PREFIX = "§7[§5Jukebox§7] ";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static File CONFIG_FILE;

    private static JukeboxMC instance;


    public String inGamePrefix;
    public String jukeboxPrefix;
    public String key;
    public String jukeboxURL;

    public JukeboxMC() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        this.inGamePrefix = ".";
        this.jukeboxPrefix = "§";
        this.key = "Unknown";
        this.jukeboxURL = "http://crafty.lowkeys.de:8000/Jukebox";

    }

    public static JukeboxMC getInstance() {
        return instance;
    }



    public void setup(FMLCommonSetupEvent event){
        this.setupConfig();
        this.loadOptions();
    }

    private void setupConfig() {
        File configFolder = new File("./mods", "JukeboxMC");
        configFolder.mkdirs();
        CONFIG_FILE = new File(configFolder, "config.txt");
    }

    private void loadOptions() {

        try {
            BufferedReader reader = Files.newReader(CONFIG_FILE, StandardCharsets.UTF_8);

            reader.lines().forEach(s -> {

                String[] option = s.split(":", 2);
                if ("ingamePrefix".equals(option[0]))
                    this.inGamePrefix = option[1];

                if ("jukeboxPrefix".equals(option[0]))
                    this.jukeboxPrefix = option[1];

                if ("key".equals(option[0]))
                    this.key = option[1];

                if ("jukeboxURL".equals(option[0]))
                    this.jukeboxURL = option[1];
            });
            reader.close();
            LOGGER.info("Loaded Options");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveOptions() {

        try {

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8));
            writer.println("ingamePrefix:" + this.inGamePrefix);
            writer.println("jukeboxPrefix:" + this.jukeboxPrefix);
            writer.println("key:" + this.key);
            writer.println("jukeboxURL:" + this.jukeboxURL);
            writer.flush();
            writer.close();
            LOGGER.info("Saved Options");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public HttpResponse requestJukebox(String key, String msg) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(this.jukeboxURL);
            httpPost.setHeader("Content-Type", "application/json");
            JsonObject object = new JsonObject();
            object.addProperty("key", key);
            object.addProperty("msg", msg);
            httpPost.setEntity(new StringEntity(object.toString(), StandardCharsets.UTF_8));
            return client.execute(httpPost);
        } catch (IOException ignored) {

        }
        return null;
    }


    @Mod.EventBusSubscriber(Dist.CLIENT)
    static class MessageListener {

        @SubscribeEvent
        public static void onMessage(ClientChatEvent event) {
            JukeboxMC jukeboxMC = JukeboxMC.getInstance();
            String message = event.getMessage();

            if (message.startsWith(jukeboxMC.inGamePrefix)) {
                event.setCanceled(true);
                Minecraft.getInstance().player.sendMessage(new TextComponent(PREFIX + "§aSending Request..."), Minecraft.getInstance().player.getUUID());
                Executors.newSingleThreadExecutor().execute(() -> {
                    HttpResponse response = jukeboxMC.requestJukebox(jukeboxMC.key, message.replaceFirst(jukeboxMC.inGamePrefix, jukeboxMC.jukeboxPrefix));
                    if (response != null) {
                        try {

                            StringBuilder res = new StringBuilder();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            reader.lines().forEach(res::append);
                            System.out.println(res);
                            JsonObject json = (JsonObject) new JsonParser().parse(res.toString());
                            if (json.get("responseMsg") != null)
                                Minecraft.getInstance().player.sendMessage(new TextComponent(PREFIX + json.get("responseMsg").getAsString()), Minecraft.getInstance().player.getUUID());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }

    }

}
