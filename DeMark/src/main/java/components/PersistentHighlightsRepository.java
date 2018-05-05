package components;

import com.intellij.openapi.components.*;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
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
    public Map<String, Set<RangeHighlighter>> deMarkHighlighters = new HashMap<String, Set<RangeHighlighter>>();

    public static PersistentHighlightsRepository getInstance(Project project) {
        return ServiceManager.getService(project, PersistentHighlightsRepository.class);
    }

    @Transient
    public void addDeMarkHighlightToStorage(String filePath, RangeHighlighter highlighter) {
        deMarkHighlighters.computeIfAbsent(filePath, k -> new HashSet<RangeHighlighter>());
        deMarkHighlighters.get(filePath).add(highlighter);
    }

    @Transient
    public void removeDeMarkHighlightFromStorage(String filePath, RangeHighlighter highlighter) {
        Set<RangeHighlighter> highlighters = deMarkHighlighters.get(filePath);
        if (highlighters != null && highlighters.contains(highlighter)) {
            deMarkHighlighters.get(filePath).remove(highlighter);
        }
    }

    @Transient
    public Set<RangeHighlighter> getFileHighlighters(String path) {
        Set<RangeHighlighter> highlighters = deMarkHighlighters.get(path);

        return highlighters != null ? highlighters : new HashSet<>();
    }

}
