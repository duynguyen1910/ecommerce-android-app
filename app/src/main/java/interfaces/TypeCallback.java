package interfaces;

import models.TypeValue;

public interface TypeCallback {
    public void updateSelectedTypeValues(TypeValue typeValue);
    public void controlPopUpVariantDialog(boolean popupable);

    public void updateSelectableTypeSet(String typeName);
    public void setVisibleForSelectSizeLayout();




}
