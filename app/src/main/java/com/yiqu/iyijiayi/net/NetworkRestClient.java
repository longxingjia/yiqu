package com.yiqu.iyijiayi.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.util.Map;

import static com.yiqu.iyijiayi.utils.LogUtils.LOGE;


public class NetworkRestClient {

//    private static final String DEV_BASE_URL = "http://121.40.80.250:8080/eduonlinews"; //DEV
//    //  private static final String UAT_BASE_URL = "http://192.168.1.112:8081/eduonlinews-1.0"; //UAT
//      private static final String UAT_BASE_URL = "http://112.74.211.111/eduonlinews"; //UAT
////    private static final String UAT_BASE_URL = "http://58.96.175.60:8080/eduonlinews"; //UAT
//    private static final String EDB_BASE_URL = "http://112.74.206.126:8080/eduonlinewsedb"; //EDB
//    private static final String BASE_URL = UAT_BASE_URL;
//
//    public static boolean isEDB(){
//        if(BASE_URL.equals(EDB_BASE_URL)){
//            return true;
//        }return false;
//    }
//    public final static String URL_LOGIN = "/rest/account/login";
//    public final static String URL_PUBLISH_HOMEWORK = "/rest/question/publish/class/";
//    public final static String URL_REFRESH_DEVICE = "/rest/deviceInfo/refresh";
//    public final static String URL_SUBMIT_HOMEWORK = "/rest/question/%s/submit";
//    public final static String URL_GET_REVIEW_INFO = "/rest/question/%s/review/class/%s";
//    public final static String URL_GET_BOOK_LIST = "/rest/book/list/subject/%s";
//    public final static String URL_GET_PUBLISH_HOMEWORK_LIST = "/rest/question/book/%s/class/%s/published";
//    public final static String URL_REVIEW_COMMENT = "/rest/question/mark/%s";
//
//    public final static String URL_GET_BOOK_CHAPTER = "/rest/bookchapter/list/book/%s";
//
//    public final static String URL_GET_STUDENT_LIST="/rest/student/list/class/%s";
//
//    public final static String URL_GET_REVIEW_ALL_INFO = "/rest/question/review/all/class/%s?questionIds=%s";
//
//    public final static String URL_GET_REVIEW_GROUP_INFO="/rest/questionByGroup/class/%s/question/%s/groupArrangementDetails";
//    public final static String URL_GET_SUBJECT_LIST = "/rest/subject/list/school/%s";
//    public final static String URL_GET_SUBMIT_HOMEWORK_LIST = "/rest/question/book/%s/submitted/student/%s";
//    public final static String URL_LOGIN_OLD = "/rest/account/login?username=%s&password=%s&uuid=%s";
//
//    public final static String URL_UPDATE_NICK_NAME = "/rest/account/updateNickName";
//    public final static String URL_GET_PIC = "http://112.74.211.111/userdata/";
//
//    public final static String URL_SUBMIT_GROUP_HOMEWORK="/rest/questionByGroup/groupAnswerSubmit";
//    public final static String URL_USER_PHOTO_HOST_NAME = "http://itecheadimage.qiniudn.com/";
//    public final static String URL_SYNC_BOOK="/rest/toolbar/resources/sync/book/%s";
//    public final static String URL_PUBLISH_GROUP_HOMEWORK="/rest/questionByGroup/groupConfirmedAndQuestionPublish/class/";
//    public final static String URL_CANCEL_PUBLISH="/rest/question/cancelPublish/class/%s/question/%s";
//    public final static String URL_IF_ORDER_BOOK="/rest/order/book/%s/account/%s";
//    public final static String URL_GET_PURCHASE_BOOK_LIST="/rest/order/book/period/account/%s";
//    public final static String USER_PHOTO_BUCKET_NAME = "itecheadimage";
//    public final static String QINIU_ACCESS_KEY="Yr538JhSKu2f_mmykksCN_qxmh9qc0pj1x90Wbga";
//    public final static String QINIU_SECRET_KEY="FORIPWDwAXOrkZ9DaVMMgAsmmCOCFhu6y6Kdq3Pr";
//    public final static String URL_ADD_DOWNLOAD_BOOK_LOG = "/rest/operationLog/add";
//
//    public final static String DICTIONARY_LINK = "https://hk.dictionary.yahoo.com/";

    public static String get(String url, String accessToken) {
        return HttpRequest.get( url).contentType("application/json").accept("application/json").header("access_token", accessToken).body();
    }

    public static String get(String url) {
        return HttpRequest.get(  url).contentType("application/json").accept("application/json").body();
    }

