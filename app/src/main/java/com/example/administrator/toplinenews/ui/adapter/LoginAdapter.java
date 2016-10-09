package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.entity.LoginLog;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
/*public class LoginAdapter extends MyBaseAdapter {
    List<LoginLog> list;
    public LoginAdapter(Context context, List<LoginLog> list) {
        super(context);
        this.list=list;
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {

        HoldView holdView = null;
        convertView = inflater.inflate(R.layout.loginloglist, null);
        holdView = new HoldView(convertView);
        LoginLog loginLog = list.get(position);
        holdView.tv_data.setText(loginLog.getTime());
        holdView.tv_place.setText(loginLog.getAddress());
        holdView.tv_type.setText(loginLog.getDevice()==1?"网页端":"手机端");
        return convertView;
    }

    private class HoldView {
        public TextView tv_data;
        public TextView tv_place;
        public TextView tv_type;
        public HoldView(View view)
        {
            this.tv_data= (TextView) view.findViewById(R.id.list_data);
            tv_place = (TextView) view.findViewById(R.id.list_place);
            tv_type = (TextView) view.findViewById(R.id.list_type);
        }
    }
}*/
public class LoginAdapter extends MyBaseAdapter<LoginLog> {

    public LoginAdapter(Context context ) {
        super(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public LoginLog getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.loginloglist, null);

            viewHolder.loginTimeTv = (TextView) convertView.findViewById(R.id.list_place);
            viewHolder.loginAddTv = (TextView) convertView.findViewById(R.id.list_data);
            viewHolder.loginTypeTv = (TextView) convertView.findViewById(R.id.list_type);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LoginLog log =myList.get(position);
        viewHolder.loginTimeTv.setText(log.getTime().split(" ")[0]);
        viewHolder.loginAddTv.setText(log.getAddress());
        viewHolder.loginTypeTv.setText(log.getDevice()== 1?"网页端":"手机端");
        return convertView;
    }


    class ViewHolder{
        TextView loginTimeTv , loginAddTv , loginTypeTv;
    }

}