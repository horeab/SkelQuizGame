package libgdx.campaign;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.graphics.GraphicUtils;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;

public class StarsBarCreator {

    private long wonStars;

    public StarsBarCreator(long wonStars) {
        this.wonStars = wonStars;
    }

    public Table createHorizontalStarsBar(float sideDimen) {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float rightMargin = verticalGeneralMarginDimen / 2;
        for (int i = 1; i <= StarsService.NR_OF_STARS_TO_DISPLAY; i++) {
            Resource starsRes = i <= wonStars ? Resource.star_enabled : Resource.star_disabled;
            table.add(GraphicUtils.getImage(starsRes)).height(sideDimen).width(sideDimen).padRight(rightMargin).expand();
        }
        table.setHeight(sideDimen);
        table.setWidth((sideDimen + rightMargin) * StarsService.NR_OF_STARS_TO_DISPLAY);
        return table;
    }

}
