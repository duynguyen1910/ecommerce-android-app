package Models;

import java.io.Serializable;

public class SliderItem implements Serializable {
    private String url;

    public SliderItem() {
    }

    public SliderItem(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
