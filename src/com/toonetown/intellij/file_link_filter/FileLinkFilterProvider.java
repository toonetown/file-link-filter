package com.toonetown.intellij.file_link_filter;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Default provider for file link filtering
 */
public class FileLinkFilterProvider implements ConsoleFilterProvider {
    @NotNull @Override
    public Filter[] getDefaultFilters(@NotNull Project project) {
        Filter filter = new FileLinkFilter(project);
        return new Filter[]{ filter };
    }
}
