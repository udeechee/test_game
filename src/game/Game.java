package game;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import resource.ResourceLoader;

/**
 * Original By Tareq 12/26/2014.
 *
 * Changes and updates by AI started on 06/02/2018.
 *
 *
 *
 * General Notes:
 *
 *      - The character himself seems to be on a finite state machine. That is, he is always on a single state,
 *          doing a single action until it is done.
 *      - This code is a working prototype of a game but it is also entirely procedural, which is a shame because
 *          it is in Java.
 *      - The addition of motion blur to a game that is far from complete is both commendable and humorous.
 */
public class Game extends Application implements EventHandler<ActionEvent> {

    public static void main(String[] args) {
        ResourceLoader resourceLoader = new ResourceLoader();
        Application.launch(args);
    }

    ResourceLoader res;
    Pane root;
    boolean runningState;
    boolean shootingState;
    boolean jumpingState;
    boolean right = true;
    boolean jumpReleased;
    int pace30FPS;
    int pace15FPS;
    int pace5FPS;
    int pace0_5FPS;
    double delay;
    int enemyLife = 10;
    double decrease;
    QuadCurve jumpCurve;
    TranslateTransition translateAnim;
    PathTransition jumpTransition;
    MotionBlur motionBlur;
    ArrayList<ImageView> list;
    ImageView view;
    Rectangle characterRect;
    TranslateTransition translateTransition;
    boolean editor;
    int typechange;
    int collisiondelay;
    private boolean slidingState;
    boolean motionblurEnabled = true;
    boolean pressed;
    boolean isFalling;
    Line line;
    Scene scene;
    boolean shift = true;

    @Override

    public void start(Stage primaryStage) {
        root = new Pane();
        scene = new Scene(root, 1000, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
        line = new Line();
        list = new ArrayList<>();
        root.setOnMouseMoved(e -> {
            if (view != null) {
                view.setX(e.getX());
                view.setY(e.getY() - 25);
                for (int j = 0; j < list.size() - 1; j++) {
                    ImageView ivIter = list.get(j);
                    Bounds b = ivIter.getBoundsInParent();
                    if (b.contains(e.getX() + b.getWidth() / 2, e.getY())) {
                        view.setX(ivIter.getX() - b.getWidth() + 1);
                        view.setY(ivIter.getY());
                        break;
                    } else if (b.contains(e.getX() - b.getWidth() / 2, e.getY())) {
                        view.setX(ivIter.getX() + b.getWidth() - 1);
                        view.setY(ivIter.getY());
                        break;
                    } else if (b.contains(e.getX(), e.getY() + b.getHeight() / 2)) {
                        view.setX(ivIter.getX());
                        view.setY(ivIter.getY() - b.getHeight());
                        break;
                    } else if (b.contains(e.getX(), e.getY() - b.getHeight() / 2)) {
                        view.setX(ivIter.getX());
                        view.setY(ivIter.getY() + b.getHeight());
                        break;
                    }
                }

            }

        });
        root.setOnMouseClicked(e -> {
                    /**
                     * This chunk of code prints out the mouse's coords and then
                     *  toggles the visibility tof the block.
                     */
            if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println(e.getX() + " - " + e.getY());
                if (editor) {
                    for (ImageView list1 : list) {
                        list1.setVisible(false);
                    }
                } else {
                    for (ImageView list1 : list) {
                        list1.setVisible(true);
                    }
                }
                editor = !editor;
            }
            /**
             * This chunk of code updates the type of block and then add its to the world.
             */
            else if (e.getButton() == MouseButton.PRIMARY) {
                view = new ImageView(res.getBlocks().returnBlock(typechange));
                view.translateXProperty().bind(res.getGround1().translateXProperty().add(view.getX()));
                addToWorld(view);
                list.add(view);

            } else if (e.getButton() == MouseButton.MIDDLE) {
                for (int j = 0; j < list.size() - 1; j++) {
                    ImageView iv = list.get(j);
                    Bounds b = iv.getBoundsInParent();
                    if (b.contains(e.getX(), e.getY())) {
                        list.remove(j);
                        root.getChildren().remove(iv);
                        break;
                    } else if (j == list.size() - 2) {
                        typechange++;
                        typechange = typechange % res.getImgsBlocks().length;
                        view.setImage(res.getBlocks().returnBlock(typechange));

                    }
                }

            }
        }
        );

        // Initializers.
        initMotionListeners();

        initResourses();

        initWorld();

        initGame();

        initCollisionDetection();
        root.setScaleShape(true);

        // Looks like an unused music loader.
        /*
         Media media = new Media("file:///c:/Untitled%20Folder/game/theme.mp3");
         MediaPlayer mediaPlayer   = new MediaPlayer(media);
         mediaPlayer.setVolume(1.0);
         mediaPlayer.play();
         */
    }

