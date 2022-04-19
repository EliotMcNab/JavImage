package com.company;

import com.company.effects.Effect;
import com.company.models.GenerationModel;
import com.company.models.ImageModel;

import java.util.Random;

public class ImageGrid<T> implements EffectReceiver<T>, Cloneable {

    // ===================================
    //               FIELDS
    // ===================================

    private final Grid<T> grid;
    private final GenerationModel<T> generationModel;
    private final ImageModel<T> imageModel;

    // ===================================
    //            CONSTRUCTOR
    // ===================================

    public ImageGrid(
            final int width,
            final int height,
            final GenerationModel<T> generationModel,
            final ImageModel<T> imageModel
    ) {;
        this.generationModel = generationModel;
        this.imageModel = imageModel;

        this.grid = generationModel.generate(width, height);
    }

    private ImageGrid(
            final GenerationModel<T> generationModel,
            final ImageModel<T> imageModel,
            final Grid<T> grid
    ) {
        this.generationModel = generationModel;
        this.imageModel = imageModel;
        // TODO: implement grid cloning
        this.grid = null;
    }


    // ===================================
    //             GENERATION
    // ===================================

    @Override
    public ImageGrid<T> generate() {
        return null;
    }

    @Override
    public ImageGrid<T> addEffect(Effect<T> effect) {
        return null;
    }
}
