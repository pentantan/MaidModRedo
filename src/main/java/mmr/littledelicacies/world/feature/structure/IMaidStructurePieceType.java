package mmr.littledelicacies.world.feature.structure;

import mmr.littledelicacies.LittleDelicacies;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

import java.util.Locale;

public interface IMaidStructurePieceType {
    IStructurePieceType MAIDCAFE = register(MaidCafePieces.Piece::new, LittleDelicacies.MODID + ".maidcafe");
    IStructurePieceType BIGTREE = register(BigTreePieces.Piece::new, LittleDelicacies.MODID + ".bigtree");

    static IStructurePieceType register(IStructurePieceType type, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), type);
    }
}