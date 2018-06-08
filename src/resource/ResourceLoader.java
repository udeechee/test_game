package resource;

import game.Block;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ResourceLoader {

    private static final String RESOURCE_DIR = "C:\\Users\\Owner\\IdeaProjects\\GamCopy\\resources";

    public ResourceLoader(){
        System.out.println("Loading Resources from: " + RESOURCE_DIR);
        File file = new File(RESOURCE_DIR + "/resources_readme.txt");
    }


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

    public Image[] getImgsRunning() {
        return imgsRunning;
    }

    public void setImgsRunning(Image[] imgsRunning) {
        this.imgsRunning = imgsRunning;
    }

    public Image[] getImgsStopped() {
        return imgsStopped;
    }

    public void setImgsStopped(Image[] imgsStopped) {
        this.imgsStopped = imgsStopped;
    }

    public Image getImgsJumpingShoot() {
        return imgsJumpingShoot;
    }

    public void setImgsJumpingShoot(Image imgsJumpingShoot) {
        this.imgsJumpingShoot = imgsJumpingShoot;
    }

    public Image getImgsJumping() {
        return imgsJumping;
    }

    public void setImgsJumping(Image imgsJumping) {
        this.imgsJumping = imgsJumping;
    }

    public Image[] getImgsRunningShoot() {
        return imgsRunningShoot;
    }

    public void setImgsRunningShoot(Image[] imgsRunningShoot) {
        this.imgsRunningShoot = imgsRunningShoot;
    }

    public Image[] getImgsStoppedShoot() {
        return imgsStoppedShoot;
    }

    public void setImgsStoppedShoot(Image[] imgsStoppedShoot) {
        this.imgsStoppedShoot = imgsStoppedShoot;
    }

    public Image[] getImgsDust() {
        return imgsDust;
    }

    public void setImgsDust(Image[] imgsDust) {
        this.imgsDust = imgsDust;
    }

    public Image[] getImgsBlocks() {
        return imgsBlocks;
    }

    public void setImgsBlocks(Image[] imgsBlocks) {
        this.imgsBlocks = imgsBlocks;
    }

    public Image getImgSky() {
        return imgSky;
    }

    public void setImgSky(Image imgSky) {
        this.imgSky = imgSky;
    }

    public Image getImgGround() {
        return imgGround;
    }

    public void setImgGround(Image imgGround) {
        this.imgGround = imgGround;
    }

    public Image getImgGun() {
        return imgGun;
    }

    public void setImgGun(Image imgGun) {
        this.imgGun = imgGun;
    }

    public Image getImgMountain() {
        return imgMountain;
    }

    public void setImgMountain(Image imgMountain) {
        this.imgMountain = imgMountain;
    }

    public Image getImgSlide() {
        return imgSlide;
    }

    public void setImgSlide(Image imgSlide) {
        this.imgSlide = imgSlide;
    }

    public Image getImgCollide() {
        return imgCollide;
    }

    public void setImgCollide(Image imgCollide) {
        this.imgCollide = imgCollide;
    }

    public ImageView getCharacter() {
        return character;
    }

    public void setCharacter(ImageView character) {
        this.character = character;
    }

    public ImageView getSky1() {
        return sky1;
    }

    public void setSky1(ImageView sky1) {
        this.sky1 = sky1;
    }

    public ImageView getSky2() {
        return sky2;
    }

    public void setSky2(ImageView sky2) {
        this.sky2 = sky2;
    }

    public ImageView getGround1() {
        return ground1;
    }

    public void setGround1(ImageView ground1) {
        this.ground1 = ground1;
    }

    public ImageView getGround2() {
        return ground2;
    }

    public void setGround2(ImageView ground2) {
        this.ground2 = ground2;
    }

    public ImageView getGun() {
        return gun;
    }

    public void setGun(ImageView gun) {
        this.gun = gun;
    }

    public ImageView getMountain1() {
        return mountain1;
    }

    public void setMountain1(ImageView mountain1) {
        this.mountain1 = mountain1;
    }

    public ImageView getMountain2() {
        return mountain2;
    }

    public void setMountain2(ImageView mountain2) {
        this.mountain2 = mountain2;
    }

    public ImageView getDust() {
        return dust;
    }

    public void setDust(ImageView dust) {
        this.dust = dust;
    }

    public ImageView getSlide() {
        return slide;
    }

    public void setSlide(ImageView slide) {
        this.slide = slide;
    }

    public Block getBlocks() {
        return blocks;
    }

    public void setBlocks(Block blocks) {
        this.blocks = blocks;
    }

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
                    imgsRunning[i] = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/test/d (" + (i + 1) + ").png"));
                }
                if (i <= 11) {
                    imgsRunningShoot[i] = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/character/shoot-" + i + ".png"));
                }
                if (i < imgsStopped.length) {
                    imgsStopped[i] = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/test/b (" + (i + 1) + ").png"));

                }

                if (i < 2) {
                    imgsStoppedShoot[i] = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/character/stoppedshoot" + i + ".png"));
                }
                if (i < 6) {
                    imgsDust[i] = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/effects/blood_a_000" + (i + 1) + ".png"));
                }
                if (i < imgsBlocks.length) {
                    imgsBlocks[i] = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/world/block" + (i) + ".png"));

                }
            }
            imgSky = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/world/background.png"));
            imgGround = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/world/ground.png"));
            imgGun = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/world/gun.png"));
            imgMountain = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/world/mountains.png"));
            imgsJumpingShoot = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/character/jump2.png"));
            imgsJumping = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/test/jumping.png"));
            imgSlide = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/character/slide.png"));
            imgCollide = new Image(RESOURCE_DIR.getClass().getResourceAsStream("/test/collide.png"));
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
