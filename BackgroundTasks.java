package Accessories;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import Manage.EndUser;
import Manage.Psychologist;
import mobileapp.mobileapp.R;
import mobileapp.mobileapp.psychologist.*;
import mobileapp.mobileapp.enduser.*;

public class BackgroundTasks {

    public static ArrayList<Integer> showed = new ArrayList<>();

    private final static Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private final static long[] pattern = {500,500,500,500,500,500};

    public static void getNotified(){

        if(!Base.isSigned()) return;

        if(Base.user == Base.userType.EndUser) getEndUserNotifications();
        else getPsychologistNotifications();

    }

    public static void previewEUUnseenMessages(){

        EndUser user = new EndUser();

        if(!user.retrieve()){
            return;
        }

        Base.isChatAlive();

        HashMap<Integer, ArrayList<String>> chatMessages = user.chatUnseenMessages(Base.target);
        TreeMap<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>(chatMessages);

        if(map.size() == 0){
            return;
        }

        for(TreeMap.Entry<Integer, ArrayList<String>> entry : map.entrySet()){
            boolean show = true;
            for(int x : showed){
                if(x == entry.getKey()){
                    show = false;
                }
            }

            if(!show) continue;
            showed.add(entry.getKey());

            Intent intent = new Intent(Base.context, userChat.class);
            showNotification(Base.target, "رسالة جديدة", entry.getValue().get(3), intent);
        }

    }

    public static void previewPSUnseenMessages(){

        Psychologist user = new Psychologist();

        if(!user.retrieve()){
            return;
        }

        Base.isChatAlive();

        HashMap<Integer, ArrayList<String>> chatMessages = user.chatUnseenMessages(Base.target);
        TreeMap<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>(chatMessages);

        if(map.size() == 0){
            return;
        }

        for(TreeMap.Entry<Integer, ArrayList<String>> entry : map.entrySet()){
            boolean show = true;
            for(int x : showed){
                if(x == entry.getKey()){
                    show = false;
                }
            }

            if(!show) continue;
            showed.add(entry.getKey());

            Intent intent = new Intent(Base.context, psChat.class);
            showNotification(Base.target, "رسالة جديدة", entry.getValue().get(3), intent);
        }

    }

    public static void EndUserHandler(){

        EndUser user = new EndUser();

        if(!user.retrieve()) return;

        ArrayList<Integer> data = user.anyChatToJoin();

        for(int chat : data){
            Intent intent = new Intent(Base.context, userChat.class);
            Base.target = chat;
            showNotification(chat, "وتطمئن قلوبهم", "لديك محادثة مع الطبيب الان الطبيب فى انتظارك", intent);
        }

    }

    public static void PsychologistHandler(){

        Psychologist user = new Psychologist();

        if(!user.retrieve()) return;

        ArrayList<Integer> data = user.anyChatToStart();

        for(int chat : data){
            Base.target = chat;
            Intent intent = new Intent(Base.context, startConsolationPS.class);
            showNotification(chat, "وتطمئن قلوبهم", "لديك محادثة مع المريض الان المريض فى انتظارك", intent);
        }

        data = user.anyChatToClose();

        for(int chat : data){
            Base.target = chat;
            Intent intent = new Intent(Base.context, psChat.class);
            showNotification(chat, "وتطمئن قلوبهم", "انتهى الوقت المخصص للمحادثة مع المريض", intent);
        }

    }

    public static void getEndUserNotifications(){

        EndUser user = new EndUser();
        HashMap<Integer,ArrayList<String>> notifications = user.getNotifications();

        Intent intent = new Intent(Base.context, Base.context.getClass());

        for(HashMap.Entry<Integer,ArrayList<String>> notification : notifications.entrySet()){
            if(notification.getValue().get(1).equals("myOrders")){
                intent = new Intent(Base.context, myOrders.class);
            }
            if(notification.getValue().get(1).equals("myConsolations")){
                intent = new Intent(Base.context, myConsolation.class);
            }
            if(notification.getValue().get(1).equals("mainMenu")){
                intent = new Intent(Base.context, mainMenu.class);
            }
            if(notification.getValue().get(1).equals("startConsolationEU")){
                intent = new Intent(Base.context, startConsolationEU.class);
                Base.target = Integer.parseInt(notification.getValue().get(2));
            }
            showNotification(notification.getKey(), "وتطمئن قلوبهم", notification.getValue().get(0), intent);
            user.setNotificationDone(notification.getKey());
        }

    }

    public static void getPsychologistNotifications(){

        Psychologist user = new Psychologist();
        HashMap<Integer,ArrayList<String>> notifications = user.getNotifications();

        Intent intent = new Intent(Base.context, Base.context.getClass());

        for(HashMap.Entry<Integer,ArrayList<String>> notification : notifications.entrySet()){
            if(notification.getValue().get(1).equals("getOrders")){
                intent = new Intent(Base.context, getOrders.class);
            }
            if(notification.getValue().get(1).equals("startConsolationPS")){
                intent = new Intent(Base.context, startConsolationPS.class);
                Base.target = Integer.parseInt(notification.getValue().get(2));
            }
            showNotification(notification.getKey(), "وتطمئن قلوبهم", notification.getValue().get(0), intent);
            if(!user.setNotificationDone(notification.getKey())){
                Toast.makeText(Base.context, user.message(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public static void showNotification(int id, String address, String title, Intent intent){

        PendingIntent contentIntent = PendingIntent.getActivity(Base.context, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Base.context);

        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(Base.context.getResources(),R.drawable.untitled));
        notificationBuilder.setSmallIcon(R.drawable.untitled);
        notificationBuilder.setContentTitle(address);
        notificationBuilder.setContentText(title);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(pattern);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) Base.context.getSystemService(Base.context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(address, title, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notificationBuilder.build());

    }

    public static void aliveNotification(int id, String address, String title, Intent intent){

        PendingIntent contentIntent = PendingIntent.getActivity(Base.context, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Base.context);

        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(Base.context.getResources(),R.drawable.untitled));
        notificationBuilder.setSmallIcon(R.drawable.untitled);
        notificationBuilder.setContentTitle(address);
        notificationBuilder.setContentText(title);
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationBuilder.setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) Base.context.getSystemService(Base.context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(address, title, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notificationBuilder.build());

    }

}
