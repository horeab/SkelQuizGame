package libgdx.controls.labelimage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.popup.ProVersionPopup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.InAppPurchaseManager;
import libgdx.utils.Utils;

public class InAppPurchaseTable {


    public Table initExtraContentTable() {
        Table extraContentTable = null;
        if (!Utils.isValidExtraContent()) {
            extraContentTable = new Table();
        }
        return extraContentTable;
    }


    public Table createForProVersion(Table extraContentTable, boolean withParentalGate) {
        float sideDimen = getUnlockImageSideDimen();
        Table table = createUnlockTable(extraContentTable, sideDimen, sideDimen);
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ProVersionPopup proVersionPopup = new ProVersionPopup(Game.getInstance().getAbstractScreen(), withParentalGate) {

                };
                proVersionPopup.addToPopupManager();
            }
        });
        return table;
    }

    public Table create(Table extraContentTable, String defaultLanguage, String defaultText, float width, float height) {
        return create(extraContentTable, defaultLanguage, defaultText, new Runnable() {
            @Override
            public void run() {
                InAppPurchaseManager.defaultRedirectScreenRunnable().run();
            }
        }, width, height);
    }

    public Table create(Table extraContentTable, String defaultLanguage, String defaultText, float imgDimen) {
        return create(extraContentTable, defaultLanguage, defaultText, imgDimen, imgDimen);
    }


    public Table create(Table extraContentTable, String defaultLanguage, String defaultText) {
        float sideDimen = getUnlockImageSideDimen();
        return create(extraContentTable, defaultLanguage, defaultText, sideDimen);
    }

    public Table create(Table extraContentTable, String defaultLanguage, String defaultText, final Runnable executeAfterBought, float imgDimen) {
        return create(extraContentTable, defaultLanguage, defaultText, executeAfterBought, imgDimen, imgDimen);
    }


    public Table create(Table extraContentTable, String defaultLanguage, String defaultText, final Runnable executeAfterBought) {
        float sideDimen = getUnlockImageSideDimen();
        return create(extraContentTable, defaultLanguage, defaultText, executeAfterBought, sideDimen, sideDimen);
    }

    public Table create(Table extraContentTable, String defaultLanguage, String defaultText, final Runnable executeAfterBought, float width, float height) {
        Table table = createUnlockTable(extraContentTable, width, height);
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.getInstance().getInAppPurchaseManager().displayInAppPurchasesPopup(defaultLanguage, defaultText, executeAfterBought);
            }
        });
        return table;
    }

    private Table createUnlockTable(Table extraContentTable, float width, float height) {
        Table lockBackgrTable = new Table();
        Image image = GraphicUtils.getImage(getUnlockRes());
        setLockedTableBackground(lockBackgrTable, image);
        image.setWidth(width);
        image.setHeight(height);
        lockBackgrTable.add(image).width(width).height(height);
        Table table = new Table();
        Stack stack = new Stack();
        stack.add(extraContentTable);
        stack.add(lockBackgrTable);
        stack.setWidth(width);
        stack.setHeight(height);
        table.add(stack).width(width).height(height);
        table.setTouchable(Touchable.enabled);
        return table;
    }

    protected Res getUnlockRes() {
        return MainResource.unlock;
    }

    protected void setLockedTableBackground(Table lockBackgrTable, Image image) {
        new ActorAnimation(Game.getInstance().getAbstractScreen()).animateFadeInFadeOut(image);
        lockBackgrTable.setBackground(GraphicUtils.getNinePatch(MainResource.inappurchase_background));
    }

    protected float getUnlockImageSideDimen() {
        return MainDimen.horizontal_general_margin.getDimen() * 15;
    }
}
