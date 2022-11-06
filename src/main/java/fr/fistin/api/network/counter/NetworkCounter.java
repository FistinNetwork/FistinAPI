package fr.fistin.api.network.counter;

/**
 * Created by AstFaster
 * on 06/11/2022 at 08:52.<br>
 *
 * Represents the counter of total players connected on the network.
 */
public interface NetworkCounter {

    /**
     * Get the total amount of players currently connected on the network.
     *
     * @return An amount of players
     */
    int getPlayers();

    /**
     * Get the counter of a category.<br>
     * E.g. the category lobby or tnttag.
     *
     * @param name The name of the category to get
     * @return The {@linkplain  NetworkCategoryCounter category counter}
     */
    NetworkCategoryCounter getCategory(String name);

}
