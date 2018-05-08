package components;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import components.facades.HighlighterProperties;
import components.facades.HighlighterPropertiesSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.Transient;
import java.util.*;

@State(
        name = "DeMarkLineHighlights",
        storages = {
                @Storage("DeMarkHighlights.xml")
        }
)

public class PersistentHighlightsRepository implements PersistentStateComponent<PersistentHighlightsRepository> {
    @Nullable
    @Override
    @Transient
    public PersistentHighlightsRepository getState() {
        return this;
    }

    @Override
    @Transient
    public void loadState(@NotNull PersistentHighlightsRepository state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /*-------Class begins------*/
    private Map<String, HighlighterPropertiesSet> deMarkHighlighters = new HashMap<>();

    public static PersistentHighlightsRepository getInstance(Project project) {
        return ServiceManager.getService(project, PersistentHighlightsRepository.class);
    }

    @Transient
    public void addDeMarkHighlightToStorage(String filePath, int lineOffset, int layer, int colorOne, int colorTwo) {
        deMarkHighlighters.computeIfAbsent(filePath, k -> new HighlighterPropertiesSet());

        HighlighterProperties highlighterProperties = new HighlighterProperties(lineOffset, layer, colorOne, colorTwo);

        final HighlighterPropertiesSet highlighterPropertiesSet = deMarkHighlighters.get(filePath);
        highlighterPropertiesSet.add(highlighterProperties);
    }

    @Transient
    public void removeDeMarkHighlightFromStorage(String filePath, int lineOffset, int layer, int colorOne, int colorTwo) {
        HighlighterPropertiesSet highlighters = deMarkHighlighters.get(filePath);
        HighlighterProperties highlighterProperties = new HighlighterProperties(lineOffset, layer, colorOne, colorTwo);
      
        if (highlighters != null && highlighters.contains(highlighterProperties)) {
            deMarkHighlighters.get(filePath).remove(highlighterProperties);
        }
    }

    @Transient
    public List<HighlighterProperties> getFileHighlighters(String path) {
        HighlighterPropertiesSet highlighters = deMarkHighlighters.get(path);

        return highlighters != null ? highlighters : new ArrayList<>();
    }
}
