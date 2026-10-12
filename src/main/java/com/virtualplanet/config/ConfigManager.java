package com.virtualplanet.config;
import com.google.gson.*; import net.fabricmc.loader.api.FabricLoader; import java.io.*; import java.nio.file.*;
public final class ConfigManager {
 private static final Gson GSON=new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create(); private static final Path PATH=FabricLoader.getInstance().getConfigDir().resolve("virtualplanet.json"); private static volatile PlanetConfig config=PlanetConfig.defaults();
 private ConfigManager(){} public static PlanetConfig get(){return config;} public static synchronized PlanetConfig load(){try{if(Files.exists(PATH))config=GSON.fromJson(Files.readString(PATH),PlanetConfig.class).validated();else save(PlanetConfig.defaults());}catch(Exception e){VirtualLog.error("Unable to read config; using defaults",e);config=PlanetConfig.defaults();}return config;}
 public static synchronized void save(PlanetConfig c)throws IOException{config=c.validated();Files.createDirectories(PATH.getParent());Files.writeString(PATH,GSON.toJson(config));}
 public static String json(){return GSON.toJson(config);} public static PlanetConfig fromJson(String s){return GSON.fromJson(s,PlanetConfig.class).validated();}
 static final class VirtualLog {static void error(String s,Throwable t){System.err.println("[Virtual Planet] "+s);t.printStackTrace();}}
}
