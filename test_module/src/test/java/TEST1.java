import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Gochin on 2021/3/25.
 */
public class TEST1 {

    public static void main(String[] args) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON("test");
        System.out.println(jsonObject);
    }
}
