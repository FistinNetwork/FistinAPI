package fr.fistin.api.packet;

import fr.fistin.api.IFistinAPIProvider;

/**
 * Indicate to FistinAPI if the processing packet is a FistinPacket.<br>
 * By default, a FistinPacket must be serializable in JSON format.
 */
public interface FistinPacket
{

    @Override
    String toString();

    /**
     * Represents a serializer of a given type of {@link FistinPacket}.
     *
     * @param <P> The type of the packet handled by the serializer
     */
    interface Serializer<P extends FistinPacket> {

        /**
         * Serialize a given packet to a {@link String}
         *
         * @param packet The packet to serialize
         * @return The serialized packet
         * @throws Exception if something bad happens during serialization process
         */
        default String serialize(P packet) throws Exception {
            return IFistinAPIProvider.GSON.toJson(this);
        }

        /**
         * Deserialize a given input to a packet
         *
         * @param packetClass The class of the packet to deserialize
         * @param input The input to deserialize
         * @return The deserialized {@link FistinPacket}
         * @throws Exception if something bad happens during deserialization process
         */
        default P deserialize(Class<P> packetClass, String input) throws Exception {
            return IFistinAPIProvider.GSON.fromJson(input, packetClass);
        }

    }

}
