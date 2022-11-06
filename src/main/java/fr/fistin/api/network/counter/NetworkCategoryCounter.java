package fr.fistin.api.network.counter;

/**
 * Created by AstFaster
 * on 06/11/2022 at 08:54.<br>
 *
 * Represents the amount of total players connected in a precise category.
 */
public interface NetworkCategoryCounter {

    /**
     * Get the total amount of players connected in this category.
     *
     * @return An amount of players
     */
    int getPlayers();

    /**
     * Get the total amount of players connected in this category AND in a subtype of the category.<br>
     * Example: category=tnttag type=normal or duos
     *
     * @param type The subtype of the category
     * @return An amount of players
     */
    int getPlayers(String type);

}
