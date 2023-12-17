package dev.denismasterherobrine.forgeprotect.database.records;

public class TableCreator {
    public static final String CREATE_BLOCK_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS block_events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "player_name TEXT, " +
                    "block_type TEXT, " +
                    "block_position TEXT, " +
                    "action TEXT, " +
                    "nbt_data TEXT, " +
                    "world TEXT, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_MOB_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS mob_events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "mob_type TEXT, " +
                    "event_type TEXT, " +
                    "mob_position TEXT, " +
                    "nbt_data TEXT, " +
                    "world TEXT, " +
                    "reason TEXT, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_PLAYER_DEATHS_TABLE =
            "CREATE TABLE IF NOT EXISTS player_deaths (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "player_name TEXT, " +
                    "death_position TEXT, " +
                    "inventory_data TEXT, " +
                    "curios_data TEXT, " +
                    "world TEXT, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_ITEM_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS item_events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "item_type TEXT, " +
                    "drop_time DATETIME, " +
                    "item_position TEXT, " +
                    "nbt_data TEXT, " +
                    "world TEXT, " +
                    "drop_source TEXT, " +
                    "pickup_source TEXT, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_ITEM_CONTAINER_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS item_container_events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "event_type TEXT, " +
                    "amount INTEGER, " +
                    "item_type TEXT, " +
                    "event_time TIMESTAMP, " +
                    "container_position TEXT, " +
                    "nbt_data TEXT, " +
                    "world TEXT, " +
                    "player TEXT)";
}
