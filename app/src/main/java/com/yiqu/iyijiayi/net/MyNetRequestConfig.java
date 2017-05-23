package com.yiqu.iyijiayi.net;

import android.content.Context;

import com.fwrestnet.NetRequest;
import com.model.ComposeVoice;

/**
 * @comments 接口的参数配置
 */
public class MyNetRequestConfig {

    /**
     * 1、短信验证码
     *
     * @param c
     * @param phone 手机号
     * @return
     */
    public static NetRequest getLoginMessageCode(Context c, String phone) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("phone", phone);

        return r;
    }

    /**
     * 2、用户注册
     */
    public static NetRequest loginPhoneCheck(Context c, String openid, String username, String userimage,
                                             String sex, String phone, String code, String key) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("openid", openid);
        r.addHttpParam("username", username);
        r.addHttpParam("userimage", userimage);
        r.addHttpParam("sex", sex);
        r.addHttpParam("phone", phone);
        r.addHttpParam("smscode", code);
        r.addHttpParam("key", key);


        return r;
    }

    /**
     * 3、用户登录
     */
    public static NetRequest login(Context c, String phone, String smscode, String key, String deviceToken) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("phone", phone);
        r.addHttpParam("smscode", smscode);
        r.addHttpParam("key", key);
        r.addHttpParam("deviceToken", deviceToken);
        return r;
    }
    public static NetRequest bindPhoneCheck(Context c, String uid, String phone, String smscode, String key,String openid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("phone", phone);
        r.addHttpParam("smscode", smscode);
        r.addHttpParam("key", key);
        r.addHttpParam("openid", openid);
        return r;
    }


    /**
     * 2、用户注册
     */
    public static NetRequest loginFromWechat(Context c, String openid, String username, String userimage, String sex) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("openid", openid);
        r.addHttpParam("username", username);
        r.addHttpParam("userimage", userimage);
        r.addHttpParam("sex", sex);
        r.addHttpParam("deviceToken", "");

        return r;
    }

    /**
     * 5、Remen
     */
    public static NetRequest remen(Context c, String checkpay) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("checkpay", checkpay);
        return r;
    }

    /**
     *
     * @param c
     * @param uid
     * @param type 1学生 2导师
     * @return
     */
    public static NetRequest findPeople(Context c, String uid,int type) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("type", String.valueOf(type));
        return r;
    }

    public static NetRequest searchByText(Context c, String text) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("text", text);
        return r;
    }

    public static NetRequest searchItems(Context c, String text, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("text", text);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        return r;
    }
   public static NetRequest getGroups(Context c, String gid, String myuid, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("gid", gid);
        r.addHttpParam("myuid", myuid);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        return r;
    }

    /**
     * 6、
     */
    public static NetRequest follow_recommend(Context c, String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        return r;
    }
    public static NetRequest getFollowTeacherList(Context c, String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("myuid", uid);
        r.addHttpParam("rows", String.valueOf(1000));
        r.addHttpParam("count", String.valueOf(0));
        return r;
    }
    /**
     * 6、
     */
    public static NetRequest getHistory(Context c, String uid, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        return r;
    }
    public static NetRequest getEventList(Context c, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        return r;
    }

    public static NetRequest addfollow(Context c, String uid, String fuid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("fuid", fuid);

        return r;
    }


    public static NetRequest delfollow(Context c, String uid, String fuid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("fuid", fuid);

        return r;
    }

    /**
     * 8、 type:1,学生,2,老师
     */
    public static NetRequest get_follow_recommend_list(Context c, String uid,
                                                       String type, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("type", type);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        return r;
    }

    /**
     * 9、
     */
    public static NetRequest editUser(Context c, String uid, String dataName, String dataValue) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam(dataName, dataValue);
        return r;
    }
    public static NetRequest addFeedback(Context c, String uid, String content) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("content", content);
        return r;
    }

    /**
     * 10、
     */
    public static NetRequest addTeacherApply(Context c, String uid, String username,
                                             String school, String title, String desc,
                                             String phone, String address, String source) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("username", username);
        r.addHttpParam("school", school);
        r.addHttpParam("title", title);
        r.addHttpParam("desc", desc);
        r.addHttpParam("phone", phone);
        r.addHttpParam("address", address);
        r.addHttpParam("source", source);

        return r;
    }

    /**
     * 20、获取热门/发现列表
     */
    public static NetRequest getFollowSoundList(Context c, String myuid, String arr,
                                                int count, int rows, String orderby, String sort) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("myuid", myuid);
        r.addHttpParam("arr", arr);
        r.addHttpParam("count", count + "");
        r.addHttpParam("rows", rows + "");
        r.addHttpParam("orderby", orderby);
        r.addHttpParam("sort", sort);
        return r;
    }

    /**
     * 20、获取热门/发现列表
     */
    public static NetRequest getSoundList(Context c, String arr,
                                          int count, int rows, String orderby, String sort) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("arr", arr);
        r.addHttpParam("count", count + "");
        r.addHttpParam("rows", rows + "");
        r.addHttpParam("orderby", orderby);
        r.addHttpParam("sort", sort);
        return r;
    }

    /**
     * 20、 NSDictionary   nsDictionary = new NSDictionary();
     * nsDictionary.isopen = "1";
     * nsDictionary.ispay = "1";
     * nsDictionary.isreply = "1";
     * nsDictionary.status = "1";
     * nsDictionary.stype = "1";
     * Gson gson = new Gson();
     * String  arr = gson.toJson(nsDictionary);
     */
    public static NetRequest getSoundList(Context c, String arr,
                                          int count, int rows, String orderby, String sort, String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("arr", arr);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        r.addHttpParam("orderby", orderby);
        r.addHttpParam("sort", sort);
        r.addHttpParam("checkpay", uid);
        return r;
    }

    public static NetRequest getHomeData(Context c, String myuid,
                                          int count, int rows, String sort) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("myuid", myuid);
        r.addHttpParam("count", String.valueOf(count));
        r.addHttpParam("rows", String.valueOf(rows));
        r.addHttpParam("sort", sort);
        return r;
    }
    public static NetRequest deleteComment(Context c, String id,
                                           String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("id", id);
        r.addHttpParam("uid", String.valueOf(uid));
        return r;
    }

    /**
     * 12、
     */
    public static NetRequest getMusicList(Context c, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("count", count + "");
        r.addHttpParam("rows", rows + "");
        return r;
    }


    public static NetRequest getSoundDetail(Context c, String sid,
                                            String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("sid", sid);
        r.addHttpParam("checkpay", uid);
        return r;
    }
    public static NetRequest addHistory(Context c, String sid,
                                            String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("sid", sid);
        r.addHttpParam("uid", uid);

        return r;
    }

    public static NetRequest getComments(Context c, String sid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("sid", sid);
        return r;
    }

    /**
     * 13、
     *
     * @param stype  1问题 2习作
     * @param isfree 1 免费
     */
    public static NetRequest addSound(Context c, String uid, String stype,String eid, String isfree, ComposeVoice composeVoice) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("stype", stype);
        r.addHttpParam("eid", eid);
        r.addHttpParam("type", composeVoice.type);
        r.addHttpParam("musicname", composeVoice.musicname);
        r.addHttpParam("musictype", composeVoice.musictype);
        r.addHttpParam("chapter", composeVoice.chapter);
        r.addHttpParam("accompaniment", composeVoice.accompaniment);
        r.addHttpParam("fromuid", uid);
        r.addHttpParam("mid", composeVoice.mid + "");
        r.addHttpParam("commentpath", composeVoice.commentpath);
        r.addHttpParam("commenttime", composeVoice.commenttime);
        r.addHttpParam("soundtime", composeVoice.soundtime + "");
        r.addHttpParam("isformulation", composeVoice.isformulation);
        r.addHttpParam("listenprice", composeVoice.listenprice);
        r.addHttpParam("isopen", composeVoice.isopen);
        r.addHttpParam("ispay", composeVoice.ispay);
        r.addHttpParam("isreply", composeVoice.isreply);
        r.addHttpParam("status", composeVoice.status);
        r.addHttpParam("touid", composeVoice.touid + "");
        r.addHttpParam("questionprice", composeVoice.questionprice);
        r.addHttpParam("desc", composeVoice.desc);
        r.addHttpParam("soundpath", composeVoice.soundpath);
        r.addHttpParam("article_content", composeVoice.article_content);
        r.addHttpParam("lrc_url", composeVoice.lrcpath);
        r.addHttpParam("isfree", isfree);
        return r;
    }
    /**
     * 13、
     *
     * @param isfree 1 免费
     */
    public static NetRequest addSound(Context c, String fromuid, String touid, String isfree,String questionStr,String questionprice) {
        MyNetRequest r = new MyNetRequest(c);

        r.addHttpParam("type","3");
        r.addHttpParam("mid", "0");
        r.addHttpParam("musicname", "");
        r.addHttpParam("musictype", "");
        r.addHttpParam("chapter", "");
        r.addHttpParam("accompaniment", "");
        r.addHttpParam("commentpath", "");
        r.addHttpParam("commenttime","0");
        r.addHttpParam("isformulation", "0");
        r.addHttpParam("listenprice", "1");
        r.addHttpParam("isopen", "1");
        r.addHttpParam("ispay", "0");
        r.addHttpParam("isreply", "0");
        r.addHttpParam("status", "1");
        r.addHttpParam("touid",touid);
        r.addHttpParam("questionprice",questionprice);
        r.addHttpParam("desc", questionStr);
        r.addHttpParam("soundpath", "");
        r.addHttpParam("soundtime", "0");
        r.addHttpParam("isfree", isfree);
        r.addHttpParam("fromuid", fromuid);
        r.addHttpParam("stype", "1");
        return r;
    }

    /**

     */
    public static NetRequest getUnfollowTeacherList(Context c, String myuid, String rows,
                                                    String count) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("myuid", myuid);
        r.addHttpParam("rows", rows);
        r.addHttpParam("count", count);
        return r;
    }
    public static NetRequest getBackgroundMusicList(Context c, String typename, String rows,
                                                    String count) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("typename", typename);
        r.addHttpParam("rows", rows);
        r.addHttpParam("count", count);
        return r;
    }
    public static NetRequest addTextImage(Context c, String desc, String fromuid,
                                                    String images) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("stype", "3");
        r.addHttpParam("desc", desc);
        r.addHttpParam("fromuid", fromuid);
        r.addHttpParam("images", images);
        return r;
    }
    public static NetRequest getSoundArticleList(Context c, String class_id, String event_id,
                                                 String rows,String count) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("class_id", class_id);
        r.addHttpParam("event_id", event_id);
        r.addHttpParam("rows", rows);
        r.addHttpParam("count", count);
        return r;
    }
    public static NetRequest plusSoundAriticleReads(Context c, String id) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("id", id);
        return r;
    }
    public static NetRequest getSoundArticleClass(Context c) {
        MyNetRequest r = new MyNetRequest(c);
        return r;
    }
    public static NetRequest searchSoundArticles(Context c, String text, String rows,
                                                 String count) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("text", text);
        r.addHttpParam("rows", rows);
        r.addHttpParam("count", count);

        return r;
    }

    public static NetRequest getFollowTeacherList(Context c, String myuid, String rows,
                                                  String count) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("myuid", myuid);
        r.addHttpParam("rows", rows);
        r.addHttpParam("count", count);
        return r;
    }


    /**
     *
     */
    public static NetRequest getUserByPhoneUid(Context c, String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);

        return r;
    }


    /**
     * @param c
     * @param type     2 点评,1 提问,3:习作
     * @param uid      个人页面对象的uid
     * @param count
     * @param rows
     * @param checkpay 传当前用户UID
     * @return
     */
    public static NetRequest getUserPageSoundList(Context c, int type,
                                                  String uid, int count, int rows, String checkpay) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("type", type + "");
        r.addHttpParam("uid", uid);
        r.addHttpParam("count", count + "");
        r.addHttpParam("rows", rows + "");
        r.addHttpParam("checkpay", checkpay);
        return r;
    }

    /**
     * 17、
     */
    public static NetRequest getUserPage(Context c, String uid, String myuid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("myuid", myuid);
        return r;
    }
    public static NetRequest deleteSound(Context c, String uid, String sid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("sid", sid);
        return r;
    }

    /**
     * 18、
     */
    public static NetRequest setPhoto(Context c, String uid,
                                      String image, String type) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("image", image);
        r.addHttpParam("type", type);
        return r;
    }

    /**
     *
     */
    public static NetRequest getNewOrder(Context c, String uid, String sid,
                                         String price) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("sid", sid);
        r.addHttpParam("price", price);
        r.addHttpParam("type", "1");
        r.addHttpParam("payment", "1");
        return r;
    }

    public static NetRequest orderQuery(Context c, String out_trade_no) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("out_trade_no", out_trade_no);
        return r;
    }

    /**
     * 20、
     */
    public static NetRequest getTeacherApply(Context c, String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        return r;
    }

    /**
     * @param c
     * @param uid
     * @param type    apple:苹果 android:安桌
     * @param payment 1:微信支付  price 价格
     * @return
     */
    public static NetRequest getNewCoinOrder(Context c, String uid, String type,
                                             String payment, String price) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("type", type);
        r.addHttpParam("payment", payment);
        r.addHttpParam("price", price);
        return r;
    }

    /**
     * 22、
     */
    public static NetRequest eavesdrop(Context c, String uid, int sid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("sid", String.valueOf(sid));
        return r;
    }
    public static NetRequest getlyric(Context c) {
        MyNetRequest r = new MyNetRequest(c);
        return r;
    }

    /**
     * 23、
     */
    public static NetRequest refuseReply(Context c, String sid,
                                         String touid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("sid", sid);
        r.addHttpParam("touid", touid);
        return r;
    }

    /**
     */
    public static NetRequest soundReply(Context c, String sid, String touid,
                                        String commentpath, String commenttime, String isnew) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("sid", sid);
        r.addHttpParam("touid", touid);
        r.addHttpParam("commentpath", commentpath);
        r.addHttpParam("commenttime", commenttime);
        r.addHttpParam("isnew", isnew);
        return r;
    }

    /**
     */
    public static NetRequest checkUpdate(Context c) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("type", "2");
        return r;
    }
   public static NetRequest getBannerList(Context c) {
        MyNetRequest r = new MyNetRequest(c);
//        r.addHttpParam("type", "2");
        return r;
    }


    /**
     */
    public static NetRequest like(Context c, String uid, String sid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("sid", sid);
        return r;
    }

    public static NetRequest addComment(Context c, String sid, String fromuid, String touid, String comment) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("fromuid", fromuid);
        r.addHttpParam("sid", sid);
        r.addHttpParam("touid", touid);
        r.addHttpParam("comment", comment);
        return r;
    }
}
