package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {

    enum Type {

        Ground,
        GroundWithGrass,
        WoodPlate,
        
    };
    private Image imgs[];

    public Block(Image[] imgs) {
        this.imgs = imgs;
    }

    public ImageView returnBlock(Type blockType) {
        switch (blockType) {
            case Ground:
                return new ImageView(imgs[0]);
            case GroundWithGrass:
                return new ImageView(imgs[1]);
            default:
                return null;
        }
    }

    public Image returnBlock(int type) {
        if (type < imgs.length) {
            return imgs[type];
        }
        else
        {
            return null;
        }

    }

}
