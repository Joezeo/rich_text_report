package com.toocol.rich_text_report;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.MultiChangeBuilder;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/8/29 23:31
 * @version: 0.0.1
 */
public class BugDemo extends Application {

    private final TextStyle defaultStyle = TextStyle.EMPTY.updateFontSize(10).updateFontFamily("Consolas").updateTextColor(Color.BLACK);
    private final TextStyle coloredStyle = TextStyle.EMPTY.updateFontSize(10).updateFontFamily("Consolas").updateTextColor(Color.CYAN);

    private TextStyle currentStyle = defaultStyle;

    private final Queue<TextStyle> queue = new ArrayDeque<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(412, 800);

        BugTextArea area = new BugTextArea();
        area.setWrapText(true);
        area.setStyle("-fx-font-family: \"Consolas\";-fx-font-size: 10pt;-fx-padding: 15 15 5 15;");
        area.prefHeightProperty().bind(pane.heightProperty());
        area.prefWidthProperty().bind(pane.widthProperty());
        area.textProperty().addListener((ob, ov, nv) -> {
            int par9Line = area.getParagraphLinesCount(9);
            int par18Line = area.getParagraphLinesCount(18);
            System.out.println("par9 = " + par9Line);
            System.out.println("par18 = " + par18Line);
        });

        VirtualizedScrollPane<BugTextArea> scrollPane = new VirtualizedScrollPane<>(area);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.getChildren().add(scrollPane);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

        appendText(area);
    }

    {
        for (int i = 0; i < 26; i++) {
            queue.offer(coloredStyle);
            queue.offer(defaultStyle);
        }
    }
    public void appendText(BugTextArea area) {
        String text = """
                [root@localhost /]# ll
                total 76
                lrwxrwxrwx.  1 root root     7 Sep 14 2020  $bin$ -> $usr/bin$
                dr-xr-xr-x.  5 root root  4096 May 24 00:28 $boot$
                drwxr-xr-x  10 root root  4096 Jul 25 22:27 $data$
                drwxr-xr-x  19 root root  2980 Jun  7 14:03 $dev$
                drwxr-xr-x. 85 root root 12288 Apr  3 00:42 $etc$
                drwxr-xr-x.  4 root root  4096 Oct 31  2021 $home$
                lrwxrwxrwx.  1 root root     7 Sep 14  2020 $lib$ -> $usr/lib$
                lrwxrwxrwx.  1 root root     9 Sep 14  2020 $lib64$ -> $usr/lib64$
                drwx------.  2 root root 16384 Sep 14  2020 $lost+found$
                drwxr-xr-x.  2 root root  4096 Apr 11  2018 $media$
                drwxr-xr-x.  2 root root  4096 Apr 11  2018 $mnt$
                drwxr-xr-x.  2 root root  4096 Apr 11  2018 $opt$
                dr-xr-xr-x  91 root root     0 Apr  3 00:58 $proc$
                drwxr-xr-x   8 root root  4096 Apr  1 10:17 $project$
                dr-xr-x---. 13 root root  4096 Aug 29 11:12 $root$
                drwxr-xr-x  25 root root   700 Aug 13 01:02 $run$
                lrwxrwxrwx.  1 root root     8 Sep 14  2020 $sbin$ -> $usr/sbin$
                drwxr-xr-x.  2 root root  4096 Apr 11  2018 $srv$
                dr-xr-xr-x  13 root root     0 Apr  4 00:46 $sys$
                drwxrwxrwt.  9 root root  4096 Aug 29 11:12 $tmp$
                drwxr-xr-x. 13 root root  4096 Sep 14  2020 $usr$
                drwxr-xr-x. 20 root root  4096 Nov  4  2021 $var$
                [root@localhost /]#
                """.trim();
        MultiChangeBuilder<ParStyle, String, TextStyle> change = area.createMultiChange();
        for (String sp : text.split("\n")) {
            for (String s : sp.split("\\$")) {
                s = (StringUtils.join(s.toCharArray(), '\uFEFF') + '\uFEFF').replaceAll(" ", "\u00A0");
                change.replace(area.getCaretPosition(), area.getCaretPosition(),
                        ReadOnlyStyledDocument.fromString(s, ParStyle.EMPTY, currentStyle, area.getSegOps())
                );
                if (!queue.isEmpty()) {
                    currentStyle = queue.poll();
                }
            }
            change.insertText(area.getCaretPosition(), "\n");
        }
        change.commit();
    }
}
