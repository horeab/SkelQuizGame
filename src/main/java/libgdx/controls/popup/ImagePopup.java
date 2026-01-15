package libgdx.controls.popup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public class ImagePopup extends MyPopup<AbstractScreen, AbstractScreenManager> {

    private Actor imageActor;

    public ImagePopup(AbstractScreen screen, Actor imageActor) {
        super(screen);
        this.imageActor = imageActor;
    }

    @Override
    public MyPopup addToPopupManager() {
        MyPopup myPopup = super.addToPopupManager();
        myPopup.getContentTable().add(imageActor);
        myPopup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }

        });
        return myPopup;
    }

    @Override
    protected void setBackground() {
        setBackground(GraphicUtils.getNinePatch(MainResource.transparent_background));
    }

    @Override
    public void addButtons() {
    }

    @Override
    protected String getLabelText() {
        return "";
    }


}
