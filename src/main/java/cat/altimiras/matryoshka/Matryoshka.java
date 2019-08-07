package cat.altimiras.matryoshka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Matryoshka {

	private final static String SEPARATOR = "/";

	private Map<String, Object> data;

	private Map<String, Object> metadata;

	private String value;

	public Matryoshka() {
	}

	Matryoshka(Object content) throws Exception {
		if (content instanceof Map) {
			this.data = (Map) content;
		}
		else if (content instanceof String) {
			this.value = (String) content;
		}
		else {
			throw new RuntimeException("Content can be only a Map or a String");
		}
	}

	public Matryoshka(String value) {
		this.value = value;
	}

	public Matryoshka(Map<String, Object> data) {
		this.data = data;
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

	public class Result {

		final private Object content;

		Result(Object o) {
			this.content = o;
		}

		public MatryoshkaList asList() {

			if (this.content == null) {
				return new MatryoshkaList();
			}

			if (this.content instanceof List) {
				return new MatryoshkaList((List) this.content);
			}
			else {
				List list = new ArrayList(1);
				list.add(this.content);
				return new MatryoshkaList(list);
			}
		}

		public Object value() {
			return content;
		}

		public Matryoshka asMatryoshka() throws Exception {
			if (this.content instanceof Map) {
				return new Matryoshka((Map) this.content);
			}
			if (this.content instanceof String) {
				return new Matryoshka((String) this.content);
			}

			throw new Exception("Can not get as Matryoshka. Hint: try with asList()");
		}
	}

	public class MatryoshkaIterator implements Iterator<Matryoshka> {

		final private Iterator<Map> it;

		MatryoshkaIterator(Iterator<Map> it) {
			this.it = it;
		}

		@Override
		public boolean hasNext() {
			if (it == null) {
				return false;
			}
			return it.hasNext();
		}

		@Override
		public Matryoshka next() {
			if (it == null) {
				return null;
			}
			Map m = it.next();
			return new Matryoshka(m);
		}
	}

	public class EmptyMatryoshkaIterator implements Iterator {

		EmptyMatryoshkaIterator() {
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			throw new NoSuchElementException();
		}
	}

	public class MatryoshkaList {

		final private List rawList;

		MatryoshkaList() {
			this.rawList = null;
		}

		MatryoshkaList(List rawList) {
			this.rawList = rawList;
		}

		public int size() {
			return rawList.size();
		}

		public boolean isEmpty() {
			if (this.rawList == null) {
				return true;
			}
			return rawList.isEmpty();
		}

		public Iterator<Matryoshka> iterator() {
			if (this.rawList == null || rawList.isEmpty()) {
				return new EmptyMatryoshkaIterator();
			}
			return new MatryoshkaIterator(this.rawList.iterator());
		}

		public Matryoshka get(int index) throws Exception {
			return new Matryoshka(this.rawList.get(index));
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
