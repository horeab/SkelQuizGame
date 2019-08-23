package libgdx.implementations.skelgame;

public enum QuizGameButtonSize implements libgdx.controls.button.ButtonSize {

    ;
    private float width;
    private float height;

    QuizGameButtonSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
