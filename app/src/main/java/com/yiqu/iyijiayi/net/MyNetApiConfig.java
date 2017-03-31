package com.yiqu.iyijiayi.net;


import android.annotation.SuppressLint;


import com.fwrestnet.NetMethod;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;

/**
 * @comments 接口的具体配置
 */
@SuppressLint("NewApi")
public class MyNetApiConfig {

    public static String ImageServerAddr = "http://www.hunanyiqu.com";
    public static String ServerAddr = "http://www.hunanyiqu.com/YijiayiServer/web";
    //    public static String ServerAddr = "http://172.28.224.107/YijiayiServer/web";
    public static String wechatUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
            "%s&secret=%s&code=%s&grant_type=authorization_code";

    public static String getWechatUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    /**
     * 1、根据手机获取用户  get
     */
    public static MyNetApi getUserByPhone = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-user-by-phone";
        }
    };

    /**
     * 2、根据UID获取用户 get
     */
    public static MyNetApi getUserByPhoneUid = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-user-by-uid";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };

    public static MyNetApi refuseReply = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/refuse-reply";
        }

    };


    /**
     * 3、添加用户
     */
    public static MyNetApi addUser = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/add-user";
        }

    };

    /**
     * 4、修改用户
     */
    public static MyNetApi editUser = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/edit-user";
        }


    };
    /**
     * 4、修改用户
     */
    public static MyNetApi addFeedback = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/add-feedback";
        }


    };


    /**
     * 5、微信登陆验证（首次登陆为自动注册）
     */
    public static MyNetApi loginFromWechat = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/login-from-wechat";
        }

    };

    /**
     * 6、发送短信验证码
     */
    public static MyNetApi getLoginMessageCode = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-login-message-code";
        }

    };
    public static MyNetApi bindPhoneCheck = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/wallet/bind-phone-check";
        }

    };


    /**
     * 7、验证手机短信验证码,同时绑定用户
     */
    public static MyNetApi loginPhoneCheck = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/login-phone-check";
        }

    };

    /**
     * 8、登陆验证（首次登陆为自动注册）
     */
    public static MyNetApi login = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/login";
        }
    };

    /**
     * 9、获取未关注的所有用户列表
     */
    public static MyNetApi getUnfollowList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-unfollow-list?myuid=11";
        }
    };

    /**
     * 10、获取未关注的老师列表
     */
    public static MyNetApi getUnfollowTeacherList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-unfollow-teacher-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };


    /**
     * 11、获取未关注的学生列表
     */
    public static MyNetApi getUnfollowStudentList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-unfollow-student-list";
        }
    };

    /**
     * 12、获取关注的老师列表
     */
    public static MyNetApi getFollowTeacherList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-follow-teacher-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }
    };

    /**
     * 13、获取关注的学生列表
     */
    public static MyNetApi getFollowStudentList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-follow-student-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;

        }
    };


    /**
     * 14、获取老师列表
     */
    public static MyNetApi getTeacherListForFollow = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-teacher-list-for-follow";
        }
    };

    /**
     * 15、获取用户关注列表
     */
    public static MyNetApi getFollowList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-follow-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }
    };

    /**
     * 17、添加到关注
     */
    public static MyNetApi addfollow = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/add-follow";
        }
    };


    /**
     * 18、取消关注
     */
    public static MyNetApi delfollow = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/del-follow";
        }

    };
    /**
     * 18、取消关注
     */
    public static MyNetApi addTeacherApply = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/add-teacher-apply";
        }

    };
    public static MyNetApi getTeacherApply = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-teacher-apply";
        }

    };

    /**
     * 根据关键字全局搜索
     */
    public static MyNetApi searchByText = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/search/search-by-text";
        }

    };
    public static MyNetApi searchMusic = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/search/search-music";
        }

    };
    public static MyNetApi searchUser = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/search/search-users";
        }

    };
    public static MyNetApi searchSoundByText = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/search/search-sound-by-text";
        }

    };
    public static MyNetApi searchByMusicName = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/search/search-sound-by-music-name";
        }

    };
    public static MyNetApi getHistory = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/get-history";
        }

    };

    /**
     * 67、获取推荐关注接口
     */
    public static MyNetApi follow_recommend = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/follow-recommend";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    /**
     * 67、热门，返回3条习作和热门问题
     */
    public static MyNetApi remen = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/remen";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    /**
     * 67、
     */
    public static MyNetApi get_follow_recommend_list = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-follow-recommend-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    public static MyNetApi getFollowSoundList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/get-follow-sound-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    public static MyNetApi getSoundList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/get-sound-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    public static MyNetApi getMusicList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/get-music-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    public static MyNetApi getSoundDetail = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/get-sound-detail";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };
    public static MyNetApi getUserPageSoundList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/get-user-page-sound-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };

    public static MyNetApi getUserPage = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/get-user-page";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }

    };

    public static MyNetApi uploadSounds = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/upload-sound";
        }
//        public static String uploadSounds = ServerAddr+"";

    };
    public static MyNetApi addHistory = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/add-history";
        }
//        public static String uploadSounds = ServerAddr+"";

    };
    public static MyNetApi soundReply = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/sound-reply";
        }
//        public static String uploadSounds = ServerAddr+"";

    };
    public static MyNetApi addSound = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/add-sound";
        }
//        public static String uploadSounds = ServerAddr+"";

    };
    public static MyNetApi setPhoto = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/user/set-photo";
        }
//        public static String uploadSounds = ServerAddr+"";

    };
    public static MyNetApi like = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/like";
        }

    };

    public static MyNetApi getNewOrder = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/payment/get-new-order";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }


    };

    public static MyNetApi orderQuery = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/payment/order-query";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }


    };
    public static MyNetApi getNewCoinOrder = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/payment/get-new-coin-order";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }


    };
    public static MyNetApi eavesdrop = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/sound/eavesdrop";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.POST;
        }


    };
    public static MyNetApi checkUpdate = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/site/check-update";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }


    };
    public static MyNetApi getComments = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/comment/getcomments";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }


    };
    public static MyNetApi getBannerList = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/site/get-banner-list";
        }

        @Override
        public NetMethod getNetMethod() {
            return NetMethod.GET;
        }


    };
    public static MyNetApi addComment = new MyNetApi() {
        @Override
        public String getPath() {
            return ServerAddr + "/comment/addcomment";
        }

    };


}
