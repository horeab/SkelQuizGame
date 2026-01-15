package libgdx.controls;


import libgdx.screen.AbstractScreen;

public abstract class ScreenRunnable implements Runnable {

    private AbstractScreen abstractScreen;

    public ScreenRunnable(AbstractScreen abstractScreen) {
        this.abstractScreen = abstractScreen;
    }

    @Override
    public void run() {
        if (abstractScreen != null && abstractScreen.isScreenValid()) {
            executeOperations();
        } else {
            executeOperationsAfterScreenChanged();
        }
    }

    public abstract void executeOperations();

    public void executeOperationsAfterScreenChanged() {
    }
}
