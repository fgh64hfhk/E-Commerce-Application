package com.app.utilts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class JsonParserUtils {

	public static JSONArray readFile(String fileName) {
		
		ClassLoader classLoader = JsonParserUtils.class.getClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(fileName);
				ReadableByteChannel rbc = Channels.newChannel(is)) {
			// 創建 ByteBuffer 區塊 (1024 Byte 大小)
			ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
			rbc.read(buffer);
			buffer.flip(); // limit 改為 position
			Charset cs = Charset.defaultCharset();
//			System.out.println(cs.decode(buffer));
			return new JSONArray(cs.decode(buffer).toString());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
