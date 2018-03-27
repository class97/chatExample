package chat.chat;

import android.app.Application;
import android.content.Intent;

public class MainTasks extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Intent controller = new Intent(getBaseContext(), Assistant.class);

        startService(controller);

    }

}
