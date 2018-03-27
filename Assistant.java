package chat.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class Assistant extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BackgroundTasks.getNotified();
        runEveryMinute();
        runEveryHalfMinute();
        runEveryTenSeconds();

        return START_STICKY;

    }

    public void runEveryMinute(){

        final long minute = (60 * 1000);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                if(userChat.activeUser == userChat.userType.Psychologist){
                    BackgroundTasks.PsychologistHandler();
                }

                if(userChat.activeUser == userChat.userType.EndUser){
                    BackgroundTasks.EndUserHandler();
                }

                handler.postDelayed(this, minute);
            }
        }, minute);

    }

    public void runEveryHalfMinute(){

        BackgroundTasks.getNotified();

        final long halfMinute = (30 * 1000);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                BackgroundTasks.getNotified();
                handler.postDelayed(this, halfMinute);
            }
        }, halfMinute);

    }

    public void runEveryTenSeconds(){

        final long tenSeconds = (10 * 1000);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                if(!userChat.chatScreenAlive && userChat.chatOpened){
                }

                if(!userChat.chatScreenAlive && userChat.isChatAlive()){
                    if(userChat.isChatAlive()){
                        if(userChat.activeUser == userChat.userType.Psychologist){
                            Intent intent = new Intent(userChat.context, psChat.class);
                            BackgroundTasks.aliveNotification(userChat.target, "app name", "show conversation", intent);
                            BackgroundTasks.previewPSUnseenMessages();
                        }
                        if(userChat.activeUser == userChat.userType.EndUser){
                            Intent intent = new Intent(userChat.context, userChat.class);
                            BackgroundTasks.aliveNotification(userChat.target, "app name", "show conversation", intent);
                            BackgroundTasks.previewEUUnseenMessages();
                        }
                    }
                }

                handler.postDelayed(this, tenSeconds);
            }
        }, tenSeconds);

    }

    @Override
    public void onDestroy() {

    }

}
