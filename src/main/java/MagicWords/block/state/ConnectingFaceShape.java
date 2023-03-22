package MagicWords.block.state;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

    public enum ConnectingFaceShape implements StringRepresentable {
        NONE("none"),
        NORTH("north"),
        EAST("east"),
        SOUTH("south"),
        WEST("west"),
        NORTH_EAST("north_east"),
        NORTH_WEST("north_west"),
        SOUTH_EAST("south_east"),
        SOUTH_WEST("south_west"),
        NORTH_SOUTH("north_south"),
        EAST_WEST("east_west"),
        NORTH_EAST_SOUTH("north_east_south"),
        NORTH_EAST_WEST("north_east_west"),
        NORTH_SOUTH_WEST("north_south_west"),
        EAST_SOUTH_WEST("east_south_west"),
        ALL("all")
        ;

        private final String name;

        ConnectingFaceShape(String name){this.name = name;}

        public String getName(){return this.name;}

        public String toString(){return this.name;}

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

