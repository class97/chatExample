package chat.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import chat.chat.R;

public class listMessage extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<String> itemDate;
    private final ArrayList<String> userName;
    private final ArrayList<Integer> type;

    public listMessage(Activity context, ArrayList<String> itemname, ArrayList<String> itemDate,
                               ArrayList<String> userName, ArrayList<Integer> type) {

        super(context, R.layout.list_item, itemname);

        this.context = context;
        this.itemname = itemname;
        this.itemDate = itemDate;
        this.userName = userName;
        this.type = type;

    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView;

        if(type.get(position) == 0){

            rowView = inflater.inflate(R.layout.list_chat_other_message, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.text_message_body);
            TextView txtSmall = (TextView) rowView.findViewById(R.id.text_message_time);

            txtTitle.setText(itemname.get(position));
            txtSmall.setText(itemDate.get(position));

        }else{

            rowView = inflater.inflate(R.layout.list_chat_message, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.text_message_body);
            TextView txtName  = (TextView) rowView.findViewById(R.id.text_message_name);
            TextView txtSmall = (TextView) rowView.findViewById(R.id.text_message_time);

            txtTitle.setText(itemname.get(position));
            txtSmall.setText(itemDate.get(position));
            txtName.setText(userName.get(position));

        }

        return rowView;

    }
}
