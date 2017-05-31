
package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utils.L;
import com.yiqu.iyijiayi.ImagePagerActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailFragment;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailPicFragment;
import com.yiqu.iyijiayi.fragment.tab1.ItemDetailTextFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.EmojiCharacterUtil;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.view.MultiView.ExpandTextView;
import com.yiqu.iyijiayi.view.MultiView.MultiImageView;
import com.yiqu.iyijiayi.view.MultiView.bean.PhotoInfo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Tab1XizuoAdapter extends BaseAdapter implements OnItemClickListener {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sound> datas = new ArrayList<Sound>();
    private Context mContext;
    private String fragmentName;
    private int mCurrent = -1;
    private OnMoreClickListener mListener;

    //为三种布局定义一个标识
    private final int TYPE_IMAGE = 0;
    private final int TYPE_SOUND = 1;
    private final int TYPE_QUESTION = 2;

    public Tab1XizuoAdapter(Context mContext, String fragmentName) {

        this.mContext = mContext;
        this.fragmentName = fragmentName;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    //这个方法必须重写，它返回了有几种不同的布局
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    // 每个convertView都会调用此方法，获得当前应该加载的布局样式
    @Override
    public int getItemViewType(int position) {
        //获取当前布局的数据
        Sound sound = datas.get(position);

        if (sound.stype == 1) {
            return TYPE_QUESTION;
        } else if (sound.stype == 2) {
            return TYPE_SOUND;
        } else {
            return TYPE_IMAGE;
        }
    }

    public void setCurrent(int current) {
        mCurrent = current;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<Sound> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Sound> allDatas) {
        datas.addAll(allDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Sound getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder1 {
        TextView musicname;
        ExpandTextView desc;
        TextView time;
        TextView tea_name;
        TextView tectitle;
        ImageView stu_header;
        ImageView tea_header;
        ImageView musictype;
        TextView comment;
        TextView listener;
        TextView tea_listen;
        TextView like;
        LinearLayout ll_question;
    }

    private class ViewHolder2 {
        TextView musicname;
        ExpandTextView content;
        TextView author;
        TextView comment;
        TextView like;
        TextView listener;
        ImageView icon;
        ImageView album;
        TextView publish_time;
        ImageView musictype;
        ImageView play_status;
        ImageView iv_status;
    }

    private class ViewHolder3 {
        ImageView header;
        TextView author;
        ImageView iv_status;
        TextView publish_time;
        ExpandTextView content;
        MultiImageView multiImageView;
        TextView listener;
        TextView comment;
        TextView like;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);

        try {
            ViewHolder1 holder1 = null;
            ViewHolder2 holder2 = null;
//            ViewHolder1 holder1 = null;
            ViewHolder3 holder3 = null;
            if (v == null) {
                switch (type) {
                    case TYPE_QUESTION:
                        holder1 = new ViewHolder1();
                        v = mLayoutInflater.inflate(R.layout.tab1_question_list_adapter, null);
                        holder1.musicname = (TextView) v.findViewById(R.id.musicname);
                        holder1.desc = (ExpandTextView) v.findViewById(R.id.desc);
                        holder1.time = (TextView) v.findViewById(R.id.time);
                        holder1.tea_name = (TextView) v.findViewById(R.id.tea_name);
                        holder1.tectitle = (TextView) v.findViewById(R.id.tectitle);
                        holder1.stu_header = (ImageView) v.findViewById(R.id.stu_header);
                        holder1.musictype = (ImageView) v.findViewById(R.id.musictype);
                        holder1.listener = (TextView) v.findViewById(R.id.listener);
                        holder1.tea_listen = (TextView) v.findViewById(R.id.tea_listen);
                        holder1.like = (TextView) v.findViewById(R.id.like);
                        holder1.tea_header = (ImageView) v.findViewById(R.id.tea_header);
                        holder1.comment = (TextView) v.findViewById(R.id.comment);
                        holder1.ll_question = (LinearLayout) v.findViewById(R.id.ll_question);
                        v.setTag(holder1);
                        break;
                    case TYPE_SOUND:
                        holder2 = new ViewHolder2();
                        v = mLayoutInflater.inflate(R.layout.tab1_zuoping_adapter, null);
                        holder2.musicname = (TextView) v.findViewById(R.id.musicname);
                        holder2.content = (ExpandTextView) v.findViewById(R.id.desc);
                        holder2.author = (TextView) v.findViewById(R.id.author);
                        holder2.publish_time = (TextView) v.findViewById(R.id.publish_time);
                        holder2.comment = (TextView) v.findViewById(R.id.comment);
                        holder2.listener = (TextView) v.findViewById(R.id.listener);
                        holder2.like = (TextView) v.findViewById(R.id.like);
                        holder2.icon = (ImageView) v.findViewById(R.id.header);
                        holder2.album = (ImageView) v.findViewById(R.id.album);
                        holder2.musictype = (ImageView) v.findViewById(R.id.musictype);
                        holder2.play_status = (ImageView) v.findViewById(R.id.play_status);
                        holder2.iv_status = (ImageView) v.findViewById(R.id.iv_status);
                        v.setTag(holder2);
                        break;
                    case TYPE_IMAGE:
                        holder3 = new ViewHolder3();
                        v = mLayoutInflater.inflate(R.layout.tab1_adapter_image, null);
                        holder3.header = (ImageView) v.findViewById(R.id.header);
                        holder3.author = (TextView) v.findViewById(R.id.author);
                        holder3.publish_time = (TextView) v.findViewById(R.id.publish_time);
                        holder3.content = (ExpandTextView) v.findViewById(R.id.desc);
                        holder3.multiImageView = (MultiImageView) v.findViewById(R.id.multiImagView);
                        holder3.comment = (TextView) v.findViewById(R.id.comment);
                        holder3.listener = (TextView) v.findViewById(R.id.listener);
                        holder3.like = (TextView) v.findViewById(R.id.like);
                        holder3.iv_status = (ImageView) v.findViewById(R.id.iv_status);
                        v.setTag(holder3);
                        break;
                    default:
                        break;
                }

            } else {
                switch (type) {
                    case TYPE_QUESTION:
                        holder1 = (ViewHolder1) v.getTag();
                        break;
                    case TYPE_SOUND:
                        holder2 = (ViewHolder2) v.getTag();
                        break;
                    case TYPE_IMAGE:
                        holder3 = (ViewHolder3) v.getTag();
                        break;
                }
            }

            switch (type) {
                case TYPE_QUESTION:
                    typeQuestion(holder1, position);
                    break;
                case TYPE_SOUND:
                    typeSound(holder2, position);
                    break;
                case TYPE_IMAGE:
                    typeImage(holder3, position);
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void typeQuestion(ViewHolder1 holder, int position) {
        final Sound f = getItem(position);
        holder.musicname.setText(f.musicname);
        holder.desc.setText(EmojiCharacterUtil.decode(f.desc));
        holder.time.setText(f.commenttime + "\"");
        holder.tea_name.setText(f.tecname);
        holder.listener.setText(String.valueOf(f.views));
        holder.tectitle.setText(f.tectitle);
        holder.like.setText(String.valueOf(f.like));
        holder.comment.setText(String.valueOf(f.comments));

        long time = System.currentTimeMillis() / 1000 - f.edited;

        if (time < 2 * 24 * 60 * 60 * 1000 && time > 0) {
            holder.tea_listen.setText("限时免费听");
        } else {
            if (f.listen == 1) {
                holder.tea_listen.setText("已付费");
            } else {
                holder.tea_listen.setText("1元偷偷听");
            }

        }
        holder.desc.setOnClickListener(new ExpandTextView.onClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StubActivity.class);
                if (f.type == 1) {
                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                } else if (f.type == 2) {
                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                } else {
                    i.putExtra("fragment", ItemDetailTextFragment.class.getName());
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("Sound", f);
                i.putExtras(bundle);
                mContext.startActivity(i);
            }
        });

        if (f.islike == 0) {
            initDianZan(holder.like, false);
        } else {
            initDianZan(holder.like, true);
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppShare.getIsLogin(mContext)) {
                    RestNetCallHelper.callNet(
                            mContext,
                            MyNetApiConfig.like,
                            MyNetRequestConfig.like(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
                            "getSoundList", new NetCallBack() {
                                @Override
                                public void onNetNoStart(String id) {

                                }

                                @Override
                                public void onNetStart(String id) {

                                }

                                @Override
                                public void onNetEnd(String id, int type, NetResponse netResponse) {
                                    //  LogUtils.LOGE(tag,netResponse.toString());
                                    if (type == TYPE_SUCCESS) {

                                        if (f.islike == 0) {
                                            f.like++;
                                            f.islike = 1;
                                            notifyDataSetChanged();
                                        } else {

                                        }
                                    }

                                }
                            });
                } else {
                    Model.startNextAct(mContext,
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                }

            }
        });
        if (f.type == 1) {
            holder.ll_question.setVisibility(View.VISIBLE);
            holder.musictype.setImageResource(R.mipmap.shengyue);
        } else if (f.type == 2) {
            holder.ll_question.setVisibility(View.VISIBLE);
            holder.musictype.setImageResource(R.mipmap.boyin);
        } else {
            holder.ll_question.setVisibility(View.GONE);
        }

        PictureUtils.showPicture(mContext, f.tecimage, holder.tea_header);
        PictureUtils.showPicture(mContext, f.stuimage, holder.stu_header);

//        if (fragmentName.equals("Tab1Fragment")) {
//            if (position < 9) {
//                holder.iv_status.setVisibility(View.VISIBLE);
//                holder.iv_status.setBackgroundResource(R.mipmap.icon_new);
//            } else {
//                holder.iv_status.setVisibility(View.GONE);
//            }
//        } else if (fragmentName.equals("Tab1XizuoListFragment")) {
//            if (position < 9) {
//                holder.iv_status.setVisibility(View.VISIBLE);
//                holder.iv_status.setBackgroundResource(R.mipmap.icon_hot);
//            } else {
//                holder.iv_status.setVisibility(View.GONE);
//            }
//        }

//        holder.tea_header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initHomepage(String.valueOf(f.touid));
//            }
//        });
//        holder.stu_header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initHomepage(String.valueOf(f.fromuid));
//            }
//        });

    }


    private void typeImage(ViewHolder3 holder, int position) throws ParseException {
        final Sound f = getItem(position);
//        L.e(f.desc,f.images);
        if (TextUtils.isEmpty(f.images)) {
            holder.multiImageView.setVisibility(View.GONE);
        } else {
            final ArrayList<String> imagesList = new Gson().fromJson(f.images, new TypeToken<ArrayList<String>>() {
            }.getType());
            holder.multiImageView.setVisibility(View.VISIBLE);
            holder.multiImageView.setList(imagesList);
            holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    ImagePagerActivity.startImagePagerActivity((mContext), imagesList, position, imageSize);
                }
            });
        }

        holder.author.setText(f.stuname);
        holder.content.setText(EmojiCharacterUtil.decode(f.desc));
        holder.comment.setText(f.comments);
        holder.like.setText(String.valueOf(f.like));
        holder.listener.setText(String.valueOf(f.views));
        holder.publish_time.setText(String2TimeUtils.longToString(f.created * 1000, "yyyy/MM/dd HH:mm"));
        PictureUtils.showPicture(mContext, f.stuimage, holder.header, 47);

        holder.content.setOnClickListener(new ExpandTextView.onClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StubActivity.class);
                i.putExtra("fragment", ItemDetailPicFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("Sound", f);
                i.putExtras(bundle);
                mContext.startActivity(i);
            }
        });

        if (f.islike == 0) {
            initDianZan(holder.like, false);
        } else {
            initDianZan(holder.like, true);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppShare.getIsLogin(mContext)) {
                    RestNetCallHelper.callNet(
                            mContext,
                            MyNetApiConfig.like,
                            MyNetRequestConfig.like(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
                            "getSoundList", new NetCallBack() {
                                @Override
                                public void onNetNoStart(String id) {

                                }

                                @Override
                                public void onNetStart(String id) {

                                }

                                @Override
                                public void onNetEnd(String id, int type, NetResponse netResponse) {
                                    //  LogUtils.LOGE(tag,netResponse.toString());
                                    if (type == TYPE_SUCCESS) {

                                        if (f.islike == 0) {
                                            f.like++;
                                            f.islike = 1;
                                            notifyDataSetChanged();
                                        } else {

                                        }
                                    }

                                }
                            });
                } else {
                    Model.startNextAct(mContext,
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                }

            }
        });

        if (fragmentName.equals("Tab1Fragment")) {
            if (position < 9) {
                holder.iv_status.setVisibility(View.VISIBLE);
                holder.iv_status.setImageResource(R.mipmap.icon_new);
            } else {
                holder.iv_status.setVisibility(View.GONE);
            }
        } else if (fragmentName.equals("Tab1XizuoListFragment")) {
            if (position < 9) {
                holder.iv_status.setVisibility(View.VISIBLE);
                holder.iv_status.setImageResource(R.mipmap.icon_hot);
            } else {
                holder.iv_status.setVisibility(View.GONE);
            }
        }
    }

    private void typeSound(ViewHolder2 holder2, int position) throws ParseException {
        final Sound f = getItem(position);
        holder2.musicname.setText(f.musicname);
        holder2.content.setText(EmojiCharacterUtil.decode(f.desc));
        holder2.comment.setText(f.comments);
        holder2.like.setText(String.valueOf(f.like));
        holder2.listener.setText(String.valueOf(f.views));
        holder2.author.setText(f.stuname);
        holder2.publish_time.setText(String2TimeUtils.longToString(f.created * 1000, "yyyy/MM/dd HH:mm"));
        if (f.type == 1) {
            holder2.musictype.setVisibility(View.VISIBLE);
            holder2.musictype.setImageResource(R.mipmap.shengyue);
        } else if (f.type == 2) {
            holder2.musictype.setVisibility(View.VISIBLE);
            holder2.musictype.setImageResource(R.mipmap.boyin);
        } else {
            holder2.musictype.setVisibility(View.GONE);
        }
        if (f.islike == 0) {
            initDianZan(holder2.like, false);
        } else {
            initDianZan(holder2.like, true);
        }
        holder2.content.setOnClickListener(new ExpandTextView.onClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StubActivity.class);
                i.putExtra("fragment", ItemDetailPicFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("Sound", f);
                i.putExtras(bundle);
                mContext.startActivity(i);
            }
        });

        holder2.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppShare.getIsLogin(mContext)) {
                    RestNetCallHelper.callNet(
                            mContext,
                            MyNetApiConfig.like,
                            MyNetRequestConfig.like(mContext, AppShare.getUserInfo(mContext).uid, String.valueOf(f.sid)),
                            "getSoundList", new NetCallBack() {
                                @Override
                                public void onNetNoStart(String id) {

                                }

                                @Override
                                public void onNetStart(String id) {

                                }

                                @Override
                                public void onNetEnd(String id, int type, NetResponse netResponse) {
                                    //  LogUtils.LOGE(tag,netResponse.toString());
                                    if (type == TYPE_SUCCESS) {

                                        if (f.islike == 0) {
                                            f.like++;
                                            f.islike = 1;
                                            notifyDataSetChanged();
                                        } else {

                                        }
                                    }

                                }
                            });
                } else {
                    Model.startNextAct(mContext,
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(mContext).showText(mContext.getString(R.string.login_tips));
                }

            }
        });

        if (fragmentName.equals("Tab1Fragment")) {
            if (position < 9) {
                holder2.iv_status.setVisibility(View.VISIBLE);
                holder2.iv_status.setImageResource(R.mipmap.icon_new);
            } else {
                holder2.iv_status.setVisibility(View.GONE);
            }
        } else if (fragmentName.equals("Tab1XizuoListFragment")) {
            if (position < 9) {
                holder2.iv_status.setVisibility(View.VISIBLE);
                holder2.iv_status.setImageResource(R.mipmap.icon_hot);
            } else {
                holder2.iv_status.setVisibility(View.GONE);
            }
        }
        if (mCurrent == f.sid) {
            holder2.musicname.setTextColor(mContext.getResources().getColor(R.color.redMain));

        } else {
            holder2.musicname.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
        }
        final int pos = position;
        holder2.play_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    h.play_status =
                mCurrent = f.sid;
                notifyDataSetChanged();
                if (mListener != null) mListener.onMoreClick(mCurrent);
            }
        });

        PictureUtils.showPicture(mContext, f.stuimage, holder2.icon, 47);
        PictureUtils.showPictureAlbum(mContext, f.stuimage, holder2.album, 75);
    }


    public void setOnMoreClickListener(OnMoreClickListener l) {
        mListener = l;
    }

    public interface OnMoreClickListener {
        public void onMoreClick(int position);

    }

    public interface OnLongClickListener {
        public void onLongClick(int sid);

    }

    private void initDianZan(TextView textView, boolean t) {
        Drawable leftDrawable;
        if (t) {
            leftDrawable = mContext.getResources().getDrawable(R.mipmap.dianzan_pressed_new);

        } else {
            leftDrawable = mContext.getResources().getDrawable(R.mipmap.dianzan__new);

        }
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        textView.setCompoundDrawables(leftDrawable, null, null, null); //(Drawable left, Drawable top, Drawable right, Drawable bottom)
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position < 2) {
            return;
        }
        Sound f = getItem(position - 2);
        int type = getItemViewType(position - 2);
        if (!isNetworkConnected(mContext)) {
            ToastManager.getInstance(mContext).showText(
                    R.string.fm_net_call_no_network);
            return;
        }
        Intent i = new Intent(mContext, StubActivity.class);
        switch (type) {
            case TYPE_QUESTION:
                //   holder1 = (ViewHolder1) convertView.getTag();
                if (f.type == 1) {
                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                } else if (f.type == 2) {
                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                } else {
                    i.putExtra("fragment", ItemDetailTextFragment.class.getName());
                }

                break;
            case TYPE_SOUND:
                i.putExtra("fragment", ItemDetailFragment.class.getName());

                break;
            case TYPE_IMAGE:
                i.putExtra("fragment", ItemDetailPicFragment.class.getName());
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("Sound", f);
        i.putExtras(bundle);
        mContext.startActivity(i);


    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


}
