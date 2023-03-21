package blockchainproject_v1;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    
    public static String zeros(int length) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            builder.append('0');
        }
        
        return builder.toString();
        
    }
    
    public static void toJsonFile(BlockChain b)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("", b.latestBlock());
        try (FileWriter file = new FileWriter("C:\\Users\\gary5\\college\\Manhattan\\Spring 2023\\CMPT 368 (BlockChain)\\BlockChainTest\\blockchain.json", true)) {
        file.write(jsonObject.toString());
        file.flush();
        file.write("\n");
        file.write("\n");
        file.write("\n");
        file.write("\n");
        file.close();
        }
        catch (IOException e) {
        e.printStackTrace();
        }
    }
    
}
