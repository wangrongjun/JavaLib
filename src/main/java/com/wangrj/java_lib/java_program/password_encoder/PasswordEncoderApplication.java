package com.wangrj.java_lib.java_program.password_encoder;

import com.wangrj.java_lib.java_util.TextUtil;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 * by wangrongjun on 2018/7/4.
 */
public class PasswordEncoderApplication extends Application {

    private static String[] args;

    public static void main(String[] args) {
        PasswordEncoderApplication.args = args;
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        stage.setWidth(600);
        stage.setHeight(600);
        WebView webView = new WebView();
        String url = getClass().getResource("view/index.html").toString();
        WebEngine engine = webView.getEngine();
        initWebEngine(engine);
        engine.load(url);
        Scene scene = new Scene(webView, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void initWebEngine(WebEngine engine) {
        PasswordEncoderController controller = new PasswordEncoderController();
        engine.getLoadWorker().stateProperty().addListener((ov, t, t1) -> {
            if (t1 == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", controller);
                String encryptionKey = "";
                if (args != null && args.length > 0 && TextUtil.isNotBlank(args[0])) {
                    encryptionKey = args[0];
                }
                window.setMember("encryptionKey", encryptionKey);
            }
        });
    }

}
