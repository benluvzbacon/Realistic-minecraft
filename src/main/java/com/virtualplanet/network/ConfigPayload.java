package com.virtualplanet.network;
import net.minecraft.network.RegistryByteBuf; import net.minecraft.network.codec.PacketCodec; import net.minecraft.network.codec.PacketCodecs; import net.minecraft.network.packet.CustomPayload; import net.minecraft.util.Identifier;
public record ConfigPayload(String json,double centerX,double centerZ,double borderWidth) implements CustomPayload {
 public static final Id<ConfigPayload> ID=new Id<>(Identifier.of("virtualplanet","config"));
 public static final PacketCodec<RegistryByteBuf,ConfigPayload> CODEC=PacketCodec.tuple(PacketCodecs.STRING,ConfigPayload::json,PacketCodecs.DOUBLE,ConfigPayload::centerX,PacketCodecs.DOUBLE,ConfigPayload::centerZ,PacketCodecs.DOUBLE,ConfigPayload::borderWidth,ConfigPayload::new);
 @Override public Id<? extends CustomPayload> getId(){return ID;}
}
