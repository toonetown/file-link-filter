package com.toonetown.intellij.file_link_filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

/**
 * A class which filters the output and turns things that look like files into links
 */
public class FileLinkFilter implements Filter {

    private static final Pattern FILE_PATTERN =
                Pattern.compile("(/[a-z0-9/\\+\\-_\\. ]+[\\.][a-z]+)([:\\[]+(\\d+))?([:,]+(\\d+))?[:,\\[\\] ]",
                                Pattern.CASE_INSENSITIVE);

    /** The project */
    private final Project project;
    public FileLinkFilter(final Project project) {
        this.project = project;
    }

    @Nullable @Override
    public Result applyFilter(final String str, final int endPoint) {
        final int startPoint = endPoint - str.length();
        final Matcher matcher = FILE_PATTERN.matcher(str);
        if (matcher.find()) {
            final VirtualFile file = project.getBaseDir().getFileSystem().findFileByPath(matcher.group(1));
            /* Only match if it exists, and is non-binary */
            if (file != null && !file.getFileType().isBinary()) {
                final int lineNum = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3)) - 1;
                final int column = matcher.group(5) == null ? 0 : Integer.parseInt(matcher.group(5)) - 1;
                OpenFileDescriptor fileDescriptor = new OpenFileDescriptor(project, file, lineNum, column);
                return new Result(startPoint + matcher.start(1),
                                  startPoint + matcher.end(1),
                                  new OpenFileHyperlinkInfo(fileDescriptor));
            }
        }
        return new Result(startPoint, endPoint, null, new TextAttributes());
    }
}
