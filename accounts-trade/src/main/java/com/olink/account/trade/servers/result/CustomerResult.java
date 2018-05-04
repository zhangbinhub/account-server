package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/9/5.
 * C户接口处理结果,body
 */
public class CustomerResult extends BaseServerResult {

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONArray getDatas() {
        return datas;
    }

    public void setDatas(JSONArray datas) {
        this.datas = datas;
    }

    private JSONObject data;

    private JSONArray datas;

}
