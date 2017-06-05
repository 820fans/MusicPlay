package com.mp.music.entity;

public class Constants {

	public static final String SERVER_URL = "http://42.96.160.5/MusicPlay/";

	public static final String GET_MUSIC = SERVER_URL + "getMusic.php";
	public static final String GET_TAGS = SERVER_URL + "getTags.php";

	public static final String GET_CHALLENGE= SERVER_URL + "getChallengeList.php";
	public static final String GET_CHALLENGED= SERVER_URL + "getChallengedList.php";
	public static final String GET_CHALLENGE_DETAIL = SERVER_URL + "getChallengeDetail.php";

	public static final String GET_GUESSED= SERVER_URL + "getGuessed.php";
	public static final String GET_GUESS_DETAIL= SERVER_URL + "getGuessDetail.php";
	public static final String UP_CHALLENGE = SERVER_URL + "upChallenge.php";
	public static final String UP_GUESS = SERVER_URL + "upGuess.php";


	public static final String GET_TALKS = SERVER_URL + "getTalkList.php";
	public static final String GET_COMMENTS = SERVER_URL + "getComments.php";
	public static final String UP_TALK = SERVER_URL + "addTalk.php";
	public static final String UP_COMMENT = SERVER_URL + "addComment.php";

	public static final String GET_USERINFO = SERVER_URL + "getUserInfo.php";

	public static final String IS_USER_ACTIVE=SERVER_URL + "APP_API/index.php/doAuth";
	public static final String DO_LOGIN=SERVER_URL + "APP_API/index.php/login";
	public static final String DO_REGISTER=SERVER_URL + "APP_API/index.php/signUp";
	public static final String DO_LOGOUT=SERVER_URL + "APP_API/index.php/logOut";

	public static final String SP_NAME = "smartweather";
	public static final String KEY_USER_ID = "key_user_id";
	public static final String KEY_USER_NAME = "key_user_name";
	public static final String KEY_USER_STAR = "key_user_star";
	public static final String KEY_LOCATION = "key_user_location";
	public static final String KEY_CITY = "key_city";
	public static final String KEY_LONGITUDE = "key_longitude";
	public static final String KEY_LATITUDE = "key_latitude";

	//音乐播放的模式
	public static final int LOCAL_MODE = 0x301;
	public static final int ONLINE_MODE = 0x302;

	//退出
	public static final String EXIT_ACTION="com.music.EXIT_ACTION";

	//下载进度
	public static final String DOWNBOX_ACTION="com.music.DOWNHELPER_ACTION";
	public static final String DOWNSERVICE_ACTION="com.music.DOWNLOAD_ACTION";
	public static final int DOWN_START=0x401;
	public static final int DOWN_PROCESS=0x402;
	public static final int DOWN_DONE=0x403;
	public static final int DOWN_SIZE=0x404;


	//MusicBox接收器所能响应的Action
	public static final String MUSICBOX_ACTION="com.music.MUSICBOX_ACTION";
	//MusicService接收器所能响应的Action
	public static final String MUSICSERVICE_ACTION="com.music.MUSICSERVICE_ACTION";
	//初始化flag
	public static final int STATE_NON=0x122;
	//播放的flag
	public static final int STATE_PLAY=0x123;

	public static final int ONLINE_STATE_PLAY=0x121;

	//检查后台有无播放音乐
	public static final int STATE_CHECK=0x119;

	public static final int PLAY_PROCESS=0x120;
	//暂停的flag
	public static final int STATE_PAUSE=0x124;
	//停止放的flag
	public static final int STATE_STOP=0x125;
	//播放上一首的flag
	public static final int STATE_PREVIOUS=0x126;
	//播放下一首的flag
	public static final int STATE_NEXT=0x127;
	//菜单关于选项的itemId
	public static final int MENU_ABOUT=0x200;
	//菜单退出选的项的itemId
	public static final int MENU_EXIT=0x201;

}
