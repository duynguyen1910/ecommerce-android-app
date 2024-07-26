package interfaces;

import models.TypeValue;

public interface TypeCallback {
    public void updateSelectedTypeValues(TypeValue typeValue);
    public void controlPopUpVariantDialog(boolean popupable);
}
