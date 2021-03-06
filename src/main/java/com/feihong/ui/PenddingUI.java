package com.feihong.ui;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//参考：https://blog.csdn.net/loongshawn/article/details/52996382
public class PenddingUI {
    private Stage dialogStage;
    private ProgressIndicator progressIndicator;

    public PenddingUI(final Task<?> task, Stage primaryStage) {
        dialogStage = new Stage();
        progressIndicator = new ProgressIndicator();

        // 需要添加窗口父子关系属性，不然加载窗口会与父窗口并存，形成2个窗口，解决这个问题需要在加载页面代码中添加 dialogStage.initOwner(primaryStage)
        // 这样加载窗口就会与父窗口融合为一个窗口
        dialogStage.initOwner(primaryStage);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        //设置stage为透明
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.initModality(Modality.APPLICATION_MODAL);


        //label.getStyleClass().add("progress-bar-root");
        progressIndicator.setProgress(-1F);
        progressIndicator.getStyleClass().add("progress-bar-root");
        progressIndicator.progressProperty().bind(task.progressProperty());

        //在 VBox 中可以加入一些其他的控件，如 label 等
        VBox vBox = new VBox();
//        vBox.setSpacing(10);
        //这个必须有，设置背景为透明，必须和 stage 同时为透明，才不会遮盖原本的UI
        vBox.setBackground(Background.EMPTY);
        vBox.getChildren().addAll(progressIndicator);

        Scene scene = new Scene(vBox);
        scene.setFill(null);
        dialogStage.setScene(scene);

        Thread inner = new Thread(task);
        inner.start();

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent event) {
                dialogStage.close();
            }
        });
    }

    public void activateProgressBar() {
        dialogStage.show();
    }
}
