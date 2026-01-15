package libgdx.screen;

import com.badlogic.gdx.utils.Align;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.resources.FontManager;
import libgdx.utils.model.FontColor;

public abstract class PercentCounter {

    private float counter = 0.00f;
    private AbstractScreen currentScreen;
    private MyWrappedLabel displayLabel;
    private float fontScale = FontManager.getBigFontDim();

    public PercentCounter(AbstractScreen currentScreen) {
        this.currentScreen = currentScreen;
        displayLabel = new MyWrappedLabel();
    }

    public void setFontScale(final float fontScale) {
        this.fontScale = fontScale;
    }

    public void start(final float percentToReach) {
        displayLabel.setFontScale(fontScale);
        displayLabel.setStyleDependingOnContrast();
        displayLabel.setAlignment(Align.center);
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final PercentCounter thisCounter = this;
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        displayLabel.setTextColor(FontColor.RED);
        displayLabel.setFontScale(fontScale * 1.2f);
        executorService.scheduleAtFixedRate(new ScreenRunnable(currentScreen) {
            @Override
            public void executeOperations() {
                float incrementVal = 0.1f;
                if (thisCounter.counter < (percentToReach / 3)) {
                    incrementVal = 5f;
                } else if (thisCounter.counter < (percentToReach / 2)) {
                    incrementVal = 2f;
                } else if (thisCounter.counter < (percentToReach / 1.6f)) {
                    incrementVal = 1f;
                } else if (thisCounter.counter < (percentToReach / 1.1f)) {
                    incrementVal = 0.5f;
                }
                thisCounter.counter = thisCounter.counter + incrementVal * 3;
                if (thisCounter.counter > percentToReach) {
                    thisCounter.counter = percentToReach;
                    executorService.shutdown();
                    displayLabel.setText(String.format("%.02f", counter) + "%");
                    executeAfterCountDownCounter();
                }
                displayLabel.setText(String.format("%.02f", counter) + "%");
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public MyWrappedLabel getDisplayLabel() {
        return displayLabel;
    }

    public abstract void executeAfterCountDownCounter();
}
