package cr.ac.una.kingdomfantasy.util;

import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author ccarranza
 */
public final class BindingUtils {

    // Each ToggleGroup keeps its own listener so it can be removed later
    // without affecting the bindings of other groups.
    private static final Map<ToggleGroup, ChangeListener<Toggle>> listeners = new WeakHashMap<>();

    private BindingUtils() {
    }

    public static <T> void bindToggleGroupToProperty(final ToggleGroup toggleGroup, final ObjectProperty<T> property) {
        // Check all toggles for required user data
        toggleGroup.getToggles().forEach(toggle -> {
            if (toggle.getUserData() == null) {
                throw new IllegalArgumentException("The ToggleGroup contains at least one Toggle without user data!");
            }
        });
        // Select initial toggle for current property state
        for (Toggle toggle : toggleGroup.getToggles()) {
            if (property.getValue() != null && property.getValue().equals(toggle.getUserData())) {
                toggleGroup.selectToggle(toggle);
                break;
            }
        }
        // Update property value on toggle selection changes
        ChangeListener<Toggle> listener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                property.setValue((T) newValue.getUserData());
            }
        };
        listeners.put(toggleGroup, listener);
        toggleGroup.selectedToggleProperty().addListener(listener);
    }

    public static <T> void unbindToggleGroupToProperty(final ToggleGroup toggleGroup, final ObjectProperty<T> property) {
        ChangeListener<Toggle> listener = listeners.remove(toggleGroup);
        if (listener != null) {
            toggleGroup.selectedToggleProperty().removeListener(listener);
        }
    }
}
