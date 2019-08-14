package cat.altimiras.matryoshka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		} else {
			List list = new ArrayList(1);
			list.add(this.content);
			return new MatryoshkaList(list);
		}
	}

	public Object value() {
		return content;
	}

	public Matryoshka asMatryoshka() throws Exception {
		if (this.content == null){
			return null;
		}
		if (this.content instanceof Map) {
			return new Matryoshka((Map) this.content);
		}
		if (this.content instanceof String) {
			return new Matryoshka(this.content);
		}

		throw new Exception("Can not get as Matryoshka. Hint: try with asList()");
	}
}