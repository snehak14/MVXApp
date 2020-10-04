package com.mvxapp.common;

import com.mvxapp.remote.APIClient;
import com.mvxapp.remote.APIInterface;

public class EndPointUtils {

    static final String Base_url = "https://onm9k3etak.execute-api.ap-south-1.amazonaws.com/live/";

    public static APIInterface getUserInfoInterface() {
            return APIClient.getClient(EndPointUtils.Base_url).create(APIInterface.class);
    }
}
