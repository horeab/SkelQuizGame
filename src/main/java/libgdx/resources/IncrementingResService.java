package libgdx.resources;

import com.badlogic.gdx.graphics.Texture;

import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;

public class IncrementingResService {

    private List<? extends IncrementingRes> incrementingResList;

    IncrementingResService(List<? extends IncrementingRes> incrementingResList) {
        this.incrementingResList = incrementingResList;
    }

    public List<? extends Res> createRes() {
        List<ResImpl> res = new ArrayList<>();
        final MutableInt index = new MutableInt();
        for (final IncrementingRes incrementingRes : incrementingResList) {
            for (int i = incrementingRes.getBeginIndex(); i <= incrementingRes.getEndIndex(); i++) {
                index.setValue(i);
                res.add(new ResImpl(String.format(incrementingRes.getPath(), index.getValue()),
                        Texture.class,
                        String.format(incrementingRes.getResName(), index.getValue())));
            }
        }
        return res;
    }

}
