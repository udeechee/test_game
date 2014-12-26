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

/**
 * Original By Tareq 12/26/2014.
 *
 * Changes and updates by AI started on 06/02/2018.
 */
public class Game extends Application implements EventHandler<ActionEvent> {

    public static void main(String[] args) {
        Application.launch(args);
    }

    Resourses res;
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
            } else if (e.getButton() == MouseButton.PRIMARY) {
                view = new ImageView(res.blocks.returnBlock(typechange));
                view.translateXProperty().bind(res.ground1.translateXProperty().add(view.getX()));
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
                        typechange = typechange % res.imgsBlocks.length;
                        view.setImage(res.blocks.returnBlock(typechange));

                    }
                }

            }
        }
        );

        initMotionListeners();

        initResourses();

        initWorld();

        initGame();

        initCollisionDetection();
        root.setScaleShape(true);

        /*
         Media media = new Media("file:///c:/Untitled%20Folder/game/theme.mp3");
         MediaPlayer mediaPlayer   = new MediaPlayer(media);
         mediaPlayer.setVolume(1.0);
         mediaPlayer.play();
         */
    }

    private void initWorld() {
        jumpCurve = new QuadCurve();
        motionBlur = new MotionBlur();
        characterRect = new Rectangle();
        translateTransition = new TranslateTransition();
        translateTransition.setInterpolator(Interpolator.EASE_IN);
        characterRect.setFill(Color.TRANSPARENT);
        characterRect.setStroke(Color.GREY);
        characterRect.setHeight(res.character.getBoundsInParent().getHeight() - 50);
        characterRect.setWidth(10);
        characterRect.xProperty().bind(res.character.translateXProperty().add(res.character.getBoundsInParent().getWidth() / 2.0 - 5.0));
        characterRect.yProperty().bind(res.character.translateYProperty().add(30));

        jumpTransition = new PathTransition(Duration.millis(1000), jumpCurve, res.character);
        jumpCurve.startXProperty().bind(res.character.translateXProperty().add(27).add(res.character.xProperty()));
        jumpCurve.startYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2).add(res.character.xProperty()));

        jumpCurve.controlXProperty().bind(res.character.translateXProperty().add(25 * 10 / 2).add(27).add(res.character.xProperty()));
        jumpCurve.controlYProperty().bind(res.character.translateYProperty().add(-80).add(res.character.xProperty()));

        jumpCurve.endXProperty().bind(res.character.translateXProperty().add(25 * 10).add(47).add(res.character.xProperty()));
        jumpCurve.endYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2).add(res.character.xProperty()));

        res.sky1.translateXProperty().bind(res.character.translateXProperty().divide(-3.5));
        res.sky2.translateXProperty().bind(res.character.translateXProperty().divide(-3.5).add(999));
        res.ground1.translateXProperty().bind(res.character.translateXProperty().divide(-2));
        res.ground2.translateXProperty().bind(res.character.translateXProperty().divide(-2).add(1000));
        res.mountain1.translateXProperty().bind(res.character.translateXProperty().divide(-3));
        res.mountain2.translateXProperty().bind(res.character.translateXProperty().divide(-3).add(1000));
        res.gun.setVisible(false);
        res.gun.translateXProperty().bind(res.character.translateXProperty().add(30));
        res.gun.translateYProperty().bind(res.character.translateYProperty().add(42));
        Rectangle r = new Rectangle(0, 280, 2000, 100);
        r.setFill(new Color(70.0 / 255.0, 38.0 / 255.0, 25 / 255.0, 1.0));
        jumpCurve.setFill(Color.TRANSPARENT);
        jumpCurve.setStroke(Color.BLACK);
        res.dust.setVisible(false);

        addToWorld(
                res.sky1,
                res.sky2,
                res.mountain1,
                res.mountain2,
                r,
                res.ground1,
                res.ground2,
                res.dust,
                res.character
                
        );

    }

    private void initGame() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(33.33), this));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        translateAnim = new TranslateTransition(Duration.millis(5000), res.character);

    }

    private void initResourses() {
        res = new Resourses();
        res.loadResourses();
    }

    private void addToWorld(Node... n) {
        root.getChildren().addAll(n);
    }

    private void addToWorld(Node n) {
        root.getChildren().add(8, n);
    }

    @Override
    public void handle(ActionEvent event) {
        pace30FPS++;
        pace15FPS = (pace30FPS % 2 == 0) ? pace15FPS + 1 : pace15FPS;
        pace5FPS = (pace30FPS % 6 == 0) ? pace5FPS + 1 : pace5FPS;
        pace0_5FPS = (pace30FPS % 60 == 0) ? pace0_5FPS + 1 : pace0_5FPS;
        if (collisiondelay == 0) {
            if (slidingState) {
                res.character.setImage(res.imgSlide);
                res.dust.setImage(res.imgsDust[pace15FPS % 6]);
                res.character.setViewport(new Rectangle2D(0, 0, 340, 180));
            } else if (shootingState && jumpTransition != null && jumpTransition.getStatus() == PathTransition.Status.STOPPED) {
                if (runningState) {
                    res.character.setImage(res.imgsRunningShoot[pace15FPS % 12]);
                    res.dust.setImage(res.imgsDust[pace15FPS % 6]);

                } else {
                    res.character.setImage(res.imgsStoppedShoot[pace0_5FPS % 2]);
                }

            } else if (!shootingState && jumpTransition != null && jumpTransition.getStatus() == PathTransition.Status.STOPPED) {
                if (runningState) {
                    res.character.setImage(res.imgsRunning[pace30FPS % res.imgsRunning.length]);
                    res.dust.setImage(res.imgsDust[pace15FPS % 6]);

                } else {
                    res.character.setImage(res.imgsStopped[pace15FPS % res.imgsStopped.length]);
                }
            } else if (jumpTransition != null && jumpTransition.getStatus() == PathTransition.Status.RUNNING) {
                if (shootingState) {
                    res.character.setImage(res.imgsJumpingShoot);
                    res.dust.setImage(res.imgsDust[pace15FPS % 6]);
                } else {
                    res.character.setImage(res.imgsJumping);

                }
            }
        } else {
            collisiondelay--;
        }
        if (!runningState && !jumpReleased) {
            if (delay > 0 && !right && motionblurEnabled) {
                motionBlur.setAngle(-180);
                motionBlur.setRadius(0);
                res.mountain1.setEffect(motionBlur);
                res.mountain2.setEffect(motionBlur);
                res.ground1.setEffect(motionBlur);
                res.ground2.setEffect(motionBlur);

            } else if (delay > 0 && right && motionblurEnabled) {
                motionBlur.setAngle(0);
                motionBlur.setRadius(0);
                res.mountain1.setEffect(motionBlur);
                res.mountain2.setEffect(motionBlur);
                res.ground1.setEffect(motionBlur);
                res.ground2.setEffect(motionBlur);

            }
        } else if (delay > 0 && jumpReleased && motionblurEnabled) {
            motionBlur.setAngle(90);
            motionBlur.setRadius((delay -= 4) * 0.7);
            res.mountain1.setEffect(motionBlur);
            res.mountain2.setEffect(motionBlur);
            res.ground1.setEffect(motionBlur);
            res.ground2.setEffect(motionBlur);

        } else {
            jumpReleased = false;
        }
        if (jumpTransition.getStatus() != Animation.Status.RUNNING) {
            for (int ir = 0; ir < list.size(); ir++) {
                if (list.get(ir).getBoundsInParent().contains(characterRect.getX() + 5, characterRect.getY() + characterRect.getHeight() + 5)) {
                    isFalling = false;
                    break;
                } else if (ir == list.size() - 1 && characterRect.getY() < 177) {
                    res.character.setTranslateY(res.character.getTranslateY() + 5);
                    isFalling = true;
                    break;
                } else {
                    isFalling = false;
                }
            }
        }
    }

    private void initMotionListeners() {
        root.requestFocus();
        root.setOnKeyPressed(e -> {
            translateAnim.setDuration(Duration.millis(4000));
            if (e.getCode() == KeyCode.LEFT && pressed == false) {
                pressed = true;
                if ((jumpTransition.getStatus() != Animation.Status.RUNNING)) {
                    res.dust.setScaleX(1);
                    right = false;
                    res.character.setScaleX(-1.0);
                    res.dust.translateXProperty().bind(res.character.translateXProperty());

                    if (motionblurEnabled) {
                        motionBlur.setAngle(-180);
                        motionBlur.setRadius(delay * 0.5);
                        res.mountain1.setEffect(motionBlur);
                        res.mountain2.setEffect(motionBlur);
                        res.ground1.setEffect(motionBlur);
                        res.ground2.setEffect(motionBlur);
                        res.dust.setEffect(motionBlur);
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
                    res.dust.setScaleX(-1);
                    res.character.setScaleX(+1.0);
                    right = true;

                    res.dust.translateXProperty().bind(res.character.translateXProperty().add(-40));
                    res.dust.translateYProperty().bind(res.character.translateYProperty().add(42));
                    if (motionblurEnabled) {
                        motionBlur.setAngle(0);
                        motionBlur.setRadius(delay * 0.5);
                        res.mountain1.setEffect(motionBlur);
                        res.mountain2.setEffect(motionBlur);
                        res.ground1.setEffect(motionBlur);
                        res.ground2.setEffect(motionBlur);
                        res.dust.setEffect(motionBlur);
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
                    if (res.gun.getScaleX() == 1.0) {
                        b = new Bullet(res.gun.getTranslateX() + 30, res.gun.getTranslateY() + 9, 3);
                        tt.setByX(1000);
                    } else {

                        b = new Bullet(res.gun.getTranslateX(), res.gun.getTranslateY() + 9, 3);
                        tt.setByX(-1000);
                    }
                    b.setScaleX(res.gun.getScaleX());
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
                res.dust.setVisible(false);
                res.dust.setVisible(false);
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
                    res.character.setTranslateY(res.character.getTranslateY() + 56);
                    slidingState = true;
                    characterRect.setTranslateY(characterRect.getTranslateY() + 34);
                    characterRect.setWidth(res.character.getBoundsInParent().getHeight());
                    characterRect.setHeight(10);
                    translateAnim.play();
                    translateAnim.setOnFinished((ActionEvent event) -> {
                        if (slidingState) {
                            slidingState = false;

                            res.character.setTranslateY(res.character.getTranslateY() - 56);
                            res.character.setViewport(new Rectangle2D(0, 0, 180, 340));
                            characterRect.setTranslateY(characterRect.getTranslateY() - 34);
                            characterRect.setHeight(res.character.getBoundsInParent().getHeight());
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
                    res.dust.setVisible(false);
                    decrease = (double) delay / 10.0;
                    runningState = false;
                    delay = 0;
                    if (motionblurEnabled) {
                        motionBlur.setRadius(0);
                        res.mountain1.setEffect(motionBlur);
                        res.mountain2.setEffect(motionBlur);
                        res.ground1.setEffect(motionBlur);
                        res.ground2.setEffect(motionBlur);
                        res.dust.setEffect(motionBlur);
                    }
                    break;
                case RIGHT:
                    pressed = false;
                    res.dust.setVisible(false);
                    translateAnim.stop();
                    decrease = (double) delay / 10.0;
                    runningState = false;
                    delay = 0;
                    if (motionblurEnabled) {
                        motionBlur.setRadius(0);
                        res.mountain1.setEffect(motionBlur);
                        res.mountain2.setEffect(motionBlur);
                        res.ground1.setEffect(motionBlur);
                        res.ground2.setEffect(motionBlur);
                        res.dust.setEffect(motionBlur);
                    }
                    break;
                case UP:
                    res.dust.setVisible(false);
                    break;
                default:
                    break;
            }

        });
    }

    private void jumpLeft() {
        if (delay > 25) {
            jumpCurve.startXProperty().bind(res.character.translateXProperty().add(47).add(res.character.xProperty()));
            jumpCurve.startYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2).add(res.character.xProperty()));

            jumpCurve.controlXProperty().bind(res.character.translateXProperty().add(-delay * 10 / 2).add(47).add(res.character.xProperty()));
            jumpCurve.controlYProperty().bind(res.character.translateYProperty().add(-80).add(res.character.xProperty()));

            jumpCurve.endXProperty().bind(res.character.translateXProperty().add(-delay * 10).add(47).add(res.character.xProperty()));
            jumpCurve.endYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2).add(res.character.xProperty()));
        } else {
            jumpCurve.startXProperty().bind(res.character.translateXProperty().add(47).add(res.character.xProperty()));
            jumpCurve.startYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2).add(res.character.xProperty()));

            jumpCurve.controlXProperty().bind(res.character.translateXProperty().add(-25 * 10 / 2).add(47).add(res.character.xProperty()));
            jumpCurve.controlYProperty().bind(res.character.translateYProperty().add(-80).add(res.character.xProperty()));

            jumpCurve.endXProperty().bind(res.character.translateXProperty().add(-25 * 10).add(47).add(res.character.xProperty()));
            jumpCurve.endYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2).add(res.character.xProperty()));
        }
    }

    private void jumpRight() {
        if (delay > 25) {
            jumpCurve.startXProperty().bind(res.character.translateXProperty().add(47));
            jumpCurve.startYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2));

            jumpCurve.controlXProperty().bind(res.character.translateXProperty().add(delay * 10 / 2).add(27));
            jumpCurve.controlYProperty().bind(res.character.translateYProperty().add(-80));

            jumpCurve.endXProperty().bind(res.character.translateXProperty().add(delay * 10).add(47));
            jumpCurve.endYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2));
        } else {
            jumpCurve.startXProperty().bind(res.character.translateXProperty().add(47));
            jumpCurve.startYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2));

            jumpCurve.controlXProperty().bind(res.character.translateXProperty().add(25 * 10 / 2).add(47));
            jumpCurve.controlYProperty().bind(res.character.translateYProperty().add(-80));

            jumpCurve.endXProperty().bind(res.character.translateXProperty().add(25 * 10).add(47));
            jumpCurve.endYProperty().bind(res.character.translateYProperty().add(res.character.getFitHeight() / 2));
        }
    }

    private void initCollisionDetection() {
        characterRect.xProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (delay < 40) {
                delay++;
            }
            if (right) {
                if (motionblurEnabled) {
                    motionBlur.setAngle(180);
                    motionBlur.setRadius(delay * 0.5);
                    res.mountain1.setEffect(motionBlur);
                    res.mountain2.setEffect(motionBlur);
                    res.ground1.setEffect(motionBlur);
                    res.ground2.setEffect(motionBlur);
                    res.dust.setEffect(motionBlur);
                }
            } else {
                if (motionblurEnabled) {
                    motionBlur.setAngle(0);
                    motionBlur.setRadius(delay * 0.5);
                    res.mountain1.setEffect(motionBlur);
                    res.mountain2.setEffect(motionBlur);
                    res.ground1.setEffect(motionBlur);
                    res.ground2.setEffect(motionBlur);
                    res.dust.setEffect(motionBlur);
                }
            }
            if (res.character.getTranslateX() + res.character.getX() < -50 && shift) {
                System.out.println("Go Right");
                System.out.println(res.character.getX());
                res.character.setX(res.character.getX() + 1000);
                shift = false;

            } else if (res.character.getTranslateX() + res.character.getX() > 950 && shift) {
                System.out.println("Go Left");
                System.out.println(res.character.getX());
                res.character.setX(res.character.getX() - 1000);
                shift = false;

            } else if (jumpTransition.getStatus() != Animation.Status.RUNNING) {
                shift = true;
                for (ImageView rec : list) {
                    if (characterRect.intersects(rec.getBoundsInParent())) {
                        if (rec.getBoundsInParent().getMinY() > characterRect.getY() + characterRect.getHeight() * 0.7) {
                            res.character.setTranslateY(res.character.getTranslateY() - 1);
                        } else if (right) {
                            translateAnim.stop();
                            res.character.setImage(res.imgCollide);
                            collisiondelay = 5;
                            res.character.setTranslateX(res.character.getTranslateX() - 1);
                            delay = 0;
                        } else {
                            translateAnim.stop();
                            res.character.setImage(res.imgCollide);
                            collisiondelay = 5;
                            res.character.setTranslateX(res.character.getTranslateX() + 1);
                            delay = 0;
                        }
                        break;
                    }
                }
            }

        }
        );
        characterRect.yProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for (ImageView iv : list) {
                Bounds b = iv.getBoundsInParent();
                if (characterRect.intersects(b)) {
                    jumpTransition.stop();
                    jumpReleased = true;
                    if (characterRect.getY() + characterRect.getHeight() - 1 < iv.getY() + b.getHeight() / 2) {

                        res.character.setTranslateY(res.character.getTranslateY() - 2);
                        if (right) {
                            res.character.setTranslateX(res.character.getTranslateX() + 0.25);
                        } else {
                            res.character.setTranslateX(res.character.getTranslateX() - 0.25);
                        }
                        break;
                    } else if (b.contains(characterRect.getX() + characterRect.getWidth() / 2.0, characterRect.getY() - 5)
                            || b.contains(characterRect.getX() + characterRect.getWidth() / 2.0 + 15, characterRect.getY() - 5)
                            || b.contains(characterRect.getX() + characterRect.getWidth() / 2.0 - 15, characterRect.getY() - 5)) {
                    } else {

                        if (right) {
                            res.character.setTranslateX(res.character.getTranslateX() - 5);
                        } else {
                            res.character.setTranslateX(res.character.getTranslateX() + 5);
                        }

                        res.character.setTranslateY(res.character.getTranslateY() + iv.getY() + iv.getBoundsInParent().getHeight() - characterRect.getY() - characterRect.getHeight());

                        break;
                    }

                }
            }
        });
    }

    private void unbindWorld() {
        for (Node n : root.getChildren()) {
            n.translateXProperty().unbind();
        }
        res.mountain2.setTranslateX(res.mountain2.getTranslateX() - 1000);
        res.mountain1.setTranslateX(res.mountain1.getTranslateX() - 1000);

    }

    private void rebindWorld(boolean left) {

        res.sky1.translateXProperty().bind(res.character.translateXProperty().divide(-3.5));
        res.sky2.translateXProperty().bind(res.character.translateXProperty().divide(-3.5).add(999));
        res.ground1.translateXProperty().bind(res.character.translateXProperty().divide(-2));
        res.ground2.translateXProperty().bind(res.character.translateXProperty().divide(-2).add(1000));
        res.mountain1.translateXProperty().bind(res.character.translateXProperty().divide(-3));
        res.mountain2.translateXProperty().bind(res.character.translateXProperty().divide(-3).add(1000));

    }
}