    private void initWorld() {

        // Seemingly the curve to define the jump. Class is empty.
        jumpCurve = new QuadCurve();

        // Seems to be a motion blur mech.
        motionBlur = new MotionBlur();

        // Seems to be the defined character hitbox?
        characterRect = new Rectangle();

        // Possibly to do with animation?
        translateTransition = new TranslateTransition();
        translateTransition.setInterpolator(Interpolator.EASE_IN);

        // Defined character properties?
        characterRect.setFill(Color.TRANSPARENT);
        characterRect.setStroke(Color.GREY);
        characterRect.setHeight(res.getCharacter().getBoundsInParent().getHeight() - 50);
        characterRect.setWidth(10);
        characterRect.xProperty().bind(res.getCharacter().translateXProperty().add(res.getCharacter().getBoundsInParent().getWidth() / 2.0 - 5.0));
        characterRect.yProperty().bind(res.getCharacter().translateYProperty().add(30));

        // Mechs for the jump, not in a class.
        jumpTransition = new PathTransition(Duration.millis(1000), jumpCurve, res.getCharacter());
        jumpCurve.startXProperty().bind(res.getCharacter().translateXProperty().add(27).add(res.getCharacter().xProperty()));
        jumpCurve.startYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2).add(res.getCharacter().xProperty()));

        jumpCurve.controlXProperty().bind(res.getCharacter().translateXProperty().add(25 * 10 / 2).add(27).add(res.getCharacter().xProperty()));
        jumpCurve.controlYProperty().bind(res.getCharacter().translateYProperty().add(-80).add(res.getCharacter().xProperty()));

        jumpCurve.endXProperty().bind(res.getCharacter().translateXProperty().add(25 * 10).add(47).add(res.getCharacter().xProperty()));
        jumpCurve.endYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2).add(res.getCharacter().xProperty()));

        // Background, items, and more.
        res.getSky1().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3.5));
        res.getSky2().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3.5).add(999));
        res.getGround1().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-2));
        res.getGround2().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-2).add(1000));
        res.getMountain1().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3));
        res.getMountain2().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3).add(1000));
        res.getGun().setVisible(false);
        res.getGun().translateXProperty().bind(res.getCharacter().translateXProperty().add(30));
        res.getGun().translateYProperty().bind(res.getCharacter().translateYProperty().add(42));
        Rectangle r = new Rectangle(0, 280, 2000, 100);
        r.setFill(new Color(70.0 / 255.0, 38.0 / 255.0, 25 / 255.0, 1.0));
        jumpCurve.setFill(Color.TRANSPARENT);
        jumpCurve.setStroke(Color.BLACK);
        res.getDust().setVisible(false);

        // Function to actually add the entities to the world.
        addToWorld(
                res.getSky1(),
                res.getSky2(),
                res.getMountain1(),
                res.getMountain2(),
                r,
                res.getGround1(),
                res.getGround2(),
                res.getDust(),
                res.getCharacter()

        );

    }

    // Seemingly to initalize the game and the animation.
    private void initGame() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(33.33), this));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        translateAnim = new TranslateTransition(Duration.millis(5000), res.getCharacter());

    }

    // To initialize/load the resources.
    private void initResourses() {
        res = new ResourceLoader();
        res.loadResourses();
    }

    private void addToWorld(Node... n) {
        root.getChildren().addAll(n);
    }

    private void addToWorld(Node n) {
        root.getChildren().add(8, n);
    }

    // Seems to be the general event handler.
    @Override
    public void handle(ActionEvent event) {
        pace30FPS++;
        pace15FPS = (pace30FPS % 2 == 0) ? pace15FPS + 1 : pace15FPS;
        pace5FPS = (pace30FPS % 6 == 0) ? pace5FPS + 1 : pace5FPS;
        pace0_5FPS = (pace30FPS % 60 == 0) ? pace0_5FPS + 1 : pace0_5FPS;
        if (collisiondelay == 0) {
            // If I had to guess this is why the sliding can result in the character going through objects/below the ground.
            if (slidingState) {
                res.getCharacter().setImage(res.getImgSlide());
                res.getDust().setImage(res.getImgsDust()[pace15FPS % 6]);
                res.getCharacter().setViewport(new Rectangle2D(0, 0, 340, 180));
            } else if (shootingState && jumpTransition != null && jumpTransition.getStatus() == PathTransition.Status.STOPPED) {
                if (runningState) {
                    res.getCharacter().setImage(res.getImgsRunningShoot()[pace15FPS % 12]);
                    res.getDust().setImage(res.getImgsDust()[pace15FPS % 6]);

                } else {
                    res.getCharacter().setImage(res.getImgsStoppedShoot()[pace0_5FPS % 2]);
                }

            } else if (!shootingState && jumpTransition != null && jumpTransition.getStatus() == PathTransition.Status.STOPPED) {
                if (runningState) {
                    res.getCharacter().setImage(res.getImgsRunning()[pace30FPS % res.getImgsRunning().length]);
                    res.getDust().setImage(res.getImgsDust()[pace15FPS % 6]);

                } else {
                    res.getCharacter().setImage(res.getImgsStopped()[pace15FPS % res.getImgsStopped().length]);
                }
            } else if (jumpTransition != null && jumpTransition.getStatus() == PathTransition.Status.RUNNING) {
                if (shootingState) {
                    res.getCharacter().setImage(res.getImgsJumpingShoot());
                    res.getDust().setImage(res.getImgsDust()[pace15FPS % 6]);
                } else {
                    res.getCharacter().setImage(res.getImgsJumping());

                }
            }
        } else {
            collisiondelay--;
        }
        if (!runningState && !jumpReleased) {
            if (delay > 0 && !right && motionblurEnabled) {
                motionBlur.setAngle(-180);
                motionBlur.setRadius(0);
                res.getMountain1().setEffect(motionBlur);
                res.getMountain2().setEffect(motionBlur);
                res.getGround1().setEffect(motionBlur);
                res.getGround2().setEffect(motionBlur);

            } else if (delay > 0 && right && motionblurEnabled) {
                motionBlur.setAngle(0);
                motionBlur.setRadius(0);
                res.getMountain1().setEffect(motionBlur);
                res.getMountain2().setEffect(motionBlur);
                res.getGround1().setEffect(motionBlur);
                res.getGround2().setEffect(motionBlur);

            }
        } else if (delay > 0 && jumpReleased && motionblurEnabled) {
            motionBlur.setAngle(90);
            motionBlur.setRadius((delay -= 4) * 0.7);
            res.getMountain1().setEffect(motionBlur);
            res.getMountain2().setEffect(motionBlur);
            res.getGround1().setEffect(motionBlur);
            res.getGround2().setEffect(motionBlur);

        } else {
            jumpReleased = false;
        }
        if (jumpTransition.getStatus() != Animation.Status.RUNNING) {
            for (int ir = 0; ir < list.size(); ir++) {
                if (list.get(ir).getBoundsInParent().contains(characterRect.getX() + 5, characterRect.getY() + characterRect.getHeight() + 5)) {
                    isFalling = false;
                    break;
                } else if (ir == list.size() - 1 && characterRect.getY() < 177) {
                    res.getCharacter().setTranslateY(res.getCharacter().getTranslateY() + 5);
                    isFalling = true;
                    break;
                } else {
                    isFalling = false;
                }
            }
        }
    }

    // Seems to be what processes the input.
    private void initMotionListeners() {
        root.requestFocus();
        root.setOnKeyPressed(e -> {
            translateAnim.setDuration(Duration.millis(4000));
            if (e.getCode() == KeyCode.LEFT && pressed == false) {
                pressed = true;
                if ((jumpTransition.getStatus() != Animation.Status.RUNNING)) {
                    res.getDust().setScaleX(1);
                    right = false;
                    res.getCharacter().setScaleX(-1.0);
                    res.getDust().translateXProperty().bind(res.getCharacter().translateXProperty());

                    if (motionblurEnabled) {
                        motionBlur.setAngle(-180);
                        motionBlur.setRadius(delay * 0.5);
                        res.getMountain1().setEffect(motionBlur);
                        res.getMountain2().setEffect(motionBlur);
                        res.getGround1().setEffect(motionBlur);
                        res.getGround2().setEffect(motionBlur);
                        res.getDust().setEffect(motionBlur);
                    }
                    if (jumpTransition.getStatus() == PathTransition.Status.STOPPED
                            && translateAnim.getStatus() != PathTransition.Status.RUNNING
                            && translateTransition.getStatus() != PathTransition.Status.RUNNING) {
                        runningState = true;
                        translateAnim.setByX(-1000);
                        translateAnim.play();
                    }
                }
            } else if (e.getCode() == KeyCode.RIGHT && pressed == false) {
                pressed = true;
                if ((jumpTransition.getStatus() != Animation.Status.RUNNING)) {
                    res.getDust().setScaleX(-1);
                    res.getCharacter().setScaleX(+1.0);
                    right = true;

                    res.getDust().translateXProperty().bind(res.getCharacter().translateXProperty().add(-40));
                    res.getDust().translateYProperty().bind(res.getCharacter().translateYProperty().add(42));
                    if (motionblurEnabled) {
                        motionBlur.setAngle(0);
                        motionBlur.setRadius(delay * 0.5);
                        res.getMountain1().setEffect(motionBlur);
                        res.getMountain2().setEffect(motionBlur);
                        res.getGround1().setEffect(motionBlur);
                        res.getGround2().setEffect(motionBlur);
                        res.getDust().setEffect(motionBlur);
                    }
                    if (jumpTransition.getStatus() == PathTransition.Status.STOPPED
                            && translateAnim.getStatus() != PathTransition.Status.RUNNING
                            && translateTransition.getStatus() != PathTransition.Status.RUNNING) {
                        translateAnim.setByX(+1000);
                        translateAnim.play();
                        runningState = true;
                    }
                }
            } else if (e.getCode() == KeyCode.SHIFT) {
                if (jumpTransition.getStatus() == PathTransition.Status.STOPPED) {
                }

            } else if (e.getCode() == KeyCode.LEFT) {

                right = false;
                if (shootingState) {
                    TranslateTransition tt = new TranslateTransition(Duration.millis(700));
                    Bullet b;
                    if (res.getGun().getScaleX() == 1.0) {
                        b = new Bullet(res.getGun().getTranslateX() + 30, res.getGun().getTranslateY() + 9, 3);
                        tt.setByX(1000);
                    } else {

                        b = new Bullet(res.getGun().getTranslateX(), res.getGun().getTranslateY() + 9, 3);
                        tt.setByX(-1000);
                    }
                    b.setScaleX(res.getGun().getScaleX());
                    addToWorld(b);
                    tt.setNode(b);

                }
            } else if (e.getCode() == KeyCode.UP && !isFalling) {
                if (right) {
                    jumpRight();

                } else {
                    jumpLeft();
                }

                translateAnim.stop();
                runningState = false;
                res.getDust().setVisible(false);
                res.getDust().setVisible(false);
                jumpTransition.setCycleCount(1);
                jumpTransition.play();
                jumpTransition.setOnFinished(ev -> {
                    motionBlur.setAngle(90);
                    delay = 30;
                    jumpReleased = true;
                });
            } else if (e.getCode() == KeyCode.DOWN) {
                if (!(translateAnim.getStatus() == Animation.Status.RUNNING)) {
                    translateAnim.setDuration(Duration.millis(400));
                    if (right) {
                        translateAnim.setByX(150);
                    } else {
                        translateAnim.setByX(-150);
                    }
                    res.getCharacter().setTranslateY(res.getCharacter().getTranslateY() + 56);
                    slidingState = true;
                    characterRect.setTranslateY(characterRect.getTranslateY() + 34);
                    characterRect.setWidth(res.getCharacter().getBoundsInParent().getHeight());
                    characterRect.setHeight(10);
                    translateAnim.play();
                    translateAnim.setOnFinished((ActionEvent event) -> {
                        if (slidingState) {
                            slidingState = false;

                            res.getCharacter().setTranslateY(res.getCharacter().getTranslateY() - 56);
                            res.getCharacter().setViewport(new Rectangle2D(0, 0, 180, 340));
                            characterRect.setTranslateY(characterRect.getTranslateY() - 34);
                            characterRect.setHeight(res.getCharacter().getBoundsInParent().getHeight());
                            characterRect.setWidth(10);

                        }
                    });
                }
            }

        });
        root.setOnKeyReleased(e -> {
            pace15FPS = 0;
            translateAnim.setDuration(Duration.millis(500));
            switch (e.getCode()) {
                case LEFT:
                    pressed = false;
                    translateAnim.stop();
                    res.getDust().setVisible(false);
                    decrease = (double) delay / 10.0;
                    runningState = false;
                    delay = 0;
                    if (motionblurEnabled) {
                        motionBlur.setRadius(0);
                        res.getMountain1().setEffect(motionBlur);
                        res.getMountain2().setEffect(motionBlur);
                        res.getGround1().setEffect(motionBlur);
                        res.getGround2().setEffect(motionBlur);
                        res.getDust().setEffect(motionBlur);
                    }
                    break;
                case RIGHT:
                    pressed = false;
                    res.getDust().setVisible(false);
                    translateAnim.stop();
                    decrease = (double) delay / 10.0;
                    runningState = false;
                    delay = 0;
                    if (motionblurEnabled) {
                        motionBlur.setRadius(0);
                        res.getMountain1().setEffect(motionBlur);
                        res.getMountain2().setEffect(motionBlur);
                        res.getGround1().setEffect(motionBlur);
                        res.getGround2().setEffect(motionBlur);
                        res.getDust().setEffect(motionBlur);
                    }
                    break;
                case UP:
                    // Cute but the dust is in the wrong place. It is not on the player.
                    res.getDust().setVisible(false);
                    break;
                default:
                    break;
            }

        });
    }

    // Code to make the view of the character move left.
    private void jumpLeft() {
        if (delay > 25) {
            jumpCurve.startXProperty().bind(res.getCharacter().translateXProperty().add(47).add(res.getCharacter().xProperty()));
            jumpCurve.startYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2).add(res.getCharacter().xProperty()));

            jumpCurve.controlXProperty().bind(res.getCharacter().translateXProperty().add(-delay * 10 / 2).add(47).add(res.getCharacter().xProperty()));
            jumpCurve.controlYProperty().bind(res.getCharacter().translateYProperty().add(-80).add(res.getCharacter().xProperty()));

            jumpCurve.endXProperty().bind(res.getCharacter().translateXProperty().add(-delay * 10).add(47).add(res.getCharacter().xProperty()));
            jumpCurve.endYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2).add(res.getCharacter().xProperty()));
        } else {
            jumpCurve.startXProperty().bind(res.getCharacter().translateXProperty().add(47).add(res.getCharacter().xProperty()));
            jumpCurve.startYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2).add(res.getCharacter().xProperty()));

            jumpCurve.controlXProperty().bind(res.getCharacter().translateXProperty().add(-25 * 10 / 2).add(47).add(res.getCharacter().xProperty()));
            jumpCurve.controlYProperty().bind(res.getCharacter().translateYProperty().add(-80).add(res.getCharacter().xProperty()));

            jumpCurve.endXProperty().bind(res.getCharacter().translateXProperty().add(-25 * 10).add(47).add(res.getCharacter().xProperty()));
            jumpCurve.endYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2).add(res.getCharacter().xProperty()));
        }
    }

    // Code to make the view of the character move right.
    private void jumpRight() {
        if (delay > 25) {
            jumpCurve.startXProperty().bind(res.getCharacter().translateXProperty().add(47));
            jumpCurve.startYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2));

            jumpCurve.controlXProperty().bind(res.getCharacter().translateXProperty().add(delay * 10 / 2).add(27));
            jumpCurve.controlYProperty().bind(res.getCharacter().translateYProperty().add(-80));

            jumpCurve.endXProperty().bind(res.getCharacter().translateXProperty().add(delay * 10).add(47));
            jumpCurve.endYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2));
        } else {
            jumpCurve.startXProperty().bind(res.getCharacter().translateXProperty().add(47));
            jumpCurve.startYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2));

            jumpCurve.controlXProperty().bind(res.getCharacter().translateXProperty().add(25 * 10 / 2).add(47));
            jumpCurve.controlYProperty().bind(res.getCharacter().translateYProperty().add(-80));

            jumpCurve.endXProperty().bind(res.getCharacter().translateXProperty().add(25 * 10).add(47));
            jumpCurve.endYProperty().bind(res.getCharacter().translateYProperty().add(res.getCharacter().getFitHeight() / 2));
        }
    }

    // Procedural collision detection.
    private void initCollisionDetection() {
        characterRect.xProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (delay < 40) {
                delay++;
            }
            if (right) {
                if (motionblurEnabled) {
                    motionBlur.setAngle(180);
                    motionBlur.setRadius(delay * 0.5);
                    res.getMountain1().setEffect(motionBlur);
                    res.getMountain2().setEffect(motionBlur);
                    res.getGround1().setEffect(motionBlur);
                    res.getGround2().setEffect(motionBlur);
                    res.getDust().setEffect(motionBlur);
                }
            } else {
                if (motionblurEnabled) {
                    motionBlur.setAngle(0);
                    motionBlur.setRadius(delay * 0.5);
                    res.getMountain1().setEffect(motionBlur);
                    res.getMountain2().setEffect(motionBlur);
                    res.getGround1().setEffect(motionBlur);
                    res.getGround2().setEffect(motionBlur);
                    res.getDust().setEffect(motionBlur);
                }
            }
            if (res.getCharacter().getTranslateX() + res.getCharacter().getX() < -50 && shift) {
                System.out.println("Go Right");
                System.out.println(res.getCharacter().getX());
                res.getCharacter().setX(res.getCharacter().getX() + 1000);
                shift = false;

            } else if (res.getCharacter().getTranslateX() + res.getCharacter().getX() > 950 && shift) {
                System.out.println("Go Left");
                System.out.println(res.getCharacter().getX());
                res.getCharacter().setX(res.getCharacter().getX() - 1000);
                shift = false;

            } else if (jumpTransition.getStatus() != Animation.Status.RUNNING) {
                shift = true;
                for (ImageView rec : list) {
                    if (characterRect.intersects(rec.getBoundsInParent())) {
                        if (rec.getBoundsInParent().getMinY() > characterRect.getY() + characterRect.getHeight() * 0.7) {
                            res.getCharacter().setTranslateY(res.getCharacter().getTranslateY() - 1);
                        } else if (right) {
                            translateAnim.stop();
                            res.getCharacter().setImage(res.getImgCollide());
                            collisiondelay = 5;
                            res.getCharacter().setTranslateX(res.getCharacter().getTranslateX() - 1);
                            delay = 0;
                        } else {
                            translateAnim.stop();
                            res.getCharacter().setImage(res.getImgCollide());
                            collisiondelay = 5;
                            res.getCharacter().setTranslateX(res.getCharacter().getTranslateX() + 1);
                            delay = 0;
                        }
                        break;
                    }
                }
            }

        }
        );
        // Um?
        characterRect.yProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for (ImageView iv : list) {
                Bounds b = iv.getBoundsInParent();
                if (characterRect.intersects(b)) {
                    jumpTransition.stop();
                    jumpReleased = true;
                    if (characterRect.getY() + characterRect.getHeight() - 1 < iv.getY() + b.getHeight() / 2) {

                        res.getCharacter().setTranslateY(res.getCharacter().getTranslateY() - 2);
                        if (right) {
                            res.getCharacter().setTranslateX(res.getCharacter().getTranslateX() + 0.25);
                        } else {
                            res.getCharacter().setTranslateX(res.getCharacter().getTranslateX() - 0.25);
                        }
                        break;
                    } else if (b.contains(characterRect.getX() + characterRect.getWidth() / 2.0, characterRect.getY() - 5)
                            || b.contains(characterRect.getX() + characterRect.getWidth() / 2.0 + 15, characterRect.getY() - 5)
                            || b.contains(characterRect.getX() + characterRect.getWidth() / 2.0 - 15, characterRect.getY() - 5)) {
                    } else {

                        if (right) {
                            res.getCharacter().setTranslateX(res.getCharacter().getTranslateX() - 5);
                        } else {
                            res.getCharacter().setTranslateX(res.getCharacter().getTranslateX() + 5);
                        }

                        res.getCharacter().setTranslateY(res.getCharacter().getTranslateY() + iv.getY() + iv.getBoundsInParent().getHeight() - characterRect.getY() - characterRect.getHeight());

                        break;
                    }

                }
            }
        });
    }

    // I thought this may have been a method to keep the "world" going with the running player, used with the "rebindWorld"
    //      method?
    private void unbindWorld() {
        for (Node n : root.getChildren()) {
            n.translateXProperty().unbind();
        }
        res.getMountain2().setTranslateX(res.getMountain2().getTranslateX() - 1000);
        res.getMountain1().setTranslateX(res.getMountain1().getTranslateX() - 1000);

    }

    // See comment on "unbindWorld()" method.
    private void rebindWorld(boolean left) {

        res.getSky1().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3.5));
        res.getSky2().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3.5).add(999));
        res.getGround1().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-2));
        res.getGround2().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-2).add(1000));
        res.getMountain1().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3));
        res.getMountain2().translateXProperty().bind(res.getCharacter().translateXProperty().divide(-3).add(1000));

    }
}
