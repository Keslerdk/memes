package com.example.memes;

import android.util.Log;

import com.vk.api.sdk.requests.VKRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VKUsersRequest extends VKRequest {
    public VKUsersRequest(@NotNull String method) {
        super("users.get");
        addParam("fields", "photo_200");
    }
    String id;
    String firstname;
    String secondName;
    String photo;

    @Override
    public String[] parse(JSONObject r) throws JSONException {
        JSONArray users = r.getJSONArray("response");
        String[] result = VKUser.parse(users.getJSONObject(0));
        this.id=result[0];
        this.firstname=result[1];
        this.secondName=result[2];
        this.photo=result[3];
        Log.d("VKUserRequest!!!", this.secondName);
//        for (int i = 0; i < users.length(); i++) {
//            result.add((VKUser.parse(users.getJSONObject(i))));
//        }

        return result;
    }


}
