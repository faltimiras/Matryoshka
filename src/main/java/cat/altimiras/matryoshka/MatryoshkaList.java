package cat.altimiras.matryoshka;

import java.util.Iterator;
import java.util.List;

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
