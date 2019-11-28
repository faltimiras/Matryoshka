package cat.altimiras.matryoshka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matryoshka {

	private final static String SEPARATOR = "/";
	private final static Pattern KEY_PATTERN = Pattern.compile("^([^\\[]+)(\\[(\\d+)\\])?$");

	private Map<String, Object> data;

	private Map<String, Object> metadata;

	private Object value;

	public Matryoshka() {
	}

	Matryoshka(Object value) {
		if (value instanceof Map) {
			this.data = (Map) value;
		} else {
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

		if (p == null) {
			return null;
		}

		if (path == null || path.length == 0) {
			return value;
		}

		Object value = getValue(p, path[pos]);
		if (path.length - 1 == pos) {
			return value;
		} else {
			return getRec((Map) value, path, ++pos);
		}
	}

	private Object getValue(Map<String, Object> p, String partialPath) {
		Matcher matcher = KEY_PATTERN.matcher(partialPath);
		if (matcher.find()) {
			String key = matcher.group(1);
			String idxStr = matcher.group(3);
			Integer idx = (idxStr != null && !idxStr.isEmpty()) ? Integer.parseInt(idxStr) : null;

			Object value = p.get(key);
			if (idx != null) {
				return getListElem(value, idx);
			} else {
				return value;
			}
		} else {
			return null;
		}
	}

	private Object getListElem(Object obj, int idx) {
		if (obj != null && obj instanceof List && idx < ((List) obj).size()) {
			return ((List) obj).get(idx);
		} else {
			return null;
		}
	}

	public Object getMetadata(String key) {
		if (metadata != null) {
			return metadata.get(key);
		}
		return null;
	}

	public void setMetadata(String key, Object value) {
		if (metadata == null) {
			metadata = new HashMap<>(1);
		}
		metadata.put(key, value);
	}

}