package libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.popup.MyPopupManager;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupManager;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ActorPositionManager;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public abstract class AbstractScreen<TScreenManager extends AbstractScreenManager> implements com.badlogic.gdx.Screen {

    public static final String BACKGROUND_CONTAINER_NAME = "backgroundContainer";

    protected TScreenManager screenManager = (TScreenManager) Game.getInstance().getScreenManager();

    private boolean isScreenValid = true;

    private MyNotificationPopupManager myNotificationPopupManager;
    private MyPopupManager myPopupManager;

    private RGBColor backgroundColor;
    private Long millisScreenFirstTimeDisplayed;
    private Group contentGroup;
    private Stage backgroundStage;
    private Stage contentStage;
    private Table allTable;

    public AbstractScreen() {
        contentStage = new Stage(new ExtendViewport(ScreenDimensionsManager.getScreenWidth(), ScreenDimensionsManager.getScreenHeight()));
        backgroundStage = new Stage(new ExtendViewport(ScreenDimensionsManager.getExternalDeviceWidth(), ScreenDimensionsManager.getExternalDeviceHeight()));
        backgroundColor = Game.getInstance().getSubGameDependencyManager().getScreenBackgroundColor();
        myPopupManager = new MyPopupManager(this);
        myNotificationPopupManager = new MyNotificationPopupManager(this);
        millisScreenFirstTimeDisplayed = DateUtils.getNowMillis();
        processGdxInput();
        createContentTable();
        myPopupManager.hideAllDisplayedPopups();
    }

    public boolean isLoading() {
        return getStage().getRoot().findActor(AbstractScreenManager.LOADING_LABEL_NAME) != null;
    }

    public AbstractScreen getAbstractScreen() {
        return this;
    }

    public Group getRoot() {
        return contentStage.getRoot();
    }

    public Stage getStage() {
        return contentStage;
    }

    private void processPopups() {
        myPopupManager.showPopupsWaitingToBeDisplayed();
        myNotificationPopupManager.showPopupsWaitingToBeDisplayed();
        myPopupManager.bringDisplayedPopupsToFront();
        if (!myPopupManager.isPopupDisplayed()) {
            myNotificationPopupManager.bringDisplayedPopupsToFront();
        }
    }

    private void processBackKeyPressed() {
        if ((Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) && !AbstractScreenManager.backAlreadyKeyPressed) {
            AbstractScreenManager.backAlreadyKeyPressed = true;
            if (myPopupManager.isPopupDisplayed()) {
                myPopupManager.onBackKeyPress();
                myPopupManager.hideAllDisplayedPopups();
            } else {
                onBackKeyPress();
            }
        } else {
            AbstractScreenManager.backAlreadyKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE));
        }
    }

    private void processGdxInput() {
        Gdx.input.setInputProcessor(contentStage);
        Gdx.input.setOnscreenKeyboardVisible(false);
    }

    public long getMillisPassedSinceScreenDisplayed() {
        return DateUtils.getNowMillis() - millisScreenFirstTimeDisplayed;
    }

    public Long getMillisScreenFirstTimeDisplayed() {
        return millisScreenFirstTimeDisplayed;
    }

    public void setBackgroundColor(RGBColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public RGBColor getBackgroundColor() {
        return backgroundColor;
    }

    public MyNotificationPopupManager getMyNotificationPopupManager() {
        return myNotificationPopupManager;
    }

    public MyPopupManager getMyPopupManager() {
        return myPopupManager;
    }

    public void addActor(Actor actor) {
        contentGroup.addActor(actor);
    }

    void removeActor(Actor actor) {
        contentGroup.removeActor(actor);
    }

    private void createContentTable() {
        contentGroup = new Group();
        contentGroup.setWidth(ScreenDimensionsManager.getScreenWidth());
        contentGroup.setHeight(ScreenDimensionsManager.getScreenHeight());

        Container<Group> contentContainer = createFullScreenContainer();
        contentContainer.setActor(contentGroup);
        contentStage.addActor(contentContainer);

        Container<Group> backgroundContainer = createFullScreenContainer();
        backgroundContainer.setName(BACKGROUND_CONTAINER_NAME);
        setBackgroundContainer(backgroundContainer);
        backgroundContainer.setFillParent(true);
        backgroundStage.addActor(backgroundContainer);
    }

    public Stage getBackgroundStage() {
        return backgroundStage;
    }

    protected void setBackgroundContainer(Container<Group> backgroundContainer) {
        Image backgr = GraphicUtils.getImage(getBackgroundTexture());
        if (!String.format("%.2f", backgr.getHeight() / backgr.getWidth()).equals(String.format("%.2f", ScreenDimensionsManager.STANDARD_SCREEN_RATIO))) {
            backgr = GraphicUtils.addTiledImage(getBackgroundTexture(), 0, Texture.TextureWrap.Repeat, ScreenDimensionsManager.getExternalDeviceHeightValue(60));
        }
        backgroundContainer.setBackground(backgr.getDrawable());
    }

    protected Res getBackgroundTexture() {
        return MainResource.background_texture;
    }

    private Container<Group> createFullScreenContainer() {
        Container<Group> container = new Container<Group>();
        container.setWidth(ScreenDimensionsManager.getExternalDeviceWidth());
        container.setHeight(ScreenDimensionsManager.getExternalDeviceHeight());
        ActorPositionManager.setActorCenterExternalScreen(container);
        return container;
    }

    public TScreenManager getScreenManager() {
        return screenManager;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(backgroundColor.r / 255f, backgroundColor.g / 255f, backgroundColor.b / 255f, backgroundColor.a);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        contentStage.getViewport().apply();
        contentStage.act(delta);
        processDraw();
        processPopups();
        processBackKeyPressed();
    }

    private void processDraw() {
        try {
            backgroundStage.draw();
            contentStage.draw();
        } catch (StringIndexOutOfBoundsException e) {
            //Should be ignored, the string should not be displayed
        }
    }

    public abstract void onBackKeyPress();

    public abstract void buildStage();

    public void afterBuildStage() {
    }

    protected void displayNotifications() {
    }

    protected void initFields() {
    }

    protected void executeShopTransactions() {
    }

    public boolean isScreenValid() {
        return isScreenValid;
    }

    public void setScreenInvalid() {
        isScreenValid = false;
    }

    public void addAction(Action action) {
        contentStage.getRoot().addAction(action);
    }

    @Override
    public void resize(int width, int height) {
        ScreenDimensionsManager.resizeViewPort(contentStage.getViewport(), width, height);
        backgroundStage.getViewport().update(ScreenDimensionsManager.getExternalDeviceWidth(), ScreenDimensionsManager.getExternalDeviceHeight(), true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        backgroundStage.dispose();
        contentStage.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(contentStage);
    }

    protected Table getAllTable() {
        return allTable;
    }

    protected void setUpAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        addActor(allTable);
        new ActorAnimation(getAbstractScreen()).animateFastFadeIn(allTable);
    }
}