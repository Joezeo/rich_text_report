package com.toocol.rich_text_report;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.SegmentOps;
import org.fxmisc.richtext.model.StyledSegment;
import org.fxmisc.richtext.model.TextOps;

import java.util.function.BiConsumer;

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/8/30 0:22
 */
public class BugTextArea extends GenericStyledArea<ParStyle, String, TextStyle> {
    public BugTextArea() {
        super(
                ParStyle.EMPTY,                                                 // default paragraph style
                (paragraph, style) -> paragraph.setStyle(style.toCss()),        // paragraph style setter
                TextStyle.EMPTY,  // default segment style
                styledTextOps,
                seg -> createNode(seg, (text, style) -> text.setStyle(style.toCss())));                     // Node creator and segment style setter
    }
    private static TextOps<String, TextStyle> styledTextOps = SegmentOps.styledTextOps();

    private static Node createNode( StyledSegment<String, TextStyle> seg,
                                    BiConsumer<? super TextExt, TextStyle> applyStyle ) {
        return StyledTextArea.createStyledTextNode(seg.getSegment(), seg.getStyle(), applyStyle);
    }
}