    public static String post(String url, String accessToken, Map<String, Object> data) {
        HttpRequest request = HttpRequest.post(  url).header("access_token", accessToken).form(data);
        return request.body();
    }

    public static String postUTF8(String url, String headerName, String headerContent, Map<String,String> data) {
        HttpRequest request = HttpRequest.post( url).contentType("application/x-www-form-urlencoded").accept("application/json").header(headerName, headerContent).form(data,"UTF-8");
        return request.body();
    }

    public static String post(String url, String accessToken, String paramJson) {
        HttpRequest request = HttpRequest.post(url).contentType("application/json").accept("application/json").header("access_token", accessToken).send(paramJson);
        return request.body();
    }

//    public static String getDeviceInfo(String deviceInfo){
//        return HttpRequest.get(BASE_URL + URL_REFRESH_DEVICE).contentType("application/json").accept("application/json").header("DeviceInfo",deviceInfo).body();
//    }
//
//
//    public static String registerDevice(String paramJson) {
//        String accessToken = "";
//        try {
//
//            HttpRequest request = HttpRequest.post(BASE_URL + URL_REFRESH_DEVICE).contentType("application/json").accept("application/json").send(paramJson);
//            String returnResult = request.body();
//
//            Gson gson = new Gson();
//            AccessTokenEntityResponse response = gson.fromJson(returnResult, new TypeToken<AccessTokenEntityResponse>() {
//            }.getType());
//            if (response != null) {
//                accessToken = response.getData().getAccessToken();
//            }
//        } catch (Exception ex) {
//            LOGE("NetWorkRest", "Error register device"+ex );
//        }
//        return accessToken;
//    }


    public static String post(String url, Map<String, Object> data) {
        HttpRequest request = HttpRequest.post( url).form(data);
        return request.body();
    }
//
//    private static String getAbsoluteUrl(String relativeUrl) {
//        return BASE_URL + relativeUrl;
//    }

    /**
     *
     * @param context
     * @return true 可用，false 不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo= getNetworkInfo(context);
        return (networkInfo != null && networkInfo.isConnected());
    }


    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cManager.getActiveNetworkInfo();
    }

    public enum RedirectUrl{
        discussionForumURL,
        studyResourceURL,
        performanceReviewURL,
        itextBookStoreURL,
        pictureResourceURL,
        videoResourceURL,
        other1ResourceURL,
        other2ResourceURL,
        other3ResourceURL,
        other4ResourceURL,
        pdfresourceURL
    }

//    public static String getForumUrl(Context context,RedirectUrl redirectUrl){
//        AccountVo accountVO = AppShare.getAccountInfo(context);
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("jforumUserITECEmail",""));
//        params.add(new BasicNameValuePair("jforumUserITECInfo",accountVO.getAccountId()));
//        params.add(new BasicNameValuePair("jforumUserITECRole",accountVO.getForumRole()));
//        params.add(new BasicNameValuePair("jforumUserITECWord", MD5Util.getMD5String(PrefUtils.getString(context, PreUtils.PREF_PASSWORD))));
//        String redirectUrlLink = "";
//        switch (redirectUrl){
//            case discussionForumURL:
//                redirectUrlLink = accountVO.getDiscussionForumURL();
//                break;
//            case studyResourceURL:
//                redirectUrlLink = accountVO.getStudyResourceURL();
//                break;
//            case performanceReviewURL:
//                redirectUrlLink = accountVO.getPerformanceReviewURL();
//                break;
//            case itextBookStoreURL:
//                redirectUrlLink = accountVO.getItextBookStoreURL();
//                break;
//            case pictureResourceURL:
//                redirectUrlLink = accountVO.getPictureResourceURL();
//                break;
//            case videoResourceURL:
//                redirectUrlLink = accountVO.getVideoResourceURL();
//                break;
//            case other1ResourceURL:
//                redirectUrlLink = accountVO.getOther1ResourceURL();
//                break;
//            case other2ResourceURL:
//                redirectUrlLink = accountVO.getOther2ResourceURL();
//                break;
//            case other3ResourceURL:
//                redirectUrlLink = accountVO.getOther3ResourceURL();
//                break;
//            case other4ResourceURL:
//                redirectUrlLink = accountVO.getOther4ResourceURL();
//                break;
//            case pdfresourceURL:
//                redirectUrlLink = accountVO.getPdfresourceURL();
//                break;
//            default:
//                break;
//        }
//        params.add(new BasicNameValuePair("redirectURL",redirectUrlLink));
//        String url =  accountVO.getForumRedirectlURL() + "?"+ URLEncodedUtils.format(params, "UTF-8");
//        Log.v("shimly", url);
//        return url;
//    }
}
