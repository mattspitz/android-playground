import java.util.Arrays;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.test.AndroidTestCase;

import com.example.testhttprequests.HootcasterResponse;
import com.example.testhttprequests.account.CreateAccountHandler.CreateAccountError;

public final class HootcasterResponseTest extends AndroidTestCase {
	
	private static final String TAG = "HootcasterResponseTest";
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public void testCreateAccountMapping() {
		for (String testStr : Arrays.asList("{\"okay\": true}", "{\"okay\": false, \"errors\": [\"username_exists\", \"phone_exists\"]}")) {
			HootcasterResponse<Void, CreateAccountError> eep;
			try {
				eep = objectMapper.readValue(testStr, 
						new TypeReference<HootcasterResponse<Void, CreateAccountError>>() {});
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
