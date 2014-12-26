package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Resourses {

    Image[] imgsRunning;
    Image[] imgsStopped;
    Image imgsJumpingShoot;
    Image imgsJumping;
    Image[] imgsRunningShoot;
    Image[] imgsStoppedShoot;
    Image[] imgsDust;
    Image[] imgsBlocks;
    Image imgSky;
    Image imgGround;
    Image imgGun;
    Image imgMountain;
    Image imgSlide;
    Image imgCollide;

    ImageView character;
    ImageView sky1;
    ImageView sky2;
    ImageView ground1;
    ImageView ground2;
    ImageView gun;
    ImageView mountain1;
    ImageView mountain2;
    ImageView dust;
    ImageView slide;
    Block blocks;

    public void loadResourses() {
        try {

            imgsRunning = new Image[23];
            imgsStopped = new Image[19];
            imgsStoppedShoot = new Image[2];
            imgsDust = new Image[6];
            imgsRunningShoot = new Image[12];
            imgsBlocks = new Image[4];

            for (int i = 0; i < imgsRunning.length; i++) {
                if (i < imgsRunning.length) {
                    imgsRunning[i] = new Image(Resourses.class.getResourceAsStream("test/d (" + (i + 1) + ").png"));
                }
                if (i <= 11) {
                    imgsRunningShoot[i] = new Image(Resourses.class.getResourceAsStream("character/shoot-" + i + ".png"));
                }
                if (i < imgsStopped.length) {
                    imgsStopped[i] = new Image(Resourses.class.getResourceAsStream("test/b (" + (i + 1) + ").png"));

                }

                if (i < 2) {
                    imgsStoppedShoot[i] = new Image(Resourses.class.getResourceAsStream("character/stoppedshoot" + i + ".png"));
                }
                if (i < 6) {
                    imgsDust[i] = new Image(Resourses.class.getResourceAsStream("effects/blood_a_000" + (i + 1) + ".png"));
                }
                if (i < imgsBlocks.length) {
                    imgsBlocks[i] = new Image(Resourses.class.getResourceAsStream("world/block" + (i) + ".png"));

                }
            }
            imgSky = new Image(Resourses.class.getResourceAsStream("world/background.png"));
            imgGround = new Image(Resourses.class.getResourceAsStream("world/ground.png"));
            imgGun = new Image(Resourses.class.getResourceAsStream("world/gun.png"));
            imgMountain = new Image(Resourses.class.getResourceAsStream("world/mountains.png"));
            imgsJumpingShoot = new Image(Resourses.class.getResourceAsStream("character/jump2.png"));
            imgsJumping = new Image(Resourses.class.getResourceAsStream("test/jumping.png"));
            imgSlide = new Image(Resourses.class.getResourceAsStream("character/slide.png"));
            imgCollide = new Image(Resourses.class.getResourceAsStream("test/collide.png"));
            attachDefaultImages();

        } catch (Exception e) {
            System.out.println("error loading one of the resourses");
        }
    }

    private void attachDefaultImages() {
        try {
            character = new ImageView(imgsStopped[0]);
            sky1 = new ImageView(imgSky);
            sky2 = new ImageView(imgSky);
            ground1 = new ImageView(imgGround);
            ground2 = new ImageView(imgGround);
            slide = new ImageView(imgSlide);

            ground1.setTranslateY(270);
            ground2.setTranslateY(270);

            character.setViewport(new Rectangle2D(0, 0, 180, 315));
            character.setFitHeight(150);
            character.setFitWidth(100);
            character.setSmooth(true);
            character.setCache(true);
            character.setTranslateY(147);
            character.setTranslateX(50);

            gun = new ImageView(imgGun);
            gun.setFitHeight(35);
            gun.setFitWidth(35);
            gun.setPreserveRatio(true);
            gun.setSmooth(true);
            gun.setCache(true);

            mountain1 = new ImageView(imgMountain);
            mountain2 = new ImageView(imgMountain);

            mountain1.setTranslateY(82);
            mountain2.setTranslateY(82);

            dust = new ImageView(imgsDust[0]);
            dust.setFitHeight(100);
            dust.setFitWidth(100);
            dust.setPreserveRatio(true);
            dust.setSmooth(true);
            dust.setCache(true);
            dust.setTranslateY(220);
            dust.setScaleX(-1);
            dust.setScaleY(0.5);
            blocks = new Block(imgsBlocks);
        } catch (Exception e) {
            System.out.println("error attaching imgs");
        }
    }
}
