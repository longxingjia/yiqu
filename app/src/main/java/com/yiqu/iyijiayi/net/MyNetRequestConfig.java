package com.yiqu.iyijiayi.net;

import android.content.Context;

import com.fwrestnet.NetRequest;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.utils.LogUtils;

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


    /**
     * 2、用户注册
     */
    public static NetRequest loginFromWechat(Context c, String openid, String username, String userimage, String sex) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("openid", openid);
        r.addHttpParam("username", username);
        r.addHttpParam("userimage", userimage);
        r.addHttpParam("sex", sex);

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
     * 6、
     */
    public static NetRequest follow_recommend(Context c, String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
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
        r.addHttpParam("count", count + "");
        r.addHttpParam("rows", rows + "");
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

    /**
     * 10、
     */
    public static NetRequest addTeacherApply(Context c, String uid, String username,
                                             String school, String title, String desc) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("uid", uid);
        r.addHttpParam("username", username);
        r.addHttpParam("school", school);
        r.addHttpParam("title", title);
        r.addHttpParam("desc", desc);

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
    public static NetRequest getSoundList(Context c,  String arr,
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
     * 12、
     */
    public static NetRequest getMusicList(Context c, int count, int rows) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("count", count + "");
        r.addHttpParam("rows", rows + "");
        return r;
    }

    /**
     * 参数：String token String shjhm //， string ztbh //
     * kchbh//课程编号,kchmch//课程名称,kchslt//课程缩略图,kchjj//简介
     * chjshj//创建时间,sdjf//所得积分,shfhdxhh//是否获得小红花 @
     */


    public static NetRequest getSoundDetail(Context c, String sid,
                                            String uid) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("sid", sid);
        r.addHttpParam("checkpay", uid);

        return r;
    }

    /**
     * 13、
     *
     * @param stype 1问题 2习作
     */
    public static NetRequest addSound(Context c, String stype, String isfree, ComposeVoice composeVoice) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("stype", stype);
        r.addHttpParam("type", composeVoice.type);
        r.addHttpParam("musicname", composeVoice.musicname);
        r.addHttpParam("musictype", composeVoice.musictype);
        r.addHttpParam("chapter", composeVoice.chapter);
        r.addHttpParam("accompaniment", composeVoice.accompaniment);
        r.addHttpParam("fromuid", composeVoice.fromuid);
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
        r.addHttpParam("isfree", isfree);
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
        r.addHttpParam("type", type+"");
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
     * 21、我的老师接口 String token//登录凭证,String shjhm //手机号，String bjbh//班级编号
     */
    public static NetRequest myTeacher(Context c, String token, String shjhm,
                                       String bjbh) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("token", token);
        r.addHttpParam("shjhm", shjhm);
        r.addHttpParam("bjbh", bjbh);
        return r;
    }

    /**
     * 22、我的学生列表接口 参数：String token//登录凭证,String shjhm //手机号,String bjbh//班级编号
     */
    public static NetRequest myStudent(Context c, String token, String shjhm,
                                       String bjbh) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("token", token);
        r.addHttpParam("shjhm", shjhm);
        r.addHttpParam("bjbh", bjbh);
        return r;
    }

    /**
     * 23、推送信息列表接口 String token//登录凭证,String shjhm //手机号
     */
    public static NetRequest findAllPushMsg(Context c, String token,
                                            String shjhm, String page, String pageSize) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("token", token);
        r.addHttpParam("shjhm", shjhm);
        r.addHttpParam("page", page);
        r.addHttpParam("pageSize", pageSize);
        return r;
    }

    /**
     * 24、根据消息ID取得推送信息详情接口 参数：String token//登录凭证,String shjhm //手机号,String
     * pushMsgId//消息ID
     */
    public static NetRequest findPushMsg(Context c, String token, String shjhm,
                                         String pushMsgId) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("token", token);
        r.addHttpParam("shjhm", shjhm);
        r.addHttpParam("pushMsgId", pushMsgId);
        return r;
    }

    /**
     * 25、老师对自己班级学生进行审核的接口 参数：String token//登录凭证 ,String shjhm //当前登录用户手机号
     * ,String xsshjhm//学生的手机号 , String type //操作类型（1，踢出操作；2，同意操作）
     */
    public static NetRequest updateCheckStatus(Context c, String token,
                                               String shjhm, String xsshjhm, String type) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("token", token);
        r.addHttpParam("shjhm", shjhm);
        r.addHttpParam("xsshjhm", xsshjhm);
        r.addHttpParam("type", type);
        return r;
    }

    /**
     * ,String xsshjhm//学生的手机号 , String type //操作类型（1，踢出操作；2，同意操作）
     */

    /**
     * 26、获取软件更新版本信息的接口 参数
     *
     * @param c
     * @param token 登录凭证
     * @param shjhm 当前登录用户手机号
     * @return 状态码：statusCode：0表示数据库访问异常，1表示成功。2表示token失效 。验证安全凭证：token,1小时失效
     */
    public static NetRequest updateApp(Context c, String token, String shjhm
    ) {
        MyNetRequest r = new MyNetRequest(c);
        r.addHttpParam("token", token);
        r.addHttpParam("shjhm", shjhm);
        return r;
    }
}
