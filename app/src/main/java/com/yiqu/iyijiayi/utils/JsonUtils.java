package com.yiqu.iyijiayi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.Xizuo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	public static ArrayList<Xizuo> parseXizuoList(String data) throws JSONException {
		ArrayList<Xizuo> list = new ArrayList<Xizuo>();
		JSONArray js = new JSONArray(data);
		for (int i = 0; i < js.length(); i++) {
			JSONObject j = (JSONObject) js.get(i);
			Gson gson = new Gson();
			Xizuo f = gson.fromJson(j.toString(), Xizuo.class);
			list.add(f);
		}
		return list;

	}

	public static ArrayList<Sound> parseSoundList(String data) throws JSONException {
		ArrayList<Sound> list = new ArrayList<Sound>();
		JSONArray js = new JSONArray(data);
		for (int i = 0; i < js.length(); i++) {
			JSONObject j = (JSONObject) js.get(i);
			Gson gson = new Gson();
			Sound f = gson.fromJson(j.toString(), Sound.class);
			list.add(f);
		}
		return list;

	}

	public static ArrayList<Teacher> parseTeacherList(String data) throws JSONException {
		ArrayList<Teacher> list = new ArrayList<Teacher>();
		JSONArray js = new JSONArray(data);
		for (int i = 0; i < js.length(); i++) {
			JSONObject j = (JSONObject) js.get(i);
			Gson gson = new Gson();
			Teacher f = gson.fromJson(j.toString(), Teacher.class);

			list.add(f);
		}
		return list;

	}


	public static ArrayList<Discovery> parseDiscoveryList(String data) throws JSONException {
		ArrayList<Discovery> list = new ArrayList<Discovery>();
		JSONArray js = new JSONArray(data);
		for (int i = 0; i < js.length(); i++) {
			JSONObject j = (JSONObject) js.get(i);
			Gson gson = new Gson();
			Discovery f = gson.fromJson(j.toString(), Discovery.class);

			list.add(f);
		}
		return list;

	}

	public static ArrayList<Music> parseMusicList(String data) throws JSONException {
		ArrayList<Music> list = new ArrayList<Music>();
		JSONArray js = new JSONArray(data);
		for (int i = 0; i < js.length(); i++) {
			JSONObject j = (JSONObject) js.get(i);
			Gson gson = new Gson();
			Music f = gson.fromJson(j.toString(), Music.class);
			list.add(f);
		}
		return list;

	}
}
