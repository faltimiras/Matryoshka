package cat.altimiras.matryoshka;

import java.util.HashMap;
import java.util.Map;

public class Matryoshka {

	private final static String SEPARATOR = "/";

	private Map<String, Object> data;

	private Map<String, Object> metadata;

	private Object value;

	public Matryoshka() {
	}

	Matryoshka(Object value) {
		if (value instanceof Map) {
			this.data = (Map) value;
		}
		else {
			this.value = value;
		}
	}

	public Matryoshka(Map<String, Object> data) {
		this.data = data;
	}

	public Object value() {
		return value;
	}

	public Result get(String path) {
		if (path == null) {
			return null;
		}
		return get(path.split(SEPARATOR));
	}

	public Result get(String... parts) {

		if (parts == null || parts.length == 0) {
			return null;
		}

		//support to / or not at the beginning of the path expression
		int start = 0;
		if (parts[0].isEmpty() && parts.length > 1) {
			start = 1;
		}
		return new Result(getRec(data, parts, start));
	}

	private Object getRec(Map<String, Object> p, String[] path, int pos) {

		if (p == null || path == null || path.length == 0) {
			return value;
		}

		if (path.length - 1 == pos) {
			return p.get(path[pos]);
		}
		else {
			if (p.get(path[pos]) instanceof String) {
				return p.get(path[pos]);
			}
			else {
				return getRec((Map) p.get(path[pos]), path, ++pos);
			}
		}
	}

	public Object getMetadata(String key){
		if (metadata != null){
			return metadata.get(key);
		}
		return null;
	}

	public void setMetadata(String key, Object value) {
		if (metadata == null){
			metadata = new HashMap<>(1);
		}
		metadata.put(key, value);
	}

}