package components;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import main.java.utils.HighlighterProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.Transient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Map<String, Set<HighlighterProperties>> deMarkHighlighters = new HashMap<>();

    public static PersistentHighlightsRepository getInstance(Project project) {
        return ServiceManager.getService(project, PersistentHighlightsRepository.class);
    }

    @Transient
    public void addDeMarkHighlightToStorage(String filePath, int lineOffset, int layer, int colorOne, int colorTwo) {
        deMarkHighlighters.computeIfAbsent(filePath, k -> new HashSet<HighlighterProperties>());


        HighlighterProperties highlighterProperties = new HighlighterProperties(lineOffset, layer, colorOne, colorTwo);
        deMarkHighlighters.get(filePath).add(highlighterProperties);
    }

    @Transient
    public void removeDeMarkHighlightFromStorage(String filePath, int lineOffset, int layer, int colorOne, int colorTwo) {
        //<editor-fold>
        Set<HighlighterProperties> highlighters = deMarkHighlighters.get(filePath);
        HighlighterProperties highlighterProperties = new HighlighterProperties(lineOffset, layer, colorOne, colorTwo);
        //</editor-fold>
        if (highlighters != null && highlighters.contains(highlighterProperties)) {
            deMarkHighlighters.get(filePath).remove(highlighterProperties);
        }
    }

    @Transient
    public Set<HighlighterProperties> getFileHighlighters(String path) {
        Set<HighlighterProperties> highlighters = deMarkHighlighters.get(path);

        return highlighters != null ? highlighters : new HashSet<>();
    }
}
